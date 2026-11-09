package com.gathin.tvhome;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * V05 Wi-Fi information surface. Android remains the owner of radio, authentication,
 * connect/disconnect and saved-network operations; this dialog only reports scan/status data.
 */
final class WifiSettingsDialog {
    private static final int REQUEST_FINE_LOCATION = 7314;
    private static final long SCAN_TIMEOUT_MS = 12_000L;
    private static WeakReference<WifiSettingsDialog> active = new WeakReference<>(null);
    private static WifiSettingsDialog pendingReturn;

    private final Activity activity;
    private final Runnable openSystemWifiSettings;
    private final Runnable returnToParent;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final TextView status;
    private final TextView wifiToggle;
    private String connectedSsid;
    private final LinearLayout networkRows;
    private final TextView scanState;
    private final TextView rescan;
    private final TextView permissionAction;
    private final TextView locationAction;
    private final TextView androidSettings;
    private final TextView close;
    private final View pointer;
    private final AlertDialog dialog;
    private final List<View> focusOrder = new ArrayList<>();
    private boolean receiverRegistered;
    private boolean scanPending;
    private Runnable pendingAfterDismiss;
    private boolean parentReturned;
    private boolean deferParentReturn;

    private final Runnable scanTimeout = this::onScanTimeout;

    private void onScanTimeout() {
        if (scanPending) {
            scanPending = false;
            scanState.setText(R.string.wifi_scan_timeout);
            showCachedResults();
        }
    }

    private final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            if (!WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) return;
            handler.removeCallbacks(scanTimeout);
            scanPending = false;
            boolean updated = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (updated) {
                scanState.setText(R.string.wifi_scan_updated);
            } else {
                scanState.setText(R.string.wifi_scan_limited);
            }
            showCachedResults();
        }
    };

    private WifiSettingsDialog(Activity activity, Runnable openSystemWifiSettings, Runnable returnToParent) {
        this.activity = activity;
        this.openSystemWifiSettings = openSystemWifiSettings;
        this.returnToParent = returnToParent;

        float density = activity.getResources().getDisplayMetrics().density;
        ScrollView scroll = new ScrollView(activity);
        scroll.setFillViewport(true);
        scroll.setVerticalScrollBarEnabled(false);
        scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        scroll.setClipToPadding(false);

        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(density, 12), 0, dp(density, 12), dp(density, 8));
        pointer = new View(activity) {
            private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            @Override protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                float w = getWidth(), h = getHeight();
                float center = Math.max(h, Math.min(w - h, getTag() instanceof Integer ? (Integer) getTag() : w * 0.72f));
                Path triangle = new Path();
                triangle.moveTo(center - h, h);
                triangle.lineTo(center, 0);
                triangle.lineTo(center + h, h);
                triangle.close();
                paint.setColor(TvUiTokens.POPUP_SURFACE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(triangle, paint);
                paint.setColor(TvUiTokens.FOCUS_BORDER);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(Math.max(1f, density));
                Path pointerEdges = new Path();
                pointerEdges.moveTo(center - h, h);
                pointerEdges.lineTo(center, 0);
                pointerEdges.lineTo(center + h, h);
                canvas.drawPath(pointerEdges, paint);
            }
        };
        content.addView(pointer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 12)));
        scroll.addView(content, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView title = text(R.string.wifi, 18f, true, density);
        title.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_wifi, 0, 0, 0);
        title.setCompoundDrawablePadding(dp(density, 6));
        LinearLayout header = new LinearLayout(activity);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        wifiToggle = action("ON", "wifi_toggle", density, () -> dismissThen(openSystemWifiSettings));
        wifiToggle.setMinHeight(dp(density, 28));
        header.addView(wifiToggle, new LinearLayout.LayoutParams(dp(density, 48), ViewGroup.LayoutParams.WRAP_CONTENT));
        content.addView(header);
        status = text(R.string.wifi_status_loading, TvUiTokens.BODY_SP, true, density);
        status.setPadding(dp(density, 10), dp(density, 5), dp(density, 10), dp(density, 5));
        GradientDrawable connectedSurface = new GradientDrawable();
        connectedSurface.setColor(TvUiTokens.SURFACE_IDLE);
        connectedSurface.setCornerRadius(dp(density, (int) TvUiTokens.CARD_RADIUS_DP));
        connectedSurface.setStroke(dp(density, 1), TvUiTokens.BORDER);
        status.setBackground(connectedSurface);
        status.setVisibility(View.GONE);

        TextView networksHeading = text(R.string.wifi_available_networks, popupTextSizeSp(), true, density);
        networksHeading.setGravity(Gravity.CENTER);
        content.addView(networksHeading);
        scanState = text(R.string.wifi_scan_idle, 12f, false, density);
        scanState.setGravity(Gravity.CENTER);
        scanState.setPadding(0, dp(density, 4), 0, dp(density, 6));
        content.addView(scanState);

        permissionAction = action(R.string.wifi_grant_location_permission, "wifi_permission", density,
                this::requestLocationPermission);
        locationAction = action(R.string.wifi_enable_location, "wifi_location_settings", density,
                () -> dismissThen(() -> launch(Settings.ACTION_LOCATION_SOURCE_SETTINGS)));
        networkRows = new LinearLayout(activity);
        networkRows.setOrientation(LinearLayout.VERTICAL);
        content.addView(permissionAction);
        content.addView(locationAction);
        ScrollView networkScroll = new ScrollView(activity);
        networkScroll.setTag("wifi_network_list");
        networkScroll.setFillViewport(false);
        networkScroll.setVerticalScrollBarEnabled(true);
        networkScroll.setScrollbarFadingEnabled(false);
        networkScroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        networkScroll.addView(networkRows, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        content.addView(networkScroll, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 120)));

        rescan = action(R.string.wifi_rescan, "wifi_rescan", density, this::requestScan);
        rescan.setText("↻  " + activity.getString(R.string.wifi_rescan));
        androidSettings = action(R.string.wifi_open_android_settings, "wifi_open_settings", density,
                () -> dismissThen(openSystemWifiSettings));
        androidSettings.setGravity(Gravity.CENTER);
        rescan.setGravity(Gravity.CENTER);
        rescan.setTextSize(popupTextSizeSp());
        androidSettings.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_settings, 0, 0, 0);
        androidSettings.setCompoundDrawablePadding(dp(density, 6));
        close = action(R.string.close_label, "wifi_close", density, () -> dismissThen(null));
        LinearLayout actions = new LinearLayout(activity);
        actions.setOrientation(LinearLayout.VERTICAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams actionWidth = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rescan.setLayoutParams(actionWidth);
        LinearLayout.LayoutParams settingsWidth = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        androidSettings.setLayoutParams(settingsWidth);
        actions.addView(rescan);
        actions.addView(androidSettings);
        content.addView(actions);
        content.addView(close);
        close.setVisibility(View.GONE);

        AlertDialog built = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setView(scroll)
                .create();
        dialog = built;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(d -> notifyReturnToParent());
        dialog.setOnDismissListener(d -> {
            handler.removeCallbacksAndMessages(null);
            unregisterScanReceiver();
            if (active.get() == this) active.clear();
            if (deferParentReturn) pendingReturn = this;
            else notifyReturnToParent();
            Runnable action = pendingAfterDismiss;
            pendingAfterDismiss = null;
            if (action != null && !activity.isFinishing() && !activity.isDestroyed()) action.run();
        });
        dialog.setOnShowListener(d -> {
            applyAnchoredPopup();
            refreshStatus();
            updatePrerequisites();
            registerScanReceiver();
            requestScan();
            showCachedResults();
            connectFocusOrder();
            View initial = firstFocusableControl();
            if (initial != null) initial.requestFocus();
        });
    }

    private void applyAnchoredPopup() {
        Window window = dialog.getWindow();
        if (window == null) return;
        View anchor = activity.findViewById(R.id.btnWifi);
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int popupWidth = Math.round(screenWidth * popupWidthFraction());
        int popupHeight = Math.round(screenHeight * popupHeightFraction());
        int popupLeft = Math.max(8, screenWidth - popupWidth - 8);
        int popupTop = 8;
        int contentInset = dp(activity.getResources().getDisplayMetrics().density, 12);
        int pointerWidth = popupWidth - contentInset * 2;
        int pointerCenter = Math.round(pointerWidth * 0.72f);
        if (anchor != null && anchor.getWidth() > 0 && anchor.getHeight() > 0) {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int anchorCenter = location[0] + anchor.getWidth() / 2;
            int popupRight = Math.min(screenWidth - 8, anchorCenter + popupWidth / 2);
            popupLeft = Math.max(8, popupRight - popupWidth);
            popupTop = Math.min(screenHeight - popupHeight - 8, location[1] + anchor.getHeight() + 14);
            pointerCenter = Math.max(16, Math.min(pointerWidth - 16,
                    anchorCenter - popupLeft - contentInset));
        }
        pointer.setTag(pointerCenter);
        pointer.invalidate();
        window.setBackgroundDrawable(TvDialogStyle.createDialogSurface(activity));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setDimAmount(popupDimAmount());
        window.setLayout(popupWidth, popupHeight);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = popupWidth;
        params.height = popupHeight;
        params.gravity = anchoredWindowGravity();
        params.x = Math.max(0, screenWidth - popupLeft - popupWidth);
        params.y = Math.max(0, popupTop);
        params.dimAmount = popupDimAmount();
        window.setAttributes(params);
    }

    static float popupWidthFraction() {
        return 0.1976f; // 4% wider than the existing 19% footprint.
    }

    static float popupHeightFraction() {
        return 0.43f; // Preserve the compact width; allocate more vertical room.
    }

    static float popupTextSizeSp() {
        return 14f;
    }

    static int anchoredWindowGravity() {
        return Gravity.TOP | Gravity.RIGHT;
    }

    static float popupDimAmount() {
        return 0.08f;
    }

    static AlertDialog show(Activity activity, Runnable openSystemWifiSettings, Runnable returnToParent) {
        if (activity == null || openSystemWifiSettings == null
                || activity.isFinishing() || activity.isDestroyed()) return null;
        WifiSettingsDialog screen = new WifiSettingsDialog(activity, openSystemWifiSettings, returnToParent);
        active = new WeakReference<>(screen);
        screen.dialog.show();
        return screen.dialog;
    }

    static void onHostResumed() {
        WifiSettingsDialog waiting = pendingReturn;
        if (waiting != null) {
            pendingReturn = null;
            waiting.notifyReturnToParent();
            return;
        }
        WifiSettingsDialog screen = active.get();
        if (screen != null && screen.dialog.isShowing()) {
            screen.refreshStatus();
            screen.updatePrerequisites();
            screen.requestScan();
            screen.showCachedResults();
        }
    }

    static void onNetworkStateChanged() {
        WifiSettingsDialog screen = active.get();
        if (screen != null && screen.dialog.isShowing()) {
            screen.refreshStatus();
            screen.updatePrerequisites();
            screen.showCachedResults();
        }
    }

    static void onExternalSettingsUnavailable() {
        WifiSettingsDialog waiting = pendingReturn;
        if (waiting != null) {
            pendingReturn = null;
            waiting.notifyReturnToParent();
        }
    }

    static void onHostDestroyed() {
        WifiSettingsDialog screen = active.get();
        if (screen != null && screen.dialog.isShowing()) screen.dialog.dismiss();
        if (pendingReturn != null) {
            pendingReturn.parentReturned = true;
            pendingReturn = null;
        }
    }

    static void onPermissionResult(int requestCode) {
        WifiSettingsDialog screen = active.get();
        if (requestCode == REQUEST_FINE_LOCATION && screen != null
                && screen.dialog.isShowing()) {
            screen.refreshStatus();
            screen.updatePrerequisites();
            screen.requestScan();
            screen.showCachedResults();
        }
    }

    private void registerScanReceiver() {
        if (receiverRegistered) return;
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity.registerReceiver(scanReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                activity.registerReceiver(scanReceiver, filter);
            }
            receiverRegistered = true;
        } catch (RuntimeException ignored) {
            scanState.setText(R.string.wifi_scan_unavailable);
        }
    }

    private void unregisterScanReceiver() {
        if (!receiverRegistered) return;
        try {
            activity.unregisterReceiver(scanReceiver);
        } catch (IllegalArgumentException ignored) {
            // Activity teardown may have already removed the receiver.
        }
        receiverRegistered = false;
    }

    private boolean hasLocationPermission() {
        return activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        try {
            LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (manager == null) return false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) return manager.isLocationEnabled();
            return Settings.Secure.getInt(activity.getContentResolver(),
                    Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
                    != Settings.Secure.LOCATION_MODE_OFF;
        } catch (RuntimeException unavailable) {
            return false;
        }
    }

    private WifiManager wifiManager() {
        return (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private boolean wifiEnabled() {
        try {
            WifiManager manager = wifiManager();
            return manager != null && manager.isWifiEnabled();
        } catch (RuntimeException unavailable) {
            return false;
        }
    }

    private void updatePrerequisites() {
        WifiScanPolicy.BlockReason reason = WifiScanPolicy.blockReason(
                hasLocationPermission(), isLocationEnabled(), wifiEnabled());
        permissionAction.setVisibility(reason == WifiScanPolicy.BlockReason.PERMISSION ? View.VISIBLE : View.GONE);
        locationAction.setVisibility(reason == WifiScanPolicy.BlockReason.LOCATION ? View.VISIBLE : View.GONE);
        rescan.setVisibility(reason == WifiScanPolicy.BlockReason.NONE ? View.VISIBLE : View.GONE);
        if (reason == WifiScanPolicy.BlockReason.PERMISSION) scanState.setText(R.string.wifi_scan_permission_required);
        else if (reason == WifiScanPolicy.BlockReason.LOCATION) scanState.setText(R.string.wifi_scan_location_required);
        else if (reason == WifiScanPolicy.BlockReason.WIFI_DISABLED) scanState.setText(R.string.wifi_scan_radio_off);
    }

    private void requestLocationPermission() {
        if (activity.isFinishing() || activity.isDestroyed()) return;
        activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    private void requestScan() {
        if (!dialog.isShowing()) return;
        updatePrerequisites();
        if (!WifiScanPolicy.mayRequestScan(hasLocationPermission(), isLocationEnabled(), wifiEnabled())) {
            showCachedResults();
            return;
        }
        WifiManager manager = wifiManager();
        if (manager == null) {
            scanState.setText(R.string.wifi_scan_unavailable);
            return;
        }
        try {
            boolean started = manager.startScan();
            if (started) {
                scanPending = true;
                scanState.setText(R.string.wifi_scan_loading);
                handler.removeCallbacks(scanTimeout);
                handler.postDelayed(scanTimeout, SCAN_TIMEOUT_MS);
            } else {
                scanState.setText(R.string.wifi_scan_limited);
                showCachedResults();
            }
        } catch (SecurityException denied) {
            scanState.setText(R.string.wifi_scan_permission_required);
            updatePrerequisites();
        } catch (RuntimeException unavailable) {
            scanState.setText(R.string.wifi_scan_unavailable);
        }
    }

    private void refreshStatus() {
        boolean wifi = false;
        WifiInfo connectionInfo = null;
        try {
            ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                for (Network network : manager.getAllNetworks()) {
                    NetworkCapabilities caps = manager.getNetworkCapabilities(network);
                    if (caps != null && caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        wifi = true;
                        if (hasLocationPermission() && isLocationEnabled()
                                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                                && caps.getTransportInfo() instanceof WifiInfo) {
                            connectionInfo = (WifiInfo) caps.getTransportInfo();
                        }
                        break;
                    }
                }
            }
        } catch (RuntimeException ignored) { }

        if (!wifi) {
            connectedSsid = null;
            status.setText(wifiEnabled() ? R.string.wifi_status_not_connected : R.string.wifi_status_off);
            wifiToggle.setText(wifiEnabled() ? "ON" : "OFF");
            return;
        }

        String ssid = null;
        try {
            if (hasLocationPermission() && isLocationEnabled()) {
                if (connectionInfo == null && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    WifiManager manager = wifiManager();
                    connectionInfo = manager != null ? manager.getConnectionInfo() : null;
                }
                if (connectionInfo != null) ssid = cleanSsid(connectionInfo.getSSID());
            }
        } catch (RuntimeException ignored) { }
        connectedSsid = ssid;
        status.setText(ssid == null
                ? activity.getString(R.string.wifi_status_connected)
                : activity.getString(R.string.wifi_status_connected_to, ssid));
        wifiToggle.setText(wifiEnabled() ? "ON" : "OFF");
    }

    private void showCachedResults() {
        if (!dialog.isShowing()) return;
        String focusedSsid = null;
        View focused = activity.getCurrentFocus();
        if (focused != null && focused.getParent() == networkRows
                && focused.getTag() instanceof String) {
            String tag = (String) focused.getTag();
            if (tag.startsWith("wifi_network:")) focusedSsid = tag.substring("wifi_network:".length());
        }
        networkRows.removeAllViews();
        refreshStatus();
        List<ScanResult> results = Collections.emptyList();
        boolean canReadResults = WifiScanPolicy.mayRequestScan(
                hasLocationPermission(), isLocationEnabled(), wifiEnabled());
        if (canReadResults) {
            try {
                WifiManager manager = wifiManager();
                if (manager != null) results = manager.getScanResults();
            } catch (SecurityException denied) {
                scanState.setText(R.string.wifi_scan_permission_required);
            } catch (RuntimeException unavailable) {
                scanState.setText(R.string.wifi_scan_unavailable);
            }
        }
        Map<String, ScanResult> strongest = new LinkedHashMap<>();
        for (ScanResult result : results) {
            String ssid = result != null ? cleanSsid(result.SSID) : null;
            if (ssid == null) continue;
            ScanResult previous = strongest.get(ssid);
            if (previous == null || result.level > previous.level) strongest.put(ssid, result);
        }
        List<ScanResult> sorted = new ArrayList<>(strongest.values());
        sorted.sort(Comparator.comparingInt((ScanResult item) -> item.level).reversed());
        boolean connectedAdded = false;
        if (connectedSsid != null) {
            ScanResult connectedResult = strongest.get(connectedSsid);
            String connectedSecurity = connectedResult == null ? ""
                    : securityLabel(connectedResult.capabilities);
            String connectedLabel = connectedSsid + "\n"
                    + activity.getString(R.string.wifi_status_connected);
            TextView connectedRow = action(connectedLabel, "wifi_network:" + connectedSsid,
                    activity.getResources().getDisplayMetrics().density,
                    () -> dismissThen(openSystemWifiSettings));
            SpannableString connectedText = new SpannableString(connectedLabel);
            int statusStart = connectedSsid.length() + 1;
            connectedText.setSpan(new RelativeSizeSpan(0.78f), statusStart,
                    connectedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            connectedRow.setText(connectedText);
            connectedRow.setMaxLines(2);
            connectedRow.setTextSize(popupTextSizeSp());
            connectedRow.setGravity(Gravity.CENTER);
            connectedRow.setLineSpacing(dp(activity.getResources().getDisplayMetrics().density, 1), 1f);
            connectedRow.setEllipsize(android.text.TextUtils.TruncateAt.END);
            connectedRow.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_wifi, 0,
                    (connectedSecurity.isEmpty()
                            || connectedSecurity.equals(activity.getString(R.string.wifi_security_open)))
                            ? 0 : R.drawable.ic_lock, 0);
            connectedRow.setCompoundDrawablePadding(dp(
                    activity.getResources().getDisplayMetrics().density, 6));
            networkRows.addView(connectedRow);
            connectedAdded = true;
        }
        if (sorted.isEmpty() && !scanPending && canReadResults && !connectedAdded) {
            TextView empty = text(R.string.wifi_scan_empty, TvUiTokens.CARD_SUBTITLE_SP, false,
                    activity.getResources().getDisplayMetrics().density);
            networkRows.addView(empty);
        } else if (!sorted.isEmpty()) {
            int added = connectedAdded ? 1 : 0;
            for (int i = 0; i < sorted.size() && added < 12; i++) {
                ScanResult result = sorted.get(i);
                if (connectedSsid != null && connectedSsid.equals(cleanSsid(result.SSID))) continue;
                String security = securityLabel(result.capabilities);
                String label = result.SSID;
                TextView row = action(label, "wifi_network:" + result.SSID,
                        activity.getResources().getDisplayMetrics().density,
                        () -> dismissThen(openSystemWifiSettings));
                row.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_wifi, 0,
                        security.equals(activity.getString(R.string.wifi_security_open))
                                ? 0 : R.drawable.ic_lock, 0);
                row.setGravity(Gravity.CENTER);
                row.setCompoundDrawablePadding(dp(
                        activity.getResources().getDisplayMetrics().density, 6));
                networkRows.addView(row);
                focusOrder.add(row);
                added++;
            }
        }
        connectFocusOrder();
        if (focusedSsid != null) {
            boolean restored = false;
            for (int i = 0; i < networkRows.getChildCount(); i++) {
                View child = networkRows.getChildAt(i);
                if (("wifi_network:" + focusedSsid).equals(child.getTag())) {
                    child.requestFocus();
                    restored = true;
                    break;
                }
            }
            if (!restored) {
                View fallback = firstFocusableControl();
                if (fallback != null) fallback.requestFocus();
            }
        } else if (focused == null || !focused.isShown() || !focused.isFocusable()) {
            View fallback = firstFocusableControl();
            if (fallback != null) fallback.requestFocus();
        }
    }

    private String securityLabel(String capabilities) {
        String value = capabilities == null ? "" : capabilities.toUpperCase(Locale.ROOT);
        if (value.contains("SAE")) return activity.getString(R.string.wifi_security_wpa3);
        if (value.contains("WEP")) return activity.getString(R.string.wifi_security_wep);
        if (value.contains("PSK") || value.contains("EAP")) return activity.getString(R.string.wifi_security_secured);
        return activity.getString(R.string.wifi_security_open);
    }

    private String signalLabel(int rssi) {
        int bars;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WifiManager manager = wifiManager();
            int maxLevel = manager != null ? manager.getMaxSignalLevel() : 4;
            int level = manager != null ? manager.calculateSignalLevel(rssi) : 0;
            bars = maxLevel <= 0 ? 1
                    : Math.max(1, Math.min(4, (int) Math.ceil((level + 1) * 4.0 / (maxLevel + 1))));
        } else {
            bars = WifiManager.calculateSignalLevel(rssi, 4) + 1;
        }
        return activity.getString(R.string.wifi_signal_bars, bars);
    }

    private String cleanSsid(String ssid) {
        if (ssid == null) return null;
        String value = ssid.replace("\"", "").trim();
        if (value.isEmpty() || value.equalsIgnoreCase("<unknown ssid>")
                || value.equalsIgnoreCase("unknown ssid") || value.equals("0x")) return null;
        return value;
    }

    private TextView text(int resource, float size, boolean bold, float density) {
        TextView view = new TextView(activity);
        view.setText(resource);
        styleText(view, size, bold, density);
        return view;
    }

    private TextView action(int resource, String tag, float density, Runnable callback) {
        TextView view = text(resource, TvUiTokens.CARD_TITLE_SP, true, density);
        return prepareAction(view, tag, density, callback);
    }

    private TextView action(String label, String tag, float density, Runnable callback) {
        TextView view = new TextView(activity);
        view.setText(label);
        styleText(view, popupTextSizeSp(), true, density);
        view.setMaxLines(1);
        view.setEllipsize(android.text.TextUtils.TruncateAt.END);
        return prepareAction(view, tag, density, callback);
    }

    private TextView prepareAction(TextView view, String tag, float density, Runnable callback) {
        if (view.getId() == View.NO_ID) view.setId(View.generateViewId());
        view.setTag(tag);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        if (view.getGravity() == 0) view.setGravity(Gravity.CENTER_VERTICAL);
        view.setMinHeight(Math.max(38, Math.round(density * 26)));
        view.setPadding(dp(density, 10), dp(density, 3), dp(density, 10), dp(density, 3));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(density, 2);
        view.setLayoutParams(lp);
        TvFocusIndicator.applyRoundedTextFocus(view, density, TvUiTokens.ROW_RADIUS_DP,
                TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_FOCUSED,
                TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER,
                TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
        view.setOnClickListener(v -> {
            if (callback != null) callback.run();
        });
        return view;
    }

    private void styleText(TextView view, float size, boolean bold, float density) {
        view.setTextSize(size);
        view.setTextColor(TvUiTokens.TEXT_PRIMARY);
        view.setTypeface(activity.getResources().getFont(R.font.inter),
                bold ? Typeface.BOLD : Typeface.NORMAL);
        view.setPadding(0, dp(density, 3), 0, dp(density, 3));
    }

    private View firstFocusableControl() {
        if (wifiToggle.isFocusable() && wifiToggle.getVisibility() == View.VISIBLE) return wifiToggle;
        if (permissionAction.getVisibility() == View.VISIBLE) return permissionAction;
        if (locationAction.getVisibility() == View.VISIBLE) return locationAction;
        for (int i = 0; i < networkRows.getChildCount(); i++) {
            View child = networkRows.getChildAt(i);
            if (child.isFocusable() && child.getVisibility() == View.VISIBLE) return child;
        }
        return rescan.getVisibility() == View.VISIBLE ? rescan : androidSettings;
    }

    private void connectFocusOrder() {
        focusOrder.clear();
        focusOrder.add(wifiToggle);
        if (permissionAction.getVisibility() == View.VISIBLE) focusOrder.add(permissionAction);
        if (locationAction.getVisibility() == View.VISIBLE) focusOrder.add(locationAction);
        for (int i = 0; i < networkRows.getChildCount(); i++) {
            View child = networkRows.getChildAt(i);
            if (child.isFocusable() && child.getVisibility() == View.VISIBLE) focusOrder.add(child);
        }
        if (rescan.getVisibility() == View.VISIBLE) focusOrder.add(rescan);
        focusOrder.add(androidSettings);
        for (int i = 0; i < focusOrder.size(); i++) {
            View view = focusOrder.get(i);
            int previous = i > 0 ? focusOrder.get(i - 1).getId() : view.getId();
            int next = i + 1 < focusOrder.size() ? focusOrder.get(i + 1).getId() : view.getId();
            view.setNextFocusUpId(previous);
            view.setNextFocusDownId(next);
            view.setNextFocusLeftId(view.getId());
            view.setNextFocusRightId(view.getId());
        }
        rescan.setNextFocusRightId(androidSettings.getId());
        androidSettings.setNextFocusLeftId(rescan.getId());
        androidSettings.setNextFocusRightId(androidSettings.getId());
        if (rescan.getVisibility() == View.VISIBLE) {
            androidSettings.setNextFocusUpId(rescan.getId());
            androidSettings.setNextFocusDownId(androidSettings.getId());
        }
    }

    private void notifyReturnToParent() {
        if (parentReturned) return;
        parentReturned = true;
        if (returnToParent != null && !activity.isFinishing() && !activity.isDestroyed()) {
            returnToParent.run();
        }
    }

    private void dismissThen(Runnable action) {
        deferParentReturn = action != null;
        pendingAfterDismiss = action;
        dialog.dismiss();
    }

    private void launch(String action) {
        try {
            activity.startActivity(new Intent(action));
        } catch (RuntimeException unavailable) {
            // A firmware may not expose a standalone Location settings activity.
            if (pendingReturn == this) pendingReturn = null;
            notifyReturnToParent();
        }
    }

    private static int dp(float density, int value) {
        return Math.max(1, Math.round(density * value));
    }
}

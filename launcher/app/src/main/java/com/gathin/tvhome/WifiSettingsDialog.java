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
import android.graphics.drawable.GradientDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
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
    private final LinearLayout networkRows;
    private final TextView scanState;
    private final TextView rescan;
    private final TextView permissionAction;
    private final TextView locationAction;
    private final TextView androidSettings;
    private final TextView close;
    private final AlertDialog dialog;
    private final List<View> focusOrder = new ArrayList<>();
    private boolean receiverRegistered;
    private boolean networkCallbackRegistered;
    private boolean scanPending;

    private final ConnectivityManager.NetworkCallback wifiNetworkCallback =
            new ConnectivityManager.NetworkCallback() {
        @Override public void onAvailable(Network network) { postNetworkRefresh(); }
        @Override public void onLost(Network network) { postNetworkRefresh(); }
        @Override public void onCapabilitiesChanged(Network network, NetworkCapabilities capabilities) {
            postNetworkRefresh();
        }
    };

    private void postNetworkRefresh() {
        handler.post(() -> {
            if (!dialog.isShowing() || activity.isFinishing() || activity.isDestroyed()) return;
            updatePrerequisites();
            showCachedResults();
        });
    }
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
        content.setPadding(dp(density, 24), dp(density, 18), dp(density, 24), dp(density, 18));
        scroll.addView(content, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView title = text(R.string.wifi, TvUiTokens.PAGE_HEADER_SP, true, density);
        content.addView(title);
        status = text(R.string.wifi_status_loading, TvUiTokens.BODY_SP, true, density);
        status.setPadding(dp(density, 16), dp(density, 12), dp(density, 16), dp(density, 12));
        GradientDrawable connectedSurface = new GradientDrawable();
        connectedSurface.setColor(TvUiTokens.SURFACE_IDLE);
        connectedSurface.setCornerRadius(dp(density, TvUiTokens.CARD_RADIUS_DP));
        connectedSurface.setStroke(dp(density, 1), TvUiTokens.BORDER);
        status.setBackground(connectedSurface);
        content.addView(status);

        TextView networksHeading = text(R.string.wifi_available_networks, TvUiTokens.SECTION_TITLE_SP, true, density);
        content.addView(networksHeading);
        scanState = text(R.string.wifi_scan_idle, TvUiTokens.CARD_SUBTITLE_SP, false, density);
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
        content.addView(networkRows, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        rescan = action(R.string.wifi_rescan, "wifi_rescan", density, this::requestScan);
        androidSettings = action(R.string.wifi_open_android_settings, "wifi_open_settings", density,
                () -> dismissThen(openSystemWifiSettings));
        close = action(R.string.close_label, "wifi_close", density, () -> dismissThen(null));
        LinearLayout actions = new LinearLayout(activity);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams actionWidth = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        actionWidth.rightMargin = dp(density, 6);
        rescan.setLayoutParams(actionWidth);
        LinearLayout.LayoutParams settingsWidth = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        settingsWidth.leftMargin = dp(density, 6);
        androidSettings.setLayoutParams(settingsWidth);
        actions.addView(rescan);
        actions.addView(androidSettings);
        content.addView(actions);
        content.addView(close);

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
            unregisterNetworkCallback();
            if (active.get() == this) active.clear();
            if (deferParentReturn) pendingReturn = this;
            else notifyReturnToParent();
            Runnable action = pendingAfterDismiss;
            pendingAfterDismiss = null;
            if (action != null && !activity.isFinishing() && !activity.isDestroyed()) action.run();
        });
        dialog.setOnShowListener(d -> {
            TvDialogStyle.apply(dialog, activity, 760, 620);
            refreshStatus();
            updatePrerequisites();
            showCachedResults();
            connectFocusOrder();
            View initial = firstFocusableControl();
            if (initial != null) initial.requestFocus();
            registerScanReceiver();
            registerNetworkCallback();
            requestScan();
        });
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
            screen.showCachedResults();
            screen.requestScan();
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

    private void registerNetworkCallback() {
        if (networkCallbackRegistered) return;
        try {
            ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return;
            NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();
            manager.registerNetworkCallback(request, wifiNetworkCallback);
            networkCallbackRegistered = true;
        } catch (RuntimeException unavailable) {
            // Status still refreshes on resume and after scan results.
        }
    }

    private void unregisterNetworkCallback() {
        if (!networkCallbackRegistered) return;
        try {
            ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) manager.unregisterNetworkCallback(wifiNetworkCallback);
        } catch (RuntimeException ignored) {
            // The system may already have detached callbacks during Activity teardown.
        }
        networkCallbackRegistered = false;
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
            status.setText(wifiEnabled() ? R.string.wifi_status_not_connected : R.string.wifi_status_off);
            return;
        }

        String ssid = null;
        try {
            if (hasLocationPermission() && isLocationEnabled()) {
                if (connectionInfo == null) {
                    WifiManager manager = wifiManager();
                    connectionInfo = manager != null ? manager.getConnectionInfo() : null;
                }
                if (connectionInfo != null) ssid = cleanSsid(connectionInfo.getSSID());
            }
        } catch (RuntimeException ignored) { }
        status.setText(ssid == null
                ? activity.getString(R.string.wifi_status_connected)
                : activity.getString(R.string.wifi_status_connected_to, ssid));
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
        List<ScanResult> results = Collections.emptyList();
        if (hasLocationPermission() && isLocationEnabled() && wifiEnabled()) {
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
        if (sorted.isEmpty()) {
            TextView empty = text(R.string.wifi_scan_empty, TvUiTokens.CARD_SUBTITLE_SP, false,
                    activity.getResources().getDisplayMetrics().density);
            networkRows.addView(empty);
        } else {
            int count = Math.min(sorted.size(), 12);
            for (int i = 0; i < count; i++) {
                ScanResult result = sorted.get(i);
                String security = securityLabel(result.capabilities);
                String label = activity.getString(R.string.wifi_network_row, result.SSID, security,
                        signalLabel(result.level));
                TextView row = action(label, "wifi_network:" + result.SSID,
                        activity.getResources().getDisplayMetrics().density,
                        () -> dismissThen(openSystemWifiSettings));
                networkRows.addView(row);
                focusOrder.add(row);
            }
        }
        refreshStatus();
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
        }
    }

    private String securityLabel(String capabilities) {
        String value = capabilities == null ? "" : capabilities.toUpperCase(Locale.ROOT);
        if (value.contains("SAE")) return activity.getString(R.string.wifi_security_wpa3);
        if (value.contains("WEP")) return activity.getString(R.string.wifi_security_wep);
        if (value.contains("PSK") || value.contains("EAP")) return activity.getString(R.string.wifi_security_secured);
        return activity.getString(R.string.wifi_security_open);
    }

    private String signalLabel(int level) {
        int bars = WifiManager.calculateSignalLevel(level, 4) + 1;
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
        styleText(view, TvUiTokens.CARD_TITLE_SP, true, density);
        return prepareAction(view, tag, density, callback);
    }

    private TextView prepareAction(TextView view, String tag, float density, Runnable callback) {
        view.setTag(tag);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setMinHeight(dp(density, TvUiTokens.MIN_DPAD_TARGET_DP));
        view.setPadding(dp(density, 16), dp(density, 8), dp(density, 16), dp(density, 8));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(density, 5);
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
        if (permissionAction.getVisibility() == View.VISIBLE) focusOrder.add(permissionAction);
        if (locationAction.getVisibility() == View.VISIBLE) focusOrder.add(locationAction);
        for (int i = 0; i < networkRows.getChildCount(); i++) {
            View child = networkRows.getChildAt(i);
            if (child.isFocusable() && child.getVisibility() == View.VISIBLE) focusOrder.add(child);
        }
        if (rescan.getVisibility() == View.VISIBLE) focusOrder.add(rescan);
        else focusOrder.add(androidSettings);
        focusOrder.add(close);
        for (int i = 0; i < focusOrder.size(); i++) {
            View view = focusOrder.get(i);
            int previous = i > 0 ? focusOrder.get(i - 1).getId() : view.getId();
            int next = i + 1 < focusOrder.size() ? focusOrder.get(i + 1).getId() : view.getId();
            view.setNextFocusUpId(previous);
            view.setNextFocusDownId(next);
            view.setNextFocusLeftId(view.getId());
            view.setNextFocusRightId(view.getId());
        }
        if (rescan.getVisibility() == View.VISIBLE) {
            androidSettings.setNextFocusUpId(rescan.getNextFocusUpId());
            androidSettings.setNextFocusDownId(rescan.getNextFocusDownId());
            rescan.setNextFocusRightId(androidSettings.getId());
            androidSettings.setNextFocusLeftId(rescan.getId());
            androidSettings.setNextFocusRightId(androidSettings.getId());
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

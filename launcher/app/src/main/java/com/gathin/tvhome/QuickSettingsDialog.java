package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Single-frame Quick Settings glass surface. Existing actions remain owned by their modules. */
final class QuickSettingsDialog {
    interface Actions {
        String wifiSummary();
        String languageSummary();
        String powerTitle();
        String cacheTitle();
        String wallpaperIntervalSummary();
        void openWifi();
        void restart();
        void chooseLanguage();
        void openPowerMenu();
        void cleanCache();
        void nextPhoto();
        void chooseWallpaperInterval();
        void manageWallpaper();
        void openGlassSettings();
        void openAndroidSettings();
    }

    private QuickSettingsDialog() { }

    static AlertDialog show(Activity activity, android.widget.FrameLayout host, View settingsAnchor,
                            View dock, Actions actions, int initialFocus, Runnable onDismiss) {
        if (activity == null || host == null || settingsAnchor == null || dock == null || actions == null
                || activity.isFinishing() || activity.isDestroyed()) return null;

        float density = activity.getResources().getDisplayMetrics().density;
        int[] hostAt = new int[2], anchorAt = new int[2], dockAt = new int[2];
        host.getLocationOnScreen(hostAt);
        settingsAnchor.getLocationOnScreen(anchorAt);
        dock.getLocationOnScreen(dockAt);
        int hostWidth = host.getWidth() > 0 ? host.getWidth() : activity.getResources().getDisplayMetrics().widthPixels;
        int hostHeight = host.getHeight() > 0 ? host.getHeight() : activity.getResources().getDisplayMetrics().heightPixels;
        int top = anchorAt[1] + settingsAnchor.getHeight() - hostAt[1] + dp(density, 5);
        int dockTop = dock.isShown() && dock.getHeight() > 0
                ? dockAt[1] - hostAt[1] : hostHeight - dp(density, 28);
        int availableHeight = dockTop - dp(density, 5) - top;
        if (availableHeight <= dp(density, 120)) {
            android.util.Log.e("TvHome.QuickSettings", "Insufficient settings-capsule-to-Dock lane: "
                    + Math.round(availableHeight / density) + "dp");
            return null;
        }
        // Reference composition uses a narrow portrait panel, not a wide dashboard.
        int width = Math.min(Math.round(hostWidth * 0.38f), dp(density, 400));
        width = Math.min(width, Math.max(dp(density, 1), hostWidth - dp(density, 24)));
        int availableDp = Math.round(availableHeight / density);
        if (availableDp < 307) {
            android.util.Log.e("TvHome.QuickSettings", "Quick Settings lane is too short for the compact layout: " + availableDp + "dp");
            return null;
        }
        // One shared row-height token keeps all actions visually aligned. The available lane
        // determines the token; text and icon sizes remain fixed and are never squeezed.
        int tileHeightDp = Math.max(32, Math.min(42, (availableDp - 83) / 6));

        View[] controls = new View[QuickSettingsFocusGraph.COUNT];
        AlertDialog[] dialogHolder = new AlertDialog[1];

        LinearLayout content = new LinearLayout(activity);
        content.setTag("quick_settings_root");
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setPadding(dp(density, 12), dp(density, 6), dp(density, 12), dp(density, 6));
        content.setClipChildren(true);
        content.setClipToPadding(true);
        content.setBackground(new GlassDrawable(content, 18f * density, density, null, 0.5f));
        Backdrop.watch(content);

        LinearLayout header = new LinearLayout(activity);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = new TextView(activity);
        title.setText(R.string.quick_settings_title);
        title.setTextSize(14f);
        title.setTextColor(TvUiTokens.TEXT_PRIMARY);
        title.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        Drawable headerIcon = activity.getDrawable(R.drawable.ic_settings);
        if (headerIcon != null) {
            headerIcon = headerIcon.mutate();
            headerIcon.setTint(TvUiTokens.TEXT_PRIMARY);
            int iconSize = dp(density, 18);
            headerIcon.setBounds(0, 0, iconSize, iconSize);
            title.setCompoundDrawablesRelative(headerIcon, null, null, null);
            title.setCompoundDrawablePadding(dp(density, 7));
        }
        title.setSingleLine(true);
        title.setEllipsize(TextUtils.TruncateAt.END);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        header.addView(title);
        content.addView(header, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 26)));
        addDivider(activity, content, density, 2);

        GridLayout grid = new GridLayout(activity);
        grid.setColumnCount(2);
        grid.setRowCount(3);
        grid.setUseDefaultMargins(false);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        controls[QuickSettingsFocusGraph.WIFI] = addGridTile(activity, grid, 0, 0, 1,
                R.drawable.ic_wifi, R.string.wifi, null,
                safe(actions.wifiSummary(), activity.getString(R.string.quick_settings_wifi_summary)),
                density, dialogHolder, actions::openWifi, tileHeightDp);
        controls[QuickSettingsFocusGraph.WIFI].setTag("quick_settings_wifi");
        controls[QuickSettingsFocusGraph.RESTART] = addGridTile(activity, grid, 0, 1, 1,
                R.drawable.ic_restart, R.string.restart_tv_title, null,
                activity.getString(R.string.quick_settings_restart_summary),
                density, dialogHolder, actions::restart, tileHeightDp);
        controls[QuickSettingsFocusGraph.RESTART].setTag("quick_settings_restart");
        controls[QuickSettingsFocusGraph.LANGUAGE] = addGridTile(activity, grid, 1, 0, 1,
                R.drawable.ic_language, R.string.app_language_title, null,
                safe(actions.languageSummary(), ""),
                density, dialogHolder, actions::chooseLanguage, tileHeightDp);
        controls[QuickSettingsFocusGraph.LANGUAGE].setTag("quick_settings_language");
        controls[QuickSettingsFocusGraph.POWER] = addGridTile(activity, grid, 1, 1, 1,
                R.drawable.ic_power, 0, actions.powerTitle(),
                activity.getString(R.string.quick_settings_power_summary),
                density, dialogHolder, actions::openPowerMenu, tileHeightDp);
        controls[QuickSettingsFocusGraph.POWER].setTag("quick_settings_power");
        controls[QuickSettingsFocusGraph.CACHE] = addGridTile(activity, grid, 2, 0, 2,
                R.drawable.ic_clean, 0, actions.cacheTitle(),
                activity.getString(R.string.quick_settings_cache_summary),
                density, dialogHolder, actions::cleanCache, tileHeightDp);
        controls[QuickSettingsFocusGraph.CACHE].setTag("quick_settings_cache");
        content.addView(grid, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addDivider(activity, content, density, 2);
        LinearLayout wallpaperHeader = new LinearLayout(activity);
        wallpaperHeader.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperHeader.setGravity(Gravity.CENTER_VERTICAL);
        TextView wallpaperTitle = new TextView(activity);
        wallpaperTitle.setText(R.string.quick_settings_wallpaper_title);
        wallpaperTitle.setTextSize(12f);
        wallpaperTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        wallpaperTitle.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        wallpaperTitle.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        wallpaperHeader.addView(wallpaperTitle);
        TextView manageWallpaper = createLink(activity, R.string.quick_settings_manage_wallpaper, 22, density);
        controls[QuickSettingsFocusGraph.WALLPAPER_MANAGE] = manageWallpaper;
        manageWallpaper.setTag("quick_settings_manage_wallpaper");
        wallpaperHeader.addView(manageWallpaper, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(density, 22)));
        content.addView(wallpaperHeader, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 20)));
        bind(manageWallpaper, dialogHolder, actions::manageWallpaper);

        LinearLayout wallpaperActions = new LinearLayout(activity);
        wallpaperActions.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperActions.setGravity(Gravity.CENTER_VERTICAL);
        TextView nextPhoto = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_next_photo,
                activity.getString(R.string.quick_settings_next_photo_summary), tileHeightDp, density);
        controls[QuickSettingsFocusGraph.NEXT_PHOTO] = nextPhoto;
        nextPhoto.setTag("quick_settings_next_photo");
        bind(nextPhoto, dialogHolder, actions::nextPhoto);
        LinearLayout.LayoutParams nextLp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        nextLp.setMargins(0, 0, dp(density, 3), 0);
        wallpaperActions.addView(nextPhoto, nextLp);

        TextView interval = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_interval_title,
                safe(actions.wallpaperIntervalSummary(), activity.getString(R.string.quick_settings_interval_summary)),
                tileHeightDp, density);
        controls[QuickSettingsFocusGraph.INTERVAL] = interval;
        interval.setTag("quick_settings_interval");
        bind(interval, dialogHolder, actions::chooseWallpaperInterval);
        LinearLayout.LayoutParams intervalLp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        intervalLp.setMargins(dp(density, 3), 0, 0, 0);
        wallpaperActions.addView(interval, intervalLp);
        content.addView(wallpaperActions, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, tileHeightDp)));

        addDivider(activity, content, density, 2);
        LinearLayout settingsActions = new LinearLayout(activity);
        settingsActions.setOrientation(LinearLayout.VERTICAL);
        settingsActions.setGravity(Gravity.CENTER_VERTICAL);
        TextView glassSettings = createTile(activity, R.drawable.ic_settings,
                R.string.quick_settings_glass_settings,
                activity.getString(R.string.quick_settings_glass_settings_summary), tileHeightDp, density);
        controls[QuickSettingsFocusGraph.GLASS_SETTINGS] = glassSettings;
        glassSettings.setTag("quick_settings_glass_settings");
        bind(glassSettings, dialogHolder, actions::openGlassSettings);
        LinearLayout.LayoutParams glassLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, tileHeightDp));
        glassLp.setMargins(0, dp(density, 1), 0, dp(density, 1));
        settingsActions.addView(glassSettings, glassLp);

        TextView androidSettings = createTile(activity, R.drawable.ic_settings,
                R.string.quick_settings_android_settings,
                activity.getString(R.string.quick_settings_android_settings_summary), tileHeightDp, density);
        controls[QuickSettingsFocusGraph.ANDROID_SETTINGS] = androidSettings;
        androidSettings.setTag("quick_settings_android_settings");
        bind(androidSettings, dialogHolder, actions::openAndroidSettings);
        LinearLayout.LayoutParams androidLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, tileHeightDp));
        androidLp.setMargins(0, dp(density, 1), 0, dp(density, 1));
        settingsActions.addView(androidSettings, androidLp);
        content.addView(settingsActions, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, tileHeightDp * 2 + 4)));
        View breathingSpace = new View(activity);
        content.addView(breathingSpace, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        QuickSettingsFocusGraph.connect(controls);

        final int panelWidth = width;
        final int panelHeight = availableHeight;
        final int panelTop = top;
        content.setLayoutParams(new ViewGroup.LayoutParams(panelWidth, panelHeight));
        content.measure(View.MeasureSpec.makeMeasureSpec(panelWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(panelHeight, View.MeasureSpec.EXACTLY));

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setView(content).create();
        dialogHolder[0] = dialog;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((ignored, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                if (event.getAction() == KeyEvent.ACTION_UP) dialog.dismiss();
                return true;
            }
            return false;
        });
        dialog.setOnDismissListener(d -> { if (onDismiss != null) onDismiss.run(); });
        dialog.setOnShowListener(d -> {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                window.setLayout(panelWidth, panelHeight);
                WindowManager.LayoutParams params = window.getAttributes();
                params.y = panelTop;
                params.dimAmount = 0f;
                window.setAttributes(params);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            View target = controls[initialFocus >= 0 && initialFocus < controls.length
                    ? initialFocus : QuickSettingsFocusGraph.WIFI];
            content.post(() -> { if (target != null && target.isShown()) target.requestFocus(); });
        });
        dialog.show();
        return dialog;
    }

    private static View addGridTile(Activity activity, GridLayout grid, int row, int column, int span,
                                    int icon, int titleRes, String titleOverride, String summary, float density,
                                    AlertDialog[] dialogHolder, Runnable action, int tileHeightDp) {
        String title = titleOverride != null ? titleOverride : activity.getString(titleRes);
        TextView tile = createTile(activity, icon, title, summary, tileHeightDp, density);
        bind(tile, dialogHolder, action);
        GridLayout.Spec rowSpec = GridLayout.spec(row);
        GridLayout.Spec columnSpec = GridLayout.spec(column, span, 1f);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
        lp.width = 0;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.setMargins(dp(density, 3), dp(density, 1), dp(density, 3), dp(density, 1));
        grid.addView(tile, lp);
        return tile;
    }

    private static TextView createTile(Activity activity, int iconRes, int titleRes,
                                       String summary, int minHeightDp, float density) {
        return createTile(activity, iconRes, activity.getString(titleRes), summary, minHeightDp, density);
    }

    private static TextView createTile(Activity activity, int iconRes, String title,
                                       String summary, int minHeightDp, float density) {
        TextView tile = new TextView(activity);
        tile.setId(View.generateViewId());
        tile.setFocusable(true);
        tile.setFocusableInTouchMode(true);
        tile.setClickable(true);
        tile.setGravity(Gravity.CENTER_VERTICAL);
        tile.setPadding(dp(density, 8), dp(density, 2), dp(density, 6), dp(density, 2));
        tile.setMinHeight(dp(density, minHeightDp));
        tile.setMaxLines(2);
        tile.setEllipsize(android.text.TextUtils.TruncateAt.END);
        tile.setTextSize(12f);
        tile.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.NORMAL);
        tile.setTextColor(TvUiTokens.TEXT_PRIMARY);
        String displayTitle = stripDecorativePrefix(title);
        tile.setText(buildLabel(displayTitle, summary));
        tile.setContentDescription(summary == null || summary.isEmpty()
                ? displayTitle : displayTitle + ", " + summary);

        if (iconRes != 0) {
            Drawable icon = activity.getDrawable(iconRes);
            if (icon != null) {
                icon = icon.mutate();
                icon.setTint(TvUiTokens.TEXT_PRIMARY);
                int size = dp(density, 18);
                icon.setBounds(0, 0, size, size);
                tile.setCompoundDrawablesRelative(icon, null, null, null);
                tile.setCompoundDrawablePadding(dp(density, 6));
            }
        }
        setNormalSurface(tile, density, 12f);
        TvFocusIndicator.applyRoundedTextFocus(tile, density, 12f,
                TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_FOCUSED,
                TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER,
                TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
        return tile;
    }

    private static TextView createLink(Activity activity, int titleRes, int heightDp, float density) {
        TextView link = new TextView(activity);
        link.setId(View.generateViewId());
        link.setFocusable(true);
        link.setFocusableInTouchMode(true);
        link.setClickable(true);
        link.setGravity(Gravity.CENTER);
        link.setPadding(dp(density, 9), dp(density, 2), dp(density, 9), dp(density, 2));
        link.setMinHeight(dp(density, heightDp));
        link.setText(titleRes);
        link.setTextSize(10.5f);
        link.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        link.setTextColor(TvUiTokens.TEXT_PRIMARY);
        link.setContentDescription(activity.getString(titleRes));
        setNormalSurface(link, density, 12f);
        TvFocusIndicator.applyRoundedTextFocus(link, density, 12f,
                TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_FOCUSED,
                TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER,
                TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
        return link;
    }

    private static String stripDecorativePrefix(String value) {
        if (value == null) return "";
        // Action glyphs are rendered by the shared 18dp drawable slot, never duplicated in the label.
        return value.replaceFirst("^[\\\\p{So}\\\\p{Sk}\\\\p{Cf}\\\\s]+", "").trim();
    }

    private static SpannableStringBuilder buildLabel(String title, String summary) {
        SpannableStringBuilder text = new SpannableStringBuilder(title == null ? "" : title);
        if (summary != null && !summary.isEmpty()) {
            int start = text.length();
            text.append("\n").append(summary);
            text.setSpan(new RelativeSizeSpan(0.75f), start, text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new ForegroundColorSpan(TvUiTokens.TEXT_SECONDARY), start, text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new StyleSpan(Typeface.NORMAL), start, text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (title != null && !title.isEmpty()) {
            text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return text;
    }

    private static void bind(TextView view, AlertDialog[] dialogHolder, Runnable action) {
        view.setOnClickListener(v -> {
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
            if (action != null) action.run();
        });
    }

    private static void addDivider(Activity activity, LinearLayout parent, float density, int topMarginDp) {
        View divider = new View(activity);
        divider.setBackgroundColor(0x66DCE5ED);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 1));
        lp.topMargin = dp(density, topMarginDp);
        lp.bottomMargin = dp(density, 2);
        parent.addView(divider, lp);
    }

        private static void setNormalSurface(TextView view, float density, float radiusDp) {
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setColor(TvUiTokens.SURFACE_IDLE);
        background.setCornerRadius(radiusDp * density);
        background.setStroke(Math.max(1, dp(density, 1)), 0x779EABB8);
        view.setBackground(background);
    }

    private static String safe(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    private static int dp(float density, int value) {
        return Math.max(1, Math.round(density * value));
    }
}

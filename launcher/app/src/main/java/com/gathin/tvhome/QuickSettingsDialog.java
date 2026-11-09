package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** V04 compact, centered Quick Settings surface. Actions remain owned by their existing modules. */
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

    static AlertDialog show(Activity activity, Actions actions, int initialFocus, View anchor,
                            Runnable onDismiss) {
        if (activity == null || actions == null || anchor == null || activity.isFinishing() || activity.isDestroyed()) {
            return null;
        }

        float density = activity.getResources().getDisplayMetrics().density;
        View[] controls = new View[QuickSettingsFocusGraph.COUNT];
        AlertDialog[] dialogHolder = new AlertDialog[1];

        // Compact two-column surface: every action remains visible without vertical scrolling.
        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(density, 10), dp(density, 6), dp(density, 10), dp(density, 6));
        content.setClipChildren(false);
        content.setClipToPadding(false);
        content.setTag("quick_settings_content");

        LinearLayout header = new LinearLayout(activity);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = new TextView(activity);
        title.setText(R.string.quick_settings_title);
        title.setTextSize(13f);
        title.setTextColor(TvUiTokens.TEXT_PRIMARY);
        title.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        Drawable headerIcon = activity.getDrawable(R.drawable.ic_settings);
        if (headerIcon != null) {
            headerIcon = headerIcon.mutate();
            headerIcon.setTint(TvUiTokens.TEXT_PRIMARY);
            int iconSize = dp(density, 18);
            headerIcon.setBounds(0, 0, iconSize, iconSize);
            title.setCompoundDrawablesRelative(headerIcon, null, null, null);
            title.setCompoundDrawablePadding(dp(density, 8));
        }
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        header.addView(title);
        TextView close = createLink(activity, R.string.close_label, 28, density);
        close.setFocusable(false);
        close.setFocusableInTouchMode(false);
        close.setText("×");
        close.setTextSize(20f);
        close.setContentDescription(activity.getString(R.string.close_label));
        close.setTag("quick_settings_close");
        header.addView(close, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(density, 28)));
        content.addView(header);
        bind(close, dialogHolder, null);
        addDivider(activity, content, density, 3);

        GridLayout primary = new GridLayout(activity);
        primary.setColumnCount(2);
        primary.setRowCount(3);
        primary.setUseDefaultMargins(false);
        primary.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        controls[QuickSettingsFocusGraph.WIFI] = addGridTile(activity, primary, 0, 0, 1, R.drawable.ic_wifi,
                R.string.wifi, null, safe(actions.wifiSummary(), activity.getString(R.string.quick_settings_wifi_summary)),
                density, dialogHolder, actions::openWifi);
        controls[QuickSettingsFocusGraph.WIFI].setTag("quick_settings_wifi");
        controls[QuickSettingsFocusGraph.RESTART] = addGridTile(activity, primary, 0, 1, 1, R.drawable.ic_power,
                R.string.restart_tv_title, null, null,
                density, dialogHolder, actions::restart);
        controls[QuickSettingsFocusGraph.RESTART].setTag("quick_settings_restart");
        controls[QuickSettingsFocusGraph.LANGUAGE] = addGridTile(activity, primary, 1, 0, 1, R.drawable.ic_settings,
                R.string.app_language_title, null, safe(actions.languageSummary(), ""),
                density, dialogHolder, actions::chooseLanguage);
        controls[QuickSettingsFocusGraph.LANGUAGE].setTag("quick_settings_language");
        controls[QuickSettingsFocusGraph.POWER] = addGridTile(activity, primary, 1, 1, 1, R.drawable.ic_power,
                0, actions.powerTitle(), null,
                density, dialogHolder, actions::openPowerMenu);
        controls[QuickSettingsFocusGraph.POWER].setTag("quick_settings_power");
        controls[QuickSettingsFocusGraph.CACHE] = addGridTile(activity, primary, 2, 0, 2, R.drawable.ic_settings,
                0, actions.cacheTitle(), activity.getString(R.string.quick_settings_cache_summary),
                density, dialogHolder, actions::cleanCache);
        controls[QuickSettingsFocusGraph.CACHE].setTag("quick_settings_cache");
        content.addView(primary, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout wallpaperHeader = new LinearLayout(activity);
        wallpaperHeader.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperHeader.setGravity(Gravity.CENTER_VERTICAL);
        TextView wallpaperTitle = new TextView(activity);
        wallpaperTitle.setText(R.string.quick_settings_wallpaper_title);
        wallpaperTitle.setTextSize(12f);
        wallpaperTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        wallpaperTitle.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        wallpaperTitle.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        wallpaperHeader.addView(wallpaperTitle);
        TextView manageWallpaper = createLink(activity, R.string.quick_settings_manage_wallpaper, 28, density);
        controls[QuickSettingsFocusGraph.WALLPAPER_MANAGE] = manageWallpaper;
        manageWallpaper.setTag("quick_settings_manage_wallpaper");
        wallpaperHeader.addView(manageWallpaper, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(density, 28)));
        content.addView(wallpaperHeader);
        bind(manageWallpaper, dialogHolder, actions::manageWallpaper);

        LinearLayout wallpaperActions = new LinearLayout(activity);
        wallpaperActions.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperActions.setGravity(Gravity.CENTER_VERTICAL);
        TextView nextPhoto = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_next_photo,
                null, 28, density);
        controls[QuickSettingsFocusGraph.NEXT_PHOTO] = nextPhoto;
        nextPhoto.setTag("quick_settings_next_photo");
        bind(nextPhoto, dialogHolder, actions::nextPhoto);
        LinearLayout.LayoutParams nextLp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        nextLp.setMargins(0, dp(density, 2), dp(density, 2), dp(density, 2));
        wallpaperActions.addView(nextPhoto, nextLp);

        TextView interval = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_interval_title,
                safe(actions.wallpaperIntervalSummary(), activity.getString(R.string.quick_settings_interval_summary)),
                28, density);
        controls[QuickSettingsFocusGraph.INTERVAL] = interval;
        interval.setTag("quick_settings_interval");
        bind(interval, dialogHolder, actions::chooseWallpaperInterval);
        LinearLayout.LayoutParams intervalLp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        intervalLp.setMargins(dp(density, 2), dp(density, 2), 0, dp(density, 2));
        wallpaperActions.addView(interval, intervalLp);
        content.addView(wallpaperActions);

        addDivider(activity, content, density, 3);

        LinearLayout settingsActions = new LinearLayout(activity);
        settingsActions.setOrientation(LinearLayout.VERTICAL);
        settingsActions.setGravity(Gravity.CENTER_VERTICAL);
        TextView glassSettings = createTile(activity, R.drawable.ic_settings,
                R.string.quick_settings_glass_settings,
                null, 32, density);
        controls[QuickSettingsFocusGraph.GLASS_SETTINGS] = glassSettings;
        glassSettings.setTag("quick_settings_glass_settings");
        bind(glassSettings, dialogHolder, actions::openGlassSettings);
        LinearLayout.LayoutParams glassLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        glassLp.setMargins(0, dp(density, 2), 0, dp(density, 2));
        settingsActions.addView(glassSettings, glassLp);

        TextView androidSettings = createTile(activity, R.drawable.ic_settings,
                R.string.quick_settings_android_settings,
                null, 32, density);
        controls[QuickSettingsFocusGraph.ANDROID_SETTINGS] = androidSettings;
        androidSettings.setTag("quick_settings_android_settings");
        bind(androidSettings, dialogHolder, actions::openAndroidSettings);
        LinearLayout.LayoutParams androidLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        androidLp.setMargins(0, dp(density, 2), 0, dp(density, 2));
        settingsActions.addView(androidSettings, androidLp);
        content.addView(settingsActions);

        QuickSettingsFocusGraph.connect(controls);

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setView(content)
                .create();
        dialogHolder[0] = dialog;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(d -> {
            if (onDismiss != null) onDismiss.run();
        });
        dialog.setOnShowListener(d -> {
            TvDialogStyle.apply(dialog, activity, 360, 360);
            android.view.Window window = dialog.getWindow();
            if (window != null) {
                android.util.DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
                int popupWidth = Math.round(metrics.widthPixels * 0.32f);
                int popupHeight = Math.round(metrics.heightPixels * 0.66f);
                int[] anchorPosition = new int[2];
                anchor.getLocationOnScreen(anchorPosition);
                int gap = dp(density, 8);
                int left = Math.min(anchorPosition[0], Math.max(0, metrics.widthPixels - popupWidth));
                int top = Math.min(anchorPosition[1] + anchor.getHeight() + gap,
                        Math.max(0, metrics.heightPixels - popupHeight));
                window.setLayout(popupWidth, popupHeight);
                window.setGravity(Gravity.TOP | Gravity.LEFT);
                android.view.WindowManager.LayoutParams params = window.getAttributes();
                params.x = left;
                params.y = top;
                window.setAttributes(params);
            }
            int requested = initialFocus >= 0 && initialFocus < controls.length
                    ? initialFocus : QuickSettingsFocusGraph.WIFI;
            View target = controls[requested] != null ? controls[requested]
                    : controls[QuickSettingsFocusGraph.WIFI];
            if (target != null) target.requestFocus();
        });
        dialog.show();
        return dialog;
    }

    private static View addGridTile(Activity activity, GridLayout grid, int row, int column, int span,
                                    int icon, int titleRes, String titleOverride, String summary, float density,
                                    AlertDialog[] dialogHolder, Runnable action) {
        String title = titleOverride != null ? titleOverride : activity.getString(titleRes);
        TextView tile = createTile(activity, icon, title, summary, 76, density);
        bind(tile, dialogHolder, action);
        GridLayout.Spec rowSpec = GridLayout.spec(row);
        GridLayout.Spec columnSpec = GridLayout.spec(column, span, 1f);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
        lp.width = 0;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.setMargins(dp(density, 2), dp(density, 2), dp(density, 2), dp(density, 2));
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
        tile.setPadding(dp(density, 6), dp(density, 2), dp(density, 6), dp(density, 2));
        tile.setMinHeight(dp(density, Math.min(minHeightDp, 32)));
        tile.setMaxLines(2);
        tile.setEllipsize(android.text.TextUtils.TruncateAt.END);
        tile.setTextSize(10f);
        tile.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.NORMAL);
        tile.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tile.setText(buildLabel(title, summary));
        tile.setContentDescription(summary == null || summary.isEmpty() ? title : title + ", " + summary);

        if (iconRes != 0) {
            Drawable icon = activity.getDrawable(iconRes);
            if (icon != null) {
                icon = icon.mutate();
                icon.setTint(TvUiTokens.TEXT_PRIMARY);
                int size = dp(density, 16);
                icon.setBounds(0, 0, size, size);
                tile.setCompoundDrawablesRelative(icon, null, null, null);
                tile.setCompoundDrawablePadding(dp(density, 4));
            }
        }
        setNormalSurface(tile, density, TvUiTokens.ROW_RADIUS_DP);
        TvFocusIndicator.applyRoundedTextFocus(tile, density, TvUiTokens.ROW_RADIUS_DP,
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
        link.setPadding(dp(density, 7), dp(density, 2), dp(density, 7), dp(density, 2));
        link.setMinHeight(dp(density, heightDp));
        link.setText(titleRes);
        link.setTextSize(10f);
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

    private static SpannableStringBuilder buildLabel(String title, String summary) {
        SpannableStringBuilder text = new SpannableStringBuilder(title == null ? "" : title);
        if (summary != null && !summary.isEmpty()) {
            int start = text.length();
            text.append("\n").append(summary);
            text.setSpan(new RelativeSizeSpan(0.78f), start, text.length(),
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
        lp.bottomMargin = dp(density, 4);
        parent.addView(divider, lp);
    }

    private static void setNormalSurface(TextView view, float density, float radiusDp) {
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setColor(TvUiTokens.SURFACE_IDLE);
        background.setCornerRadius(radiusDp * density);
        background.setStroke(Math.max(1, dp(density, 1)), TvUiTokens.BORDER);
        view.setBackground(background);
    }

    private static String safe(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    private static int dp(float density, int value) {
        return Math.max(1, Math.round(density * value));
    }
}

package com.gathin.tvhome;

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
import android.widget.ScrollView;
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

    static AlertDialog show(MainActivity activity, Actions actions, int initialFocus,
                            Runnable onDismiss) {
        if (activity == null || actions == null || activity.isFinishing() || activity.isDestroyed()) {
            return null;
        }

        float density = activity.getResources().getDisplayMetrics().density;
        View[] controls = new View[QuickSettingsFocusGraph.COUNT];
        AlertDialog[] dialogHolder = new AlertDialog[1];

        ScrollView scroll = new ScrollView(activity);
        scroll.setFillViewport(true);
        scroll.setVerticalScrollBarEnabled(false);
        scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        scroll.setClipChildren(false);
        scroll.setClipToPadding(false);

        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(density, 20), dp(density, 16), dp(density, 20), dp(density, 16));
        content.setClipChildren(false);
        content.setClipToPadding(false);
        scroll.addView(content, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout header = new LinearLayout(activity);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = new TextView(activity);
        title.setText(R.string.quick_settings_title);
        title.setTextSize(TvUiTokens.PAGE_HEADER_SP);
        title.setTextColor(TvUiTokens.TEXT_PRIMARY);
        title.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        header.addView(title);

        TextView close = createLink(activity, R.string.close_label, TvUiTokens.MIN_DPAD_TARGET_DP, density);
        controls[QuickSettingsFocusGraph.CLOSE] = close;
        header.addView(close, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(density, TvUiTokens.MIN_DPAD_TARGET_DP)));
        content.addView(header);
        bind(close, dialogHolder, null);

        addDivider(activity, content, density, 10);

        GridLayout grid = new GridLayout(activity);
        grid.setColumnCount(2);
        grid.setRowCount(3);
        grid.setUseDefaultMargins(false);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        controls[QuickSettingsFocusGraph.WIFI] = addGridTile(activity, grid, 0, 0, 1,
                R.drawable.ic_wifi, R.string.wifi, null,
                safe(actions.wifiSummary(), activity.getString(R.string.quick_settings_wifi_summary)),
                density, dialogHolder, actions::openWifi);
        controls[QuickSettingsFocusGraph.RESTART] = addGridTile(activity, grid, 0, 1, 1,
                R.drawable.ic_power, R.string.restart_tv_title, null,
                activity.getString(R.string.quick_settings_restart_summary),
                density, dialogHolder, actions::restart);
        controls[QuickSettingsFocusGraph.LANGUAGE] = addGridTile(activity, grid, 1, 0, 1,
                R.drawable.ic_settings, R.string.app_language_title, null,
                safe(actions.languageSummary(), ""),
                density, dialogHolder, actions::chooseLanguage);
        controls[QuickSettingsFocusGraph.POWER] = addGridTile(activity, grid, 1, 1, 1,
                R.drawable.ic_power, 0, actions.powerTitle(),
                activity.getString(R.string.quick_settings_power_summary),
                density, dialogHolder, actions::openPowerMenu);
        controls[QuickSettingsFocusGraph.CACHE] = addGridTile(activity, grid, 2, 0, 2,
                R.drawable.ic_settings, 0, actions.cacheTitle(),
                activity.getString(R.string.quick_settings_cache_summary),
                density, dialogHolder, actions::cleanCache);
        content.addView(grid, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addDivider(activity, content, density, 12);

        LinearLayout wallpaperHeader = new LinearLayout(activity);
        wallpaperHeader.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperHeader.setGravity(Gravity.CENTER_VERTICAL);
        TextView wallpaperTitle = new TextView(activity);
        wallpaperTitle.setText(R.string.quick_settings_wallpaper_title);
        wallpaperTitle.setTextSize(TvUiTokens.SECTION_TITLE_SP);
        wallpaperTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        wallpaperTitle.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        wallpaperTitle.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        wallpaperHeader.addView(wallpaperTitle);
        TextView manageWallpaper = createLink(activity, R.string.quick_settings_manage_wallpaper, TvUiTokens.MIN_DPAD_TARGET_DP, density);
        controls[QuickSettingsFocusGraph.WALLPAPER_MANAGE] = manageWallpaper;
        wallpaperHeader.addView(manageWallpaper, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(density, TvUiTokens.MIN_DPAD_TARGET_DP)));
        content.addView(wallpaperHeader);
        bind(manageWallpaper, dialogHolder, actions::manageWallpaper);

        LinearLayout wallpaperActions = new LinearLayout(activity);
        wallpaperActions.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperActions.setGravity(Gravity.CENTER_VERTICAL);
        TextView nextPhoto = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_next_photo,
                activity.getString(R.string.quick_settings_next_photo_summary), 72, density);
        controls[QuickSettingsFocusGraph.NEXT_PHOTO] = nextPhoto;
        bind(nextPhoto, dialogHolder, actions::nextPhoto);
        LinearLayout.LayoutParams nextLp = new LinearLayout.LayoutParams(0,
                dp(density, 72), 1f);
        nextLp.setMargins(0, dp(density, 5), dp(density, 5), dp(density, 5));
        wallpaperActions.addView(nextPhoto, nextLp);

        TextView interval = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_interval_title,
                safe(actions.wallpaperIntervalSummary(), activity.getString(R.string.quick_settings_interval_summary)),
                72, density);
        controls[QuickSettingsFocusGraph.INTERVAL] = interval;
        bind(interval, dialogHolder, actions::chooseWallpaperInterval);
        LinearLayout.LayoutParams intervalLp = new LinearLayout.LayoutParams(0,
                dp(density, 72), 1f);
        intervalLp.setMargins(dp(density, 5), dp(density, 5), 0, dp(density, 5));
        wallpaperActions.addView(interval, intervalLp);
        content.addView(wallpaperActions);

        addDivider(activity, content, density, 8);

        LinearLayout settingsActions = new LinearLayout(activity);
        settingsActions.setOrientation(LinearLayout.HORIZONTAL);
        settingsActions.setGravity(Gravity.CENTER_VERTICAL);
        TextView glassSettings = createTile(activity, R.drawable.ic_settings,
                R.string.quick_settings_glass_settings,
                activity.getString(R.string.quick_settings_glass_settings_summary), 64, density);
        controls[QuickSettingsFocusGraph.GLASS_SETTINGS] = glassSettings;
        bind(glassSettings, dialogHolder, actions::openGlassSettings);
        LinearLayout.LayoutParams glassLp = new LinearLayout.LayoutParams(0,
                dp(density, 64), 1f);
        glassLp.setMargins(0, dp(density, 4), dp(density, 5), dp(density, 4));
        settingsActions.addView(glassSettings, glassLp);

        TextView androidSettings = createTile(activity, R.drawable.ic_settings,
                R.string.quick_settings_android_settings,
                activity.getString(R.string.quick_settings_android_settings_summary), 64, density);
        controls[QuickSettingsFocusGraph.ANDROID_SETTINGS] = androidSettings;
        bind(androidSettings, dialogHolder, actions::openAndroidSettings);
        LinearLayout.LayoutParams androidLp = new LinearLayout.LayoutParams(0,
                dp(density, 64), 1f);
        androidLp.setMargins(dp(density, 5), dp(density, 4), 0, dp(density, 4));
        settingsActions.addView(androidSettings, androidLp);
        content.addView(settingsActions);

        QuickSettingsFocusGraph.connect(controls);

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setView(scroll, 0, 0, 0, 0)
                .create();
        dialogHolder[0] = dialog;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(d -> {
            if (onDismiss != null) onDismiss.run();
        });
        dialog.setOnShowListener(d -> {
            TvDialogStyle.apply(dialog, activity, 560, 420);
            int requested = initialFocus >= 0 && initialFocus < controls.length
                    ? initialFocus : QuickSettingsFocusGraph.WIFI;
            View target = controls[requested] != null ? controls[requested]
                    : controls[QuickSettingsFocusGraph.WIFI];
            if (target != null) target.requestFocus();
        });
        dialog.show();
        return dialog;
    }

    private static View addGridTile(MainActivity activity, GridLayout grid, int row, int column, int span,
                                    int icon, int titleRes, String titleOverride, String summary, float density,
                                    AlertDialog[] dialogHolder, Runnable action) {
        String title = titleOverride != null ? titleOverride : activity.getString(titleRes);
        TextView tile = createTile(activity, icon, title, summary, 76, density);
        bind(tile, dialogHolder, action);
        GridLayout.Spec rowSpec = GridLayout.spec(row);
        GridLayout.Spec columnSpec = GridLayout.spec(column, span, 1f);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
        lp.width = 0;
        lp.height = dp(density, 76);
        lp.setMargins(dp(density, 5), dp(density, 5), dp(density, 5), dp(density, 5));
        grid.addView(tile, lp);
        return tile;
    }

    private static TextView createTile(MainActivity activity, int iconRes, int titleRes,
                                       String summary, int minHeightDp, float density) {
        return createTile(activity, iconRes, activity.getString(titleRes), summary, minHeightDp, density);
    }

    private static TextView createTile(MainActivity activity, int iconRes, String title,
                                       String summary, int minHeightDp, float density) {
        TextView tile = new TextView(activity);
        tile.setId(View.generateViewId());
        tile.setFocusable(true);
        tile.setFocusableInTouchMode(true);
        tile.setClickable(true);
        tile.setGravity(Gravity.CENTER_VERTICAL);
        tile.setPadding(dp(density, 14), dp(density, 9), dp(density, 12), dp(density, 9));
        tile.setMinHeight(dp(density, minHeightDp));
        tile.setMaxLines(3);
        tile.setEllipsize(android.text.TextUtils.TruncateAt.END);
        tile.setTextSize(TvUiTokens.CARD_TITLE_SP);
        tile.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.NORMAL);
        tile.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tile.setText(buildLabel(title, summary));
        tile.setContentDescription(summary == null || summary.isEmpty() ? title : title + ", " + summary);

        if (iconRes != 0) {
            Drawable icon = activity.getDrawable(iconRes);
            if (icon != null) {
                icon = icon.mutate();
                icon.setTint(TvUiTokens.TEXT_PRIMARY);
                int size = dp(density, 28);
                icon.setBounds(0, 0, size, size);
                tile.setCompoundDrawablesRelative(icon, null, null, null);
                tile.setCompoundDrawablePadding(dp(density, 12));
            }
        }
        setNormalSurface(tile, density, TvUiTokens.ROW_RADIUS_DP);
        TvFocusIndicator.applyRoundedTextFocus(tile, density, TvUiTokens.ROW_RADIUS_DP,
                TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_FOCUSED,
                TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER,
                TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
        return tile;
    }

    private static TextView createLink(MainActivity activity, int titleRes, int heightDp, float density) {
        TextView link = new TextView(activity);
        link.setId(View.generateViewId());
        link.setFocusable(true);
        link.setFocusableInTouchMode(true);
        link.setClickable(true);
        link.setGravity(Gravity.CENTER);
        link.setPadding(dp(density, 14), dp(density, 6), dp(density, 14), dp(density, 6));
        link.setMinHeight(dp(density, heightDp));
        link.setText(titleRes);
        link.setTextSize(14f);
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

    private static void addDivider(MainActivity activity, LinearLayout parent, float density, int topMarginDp) {
        View divider = new View(activity);
        divider.setBackgroundColor(0x66426BAA);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 1));
        lp.topMargin = dp(density, topMarginDp);
        lp.bottomMargin = dp(density, 6);
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

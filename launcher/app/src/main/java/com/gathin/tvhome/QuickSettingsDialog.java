package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Layout;
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
        String languageSummary();
        String powerTitle();
        String cacheTitle();
        String wallpaperIntervalSummary();
        void openInputs();
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

    // Reuse the approved global Frosted Silver Glass material; this popup is not a second theme.
    private static final int PANEL_TEXT_PRIMARY = TvUiTokens.TEXT_PRIMARY;
    private static final int PANEL_TEXT_SECONDARY = TvUiTokens.TEXT_SECONDARY;
    private static final int CAPSULE_IDLE_FILL = 0x662D3741;
    private static final int CAPSULE_IDLE_BORDER = 0x559EABB8;
    private static final int CAPSULE_FOCUS_FILL = TvUiTokens.SURFACE_FOCUSED;
    private static final int CAPSULE_FOCUS_BORDER = TvUiTokens.FOCUS_BORDER;
    private static final int PANEL_FALLBACK_SURFACE = TvUiTokens.POPUP_SURFACE;
    private static final int PANEL_FALLBACK_BORDER = TvUiTokens.BORDER;
    private static final int TILE_HEIGHT_DP = 36;
    private static final int PANEL_HEIGHT_DP = 320;
    private static final int HALF_TILE_WIDTH_DP = 115;
    private static final int WIDE_TILE_WIDTH_DP = 235;
    private static final int CAPSULE_GAP_DP = 5;
    private static final int PANEL_VERTICAL_INSET_DP = 10;
    private static final int PANEL_SIDE_INSET_DP = 10;

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
        int topClearancePx = dp(density, 5);
        int bottomClearancePx = dp(density, 5);
        int top = panelTopInHost(anchorAt[1], settingsAnchor.getHeight(), hostAt[1], topClearancePx);
        int dockTop = dock.isShown() && dock.getHeight() > 0
                ? dockAt[1] - hostAt[1] : hostHeight - dp(density, 28);
        int availableHeight = availablePanelHeight(dockTop, top, bottomClearancePx);
        if (availableHeight <= 0) {
            android.util.Log.e("TvHome.QuickSettings", "No vertical space remains between navigation and dock");
            return null;
        }
        // Keep every action row the same height. Do not squeeze rows dynamically from screen
        // height: that made the panel's vertical rhythm change with device/window measurements.
        // The panel must end 5dp above the dock; never overlap the dock or silently clip content.
        int tileHeightDp = TILE_HEIGHT_DP;

        // The 255dp panel is positioned from the Settings-tab gear, so the popup header gear
        // sits on the same vertical axis as the navigation gear instead of being screen-centered.
        int width = dp(density, 255);
        if (hostWidth < width + dp(density, 24)) {
            android.util.Log.e("TvHome.QuickSettings", "Host is too narrow for the fixed 255dp panel");
            return null;
        }
        int settingsGearCenter = settingsIconCenterX(settingsAnchor, anchorAt[0], density) - hostAt[0];
        int panelLeft = settingsGearCenter - dp(density, 12) - dp(density, 18) / 2;
        panelLeft = Math.max(dp(density, 12), Math.min(panelLeft, hostWidth - width - dp(density, 12)));

        View[] controls = new View[QuickSettingsFocusGraph.COUNT];
        AlertDialog[] dialogHolder = new AlertDialog[1];

        LinearLayout content = new LinearLayout(activity);
        content.setTag("quick_settings_root");
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setPadding(dp(density, PANEL_SIDE_INSET_DP), dp(density, PANEL_VERTICAL_INSET_DP),
                dp(density, PANEL_SIDE_INSET_DP), dp(density, PANEL_VERTICAL_INSET_DP));
        content.setClipChildren(true);
        content.setClipToPadding(true);
        content.setBackground(UniversalGlassMaterial.panel(activity, density, TvUiTokens.POPUP_RADIUS_DP,
                PANEL_FALLBACK_SURFACE, PANEL_FALLBACK_BORDER));

        LinearLayout header = new LinearLayout(activity);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = new TextView(activity);
        title.setText(R.string.quick_settings_title);
        title.setTextSize(15f);
        title.setTextColor(PANEL_TEXT_PRIMARY);
        title.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        Drawable headerIcon = activity.getDrawable(R.drawable.ic_settings);
        if (headerIcon != null) {
            headerIcon = headerIcon.mutate();
            headerIcon.setTint(PANEL_TEXT_PRIMARY);
            int iconSize = dp(density, 18);
            headerIcon.setBounds(0, 0, iconSize, iconSize);
            title.setCompoundDrawablesRelative(headerIcon, null, null, null);
            title.setCompoundDrawablePadding(dp(density, 7));
        }
        title.setSingleLine(true);
        title.setEllipsize(TextUtils.TruncateAt.END);
        title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        header.addView(title);
        content.addView(header, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 26)));
        addFixedVerticalGap(activity, content, CAPSULE_GAP_DP, "quick_settings_gap_header_grid");

        GridLayout grid = new GridLayout(activity);
        grid.setColumnCount(3);
        grid.setRowCount(3);
        grid.setUseDefaultMargins(false);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        controls[QuickSettingsFocusGraph.INPUT] = addGridTile(activity, grid, 0, 0, 1,
                R.drawable.ic_input, R.string.quick_settings_input_title, null,
                null, density, dialogHolder, actions::openInputs, tileHeightDp);
        controls[QuickSettingsFocusGraph.INPUT].setTag("quick_settings_input");
        controls[QuickSettingsFocusGraph.RESTART] = addGridTile(activity, grid, 0, 2, 1,
                R.drawable.ic_restart, R.string.quick_settings_restart_title, null,
                null,
                density, dialogHolder, actions::restart, tileHeightDp);
        controls[QuickSettingsFocusGraph.RESTART].setTag("quick_settings_restart");
        addGridColumnGap(activity, grid, 0, density, tileHeightDp);
        controls[QuickSettingsFocusGraph.LANGUAGE] = addGridTile(activity, grid, 1, 0, 1,
                R.drawable.ic_language, R.string.quick_settings_language_title, null,
                safe(actions.languageSummary(), ""),
                density, dialogHolder, actions::chooseLanguage, tileHeightDp);
        controls[QuickSettingsFocusGraph.LANGUAGE].setTag("quick_settings_language");
        controls[QuickSettingsFocusGraph.POWER] = addGridTile(activity, grid, 1, 2, 1,
                R.drawable.ic_power, 0, actions.powerTitle(),
                null,
                density, dialogHolder, actions::openPowerMenu, tileHeightDp);
        controls[QuickSettingsFocusGraph.POWER].setTag("quick_settings_power");
        addGridColumnGap(activity, grid, 1, density, tileHeightDp);
        controls[QuickSettingsFocusGraph.CACHE] = addGridTile(activity, grid, 2, 0, 3,
                R.drawable.ic_clean, 0, actions.cacheTitle(),
                activity.getString(R.string.quick_settings_cache_summary),
                density, dialogHolder, actions::cleanCache, tileHeightDp);
        controls[QuickSettingsFocusGraph.CACHE].setTag("quick_settings_cache");
        LinearLayout.LayoutParams gridLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, tileHeightDp * 3 + CAPSULE_GAP_DP * 2));
        content.addView(grid, gridLp);
        addFixedVerticalGap(activity, content, CAPSULE_GAP_DP, "quick_settings_gap_grid_wallpaper");
        LinearLayout wallpaperHeader = new LinearLayout(activity);
        wallpaperHeader.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperHeader.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        wallpaperHeader.setPaddingRelative(0, 0, 0, 0);
        TextView wallpaperTitle = new TextView(activity);
        wallpaperTitle.setText(R.string.quick_settings_wallpaper_title);
        wallpaperTitle.setTextSize(12f);
        wallpaperTitle.setTextColor(PANEL_TEXT_PRIMARY);
        wallpaperTitle.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        wallpaperTitle.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        wallpaperTitle.setPaddingRelative(0, 0, 0, 0);
        wallpaperTitle.setTranslationY(dp(density, 2));
        wallpaperTitle.setTag("quick_settings_wallpaper_title");
        wallpaperTitle.setLayoutParams(new LinearLayout.LayoutParams(dp(density, 166),
                ViewGroup.LayoutParams.MATCH_PARENT));
        wallpaperHeader.addView(wallpaperTitle);
        View wallpaperHeaderGap = new View(activity);
        wallpaperHeader.addView(wallpaperHeaderGap, new LinearLayout.LayoutParams(dp(density, 5), 1));
        TextView manageWallpaper = createLink(activity, R.string.quick_settings_manage_wallpaper, 20, density);
        controls[QuickSettingsFocusGraph.WALLPAPER_MANAGE] = manageWallpaper;
        manageWallpaper.setTag("quick_settings_manage_wallpaper");
        wallpaperHeader.addView(manageWallpaper, new LinearLayout.LayoutParams(
                dp(density, 64), dp(density, 20)));
        LinearLayout.LayoutParams wallpaperHeaderLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 22));
        content.addView(wallpaperHeader, wallpaperHeaderLp);
        addFixedVerticalGap(activity, content, CAPSULE_GAP_DP, "quick_settings_gap_wallpaper_header_actions");
        bind(manageWallpaper, dialogHolder, actions::manageWallpaper);

        LinearLayout wallpaperActions = new LinearLayout(activity);
        wallpaperActions.setOrientation(LinearLayout.HORIZONTAL);
        wallpaperActions.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        wallpaperActions.setPaddingRelative(0, 0, 0, 0);
        TextView nextPhoto = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_next_photo,
                null, tileHeightDp, density);
        controls[QuickSettingsFocusGraph.NEXT_PHOTO] = nextPhoto;
        nextPhoto.setTag("quick_settings_next_photo");
        bind(nextPhoto, dialogHolder, actions::nextPhoto);
        LinearLayout.LayoutParams nextLp = new LinearLayout.LayoutParams(dp(density, HALF_TILE_WIDTH_DP),
                ViewGroup.LayoutParams.MATCH_PARENT);
        nextLp.setMargins(0, 0, dp(density, CAPSULE_GAP_DP), 0);
        wallpaperActions.addView(nextPhoto, nextLp);

        TextView interval = createTile(activity, R.drawable.ic_wallpaper,
                R.string.quick_settings_interval_title,
                safe(actions.wallpaperIntervalSummary(), activity.getString(R.string.quick_settings_interval_summary)),
                tileHeightDp, density);
        controls[QuickSettingsFocusGraph.INTERVAL] = interval;
        interval.setTag("quick_settings_interval");
        bind(interval, dialogHolder, actions::chooseWallpaperInterval);
        LinearLayout.LayoutParams intervalLp = new LinearLayout.LayoutParams(dp(density, HALF_TILE_WIDTH_DP),
                ViewGroup.LayoutParams.MATCH_PARENT);
        intervalLp.setMargins(0, 0, 0, 0);
        wallpaperActions.addView(interval, intervalLp);
        LinearLayout.LayoutParams wallpaperActionsLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, tileHeightDp));
        content.addView(wallpaperActions, wallpaperActionsLp);
        addFixedVerticalGap(activity, content, CAPSULE_GAP_DP, "quick_settings_gap_wallpaper_to_settings");
        LinearLayout settingsActions = new LinearLayout(activity);
        settingsActions.setOrientation(LinearLayout.VERTICAL);
        settingsActions.setGravity(Gravity.CENTER_VERTICAL);
        TextView glassSettings = createTile(activity, R.drawable.ic_appearance,
                R.string.quick_settings_glass_settings,
                activity.getString(R.string.quick_settings_glass_settings_summary), tileHeightDp, density);
        controls[QuickSettingsFocusGraph.GLASS_SETTINGS] = glassSettings;
        glassSettings.setTag("quick_settings_glass_settings");
        bind(glassSettings, dialogHolder, actions::openGlassSettings);
        LinearLayout.LayoutParams glassLp = new LinearLayout.LayoutParams(
                dp(density, WIDE_TILE_WIDTH_DP), dp(density, tileHeightDp));
        glassLp.setMargins(0, 0, 0, dp(density, CAPSULE_GAP_DP));
        settingsActions.addView(glassSettings, glassLp);

        TextView androidSettings = createTile(activity, R.drawable.ic_tv_display,
                R.string.quick_settings_android_settings,
                activity.getString(R.string.quick_settings_android_settings_summary), tileHeightDp, density);
        controls[QuickSettingsFocusGraph.ANDROID_SETTINGS] = androidSettings;
        androidSettings.setTag("quick_settings_android_settings");
        bind(androidSettings, dialogHolder, actions::openAndroidSettings);
        LinearLayout.LayoutParams androidLp = new LinearLayout.LayoutParams(
                dp(density, WIDE_TILE_WIDTH_DP), dp(density, tileHeightDp));
        androidLp.setMargins(0, 0, 0, 0);
        settingsActions.addView(androidSettings, androidLp);
        LinearLayout.LayoutParams settingsActionsLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(density, tileHeightDp * 2 + CAPSULE_GAP_DP));
        content.addView(settingsActions, settingsActionsLp);

        QuickSettingsFocusGraph.connect(controls);

        int naturalHeight = measureNaturalHeight(content, width);
        int fixedPanelHeight = dp(density, PANEL_HEIGHT_DP);
        if (naturalHeight > fixedPanelHeight || fixedPanelHeight > availableHeight) {
            android.util.Log.e("TvHome.QuickSettings", "Quick Settings cannot fit with fixed geometry: "
                    + Math.round(naturalHeight / density) + "dp content, "
                    + PANEL_HEIGHT_DP + "dp panel, "
                    + Math.round(availableHeight / density) + "dp available");
            return null;
        }

        // Fixed 320dp panel; content is measured independently and must fit without compression.
        // The remaining 1dp is intentional fixed bottom breathing room, not a weighted spacer.
        int panelHeight = fixedPanelHeight;
        android.util.Log.i("TvHome.QuickSettings", "Fixed panel " + Math.round(width / density)
                + "x" + Math.round(panelHeight / density) + "dp; side inset "
                + PANEL_SIDE_INSET_DP + "dp; capsule width " + WIDE_TILE_WIDTH_DP
                + "dp; capsule height " + tileHeightDp + "dp; all section gaps "
                + CAPSULE_GAP_DP + "dp; vertical inset " + PANEL_VERTICAL_INSET_DP + "dp.");
        final int panelWidth = width;
        final int panelTop = top;
        final int finalPanelLeft = panelLeft;
        content.setLayoutParams(new ViewGroup.LayoutParams(panelWidth, panelHeight));
        content.measure(View.MeasureSpec.makeMeasureSpec(panelWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(panelHeight, View.MeasureSpec.EXACTLY));

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setView(content).create();
        dialogHolder[0] = dialog;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((ignored, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_TV_INPUT) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    dialog.dismiss();
                    actions.openInputs();
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                if (event.getAction() == KeyEvent.ACTION_UP) dialog.dismiss();
                return true;
            }
            return false;
        });
        dialog.setOnDismissListener(d -> {
            Object tag = dialog.getWindow() != null ? dialog.getWindow().getDecorView().getTag() : null;
            if (tag instanceof Glider) {
                Glider glider = (Glider) tag;
                glider.hideNow();
                if (glider.getParent() instanceof ViewGroup) ((ViewGroup) glider.getParent()).removeView(glider);
            }
            if (onDismiss != null) onDismiss.run();
        });
        dialog.setOnShowListener(d -> {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setGravity(Gravity.TOP | Gravity.LEFT);
                window.setLayout(panelWidth, panelHeight);
                WindowManager.LayoutParams params = window.getAttributes();
                params.x = finalPanelLeft;
                params.y = panelTop;
                // Keep the accepted no-dim window behavior; popup separation is handled by
                // the popup's own smoked glass panel, not by dimming the entire Home screen.
                params.dimAmount = 0f;
                window.setAttributes(params);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            View target = controls[initialFocus >= 0 && initialFocus < controls.length
                    ? initialFocus : QuickSettingsFocusGraph.INPUT];
            installDialogGlider(activity, dialog, controls, density);
            content.post(() -> {
                alignWallpaperHeadingToCapsule(wallpaperTitle, nextPhoto, density);
                if (target != null && target.isShown()) target.requestFocus();
            });
        });
        dialog.show();
        return dialog;
    }

    /**
     * Quick Settings owns a dialog-window Glider, using the exact same spring-driven shape as
     * Home app icons. Each capsule paints that one moving shape in its own background pass.
     */
    private static void installDialogGlider(Activity activity, AlertDialog dialog, View[] controls,
                                            float density) {
        Window window = dialog.getWindow();
        if (window == null) return;
        View decor = window.getDecorView();
        if (!(decor instanceof ViewGroup)) return;
        ViewGroup layer = (ViewGroup) decor;
        Glider glider = new Glider(activity, false);
        glider.setStyle(Glider.GLASS);
        // Keep the Quick Settings focus rim slightly finer without changing Home app icons.
        glider.setFocusStrokeWidth(1.5f * density);
        layer.addView(glider, 0, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        decor.setTag(glider);
        for (View control : controls) {
            if (control == null) continue;
            boolean manageCapsule = "quick_settings_manage_wallpaper".equals(control.getTag());
            float cornerRadiusDp = manageCapsule ? 7f : 10f;
            Drawable idle = UniversalGlassMaterial.controlIdle(activity, density, cornerRadiusDp,
                    CAPSULE_IDLE_FILL, CAPSULE_IDLE_BORDER);
            control.setBackground(new LayerDrawable(new Drawable[]{
                    idle, new GliderFocusDrawable(control, glider)
            }));
            glider.addCompanion(control);
            control.setOnFocusChangeListener((view, focused) -> {
                if (!focused || !view.isShown()) return;
                int[] targetAt = new int[2], gliderAt = new int[2];
                view.getLocationInWindow(targetAt);
                glider.getLocationInWindow(gliderAt);
                float left = targetAt[0] - gliderAt[0];
                float top = targetAt[1] - gliderAt[1];
                float pad = dp(density, 2);
                glider.moveTo(left + pad, top + pad,
                        left + view.getWidth() - pad, top + view.getHeight() - pad,
                        density * cornerRadiusDp);
                if (view instanceof TextView) {
                    TextView label = (TextView) view;
                    // Preserve the approved white typography in every focus state. A dark,
                    // tight shadow/halo separates white glyphs from the moving silver glass.
                    label.setTextColor(PANEL_TEXT_PRIMARY);
                    label.setShadowLayer((focused ? 2.0f : 1.25f) * density, 0f,
                            0.5f * density, focused ? 0xF0000000 : 0xCC000000);
                }
            });
        }
    }

    static int panelTopInHost(int anchorScreenTop, int anchorHeight, int hostScreenTop,
                              int topClearancePx) {
        return anchorScreenTop + anchorHeight - hostScreenTop + topClearancePx;
    }

    static int availablePanelHeight(int dockTopInHost, int panelTopInHost, int bottomClearancePx) {
        return dockTopInHost - panelTopInHost - bottomClearancePx;
    }

    /**
     * Aligns the first painted pixel of the Wallpaper heading with the capsule's actual left edge.
     * This runs after layout so it includes the final Dialog window position, parent insets,
     * TextView compound padding, and the runtime Inter font's glyph side-bearing.
     */
    private static void alignWallpaperHeadingToCapsule(TextView heading, View capsule, float density) {
        if (heading == null || capsule == null || !heading.isLaidOut() || !capsule.isLaidOut()) return;
        android.graphics.Rect inkBounds = new android.graphics.Rect();
        String label = heading.getText().toString();
        heading.getPaint().getTextBounds(label, 0, label.length(), inkBounds);

        int[] headingAt = new int[2];
        int[] capsuleAt = new int[2];
        heading.getLocationOnScreen(headingAt);
        capsule.getLocationOnScreen(capsuleAt);
        float paintedLeft = headingAt[0] + heading.getCompoundPaddingLeft() + inkBounds.left;
        float correction = capsuleAt[0] - paintedLeft + dp(density, 2);
        if (Math.abs(correction) >= 0.25f) {
            heading.setTranslationX(heading.getTranslationX() + correction);
        }
        android.util.Log.d("TvHome.QuickSettings", "Wallpaper heading ink-edge correction (+2dp design offset): "
                + correction + "px");
    }

    private static void addFixedVerticalGap(Activity activity, LinearLayout parent, int heightDp, String tag) {
        View spacer = new View(activity);
        spacer.setTag(tag);
        parent.addView(spacer, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(activity.getResources().getDisplayMetrics().density, heightDp)));
    }

    private static int measureNaturalHeight(View content, int widthPx) {
        content.measure(View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return content.getMeasuredHeight();
    }

    private static void setBottomMargin(View child, int marginPx) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        params.bottomMargin = marginPx;
        child.setLayoutParams(params);
    }

    private static View addGridTile(Activity activity, GridLayout grid, int row, int column, int span,
                                    int icon, int titleRes, String titleOverride, String summary, float density,
                                    AlertDialog[] dialogHolder, Runnable action, int tileHeightDp) {
        String title = titleOverride != null ? titleOverride : activity.getString(titleRes);
        TextView tile = createTile(activity, icon, title, summary, tileHeightDp, density);
        bind(tile, dialogHolder, action);
        GridLayout.Spec rowSpec = GridLayout.spec(row, GridLayout.TOP);
        GridLayout.Spec columnSpec = GridLayout.spec(column, span);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
        lp.width = dp(density, span == 3 ? WIDE_TILE_WIDTH_DP : HALF_TILE_WIDTH_DP);
        lp.height = dp(density, tileHeightDp);
        lp.setMargins(0, 0, 0, row < 2 ? dp(density, CAPSULE_GAP_DP) : 0);
        grid.addView(tile, lp);
        return tile;
    }

    private static void addGridColumnGap(Activity activity, GridLayout grid, int row, float density, int tileHeightDp) {
        View gap = new View(activity);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(GridLayout.spec(row, GridLayout.TOP), GridLayout.spec(1));
        lp.width = dp(density, CAPSULE_GAP_DP);
        lp.height = dp(density, tileHeightDp);
        grid.addView(gap, lp);
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
        tile.setPadding(dp(density, 8), dp(density, 2), dp(density, 7), dp(density, 2));
        tile.setMinHeight(dp(density, minHeightDp));
        tile.setMaxLines(summary == null || summary.isEmpty() ? 1 : 2);
        tile.setEllipsize(android.text.TextUtils.TruncateAt.END);
        tile.setTextSize(13f);
        tile.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.NORMAL);
        tile.setTextColor(PANEL_TEXT_PRIMARY);
        String displayTitle = stripDecorativePrefix(title);
        tile.setText(buildLabel(displayTitle, summary));
        tile.setContentDescription(summary == null || summary.isEmpty()
                ? displayTitle : displayTitle + ", " + summary);

        if (iconRes != 0) {
            Drawable icon = activity.getDrawable(iconRes);
            if (icon != null) {
                icon = icon.mutate();
                icon.setTint(PANEL_TEXT_PRIMARY);
                int size = dp(density, 16);
                icon.setBounds(0, 0, size, size);
                tile.setCompoundDrawablesRelative(icon, null, null, null);
                tile.setCompoundDrawablePadding(dp(density, 6));
            }
        }
        TvFocusIndicator.applyRoundedTextFocus(tile, density, 10f,
                CAPSULE_IDLE_FILL, CAPSULE_FOCUS_FILL,
                CAPSULE_IDLE_BORDER, CAPSULE_FOCUS_BORDER,
                PANEL_TEXT_PRIMARY, PANEL_TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
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
        link.setTextSize(12f);
        link.setTypeface(activity.getResources().getFont(R.font.inter), Typeface.BOLD);
        link.setTextColor(PANEL_TEXT_PRIMARY);
        link.setContentDescription(activity.getString(titleRes));
        TvFocusIndicator.applyRoundedTextFocus(link, density, 10f,
                CAPSULE_IDLE_FILL, CAPSULE_FOCUS_FILL,
                CAPSULE_IDLE_BORDER, CAPSULE_FOCUS_BORDER,
                PANEL_TEXT_PRIMARY, PANEL_TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
        return link;
    }

    private static String stripDecorativePrefix(String value) {
        if (value == null) return "";
        int offset = 0;
        while (offset < value.length()) {
            int codePoint = value.codePointAt(offset);
            int type = Character.getType(codePoint);
            boolean decorative = Character.isWhitespace(codePoint)
                    || type == Character.OTHER_SYMBOL
                    || type == Character.MODIFIER_SYMBOL
                    || type == Character.FORMAT
                    || type == Character.NON_SPACING_MARK
                    || type == Character.ENCLOSING_MARK
                    || codePoint == 0xFE0E || codePoint == 0xFE0F || codePoint == 0x200D;
            if (!decorative) break;
            offset += Character.charCount(codePoint);
        }
        return value.substring(offset).trim();
    }

    private static SpannableStringBuilder buildLabel(String title, String summary) {
        SpannableStringBuilder text = new SpannableStringBuilder(title == null ? "" : title);
        if (summary != null && !summary.isEmpty()) {
            int start = text.length();
            text.append("\n").append(summary);
            text.setSpan(new RelativeSizeSpan(0.75f), start, text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new ForegroundColorSpan(PANEL_TEXT_SECONDARY), start, text.length(),
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

        private static String safe(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    private static int settingsIconCenterX(View anchor, int anchorScreenX, float density) {
        int fallback = anchorScreenX + anchor.getWidth() / 2;
        if (!(anchor instanceof TextView)) return fallback;
        TextView settings = (TextView) anchor;
        Drawable start = settings.getCompoundDrawablesRelative()[0];
        if (start == null) return fallback;
        int iconWidth = start.getBounds().width() > 0 ? start.getBounds().width() : dp(density, 20);
        Layout textLayout = settings.getLayout();
        if (textLayout == null || textLayout.getLineCount() == 0) return fallback;
        int textWidth = (int) Math.ceil(textLayout.getLineWidth(0));
        int contentWidth = settings.getWidth() - settings.getPaddingLeft() - settings.getPaddingRight();
        int groupWidth = iconWidth + settings.getCompoundDrawablePadding() + textWidth;
        int groupStart = settings.getPaddingLeft() + Math.max(0, (contentWidth - groupWidth) / 2);
        return anchorScreenX + groupStart + iconWidth / 2;
    }

    private static int dp(float density, int value) {
        return Math.max(1, Math.round(density * value));
    }
}

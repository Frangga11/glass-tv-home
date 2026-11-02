package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Compact wallpaper control popup. Presentation only: all wallpaper state and behavior
 * remain owned by the existing MainActivity wallpaper flow.
 */
final class WallpaperSettingsDialog {
    interface Actions {
        void openCollection();
        void openInterval();
        void showNext();
        boolean isShuffleEnabled();
        String intervalSummary();
        void toggleShuffle();
    }

    private static final int TAB_CHANGE = 0;
    private static final int TAB_SHUFFLE = 1;
    private static final int TAB_NEXT = 2;

    private WallpaperSettingsDialog() { }

    static void show(Activity activity, Actions actions) {
        if (activity == null || actions == null || activity.isFinishing() || activity.isDestroyed()) return;

        final float density = activity.getResources().getDisplayMetrics().density;
        final String language = AppLanguage.getLanguage(activity);
        final boolean english = AppLanguage.LANG_EN.equals(language);
        final boolean malay = !english && !AppLanguage.LANG_ID.equals(language);
        final String[] tabLabels = english
                ? new String[]{"Change", "Shuffle", "Next"}
                : malay
                ? new String[]{"Tukar", "Shuffle", "Seterusnya"}
                : new String[]{"Ganti", "Shuffle", "Berikutnya"};

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(density, 18), dp(density, 14), dp(density, 18), dp(density, 8));

        TextView title = label(activity,
                english ? "Wallpaper" : malay ? "Wallpaper" : "Wallpaper",
                18, true);
        root.addView(title);

        LinearLayout tabs = new LinearLayout(activity);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams tabsParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabsParams.topMargin = dp(density, 10);
        root.addView(tabs, tabsParams);

        LinearLayout page = new LinearLayout(activity);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setGravity(Gravity.CENTER_VERTICAL);
        page.setPadding(dp(density, 16), dp(density, 14), dp(density, 16), dp(density, 10));
        page.setBackground(surface(density, TvUiTokens.SURFACE, TvUiTokens.BORDER,
                TvUiTokens.CARD_RADIUS_DP));
        LinearLayout.LayoutParams pageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        pageParams.topMargin = dp(density, 10);
        root.addView(page, pageParams);

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setView(root)
                .setNegativeButton(AppLanguage.strClose(activity), (d, w) -> d.dismiss())
                .create();

        final TextView[] tabViews = new TextView[3];
        for (int i = 0; i < tabViews.length; i++) {
            final int tab = i;
            TextView tabView = label(activity, tabLabels[i], 13, true);
            tabView.setGravity(Gravity.CENTER);
            tabView.setFocusable(true);
            tabView.setClickable(true);
            tabView.setMinHeight(dp(density, 42));
            tabView.setPadding(dp(density, 8), dp(density, 5), dp(density, 8), dp(density, 5));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            if (i > 0) lp.leftMargin = dp(density, 6);
            tabs.addView(tabView, lp);
            tabViews[i] = tabView;
            tabView.setOnClickListener(v ->
                    render(activity, actions, dialog, page, tabViews, tab, density, english, malay));
        }

        render(activity, actions, dialog, page, tabViews, TAB_CHANGE, density, english, malay);
        dialog.show();
        TvDialogStyle.apply(dialog, activity, 680, 420);
        tabViews[TAB_CHANGE].requestFocus();
    }

    private static void render(Activity activity, Actions actions, AlertDialog dialog,
                               LinearLayout page, TextView[] tabs, int selected, float density,
                               boolean english, boolean malay) {
        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setBackground(tabSurface(density, i == selected));
            tabs[i].setTextColor(TvUiTokens.TEXT_PRIMARY);
            tabs[i].setSelected(i == selected);
            tabs[i].setContentDescription(tabs[i].getText());
        }

        page.removeAllViews();
        if (selected == TAB_CHANGE) {
            addHeading(page, activity, english ? "Choose wallpaper"
                    : malay ? "Pilih wallpaper" : "Pilih wallpaper", density);
            addAction(page, activity, density,
                    english ? "Choose collection" : malay ? "Pilih koleksi" : "Pilih koleksi",
                    () -> dismissAndRun(dialog, actions::openCollection));
        } else if (selected == TAB_SHUFFLE) {
            addHeading(page, activity, english ? "Shuffle"
                    : malay ? "Shuffle" : "Shuffle", density);
            TextView status = label(activity,
                    (actions.isShuffleEnabled()
                            ? (english ? "Random" : malay ? "Rawak" : "Acak")
                            : (english ? "Sequential" : malay ? "Berurutan" : "Berurutan"))
                            + "  •  " + (english ? "Interval" : malay ? "Selang" : "Interval")
                            + ": " + actions.intervalSummary(),
                    13, false);
            status.setTextColor(TvUiTokens.TEXT_SECONDARY);
            status.setPadding(0, 0, 0, dp(density, 12));
            page.addView(status);

            TextView toggle = addAction(page, activity, density,
                    actions.isShuffleEnabled()
                            ? (english ? "Turn Shuffle OFF" : malay ? "Matikan Shuffle" : "Matikan Shuffle")
                            : (english ? "Turn Shuffle ON" : malay ? "Hidupkan Shuffle" : "Aktifkan Shuffle"),
                    () -> {
                        actions.toggleShuffle();
                        render(activity, actions, dialog, page, tabs, TAB_SHUFFLE, density, english, malay);
                        View refreshedToggle = page.getChildAt(2);
                        if (refreshedToggle != null) refreshedToggle.requestFocus();
                    });
            addAction(page, activity, density,
                    english ? "Set interval" : malay ? "Atur selang" : "Atur interval",
                    () -> dismissAndRun(dialog, actions::openInterval));
            if (toggle != null) toggle.setNextFocusDownId(page.getChildAt(3).getId());
        } else {
            addHeading(page, activity, english ? "Next wallpaper"
                    : malay ? "Wallpaper seterusnya" : "Wallpaper berikutnya", density);
            addAction(page, activity, density,
                    english ? "Show next" : malay ? "Papar seterusnya" : "Tampilkan berikutnya",
                    () -> dismissAndRun(dialog, actions::showNext));
        }
    }

    private static void addHeading(LinearLayout page, Activity activity, String text, float density) {
        TextView heading = label(activity, text, 16, true);
        heading.setTextColor(TvUiTokens.TEXT_PRIMARY);
        heading.setPadding(0, 0, 0, dp(density, 12));
        page.addView(heading);
    }

    private static TextView addAction(LinearLayout page, Activity activity, float density,
                                      String text, Runnable action) {
        TextView button = label(activity, text, 14, true);
        button.setGravity(Gravity.CENTER);
        button.setFocusable(true);
        button.setClickable(true);
        button.setMinHeight(dp(density, 44));
        button.setPadding(dp(density, 12), dp(density, 7), dp(density, 12), dp(density, 7));
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_focused},
                surface(density, TvUiTokens.SURFACE_FOCUSED, TvUiTokens.FOCUS_BORDER,
                        TvUiTokens.CARD_RADIUS_DP));
        states.addState(new int[]{},
                surface(density, TvUiTokens.SURFACE, TvUiTokens.BORDER,
                        TvUiTokens.CARD_RADIUS_DP));
        button.setBackground(states);
        button.setTextColor(TvUiTokens.TEXT_PRIMARY);
        button.setOnClickListener(v -> action.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(density, 7);
        page.addView(button, lp);
        return button;
    }

    private static StateListDrawable tabSurface(float density, boolean selected) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_focused},
                surface(density, TvUiTokens.SURFACE_FOCUSED, TvUiTokens.FOCUS_BORDER,
                        TvUiTokens.CARD_RADIUS_DP));
        states.addState(new int[]{},
                surface(density, selected ? TvUiTokens.NAV_ITEM_SELECTED : TvUiTokens.SURFACE,
                        selected ? TvUiTokens.NAV_SELECTION_BORDER : TvUiTokens.BORDER,
                        TvUiTokens.CARD_RADIUS_DP));
        return states;
    }

    private static GradientDrawable surface(float density, int fill, int stroke, float radiusDp) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(fill);
        bg.setCornerRadius(dp(density, Math.round(radiusDp)));
        bg.setStroke(Math.max(1, dp(density, 1)), stroke);
        return bg;
    }

    private static TextView label(Activity activity, String text, float size, boolean bold) {
        TextView view = new TextView(activity);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(TvUiTokens.TEXT_PRIMARY);
        view.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return view;
    }

    private static void dismissAndRun(AlertDialog dialog, Runnable action) {
        dialog.dismiss();
        action.run();
    }

    private static int dp(float density, int value) {
        return Math.round(density * value);
    }
}

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
 * Single entry point for wallpaper controls. The dialog owns presentation only;
 * wallpaper state, selection, rotation and persistence remain in MainActivity.
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
        final float density = activity.getResources().getDisplayMetrics().density;
        final String language = AppLanguage.getLanguage(activity);
        final boolean english = AppLanguage.LANG_EN.equals(language);
        final boolean malay = !english && !AppLanguage.LANG_ID.equals(language);
        final String[] tabLabels = english
                ? new String[]{"Change Wallpaper", "Shuffle Interval", "Next Wallpaper"}
                : malay
                ? new String[]{"Tukar Wallpaper", "Selang Shuffle", "Wallpaper Seterusnya"}
                : new String[]{"Ganti Wallpaper", "Interval Shuffle", "Wallpaper Berikutnya"};

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(density, 22), dp(density, 18), dp(density, 22), dp(density, 12));

        TextView title = label(activity, english ? "Wallpaper Control Center" : malay
                ? "Pusat Kawalan Wallpaper" : "Pusat Kendali Wallpaper", 21, true);
        root.addView(title);

        TextView subtitle = label(activity, english
                ? "Choose, schedule, and preview your Home background."
                : malay ? "Pilih, jadualkan dan pratonton latar Home."
                : "Pilih, atur jadwal, dan kendalikan latar layar utama.", 13, false);
        subtitle.setPadding(0, dp(density, 4), 0, dp(density, 14));
        root.addView(subtitle);

        LinearLayout tabs = new LinearLayout(activity);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setGravity(Gravity.CENTER);
        root.addView(tabs, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout page = new LinearLayout(activity);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setGravity(Gravity.CENTER_VERTICAL);
        page.setPadding(dp(density, 18), dp(density, 18), dp(density, 18), dp(density, 18));
        GradientDrawable pageBg = new GradientDrawable();
        pageBg.setColor(0x332B3444);
        pageBg.setCornerRadius(dp(density, 16));
        pageBg.setStroke(Math.max(1, dp(density, 1)), 0x667F91A8);
        page.setBackground(pageBg);
        LinearLayout.LayoutParams pageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        pageParams.topMargin = dp(density, 14);
        root.addView(page, pageParams);

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setView(root)
                .setNegativeButton(AppLanguage.strClose(activity), (d, w) -> d.dismiss())
                .create();

        final int[] activeTab = {TAB_CHANGE};
        final TextView[] tabViews = new TextView[3];
        for (int i = 0; i < 3; i++) {
            final int tab = i;
            TextView tabView = label(activity, tabLabels[i], 13, true);
            tabView.setGravity(Gravity.CENTER);
            tabView.setFocusable(true);
            tabView.setClickable(true);
            tabView.setMinHeight(dp(density, 54));
            tabView.setPadding(dp(density, 8), dp(density, 8), dp(density, 8), dp(density, 8));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            if (i > 0) lp.leftMargin = dp(density, 6);
            tabs.addView(tabView, lp);
            tabViews[i] = tabView;
            tabView.setOnClickListener(v -> {
                activeTab[0] = tab;
                render(activity, actions, dialog, page, tabViews, activeTab[0], density, english, malay);
            });
        }

        render(activity, actions, dialog, page, tabViews, activeTab[0], density, english, malay);
        dialog.show();
        TvDialogStyle.apply(dialog, activity, 820, 620);
        if (tabViews[0] != null) tabViews[0].requestFocus();
    }

    private static void render(Activity activity, Actions actions, AlertDialog dialog,
                               LinearLayout page, TextView[] tabs, int selected, float density,
                               boolean english, boolean malay) {
        for (int i = 0; i < tabs.length; i++) {
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_focused}, surface(density, 0xFF3B82F6, 0xFFBFDBFE));
            states.addState(new int[]{}, surface(density,
                    i == selected ? 0xFF2563EB : 0x332B3444,
                    i == selected ? 0xFF93C5FD : 0x667F91A8));
            tabs[i].setBackground(states);
            tabs[i].setTextColor(0xFFFFFFFF);
            tabs[i].setSelected(i == selected);
            tabs[i].setContentDescription(tabs[i].getText());
        }

        page.removeAllViews();
        String heading;
        String description;
        if (selected == TAB_CHANGE) {
            heading = english ? "Choose your wallpaper" : malay ? "Pilih wallpaper anda" : "Pilih wallpaper";
            description = english
                    ? "Browse the built-in collection or manage your personal photos. Your current wallpaper changes only after you choose a collection."
                    : malay ? "Pilih koleksi terbina dalam atau urus foto peribadi. Wallpaper berubah selepas anda memilih koleksi."
                    : "Pilih koleksi bawaan atau kelola foto pribadi. Wallpaper berubah setelah koleksi dipilih.";
            addAction(page, activity, density,
                    english ? "Browse Wallpaper Collections" : malay ? "Lihat Koleksi Wallpaper" : "Pilih Koleksi Wallpaper",
                    () -> dismissAndRun(dialog, actions::openCollection));
        } else if (selected == TAB_SHUFFLE) {
            heading = english ? "Shuffle & automatic interval" : malay ? "Shuffle & selang automatik" : "Shuffle & interval otomatis";
            description = (english ? "Current order: " : malay ? "Susunan semasa: " : "Urutan saat ini: ")
                    + (actions.isShuffleEnabled()
                    ? (english ? "Random without repeats" : malay ? "Rawak tanpa ulangan" : "Acak tanpa pengulangan")
                    : (english ? "Sequential" : malay ? "Berurutan" : "Berurutan"))
                    + "\n" + (english ? "Automatic change: " : malay ? "Pertukaran automatik: " : "Pergantian otomatis: ")
                    + actions.intervalSummary();
            addAction(page, activity, density,
                    actions.isShuffleEnabled()
                            ? (english ? "Turn Shuffle OFF" : malay ? "Matikan Shuffle" : "Matikan Shuffle")
                            : (english ? "Turn Shuffle ON" : malay ? "Hidupkan Shuffle" : "Aktifkan Shuffle"),
                    () -> {
                        actions.toggleShuffle();
                        render(activity, actions, dialog, page, tabs, TAB_SHUFFLE, density, english, malay);
                        // Re-rendering the status copy must not discard the remote's focus target.
                        View toggle = page.getChildAt(2);
                        if (toggle != null) toggle.requestFocus();
                    });
            addAction(page, activity, density,
                    english ? "Change Shuffle Interval" : malay ? "Ubah Selang Shuffle" : "Atur Interval Shuffle",
                    () -> dismissAndRun(dialog, actions::openInterval));
        } else {
            heading = english ? "Next wallpaper" : malay ? "Wallpaper seterusnya" : "Wallpaper berikutnya";
            description = english
                    ? "Move to the next image immediately. The existing shuffle order and automatic timer are preserved."
                    : malay ? "Paparkan gambar seterusnya sekarang. Susunan shuffle dan pemasa automatik sedia ada dikekalkan."
                    : "Tampilkan gambar selanjutnya sekarang. Urutan shuffle dan pengatur waktu otomatis tetap dipertahankan.";
            addAction(page, activity, density,
                    english ? "Show Next Wallpaper" : malay ? "Papar Wallpaper Seterusnya" : "Tampilkan Wallpaper Berikutnya",
                    () -> dismissAndRun(dialog, actions::showNext));
        }

        TextView headingView = label(activity, heading, 19, true);
        headingView.setPadding(0, 0, 0, dp(density, 8));
        page.addView(headingView, 0);
        TextView descView = label(activity, description, 14, false);
        descView.setPadding(0, 0, 0, dp(density, 18));
        page.addView(descView, 1);
    }

    private static void addAction(LinearLayout page, Activity activity, float density,
                                  String text, Runnable action) {
        TextView button = label(activity, text, 15, true);
        button.setGravity(Gravity.CENTER);
        button.setFocusable(true);
        button.setClickable(true);
        button.setMinHeight(dp(density, 58));
        button.setPadding(dp(density, 14), dp(density, 10), dp(density, 14), dp(density, 10));
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_focused}, surface(density, 0xFF1E40AF, 0xFFDBEAFE));
        states.addState(new int[]{}, surface(density, 0xFF1D4ED8, 0xFF93C5FD));
        button.setBackground(states);
        button.setOnClickListener(v -> action.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(density, 10);
        page.addView(button, lp);
    }

    private static GradientDrawable surface(float density, int fill, int stroke) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(fill);
        bg.setCornerRadius(dp(density, 12));
        bg.setStroke(Math.max(1, dp(density, 1)), stroke);
        return bg;
    }

    private static TextView label(Activity activity, String text, float size, boolean bold) {
        TextView view = new TextView(activity);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(0xFFFFFFFF);
        view.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        view.setShadowLayer(bold ? 2f : 1f, 0f, 1f, 0xAA000000);
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

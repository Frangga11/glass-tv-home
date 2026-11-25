package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.WeakHashMap;

/**
 * Owns only the standby dashboard's visual composition. The original Compact hierarchy is
 * retained and restored verbatim; the other compositions reuse the same live bound views.
 */
final class StandbyWidgetLayoutController {
    static final String COMPACT = "compact";
    static final String LINEAR = "linear";
    static final String COLUMN = "column";
    static final String BENTANG = "bentang";

    private static final WeakHashMap<LinearLayout, Snapshot> SNAPSHOTS = new WeakHashMap<>();

    private StandbyWidgetLayoutController() { }

    static void apply(Context context, LinearLayout content, float density, String style) {
        if (context == null || content == null) return;
        Snapshot s = SNAPSHOTS.get(content);
        if (s == null) {
            s = new Snapshot(content);
            SNAPSHOTS.put(content, s);
        }
        if (!LINEAR.equals(style) && !COLUMN.equals(style) && !BENTANG.equals(style)) {
            restoreCompact(content, s);
            content.setOrientation(LinearLayout.VERTICAL);
            content.setGravity(Gravity.CENTER_HORIZONTAL);
            content.setPadding(dp(density, 9), dp(density, 8), dp(density, 9), dp(density, 8));
            content.setBackground(UniversalGlassMaterial.homeCapsule(
                    context, density, 26f, 0xEAF8FFFF, 0xEFFFFFFF));
            setSize(content, dp(density, 280), ViewGroup.LayoutParams.WRAP_CONTENT);
            return;
        }

        detachAll(content);
        content.setPadding(dp(density, 14), dp(density, 12), dp(density, 14), dp(density, 12));
        content.setBackground(UniversalGlassMaterial.homeCapsule(
                context, density, COLUMN.equals(style) ? 24f : 34f, 0xEAF8FFFF, 0xEFFFFFFF));
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);

        if (LINEAR.equals(style)) {
            buildLinear(context, content, s, density);
        } else if (COLUMN.equals(style)) {
            buildColumn(context, content, s, density);
        } else {
            buildBentang(context, content, s, density);
        }
    }

    private static void buildLinear(Context c, LinearLayout root, Snapshot s, float d) {
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.CENTER_VERTICAL);
        root.setPadding(dp(d, 18), dp(d, 10), dp(d, 18), dp(d, 10));
        setSize(root, dp(d, 700), dp(d, 104));

        LinearLayout clockDate = row(c, d, Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams left = new LinearLayout.LayoutParams(0, -1, 1.05f);
        clockDate.addView(s.clock, new LinearLayout.LayoutParams(dp(d, 112), -2));
        LinearLayout dateStack = column(c, d, Gravity.CENTER_VERTICAL);
        dateStack.addView(s.date, matchWrap());
        dateStack.addView(s.hijri, matchWrap());
        clockDate.addView(dateStack, new LinearLayout.LayoutParams(0, -2, 1f));
        root.addView(clockDate, left);
        root.addView(separator(c, d, true), new LinearLayout.LayoutParams(dp(d, 1), dp(d, 66)));

        stylePrayer(s.prayer, d, 15f);
        s.prayer.setGravity(Gravity.CENTER);
        s.prayer.setPadding(dp(d, 10), dp(d, 8), dp(d, 10), dp(d, 8));
        s.prayer.setBackground(pill(d, 0xB812DCEB, 0xFF24EAF4, 26));
        root.addView(s.prayer, new LinearLayout.LayoutParams(dp(d, 184), dp(d, 62)));
        root.addView(separator(c, d, true), new LinearLayout.LayoutParams(dp(d, 1), dp(d, 66)));
        styleScheduleHorizontal(s, d, 13f, 16f);
        root.addView(s.schedule, new LinearLayout.LayoutParams(0, dp(d, 72), 1.8f));
    }

    private static void buildColumn(Context c, LinearLayout root, Snapshot s, float d) {
        setSize(root, dp(d, 276), -2);
        LinearLayout header = row(c, d, Gravity.CENTER_VERTICAL);
        header.addView(s.clock, new LinearLayout.LayoutParams(0, -2, 1.05f));
        LinearLayout dates = column(c, d, Gravity.CENTER_VERTICAL);
        dates.addView(s.date, matchWrap());
        dates.addView(s.hijri, matchWrap());
        header.addView(dates, new LinearLayout.LayoutParams(0, -2, 1f));
        root.addView(header, new LinearLayout.LayoutParams(-1, dp(d, 66)));
        root.addView(separator(c, d, false), new LinearLayout.LayoutParams(-1, dp(d, 1)));
        stylePrayer(s.prayer, d, 15f);
        s.prayer.setGravity(Gravity.CENTER);
        s.prayer.setPadding(dp(d, 8), dp(d, 8), dp(d, 8), dp(d, 8));
        s.prayer.setBackground(pill(d, 0xB812DCEB, 0xFF24EAF4, 24));
        LinearLayout.LayoutParams prayerLp = new LinearLayout.LayoutParams(-1, dp(d, 48));
        prayerLp.topMargin = dp(d, 12);
        prayerLp.bottomMargin = dp(d, 10);
        root.addView(s.prayer, prayerLp);
        buildScheduleRows(c, root, s, d);
    }

    private static void buildBentang(Context c, LinearLayout root, Snapshot s, float d) {
        setSize(root, dp(d, 620), -2);
        root.setPadding(dp(d, 22), dp(d, 14), dp(d, 22), dp(d, 14));
        LinearLayout header = row(c, d, Gravity.CENTER);
        header.addView(s.clock, new LinearLayout.LayoutParams(dp(d, 250), -2));
        header.addView(separator(c, d, true), new LinearLayout.LayoutParams(dp(d, 1), dp(d, 72)));
        LinearLayout dates = column(c, d, Gravity.CENTER_VERTICAL);
        dates.addView(s.date, matchWrap());
        dates.addView(s.hijri, matchWrap());
        header.addView(dates, new LinearLayout.LayoutParams(0, -2, 1f));
        root.addView(header, new LinearLayout.LayoutParams(-1, dp(d, 104)));
        View rule = separator(c, d, false);
        LinearLayout.LayoutParams ruleLp = new LinearLayout.LayoutParams(dp(d, 360), dp(d, 1));
        ruleLp.gravity = Gravity.CENTER_HORIZONTAL;
        ruleLp.topMargin = dp(d, 6);
        ruleLp.bottomMargin = dp(d, 12);
        root.addView(rule, ruleLp);
        styleScheduleHorizontal(s, d, 15f, 24f);
        LinearLayout.LayoutParams scheduleLp = new LinearLayout.LayoutParams(-1, dp(d, 82));
        root.addView(s.schedule, scheduleLp);
        stylePrayer(s.prayer, d, 20f);
        s.prayer.setGravity(Gravity.CENTER);
        s.prayer.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams prayerLp = new LinearLayout.LayoutParams(-1, dp(d, 52));
        prayerLp.topMargin = dp(d, 8);
        root.addView(s.prayer, prayerLp);
    }

    private static void buildScheduleRows(Context c, LinearLayout root, Snapshot s, float d) {
        LinearLayout schedule = column(c, d, Gravity.CENTER);
        schedule.setId(R.id.nightstandPrayerSchedule);
        for (int i = 0; i < s.labels.length; i++) {
            LinearLayout line = row(c, d, Gravity.CENTER_VERTICAL);
            TextView label = s.labels[i];
            TextView time = s.times[i];
            label.setTextSize(15f);
            label.setTextColor(0xFFF3FAFF);
            label.setGravity(Gravity.CENTER_VERTICAL);
            time.setTextSize(16f);
            time.setTextColor(0xFFFFFFFF);
            time.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            line.addView(label, new LinearLayout.LayoutParams(0, dp(d, 30), 1f));
            line.addView(time, new LinearLayout.LayoutParams(dp(d, 76), dp(d, 30)));
            schedule.addView(line, new LinearLayout.LayoutParams(-1, dp(d, 31)));
            if (i == s.activePrayerIndex) {
                line.setBackground(pill(d, 0xD900DCE8, 0xFF00E8F2, 14));
                label.setTextColor(0xFFFFFFFF);
                time.setTextColor(0xFFFFFFFF);
            } else {
                line.setBackground(pill(d, 0x223A7895, 0x446EC7E0, 14));
            }
        }
        root.addView(schedule, new LinearLayout.LayoutParams(-1, -2));
    }

    private static void styleScheduleHorizontal(Snapshot s, float d, float labelSize, float timeSize) {
        s.schedule.setOrientation(LinearLayout.HORIZONTAL);
        s.schedule.setGravity(Gravity.CENTER);
        s.schedule.setBackground(pill(d, 0x5531CDE5, 0xAA45EAF4, 30));
        for (int i = 0; i < s.scheduleItems.length; i++) {
            LinearLayout item = s.scheduleItems[i];
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.setBackgroundColor(Color.TRANSPARENT);
            TextView label = s.labels[i];
            TextView time = s.times[i];
            label.setTextSize(labelSize);
            label.setTextColor(0xFFF4FAFF);
            label.setGravity(Gravity.CENTER);
            time.setTextSize(timeSize);
            time.setTextColor(0xFFFFFFFF);
            time.setGravity(Gravity.CENTER);
            item.removeAllViews();
            item.addView(label, matchWrap());
            item.addView(time, matchWrap());
            s.schedule.addView(item, new LinearLayout.LayoutParams(0, -1, 1f));
            if (i < s.scheduleDividers.length) {
                View divider = s.scheduleDividers[i];
                divider.setBackgroundColor(0xAA18DDEB);
                s.schedule.addView(divider, new LinearLayout.LayoutParams(dp(d, 1), dp(d, 42)));
            }
        }
    }

    private static void restoreCompact(LinearLayout content, Snapshot s) {
        detachAll(content);
        for (int i = 0; i < s.scheduleItems.length; i++) {
            LinearLayout item = s.scheduleItems[i];
            item.removeAllViews();
            s.labels[i].setTextSize(s.labelSizes[i]);
            s.labels[i].setTextColor(s.labelColors[i]);
            s.labels[i].setGravity(s.labelGravities[i]);
            s.times[i].setTextSize(s.timeSizes[i]);
            s.times[i].setTextColor(s.timeColors[i]);
            s.times[i].setGravity(s.timeGravities[i]);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.addView(s.labels[i], s.labelParams[i]);
            item.addView(s.times[i], s.timeParams[i]);
        }
        s.schedule.removeAllViews();
        for (int i = 0; i < s.scheduleItems.length; i++) {
            s.schedule.addView(s.scheduleItems[i], s.scheduleItemParams[i]);
            if (i < s.scheduleDividers.length) {
                s.schedule.addView(s.scheduleDividers[i], s.scheduleDividerParams[i]);
            }
        }
        for (int i = 0; i < s.children.length; i++) {
            View child = s.children[i];
            if (child.getParent() instanceof ViewGroup) ((ViewGroup) child.getParent()).removeView(child);
            content.addView(child, s.childParams[i]);
        }
        s.restoreClockDateHijriPrayer();
        content.setOrientation(s.orientation);
        content.setGravity(s.gravity);
    }

    private static void detachAll(ViewGroup group) {
        for (int i = group.getChildCount() - 1; i >= 0; i--) group.removeViewAt(i);
    }

    private static LinearLayout row(Context c, float d, int gravity) {
        LinearLayout v = new LinearLayout(c);
        v.setOrientation(LinearLayout.HORIZONTAL);
        v.setGravity(gravity);
        return v;
    }

    private static LinearLayout column(Context c, float d, int gravity) {
        LinearLayout v = new LinearLayout(c);
        v.setOrientation(LinearLayout.VERTICAL);
        v.setGravity(gravity);
        return v;
    }

    private static View separator(Context c, float d, boolean vertical) {
        View v = new View(c);
        v.setBackgroundColor(0x8868DDEB);
        return v;
    }

    private static GradientDrawable pill(float d, int fill, int stroke, int radius) {
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setColor(fill);
        bg.setStroke(Math.max(1, dp(d, 1)), stroke);
        bg.setCornerRadius(dp(d, radius));
        return bg;
    }

    private static void stylePrayer(TextView v, float d, float size) {
        v.setTextSize(size);
        v.setTextColor(0xFFF5FBFF);
        v.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        v.setMaxLines(2);
        v.setEllipsize(android.text.TextUtils.TruncateAt.END);
    }

    private static LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    private static int dp(float d, float value) { return Math.round(d * value); }

    private static void setSize(View v, int width, int height) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp instanceof FrameLayout.LayoutParams) {
            lp.width = width;
            lp.height = height;
            v.setLayoutParams(lp);
        } else if (lp instanceof LinearLayout.LayoutParams) {
            lp.width = width;
            lp.height = height;
            v.setLayoutParams(lp);
        }
    }

    private static final class Snapshot {
        final View[] children;
        final ViewGroup.LayoutParams[] childParams;
        final int orientation;
        final int gravity;
        final TextView prayer, date, hijri, clock;
        final LinearLayout schedule;
        final LinearLayout[] scheduleItems = new LinearLayout[5];
        final View[] scheduleDividers = new View[4];
        final TextView[] labels = new TextView[5];
        final TextView[] times = new TextView[5];
        final ViewGroup.LayoutParams[] scheduleItemParams = new ViewGroup.LayoutParams[5];
        final ViewGroup.LayoutParams[] scheduleDividerParams = new ViewGroup.LayoutParams[4];
        final ViewGroup.LayoutParams[] labelParams = new ViewGroup.LayoutParams[5];
        final ViewGroup.LayoutParams[] timeParams = new ViewGroup.LayoutParams[5];
        final float[] labelSizes = new float[5], timeSizes = new float[5];
        final int[] labelColors = new int[5], timeColors = new int[5];
        final int[] labelGravities = new int[5], timeGravities = new int[5];
        final int activePrayerIndex = 3;

        Snapshot(LinearLayout content) {
            orientation = content.getOrientation();
            gravity = content.getGravity();
            children = new View[content.getChildCount()];
            childParams = new ViewGroup.LayoutParams[content.getChildCount()];
            for (int i = 0; i < content.getChildCount(); i++) {
                children[i] = content.getChildAt(i);
                childParams[i] = children[i].getLayoutParams();
            }
            prayer = content.findViewById(R.id.nightstandPrayer);
            date = content.findViewById(R.id.nightstandDate);
            hijri = content.findViewById(R.id.nightstandHijri);
            clock = content.findViewById(R.id.nightstandClock);
            schedule = content.findViewById(R.id.nightstandPrayerSchedule);
            int[] labelIds = {R.id.nightstandFajrLabel,R.id.nightstandDhuhrLabel,R.id.nightstandAsrLabel,R.id.nightstandMaghribLabel,R.id.nightstandIshaLabel};
            int[] timeIds = {R.id.nightstandFajrTime,R.id.nightstandDhuhrTime,R.id.nightstandAsrTime,R.id.nightstandMaghribTime,R.id.nightstandIshaTime};
            int item = 0, divider = 0;
            for (int i = 0; i < schedule.getChildCount(); i++) {
                View child = schedule.getChildAt(i);
                if (child instanceof LinearLayout && item < 5) {
                    scheduleItems[item] = (LinearLayout) child;
                    scheduleItemParams[item] = child.getLayoutParams();
                    labels[item] = child.findViewById(labelIds[item]);
                    times[item] = child.findViewById(timeIds[item]);
                    labelParams[item] = labels[item].getLayoutParams();
                    timeParams[item] = times[item].getLayoutParams();
                    labelSizes[item] = labels[item].getTextSize() / content.getResources().getDisplayMetrics().scaledDensity;
                    timeSizes[item] = times[item].getTextSize() / content.getResources().getDisplayMetrics().scaledDensity;
                    labelColors[item] = labels[item].getCurrentTextColor();
                    timeColors[item] = times[item].getCurrentTextColor();
                    labelGravities[item] = labels[item].getGravity();
                    timeGravities[item] = times[item].getGravity();
                    item++;
                } else if (divider < 4) {
                    scheduleDividers[divider] = child;
                    scheduleDividerParams[divider] = child.getLayoutParams();
                    divider++;
                }
            }
        }

        void restoreClockDateHijriPrayer() {
            // Original XML attributes are restored explicitly after alternate compositions.
            clock.setTextSize(48f);
            date.setTextSize(12f);
            hijri.setTextSize(9f);
            prayer.setTextSize(9f);
            clock.setGravity(Gravity.CENTER);
            date.setGravity(Gravity.CENTER);
            hijri.setGravity(Gravity.CENTER);
        }
    }
}

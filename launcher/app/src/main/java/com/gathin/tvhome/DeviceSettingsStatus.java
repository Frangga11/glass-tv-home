package com.gathin.tvhome;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Read-only status helpers for the device controls shown in Settings. */
final class DeviceSettingsStatus {
    private DeviceSettingsStatus() {}

    static List<ResolveInfo> getHomeActivities(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .addCategory(Intent.CATEGORY_DEFAULT);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> queried = pm.queryIntentActivities(home, PackageManager.MATCH_DEFAULT_ONLY);
        List<ResolveInfo> unique = new ArrayList<>();
        Set<String> packages = new HashSet<>();
        if (queried != null) {
            for (ResolveInfo info : queried) {
                if (info == null || info.activityInfo == null) continue;
                String packageName = info.activityInfo.packageName;
                if (packageName != null && packages.add(packageName)) unique.add(info);
            }
        }
        return unique;
    }

    static ResolveInfo getResolvedHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .addCategory(Intent.CATEGORY_DEFAULT);
        return context.getPackageManager().resolveActivity(home, PackageManager.MATCH_DEFAULT_ONLY);
    }

    static boolean isAccessibilityServiceEnabled(Context context, Class<?> serviceClass) {
        ComponentName component = new ComponentName(context, serviceClass);
        String enabled = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (!containsComponent(enabled, component)) return false;
        return Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
    }

    static boolean isNotificationListenerEnabled(Context context, Class<?> serviceClass) {
        ComponentName component = new ComponentName(context, serviceClass);
        String enabled = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        return containsComponent(enabled, component);
    }

    private static boolean containsComponent(String flattenedList, ComponentName expected) {
        if (flattenedList == null || flattenedList.isEmpty()) return false;
        for (String item : flattenedList.split(":")) {
            ComponentName parsed = ComponentName.unflattenFromString(item);
            if (expected.equals(parsed)) return true;
        }
        return false;
    }

    static String getDisplayLabel(PackageManager pm, ResolveInfo info) {
        if (info == null || info.activityInfo == null) return null;
        CharSequence label = info.loadLabel(pm);
        if (label != null && label.length() > 0) return label.toString();
        return info.activityInfo.packageName;
    }
}

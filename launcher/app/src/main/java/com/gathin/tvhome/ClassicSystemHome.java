package com.gathin.tvhome;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Capability-based system HOME discovery copied from Home Universal Classic. */
final class ClassicSystemHome {
    private ClassicSystemHome() {}

    static ActivityInfo resolve(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent home = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> candidates = new ArrayList<>(pm.queryIntentActivities(home, 0));
        ResolveInfo preferred = pm.resolveActivity(home, PackageManager.MATCH_DEFAULT_ONLY);
        if (preferred != null) candidates.add(0, preferred);
        for (ResolveInfo result : candidates) {
            ActivityInfo info = result.activityInfo;
            if (info == null || info.packageName.equals(context.getPackageName())
                    || !info.enabled || !info.exported) continue;
            String name = info.name.toLowerCase(Locale.ROOT);
            if (name.contains("fallback") || name.contains("recovery") || name.contains("setup")) continue;
            if ((info.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) {
                return info;
            }
        }
        return null;
    }

    static Intent launchIntent(ActivityInfo info) {
        return new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .setComponent(new ComponentName(info.packageName, info.name))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    }
}

package com.gathin.tvhome;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RecentAppsManager {
    private static final String TAG = "RecentAppsManager";
    private static final String PREFS_NAME = "recent_apps_history";
    private static final String KEY_HISTORY = "recent_packages";

    private static final Set<String> SYSTEM_PACKAGES_FILTER = new HashSet<>(Arrays.asList(
            "android",
            "com.android.systemui",
            "com.android.keychain",
            "com.android.shell",
            "com.android.networkstack.process",
            "com.android.se",
            "com.google.android.inputmethod.latin",
            "com.google.android.tts",
            "com.google.android.katniss",
            "com.google.android.ext.services",
            "com.google.android.permissioncontroller",
            "com.google.android.gms",
            "com.google.android.gms.unstable",
            "com.mi.bluetooth",
            "com.droidlogic",
            "com.dolby.android.audio.service",
            "com.android.providers.media",
            "com.android.providers.calendar",
            "com.android.providers.tv",
            "com.android.externalstorage"
    ));

    public static class MemoryStats {
        public final long totalBytes;
        public final long usedBytes;
        public final long availBytes;
        public final int percentUsed;
        public final String formattedText;

        public MemoryStats(long total, long used, long avail, int percent, String formatted) {
            this.totalBytes = total;
            this.usedBytes = used;
            this.availBytes = avail;
            this.percentUsed = percent;
            this.formattedText = formatted;
        }
    }

    public static MemoryStats getMemoryStats(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return new MemoryStats(0, 0, 0, 0, "RAM info unavailable");
        }
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        long total = mi.totalMem;
        long avail = mi.availMem;
        long used = total - avail;
        int percent = total > 0 ? (int) ((used * 100) / total) : 0;
        double usedGb = used / (1024.0 * 1024.0 * 1024.0);
        double totalGb = total / (1024.0 * 1024.0 * 1024.0);
        String text = String.format(Locale.US, "RAM %.1f GB used of %.1f GB", usedGb, totalGb);
        return new MemoryStats(total, used, avail, percent, text);
    }

    public static void recordAppLaunch(Context context, String packageName) {
        if (packageName == null || packageName.equals(context.getPackageName())) return;
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String saved = sp.getString(KEY_HISTORY, "");
            List<String> list = new ArrayList<>();
            list.add(packageName);
            if (!saved.isEmpty()) {
                String[] parts = saved.split(",");
                for (String p : parts) {
                    if (!p.isEmpty() && !p.equals(packageName) && list.size() < 25) {
                        list.add(p);
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(list.get(i));
            }
            sp.edit().putString(KEY_HISTORY, sb.toString()).apply();
        } catch (Exception e) {
            Log.w(TAG, "Failed to record app launch", e);
        }
    }

    public static List<RecentAppItem> getRecentApps(Context context) {
        List<RecentAppItem> result = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (pm == null || am == null) return result;

        String selfPackage = context.getPackageName();
        LinkedHashSet<String> candidatePackages = new LinkedHashSet<>();

        // 1. Try UsageStatsManager (Most accurate for recently used order)
        try {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (usm != null) {
                long now = System.currentTimeMillis();
                List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 86400000L * 3, now);
                if (stats != null && !stats.isEmpty()) {
                    List<UsageStats> sorted = new ArrayList<>(stats);
                    Collections.sort(sorted, (a, b) -> Long.compare(b.getLastTimeUsed(), a.getLastTimeUsed()));
                    for (UsageStats s : sorted) {
                        if (isValidUserApp(s.getPackageName(), selfPackage)) {
                            candidatePackages.add(s.getPackageName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "UsageStatsManager query failed", e);
        }

        // 2. Try parsing dumpsys activity recents if possible
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"dumpsys", "activity", "recents"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("cmp=")) {
                    int start = line.indexOf("cmp=") + 4;
                    int end = line.indexOf("/", start);
                    if (end > start) {
                        String pkg = line.substring(start, end).trim();
                        if (isValidUserApp(pkg, selfPackage)) {
                            candidatePackages.add(pkg);
                        }
                    }
                }
            }
            reader.close();
            p.destroy();
        } catch (Exception ignored) {}

        // 3. Add running app processes
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.pkgList != null) {
                    for (String pkg : processInfo.pkgList) {
                        if (isValidUserApp(pkg, selfPackage)) {
                            candidatePackages.add(pkg);
                        }
                    }
                }
            }
        }

        // 4. Add saved recent app history from launcher launches
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String saved = sp.getString(KEY_HISTORY, "");
            if (!saved.isEmpty()) {
                String[] parts = saved.split(",");
                for (String pkg : parts) {
                    if (isValidUserApp(pkg, selfPackage)) {
                        candidatePackages.add(pkg);
                    }
                }
            }
        } catch (Exception ignored) {}

        // 5. Fallback: If still few items, add top installed Leanback apps
        if (candidatePackages.size() < 3) {
            try {
                Intent leanbackIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
                List<ResolveInfo> activities = pm.queryIntentActivities(leanbackIntent, 0);
                for (ResolveInfo ri : activities) {
                    if (ri.activityInfo != null) {
                        String pkg = ri.activityInfo.packageName;
                        if (isValidUserApp(pkg, selfPackage)) {
                            candidatePackages.add(pkg);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        // Always ensure Settings is accessible if candidate list has room
        candidatePackages.add("com.android.tv.settings");

        // 6. Resolve labels, icons, and intents
        for (String pkg : candidatePackages) {
            try {
                Intent launchIntent = pm.getLeanbackLaunchIntentForPackage(pkg);
                if (launchIntent == null) {
                    launchIntent = pm.getLaunchIntentForPackage(pkg);
                }
                if (launchIntent == null && "com.android.tv.settings".equals(pkg)) {
                    launchIntent = new Intent(Settings.ACTION_SETTINGS);
                }
                if (launchIntent == null) continue;

                ApplicationInfo appInfo = pm.getApplicationInfo(pkg, 0);
                CharSequence label = appInfo.loadLabel(pm);
                Drawable icon = appInfo.loadIcon(pm);

                result.add(new RecentAppItem(pkg, String.valueOf(label), icon, launchIntent));
            } catch (PackageManager.NameNotFoundException ignored) {
            } catch (Exception e) {
                Log.w(TAG, "Error resolving app: " + pkg, e);
            }
        }

        return result;
    }

    public static void killApp(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty()) return;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am != null) {
                am.killBackgroundProcesses(packageName);
                Log.i(TAG, "Killed background processes for: " + packageName);
            }
            SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String saved = sp.getString(KEY_HISTORY, "");
            if (!saved.isEmpty()) {
                String[] parts = saved.split(",");
                StringBuilder sb = new StringBuilder();
                for (String p : parts) {
                    if (!p.equals(packageName) && !p.isEmpty()) {
                        if (sb.length() > 0) sb.append(",");
                        sb.append(p);
                    }
                }
                sp.edit().putString(KEY_HISTORY, sb.toString()).apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to kill app: " + packageName, e);
        }
    }

    public static void closeAll(Context context, List<RecentAppItem> items) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (items != null && am != null) {
            for (RecentAppItem item : items) {
                if (!item.isCloseAll && !item.packageName.isEmpty()) {
                    try {
                        am.killBackgroundProcesses(item.packageName);
                    } catch (Exception ignored) {}
                }
            }
        }
        try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
        } catch (Exception ignored) {}
    }

    private static boolean isValidUserApp(String pkg, String selfPackage) {
        if (pkg == null || pkg.isEmpty() || pkg.equals(selfPackage)) return false;
        if (SYSTEM_PACKAGES_FILTER.contains(pkg)) return false;
        if (pkg.startsWith("com.android.internal")) return false;
        return true;
    }
}

package com.gathin.tvhome;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Applies the approved launcher-scoped parent-PIN policy to selected Android Settings pages.
 * This does not provide system-wide parental control outside Glass TV Home entry points.
 */
final class SystemSettingsAccess {
    private SystemSettingsAccess() {}

    static boolean requiresParentPin(String action) {
        return Settings.ACTION_SETTINGS.equals(action)
                || Settings.ACTION_ACCESSIBILITY_SETTINGS.equals(action)
                || Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS.equals(action)
                || Settings.ACTION_HOME_SETTINGS.equals(action)
                || Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS.equals(action);
    }

    static boolean open(Context context, Intent... intents) {
        if (context == null || intents == null || intents.length == 0) return false;
        return openFrom(context, intents, 0, false);
    }

    private static boolean openFrom(Context context, Intent[] intents, int index, boolean authorized) {
        if (index >= intents.length || intents[index] == null) return false;

        Intent intent = intents[index];
        boolean childMode = ChildModeManager.isChildModeEnabled(context);
        if (!authorized && childMode && requiresParentPin(intent.getAction())) {
            ChildModeManager.promptPin(context,
                    context.getString(R.string.verify_parent_pin_title),
                    context.getString(R.string.android_settings_summary),
                    new ChildModeManager.PinCallback() {
                        @Override public void onSuccess() {
                            openFrom(context, intents, index, true);
                        }

                        @Override public void onCancelled() {
                            // Cancellation is intentionally a no-op: do not open the system page.
                        }
                    });
            return true;
        }

        try {
            Intent launch = new Intent(intent);
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launch);
            return true;
        } catch (Exception unavailable) {
            // Preserve one successful PIN authorization while trying firmware fallbacks.
            return openFrom(context, intents, index + 1, authorized);
        }
    }
}

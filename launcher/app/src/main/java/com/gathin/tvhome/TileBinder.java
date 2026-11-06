package com.gathin.tvhome;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * RecyclerView-independent tile presentation and activation behavior.
 * A Dock renderer and an App Grid renderer can share this binder without sharing positions,
 * layout geometry, or adapter state.
 */
final class TileBinder {
    private final Context context;

    TileBinder(Context context) {
        this.context = context;
    }

    void bind(AppAdapter.Holder holder, AppAdapter.Entry entry,
              boolean showName, boolean iconAnimationsEnabled) {
        holder.name.setText(entry.name);
        holder.name.setVisibility(showName ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        boolean focused = holder.card.isFocused();
        holder.name.setAlpha(focused ? 1f : 0f);
        holder.card.setScaleX(focused && iconAnimationsEnabled ? AppAdapter.FOCUS_SCALE : 1f);
        holder.card.setScaleY(focused && iconAnimationsEnabled ? AppAdapter.FOCUS_SCALE : 1f);
        holder.name.setTranslationY(0f);
        if (entry.banner != null) {
            holder.banner.setImageDrawable(entry.banner);
            holder.banner.setVisibility(android.view.View.VISIBLE);
            holder.icon.setVisibility(android.view.View.GONE);
            holder.label.setVisibility(android.view.View.GONE);
        } else {
            holder.banner.setImageDrawable(null);
            holder.banner.setVisibility(android.view.View.GONE);
            holder.icon.setImageDrawable(entry.icon);
            holder.icon.setVisibility(android.view.View.VISIBLE);
            holder.label.setText(entry.label);
            holder.label.setVisibility(android.view.View.VISIBLE);
        }
    }

    void launch(AppAdapter.Entry entry) {
        SoundEffects.playSelect();
        if (entry.kind == AppAdapter.Entry.HOME_SETTINGS) {
            context.startActivity(new Intent(context, HomeSettingsActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return;
        }
        if (ChildModeManager.isAppLocked(context, entry.pkg)) {
            Context localized = LocaleContext.wrap(context);
            ChildModeManager.promptPin(localized,
                    localized.getString(R.string.locked_app_title),
                    localized.getString(R.string.enter_pin_to_open_app, entry.label),
                    new ChildModeManager.PinCallback() {
                        @Override
                        public void onSuccess() {
                            performLaunch(entry);
                        }

                        @Override
                        public void onCancelled() { }
                    });
            return;
        }
        performLaunch(entry);
    }

    private void performLaunch(AppAdapter.Entry entry) {
        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
                .setComponent(entry.component)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try {
            RecentAppsManager.recordAppLaunch(context, entry.pkg);
            context.startActivity(intent);
        } catch (Exception ex) {
            TvFeedback.show(context, context.getString(R.string.cant_open, entry.label), Toast.LENGTH_SHORT);
        }
    }
}

package com.gathin.tvhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Modern floating Glass TV Settings modal matching the Apple TV / modern TV OS 2-column UI.
 * Features:
 * - Left column: Search, Profile & User card, and categorized navigation tabs.
 * - Right column: Dynamic content pane (Multi-user profiles, 7 Animal Avatars, Home, etc.).
 * - TV & Sistem: Displays native 4K Ultra HD panel resolution (3840x2160) & UI scale.
 * - Anti-clipping: Zero crop on focus borders and selection items across all TV screens.
 * - All Candy Box customizations consolidated here.
 */
public final class ModernSettingsDialog {

    private static final int TAB_PROFILE = NavigationState.TAB_PROFILE;
    private static final int TAB_HOME_SCREEN = NavigationState.TAB_HOME_SCREEN;
    private static final int TAB_APPEARANCE = NavigationState.TAB_APPEARANCE;
    private static final int TAB_STATUS_WEATHER = NavigationState.TAB_STATUS_WEATHER;
    private static final int TAB_EFFECTS = NavigationState.TAB_EFFECTS;
    private static final int TAB_STANDBY = NavigationState.TAB_STANDBY;
    private static final int TAB_DEVICE_SETTINGS = NavigationState.TAB_DEVICE_SETTINGS;

    private static Runnable sDpadLeftTarget;
    private static final String PREF_SETTINGS_FOCUS_AREA = "settings_focus_area";
    private static final String PREF_SETTINGS_FOCUS_PATH = "settings_focus_path";
    private static final String PREF_SETTINGS_FOCUS_TAB = "settings_focus_tab";
    private static final String PREF_SETTINGS_FOCUS_SIGNATURE = "settings_focus_signature";
    private static final String PREF_SETTINGS_LEFT_SCROLL = "settings_left_scroll";

    private static String rightScrollKey(int tab) {
        return "settings_right_scroll_" + NavigationState.normalizeSettingsTab(tab);
    }

    private static boolean isDescendantOf(ViewGroup ancestor, View view) {
        if (ancestor == null || view == null) return false;
        View current = view;
        while (current != null) {
            if (current == ancestor) return true;
            ViewParent parent = current.getParent();
            current = parent instanceof View ? (View) parent : null;
        }
        return false;
    }

    private static String focusPath(ViewGroup ancestor, View target) {
        if (ancestor == null || target == null || target == ancestor) return null;
        ArrayList<Integer> reversed = new ArrayList<>();
        View current = target;
        while (current != ancestor) {
            ViewParent parent = current.getParent();
            if (!(parent instanceof ViewGroup)) return null;
            ViewGroup group = (ViewGroup) parent;
            int index = group.indexOfChild(current);
            if (index < 0) return null;
            reversed.add(index);
            current = group;
        }
        int[] indexes = new int[reversed.size()];
        for (int i = 0; i < reversed.size(); i++) {
            indexes[i] = reversed.get(reversed.size() - 1 - i);
        }
        return SettingsFocusPath.encode(indexes);
    }

    private static String focusSignature(View target) {
        if (target == null) return "";
        String label = "";
        if (target instanceof EditText) {
            CharSequence hint = ((EditText) target).getHint();
            if (hint != null) label = hint.toString();
        } else if (target instanceof TextView) {
            CharSequence text = ((TextView) target).getText();
            if (text != null) label = text.toString();
        }
        if (label.isEmpty() && target.getContentDescription() != null) {
            label = target.getContentDescription().toString();
        }
        if (label.length() > 160) label = label.substring(0, 160);
        return target.getClass().getName() + "#" + target.getId() + "#" + label;
    }

    private static View resolveFocusPath(ViewGroup root, String encodedPath, String signature) {
        int[] indexes = SettingsFocusPath.decode(encodedPath);
        if (root == null || indexes == null || signature == null || signature.isEmpty()) return null;
        View current = root;
        for (int index : indexes) {
            if (!(current instanceof ViewGroup)) return null;
            ViewGroup group = (ViewGroup) current;
            if (index < 0 || index >= group.getChildCount()) return null;
            current = group.getChildAt(index);
        }
        return current.isFocusable() && current.isEnabled() && current.isShown()
                && signature.equals(focusSignature(current)) ? current : null;
    }

    private static boolean focusRightPane(ViewGroup container) {
        if (container == null) return false;
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child.isFocusable() && child.getVisibility() == View.VISIBLE) {
                child.requestFocus();
                return true;
            }
            if (child instanceof ViewGroup) {
                if (focusRightPane((ViewGroup) child)) return true;
            }
        }
        return false;
    }

    public static void show(MainActivity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        SoundEffects.playSelect();

        float density = activity.getResources().getDisplayMetrics().density;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        Typeface outfit = activity.getResources().getFont(R.font.inter);

        AlertDialog[] dialogHolder = new AlertDialog[1];
        android.content.SharedPreferences navigationPrefs = activity.getSharedPreferences(NavigationState.PREFS, Context.MODE_PRIVATE);
        int[] activeTab = new int[]{NavigationState.normalizeSettingsTab(navigationPrefs.getInt(NavigationState.SETTINGS_TAB, TAB_PROFILE))};

        // Main Horizontal Container (2 Columns)
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.CENTER);
        root.setClipChildren(false);
        root.setClipToPadding(false);

        // Dark matte rounded container matching screenshot
        GradientDrawable dialogBg = new GradientDrawable();
        dialogBg.setColor(TvUiTokens.SHELL);
        dialogBg.setCornerRadius(24 * density);
        dialogBg.setStroke((int) (1.5f * density), 0xFF2F323B);
        root.setBackground(dialogBg);
        root.setClipToOutline(true);

        // --- LEFT COLUMN: Sidebar ---
        LinearLayout leftCol = new LinearLayout(activity);
        leftCol.setOrientation(LinearLayout.VERTICAL);
        int leftWidth = (int) (250 * density);
        LinearLayout.LayoutParams leftLp = new LinearLayout.LayoutParams(leftWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        leftCol.setLayoutParams(leftLp);
        leftCol.setPadding((int) (14 * density), (int) (12 * density), (int) (14 * density), (int) (12 * density));
        leftCol.setClipChildren(false);
        leftCol.setClipToPadding(false);

        // Subtle dark translucent background for left pane
        GradientDrawable leftBg = new GradientDrawable();
        leftBg.setColor(0x5514161C);
        leftCol.setBackground(leftBg);

        final LinearLayout profileCard = new LinearLayout(activity);
        final LinearLayout rightContent = new LinearLayout(activity);

        // 1. Search Bar (Optimized height & margins to prevent clipping)
        LinearLayout searchBox = new LinearLayout(activity);
        searchBox.setOrientation(LinearLayout.HORIZONTAL);
        searchBox.setGravity(Gravity.CENTER_VERTICAL);
        searchBox.setPadding((int) (12 * density), 0, (int) (12 * density), 0);
        searchBox.setFocusable(true);
        searchBox.setClickable(true);

        GradientDrawable searchBg = new GradientDrawable();
        searchBg.setColor(TvUiTokens.INPUT_SURFACE);
        searchBg.setCornerRadius(10 * density);
        searchBox.setBackground(searchBg);

        ImageView searchIcon = new ImageView(activity);
        searchIcon.setImageResource(R.drawable.ic_search);
        searchIcon.setColorFilter(0xFF8E929E);
        LinearLayout.LayoutParams sIconLp = new LinearLayout.LayoutParams((int) (15 * density), (int) (15 * density));
        sIconLp.setMarginEnd((int) (8 * density));
        searchBox.addView(searchIcon, sIconLp);

        TextView searchPrompt = new TextView(activity);
        searchPrompt.setText(AppLanguage.strSettingsSearch(activity));
        searchPrompt.setTextSize(12.5f);
        searchPrompt.setTextColor(0xFF8E929E);
        searchPrompt.setTypeface(outfit, Typeface.NORMAL);
        searchBox.addView(searchPrompt);

        searchBox.setOnFocusChangeListener((v, hasFocus) -> {
            GradientDrawable g = new GradientDrawable();
            g.setColor(hasFocus ? TvUiTokens.SURFACE_FOCUSED : TvUiTokens.INPUT_SURFACE);
            g.setCornerRadius(10 * density);
            if (hasFocus) g.setStroke((int) (2 * density), 0xFFFFFFFF);
            v.setBackground(g);
            searchPrompt.setTextColor(hasFocus ? 0xFFFFFFFF : 0xFF8E929E);
            searchIcon.setColorFilter(hasFocus ? 0xFFFFFFFF : 0xFF8E929E);
            v.animate().scaleX(hasFocus ? 1.015f : 1.0f).scaleY(hasFocus ? 1.015f : 1.0f).setDuration(120).start();
        });

        // The real search action is attached after the settings tabs are registered.

        searchBox.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    profileCard.requestFocus();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return focusRightPane(rightContent);
                }
            }
            return false;
        });

        LinearLayout.LayoutParams sbLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (36 * density));
        sbLp.topMargin = (int) (2 * density);
        sbLp.bottomMargin = (int) (8 * density);
        leftCol.addView(searchBox, sbLp);

        // 2. Profile Card (User Profiles - No mention of backup)
        profileCard.setOrientation(LinearLayout.HORIZONTAL);
        profileCard.setGravity(Gravity.CENTER_VERTICAL);
        profileCard.setPadding((int) (10 * density), (int) (6 * density), (int) (10 * density), (int) (6 * density));
        profileCard.setFocusable(true);
        profileCard.setClickable(true);

        ImageView profileAvatarView = new ImageView(activity);
        ProfileManager.UserProfile sidebarProfile = ProfileManager.getActiveProfile(activity);
        profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, sidebarProfile,
                Math.round(38 * density), ChildModeManager.isChildModeEnabled(activity, sidebarProfile.id)));
        LinearLayout.LayoutParams pavLp = new LinearLayout.LayoutParams((int) (38 * density), (int) (38 * density));
        pavLp.setMarginEnd((int) (10 * density));
        profileCard.addView(profileAvatarView, pavLp);

        LinearLayout profileTextCol = new LinearLayout(activity);
        profileTextCol.setOrientation(LinearLayout.VERTICAL);

        TextView tvProfileName = new TextView(activity);
        tvProfileName.setText(ProfileManager.getProfileName(activity));
        tvProfileName.setTextSize(14f);
        tvProfileName.setTextColor(0xFFFFFFFF);
        tvProfileName.setTypeface(outfit, Typeface.BOLD);
        tvProfileName.setSingleLine(true);
        profileTextCol.addView(tvProfileName);

        TextView tvProfileSub = new TextView(activity);
        tvProfileSub.setText(AppLanguage.strProfileSubtitle(activity));
        tvProfileSub.setTextSize(11f);
        tvProfileSub.setTextColor(0xFF8E929E);
        tvProfileSub.setTypeface(outfit, Typeface.NORMAL);
        profileTextCol.addView(tvProfileSub);

        profileCard.addView(profileTextCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        LinearLayout.LayoutParams pcLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (52 * density));
        pcLp.bottomMargin = (int) (8 * density);
        leftCol.addView(profileCard, pcLp);

        // Vertical divider between Left and Right
        View verticalDivider = new View(activity);
        verticalDivider.setBackgroundColor(0xFF262832);
        LinearLayout.LayoutParams divLp = new LinearLayout.LayoutParams((int) (1 * density), ViewGroup.LayoutParams.MATCH_PARENT);

        // --- RIGHT COLUMN: Content Area ---
        FrameLayout rightCol = new FrameLayout(activity);
        LinearLayout.LayoutParams rightLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        rightCol.setLayoutParams(rightLp);
        rightCol.setPadding((int) (22 * density), (int) (16 * density), (int) (22 * density), (int) (16 * density));
        rightCol.setClipChildren(false);
        rightCol.setClipToPadding(false);

        ScrollView rightScroll = new ScrollView(activity);
        rightScroll.setFillViewport(true);
        rightScroll.setVerticalScrollBarEnabled(false);
        rightScroll.setClipChildren(false);
        rightScroll.setClipToPadding(false);
        rightScroll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        rightContent.setOrientation(LinearLayout.VERTICAL);
        rightContent.setClipChildren(false);
        rightContent.setClipToPadding(false);
        rightContent.setPadding((int) (8 * density), (int) (8 * density), (int) (8 * density), (int) (24 * density));
        rightScroll.addView(rightContent, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rightCol.addView(rightScroll);

        // Sidebar Navigation Items
        ScrollView leftScroll = new ScrollView(activity);
        leftScroll.setVerticalScrollBarEnabled(false);
        leftScroll.setClipChildren(false);
        leftScroll.setClipToPadding(false);
        LinearLayout menuList = new LinearLayout(activity);
        menuList.setOrientation(LinearLayout.VERTICAL);
        menuList.setClipChildren(false);
        menuList.setClipToPadding(false);
        leftScroll.addView(menuList);
        leftCol.addView(leftScroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        class NavItem {
            final int tabId;
            final String label;
            final int iconRes;
            final int squircleBg;
            View rowView;

            NavItem(int tabId, String label, int iconRes, int squircleBg) {
                this.tabId = tabId;
                this.label = label;
                this.iconRes = iconRes;
                this.squircleBg = squircleBg;
            }
        }

        List<NavItem> navItems = new ArrayList<>();
        navItems.add(new NavItem(TAB_HOME_SCREEN, AppLanguage.strHomeScreen(activity), R.drawable.ic_home_screen, 0xFF7B1FA2));
        navItems.add(new NavItem(TAB_APPEARANCE, AppLanguage.strAppearance(activity), R.drawable.ic_appearance, 0xFF0097A7));
        navItems.add(new NavItem(TAB_STATUS_WEATHER, AppLanguage.strStatusBarWeather(activity), R.drawable.ic_screen_weather, 0xFF1976D2));
        navItems.add(new NavItem(TAB_EFFECTS, AppLanguage.strVisualEffects(activity), R.drawable.ic_settings, 0xFF7C3AED));
        navItems.add(new NavItem(TAB_STANDBY, AppLanguage.strStandByMode(activity), R.drawable.ic_standby_clock, 0xFF374151));
        String deviceLabel = AppLanguage.strDeviceSettings(activity);
        if (AppUpdateManager.isUpdateAvailable(activity)) {
            deviceLabel += " 🔴";
        }
        navItems.add(new NavItem(TAB_DEVICE_SETTINGS, deviceLabel, R.drawable.ic_tv_display, 0xFF374151));

        Runnable[] renderRightPane = new Runnable[1];
        int[] lastRenderedTab = new int[]{activeTab[0]};
        boolean[] hasRenderedPane = new boolean[]{false};

        Runnable updateSidebarSelection = () -> {
            navigationPrefs.edit().putInt(NavigationState.SETTINGS_TAB, NavigationState.normalizeSettingsTab(activeTab[0])).apply();
            boolean isProfile = (activeTab[0] == TAB_PROFILE);
            GradientDrawable pcBg = new GradientDrawable();
            pcBg.setColor(isProfile ? 0xFF333742 : 0xFF22242D);
            pcBg.setCornerRadius(12 * density);
            if (isProfile) pcBg.setStroke((int) (1.5f * density), 0x55FFFFFF);
            profileCard.setBackground(pcBg);

            ProfileManager.UserProfile sidebarActive = ProfileManager.getActiveProfile(activity);
            profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, sidebarActive,
                    Math.round(38 * density), ChildModeManager.isChildModeEnabled(activity, sidebarActive.id)));
            tvProfileName.setText(ProfileManager.getProfileName(activity));

            for (NavItem item : navItems) {
                if (item.rowView == null) continue;
                boolean isSelected = (activeTab[0] == item.tabId);
                GradientDrawable rbg = new GradientDrawable();
                rbg.setColor(isSelected ? 0xFF30333D : Color.TRANSPARENT);
                rbg.setCornerRadius(10 * density);
                if (isSelected) rbg.setStroke((int) (1 * density), 0x44FFFFFF);
                item.rowView.setBackground(rbg);
            }
        };

        View[] activeSidebarView = new View[]{profileCard};
        sDpadLeftTarget = () -> {
            if (activeSidebarView[0] != null) {
                activeSidebarView[0].requestFocus();
            }
        };

        profileCard.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                activeSidebarView[0] = profileCard;
                activeTab[0] = TAB_PROFILE;
                updateSidebarSelection.run();
                if (renderRightPane[0] != null) renderRightPane[0].run();
            }
            v.animate().scaleX(hasFocus ? 1.015f : 1.0f).scaleY(hasFocus ? 1.015f : 1.0f).setDuration(120).start();
        });

        profileCard.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    searchBox.requestFocus();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (!navItems.isEmpty() && navItems.get(0).rowView != null) {
                        navItems.get(0).rowView.requestFocus();
                        return true;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return focusRightPane(rightContent);
                }
            }
            return false;
        });

        profileCard.setOnClickListener(v -> {
            SoundEffects.playSelect();
            activeSidebarView[0] = profileCard;
            activeTab[0] = TAB_PROFILE;
            updateSidebarSelection.run();
            if (renderRightPane[0] != null) renderRightPane[0].run();
        });

        for (int i = 0; i < navItems.size(); i++) {
            NavItem item = navItems.get(i);
            final int itemIndex = i;
            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding((int) (10 * density), (int) (4 * density), (int) (10 * density), (int) (4 * density));
            row.setFocusable(true);
            row.setClickable(true);

            // Squircle Icon
            FrameLayout iconSquircle = new FrameLayout(activity);
            GradientDrawable sqBg = new GradientDrawable();
            sqBg.setColor(item.squircleBg);
            sqBg.setCornerRadius(8 * density);
            iconSquircle.setBackground(sqBg);

            ImageView iv = new ImageView(activity);
            iv.setImageResource(item.iconRes);
            iv.setColorFilter(0xFFFFFFFF);
            FrameLayout.LayoutParams ivLp = new FrameLayout.LayoutParams((int) (15 * density), (int) (15 * density), Gravity.CENTER);
            iconSquircle.addView(iv, ivLp);

            LinearLayout.LayoutParams sqLp = new LinearLayout.LayoutParams((int) (26 * density), (int) (26 * density));
            sqLp.setMarginEnd((int) (10 * density));
            row.addView(iconSquircle, sqLp);

            // Label
            TextView lbl = new TextView(activity);
            lbl.setText(item.label);
            lbl.setTextSize(13f);
            lbl.setTextColor(0xFFE2E8F0);
            lbl.setTypeface(outfit, Typeface.NORMAL);
            row.addView(lbl);

            row.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    activeSidebarView[0] = row;
                    activeTab[0] = item.tabId;
                    updateSidebarSelection.run();
                    if (renderRightPane[0] != null) renderRightPane[0].run();
                }
                v.animate().scaleX(hasFocus ? 1.015f : 1.0f).scaleY(hasFocus ? 1.015f : 1.0f).setDuration(120).start();
            });

            row.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP && itemIndex == 0) {
                        profileCard.requestFocus();
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        return focusRightPane(rightContent);
                    }
                }
                return false;
            });

            row.setOnClickListener(v -> {
                SoundEffects.playSelect();
                activeSidebarView[0] = row;
                activeTab[0] = item.tabId;
                updateSidebarSelection.run();
                if (renderRightPane[0] != null) renderRightPane[0].run();
            });

            LinearLayout.LayoutParams rLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (36 * density));
            rLp.bottomMargin = (int) (2 * density);
            menuList.addView(row, rLp);
            item.rowView = row;
        }

        // Restore the selected Settings destination and its sidebar cursor.
        if (activeTab[0] == TAB_PROFILE) {
            activeSidebarView[0] = profileCard;
        } else {
            for (NavItem item : navItems) {
                if (item.tabId == activeTab[0]) {
                    activeSidebarView[0] = item.rowView;
                    break;
                }
            }
        }

        // --- RENDER RIGHT CONTENT PANE ---
        renderRightPane[0] = () -> {
            if (hasRenderedPane[0]) {
                navigationPrefs.edit()
                        .putInt(rightScrollKey(lastRenderedTab[0]), rightScroll.getScrollY())
                        .apply();
            }
            int tab = NavigationState.normalizeSettingsTab(activeTab[0]);
            activeTab[0] = tab;
            lastRenderedTab[0] = tab;
            hasRenderedPane[0] = true;
            rightContent.removeAllViews();
            rightScroll.post(() -> rightScroll.scrollTo(0,
                    Math.max(0, navigationPrefs.getInt(rightScrollKey(tab), 0))));

            if (tab == TAB_PROFILE) {
                // === TAB 0: PROFILES & MULTI-USER MANAGEMENT ===
                TextView tvTitle = new TextView(activity);
                tvTitle.setText(AppLanguage.strProfileTitle(activity));
                tvTitle.setTextSize(24);
                tvTitle.setTextColor(0xFFFFFFFF);
                tvTitle.setTypeface(outfit, Typeface.BOLD);
                rightContent.addView(tvTitle);

                ProfileManager.UserProfile activeP = ProfileManager.getActiveProfile(activity);
                ImageView[] activeProfileListAvatar = new ImageView[1];

                // Section 1: Active Profile Avatars
                TextView tvAvatars = new TextView(activity);
                tvAvatars.setText(activity.getString(R.string.active_profile_avatar_header, activeP.name.toUpperCase()));
                tvAvatars.setTextSize(11.5f);
                tvAvatars.setTextColor(0xFF8E929E);
                tvAvatars.setTypeface(outfit, Typeface.BOLD);
                tvAvatars.setPadding(0, (int) (14 * density), 0, (int) (8 * density));
                rightContent.addView(tvAvatars);

                // Horizontal Row of 7 Animal Avatars
                LinearLayout avatarRow = new LinearLayout(activity);
                avatarRow.setOrientation(LinearLayout.HORIZONTAL);
                avatarRow.setGravity(Gravity.CENTER_VERTICAL);
                avatarRow.setPadding(0, (int) (4 * density), 0, (int) (14 * density));
                avatarRow.setClipChildren(false);
                avatarRow.setClipToPadding(false);

                FrameLayout[] avatarFrames = new FrameLayout[7];
                View[] avatarRings = new View[7];

                int activeAvatar = activeP.avatarIndex;

                for (int i = 0; i < 7; i++) {
                    final int idx = i;
                    FrameLayout frame = new FrameLayout(activity);
                    frame.setFocusable(true);
                    frame.setClickable(true);

                    int frameDim = (int) (60 * density);
                    LinearLayout.LayoutParams fLp = new LinearLayout.LayoutParams(frameDim, frameDim);
                    fLp.setMarginEnd((int) (12 * density));
                    frame.setLayoutParams(fLp);

                    // Outer White Ring for Selected Avatar
                    View ring = new View(activity);
                    GradientDrawable ringBg = new GradientDrawable();
                    ringBg.setShape(GradientDrawable.OVAL);
                    ringBg.setColor(Color.TRANSPARENT);
                    ringBg.setStroke((int) (3f * density), 0xFFFFFFFF);
                    ring.setBackground(ringBg);
                    ring.setVisibility(idx == activeAvatar ? View.VISIBLE : View.GONE);
                    frame.addView(ring, new FrameLayout.LayoutParams(frameDim, frameDim));
                    // Inner Avatar Circle ImageView
                    ImageView img = new ImageView(activity);
                    img.setImageResource(ProfileManager.getAvatarResId(idx));
                    int innerDim = (int) (48 * density);
                    FrameLayout.LayoutParams imgLp = new FrameLayout.LayoutParams(innerDim, innerDim, Gravity.CENTER);
                    frame.addView(img, imgLp);
                    // Draw above the avatar image so the focus ring cannot be clipped by it.
                    TvFocusIndicator.attachAvatarFocusRing(frame, frameDim, density);

                    avatarFrames[idx] = frame;
                    avatarRings[idx] = ring;

                    if (idx == 0) {
                        frame.setOnKeyListener((v, keyCode, event) -> {
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                if (sDpadLeftTarget != null) {
                                    sDpadLeftTarget.run();
                                    return true;
                                }
                            }
                            return false;
                        });
                    }

                    frame.setOnClickListener(v -> {
                        SoundEffects.playSelect();
                        // Selecting a built-in avatar replaces a personal photo only after its reference is cleared safely.
                        if (!ProfileManager.clearCustomAvatar(activity, activeP.id)) {
                            TvFeedback.show(activity, R.string.avatar_delete_failed, Toast.LENGTH_LONG);
                            return;
                        }
                        ProfileManager.setAvatarIndex(activity, idx);
                        for (int k = 0; k < 7; k++) {
                            if (avatarRings[k] != null) {
                                avatarRings[k].setVisibility(k == idx ? View.VISIBLE : View.GONE);
                            }
                        }
                        ProfileManager.UserProfile refreshedProfile = ProfileManager.getActiveProfile(activity);
                        boolean refreshedChildMode = ChildModeManager.isChildModeEnabled(activity, refreshedProfile.id);
                        profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, refreshedProfile,
                                Math.round(38 * density), refreshedChildMode));
                        if (activeProfileListAvatar[0] != null) {
                            activeProfileListAvatar[0].setImageDrawable(ProfileManager.getAvatarDrawable(activity, refreshedProfile,
                                    Math.round(32 * density), refreshedChildMode));
                        }
                        activity.updateTopBarUserIcon();
                        TvFeedback.show(activity, activity.getString(R.string.avatar_selected_toast, ProfileManager.AVATAR_NAMES[idx]), Toast.LENGTH_SHORT);
                    });

                    avatarRow.addView(frame);
                }
                rightContent.addView(avatarRow);

                // Personal photo avatar actions use the same profile-scoped storage as built-in avatar selection.
                LinearLayout avatarPhotoActions = new LinearLayout(activity);
                avatarPhotoActions.setOrientation(LinearLayout.VERTICAL);
                avatarPhotoActions.setPadding(0, 0, 0, (int) (12 * density));

                Button uploadAvatarButton = new Button(activity);
                uploadAvatarButton.setText(R.string.avatar_upload_qr_action);
                uploadAvatarButton.setTextColor(0xFFFFFFFF);
                uploadAvatarButton.setTextSize(13);
                uploadAvatarButton.setAllCaps(false);
                uploadAvatarButton.setFocusable(true);
                GradientDrawable uploadAvatarBg = new GradientDrawable();
                uploadAvatarBg.setColor(0xFF176B87);
                uploadAvatarBg.setCornerRadius(10 * density);
                uploadAvatarButton.setBackground(uploadAvatarBg);
                TvFocusIndicator.applyRoundedTextFocus(uploadAvatarButton, density, 10f,
                        0xFF176B87, 0xFF1B7189, 0xFF2C7F98, 0xFFFFFFFF,
                        0xFFFFFFFF, 0xFFFFFFFF, 1.025f);
                uploadAvatarButton.setOnClickListener(v -> activity.showAvatarUploadQrDialog(activeP.id, () -> {
                    ProfileManager.UserProfile updated = ProfileManager.getActiveProfile(activity);
                    boolean updatedChildMode = ChildModeManager.isChildModeEnabled(activity, updated.id);
                    profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, updated,
                            Math.round(38 * density), updatedChildMode));
                    if (activeProfileListAvatar[0] != null) {
                        activeProfileListAvatar[0].setImageDrawable(ProfileManager.getAvatarDrawable(activity, updated,
                                Math.round(32 * density), updatedChildMode));
                    }
                }));
                avatarPhotoActions.addView(uploadAvatarButton, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) (44 * density)));

                Button defaultAvatarButton = new Button(activity);
                defaultAvatarButton.setText(R.string.avatar_use_default_action);
                defaultAvatarButton.setTextColor(0xFFCBD5E1);
                defaultAvatarButton.setTextSize(13);
                defaultAvatarButton.setAllCaps(false);
                defaultAvatarButton.setFocusable(true);
                GradientDrawable defaultAvatarBg = new GradientDrawable();
                defaultAvatarBg.setColor(0xFF292D37);
                defaultAvatarBg.setCornerRadius(10 * density);
                defaultAvatarButton.setBackground(defaultAvatarBg);
                TvFocusIndicator.applyRoundedTextFocus(defaultAvatarButton, density, 10f,
                        0xFF292D37, 0xFF3B4354, 0xFF454B5A, 0xFFFFFFFF,
                        0xFFCBD5E1, 0xFFFFFFFF, 1.025f);
                LinearLayout.LayoutParams defaultAvatarLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) (42 * density));
                defaultAvatarLp.topMargin = (int) (6 * density);
                defaultAvatarButton.setOnClickListener(v -> {
                    if (!ProfileManager.clearCustomAvatar(activity, activeP.id)) {
                        TvFeedback.show(activity, R.string.avatar_delete_failed, Toast.LENGTH_LONG);
                        return;
                    }
                    ProfileManager.UserProfile restored = ProfileManager.getActiveProfile(activity);
                    boolean restoredChildMode = ChildModeManager.isChildModeEnabled(activity, restored.id);
                    profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, restored,
                            Math.round(38 * density), restoredChildMode));
                    if (activeProfileListAvatar[0] != null) {
                        activeProfileListAvatar[0].setImageDrawable(ProfileManager.getAvatarDrawable(activity, restored,
                                Math.round(32 * density), restoredChildMode));
                    }
                    activity.updateTopBarUserIcon();
                    TvFeedback.show(activity, R.string.avatar_default_restored, Toast.LENGTH_SHORT);
                });
                avatarPhotoActions.addView(defaultAvatarButton, defaultAvatarLp);
                rightContent.addView(avatarPhotoActions);

                // Edit Active Profile Name Card
                LinearLayout editNameCard = new LinearLayout(activity);
                editNameCard.setOrientation(LinearLayout.HORIZONTAL);
                editNameCard.setGravity(Gravity.CENTER_VERTICAL);
                editNameCard.setPadding((int) (14 * density), (int) (10 * density), (int) (16 * density), (int) (10 * density));
                editNameCard.setFocusable(true);
                editNameCard.setClickable(true);

                GradientDrawable enBg = new GradientDrawable();
                enBg.setColor(0xFF272A33);
                enBg.setCornerRadius(12 * density);
                editNameCard.setBackground(enBg);

                // Pencil icon squircle
                FrameLayout pencilSquircle = new FrameLayout(activity);
                GradientDrawable psq = new GradientDrawable();
                psq.setColor(0xFF1E2026);
                psq.setCornerRadius(8 * density);
                pencilSquircle.setBackground(psq);

                ImageView ivPencil = new ImageView(activity);
                ivPencil.setImageResource(R.drawable.ic_pencil);
                FrameLayout.LayoutParams ivpLp = new FrameLayout.LayoutParams((int) (18 * density), (int) (18 * density), Gravity.CENTER);
                pencilSquircle.addView(ivPencil, ivpLp);

                LinearLayout.LayoutParams psqLp = new LinearLayout.LayoutParams((int) (32 * density), (int) (32 * density));
                psqLp.setMarginEnd((int) (12 * density));
                editNameCard.addView(pencilSquircle, psqLp);

                TextView tvEditLabel = new TextView(activity);
                tvEditLabel.setText(AppLanguage.strEditName(activity));
                tvEditLabel.setTextSize(14f);
                tvEditLabel.setTextColor(0xFFFFFFFF);
                tvEditLabel.setTypeface(outfit, Typeface.BOLD);
                editNameCard.addView(tvEditLabel, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                TextView tvCurName = new TextView(activity);
                tvCurName.setText(activeP.name);
                tvCurName.setTextSize(13.5f);
                tvCurName.setTextColor(0xFF8E929E);
                tvCurName.setTypeface(outfit, Typeface.NORMAL);
                editNameCard.addView(tvCurName);

                editNameCard.setOnFocusChangeListener((v, hasFocus) -> {
                    GradientDrawable g = new GradientDrawable();
                    g.setColor(hasFocus ? 0xFF353844 : 0xFF272A33);
                    g.setCornerRadius(12 * density);
                    if (hasFocus) g.setStroke((int) (2f * density), 0xFFFFFFFF);
                    v.setBackground(g);
                    v.animate().scaleX(hasFocus ? 1.015f : 1.0f).scaleY(hasFocus ? 1.015f : 1.0f).setDuration(120).start();
                });

                editNameCard.setOnKeyListener((v, keyCode, event) -> {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        if (sDpadLeftTarget != null) {
                            sDpadLeftTarget.run();
                            return true;
                        }
                    }
                    return false;
                });

                editNameCard.setOnClickListener(v -> {
                    SoundEffects.playSelect();
                    showEditNameDialog(activity, density, outfit, newName -> {
                        ProfileManager.setProfileName(activity, newName);
                        tvCurName.setText(newName);
                        tvProfileName.setText(newName);
                        activity.updateTopBarUserIcon();
                        updateSidebarSelection.run();
                        if (renderRightPane[0] != null) renderRightPane[0].run();
                    });
                });

                LinearLayout.LayoutParams encLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                encLp.bottomMargin = (int) (14 * density);
                rightContent.addView(editNameCard, encLp);

                // Section 2: Manage All Users (Add & Delete User)
                List<ProfileManager.UserProfile> allProfiles = ProfileManager.getAllProfiles(activity);
                TextView tvUserList = new TextView(activity);
                tvUserList.setText(activity.getString(R.string.profile_list_header, allProfiles.size()));
                tvUserList.setTextSize(11.5f);
                tvUserList.setTextColor(0xFF8E929E);
                tvUserList.setTypeface(outfit, Typeface.BOLD);
                tvUserList.setPadding(0, 0, 0, (int) (8 * density));
                rightContent.addView(tvUserList);

                for (ProfileManager.UserProfile p : allProfiles) {
                    boolean isCur = p.id.equals(activeP.id);

                    LinearLayout pRow = new LinearLayout(activity);
                    pRow.setOrientation(LinearLayout.HORIZONTAL);
                    pRow.setGravity(Gravity.CENTER_VERTICAL);
                    pRow.setPadding((int) (14 * density), (int) (8 * density), (int) (14 * density), (int) (8 * density));
                    pRow.setFocusable(true);
                    pRow.setClickable(true);

                    GradientDrawable pBg = new GradientDrawable();
                    pBg.setColor(isCur ? 0xFF2D313D : 0xFF23262F);
                    pBg.setCornerRadius(12 * density);
                    if (isCur) pBg.setStroke((int) (1.5f * density), 0x77FFFFFF);
                    pRow.setBackground(pBg);

                    boolean profileChildMode = ChildModeManager.isChildModeEnabled(activity, p.id);
                    ImageView ivP = new ImageView(activity);
                    ivP.setImageDrawable(ProfileManager.getAvatarDrawable(activity, p,
                            Math.round(32 * density), profileChildMode));
                    if (isCur) activeProfileListAvatar[0] = ivP;
                    LinearLayout.LayoutParams ivpLp2 = new LinearLayout.LayoutParams((int) (32 * density), (int) (32 * density));
                    ivpLp2.setMarginEnd((int) (10 * density));
                    pRow.addView(ivP, ivpLp2);

                    TextView tvPName = new TextView(activity);
                    tvPName.setText(p.name + (profileChildMode ? activity.getString(R.string.child_mode_suffix) : ""));
                    tvPName.setTextSize(13.5f);
                    tvPName.setTextColor(0xFFFFFFFF);
                    tvPName.setTypeface(outfit, isCur ? Typeface.BOLD : Typeface.NORMAL);
                    pRow.addView(tvPName, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                    if (isCur) {
                        TextView tvTag = new TextView(activity);
                        tvTag.setText(R.string.active_label);
                        tvTag.setTextSize(11f);
                        tvTag.setTextColor(0xFF38BDF8);
                        tvTag.setTypeface(outfit, Typeface.BOLD);
                        pRow.addView(tvTag);
                    } else if (allProfiles.size() > 1) {
                        TextView tvDel = new TextView(activity);
                        tvDel.setText(R.string.delete_label);
                        tvDel.setTextSize(11.5f);
                        tvDel.setTextColor(0xFFF87171);
                        tvDel.setTypeface(outfit, Typeface.NORMAL);
                        pRow.addView(tvDel);
                    }

                    pRow.setOnFocusChangeListener((v, hasFocus) -> {
                        GradientDrawable g = new GradientDrawable();
                        g.setColor(hasFocus ? 0xFF353946 : (isCur ? 0xFF2D313D : 0xFF23262F));
                        g.setCornerRadius(12 * density);
                        if (hasFocus) g.setStroke((int) (2f * density), 0xFFFFFFFF);
                        else if (isCur) g.setStroke((int) (1.5f * density), 0x77FFFFFF);
                        v.setBackground(g);
                        v.animate().scaleX(hasFocus ? 1.015f : 1.0f).scaleY(hasFocus ? 1.015f : 1.0f).setDuration(120).start();
                    });

                    pRow.setOnKeyListener((v, keyCode, event) -> {
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            if (sDpadLeftTarget != null) {
                                sDpadLeftTarget.run();
                                return true;
                            }
                        }
                        return false;
                    });

                    pRow.setOnClickListener(v -> {
                        SoundEffects.playSelect();
                        if (isCur) {
                            TvFeedback.show(activity, activity.getString(R.string.profile_already_active, p.name), Toast.LENGTH_SHORT);
                        } else {
                            // Show options: Switch or Delete
                            AlertDialog.Builder ob = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
                            ob.setTitle(activity.getString(R.string.profile_user_title, p.name));
                            String[] opts = allProfiles.size() > 1 ? new String[]{activity.getString(R.string.switch_profile_action), activity.getString(R.string.delete_user_action)} : new String[]{activity.getString(R.string.switch_profile_action)};
                            ob.setItems(opts, (d, which) -> {
                                if (which == 0) {
                                    UserSwitchDialog.requestProfileSwitch(activity, p, null, () -> {
                                        d.dismiss();
                                        updateSidebarSelection.run();
                                        if (renderRightPane[0] != null) renderRightPane[0].run();
                                    });
                                } else if (which == 1) {
                                    UserSwitchDialog.requestDeleteProfile(activity, p.id, p.name, () -> {
                                        d.dismiss();
                                        updateSidebarSelection.run();
                                        if (renderRightPane[0] != null) renderRightPane[0].run();
                                    });
                                }
                            });
                            AlertDialog profileActions = ob.show();
                            TvDialogStyle.apply(profileActions, activity, 520, 0);
                        }
                    });

                    LinearLayout.LayoutParams prLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    prLp.bottomMargin = (int) (6 * density);
                    rightContent.addView(pRow, prLp);
                }

                // Add User Button
                LinearLayout addUserBtn = buildCardRow(activity, density, outfit, activity.getString(R.string.add_profile_card_title), activity.getString(R.string.add_profile_card_summary), () -> {
                    SoundEffects.playSelect();
                    UserSwitchDialog.showAddProfileDialog(activity, density, outfit, () -> {
                        updateSidebarSelection.run();
                        if (renderRightPane[0] != null) renderRightPane[0].run();
                    });
                });
                rightContent.addView(addUserBtn);

                // Child Mode belongs with user/profile management, not as a separate navigation destination.
                // === PARENTAL CONTROLS: GROUPED UNDER PROFILE ===
                addSectionHeader(rightContent, outfit, activity.getString(R.string.parent_controls_section));

                boolean isChildOn = ChildModeManager.isChildModeEnabled(activity);
                Set<String> lockedSet = ChildModeManager.getLockedApps(activity);
                Set<String> hiddenSet = ChildModeManager.getHiddenApps(activity);

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.child_mode_switch_title),
                        activity.getString(R.string.child_mode_switch_description),
                        isChildOn, isChecked -> {
                            ChildModeManager.promptPin(activity, activity.getString(R.string.child_mode_pin_confirm_title), activity.getString(R.string.child_mode_pin_confirm_message), new ChildModeManager.PinCallback() {
                                @Override
                                public void onSuccess() {
                                    ChildModeManager.setChildModeEnabled(activity, isChecked);
                                    activity.reloadTiles();
                                    activity.updateTopBarUserIcon();
                                    updateSidebarSelection.run();
                                    TvFeedback.show(activity, isChecked ? R.string.child_mode_enabled : R.string.child_mode_disabled, Toast.LENGTH_SHORT);
                                    if (renderRightPane[0] != null) renderRightPane[0].run();
                                }

                                @Override
                                public void onCancelled() {
                                    if (renderRightPane[0] != null) renderRightPane[0].run();
                                }
                            });
                        }));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.lock_apps_count, lockedSet.size()),
                        activity.getString(R.string.lock_apps_count_summary),
                        () -> showAppLockPicker(activity, density, outfit, false)));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.hide_apps_count, hiddenSet.size()),
                        activity.getString(R.string.hide_apps_count_summary),
                        () -> showAppLockPicker(activity, density, outfit, true)));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.change_pin_title),
                        activity.getString(R.string.change_pin_summary, ChildModeManager.getPin(activity).equals("0000") ? activity.getString(R.string.default_pin_label) : "••••"),
                        () -> showChangePinDialog(activity, density, outfit)));

            } else if (tab == TAB_HOME_SCREEN) {
                // === TAB 1: HOME SCREEN (CONSOLIDATING CANDY BOX SETTINGS) ===
                addHeader(rightContent, outfit, AppLanguage.strHomeScreen(activity));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.daily_prayer_panel_title), activity.getString(R.string.daily_prayer_panel_summary),
                        activity.isIslamicShelfEnabled(), isChecked -> {
                            activity.setIslamicShelfEnabled(isChecked);
                        }));

                boolean ayahShown = WeeklyAyah.shown(activity);
                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.daily_ayah_title), activity.getString(R.string.daily_ayah_summary),
                        ayahShown, isChecked -> {
                            WeeklyAyah.setShown(activity, isChecked);
                            activity.showAyah(WeeklyAyah.last(activity));
                            if (isChecked) activity.updateAyah();
                        }));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.interface_sound_title), activity.getString(R.string.interface_sound_summary),
                        activity::showSoundEffectsDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.azan_settings_title), activity.getString(R.string.azan_settings_summary),
                        activity::showAzanSettingsDialog));

            } else if (tab == TAB_APPEARANCE) {
                // === TAB 2: APPEARANCE ===
                addHeader(rightContent, outfit, AppLanguage.strAppearance(activity));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.wallpaper_collection_title), activity.getString(R.string.wallpaper_collection_summary),
                        activity::showWallpaperDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.wallpaper_interval_title), activity.getString(R.string.wallpaper_interval_summary),
                        activity::showWallpaperIntervalDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.wallpaper_manage_title_card),
                        activity.getString(R.string.wallpaper_manage_summary_card),
                        activity::showCustomWallpaperManagerDialog));

            } else if (tab == TAB_STATUS_WEATHER) {
                // === TAB: WIDGET & INFORMATION ===
                addHeader(rightContent, outfit, AppLanguage.strStatusBarWeather(activity));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.location_prayer_title), activity.getString(R.string.location_prayer_summary),
                        () -> activity.startActivity(new Intent(activity, LocationActivity.class))));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.weather_widget_title),
                        activity.getString(R.string.weather_widget_summary),
                        activity.isWeatherWidgetEnabled(), isChecked -> {
                            activity.setWeatherWidgetEnabled(isChecked);
                        }));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.info_actions_title_card), activity.getString(R.string.info_actions_summary_card),
                        activity::showInfoActionsDialog));

            } else if (tab == TAB_EFFECTS) {
                // === TAB: ANIMATIONS & VISUAL EFFECTS ===
                addHeader(rightContent, outfit, AppLanguage.strVisualEffects(activity));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.icon_animation_setting_title),
                        activity.getString(R.string.icon_animation_setting_description),
                        activity.isIconAnimationsEnabled(), isChecked -> {
                            activity.setIconAnimationsEnabled(isChecked);
                            TvFeedback.show(activity, isChecked ? R.string.icon_animation_enabled : R.string.icon_animation_disabled, Toast.LENGTH_SHORT);
                            if (renderRightPane[0] != null) renderRightPane[0].run();
                        }));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.glass_effect_setting_title),
                        activity.getString(R.string.glass_effect_setting_description),
                        activity.isGlassEffectsEnabled(), isChecked -> {
                            activity.setGlassEffectsEnabled(isChecked);
                            TvFeedback.show(activity, isChecked ? R.string.glass_effect_enabled : R.string.glass_effect_disabled, Toast.LENGTH_SHORT);
                            if (renderRightPane[0] != null) renderRightPane[0].run();
                        }));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.weather_animation_setting_title),
                        activity.getString(R.string.weather_animation_setting_description),
                        activity.isDynamicWeatherEffectsEnabled(), isChecked -> {
                            activity.setDynamicWeatherEffectsEnabled(isChecked);
                            TvFeedback.show(activity, isChecked ? R.string.weather_animation_enabled : R.string.weather_animation_disabled, Toast.LENGTH_SHORT);
                            if (renderRightPane[0] != null) renderRightPane[0].run();
                        }));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.icon_glow_setting_title),
                        activity.getString(R.string.icon_glow_setting_description),
                        activity.isIconGlowEffectsEnabled(), isChecked -> {
                            activity.setIconGlowEffectsEnabled(isChecked);
                            TvFeedback.show(activity, isChecked ? R.string.icon_glow_enabled : R.string.icon_glow_disabled, Toast.LENGTH_SHORT);
                            if (renderRightPane[0] != null) renderRightPane[0].run();
                        }));

            } else if (tab == TAB_STANDBY) {
                // === TAB: STANDBY / PERSONAL PHOTO SCREENSAVER ===
                addHeader(rightContent, outfit, AppLanguage.strStandByMode(activity));

                int photoCount = activity.getStandbyPhotoCount();
                boolean photoMode = activity.isStandbyPhotoMode();
                rightContent.addView(buildInfoCard(activity, density, outfit,
                        activity.getString(R.string.standby_collection_title), activity.getString(R.string.standby_collection_summary, photoCount)));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.standby_upload_title),
                        activity.getString(R.string.standby_upload_summary),
                        activity::showStandbyUploadQrDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.standby_pick_title),
                        activity.getString(R.string.standby_pick_summary),
                        activity::pickStandbyPhotos));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        activity.getString(R.string.standby_use_photos_title),
                        activity.getString(R.string.standby_use_photos_summary),
                        photoMode, isChecked -> {
                            activity.setStandbyPhotoMode(isChecked);
                            if (renderRightPane[0] != null) renderRightPane[0].run();
                        }));

                if (photoCount > 0) {
                    rightContent.addView(buildCardRow(activity, density, outfit,
                            activity.getString(R.string.standby_clear_title),
                            activity.getString(R.string.standby_clear_summary),
                            () -> {
                                activity.clearStandbyPhotos();
                                if (renderRightPane[0] != null) renderRightPane[0].run();
                            }));
                }

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.standby_preview_title),
                        activity.getString(R.string.standby_preview_summary),
                        activity::showNightstandScreensaver));

            } else if (tab == TAB_DEVICE_SETTINGS) {
                // === TAB: DEVICE SETTINGS (GENERAL + REMOTE + ACCESSIBILITY + TV) ===
                addHeader(rightContent, outfit, AppLanguage.strDeviceSettings(activity));

                addSectionHeader(rightContent, outfit, activity.getString(R.string.device_accessibility_section));
                Display display = activity.getWindowManager().getDefaultDisplay();
                Display.Mode mode = (display != null) ? display.getMode() : null;
                int physW = (mode != null) ? mode.getPhysicalWidth() : dm.widthPixels;
                int physH = (mode != null) ? mode.getPhysicalHeight() : dm.heightPixels;
                float fps = (mode != null) ? mode.getRefreshRate() : 60f;

                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                ResolveInfo resolveInfo = activity.getPackageManager().resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
                boolean isDefault = resolveInfo != null && activity.getPackageName().equals(resolveInfo.activityInfo.packageName);
                String activePkg = resolveInfo != null ? resolveInfo.activityInfo.packageName : activity.getString(R.string.unknown_value);

                String homeSub = isDefault ? activity.getString(R.string.launcher_is_default) : activity.getString(R.string.launcher_currently_active, activePkg);
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.default_launcher_status_title), activity.getString(R.string.default_launcher_status_summary, homeSub),
                        () -> showDefaultLauncherDialog(activity, density, outfit, isDefault, activePkg)));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.accessibility_remote_title),
                        activity.getString(R.string.accessibility_remote_summary),
                        () -> {
                            SoundEffects.playSelect();
                            try {
                                activity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                            } catch (Exception e) {
                                TvFeedback.show(activity, R.string.open_accessibility_settings_hint, Toast.LENGTH_LONG);
                            }
                        }));

                addSectionHeader(rightContent, outfit, activity.getString(R.string.power_maintenance_section));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.restart_tv_title), activity.getString(R.string.restart_tv_summary),
                        activity::showRestartDialog));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.power_tv_title), activity.getString(R.string.power_tv_summary),
                        activity::showTurnOffDialog));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.clear_tv_cache_title), activity.getString(R.string.clear_tv_cache_summary),
                        activity::showCleanTvCacheDialog));

                long freeBytes = CacheCleaner.getFreeStorageBytes();
                long totalBytes = CacheCleaner.getTotalStorageBytes();
                String freeStr = android.text.format.Formatter.formatFileSize(activity, freeBytes);
                String totalStr = android.text.format.Formatter.formatFileSize(activity, totalBytes);
                rightContent.addView(buildInfoCard(activity, density, outfit,
                        activity.getString(R.string.storage_capacity_title), activity.getString(R.string.storage_capacity_summary, freeStr, totalStr, 100 - CacheCleaner.usedPercent())));

                addSectionHeader(rightContent, outfit, activity.getString(R.string.device_information_section));
                String resTitle;
                if (physW >= 3840 || physH >= 2160) {
                    resTitle = "4K Ultra HD (" + physW + " × " + physH + " @" + Math.round(fps) + " Hz)";
                } else if (physW >= 2560 || physH >= 1440) {
                    resTitle = "2K QHD (" + physW + " × " + physH + " @" + Math.round(fps) + " Hz)";
                } else if (physW >= 1920 || physH >= 1080 || dm.widthPixels >= 1920) {
                    resTitle = "Full HD (" + physW + " × " + physH + " @" + Math.round(fps) + " Hz)";
                } else {
                    resTitle = physW + " × " + physH + " @" + Math.round(fps) + " Hz";
                }

                rightContent.addView(buildInfoCard(activity, density, outfit,
                        activity.getString(R.string.device_model_title), Build.MODEL + " (" + Build.MANUFACTURER + ")"));
                rightContent.addView(buildInfoCard(activity, density, outfit,
                        activity.getString(R.string.android_version_title), activity.getString(R.string.android_version_value, Build.VERSION.RELEASE, Build.VERSION.SDK_INT)));
                rightContent.addView(buildInfoCard(activity, density, outfit,
                        activity.getString(R.string.physical_resolution_title), resTitle));
                rightContent.addView(buildInfoCard(activity, density, outfit,
                        activity.getString(R.string.ui_resolution_title), dm.widthPixels + " × " + dm.heightPixels + " (" + Math.round(density * 160) + " dpi)"));

                addSectionHeader(rightContent, outfit, activity.getString(R.string.system_settings_section));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.app_language_title), activity.getString(R.string.app_language_summary),
                        activity::showLanguageDialog));

                addSectionHeader(rightContent, outfit, activity.getString(R.string.system_updates_section));
                boolean hasUpdate = AppUpdateManager.isUpdateAvailable(activity);
                String otaTitle = hasUpdate ? activity.getString(R.string.update_available_title) : activity.getString(R.string.check_updates_title);
                String otaSub = hasUpdate
                        ? activity.getString(R.string.update_available_summary, AppUpdateManager.getNewVersionName(activity))
                        : activity.getString(R.string.check_updates_summary);
                rightContent.addView(buildCardRow(activity, density, outfit, otaTitle, otaSub,
                        () -> {
                            if (hasUpdate) AppUpdateManager.checkForUpdates(activity, true);
                            else activity.showAutoUpdateDialog();
                        }));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.android_settings_title), activity.getString(R.string.android_settings_summary),
                        () -> {
                            try {
                                activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                            } catch (Exception e) {
                                TvFeedback.show(activity, R.string.android_settings_unavailable, Toast.LENGTH_SHORT);
                            }
                        }));
            }
        };

        searchBox.setOnClickListener(v -> {
            SoundEffects.playSelect();
            EditText searchInput = new EditText(activity);
            searchInput.setSingleLine(true);
            searchInput.setTextColor(0xFFFFFFFF);
            searchInput.setHintTextColor(0xFF8E929E);
            searchInput.setHint(R.string.settings_search_hint);
            searchInput.setTextSize(15f);
            searchInput.setPadding((int) (14 * density), (int) (10 * density), (int) (14 * density), (int) (10 * density));
            GradientDrawable inputBg = new GradientDrawable();
            inputBg.setColor(0xFF282A33);
            inputBg.setCornerRadius(10 * density);
            inputBg.setStroke((int) (1 * density), 0xFF4A4E5C);
            searchInput.setBackground(inputBg);

            AlertDialog searchDialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                    .setTitle(R.string.settings_search_title)
                    .setView(searchInput)
                    .setPositiveButton(R.string.search_action, (dialog, which) -> {
                        String query = searchInput.getText().toString().trim().toLowerCase(java.util.Locale.ROOT);
                        if (query.isEmpty()) {
                            TvFeedback.show(activity, R.string.search_keyword_required, Toast.LENGTH_SHORT);
                            return;
                        }

                        String[] labels = {
                                activity.getString(R.string.search_tab_profile),
                                activity.getString(R.string.search_tab_home),
                                activity.getString(R.string.search_tab_appearance),
                                activity.getString(R.string.search_tab_information),
                                activity.getString(R.string.search_tab_effects),
                                activity.getString(R.string.search_tab_standby),
                                activity.getString(R.string.search_tab_parental),
                                activity.getString(R.string.search_tab_device)
                        };
                        String[] keywords = {
                                "profil pengguna akun avatar nama",
                                "beranda layar utama doa salat azan audio suara",
                                "tampilan wallpaper latar gambar koleksi interval foto pribadi qr upload hp",
                                "widget informasi lokasi cuaca udara kualitas aqi jadwal salat pusat informasi tindakan status sistem ringkasan",
                                "animasi efek cuaca dinamis hujan cahaya warna ikon glow transisi",
                                "layar siaga standby screensaver jam foto pribadi foto upload qr hp ponsel",
                                "mode anak pin kunci aplikasi sembunyikan kontrol orang tua",
                                "perangkat remote tombol home aksesibilitas restart matikan daya tv android resolusi penyimpanan bahasa pembaruan cache"
                        };
                        int[] tabIds = {
                                TAB_PROFILE, TAB_HOME_SCREEN, TAB_APPEARANCE, TAB_STATUS_WEATHER,
                                TAB_EFFECTS, TAB_STANDBY, TAB_PROFILE, TAB_DEVICE_SETTINGS
                        };
                        ArrayList<String> resultLabels = new ArrayList<>();
                        ArrayList<Integer> resultTabs = new ArrayList<>();
                        String[] terms = query.split("\\s+");
                        for (int i = 0; i < labels.length; i++) {
                            String searchable = (labels[i] + " " + keywords[i]).toLowerCase(java.util.Locale.ROOT);
                            boolean matches = true;
                            for (String term : terms) {
                                if (!searchable.contains(term)) {
                                    matches = false;
                                    break;
                                }
                            }
                            if (matches) {
                                resultLabels.add(labels[i]);
                                resultTabs.add(tabIds[i]);
                            }
                        }

                        if (resultTabs.isEmpty()) {
                            TvFeedback.show(activity, R.string.settings_search_not_found, Toast.LENGTH_LONG);
                            return;
                        }
                        String[] choices = resultLabels.toArray(new String[0]);
                        AlertDialog resultsDialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                                .setTitle(R.string.settings_search_results)
                                .setItems(choices, (resultDialog, index) -> {
                                    int selectedTab = resultTabs.get(index);
                                    activeTab[0] = selectedTab;
                                    if (selectedTab == TAB_PROFILE) {
                                        activeSidebarView[0] = profileCard;
                                    } else {
                                        for (NavItem item : navItems) {
                                            if (item.tabId == selectedTab) {
                                                activeSidebarView[0] = item.rowView;
                                                break;
                                            }
                                        }
                                    }
                                    updateSidebarSelection.run();
                                    renderRightPane[0].run();
                                    if (activeSidebarView[0] != null) activeSidebarView[0].requestFocus();
                                })
                                .setNegativeButton(R.string.cancel_action, null)
                                .create();
                        resultsDialog.show();
                        TvDialogStyle.apply(resultsDialog, activity, 520, 0);
                    })
                    .setNegativeButton(R.string.cancel_action, null)
                    .create();
            searchDialog.show();
            TvDialogStyle.apply(searchDialog, activity, 520, 0);
            searchInput.requestFocus();
        });

        // Initial render of Tab 0 (Profile)
        updateSidebarSelection.run();
        renderRightPane[0].run();

        root.addView(leftCol, leftLp);
        root.addView(verticalDivider, divLp);
        root.addView(rightCol, rightLp);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        dialogHolder[0] = dialog;
        final ViewTreeObserver.OnGlobalFocusChangeListener[] focusListener =
                new ViewTreeObserver.OnGlobalFocusChangeListener[1];
        dialog.setOnDismissListener(d -> {
            if (focusListener[0] != null && root.getViewTreeObserver().isAlive()) {
                root.getViewTreeObserver().removeOnGlobalFocusChangeListener(focusListener[0]);
            }
            sDpadLeftTarget = null;
        });
        dialog.show();

        // Keep a validated path into the active pane so a recreated Settings dialog can return
        // to the last control. If the content has changed, restoration falls back to the sidebar.
        focusListener[0] = (oldFocus, newFocus) -> {
            if (newFocus == null) return;
            if (isDescendantOf(rightContent, newFocus)) {
                String path = focusPath(rightContent, newFocus);
                if (path != null) {
                    navigationPrefs.edit()
                            .putString(PREF_SETTINGS_FOCUS_AREA, "right")
                            .putString(PREF_SETTINGS_FOCUS_PATH, path)
                            .putString(PREF_SETTINGS_FOCUS_SIGNATURE, focusSignature(newFocus))
                            .putInt(PREF_SETTINGS_FOCUS_TAB, activeTab[0])
                            .putInt(rightScrollKey(activeTab[0]), rightScroll.getScrollY())
                            .apply();
                }
            } else if (isDescendantOf(leftCol, newFocus)) {
                String path = focusPath(leftCol, newFocus);
                if (path != null) {
                    navigationPrefs.edit()
                            .putString(PREF_SETTINGS_FOCUS_AREA, "sidebar")
                            .putString(PREF_SETTINGS_FOCUS_PATH, path)
                            .putString(PREF_SETTINGS_FOCUS_SIGNATURE, focusSignature(newFocus))
                            .putInt(PREF_SETTINGS_FOCUS_TAB, activeTab[0])
                            .putInt(PREF_SETTINGS_LEFT_SCROLL, leftScroll.getScrollY())
                            .apply();
                }
            }
        };
        root.getViewTreeObserver().addOnGlobalFocusChangeListener(focusListener[0]);

        Window win = dialog.getWindow();
        if (win != null) {
            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            win.addFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            win.setDimAmount(0.65f);
            int w = Math.min((int) (1000 * density), (int) (dm.widthPixels * 0.92f));
            int h = Math.min((int) (580 * density), (int) (dm.heightPixels * 0.92f));
            win.setLayout(w, h);
            win.setGravity(Gravity.CENTER);
        }

        leftScroll.post(() -> leftScroll.scrollTo(0,
                Math.max(0, navigationPrefs.getInt(PREF_SETTINGS_LEFT_SCROLL, 0))));
        String savedFocusArea = navigationPrefs.getString(PREF_SETTINGS_FOCUS_AREA, "sidebar");
        int savedFocusTab = navigationPrefs.getInt(PREF_SETTINGS_FOCUS_TAB, -1);
        String savedFocusPath = navigationPrefs.getString(PREF_SETTINGS_FOCUS_PATH, null);
        String savedFocusSignature = navigationPrefs.getString(PREF_SETTINGS_FOCUS_SIGNATURE, null);
        if (savedFocusTab == activeTab[0] && "right".equals(savedFocusArea)) {
            root.post(() -> {
                rightScroll.scrollTo(0, Math.max(0, navigationPrefs.getInt(rightScrollKey(activeTab[0]), 0)));
                rightScroll.post(() -> {
                    View restored = resolveFocusPath(rightContent, savedFocusPath, savedFocusSignature);
                    if (restored != null && restored.requestFocus()) return;
                    if (activeSidebarView[0] != null) activeSidebarView[0].requestFocus();
                    else profileCard.requestFocus();
                });
            });
        } else if (savedFocusTab == activeTab[0] && "sidebar".equals(savedFocusArea)) {
            root.post(() -> {
                View restored = resolveFocusPath(leftCol, savedFocusPath, savedFocusSignature);
                if (restored != null && restored.requestFocus()) return;
                if (activeSidebarView[0] != null) activeSidebarView[0].requestFocus();
                else profileCard.requestFocus();
            });
        } else {
            root.post(() -> {
                if (activeSidebarView[0] != null) activeSidebarView[0].requestFocus();
                else profileCard.requestFocus();
            });
        }
    }

    private static void addSectionHeader(LinearLayout root, Typeface font, String title) {
        TextView tv = new TextView(root.getContext());
        tv.setText(title);
        tv.setTextSize(TvUiTokens.SECTION_LABEL_SP);
        tv.setTextColor(TvUiTokens.ACCENT_SECTION);
        tv.setTypeface(font, Typeface.BOLD);
        tv.setLetterSpacing(0.08f);
        tv.setPadding(0, (int) (16 * root.getContext().getResources().getDisplayMetrics().density),
                0, (int) (7 * root.getContext().getResources().getDisplayMetrics().density));
        root.addView(tv);
    }

    private static void addHeader(LinearLayout root, Typeface font, String title) {
        TextView tv = new TextView(root.getContext());
        tv.setText(title);
        tv.setTextSize(TvUiTokens.PAGE_HEADER_SP);
        tv.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tv.setTypeface(font, Typeface.BOLD);
        tv.setPadding(0, 0, 0, (int) (14 * root.getContext().getResources().getDisplayMetrics().density));
        root.addView(tv);
    }

    private static LinearLayout buildCardRow(Context ctx, float density, Typeface font, String title, String subtitle, Runnable action) {
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding((int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density), (int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density));
        card.setFocusable(true);
        card.setClickable(true);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(TvUiTokens.SURFACE);
        bg.setCornerRadius(TvUiTokens.CARD_RADIUS_DP * density);
        card.setBackground(bg);

        TextView tvTitle = new TextView(ctx);
        tvTitle.setText(title);
        tvTitle.setTextSize(TvUiTokens.CARD_TITLE_SP);
        tvTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tvTitle.setTypeface(font, Typeface.BOLD);
        card.addView(tvTitle);

        if (subtitle != null && !subtitle.isEmpty()) {
            TextView tvSub = new TextView(ctx);
            tvSub.setText(subtitle);
            tvSub.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
            tvSub.setTextColor(TvUiTokens.TEXT_SECONDARY);
            tvSub.setTypeface(font, Typeface.NORMAL);
            tvSub.setPadding(0, (int) (2 * density), 0, 0);
            card.addView(tvSub);
        }

        card.setOnFocusChangeListener((v, hasFocus) -> {
            GradientDrawable g = new GradientDrawable();
            g.setColor(hasFocus ? TvUiTokens.SURFACE_FOCUSED : TvUiTokens.SURFACE);
            g.setCornerRadius(TvUiTokens.CARD_RADIUS_DP * density);
            if (hasFocus) g.setStroke((int) (2f * density), TvUiTokens.FOCUS_BORDER);
            v.setBackground(g);
            v.animate().scaleX(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).scaleY(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).setDuration(120).start();
        });

        card.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (sDpadLeftTarget != null) {
                    sDpadLeftTarget.run();
                    return true;
                }
            }
            return false;
        });

        card.setOnClickListener(v -> {
            if (action != null) action.run();
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) (TvUiTokens.CARD_BOTTOM_MARGIN_DP * density);
        card.setLayoutParams(lp);
        return card;
    }

    private static LinearLayout buildSwitchRow(Context ctx, float density, Typeface font, String title, String subtitle, boolean isChecked, SwitchCallback callback) {
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding((int) (16 * density), (int) (10 * density), (int) (16 * density), (int) (10 * density));
        card.setFocusable(true);
        card.setClickable(true);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xFF272A33);
        bg.setCornerRadius(12 * density);
        card.setBackground(bg);

        LinearLayout textCol = new LinearLayout(ctx);
        textCol.setOrientation(LinearLayout.VERTICAL);

        TextView tvTitle = new TextView(ctx);
        tvTitle.setText(title);
        tvTitle.setTextSize(14f);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTypeface(font, Typeface.BOLD);
        textCol.addView(tvTitle);

        if (subtitle != null && !subtitle.isEmpty()) {
            TextView tvSub = new TextView(ctx);
            tvSub.setText(subtitle);
            tvSub.setTextSize(12);
            tvSub.setTextColor(0xFF8E929E);
            tvSub.setTypeface(font, Typeface.NORMAL);
            tvSub.setPadding(0, (int) (2 * density), 0, 0);
            textCol.addView(tvSub);
        }

        card.addView(textCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Switch sw = new Switch(ctx);
        sw.setChecked(isChecked);
        sw.setFocusable(false);
        sw.setClickable(false);
        card.addView(sw);

        card.setOnFocusChangeListener((v, hasFocus) -> {
            GradientDrawable g = new GradientDrawable();
            g.setColor(hasFocus ? 0xFF353844 : 0xFF272A33);
            g.setCornerRadius(12 * density);
            if (hasFocus) g.setStroke((int) (2f * density), 0xFFFFFFFF);
            v.setBackground(g);
            v.animate().scaleX(hasFocus ? 1.015f : 1.0f).scaleY(hasFocus ? 1.015f : 1.0f).setDuration(120).start();
        });

        card.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (sDpadLeftTarget != null) {
                    sDpadLeftTarget.run();
                    return true;
                }
            }
            return false;
        });

        card.setOnClickListener(v -> {
            SoundEffects.playSelect();
            boolean newVal = !sw.isChecked();
            sw.setChecked(newVal);
            if (callback != null) callback.onChanged(newVal);
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) (8 * density);
        card.setLayoutParams(lp);
        return card;
    }

    private static LinearLayout buildInfoCard(Context ctx, float density, Typeface font, String title, String value) {
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding((int) (16 * density), (int) (10 * density), (int) (16 * density), (int) (10 * density));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xFF22252E);
        bg.setCornerRadius(12 * density);
        card.setBackground(bg);

        TextView tvTitle = new TextView(ctx);
        tvTitle.setText(title);
        tvTitle.setTextSize(13.5f);
        tvTitle.setTextColor(0xFFE2E8F0);
        tvTitle.setTypeface(font, Typeface.BOLD);
        card.addView(tvTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvVal = new TextView(ctx);
        tvVal.setText(value);
        tvVal.setTextSize(13f);
        tvVal.setTextColor(0xFF38BDF8);
        tvVal.setTypeface(font, Typeface.NORMAL);
        card.addView(tvVal);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) (8 * density);
        card.setLayoutParams(lp);
        return card;
    }

    private static void showEditNameDialog(MainActivity activity, float density, Typeface font, EditNameCallback callback) {
        EditText et = new EditText(activity);
        et.setText(ProfileManager.getProfileName(activity));
        et.setSelection(et.getText().length());
        et.setTextColor(0xFFFFFFFF);
        et.setTextSize(16);
        et.setTypeface(font, Typeface.NORMAL);
        et.setPadding((int) (16 * density), (int) (12 * density), (int) (16 * density), (int) (12 * density));

        GradientDrawable etBg = new GradientDrawable();
        etBg.setColor(0xFF282A33);
        etBg.setCornerRadius(10 * density);
        etBg.setStroke((int) (1 * density), 0xFF4A4E5C);
        et.setBackground(etBg);
        TvFocusIndicator.applyRoundedTextFocus(et, density, 10f,
                0xFF282A33, 0xFF333B4B, 0xFF4A4E5C, 0xFF7DD3FC,
                0xFFFFFFFF, 0xFFFFFFFF, 1.0f);

        FrameLayout container = new FrameLayout(activity);
        container.setPadding((int) (24 * density), (int) (12 * density), (int) (24 * density), (int) (8 * density));
        container.addView(et);

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setTitle(AppLanguage.strEditName(activity))
                .setView(container)
                .setPositiveButton(R.string.save_action, (d, which) -> {
                    String newName = et.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        if (callback != null) callback.onSave(newName);
                    }
                })
                .setNegativeButton(AppLanguage.strCancel(activity), (d, which) -> d.dismiss())
                .create();
        dialog.show();
        TvDialogStyle.apply(dialog, activity, 520, 0);
    }

    private static void showChangePinDialog(MainActivity activity, float density, Typeface font) {
        ChildModeManager.promptPin(activity, activity.getString(R.string.verify_current_pin_title),
                activity.getString(R.string.verify_current_pin_message), new ChildModeManager.PinCallback() {
            @Override
            public void onSuccess() {
                EditText newPin = new EditText(activity);
                newPin.setHint(R.string.new_pin_hint);
                newPin.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                newPin.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(4)});
                newPin.setSingleLine(true);

                EditText confirmPin = new EditText(activity);
                confirmPin.setHint(R.string.confirm_pin_hint);
                confirmPin.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                confirmPin.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(4)});
                confirmPin.setSingleLine(true);

                LinearLayout fields = new LinearLayout(activity);
                fields.setOrientation(LinearLayout.VERTICAL);
                int pad = (int) (22 * density);
                fields.setPadding(pad, (int) (8 * density), pad, 0);
                fields.addView(newPin);
                fields.addView(confirmPin);

                AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                        .setTitle(R.string.new_pin_title)
                        .setMessage(R.string.new_pin_message)
                        .setView(fields)
                        .setNegativeButton(R.string.cancel_action, (d, w) -> d.dismiss())
                        .setPositiveButton(R.string.save_action, null)
                        .create();
                dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String first = newPin.getText().toString();
                    String second = confirmPin.getText().toString();
                    if (!first.matches("[0-9]{4}")) {
                        newPin.setError(activity.getString(R.string.pin_exactly_four_digits));
                        newPin.requestFocus();
                        return;
                    }
                    if (!first.equals(second)) {
                        confirmPin.setError(activity.getString(R.string.pin_mismatch));
                        confirmPin.requestFocus();
                        return;
                    }
                    ChildModeManager.setPin(activity, first);
                    dialog.dismiss();
                    TvFeedback.show(activity, R.string.pin_updated, Toast.LENGTH_SHORT);
                }));
                dialog.show();
                TvDialogStyle.apply(dialog, activity, 520, 0);
            }
            @Override
            public void onCancelled() {}
        });
    }

    /** Require parent PIN before changing either the locked-app or hidden-app policy. */
    private static void showAppLockPicker(MainActivity activity, float density, Typeface font, boolean isHideMode) {
        ChildModeManager.promptPin(activity, activity.getString(R.string.verify_parent_pin_title),
                activity.getString(R.string.verify_parent_pin_message, activity.getString(isHideMode ? R.string.hidden_apps_label : R.string.locked_apps_label)),
                new ChildModeManager.PinCallback() {
                    @Override public void onSuccess() {
                        showAppLockPickerAuthorized(activity, density, font, isHideMode);
                    }
                    @Override public void onCancelled() { }
                });
    }

    private static void showAppLockPickerAuthorized(MainActivity activity, float density, Typeface font, boolean isHideMode) {
        PackageManager pm = activity.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
        if (apps == null || apps.isEmpty()) {
            mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            apps = pm.queryIntentActivities(mainIntent, 0);
        }

        String selfPkg = activity.getPackageName();
        class AppEntry {
            final String pkg;
            final String label;
            final android.graphics.drawable.Drawable icon;
            AppEntry(String pkg, String label, android.graphics.drawable.Drawable icon) {
                this.pkg = pkg;
                this.label = label;
                this.icon = icon;
            }
        }

        List<AppEntry> entryList = new ArrayList<>();
        if (apps != null) {
            for (ResolveInfo ri : apps) {
                String p = ri.activityInfo.packageName;
                if (p.equals(selfPkg)) continue;
                String lbl = ri.loadLabel(pm).toString();
                entryList.add(new AppEntry(p, lbl, ri.loadIcon(pm)));
            }
        }
        Collections.sort(entryList, (a, b) -> a.label.compareToIgnoreCase(b.label));

        final Set<String> targetSet = isHideMode ? ChildModeManager.getHiddenApps(activity) : ChildModeManager.getLockedApps(activity);

        LinearLayout pickerLayout = new LinearLayout(activity);
        pickerLayout.setOrientation(LinearLayout.VERTICAL);
        pickerLayout.setPadding((int) (22 * density), (int) (18 * density), (int) (22 * density), (int) (16 * density));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xFF1E2026);
        bg.setCornerRadius(20 * density);
        bg.setStroke((int) (1.5f * density), 0xFF2F323B);
        pickerLayout.setBackground(bg);

        TextView tvTitle = new TextView(activity);
        tvTitle.setText(isHideMode ? R.string.hide_apps_title : R.string.lock_apps_title);
        tvTitle.setTextSize(17);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTypeface(font, Typeface.BOLD);
        pickerLayout.addView(tvTitle);

        TextView tvSub = new TextView(activity);
        tvSub.setText(isHideMode ? R.string.hide_apps_description : R.string.lock_apps_description);
        tvSub.setTextSize(12);
        tvSub.setTextColor(0xFF8E929E);
        tvSub.setTypeface(font, Typeface.NORMAL);
        tvSub.setPadding(0, (int) (2 * density), 0, (int) (10 * density));
        pickerLayout.addView(tvSub);

        ScrollView sv = new ScrollView(activity);
        sv.setVerticalScrollBarEnabled(true);
        LinearLayout list = new LinearLayout(activity);
        list.setOrientation(LinearLayout.VERTICAL);

        for (AppEntry app : entryList) {
            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding((int) (12 * density), (int) (8 * density), (int) (12 * density), (int) (8 * density));
            row.setFocusable(true);
            row.setClickable(true);

            GradientDrawable rbg = new GradientDrawable();
            rbg.setColor(0xFF242730);
            rbg.setCornerRadius(10 * density);
            row.setBackground(rbg);

            ImageView iv = new ImageView(activity);
            iv.setImageDrawable(app.icon);
            LinearLayout.LayoutParams ivlp = new LinearLayout.LayoutParams((int) (28 * density), (int) (28 * density));
            ivlp.setMarginEnd((int) (10 * density));
            row.addView(iv, ivlp);

            TextView tv = new TextView(activity);
            tv.setText(app.label);
            tv.setTextSize(13.5f);
            tv.setTextColor(0xFFFFFFFF);
            tv.setTypeface(font, Typeface.BOLD);
            row.addView(tv, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            CheckBox cb = new CheckBox(activity);
            cb.setChecked(targetSet.contains(app.pkg));
            cb.setFocusable(false);
            cb.setClickable(false);
            row.addView(cb);

            row.setOnFocusChangeListener((v, hasFocus) -> {
                GradientDrawable g = new GradientDrawable();
                g.setColor(hasFocus ? 0xFF353946 : 0xFF242730);
                g.setCornerRadius(10 * density);
                if (hasFocus) g.setStroke((int) (2f * density), 0xFFFFFFFF);
                v.setBackground(g);
                v.animate().scaleX(hasFocus ? 1.015f : 1.0f).scaleY(hasFocus ? 1.015f : 1.0f).setDuration(120).start();
            });

            row.setOnClickListener(v -> {
                SoundEffects.playSelect();
                boolean nowChecked = !cb.isChecked();
                cb.setChecked(nowChecked);
                if (isHideMode) {
                    ChildModeManager.setAppHidden(activity, app.pkg, nowChecked);
                } else {
                    ChildModeManager.setAppLocked(activity, app.pkg, nowChecked);
                }
            });

            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rlp.bottomMargin = (int) (6 * density);
            list.addView(row, rlp);
        }

        sv.addView(list);
        LinearLayout.LayoutParams svLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (260 * density));
        svLp.bottomMargin = (int) (12 * density);
        pickerLayout.addView(sv, svLp);

        AlertDialog[] pickerHolder = new AlertDialog[1];
        Button btnDone = new Button(activity);
        btnDone.setText(R.string.done_action);
        btnDone.setTextSize(13.5f);
        btnDone.setTextColor(0xFFFFFFFF);
        btnDone.setTypeface(font, Typeface.BOLD);

        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setColor(0xFF10B981);
        btnBg.setCornerRadius(10 * density);
        btnDone.setBackground(btnBg);

        btnDone.setOnFocusChangeListener((v, hasFocus) -> {
            GradientDrawable g = new GradientDrawable();
            g.setColor(hasFocus ? 0xFF059669 : 0xFF10B981);
            g.setCornerRadius(10 * density);
            if (hasFocus) g.setStroke((int) (2 * density), 0xFFFFFFFF);
            v.setBackground(g);
        });

        btnDone.setOnClickListener(v -> {
            SoundEffects.playSelect();
            if (pickerHolder[0] != null) pickerHolder[0].dismiss();
            activity.reloadTiles();
        });

        pickerLayout.addView(btnDone, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (42 * density)));

        AlertDialog.Builder pb = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        pb.setView(pickerLayout);
        AlertDialog d = pb.create();
        pickerHolder[0] = d;
        d.show();
        TvDialogStyle.apply(d, activity, 520, 0);
    }

    private static void showDefaultLauncherDialog(MainActivity activity, float density, Typeface font, boolean isDefault, String activePkg) {
        if (activity == null || activity.isFinishing()) return;
        SoundEffects.playSelect();

        ScrollView scroll = new ScrollView(activity);
        scroll.setFillViewport(true);

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding((int) (24 * density), (int) (20 * density), (int) (24 * density), (int) (20 * density));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xFF1E2026);
        bg.setCornerRadius(18 * density);
        bg.setStroke((int) (1.5f * density), 0xFF2F323B);
        root.setBackground(bg);

        TextView tvTitle = new TextView(activity);
        tvTitle.setText(R.string.set_default_home_title);
        tvTitle.setTextSize(18f);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTypeface(font, Typeface.BOLD);
        tvTitle.setPadding(0, 0, 0, (int) (8 * density));
        root.addView(tvTitle);

        TextView tvDesc = new TextView(activity);
        String desc = isDefault
                ? activity.getString(R.string.default_launcher_success)
                : activity.getString(R.string.default_launcher_explanation, activePkg);
        tvDesc.setText(desc);
        tvDesc.setTextSize(13f);
        tvDesc.setTextColor(isDefault ? 0xFF34D399 : 0xFFCBD5E1);
        tvDesc.setTypeface(font, Typeface.NORMAL);
        tvDesc.setPadding(0, 0, 0, (int) (16 * density));
        root.addView(tvDesc);

        AlertDialog[] dialogHolder = new AlertDialog[1];

        // Option 1: Open TV Settings
        LinearLayout btnSettings = buildCardRow(activity, density, font,
                activity.getString(R.string.open_home_picker_title),
                activity.getString(R.string.open_home_picker_summary),
                () -> {
                    try {
                        Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                        activity.startActivity(intent);
                    } catch (Exception e1) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                            activity.startActivity(intent);
                        } catch (Exception e2) {
                            TvFeedback.show(activity, R.string.launcher_settings_unsupported, Toast.LENGTH_SHORT);
                        }
                    }
                });
        root.addView(btnSettings);

        // Option 2: ADB Command guidance
        LinearLayout btnAdb = buildCardRow(activity, density, font,
                activity.getString(R.string.set_home_adb_title),
                activity.getString(R.string.adb_command_guidance, isDefault ? "" : activity.getString(R.string.disable_old_launcher_hint, activePkg)),
                () -> {
                    TvFeedback.show(activity, R.string.adb_home_command_hint, Toast.LENGTH_LONG);
                });
        root.addView(btnAdb);

        // Option 3: Phone Installer
        LinearLayout btnPhone = buildCardRow(activity, density, font,
                activity.getString(R.string.phone_installer_title),
                activity.getString(R.string.phone_installer_summary),
                () -> {
                    TvFeedback.show(activity, R.string.installer_phone_hint, Toast.LENGTH_SHORT);
                });
        root.addView(btnPhone);

        // Close button
        Button btnClose = new Button(activity);
        btnClose.setText(R.string.close_label);
        btnClose.setTextColor(0xFFFFFFFF);
        btnClose.setTypeface(font, Typeface.BOLD);
        btnClose.setTextSize(14f);
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setColor(0xFF374151);
        closeBg.setCornerRadius(10 * density);
        btnClose.setBackground(closeBg);
        btnClose.setFocusable(true);
        btnClose.setPadding((int) (16 * density), (int) (8 * density), (int) (16 * density), (int) (8 * density));
        btnClose.setOnFocusChangeListener((v, hasFocus) -> {
            GradientDrawable g = new GradientDrawable();
            g.setColor(hasFocus ? 0xFF4B5563 : 0xFF374151);
            g.setCornerRadius(10 * density);
            if (hasFocus) g.setStroke((int) (2 * density), 0xFFFFFFFF);
            v.setBackground(g);
        });
        btnClose.setOnClickListener(v -> {
            SoundEffects.playSelect();
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
        });

        LinearLayout.LayoutParams closeLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (42 * density));
        closeLp.topMargin = (int) (12 * density);
        root.addView(btnClose, closeLp);

        scroll.addView(root);

        AlertDialog.Builder pb = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        pb.setView(scroll);
        AlertDialog d = pb.create();
        dialogHolder[0] = d;
        d.show();
        TvDialogStyle.apply(d, activity, 620, 520);

        btnSettings.requestFocus();
    }

    interface EditNameCallback {
        void onSave(String name);
    }

    interface SwitchCallback {
        void onChanged(boolean isChecked);
    }
}

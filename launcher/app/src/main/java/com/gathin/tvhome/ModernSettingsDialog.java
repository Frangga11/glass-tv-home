package com.gathin.tvhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
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

import com.gathin.tvhome.qrcode.QrBitmapUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
    private static WeakReference<MainActivity> sSettingsHost;
    private static WeakReference<AlertDialog> sSettingsDialog;
    private static WeakReference<TextView> sSettingsNetworkStatus;
    private static WeakReference<LinearLayout> sSettingsRightContent;
    private static WeakReference<ScrollView> sSettingsRightScroll;
    private static WeakReference<Runnable> sSettingsPaneRefresh;
    private static WeakReference<int[]> sSettingsActiveTab;
    private static WeakReference<View[]> sSettingsActiveSidebarView;
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

    /** Accepts the pre-search focus signature so existing saved focus paths survive this UI refresh. */
    private static String legacyFocusSignature(View target) {
        if (target == null) return "";
        String label = "";
        if (target instanceof EditText) {
            CharSequence hint = ((EditText) target).getHint();
            if (hint != null) label = hint.toString();
        } else if (target instanceof TextView) {
            CharSequence text = ((TextView) target).getText();
            if (text != null) label = text.toString();
        }
        if (label.length() > 160) label = label.substring(0, 160);
        return target.getClass().getName() + "#" + target.getId() + "#" + label;
    }

    private static View resolveLegacySidebarFocusPath(ViewGroup leftCol, View searchBox,
                                                          String encodedPath, String signature) {
        int[] indexes = SettingsFocusPath.decode(encodedPath);
        if (indexes == null || indexes.length == 0) return null;
        // The old sidebar began with Search. It now lives in the V06 header.
        if (indexes[0] == 0) {
            return signature != null && signature.equals(legacyFocusSignature(searchBox))
                    ? searchBox : null;
        }
        indexes[0]--;
        return resolveFocusPath(leftCol, SettingsFocusPath.encode(indexes), signature);
    }

    private static View resolveLegacyRightFocusPath(ViewGroup rightContent, int tab,
                                                      String encodedPath, String signature) {
        int[] indexes = SettingsFocusPath.decode(encodedPath);
        if (indexes == null || indexes.length == 0) return null;
        // V06 adds a non-focusable active-profile summary after the existing page title.
        if (NavigationState.normalizeSettingsTab(tab) == TAB_PROFILE && indexes[0] >= 1) {
            indexes[0]++;
        }
        return resolveFocusPath(rightContent, SettingsFocusPath.encode(indexes), signature);
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
                && (signature.equals(focusSignature(current))
                || signature.equals(legacyFocusSignature(current))) ? current : null;
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
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setClipChildren(false);
        root.setClipToPadding(false);
        root.setPadding((int) (12 * density), (int) (8 * density), (int) (12 * density), (int) (12 * density));

        // Dark matte rounded container matching screenshot
        GradientDrawable dialogBg = new GradientDrawable();
        dialogBg.setColor(TvUiTokens.SHELL);
        dialogBg.setCornerRadius(24 * density);
        dialogBg.setStroke((int) (1.5f * density), TvUiTokens.BORDER);
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
        leftBg.setColor(TvUiTokens.GLASS_VEIL_COLOR);
        leftCol.setBackground(leftBg);

        final LinearLayout profileCard = new LinearLayout(activity);
        final LinearLayout rightContent = new LinearLayout(activity);
        LinearLayout header = new LinearLayout(activity);

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
        searchIcon.setColorFilter(TvUiTokens.TEXT_SECONDARY);
        LinearLayout.LayoutParams sIconLp = new LinearLayout.LayoutParams((int) (15 * density), (int) (15 * density));
        sIconLp.setMarginEnd((int) (8 * density));
        searchBox.addView(searchIcon, sIconLp);

        TextView searchPrompt = new TextView(activity);
        searchPrompt.setText(AppLanguage.strSettingsSearch(activity));
        searchPrompt.setTextSize(12.5f);
        searchPrompt.setTextColor(TvUiTokens.TEXT_SECONDARY);
        searchPrompt.setTypeface(outfit, Typeface.NORMAL);
        searchBox.addView(searchPrompt);

        searchBox.setBackground(TvFocusIndicator.createRoundedStateBackground(
                density, 10f, TvUiTokens.INPUT_SURFACE, TvUiTokens.SURFACE_FOCUSED,
                TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER));
        searchBox.setOnFocusChangeListener((v, hasFocus) -> {
            searchPrompt.setTextColor(hasFocus ? TvUiTokens.TEXT_PRIMARY : TvUiTokens.TEXT_SECONDARY);
            searchIcon.setColorFilter(hasFocus ? TvUiTokens.TEXT_PRIMARY : TvUiTokens.TEXT_SECONDARY);
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

        // Search is inserted after the brand block is built, preserving the V06 header order.

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
        tvProfileName.setText(AppLanguage.strProfileTitle(activity));
        tvProfileName.setTextSize(14f);
        tvProfileName.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tvProfileName.setTypeface(outfit, Typeface.BOLD);
        tvProfileName.setSingleLine(true);
        profileTextCol.addView(tvProfileName);

        TextView tvProfileSub = new TextView(activity);
        tvProfileSub.setText(ProfileManager.getProfileName(activity));
        tvProfileSub.setTextSize(11f);
        tvProfileSub.setTextColor(TvUiTokens.TEXT_SECONDARY);
        tvProfileSub.setTypeface(outfit, Typeface.NORMAL);
        profileTextCol.addView(tvProfileSub);

        profileCard.addView(profileTextCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        LinearLayout.LayoutParams pcLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (52 * density));
        pcLp.bottomMargin = (int) (8 * density);
        leftCol.addView(profileCard, pcLp);

        // Vertical divider between Left and Right
        View verticalDivider = new View(activity);
        verticalDivider.setBackgroundColor(TvUiTokens.BORDER);
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
        navItems.add(new NavItem(TAB_HOME_SCREEN, AppLanguage.strHomeScreen(activity), R.drawable.ic_home_screen, TvUiTokens.SURFACE_IDLE));
        navItems.add(new NavItem(TAB_APPEARANCE, AppLanguage.strAppearance(activity), R.drawable.ic_appearance, TvUiTokens.SURFACE_IDLE));
        navItems.add(new NavItem(TAB_STATUS_WEATHER, AppLanguage.strStatusBarWeather(activity), R.drawable.ic_screen_weather, TvUiTokens.SURFACE_IDLE));
        navItems.add(new NavItem(TAB_EFFECTS, AppLanguage.strVisualEffects(activity), R.drawable.ic_settings, TvUiTokens.SURFACE_IDLE));
        navItems.add(new NavItem(TAB_STANDBY, AppLanguage.strStandByMode(activity), R.drawable.ic_standby_clock, TvUiTokens.SURFACE_IDLE));
        String deviceLabel = AppLanguage.strDeviceSettings(activity);
        if (AppUpdateManager.isUpdateAvailable(activity)) {
            deviceLabel += " 🔴";
        }
        navItems.add(new NavItem(TAB_DEVICE_SETTINGS, deviceLabel, R.drawable.ic_tv_display, TvUiTokens.SURFACE_IDLE));

        Runnable[] renderRightPane = new Runnable[1];
        int[] lastRenderedTab = new int[]{activeTab[0]};
        boolean[] hasRenderedPane = new boolean[]{false};

        Runnable updateSidebarSelection = () -> {
            navigationPrefs.edit().putInt(NavigationState.SETTINGS_TAB, NavigationState.normalizeSettingsTab(activeTab[0])).apply();
            boolean isProfile = (activeTab[0] == TAB_PROFILE);
            GradientDrawable pcBg = new GradientDrawable();
            pcBg.setColor(isProfile ? TvUiTokens.NAV_PROFILE_SELECTED : TvUiTokens.NAV_PROFILE_IDLE);
            pcBg.setCornerRadius(TvUiTokens.CARD_RADIUS_DP * density);
            if (isProfile) pcBg.setStroke((int) (1.5f * density), TvUiTokens.NAV_SELECTION_BORDER);
            profileCard.setBackground(pcBg);

            ProfileManager.UserProfile sidebarActive = ProfileManager.getActiveProfile(activity);
            profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, sidebarActive,
                    Math.round(38 * density), ChildModeManager.isChildModeEnabled(activity, sidebarActive.id)));
            tvProfileName.setText(AppLanguage.strProfileTitle(activity));
            tvProfileSub.setText(ProfileManager.getProfileName(activity));

            for (NavItem item : navItems) {
                if (item.rowView == null) continue;
                boolean isSelected = (activeTab[0] == item.tabId);
                GradientDrawable rbg = new GradientDrawable();
                rbg.setColor(isSelected ? TvUiTokens.NAV_ITEM_SELECTED : Color.TRANSPARENT);
                rbg.setCornerRadius(TvUiTokens.NAV_ROW_RADIUS_DP * density);
                if (isSelected) rbg.setStroke((int) (1 * density), TvUiTokens.NAV_SELECTION_BORDER);
                item.rowView.setBackground(rbg);
            }
        };

        View[] activeSidebarView = new View[]{profileCard};
        sDpadLeftTarget = () -> {
            if (activeSidebarView[0] != null) {
                activeSidebarView[0].requestFocus();
            }
        };

        profileCard.setForeground(activity.getDrawable(R.drawable.bg_tv_focus_outline));

        profileCard.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                activeSidebarView[0] = profileCard;
                activeTab[0] = TAB_PROFILE;
                updateSidebarSelection.run();
                if (renderRightPane[0] != null) renderRightPane[0].run();
            }
            v.animate().scaleX(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).scaleY(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
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
            row.setPadding((int) (TvUiTokens.NAV_ROW_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.NAV_ROW_PADDING_VERTICAL_DP * density), (int) (TvUiTokens.NAV_ROW_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.NAV_ROW_PADDING_VERTICAL_DP * density));
            row.setFocusable(true);
            row.setClickable(true);

            // Squircle Icon
            FrameLayout iconSquircle = new FrameLayout(activity);
            GradientDrawable sqBg = new GradientDrawable();
            sqBg.setColor(item.squircleBg);
            sqBg.setCornerRadius(TvUiTokens.NAV_ICON_RADIUS_DP * density);
            iconSquircle.setBackground(sqBg);

            ImageView iv = new ImageView(activity);
            iv.setImageResource(item.iconRes);
            iv.setColorFilter(TvUiTokens.NAV_ICON_TINT);
            FrameLayout.LayoutParams ivLp = new FrameLayout.LayoutParams((int) (TvUiTokens.NAV_ICON_GLYPH_DP * density), (int) (TvUiTokens.NAV_ICON_GLYPH_DP * density), Gravity.CENTER);
            iconSquircle.addView(iv, ivLp);

            LinearLayout.LayoutParams sqLp = new LinearLayout.LayoutParams((int) (TvUiTokens.NAV_ICON_CONTAINER_DP * density), (int) (TvUiTokens.NAV_ICON_CONTAINER_DP * density));
            sqLp.setMarginEnd((int) (TvUiTokens.NAV_ICON_MARGIN_END_DP * density));
            row.addView(iconSquircle, sqLp);

            // Label
            TextView lbl = new TextView(activity);
            lbl.setText(item.label);
            lbl.setTextSize(TvUiTokens.NAV_ITEM_TEXT_SP);
            lbl.setTextColor(TvUiTokens.NAV_ITEM_TEXT);
            lbl.setTypeface(outfit, Typeface.NORMAL);
            row.addView(lbl);

            row.setForeground(activity.getDrawable(R.drawable.bg_tv_focus_outline));
            row.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    activeSidebarView[0] = row;
                    activeTab[0] = item.tabId;
                    updateSidebarSelection.run();
                    if (renderRightPane[0] != null) renderRightPane[0].run();
                }
                v.animate().scaleX(hasFocus ? TvUiTokens.NAV_FOCUS_SCALE : 1.0f).scaleY(hasFocus ? TvUiTokens.NAV_FOCUS_SCALE : 1.0f).setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
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

            LinearLayout.LayoutParams rLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (TvUiTokens.NAV_ROW_HEIGHT_DP * density));
            rLp.bottomMargin = (int) (TvUiTokens.NAV_ROW_BOTTOM_MARGIN_DP * density);
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
                tvTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
                tvTitle.setTypeface(outfit, Typeface.BOLD);
                rightContent.addView(tvTitle);

                ProfileManager.UserProfile activeP = ProfileManager.getActiveProfile(activity);
                ImageView[] activeProfileListAvatar = new ImageView[1];

                // V06 identity/status summary; profile storage and avatar remain owned by ProfileManager.
                boolean summaryChildMode = ChildModeManager.isChildModeEnabled(activity, activeP.id);
                LinearLayout profileSummary = new LinearLayout(activity);
                profileSummary.setOrientation(LinearLayout.HORIZONTAL);
                profileSummary.setGravity(Gravity.CENTER_VERTICAL);
                profileSummary.setPadding((int) (18 * density), (int) (14 * density),
                        (int) (18 * density), (int) (14 * density));
                GradientDrawable profileSummaryBg = new GradientDrawable();
                profileSummaryBg.setColor(TvUiTokens.SURFACE_SELECTED);
                profileSummaryBg.setCornerRadius(TvUiTokens.CARD_RADIUS_DP * density);
                profileSummaryBg.setStroke(Math.max(1, (int) density), TvUiTokens.BORDER);
                profileSummary.setBackground(profileSummaryBg);
                ImageView summaryAvatar = new ImageView(activity);
                summaryAvatar.setImageDrawable(ProfileManager.getAvatarDrawable(activity, activeP,
                        Math.round(64 * density), summaryChildMode));
                LinearLayout.LayoutParams summaryAvatarLp = new LinearLayout.LayoutParams(
                        (int) (64 * density), (int) (64 * density));
                summaryAvatarLp.setMarginEnd((int) (16 * density));
                profileSummary.addView(summaryAvatar, summaryAvatarLp);
                LinearLayout summaryText = new LinearLayout(activity);
                summaryText.setOrientation(LinearLayout.VERTICAL);
                TextView summaryName = new TextView(activity);
                summaryName.setText(activeP.name);
                summaryName.setTextSize(TvUiTokens.SECTION_TITLE_SP);
                summaryName.setTextColor(TvUiTokens.TEXT_PRIMARY);
                summaryName.setTypeface(outfit, Typeface.BOLD);
                summaryText.addView(summaryName);
                TextView summaryState = new TextView(activity);
                summaryState.setText(activity.getString(R.string.active_label)
                        + (summaryChildMode ? " · " + activity.getString(R.string.child_mode_suffix) : ""));
                summaryState.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
                summaryState.setTextColor(summaryChildMode ? TvUiTokens.WARNING : TvUiTokens.SUCCESS);
                summaryText.addView(summaryState);
                profileSummary.addView(summaryText, new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                LinearLayout.LayoutParams profileSummaryLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                profileSummaryLp.bottomMargin = (int) (10 * density);
                rightContent.addView(profileSummary, 1, profileSummaryLp);

                // Section 1: Active Profile Avatars
                TextView tvAvatars = new TextView(activity);
                tvAvatars.setText(activity.getString(R.string.active_profile_avatar_header, activeP.name.toUpperCase()));
                tvAvatars.setTextSize(11.5f);
                tvAvatars.setTextColor(TvUiTokens.TEXT_SECONDARY);
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
                    frame.setContentDescription(ProfileManager.AVATAR_NAMES[idx]);
                    frame.setTag("profile.avatar.builtin." + idx);

                    int frameDim = (int) (60 * density);
                    LinearLayout.LayoutParams fLp = new LinearLayout.LayoutParams(frameDim, frameDim);
                    fLp.setMarginEnd((int) (12 * density));
                    frame.setLayoutParams(fLp);

                    // Outer White Ring for Selected Avatar
                    View ring = new View(activity);
                    GradientDrawable ringBg = new GradientDrawable();
                    ringBg.setShape(GradientDrawable.OVAL);
                    ringBg.setColor(Color.TRANSPARENT);
                    ringBg.setStroke((int) (3f * density), TvUiTokens.NAV_SELECTION_BORDER);
                    ring.setBackground(ringBg);
                    ring.setVisibility(ProfileManager.isBuiltInAvatarSelected(activeP, idx) ? View.VISIBLE : View.GONE);
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
                        // Commit the built-in index and custom-photo reset as one profile transaction.
                        if (!ProfileManager.selectBuiltInAvatar(activity, activeP.id, idx)) {
                            TvFeedback.show(activity, R.string.avatar_delete_failed, Toast.LENGTH_LONG);
                            return;
                        }
                        ProfileManager.UserProfile refreshedProfile = ProfileManager.getActiveProfile(activity);
                        for (int k = 0; k < 7; k++) {
                            if (avatarRings[k] != null) {
                                avatarRings[k].setVisibility(ProfileManager.isBuiltInAvatarSelected(refreshedProfile, k)
                                        ? View.VISIBLE : View.GONE);
                            }
                        }
                        boolean refreshedChildMode = ChildModeManager.isChildModeEnabled(activity, refreshedProfile.id);
                        summaryAvatar.setImageDrawable(ProfileManager.getAvatarDrawable(activity, refreshedProfile,
                                Math.round(64 * density), refreshedChildMode));
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
                uploadAvatarButton.setContentDescription(activity.getString(R.string.avatar_upload_qr_action));
                uploadAvatarButton.setTextColor(TvUiTokens.TEXT_PRIMARY);
                uploadAvatarButton.setTextSize(13);
                uploadAvatarButton.setAllCaps(false);
                uploadAvatarButton.setFocusable(true);
                GradientDrawable uploadAvatarBg = new GradientDrawable();
                uploadAvatarBg.setColor(TvUiTokens.SURFACE_SELECTED);
                uploadAvatarBg.setCornerRadius(10 * density);
                uploadAvatarButton.setBackground(uploadAvatarBg);
                TvFocusIndicator.applyRoundedTextFocus(uploadAvatarButton, density, 10f,
                        TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER,
                        TvUiTokens.FOCUS_BORDER, TvUiTokens.TEXT_PRIMARY,
                        TvUiTokens.TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
                uploadAvatarButton.setOnClickListener(v -> activity.showAvatarUploadQrDialog(activeP.id, () -> {
                    ProfileManager.UserProfile updated = ProfileManager.getActiveProfile(activity);
                    boolean updatedChildMode = ChildModeManager.isChildModeEnabled(activity, updated.id);
                    summaryAvatar.setImageDrawable(ProfileManager.getAvatarDrawable(activity, updated,
                            Math.round(64 * density), updatedChildMode));
                    profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, updated,
                            Math.round(38 * density), updatedChildMode));
                    if (activeProfileListAvatar[0] != null) {
                        activeProfileListAvatar[0].setImageDrawable(ProfileManager.getAvatarDrawable(activity, updated,
                                Math.round(32 * density), updatedChildMode));
                    }
                    for (int k = 0; k < 7; k++) {
                        if (avatarRings[k] != null) {
                            avatarRings[k].setVisibility(ProfileManager.isBuiltInAvatarSelected(updated, k)
                                    ? View.VISIBLE : View.GONE);
                        }
                    }
                }));
                avatarPhotoActions.addView(uploadAvatarButton, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) (TvUiTokens.MIN_DPAD_TARGET_DP * density)));

                Button defaultAvatarButton = new Button(activity);
                defaultAvatarButton.setText(R.string.avatar_use_default_action);
                defaultAvatarButton.setContentDescription(activity.getString(R.string.avatar_use_default_action));
                defaultAvatarButton.setTextColor(TvUiTokens.TEXT_SECONDARY);
                defaultAvatarButton.setTextSize(13);
                defaultAvatarButton.setAllCaps(false);
                defaultAvatarButton.setFocusable(true);
                GradientDrawable defaultAvatarBg = new GradientDrawable();
                defaultAvatarBg.setColor(TvUiTokens.SURFACE);
                defaultAvatarBg.setCornerRadius(10 * density);
                defaultAvatarButton.setBackground(defaultAvatarBg);
                TvFocusIndicator.applyRoundedTextFocus(defaultAvatarButton, density, 10f,
                        TvUiTokens.SURFACE, TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER,
                        TvUiTokens.FOCUS_BORDER, TvUiTokens.TEXT_SECONDARY,
                        TvUiTokens.TEXT_PRIMARY, TvUiTokens.CARD_FOCUS_SCALE);
                LinearLayout.LayoutParams defaultAvatarLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) (TvUiTokens.MIN_DPAD_TARGET_DP * density));
                defaultAvatarLp.topMargin = (int) (6 * density);
                defaultAvatarButton.setOnClickListener(v -> {
                    if (!ProfileManager.clearCustomAvatar(activity, activeP.id)) {
                        TvFeedback.show(activity, R.string.avatar_delete_failed, Toast.LENGTH_LONG);
                        return;
                    }
                    ProfileManager.UserProfile restored = ProfileManager.getActiveProfile(activity);
                    boolean restoredChildMode = ChildModeManager.isChildModeEnabled(activity, restored.id);
                    summaryAvatar.setImageDrawable(ProfileManager.getAvatarDrawable(activity, restored,
                            Math.round(64 * density), restoredChildMode));
                    profileAvatarView.setImageDrawable(ProfileManager.getAvatarDrawable(activity, restored,
                            Math.round(38 * density), restoredChildMode));
                    if (activeProfileListAvatar[0] != null) {
                        activeProfileListAvatar[0].setImageDrawable(ProfileManager.getAvatarDrawable(activity, restored,
                                Math.round(32 * density), restoredChildMode));
                    }
                    for (int k = 0; k < 7; k++) {
                        if (avatarRings[k] != null) {
                            avatarRings[k].setVisibility(ProfileManager.isBuiltInAvatarSelected(restored, k)
                                    ? View.VISIBLE : View.GONE);
                        }
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
                editNameCard.setContentDescription(AppLanguage.strEditName(activity));

                GradientDrawable enBg = new GradientDrawable();
                enBg.setColor(TvUiTokens.SURFACE);
                enBg.setCornerRadius(12 * density);
                editNameCard.setBackground(enBg);

                // Pencil icon squircle
                FrameLayout pencilSquircle = new FrameLayout(activity);
                GradientDrawable psq = new GradientDrawable();
                psq.setColor(TvUiTokens.SHELL);
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
                tvEditLabel.setTextColor(TvUiTokens.TEXT_PRIMARY);
                tvEditLabel.setTypeface(outfit, Typeface.BOLD);
                editNameCard.addView(tvEditLabel, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                TextView tvCurName = new TextView(activity);
                tvCurName.setText(activeP.name);
                tvCurName.setTextSize(13.5f);
                tvCurName.setTextColor(TvUiTokens.TEXT_SECONDARY);
                tvCurName.setTypeface(outfit, Typeface.NORMAL);
                editNameCard.addView(tvCurName);

                editNameCard.setBackground(TvFocusIndicator.createRoundedStateBackground(
                        density, 12f, TvUiTokens.SURFACE, TvUiTokens.SURFACE_FOCUSED,
                        TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER));

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
                        if (!ProfileManager.setProfileName(activity, newName)) {
                            TvFeedback.show(activity, R.string.profile_save_failed, Toast.LENGTH_LONG);
                            return;
                        }
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
                tvUserList.setTextColor(TvUiTokens.TEXT_SECONDARY);
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
                    pRow.setContentDescription(p.name);
                    pRow.setTag("profile.profile." + p.id);

                    GradientDrawable pBg = new GradientDrawable();
                    pBg.setColor(isCur ? TvUiTokens.SURFACE_SELECTED : TvUiTokens.SURFACE_IDLE);
                    pBg.setCornerRadius(12 * density);
                    if (isCur) pBg.setStroke((int) (1.5f * density), TvUiTokens.NAV_SELECTION_BORDER);
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
                    tvPName.setTextColor(TvUiTokens.TEXT_PRIMARY);
                    tvPName.setTypeface(outfit, isCur ? Typeface.BOLD : Typeface.NORMAL);
                    pRow.addView(tvPName, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                    if (isCur) {
                        TextView tvTag = new TextView(activity);
                        tvTag.setText(R.string.active_label);
                        tvTag.setTextSize(11f);
                        tvTag.setTextColor(TvUiTokens.ACCENT_SECTION);
                        tvTag.setTypeface(outfit, Typeface.BOLD);
                        pRow.addView(tvTag);
                    } else if (allProfiles.size() > 1) {
                        TextView tvDel = new TextView(activity);
                        tvDel.setText(R.string.delete_label);
                        tvDel.setTextSize(11.5f);
                        tvDel.setTextColor(TvUiTokens.DANGER);
                        tvDel.setTypeface(outfit, Typeface.NORMAL);
                        pRow.addView(tvDel);
                    }

                    pRow.setSelected(isCur);
                    pRow.setBackground(TvFocusIndicator.createSelectableRoundedStateBackground(
                            density, 12f, TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_SELECTED,
                            TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER,
                            TvUiTokens.NAV_SELECTION_BORDER, TvUiTokens.FOCUS_BORDER));
                    pRow.setOnFocusChangeListener((v, hasFocus) -> {
                        v.animate().cancel();
                        v.setScaleX(1.0f);
                        v.setScaleY(1.0f);
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
                            AlertDialog optionsDialog = ob.create();
                            optionsDialog.show();
                            TvDialogStyle.apply(optionsDialog, activity, 480, 0);
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
                                    if (!ChildModeManager.setChildModeEnabled(activity, isChecked)) {
                                        TvFeedback.show(activity, R.string.profile_save_failed, Toast.LENGTH_LONG);
                                        if (renderRightPane[0] != null) renderRightPane[0].run();
                                        return;
                                    }
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
                        () -> showAppLockPicker(activity, density, outfit, false, () -> {
                            if (renderRightPane[0] != null) renderRightPane[0].run();
                        })));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.hide_apps_count, hiddenSet.size()),
                        activity.getString(R.string.hide_apps_count_summary),
                        () -> showAppLockPicker(activity, density, outfit, true, () -> {
                            if (renderRightPane[0] != null) renderRightPane[0].run();
                        })));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.change_pin_title),
                        activity.getString(R.string.change_pin_summary, ChildModeManager.isDefaultPin(activity) ? activity.getString(R.string.default_pin_label) : "••••"),
                        () -> showChangePinDialog(activity, density, outfit)));

            } else if (tab == TAB_HOME_SCREEN) {
                // === TAB 1: HOME SCREEN (CONSOLIDATING CANDY BOX SETTINGS) ===
                addHeader(rightContent, outfit, AppLanguage.strHomeScreen(activity));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.dock_orientation_title),
                        activity.getString(R.string.dock_orientation_summary),
                        activity::showDockOrientationDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.dock_capacity_title),
                        activity.getString(R.string.dock_capacity_summary),
                        activity::showDockCapacityDialog));

                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        AppLanguage.strDailyPrayerPanelTitle(activity), AppLanguage.strDailyPrayerPanelSummary(activity),
                        activity.isIslamicShelfEnabled(), isChecked -> {
                            activity.setIslamicShelfEnabled(isChecked);
                        }));

                boolean ayahShown = WeeklyAyah.shown(activity);
                rightContent.addView(buildSwitchRow(activity, density, outfit,
                        AppLanguage.strDailyAyahTitle(activity), AppLanguage.strDailyAyahSummary(activity),
                        ayahShown, isChecked -> {
                            WeeklyAyah.setShown(activity, isChecked);
                            activity.showAyah(WeeklyAyah.last(activity));
                            if (isChecked) activity.updateAyah();
                        }));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        AppLanguage.strInterfaceSoundTitle(activity), AppLanguage.strInterfaceSoundSummary(activity),
                        activity::showSoundEffectsDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        AppLanguage.strAzanSettingsTitle(activity), AppLanguage.strAzanSettingsSummary(activity),
                        activity::showAzanSettingsDialog));

            } else if (tab == TAB_APPEARANCE) {
                // === TAB 2: APPEARANCE ===
                addHeader(rightContent, outfit, AppLanguage.strAppearance(activity));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.wallpaper_collection_title),
                        activity.getString(R.string.wallpaper_collection_summary),
                        activity::showWallpaperCollectionManagementDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.wallpaper_interval_title),
                        activity.getString(R.string.wallpaper_interval_summary),
                        activity::showWallpaperIntervalManagementDialog));

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.wallpaper_manage_title_card),
                        activity.getString(R.string.wallpaper_manage_summary_card),
                        activity::showCustomWallpaperManagerManagementDialog));

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

                Display display = activity.getWindowManager().getDefaultDisplay();
                Display.Mode mode = (display != null) ? display.getMode() : null;
                int physW = (mode != null) ? mode.getPhysicalWidth() : dm.widthPixels;
                int physH = (mode != null) ? mode.getPhysicalHeight() : dm.heightPixels;
                float fps = (mode != null) ? mode.getRefreshRate() : 60f;

                addSectionHeader(rightContent, outfit, activity.getString(R.string.device_accessibility_section));

                List<ResolveInfo> homeCandidates = DeviceSettingsStatus.getHomeActivities(activity);
                ResolveInfo resolvedHome = DeviceSettingsStatus.getResolvedHome(activity);
                String activePkg = resolvedHome != null && resolvedHome.activityInfo != null
                        ? resolvedHome.activityInfo.packageName : "";
                String activeLabel = DeviceSettingsStatus.getDisplayLabel(activity.getPackageManager(), resolvedHome);
                if (activeLabel == null || activeLabel.isEmpty()) {
                    activeLabel = activity.getString(R.string.home_status_unknown);
                }
                boolean isDefault = activity.getPackageName().equals(activePkg);
                String homeSub = isDefault
                        ? activity.getString(R.string.launcher_is_default)
                        : activity.getString(R.string.launcher_currently_active, activeLabel);
                homeSub += "\n" + activity.getString(R.string.home_candidates_summary, homeCandidates.size());

                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.default_launcher_status_title),
                        activity.getString(R.string.default_launcher_status_summary, homeSub),
                        () -> showDefaultLauncherDialog(activity, density, outfit)));

                boolean homeKeyEnabled = DeviceSettingsStatus.isAccessibilityServiceEnabled(activity, HomeKeyService.class);
                String homeKeyStatus = activity.getString(homeKeyEnabled
                        ? R.string.device_service_enabled : R.string.device_service_disabled);
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.home_key_status_title),
                        activity.getString(R.string.home_key_status_summary, homeKeyStatus),
                        () -> openDeviceSettingsOrFeedback(activity, new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
                                new Intent(Settings.ACTION_SETTINGS))));

                boolean inputKeyEnabled = DeviceSettingsStatus.isAccessibilityServiceEnabled(activity, InputKeyService.class);
                String inputKeyStatus = activity.getString(inputKeyEnabled
                        ? R.string.device_service_enabled : R.string.device_service_disabled);
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.input_key_status_title),
                        activity.getString(R.string.input_key_status_summary, inputKeyStatus),
                        () -> openDeviceSettings(activity, new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))));

                boolean notificationAccessEnabled = DeviceSettingsStatus.isNotificationListenerEnabled(
                        activity, MediaNotificationListener.class);
                String notificationStatus = activity.getString(notificationAccessEnabled
                        ? R.string.device_service_enabled : R.string.device_service_disabled);
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.notification_access_title),
                        activity.getString(R.string.notification_access_summary, notificationStatus),
                        () -> openDeviceSettingsOrFeedback(activity, new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),
                                new Intent(Settings.ACTION_SETTINGS))));

                addSectionHeader(rightContent, outfit, activity.getString(R.string.screensaver_settings_section));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.screensaver_settings_title),
                        activity.getString(R.string.screensaver_settings_summary),
                        () -> openDeviceSettingsOrFeedback(activity, new Intent(Settings.ACTION_DREAM_SETTINGS),
                                new Intent(Settings.ACTION_DISPLAY_SETTINGS), new Intent(Settings.ACTION_SETTINGS))));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.display_timeout_title),
                        activity.getString(R.string.display_timeout_summary),
                        () -> openDeviceSettingsOrFeedback(activity, new Intent(Settings.ACTION_DISPLAY_SETTINGS),
                                new Intent(Settings.ACTION_SETTINGS))));
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
                            else activity.showManualUpdateDialog();
                        }));
                rightContent.addView(buildCardRow(activity, density, outfit,
                        activity.getString(R.string.android_settings_title), activity.getString(R.string.android_settings_summary),
                        () -> {
                            if (!openDeviceSettings(activity, activity.systemIntent(Settings.ACTION_SETTINGS))) {
                                TvFeedback.show(activity, R.string.android_settings_unavailable, Toast.LENGTH_SHORT);
                            }
                        }));
            }
        };

        searchBox.setOnClickListener(v -> {
            SoundEffects.playSelect();
            EditText searchInput = new EditText(activity);
            searchInput.setSingleLine(true);
            searchInput.setTextColor(TvUiTokens.TEXT_PRIMARY);
            searchInput.setHintTextColor(TvUiTokens.TEXT_SECONDARY);
            searchInput.setHint(R.string.settings_search_hint);
            searchInput.setTextSize(TvUiTokens.CARD_TITLE_SP);
            searchInput.setPadding((int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density),
                    (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density),
                    (int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density),
                    (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density));
            GradientDrawable inputBg = new GradientDrawable();
            inputBg.setColor(TvUiTokens.INPUT_SURFACE);
            inputBg.setCornerRadius(TvUiTokens.CARD_RADIUS_DP * density);
            inputBg.setStroke((int) (1 * density), TvUiTokens.BORDER);
            searchInput.setBackground(inputBg);

            AlertDialog searchDialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                    .setTitle(R.string.settings_search_title)
                    .setView(searchInput)
                    .setPositiveButton(R.string.search_action, (dialog, which) -> {
                        String query = searchInput.getText().toString().trim();
                        if (query.isEmpty()) {
                            TvFeedback.show(activity, R.string.search_keyword_required, Toast.LENGTH_SHORT);
                            return;
                        }
                        List<SearchEntry> matches = new ArrayList<>();
                        for (SearchEntry entry : buildSearchEntries(activity)) {
                            if (SettingsSearchIndex.matches(query, entry.title, entry.summary)) {
                                matches.add(entry);
                            }
                        }
                        if (matches.isEmpty()) {
                            TvFeedback.show(activity, R.string.settings_search_not_found, Toast.LENGTH_LONG);
                            return;
                        }
                        String[] choices = new String[matches.size()];
                        for (int i = 0; i < matches.size(); i++) {
                            SearchEntry entry = matches.get(i);
                            choices[i] = entry.title + "  ·  " + settingsCategoryLabel(activity, entry.tabId);
                        }
                        AlertDialog resultsDialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                                .setTitle(R.string.settings_search_results)
                                .setItems(choices, (resultDialog, index) -> {
                                    SearchEntry selected = matches.get(index);
                                    activeTab[0] = selected.tabId;
                                    if (selected.tabId == TAB_PROFILE) {
                                        activeSidebarView[0] = profileCard;
                                    } else {
                                        for (NavItem item : navItems) {
                                            if (item.tabId == selected.tabId) {
                                                activeSidebarView[0] = item.rowView;
                                                break;
                                            }
                                        }
                                    }
                                    updateSidebarSelection.run();
                                    renderRightPane[0].run();
                                    rightContent.post(() -> {
                                        View target = findControlBySearchEntry(rightContent, selected);
                                        if (target != null && target.isFocusable() && target.requestFocus()) return;
                                        if (activeSidebarView[0] != null) activeSidebarView[0].requestFocus();
                                    });
                                })
                                .setNegativeButton(R.string.cancel_action, null)
                                .create();
                        resultsDialog.show();
                        TvDialogStyle.apply(resultsDialog, activity, 620, 0);
                    })
                    .setNegativeButton(R.string.cancel_action, null)
                    .create();
            searchDialog.show();
            TvDialogStyle.apply(searchDialog, activity, 620, 0);
            searchInput.requestFocus();
        });

        // Initial render of Tab 0 (Profile)
        updateSidebarSelection.run();
        renderRightPane[0].run();

        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding((int) (12 * density), (int) (4 * density),
                (int) (12 * density), (int) (10 * density));
        LinearLayout brandBlock = new LinearLayout(activity);
        brandBlock.setOrientation(LinearLayout.VERTICAL);
        TextView brandTitle = new TextView(activity);
        brandTitle.setText("GLASS TV HOME");
        brandTitle.setTextSize(TvUiTokens.SECTION_TITLE_SP);
        brandTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        brandTitle.setTypeface(outfit, Typeface.BOLD);
        brandBlock.addView(brandTitle);
        TextView brandSubtitle = new TextView(activity);
        brandSubtitle.setText(R.string.global_settings_header);
        brandSubtitle.setTextSize(TvUiTokens.METADATA_SP);
        brandSubtitle.setTextColor(TvUiTokens.TEXT_SECONDARY);
        brandBlock.addView(brandSubtitle);
        header.addView(brandBlock, new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        header.addView(searchBox, new LinearLayout.LayoutParams(
                (int) (390 * density), (int) (52 * density)));

        // Reuse the existing E01/MainActivity network-state owner; Settings is read-only here.
        TextView wifiStatus = new TextView(activity);
        wifiStatus.setText(activity.getString(R.string.wifi) + "  ·  "
                + activity.getGlobalSettingsNetworkSummary());
        wifiStatus.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
        wifiStatus.setTextColor(TvUiTokens.TEXT_PRIMARY);
        wifiStatus.setSingleLine(true);
        wifiStatus.setMaxWidth((int) (260 * density));
        wifiStatus.setEllipsize(android.text.TextUtils.TruncateAt.END);
        wifiStatus.setPadding((int) (14 * density), (int) (10 * density),
                (int) (14 * density), (int) (10 * density));
        GradientDrawable wifiStatusBg = new GradientDrawable();
        wifiStatusBg.setColor(TvUiTokens.SURFACE);
        wifiStatusBg.setCornerRadius(TvUiTokens.POPUP_RADIUS_DP * density);
        wifiStatusBg.setStroke((int) density, TvUiTokens.BORDER);
        wifiStatus.setBackground(wifiStatusBg);
        wifiStatus.setContentDescription(wifiStatus.getText());
        LinearLayout.LayoutParams wifiStatusLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wifiStatusLp.setMarginStart((int) (12 * density));
        header.addView(wifiStatus, wifiStatusLp);

        LinearLayout body = new LinearLayout(activity);
        body.setOrientation(LinearLayout.HORIZONTAL);
        body.setGravity(Gravity.CENTER);
        body.setClipChildren(false);
        body.setClipToPadding(false);
        body.addView(leftCol, leftLp);
        body.addView(verticalDivider, divLp);
        body.addView(rightCol, rightLp);
        root.addView(header, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (64 * density)));
        root.addView(body, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        WeakReference<View> originFocus = new WeakReference<>(activity.getCurrentFocus());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        dialogHolder[0] = dialog;
        sSettingsHost = new WeakReference<>(activity);
        sSettingsDialog = new WeakReference<>(dialog);
        sSettingsNetworkStatus = new WeakReference<>(wifiStatus);
        sSettingsRightContent = new WeakReference<>(rightContent);
        sSettingsRightScroll = new WeakReference<>(rightScroll);
        sSettingsPaneRefresh = new WeakReference<>(renderRightPane[0]);
        sSettingsActiveTab = new WeakReference<>(activeTab);
        sSettingsActiveSidebarView = new WeakReference<>(activeSidebarView);
        final ViewTreeObserver.OnGlobalFocusChangeListener[] focusListener =
                new ViewTreeObserver.OnGlobalFocusChangeListener[1];
        dialog.setOnDismissListener(d -> {
            if (focusListener[0] != null && root.getViewTreeObserver().isAlive()) {
                root.getViewTreeObserver().removeOnGlobalFocusChangeListener(focusListener[0]);
            }
            sDpadLeftTarget = null;
            clearHostReferences(activity);
            View origin = originFocus.get();
            if (origin != null && origin.isFocusable() && origin.isShown()) {
                origin.post(origin::requestFocus);
            }
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

        TvDialogStyle.apply(dialog, activity, 1000, 580);

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
                    if (restored == null) {
                        restored = resolveLegacyRightFocusPath(rightContent, activeTab[0],
                                savedFocusPath, savedFocusSignature);
                    }
                    if (restored != null && restored.requestFocus()) return;
                    if (activeSidebarView[0] != null) activeSidebarView[0].requestFocus();
                    else profileCard.requestFocus();
                });
            });
        } else if (savedFocusTab == activeTab[0] && "sidebar".equals(savedFocusArea)) {
            root.post(() -> {
                View restored = resolveFocusPath(leftCol, savedFocusPath, savedFocusSignature);
                if (restored == null) {
                    restored = resolveLegacySidebarFocusPath(leftCol, searchBox,
                            savedFocusPath, savedFocusSignature);
                }
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

    private static void clearHostReferences(MainActivity activity) {
        if (sSettingsHost == null || sSettingsHost.get() != activity) return;
        sDpadLeftTarget = null;
        sSettingsHost = null;
        sSettingsDialog = null;
        sSettingsNetworkStatus = null;
        sSettingsRightContent = null;
        sSettingsRightScroll = null;
        sSettingsPaneRefresh = null;
        sSettingsActiveTab = null;
        sSettingsActiveSidebarView = null;
    }

    /** Called from the existing host network-state path; no second connectivity callback is added. */
    static void refreshNetworkStatus(MainActivity activity) {
        if (activity == null || sSettingsHost == null || sSettingsHost.get() != activity) return;
        TextView status = sSettingsNetworkStatus == null ? null : sSettingsNetworkStatus.get();
        if (status == null) return;
        CharSequence value = activity.getString(R.string.wifi) + "  ·  "
                + activity.getGlobalSettingsNetworkSummary();
        status.setText(value);
        status.setContentDescription(value);
    }

    /** Refresh external Android device state on return without discarding the focused Settings control. */
    static void onHostResumed(MainActivity activity) {
        if (activity == null || sSettingsHost == null || sSettingsHost.get() != activity) return;
        AlertDialog dialog = sSettingsDialog == null ? null : sSettingsDialog.get();
        if (dialog == null || !dialog.isShowing()) return;
        refreshNetworkStatus(activity);

        int[] activeTab = sSettingsActiveTab == null ? null : sSettingsActiveTab.get();
        if (activeTab == null || NavigationState.normalizeSettingsTab(activeTab[0]) != TAB_DEVICE_SETTINGS) return;
        LinearLayout content = sSettingsRightContent == null ? null : sSettingsRightContent.get();
        ScrollView scroll = sSettingsRightScroll == null ? null : sSettingsRightScroll.get();
        Runnable refresh = sSettingsPaneRefresh == null ? null : sSettingsPaneRefresh.get();
        View[] sidebar = sSettingsActiveSidebarView == null ? null : sSettingsActiveSidebarView.get();
        if (content == null || scroll == null || refresh == null) return;

        View focused = content.findFocus();
        boolean restoreRight = focused != null;
        String focusedDescription = focused != null && focused.getContentDescription() != null
                ? focused.getContentDescription().toString() : null;
        int previousScroll = scroll.getScrollY();
        refresh.run();
        scroll.post(() -> {
            scroll.scrollTo(0, previousScroll);
            if (restoreRight && focusedDescription != null) {
                View target = findControlByDescription(content, focusedDescription);
                if (target != null && target.isFocusable() && target.requestFocus()) return;
            }
            if (sidebar != null && sidebar.length > 0 && sidebar[0] != null) {
                sidebar[0].requestFocus();
            }
        });
    }

    static void onHostDestroyed(MainActivity activity) {
        clearHostReferences(activity);
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

    private static final class SearchEntry {
        final String id;
        final int tabId;
        final String title;
        final String summary;

        SearchEntry(String id, int tabId, String title, String summary) {
            this.id = id;
            this.tabId = tabId;
            this.title = title;
            this.summary = summary == null ? "" : summary;
        }
    }

    private static void addSearchEntry(List<SearchEntry> entries, String id, int tabId,
                                       String title, String summary) {
        entries.add(new SearchEntry(id, tabId, title, summary));
    }

    /** Search entries use the same localized labels and summaries displayed by each control. */
    private static List<SearchEntry> buildSearchEntries(MainActivity activity) {
        ArrayList<SearchEntry> entries = new ArrayList<>();
        int locked = ChildModeManager.getLockedApps(activity).size();
        int hidden = ChildModeManager.getHiddenApps(activity).size();
        boolean hasUpdate = AppUpdateManager.isUpdateAvailable(activity);

        addSearchEntry(entries, "profile.avatar.upload", TAB_PROFILE,
                activity.getString(R.string.avatar_upload_qr_action), "");
        addSearchEntry(entries, "profile.avatar.default", TAB_PROFILE,
                activity.getString(R.string.avatar_use_default_action), "");
        addSearchEntry(entries, "profile.rename", TAB_PROFILE, AppLanguage.strEditName(activity), "");
        ProfileManager.UserProfile activeProfile = ProfileManager.getActiveProfile(activity);
        for (int i = 0; i < ProfileManager.AVATAR_NAMES.length; i++) {
            addSearchEntry(entries, "profile.avatar.builtin." + i, TAB_PROFILE,
                    ProfileManager.AVATAR_NAMES[i],
                    activity.getString(R.string.active_profile_avatar_header, activeProfile.name));
        }
        for (ProfileManager.UserProfile profile : ProfileManager.getAllProfiles(activity)) {
            addSearchEntry(entries, "profile.profile." + profile.id, TAB_PROFILE,
                    profile.name, activity.getString(R.string.profile_user_title, profile.name));
        }
        addSearchEntry(entries, "profile.add", TAB_PROFILE,
                activity.getString(R.string.add_profile_card_title), activity.getString(R.string.add_profile_card_summary));
        addSearchEntry(entries, "profile.child-mode", TAB_PROFILE,
                activity.getString(R.string.child_mode_switch_title), activity.getString(R.string.child_mode_switch_description));
        addSearchEntry(entries, "profile.lock-apps", TAB_PROFILE,
                activity.getString(R.string.lock_apps_count, locked), activity.getString(R.string.lock_apps_count_summary));
        addSearchEntry(entries, "profile.hide-apps", TAB_PROFILE,
                activity.getString(R.string.hide_apps_count, hidden), activity.getString(R.string.hide_apps_count_summary));
        addSearchEntry(entries, "profile.pin", TAB_PROFILE,
                activity.getString(R.string.change_pin_title), activity.getString(R.string.change_pin_summary));

        addSearchEntry(entries, "home.dock-orientation", TAB_HOME_SCREEN,
                activity.getString(R.string.dock_orientation_title), activity.getString(R.string.dock_orientation_summary));
        addSearchEntry(entries, "home.dock-capacity", TAB_HOME_SCREEN,
                activity.getString(R.string.dock_capacity_title), activity.getString(R.string.dock_capacity_summary));
        addSearchEntry(entries, "home.prayer-panel", TAB_HOME_SCREEN,
                AppLanguage.strDailyPrayerPanelTitle(activity), AppLanguage.strDailyPrayerPanelSummary(activity));
        addSearchEntry(entries, "home.daily-ayah", TAB_HOME_SCREEN,
                AppLanguage.strDailyAyahTitle(activity), AppLanguage.strDailyAyahSummary(activity));
        addSearchEntry(entries, "home.interface-sound", TAB_HOME_SCREEN,
                AppLanguage.strInterfaceSoundTitle(activity), AppLanguage.strInterfaceSoundSummary(activity));
        addSearchEntry(entries, "home.azan", TAB_HOME_SCREEN,
                AppLanguage.strAzanSettingsTitle(activity), AppLanguage.strAzanSettingsSummary(activity));

        addSearchEntry(entries, "appearance.wallpaper-collection", TAB_APPEARANCE,
                activity.getString(R.string.wallpaper_collection_title), activity.getString(R.string.wallpaper_collection_summary));
        addSearchEntry(entries, "appearance.wallpaper-interval", TAB_APPEARANCE,
                activity.getString(R.string.wallpaper_interval_title), activity.getString(R.string.wallpaper_interval_summary));
        addSearchEntry(entries, "appearance.custom-wallpaper", TAB_APPEARANCE,
                activity.getString(R.string.wallpaper_manage_title_card), activity.getString(R.string.wallpaper_manage_summary_card));

        addSearchEntry(entries, "widgets.location-prayer", TAB_STATUS_WEATHER,
                activity.getString(R.string.location_prayer_title), activity.getString(R.string.location_prayer_summary));
        addSearchEntry(entries, "widgets.weather", TAB_STATUS_WEATHER,
                activity.getString(R.string.weather_widget_title), activity.getString(R.string.weather_widget_summary));
        addSearchEntry(entries, "widgets.info-actions", TAB_STATUS_WEATHER,
                activity.getString(R.string.info_actions_title_card), activity.getString(R.string.info_actions_summary_card));

        addSearchEntry(entries, "effects.icon-animation", TAB_EFFECTS,
                activity.getString(R.string.icon_animation_setting_title), activity.getString(R.string.icon_animation_setting_description));
        addSearchEntry(entries, "effects.glass", TAB_EFFECTS,
                activity.getString(R.string.glass_effect_setting_title), activity.getString(R.string.glass_effect_setting_description));
        addSearchEntry(entries, "effects.weather", TAB_EFFECTS,
                activity.getString(R.string.weather_animation_setting_title), activity.getString(R.string.weather_animation_setting_description));
        addSearchEntry(entries, "effects.icon-glow", TAB_EFFECTS,
                activity.getString(R.string.icon_glow_setting_title), activity.getString(R.string.icon_glow_setting_description));

        addSearchEntry(entries, "standby.upload", TAB_STANDBY,
                activity.getString(R.string.standby_upload_title), activity.getString(R.string.standby_upload_summary));
        addSearchEntry(entries, "standby.pick-photos", TAB_STANDBY,
                activity.getString(R.string.standby_pick_title), activity.getString(R.string.standby_pick_summary));
        addSearchEntry(entries, "standby.use-photos", TAB_STANDBY,
                activity.getString(R.string.standby_use_photos_title), activity.getString(R.string.standby_use_photos_summary));
        if (activity.getStandbyPhotoCount() > 0) {
            addSearchEntry(entries, "standby.clear-photos", TAB_STANDBY,
                    activity.getString(R.string.standby_clear_title), activity.getString(R.string.standby_clear_summary));
        }
        addSearchEntry(entries, "standby.preview", TAB_STANDBY,
                activity.getString(R.string.standby_preview_title), activity.getString(R.string.standby_preview_summary));

        addSearchEntry(entries, "device.default-launcher", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.default_launcher_status_title), activity.getString(R.string.default_launcher_status_summary,
                        activity.getString(R.string.launcher_is_default)));
        addSearchEntry(entries, "device.home-key", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.home_key_status_title), activity.getString(R.string.home_key_status_summary,
                        activity.getString(DeviceSettingsStatus.isAccessibilityServiceEnabled(activity, HomeKeyService.class)
                                ? R.string.device_service_enabled : R.string.device_service_disabled)));
        addSearchEntry(entries, "device.input-key", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.input_key_status_title), activity.getString(R.string.input_key_status_summary,
                        activity.getString(DeviceSettingsStatus.isAccessibilityServiceEnabled(activity, InputKeyService.class)
                                ? R.string.device_service_enabled : R.string.device_service_disabled)));
        addSearchEntry(entries, "device.notification-access", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.notification_access_title), activity.getString(R.string.notification_access_summary,
                        activity.getString(DeviceSettingsStatus.isNotificationListenerEnabled(activity, MediaNotificationListener.class)
                                ? R.string.device_service_enabled : R.string.device_service_disabled)));
        addSearchEntry(entries, "device.screensaver", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.screensaver_settings_title), activity.getString(R.string.screensaver_settings_summary));
        addSearchEntry(entries, "device.display-timeout", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.display_timeout_title), activity.getString(R.string.display_timeout_summary));
        addSearchEntry(entries, "device.restart", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.restart_tv_title), activity.getString(R.string.restart_tv_summary));
        addSearchEntry(entries, "device.power", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.power_tv_title), activity.getString(R.string.power_tv_summary));
        addSearchEntry(entries, "device.clear-cache", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.clear_tv_cache_title), activity.getString(R.string.clear_tv_cache_summary));
        addSearchEntry(entries, "device.language", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.app_language_title), activity.getString(R.string.app_language_summary));
        String updateTitle = hasUpdate ? activity.getString(R.string.update_available_title)
                : activity.getString(R.string.check_updates_title);
        String updateSummary = hasUpdate
                ? activity.getString(R.string.update_available_summary, AppUpdateManager.getNewVersionName(activity))
                : activity.getString(R.string.check_updates_summary);
        addSearchEntry(entries, "device.updates", TAB_DEVICE_SETTINGS, updateTitle, updateSummary);
        addSearchEntry(entries, "device.android-settings", TAB_DEVICE_SETTINGS,
                activity.getString(R.string.android_settings_title), activity.getString(R.string.android_settings_summary));
        return entries;
    }

    private static String settingsCategoryLabel(MainActivity activity, int tabId) {
        switch (NavigationState.normalizeSettingsTab(tabId)) {
            case TAB_PROFILE: return AppLanguage.strProfileTitle(activity);
            case TAB_HOME_SCREEN: return AppLanguage.strHomeScreen(activity);
            case TAB_APPEARANCE: return AppLanguage.strAppearance(activity);
            case TAB_STATUS_WEATHER: return AppLanguage.strStatusBarWeather(activity);
            case TAB_EFFECTS: return AppLanguage.strVisualEffects(activity);
            case TAB_STANDBY: return AppLanguage.strStandByMode(activity);
            case TAB_DEVICE_SETTINGS: return AppLanguage.strDeviceSettings(activity);
            default: return AppLanguage.strProfileTitle(activity);
        }
    }

    private static View findControlBySearchEntry(View root, SearchEntry entry) {
        if (root == null || entry == null) return null;
        Object stableId = root.getTag();
        if (entry.id.equals(stableId)) return root;
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0; i < group.getChildCount(); i++) {
                View found = findControlBySearchEntry(group.getChildAt(i), entry);
                if (found != null) return found;
            }
        }
        CharSequence description = root.getContentDescription();
        if (description != null && entry.title.contentEquals(description) && root.isFocusable()) {
            root.setTag(entry.id);
            return root;
        }
        return null;
    }

    private static View findControlByDescription(View root, String description) {
        if (root == null || description == null) return null;
        CharSequence current = root.getContentDescription();
        if (current != null && description.contentEquals(current)) return root;
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0; i < group.getChildCount(); i++) {
                View found = findControlByDescription(group.getChildAt(i), description);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static LinearLayout buildCardRow(Context ctx, float density, Typeface font, String title, String subtitle, Runnable action) {
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding((int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density), (int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density));
        card.setFocusable(true);
        card.setClickable(true);
        card.setContentDescription(title);

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

        card.setBackground(TvFocusIndicator.createRoundedStateBackground(
                density, TvUiTokens.CARD_RADIUS_DP, TvUiTokens.SURFACE,
                TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER));

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
        card.setPadding((int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density), (int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density), (int) (TvUiTokens.CARD_PADDING_VERTICAL_DP * density));
        card.setFocusable(true);
        card.setClickable(true);
        card.setContentDescription(title);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(TvUiTokens.SURFACE);
        bg.setCornerRadius(TvUiTokens.CARD_RADIUS_DP * density);
        card.setBackground(bg);

        LinearLayout textCol = new LinearLayout(ctx);
        textCol.setOrientation(LinearLayout.VERTICAL);

        TextView tvTitle = new TextView(ctx);
        tvTitle.setText(title);
        tvTitle.setTextSize(TvUiTokens.CARD_TITLE_SP);
        tvTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tvTitle.setTypeface(font, Typeface.BOLD);
        textCol.addView(tvTitle);

        if (subtitle != null && !subtitle.isEmpty()) {
            TextView tvSub = new TextView(ctx);
            tvSub.setText(subtitle);
            tvSub.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
            tvSub.setTextColor(TvUiTokens.TEXT_SECONDARY);
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

        card.setBackground(TvFocusIndicator.createRoundedStateBackground(
                density, TvUiTokens.CARD_RADIUS_DP, TvUiTokens.SURFACE,
                TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER));

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
        lp.bottomMargin = (int) (TvUiTokens.CARD_BOTTOM_MARGIN_DP * density);
        card.setLayoutParams(lp);
        return card;
    }

    private static LinearLayout buildInfoCard(Context ctx, float density, Typeface font, String title, String value) {
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding((int) (16 * density), (int) (10 * density), (int) (16 * density), (int) (10 * density));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(TvUiTokens.SURFACE);
        bg.setCornerRadius(12 * density);
        card.setBackground(bg);

        TextView tvTitle = new TextView(ctx);
        tvTitle.setText(title);
        tvTitle.setTextSize(13.5f);
        tvTitle.setTextColor(TvUiTokens.NAV_ITEM_TEXT);
        tvTitle.setTypeface(font, Typeface.BOLD);
        card.addView(tvTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvVal = new TextView(ctx);
        tvVal.setText(value);
        tvVal.setTextSize(13f);
        tvVal.setTextColor(TvUiTokens.ACCENT_CYAN);
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
        et.setTextColor(TvUiTokens.TEXT_PRIMARY);
        et.setTextSize(16);
        et.setTypeface(font, Typeface.NORMAL);
        et.setPadding((int) (16 * density), (int) (12 * density), (int) (16 * density), (int) (12 * density));

        GradientDrawable etBg = new GradientDrawable();
        etBg.setColor(TvUiTokens.INPUT_SURFACE);
        etBg.setCornerRadius(10 * density);
        etBg.setStroke((int) (1 * density), TvUiTokens.BORDER);
        et.setBackground(etBg);
        TvFocusIndicator.applyRoundedTextFocus(et, density, 10f,
                TvUiTokens.INPUT_SURFACE, TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER,
                TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY, 1.0f);

        FrameLayout container = new FrameLayout(activity);
        container.setPadding((int) (24 * density), (int) (12 * density), (int) (24 * density), (int) (8 * density));
        container.addView(et);

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setTitle(AppLanguage.strEditName(activity))
                .setView(container)
                .setPositiveButton(R.string.save_action, null)
                .setNegativeButton(AppLanguage.strCancel(activity), (d, which) -> d.dismiss())
                .create();
        dialog.setOnShowListener(ignored -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newName = et.getText().toString().trim();
            if (newName.isEmpty()) {
                et.setError(activity.getString(R.string.user_name_required));
                et.requestFocus();
                return;
            }
            dialog.dismiss();
            if (callback != null) callback.onSave(newName);
        }));
        dialog.show();
        TvDialogStyle.apply(dialog, activity, 520, 0);
        et.requestFocus();
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
                    Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    saveButton.setEnabled(false);
                    ChildModeManager.setPinAsync(activity, first, saved -> {
                        if (!dialog.isShowing()) return;
                        saveButton.setEnabled(true);
                        if (saved) {
                            dialog.dismiss();
                            TvFeedback.show(activity, R.string.pin_updated, Toast.LENGTH_SHORT);
                        } else {
                            TvFeedback.show(activity, R.string.profile_save_failed, Toast.LENGTH_LONG);
                        }
                    });
                }));
                dialog.show();
                TvDialogStyle.apply(dialog, activity, 520, 0);
                newPin.requestFocus();
            }
            @Override
            public void onCancelled() {}
        });
    }

    /** Require parent PIN before changing either the locked-app or hidden-app policy. */
    private static void showAppLockPicker(MainActivity activity, float density, Typeface font, boolean isHideMode, Runnable onFinished) {
        ChildModeManager.promptPin(activity, activity.getString(R.string.verify_parent_pin_title),
                activity.getString(R.string.verify_parent_pin_message, activity.getString(isHideMode ? R.string.hidden_apps_label : R.string.locked_apps_label)),
                new ChildModeManager.PinCallback() {
                    @Override public void onSuccess() {
                        showAppLockPickerAuthorized(activity, density, font, isHideMode, onFinished);
                    }
                    @Override public void onCancelled() { }
                });
    }

    private static void showAppLockPickerAuthorized(MainActivity activity, float density, Typeface font, boolean isHideMode, Runnable onFinished) {
        PackageManager pm = activity.getPackageManager();
        // Merge both TV and generic launcher entry points. Some TV apps expose only one
        // category, so falling back only when Leanback is empty silently omits valid apps.
        List<ResolveInfo> apps = new ArrayList<>();
        Intent leanbackIntent = new Intent(Intent.ACTION_MAIN, null);
        leanbackIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> leanbackApps = pm.queryIntentActivities(leanbackIntent, 0);
        if (leanbackApps != null) apps.addAll(leanbackApps);

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> launcherApps = pm.queryIntentActivities(launcherIntent, 0);
        if (launcherApps != null) apps.addAll(launcherApps);

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
        Set<String> seenPackages = new HashSet<>();
        if (apps != null) {
            for (ResolveInfo ri : apps) {
                if (ri == null || ri.activityInfo == null) continue;
                String p = ri.activityInfo.packageName;
                // Policy is package-scoped: merge Leanback + Launcher entries and show each app once.
                if (!ChildModeManager.addAppPolicyPackage(seenPackages, p, selfPkg)) continue;
                CharSequence loadedLabel = ri.loadLabel(pm);
                String label = loadedLabel != null ? loadedLabel.toString() : p;
                android.graphics.drawable.Drawable icon = ri.loadIcon(pm);
                if (icon == null) icon = pm.getDefaultActivityIcon();
                entryList.add(new AppEntry(p, label, icon));
            }
        }
        Collections.sort(entryList, (a, b) -> a.label.compareToIgnoreCase(b.label));

        ProfileManager.UserProfile targetProfile = ProfileManager.getActiveProfile(activity);
        if (targetProfile == null) return;
        final String targetProfileId = targetProfile.id;
        final Set<String> targetSet = isHideMode
                ? ChildModeManager.getHiddenApps(activity, targetProfileId)
                : ChildModeManager.getLockedApps(activity, targetProfileId);
        final Set<String> selectedPackages = new HashSet<>(targetSet);

        LinearLayout pickerLayout = new LinearLayout(activity);
        pickerLayout.setOrientation(LinearLayout.VERTICAL);
        pickerLayout.setPadding((int) (22 * density), (int) (18 * density), (int) (22 * density), (int) (16 * density));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(TvUiTokens.SHELL);
        bg.setCornerRadius(20 * density);
        bg.setStroke((int) (1.5f * density), TvUiTokens.BORDER);
        pickerLayout.setBackground(bg);

        TextView tvTitle = new TextView(activity);
        tvTitle.setText(isHideMode ? R.string.hide_apps_title : R.string.lock_apps_title);
        tvTitle.setTextSize(17);
        tvTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tvTitle.setTypeface(font, Typeface.BOLD);
        pickerLayout.addView(tvTitle);

        TextView tvSub = new TextView(activity);
        tvSub.setText(isHideMode ? R.string.hide_apps_description : R.string.lock_apps_description);
        tvSub.setTextSize(12);
        tvSub.setTextColor(TvUiTokens.TEXT_SECONDARY);
        tvSub.setTypeface(font, Typeface.NORMAL);
        tvSub.setPadding(0, (int) (2 * density), 0, (int) (10 * density));
        pickerLayout.addView(tvSub);

        TextView tvCount = new TextView(activity);
        tvCount.setText(activity.getString(R.string.app_policy_selected_count, selectedPackages.size()));
        tvCount.setTextSize(12f);
        tvCount.setTextColor(TvUiTokens.ACCENT_SECTION);
        tvCount.setTypeface(font, Typeface.BOLD);
        tvCount.setPadding(0, 0, 0, (int) (10 * density));
        pickerLayout.addView(tvCount);

        ScrollView sv = new ScrollView(activity);
        sv.setVerticalScrollBarEnabled(true);
        LinearLayout list = new LinearLayout(activity);
        list.setOrientation(LinearLayout.VERTICAL);
        if (entryList.isEmpty()) {
            TextView empty = new TextView(activity);
            empty.setText(R.string.app_picker_empty);
            empty.setTextSize(13f);
            empty.setTextColor(TvUiTokens.TEXT_SECONDARY);
            empty.setTypeface(font, Typeface.NORMAL);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(0, (int) (20 * density), 0, (int) (20 * density));
            empty.setFocusable(false);
            list.addView(empty, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        for (AppEntry app : entryList) {
            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding((int) (12 * density), (int) (8 * density), (int) (12 * density), (int) (8 * density));
            row.setFocusable(true);
            row.setClickable(true);

            GradientDrawable rbg = new GradientDrawable();
            rbg.setColor(TvUiTokens.SURFACE_IDLE);
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
            tv.setTextColor(TvUiTokens.TEXT_PRIMARY);
            tv.setTypeface(font, Typeface.BOLD);
            row.addView(tv, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            CheckBox cb = new CheckBox(activity);
            cb.setChecked(selectedPackages.contains(app.pkg));
            cb.setFocusable(false);
            cb.setClickable(false);
            row.addView(cb);

            row.setBackground(TvFocusIndicator.createRoundedStateBackground(
                    density, 10f, TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_FOCUSED,
                    TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER));

            row.setOnClickListener(v -> {
                SoundEffects.playSelect();
                boolean nowChecked = !cb.isChecked();
                cb.setChecked(nowChecked);
                if (nowChecked) selectedPackages.add(app.pkg);
                else selectedPackages.remove(app.pkg);
                tvCount.setText(activity.getString(R.string.app_policy_selected_count, selectedPackages.size()));
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
        btnDone.setTextColor(TvUiTokens.TEXT_PRIMARY);
        btnDone.setTypeface(font, Typeface.BOLD);
        btnDone.setFocusable(true);
        btnDone.setClickable(true);

        TvFocusIndicator.applyRoundedTextFocus(btnDone, density, 10f,
                TvUiTokens.SUCCESS, TvUiTokens.SUCCESS, TvUiTokens.SUCCESS,
                TvUiTokens.FOCUS_BORDER, TvUiTokens.TEXT_PRIMARY,
                TvUiTokens.TEXT_PRIMARY, 1.0f);

        final boolean[] savedSelection = new boolean[]{false};
        btnDone.setOnClickListener(v -> {
            SoundEffects.playSelect();
            if (!ChildModeManager.replaceAppPolicyPackages(activity, targetProfileId, isHideMode, selectedPackages)) {
                TvFeedback.show(activity, R.string.app_policy_save_failed, Toast.LENGTH_LONG);
                return;
            }
            savedSelection[0] = true;
            if (pickerHolder[0] != null) pickerHolder[0].dismiss();
        });

        pickerLayout.addView(btnDone, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (TvUiTokens.MIN_DPAD_TARGET_DP * density)));

        AlertDialog.Builder pb = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        pb.setView(pickerLayout);
        AlertDialog d = pb.create();
        pickerHolder[0] = d;
        d.setOnDismissListener(ignored -> {
            if (!savedSelection[0] || activity.isFinishing() || activity.isDestroyed()) return;
            activity.reloadTiles();
            if (onFinished != null) onFinished.run();
        });
        d.show();
        TvDialogStyle.applyCustom(d, activity, 520, 0, 0.68f);
        if (entryList.isEmpty()) btnDone.requestFocus();
        else if (list.getChildCount() > 0) list.getChildAt(0).requestFocus();
    }

    private static void showDefaultLauncherDialog(MainActivity activity, float density, Typeface font) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        SoundEffects.playSelect();

        List<ResolveInfo> candidates = DeviceSettingsStatus.getHomeActivities(activity);
        ResolveInfo resolvedHome = DeviceSettingsStatus.getResolvedHome(activity);
        String activePkg = resolvedHome != null && resolvedHome.activityInfo != null
                ? resolvedHome.activityInfo.packageName : "";
        String activeLabel = DeviceSettingsStatus.getDisplayLabel(activity.getPackageManager(), resolvedHome);
        if (activeLabel == null || activeLabel.isEmpty()) {
            activeLabel = activity.getString(R.string.home_status_unknown);
        }
        boolean isDefault = activity.getPackageName().equals(activePkg);

        // Keep the entire popup surface opaque. Previously the AlertDialog window and ScrollView
        // were explicitly transparent, which could expose the launcher behind the content on
        // some TV themes/OEM builds and recreate the "see-through" appearance.
        ScrollView scroll = new ScrollView(activity);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);
        scroll.setClipChildren(true);
        scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // The dialog window owns the shared opaque glass surface; keep the scrolling content transparent.
        scroll.setBackgroundColor(Color.TRANSPARENT);

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding((int) (24 * density), (int) (20 * density), (int) (24 * density), (int) (20 * density));
        root.setBackgroundColor(Color.TRANSPARENT);

        TextView tvTitle = new TextView(activity);
        tvTitle.setText(R.string.set_default_home_title);
        tvTitle.setTextSize(18f);
        tvTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tvTitle.setTypeface(font, Typeface.BOLD);
        tvTitle.setPadding(0, 0, 0, (int) (8 * density));
        root.addView(tvTitle);

        TextView tvDesc = new TextView(activity);
        tvDesc.setText(isDefault
                ? activity.getString(R.string.default_launcher_success)
                : activity.getString(R.string.default_launcher_explanation, activeLabel));
        tvDesc.setTextSize(13f);
        tvDesc.setTextColor(isDefault ? 0xFF34D399 : TvUiTokens.TEXT_SECONDARY);
        tvDesc.setTypeface(font, Typeface.NORMAL);
        tvDesc.setLineSpacing(0, 1.15f);
        tvDesc.setPadding(0, 0, 0, (int) (12 * density));
        root.addView(tvDesc);

        TextView currentHeading = new TextView(activity);
        currentHeading.setText(R.string.home_current_heading);
        currentHeading.setTextSize(12f);
        currentHeading.setTextColor(TvUiTokens.ACCENT_CYAN);
        currentHeading.setTypeface(font, Typeface.BOLD);
        root.addView(currentHeading);

        TextView currentValue = new TextView(activity);
        currentValue.setText(activeLabel + (activePkg.isEmpty() ? "" : "  •  " + activePkg));
        currentValue.setTextSize(12.5f);
        currentValue.setTextColor(TvUiTokens.NAV_ITEM_TEXT);
        currentValue.setTypeface(font, Typeface.NORMAL);
        currentValue.setPadding(0, (int) (3 * density), 0, (int) (10 * density));
        root.addView(currentValue);

        TextView candidatesHeading = new TextView(activity);
        candidatesHeading.setText(R.string.home_candidates_heading);
        candidatesHeading.setTextSize(12f);
        candidatesHeading.setTextColor(TvUiTokens.ACCENT_CYAN);
        candidatesHeading.setTypeface(font, Typeface.BOLD);
        root.addView(candidatesHeading);

        if (candidates.isEmpty()) {
            TextView none = new TextView(activity);
            none.setText(R.string.home_status_unknown);
            none.setTextSize(12.5f);
            none.setTextColor(TvUiTokens.TEXT_SECONDARY);
            none.setPadding(0, (int) (3 * density), 0, (int) (10 * density));
            root.addView(none);
        } else {
            for (ResolveInfo candidate : candidates) {
                String label = DeviceSettingsStatus.getDisplayLabel(activity.getPackageManager(), candidate);
                String pkg = candidate.activityInfo != null ? candidate.activityInfo.packageName : "";
                TextView item = new TextView(activity);
                boolean isSelectedHome = !activePkg.isEmpty() && activePkg.equals(pkg);
                item.setText((isSelectedHome ? "✓ " : "• ") + label + "  (" + pkg + ")");
                item.setTextSize(12.5f);
                item.setTextColor(isSelectedHome ? 0xFF34D399 : TvUiTokens.TEXT_SECONDARY);
                item.setTypeface(font, Typeface.NORMAL);
                item.setPadding((int) (4 * density), (int) (3 * density), 0, (int) (3 * density));
                root.addView(item);
            }
        }

        AlertDialog[] dialogHolder = new AlertDialog[1];

        LinearLayout btnSettings = buildCardRow(activity, density, font,
                activity.getString(R.string.open_home_picker_title),
                activity.getString(R.string.open_home_picker_summary),
                () -> {
                    boolean opened = openDeviceSettings(activity, new Intent(Settings.ACTION_HOME_SETTINGS),
                            new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS));
                    if (!opened) {
                        TvFeedback.show(activity, R.string.home_picker_unavailable, Toast.LENGTH_LONG);
                    } else if (dialogHolder[0] != null) {
                        dialogHolder[0].dismiss();
                    }
                });
        root.addView(btnSettings);

        LinearLayout btnAdb = buildCardRow(activity, density, font,
                activity.getString(R.string.set_home_adb_title),
                activity.getString(R.string.adb_command_guidance),
                () -> {
                    String command = "adb shell cmd package set-home-activity com.gathin.tvhome/.MainActivity";
                    try {
                        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard == null) throw new IllegalStateException("Clipboard unavailable");
                        clipboard.setPrimaryClip(ClipData.newPlainText("Glass TV HOME command", command));
                        TvFeedback.show(activity, R.string.adb_command_copied, Toast.LENGTH_LONG);
                    } catch (Exception unavailable) {
                        TvFeedback.show(activity, R.string.adb_home_command_hint, Toast.LENGTH_LONG);
                    }
                });
        root.addView(btnAdb);

        LinearLayout btnPhone = buildCardRow(activity, density, font,
                activity.getString(R.string.phone_installer_title),
                activity.getString(R.string.phone_installer_summary),
                () -> showPhoneInstallerDialog(activity, density, font));
        root.addView(btnPhone);

        LinearLayout btnRefresh = buildCardRow(activity, density, font,
                activity.getString(R.string.home_refresh_action),
                activity.getString(R.string.home_refresh_summary),
                () -> {
                    if (dialogHolder[0] != null) dialogHolder[0].dismiss();
                    showDefaultLauncherDialog(activity, density, font);
                });
        root.addView(btnRefresh);

        Button btnClose = new Button(activity);
        btnClose.setText(R.string.close_label);
        btnClose.setTextColor(TvUiTokens.TEXT_PRIMARY);
        btnClose.setTypeface(font, Typeface.BOLD);
        btnClose.setTextSize(14f);
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setColor(TvUiTokens.SURFACE_IDLE);
        closeBg.setCornerRadius(10 * density);
        btnClose.setBackground(closeBg);
        btnClose.setFocusable(true);
        btnClose.setPadding((int) (16 * density), (int) (8 * density), (int) (16 * density), (int) (8 * density));
        TvFocusIndicator.applyRoundedTextFocus(btnClose, density, 10f,
                TvUiTokens.SURFACE_IDLE, TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER,
                TvUiTokens.FOCUS_BORDER, TvUiTokens.TEXT_PRIMARY,
                TvUiTokens.TEXT_PRIMARY, 1.0f);
        btnClose.setOnClickListener(v -> {
            SoundEffects.playSelect();
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
        });

        LinearLayout.LayoutParams closeLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (42 * density));
        closeLp.topMargin = (int) (12 * density);
        root.addView(btnClose, closeLp);

        scroll.addView(root, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AlertDialog.Builder pb = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        pb.setView(scroll);
        AlertDialog d = pb.create();
        dialogHolder[0] = d;
        d.show();

        activity.styleDeviceSettingsPopup(d, 620, 560);

        btnSettings.requestFocus();
    }

    private static void showPhoneInstallerDialog(MainActivity activity, float density, Typeface font) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        final String downloadPage = "https://github.com/Frangga11/glass-tv-home/tree/main/release";

        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setPadding((int) (24 * density), (int) (18 * density),
                (int) (24 * density), (int) (10 * density));

        TextView hint = new TextView(activity);
        hint.setText(R.string.phone_installer_qr_hint);
        hint.setTextSize(13f);
        hint.setTextColor(TvUiTokens.NAV_ITEM_TEXT);
        hint.setTypeface(font, Typeface.NORMAL);
        hint.setGravity(Gravity.CENTER);
        content.addView(hint);

        Bitmap qr = QrBitmapUtil.createQrBitmap(downloadPage, (int) (220 * density));
        if (qr != null) {
            ImageView qrView = new ImageView(activity);
            qrView.setImageBitmap(qr);
            qrView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            qrView.setBackgroundColor(Color.WHITE);
            qrView.setPadding((int) (8 * density), (int) (8 * density),
                    (int) (8 * density), (int) (8 * density));
            LinearLayout.LayoutParams qrLp = new LinearLayout.LayoutParams(
                    (int) (236 * density), (int) (236 * density));
            qrLp.topMargin = (int) (12 * density);
            qrLp.bottomMargin = (int) (12 * density);
            content.addView(qrView, qrLp);
        }

        TextView steps = new TextView(activity);
        steps.setText(activity.getString(R.string.phone_installer_steps)
                + "\n\n" + downloadPage);
        steps.setTextSize(12.5f);
        steps.setTextColor(TvUiTokens.TEXT_SECONDARY);
        steps.setTypeface(font, Typeface.NORMAL);
        steps.setLineSpacing(0, 1.15f);
        content.addView(steps);

        AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                .setTitle(R.string.phone_installer_title)
                .setView(content)
                .setPositiveButton(R.string.close_label, (d, which) -> d.dismiss())
                .create();
        dialog.show();
        activity.styleDeviceSettingsPopup(dialog, 560, 0);
        Button close = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (close != null) close.requestFocus();
    }

    private static void openDeviceSettingsOrFeedback(Context context, Intent... intents) {
        if (!openDeviceSettings(context, intents)) {
            TvFeedback.show(context, R.string.android_settings_unavailable, Toast.LENGTH_LONG);
        }
    }

    private static boolean openDeviceSettings(Context context, Intent... intents) {
        return SystemSettingsAccess.open(context, intents);
    }

    interface EditNameCallback {
        void onSave(String name);
    }

    interface SwitchCallback {
        void onChanged(boolean isChecked);
    }
}

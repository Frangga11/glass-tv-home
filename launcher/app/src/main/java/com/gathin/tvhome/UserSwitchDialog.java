package com.gathin.tvhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Fast floating TV popup for switching user profiles and managing Child Mode.
 * Triggered from top bar Candy/User button.
 */
public final class UserSwitchDialog {

    public static void show(MainActivity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        SoundEffects.playSelect();

        float density = activity.getResources().getDisplayMetrics().density;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        Typeface outfit = activity.getResources().getFont(R.font.inter);

        AlertDialog[] dialogHolder = new AlertDialog[1];

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding((int) (22 * density), (int) (20 * density), (int) (22 * density), (int) (20 * density));
        root.setClipChildren(false);
        root.setClipToPadding(false);

        root.setBackground(TvDialogStyle.createDialogSurface(activity));

        // Header Title
        LinearLayout header = new LinearLayout(activity);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        ImageView ivIcon = new ImageView(activity);
        ivIcon.setImageResource(R.drawable.ic_shield_child);
        ivIcon.setColorFilter(TvUiTokens.WARNING); // Amber
        header.addView(ivIcon, new LinearLayout.LayoutParams((int) (24 * density), (int) (24 * density)));

        LinearLayout headerTextCol = new LinearLayout(activity);
        headerTextCol.setOrientation(LinearLayout.VERTICAL);
        headerTextCol.setPadding((int) (10 * density), 0, 0, 0);

        TextView tvTitle = new TextView(activity);
        tvTitle.setText(R.string.switch_user_title);
        tvTitle.setTextSize(TvUiTokens.SECTION_TITLE_SP);
        tvTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tvTitle.setTypeface(outfit, Typeface.BOLD);
        headerTextCol.addView(tvTitle);

        TextView tvSub = new TextView(activity);
        tvSub.setText(R.string.switch_user_subtitle);
        tvSub.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
        tvSub.setTextColor(TvUiTokens.TEXT_SECONDARY);
        tvSub.setTypeface(outfit, Typeface.NORMAL);
        headerTextCol.addView(tvSub);

        header.addView(headerTextCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        root.addView(header);

        // Divider
        View div1 = new View(activity);
        div1.setBackgroundColor(TvUiTokens.BORDER);
        LinearLayout.LayoutParams divLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1 * density));
        divLp.topMargin = (int) (14 * density);
        divLp.bottomMargin = (int) (14 * density);
        root.addView(div1, divLp);

        // Section 1: User Profiles
        TextView tvProfSec = new TextView(activity);
        tvProfSec.setText(R.string.switch_user_section);
        tvProfSec.setTextSize(TvUiTokens.SECTION_LABEL_SP);
        tvProfSec.setTextColor(TvUiTokens.TEXT_SECONDARY);
        tvProfSec.setTypeface(outfit, Typeface.BOLD);
        root.addView(tvProfSec);

        HorizontalScrollView profScroll = new HorizontalScrollView(activity);
        profScroll.setHorizontalScrollBarEnabled(false);
        profScroll.setClipChildren(false);
        profScroll.setClipToPadding(false);
        profScroll.setPadding(0, (int) (10 * density), 0, (int) (12 * density));

        LinearLayout profRow = new LinearLayout(activity);
        profRow.setOrientation(LinearLayout.HORIZONTAL);
        profRow.setGravity(Gravity.CENTER_VERTICAL);
        profRow.setClipChildren(false);
        profRow.setClipToPadding(false);

        List<ProfileManager.UserProfile> profiles = ProfileManager.getAllProfiles(activity);
        ProfileManager.UserProfile activeProfile = ProfileManager.getActiveProfile(activity);

        for (ProfileManager.UserProfile prof : profiles) {
            boolean isActive = prof.id.equals(activeProfile.id);

            LinearLayout itemCol = new LinearLayout(activity);
            itemCol.setOrientation(LinearLayout.VERTICAL);
            itemCol.setGravity(Gravity.CENTER_HORIZONTAL);
            itemCol.setFocusable(true);
            itemCol.setClickable(true);
            itemCol.setPadding((int) (8 * density), (int) (6 * density), (int) (8 * density), (int) (6 * density));

            GradientDrawable iBg = new GradientDrawable();
            iBg.setColor(isActive ? TvUiTokens.NAV_PROFILE_SELECTED : Color.TRANSPARENT);
            iBg.setCornerRadius(14 * density);
            if (isActive) iBg.setStroke((int) (1.5f * density), TvUiTokens.NAV_SELECTION_BORDER);
            itemCol.setBackground(iBg);

            // Avatar Frame with Ring
            FrameLayout aFrame = new FrameLayout(activity);
            int frameDim = (int) (60 * density);

            View ring = new View(activity);
            GradientDrawable ringBg = new GradientDrawable();
            ringBg.setShape(GradientDrawable.OVAL);
            ringBg.setColor(Color.TRANSPARENT);
            ringBg.setStroke((int) (3 * density), isActive ? TvUiTokens.NAV_SELECTION_BORDER : Color.TRANSPARENT);
            ring.setBackground(ringBg);
            aFrame.addView(ring, new FrameLayout.LayoutParams(frameDim, frameDim));

            boolean profileChildMode = ChildModeManager.isChildModeEnabled(activity, prof.id);
            ImageView aImg = new ImageView(activity);
            aImg.setImageDrawable(ProfileManager.getAvatarDrawable(activity, prof,
                    Math.round(48 * density), profileChildMode));
            int imgDim = (int) (48 * density);
            aFrame.addView(aImg, new FrameLayout.LayoutParams(imgDim, imgDim, Gravity.CENTER));

            itemCol.addView(aFrame, new LinearLayout.LayoutParams(frameDim, frameDim));

            TextView tvName = new TextView(activity);
            tvName.setText(prof.name + (profileChildMode ? " 👶" : ""));
            tvName.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
            tvName.setTextColor(isActive ? TvUiTokens.TEXT_PRIMARY : TvUiTokens.TEXT_SECONDARY);
            tvName.setTypeface(outfit, isActive ? Typeface.BOLD : Typeface.NORMAL);
            tvName.setPadding(0, (int) (6 * density), 0, 0);
            tvName.setSingleLine(true);
            tvName.setEllipsize(android.text.TextUtils.TruncateAt.END);
            itemCol.addView(tvName);

            itemCol.setOnFocusChangeListener((v, hasFocus) -> {
                GradientDrawable g = new GradientDrawable();
                g.setColor(hasFocus ? TvUiTokens.SURFACE_FOCUSED : (isActive ? TvUiTokens.NAV_PROFILE_SELECTED : Color.TRANSPARENT));
                g.setCornerRadius(14 * density);
                if (hasFocus) {
                    g.setStroke((int) (2 * density), TvUiTokens.FOCUS_BORDER);
                } else if (isActive) {
                    g.setStroke((int) (1.5f * density), TvUiTokens.NAV_SELECTION_BORDER);
                }
                v.setBackground(g);
                v.animate().scaleX(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).scaleY(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
            });

            itemCol.setOnClickListener(v -> {
                SoundEffects.playSelect();
                if (isActive) {
                    TvFeedback.show(activity, activity.getString(R.string.profile_already_active, prof.name), Toast.LENGTH_SHORT);
                    return;
                }

                requestProfileSwitch(activity, prof, dialogHolder[0], null);
            });

            LinearLayout.LayoutParams iLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iLp.setMarginEnd((int) (12 * density));
            profRow.addView(itemCol, iLp);
        }

        // Add Profile Button in Row
        LinearLayout addCol = new LinearLayout(activity);
        addCol.setOrientation(LinearLayout.VERTICAL);
        addCol.setGravity(Gravity.CENTER_HORIZONTAL);
        addCol.setFocusable(true);
        addCol.setClickable(true);
        addCol.setPadding((int) (12 * density), (int) (8 * density), (int) (12 * density), (int) (8 * density));

        GradientDrawable addBg = new GradientDrawable();
        addBg.setColor(TvUiTokens.SURFACE_IDLE);
        addBg.setCornerRadius(14 * density);
        addCol.setBackground(addBg);

        FrameLayout plusFrame = new FrameLayout(activity);
        int pfDim = (int) (52 * density);
        GradientDrawable pfBg = new GradientDrawable();
        pfBg.setShape(GradientDrawable.OVAL);
        pfBg.setColor(TvUiTokens.SURFACE);
        plusFrame.setBackground(pfBg);

        TextView tvPlus = new TextView(activity);
        tvPlus.setText("＋");
        tvPlus.setTextSize(TvUiTokens.SECTION_TITLE_SP);
        tvPlus.setTextColor(TvUiTokens.FOCUS_BORDER);
        tvPlus.setGravity(Gravity.CENTER);
        plusFrame.addView(tvPlus, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addCol.addView(plusFrame, new LinearLayout.LayoutParams(pfDim, pfDim));

        TextView tvAddLbl = new TextView(activity);
        tvAddLbl.setText(R.string.add_user_label);
        tvAddLbl.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
        tvAddLbl.setTextColor(TvUiTokens.TEXT_SECONDARY);
        tvAddLbl.setTypeface(outfit, Typeface.NORMAL);
        tvAddLbl.setPadding(0, (int) (6 * density), 0, 0);
        addCol.addView(tvAddLbl);

        addCol.setOnFocusChangeListener((v, hasFocus) -> {
            GradientDrawable g = new GradientDrawable();
            g.setColor(hasFocus ? TvUiTokens.SURFACE_FOCUSED : TvUiTokens.SURFACE_IDLE);
            g.setCornerRadius(14 * density);
            if (hasFocus) g.setStroke((int) (2 * density), TvUiTokens.FOCUS_BORDER);
            v.setBackground(g);
            v.animate().scaleX(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).scaleY(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
        });

        addCol.setOnClickListener(v -> {
            SoundEffects.playSelect();
            showAddProfileDialog(activity, density, outfit, () -> {
                if (dialogHolder[0] != null) dialogHolder[0].dismiss();
                show(activity);
            });
        });

        profRow.addView(addCol);
        profScroll.addView(profRow);
        root.addView(profScroll);

        // Close / Batal Button
        Button btnClose = new Button(activity);
        btnClose.setText(R.string.close_label);
        btnClose.setTextSize(TvUiTokens.NAV_ITEM_TEXT_SP);
        btnClose.setTextColor(TvUiTokens.TEXT_SECONDARY);
        btnClose.setTypeface(outfit, Typeface.BOLD);
        btnClose.setFocusable(true);
        btnClose.setClickable(true);

        GradientDrawable cBg = new GradientDrawable();
        cBg.setColor(TvUiTokens.SURFACE);
        cBg.setCornerRadius(10 * density);
        btnClose.setBackground(cBg);

        btnClose.setOnFocusChangeListener((v, hasFocus) -> {
            GradientDrawable g = new GradientDrawable();
            g.setColor(hasFocus ? TvUiTokens.SURFACE_FOCUSED : TvUiTokens.SURFACE);
            g.setCornerRadius(10 * density);
            if (hasFocus) g.setStroke((int) (2 * density), TvUiTokens.FOCUS_BORDER);
            v.setBackground(g);
        });

        btnClose.setOnClickListener(v -> {
            SoundEffects.playSelect();
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
        });

        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (42 * density));
        root.addView(btnClose, btnLp);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        dialogHolder[0] = dialog;
        dialog.show();

        TvDialogStyle.applyCustom(dialog, activity, 580, 0, 0.68f);

        profRow.getChildAt(0).requestFocus();
    }

    /** Centralized profile-switch policy shared by every profile entry point. */
    static void requestProfileSwitch(MainActivity activity, ProfileManager.UserProfile target,
                                     AlertDialog dialog, Runnable afterSwitch) {
        if (activity == null || target == null) return;
        ProfileManager.UserProfile current = ProfileManager.getActiveProfile(activity);
        if (current == null || target.id.equals(current.id)) return;

        boolean currentChildMode = ChildModeManager.isChildModeEnabled(activity, current.id);
        Runnable commitSwitch = () -> {
            switchProfile(activity, target, dialog);
            if (afterSwitch != null) afterSwitch.run();
        };
        if (ChildModeManager.requiresParentPinToSwitch(currentChildMode)) {
            ChildModeManager.promptPin(activity, activity.getString(R.string.verify_parent_pin_title),
                    activity.getString(R.string.switch_from_child_pin_prompt, target.name),
                    new ChildModeManager.PinCallback() {
                        @Override public void onSuccess() { commitSwitch.run(); }
                        @Override public void onCancelled() { }
                    });
        } else {
            commitSwitch.run();
        }
    }

    /** Parent PIN is required before any profile can be deleted. */
    static void requestDeleteProfile(MainActivity activity, String profileId, String profileName,
                                     Runnable afterDelete) {
        if (activity == null || profileId == null) return;
        ChildModeManager.promptPin(activity, activity.getString(R.string.parent_pin_delete_title),
                activity.getString(R.string.parent_pin_delete_message, profileName),
                new ChildModeManager.PinCallback() {
                    @Override public void onSuccess() {
                        AlertDialog deleteDialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                                .setTitle(activity.getString(R.string.delete_profile_title, profileName))
                                .setMessage(R.string.delete_profile_message)
                                .setNegativeButton(R.string.cancel_action, (d, w) -> d.dismiss())
                                .setPositiveButton(R.string.delete_action, (d, w) -> {
                                    boolean deleted = ProfileManager.deleteProfile(activity, profileId);
                                    if (deleted) {
                                        TvFeedback.show(activity, activity.getString(R.string.profile_deleted, profileName), Toast.LENGTH_SHORT);
                                        if (afterDelete != null) afterDelete.run();
                                        activity.reloadTiles();
                                        activity.updateTopBarUserIcon();
                                    } else {
                                        TvFeedback.show(activity, R.string.profile_delete_failed, Toast.LENGTH_LONG);
                                    }
                                }).create();
                        deleteDialog.show();
                        TvDialogStyle.apply(deleteDialog, activity, 520, 0);
                    }
                    @Override public void onCancelled() { }
                });
    }

    static void switchProfile(MainActivity activity, ProfileManager.UserProfile prof, AlertDialog dialog) {
        if (activity == null || prof == null) return;
        ProfileManager.setActiveProfile(activity, prof.id);
        activity.reloadTiles();
        activity.updateTopBarUserIcon();
        TvFeedback.show(activity, activity.getString(R.string.profile_active_message, prof.name,
                ChildModeManager.isChildModeEnabled(activity, prof.id) ? activity.getString(R.string.child_mode_suffix) : ""),
                Toast.LENGTH_SHORT);
        if (dialog != null) dialog.dismiss();
    }

    public static void showAddProfileDialog(MainActivity activity, float density, Typeface font, Runnable onAdded) {
        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding((int) (22 * density), (int) (18 * density), (int) (22 * density), (int) (16 * density));

        content.setBackground(TvDialogStyle.createDialogSurface(activity));

        TextView tvTitle = new TextView(activity);
        tvTitle.setText(R.string.add_user_title);
        tvTitle.setTextSize(TvUiTokens.SECTION_TITLE_SP);
        tvTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
        tvTitle.setTypeface(font, Typeface.BOLD);
        content.addView(tvTitle);

        TextView tvSub = new TextView(activity);
        tvSub.setText(R.string.add_user_subtitle);
        tvSub.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
        tvSub.setTextColor(TvUiTokens.TEXT_SECONDARY);
        tvSub.setTypeface(font, Typeface.NORMAL);
        tvSub.setPadding(0, (int) (2 * density), 0, (int) (12 * density));
        content.addView(tvSub);

        // Edit text for Name
        EditText et = new EditText(activity);
        et.setHint(R.string.add_user_name_hint);
        et.setHintTextColor(TvUiTokens.TEXT_SECONDARY);
        et.setTextColor(TvUiTokens.TEXT_PRIMARY);
        et.setTextSize(TvUiTokens.BODY_SP);
        et.setTypeface(font, Typeface.NORMAL);
        et.setPadding((int) (14 * density), (int) (10 * density), (int) (14 * density), (int) (10 * density));

        GradientDrawable etBg = new GradientDrawable();
        etBg.setColor(TvUiTokens.INPUT_SURFACE);
        etBg.setCornerRadius(10 * density);
        etBg.setStroke((int) (1 * density), TvUiTokens.BORDER);
        et.setBackground(etBg);
        TvFocusIndicator.applyRoundedTextFocus(et, density, 10f,
                TvUiTokens.INPUT_SURFACE, TvUiTokens.SURFACE_FOCUSED, TvUiTokens.BORDER, TvUiTokens.FOCUS_BORDER,
                TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY, 1.0f);
        content.addView(et);

        // Avatar Picker Header
        TextView tvAvHeader = new TextView(activity);
        tvAvHeader.setText(R.string.choose_avatar_label);
        tvAvHeader.setTextSize(TvUiTokens.SECTION_LABEL_SP);
        tvAvHeader.setTextColor(TvUiTokens.TEXT_SECONDARY);
        tvAvHeader.setTypeface(font, Typeface.BOLD);
        tvAvHeader.setPadding(0, (int) (12 * density), 0, (int) (6 * density));
        content.addView(tvAvHeader);

        // Horizontal Avatar Picker Row
        HorizontalScrollView avScroll = new HorizontalScrollView(activity);
        avScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout avRow = new LinearLayout(activity);
        avRow.setOrientation(LinearLayout.HORIZONTAL);
        avRow.setPadding(0, (int) (4 * density), 0, (int) (8 * density));

        int[] chosenAvatar = new int[]{0}; // default Fox
        View[] rings = new View[7];

        for (int i = 0; i < 7; i++) {
            final int idx = i;
            FrameLayout frame = new FrameLayout(activity);
            frame.setFocusable(true);
            frame.setClickable(true);

            int frameDim = (int) (52 * density);
            LinearLayout.LayoutParams fLp = new LinearLayout.LayoutParams(frameDim, frameDim);
            fLp.setMarginEnd((int) (8 * density));
            frame.setLayoutParams(fLp);

            View ring = new View(activity);
            GradientDrawable ringBg = new GradientDrawable();
            ringBg.setShape(GradientDrawable.OVAL);
            ringBg.setColor(Color.TRANSPARENT);
            ringBg.setStroke((int) (2.5f * density), TvUiTokens.NAV_SELECTION_BORDER);
            ring.setBackground(ringBg);
            ring.setVisibility(idx == 0 ? View.VISIBLE : View.GONE);
            frame.addView(ring, new FrameLayout.LayoutParams(frameDim, frameDim));
            rings[idx] = ring;
            ImageView img = new ImageView(activity);
            img.setImageResource(ProfileManager.getAvatarResId(idx));
            int innerDim = (int) (42 * density);
            frame.addView(img, new FrameLayout.LayoutParams(innerDim, innerDim, Gravity.CENTER));
            // Draw above the avatar image so the focus ring remains visible at every edge.
            TvFocusIndicator.attachAvatarFocusRing(frame, frameDim, density);

            frame.setOnClickListener(v -> {
                SoundEffects.playSelect();
                chosenAvatar[0] = idx;
                for (int k = 0; k < 7; k++) {
                    if (rings[k] != null) rings[k].setVisibility(k == idx ? View.VISIBLE : View.GONE);
                }
            });

            avRow.addView(frame);
        }
        avScroll.addView(avRow);
        content.addView(avScroll);

        // Checkbox: Child Mode profile
        CheckBox cbChild = new CheckBox(activity);
        cbChild.setText(R.string.child_profile_checkbox);
        cbChild.setTextColor(TvUiTokens.TEXT_SECONDARY);
        cbChild.setTextSize(TvUiTokens.CARD_SUBTITLE_SP);
        cbChild.setTypeface(font, Typeface.NORMAL);
        cbChild.setPadding((int) (6 * density), (int) (6 * density), 0, (int) (12 * density));
        content.addView(cbChild);

        AlertDialog[] dHolder = new AlertDialog[1];

        // Button Row: Cancel & Simpan
        LinearLayout btnRow = new LinearLayout(activity);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        btnRow.setGravity(Gravity.END);

        Button btnCancel = new Button(activity);
        btnCancel.setText(R.string.cancel_action);
        btnCancel.setTextColor(TvUiTokens.TEXT_SECONDARY);
        btnCancel.setTypeface(font, Typeface.NORMAL);
        btnCancel.setFocusable(true);
        btnCancel.setClickable(true);
        GradientDrawable cBg = new GradientDrawable();
        cBg.setColor(TvUiTokens.INPUT_SURFACE);
        cBg.setCornerRadius(8 * density);
        btnCancel.setBackground(cBg);
        btnCancel.setPadding((int) (16 * density), 0, (int) (16 * density), 0);
        btnCancel.setOnFocusChangeListener((v, hasFocus) -> {
            cBg.setStroke((int) (2 * density), hasFocus ? TvUiTokens.FOCUS_BORDER : 0);
            btnCancel.setTextColor(hasFocus ? TvUiTokens.TEXT_PRIMARY : TvUiTokens.TEXT_SECONDARY);
            v.animate().scaleX(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).scaleY(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
        });
        btnCancel.setOnClickListener(v -> {
            if (dHolder[0] != null) dHolder[0].dismiss();
        });
        btnRow.addView(btnCancel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (40 * density)));

        Button btnSave = new Button(activity);
        btnSave.setText(R.string.save_profile_action);
        btnSave.setTextColor(TvUiTokens.TEXT_PRIMARY);
        btnSave.setTextSize(TvUiTokens.NAV_ITEM_TEXT_SP);
        btnSave.setTypeface(font, Typeface.BOLD);
        btnSave.setFocusable(true);
        btnSave.setClickable(true);
        GradientDrawable sBg = new GradientDrawable();
        sBg.setColor(TvUiTokens.SUCCESS); // Emerald
        sBg.setCornerRadius(8 * density);
        btnSave.setBackground(sBg);
        btnSave.setPadding((int) (16 * density), 0, (int) (16 * density), 0);
        btnSave.setOnFocusChangeListener((v, hasFocus) -> {
            sBg.setStroke((int) (2 * density), hasFocus ? TvUiTokens.FOCUS_BORDER : 0);
            v.animate().scaleX(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).scaleY(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f).setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
        });

        btnSave.setOnClickListener(v -> {
            String name = et.getText().toString().trim();
            if (name.isEmpty()) {
                TvFeedback.show(activity, R.string.user_name_required, Toast.LENGTH_SHORT);
                return;
            }
            SoundEffects.playSelect();
            ProfileManager.addProfile(activity, name, chosenAvatar[0], cbChild.isChecked());
            TvFeedback.show(activity, activity.getString(R.string.profile_created, name), Toast.LENGTH_SHORT);
            if (dHolder[0] != null) dHolder[0].dismiss();
            if (onAdded != null) onAdded.run();
        });

        LinearLayout.LayoutParams svLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (40 * density));
        svLp.setMarginStart((int) (10 * density));
        btnRow.addView(btnSave, svLp);
        content.addView(btnRow);

        AlertDialog.Builder b = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog);
        b.setView(content);
        AlertDialog d = b.create();
        dHolder[0] = d;
        d.show();

        TvDialogStyle.applyCustom(d, activity, 520, 0, 0.68f);
    }
}

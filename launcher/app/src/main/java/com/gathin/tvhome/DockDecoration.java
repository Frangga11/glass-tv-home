package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws the glass tray behind the independent Dock surface and adds orientation-aware spacing: the blurred wallpaper
 * behind it, an even veil and a light rim, with no brighter top.
 */
public class DockDecoration extends RecyclerView.ItemDecoration {

    private int dockSize;
    private final float density;
    private boolean vertical;
    /** Non-visual scroll extent after the last horizontal carousel card. */
    private int horizontalTailInsetPx;
    private final Paint trayShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint solidTrayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint solidTrayBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final UniversalGlassDrawable universalGlass;
    private Glider glider;
    private final int[] at = new int[2], gliderAt = new int[2];


    private final RectF rect = new RectF();
    /** The tray as last drawn, in the grid's coordinates: where the focus shape shows on it. */
    final RectF area = new RectF();

    DockDecoration(int dockSize, float density) {
        this(dockSize, density, false);
    }

    DockDecoration(int dockSize, float density, boolean vertical) {
        this.dockSize = dockSize;
        this.density = density;
        this.vertical = vertical;
        // Ported Universal Home surface material; focus remains owned by the existing Glider.
        universalGlass = new UniversalGlassDrawable(density, 26f);
        trayShadowPaint.setColor(0x38000000);
        solidTrayPaint.setColor(TvUiTokens.DOCK_SURFACE);
        solidTrayBorder.setColor(TvUiTokens.BORDER);
        solidTrayBorder.setStyle(Paint.Style.STROKE);
        solidTrayBorder.setStrokeWidth(1.5f * density);
        trayShadowPaint.setMaskFilter(new android.graphics.BlurMaskFilter(14f * density, android.graphics.BlurMaskFilter.Blur.NORMAL));

    }

    void update(int dockSize, boolean vertical) {
        this.dockSize = Math.max(0, dockSize);
        this.vertical = vertical;
    }

    /** Returns true when the final-card scroll extent changed and offsets need recalculation. */
    boolean setHorizontalTailInsetPx(int insetPx) {
        int next = Math.max(0, insetPx);
        if (horizontalTailInsetPx == next) return false;
        horizontalTailInsetPx = next;
        return true;
    }

    /** The focus shape: the tray's glass would hide it, so the tray paints it over the glass itself. */
    void setGlider(Glider glider) {
        this.glider = glider;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos >= 0 && pos < dockSize) {
            if (vertical) {
                outRect.left = DockGeometry.dockItemTopOffsetPx(density);
                outRect.right = DockGeometry.dockItemBottomOffsetPx(density);
            } else {
                outRect.top = DockGeometry.dockItemTopOffsetPx(density);
                outRect.bottom = DockGeometry.horizontalDockItemBottomOffsetPx(density);
            }
        }
        // Add content extent after the final card without shrinking RecyclerView's viewport.
        if (!vertical && pos >= 0 && pos == state.getItemCount() - 1) {
            outRect.right += horizontalTailInsetPx;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        boolean any = false;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            if (pos < 0 || pos >= dockSize) continue;
            View card = child.findViewById(R.id.card);
            if (card == null) continue;
            float l = child.getLeft() + child.getTranslationX() + card.getLeft();
            float t = child.getTop() + child.getTranslationY() + card.getTop();
            float r = l + card.getWidth();
            float b = t + card.getHeight();
            if (!any) {
                rect.set(l, t, r, b);
                any = true;
            } else {
                rect.union(l, t, r, b);
            }
        }
        if (!any) {
            area.setEmpty();
            return;
        }
        float pad = DockGeometry.trayPaddingPx(density);
        rect.inset(-pad, -pad);
        if (!vertical) {
            // Horizontal tray spans the same content frame as the clock/weather and
            // prayer/ayah shelves; tile group remains centered within this full-width glass.
            rect.left = 0f;
            rect.right = parent.getWidth();
        }
        area.set(rect);
        float radius = 26 * density;
        boolean glassEnabled = parent.getContext().getSharedPreferences("wallpaper_prefs", android.content.Context.MODE_PRIVATE)
                .getBoolean("glass_effects_enabled", true);
        if (!glassEnabled) {
            // Keep the dock tray's structure and contrast; only remove the glass treatment.
            RectF solidShadow = new RectF(rect);
            solidShadow.offset(0, 4f * density);
            c.drawRoundRect(solidShadow, radius, radius, trayShadowPaint);
            c.drawRoundRect(rect, radius, radius, solidTrayPaint);
            c.drawRoundRect(rect, radius, radius, solidTrayBorder);
            // Keep D-pad focus feedback.
            if (glider != null && glider.isShowing()) {
                parent.getLocationInWindow(at);
                glider.getLocationInWindow(gliderAt);
                c.save();
                RectF clipArea = new RectF(rect);
                clipArea.inset(-36f * density, -36f * density);
                c.clipRect(clipArea);
                glider.drawShape(c, gliderAt[0] - at[0], gliderAt[1] - at[1], parent);
                c.restore();
            }
            return;
        }
        // Universal Home's DockGlassDrawable material, adapted to the existing tray bounds.
        // Keep the current focus compositor between the glass surface and its reflections/rims.
        universalGlass.drawSurface(c, rect);
        if (glider != null && glider.isShowing()) {
            parent.getLocationInWindow(at);
            glider.getLocationInWindow(gliderAt);
            c.save();
            RectF clipArea = new RectF(rect);
            float shadowBleed = 36f * density;
            clipArea.inset(-shadowBleed, -shadowBleed);
            c.clipRect(clipArea);
            glider.drawShape(c, gliderAt[0] - at[0], gliderAt[1] - at[1], parent);
            c.restore();
        }
        universalGlass.drawReflection(c, rect);
        universalGlass.drawRim(c, rect);
    }
}

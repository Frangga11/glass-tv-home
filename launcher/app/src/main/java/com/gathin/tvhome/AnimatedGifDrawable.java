package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import java.io.File;
import java.io.FileInputStream;

/** Lightweight platform GIF playback for profile avatars (API 26+); no extra decoder dependency. */
final class AnimatedGifDrawable extends Drawable implements Runnable {
    private final Movie movie;
    private final Paint imagePaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    private final Paint ringPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path clip=new Path();
    private final float inset;
    private final int ringColor;
    private long startedAt;
    private boolean running=true;

    static AnimatedGifDrawable fromFile(File file,float density,boolean child) {
        try(FileInputStream in=new FileInputStream(file)) {
            Movie movie=Movie.decodeStream(in);
            return movie==null?null:new AnimatedGifDrawable(movie,density,child);
        } catch(Exception ignored) { return null; }
    }
    private AnimatedGifDrawable(Movie movie,float density,boolean child) {
        this.movie=movie; this.inset=Math.max(1f,2f*density); this.ringColor=child?0xFFF59E0B:0xFF38BDF8;
        ringPaint.setStyle(Paint.Style.STROKE); ringPaint.setStrokeWidth(inset); ringPaint.setColor(ringColor);
    }
    @Override public void draw(Canvas canvas) {
        if(!running) return;
        Rect b=getBounds(); if(b.width()<=0||b.height()<=0) return;
        if(startedAt==0) startedAt=SystemClock.uptimeMillis();
        int duration=Math.max(100,movie.duration());
        int time=(int)((SystemClock.uptimeMillis()-startedAt)%duration);
        movie.setTime(time);
        float radius=Math.min(b.width(),b.height())/2f;
        float cx=b.exactCenterX(), cy=b.exactCenterY();
        clip.reset(); clip.addCircle(cx,cy,Math.max(0,radius-inset),Path.Direction.CW);
        int save=canvas.save(); canvas.clipPath(clip);
        float scale=Math.max((2*radius-2*inset)/Math.max(1,movie.width()),
                (2*radius-2*inset)/Math.max(1,movie.height()));
        float w=movie.width()*scale,h=movie.height()*scale;
        movie.draw(canvas,cx-w/2f,cy-h/2f,imagePaint);
        canvas.restoreToCount(save);
        canvas.drawCircle(cx,cy,radius-inset/2f,ringPaint);
        scheduleSelf(this,SystemClock.uptimeMillis()+33);
    }
    @Override public void run() { invalidateSelf(); }
    @Override protected void onBoundsChange(Rect bounds) { invalidateSelf(); }
    @Override public void setAlpha(int alpha) { imagePaint.setAlpha(alpha); ringPaint.setAlpha(alpha); invalidateSelf(); }
    @Override public void setColorFilter(ColorFilter filter) { imagePaint.setColorFilter(filter); invalidateSelf(); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
    public void stop() { running=false; unscheduleSelf(this); }
}

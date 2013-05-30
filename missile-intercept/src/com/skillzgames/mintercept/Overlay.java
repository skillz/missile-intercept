package com.skillzgames.mintercept;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.skillzgames.mintercept.common.Element;
import com.skillzgames.mintercept.common.Setting;
import com.skillzgames.mintercept.views.Level;
import com.skillzgames.mintercept.views.Title;


public class Overlay extends Element {
    private class OnOff extends Element {
        private Setting setting;
        private int offset;
        private BitmapDrawable on, off;
        private Rect location;
        public OnOff(Context context, View view, Setting setting, int on_res, int off_res, int offset) {
            this.setting = setting;
            this.offset = offset;
            on = (BitmapDrawable)context.getResources().getDrawable(on_res);
            off = (BitmapDrawable)context.getResources().getDrawable(off_res);
        }
        @Override
        public boolean tick() {
            return false;
        }
        @Override
        public void draw(Canvas c, Layer layer) {
            if ( location == null ) {
                location = new Rect();
                location.top = view.getHeight() / 3 + on.getMinimumHeight() * offset * 2;
                location.bottom = location.top + on.getMinimumHeight();
                location.left = ( view.getWidth() - on.getMinimumWidth() ) / 2;
                location.right = location.left +on.getMinimumWidth();
                on.setBounds(location);
                off.setBounds(location);
            }
            if ( setting.get() )
                on.draw(c);
            else
                off.draw(c);
        }
        public boolean onTouchEvent(MotionEvent event) {
            if ( location != null && event.getAction() == MotionEvent.ACTION_DOWN &&
                    event.getY() >= location.top && event.getY() <= location.bottom
            ) setting.toggle();
            return false;
        }
    };

    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private View view;
    private boolean allowBackground;
    private boolean active;
    private Rect location = new Rect();
    
    private OnOff vibrate, sound;


    private Overlay(MIntercept context, View v, boolean allowbg) {
        view = v;
        allowBackground = allowbg;
        deactivate();

        vibrate = new OnOff(context, view, context.vibrator, R.drawable.vibrate_on, R.drawable.vibrate_off, 0);
        sound = new OnOff(context, view, context.sounds, R.drawable.sound_on, R.drawable.sound_off, 1);
    }
    public Overlay(MIntercept context, Title title) {
        this(context, title, true);
    }
    public Overlay(MIntercept context, Level level) {
        this(context, level, false);
    }

    public void activate() {
        active = true;
    }
    public void deactivate() {
        active = false;
    }
    public boolean isActive() {
        return active;
    }

    /**
     * Pass on touch events to the overlay when it is active
     */
    public boolean onTouchEvent(MotionEvent event) {
        if ( active ) {
            vibrate.onTouchEvent(event);
            sound.onTouchEvent(event);
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas c, Layer layer) {
        if ( active ) {
            location.top = 0; location.bottom = view.getHeight();
            location.left = 0; location.right = view.getWidth();
            paint.setColor(Color.BLACK);
            paint.setAlpha(180);
            c.drawRect(location, paint);
            paint.setAlpha(255);

            vibrate.draw(c, layer);
            sound.draw(c, layer);
        }
    }

    @Override
    public boolean tick() {
        vibrate.tick();
        sound.tick();
        return allowBackground || !active;
    }
}

package com.skillzgames.mintercept;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.skillzgames.mintercept.common.Element;
import com.skillzgames.mintercept.R.drawable;


public class City extends Element {
    private MIntercept context;
    private View view;

    private boolean drawcity;
    private Drawable city;
    private int number, outof;
    private Rect location;
    private Explosion explosion;

    public City(Game game, MIntercept context, View view, int number, int outof) {
        this.context = context;
        this.view = view;
        this.number = number;
        this.outof = outof;
        city = context.getResources().getDrawable(drawable.city);
        reset();
    }

    public void reset() {
        explosion = null;
        drawcity = true;
    }
    private boolean isdead() {
        return explosion != null;
    }
    public boolean hasStruck(float x) {
        if ( isdead() )
            return false;
        else
            return x >= location.left && x <= location.right;
    }

    public void explode(Explosion explosion) {
        this.explosion = explosion;
        explosion.setSize(city.getMinimumWidth());
        explosion.reset(
            location.left + (location.right - location.left) / 2,
            location.top + (location.bottom - location.top) / 2
        );
        context.vibrator.vibrate(500);
    }

    @Override
    public boolean tick() {
        if ( drawcity && isdead() && explosion.pastZenith() )
            drawcity = false;
        return !isdead();
    }

    @Override
    public void draw(Canvas c, Layer layer) {
        if ( layer == Layer.CITIES && drawcity ) {
            if ( location == null ) {
                int w = city.getMinimumWidth();
                int spacing = view.getWidth() / outof;
                int left = spacing * number + (spacing - w) / 2;
                location = new Rect(
                        left, view.getHeight() - city.getMinimumHeight(),
                        left + w, view.getHeight()
                    );
            }
            city.setBounds(location);
            city.draw(c);
        }
    }
}

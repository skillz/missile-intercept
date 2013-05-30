package com.skillzgames.mintercept;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.skillzgames.mintercept.common.Element;
import com.skillzgames.mintercept.common.Spectrum;


public class Explosion extends Element {
    private Layer layer;
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean draw = false;
    private Spectrum colour = new Spectrum(0.0f, 0.75f, 0.75f);
    private int paint_color;
    private float cx, cy, inner_radius, outer_radius;
    private int size, fade;

    public Explosion(int s, Layer l) {
        layer = l;
        size = s;
    }
    public Explosion(Game game, int s, Layer l) {
        this(s, l);
        game.explosion(this);
    }

    public void setSize(int s) {
        size = s;
    }
    private void reset() {
        draw = false;
        inner_radius = 0;
        outer_radius = 0;
    }
    public boolean reset(float x, float y) {
        if ( !draw ) {
            reset();
            draw = true;
            cx = x; cy = y;
            fade = 8;
            return true;
        } else
            return false;
    }

    /**
     * Returns true when the explosion has reached it's maximum size
     */
    public boolean pastZenith() {
        return inner_radius > 1;
    }
    /**
     * Returns true if the location is inside the explosion
     */
    public boolean inside(float x, float y) {
        return draw && fade == 8 && ( cx - x ) * ( cx - x ) + ( cy - y ) * ( cy - y ) < outer_radius * outer_radius;
    }

    @Override
    public boolean tick() {
        if ( draw ) {
            if (inner_radius < size) {
                paint_color = colour.next(size);
                if ( outer_radius > size )
                    ++inner_radius;
                else
                    ++outer_radius;
            } else if ( fade > 0 )
                --fade;
            else
                reset();
        }
        return draw;
    }

    @Override
    public void draw(Canvas c, Layer l) {
        if (layer == l && draw ) {
            if (inner_radius < size) {
                paint.setColor(paint_color);
                c.drawCircle(cx, cy, outer_radius, paint);
                if ( outer_radius > size ) {
                    paint.setColor(0xff404040);
                    c.drawCircle(cx, cy, inner_radius, paint);
                }
            } else if ( fade > 0 ) {
                paint.setColor(0x404040 + fade * 0x02000000 + (fade-1) * 0x20000000);
                c.drawCircle(cx, cy, inner_radius, paint);
            }
        }
    }
}

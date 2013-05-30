package com.skillzgames.mintercept.common;

import android.graphics.Canvas;


public abstract class Element {
    public enum Layer {BACKGROUND, CITIES, TRAILS, EXPLOSIONS, MISSILES, CHROME};

    abstract public boolean tick();
    abstract public void draw(Canvas c, Layer layer);
}

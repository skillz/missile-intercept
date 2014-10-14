package com.skillzgames.mintercept.views;

import com.skillzgames.mintercept.MIntercept;
import com.skillzgames.mintercept.Overlay;
import com.skillzgames.mintercept.common.Element;
import com.skillzgames.mintercept.common.Element.Layer;

import android.graphics.Canvas;
import android.view.View;


public abstract class Scene extends View {
    private MIntercept mintercept;
    private Element todraw;
    protected Overlay overlay;


    public Scene(MIntercept context) {
        super(context);
        mintercept = context;
        setFocusable(true);
        setFocusableInTouchMode(true);;
    }

    protected void draw(Element e) {
        todraw = e;
    }
    protected void setOverlay(Overlay o) {
        overlay = o;
    }

    public void toggleOverlay() {
        if ( overlay.isActive() )
            overlay.deactivate();
        else
            overlay.activate();
    }
    public void resume() {
        overlay.deactivate();
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawARGB(255, 0, 0, 0);
        if ( overlay.tick() )
            if ( !todraw.tick() )
                mintercept.endGame();
        todraw.draw(c, Layer.BACKGROUND);
        todraw.draw(c, Layer.CITIES);
        todraw.draw(c, Layer.TRAILS);
        todraw.draw(c, Layer.EXPLOSIONS);
        todraw.draw(c, Layer.MISSILES);
        todraw.draw(c, Layer.CHROME);
        overlay.draw(c, Layer.CHROME);
    }
    
    public abstract void reset();
}


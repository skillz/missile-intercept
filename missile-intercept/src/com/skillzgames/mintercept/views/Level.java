package com.skillzgames.mintercept.views;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.skillzgames.mintercept.Game;
import com.skillzgames.mintercept.MIntercept;
import com.skillzgames.mintercept.Overlay;


public class Level extends Scene {
    private Game game;


    public Level(MIntercept context) {
        super(context);
        game = new Game(context, this);
        draw(game);
        setOverlay(new Overlay(context, this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( !overlay.onTouchEvent(event) ) {
            if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
                game.getPlayer().tap(event.getX(), event.getY());
                return true;
            }
            return false;
        } else
            // Event was handled by overlay
            return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( event.getAction() == MotionEvent.ACTION_DOWN && (
                keyCode == KeyEvent.KEYCODE_DPAD_UP ||
                keyCode == KeyEvent.KEYCODE_MENU
        ) ) {
            toggleOverlay();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public void reset() {
        game.reset();
    }
}

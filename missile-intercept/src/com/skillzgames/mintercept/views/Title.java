package com.skillzgames.mintercept.views;

import java.lang.ref.WeakReference;
import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.skillz.android.client.Skillz;
import com.skillzgames.mintercept.Explosions;
import com.skillzgames.mintercept.MIntercept;
import com.skillzgames.mintercept.Overlay;
import com.skillzgames.mintercept.R;
import com.skillzgames.mintercept.common.Element;


public class Title extends Scene {
    private class Demo extends Element {
        View view;
        BitmapDrawable copyright, instructions, tournaments, logo;
        int instruction_pos, copyright_pos;
        Rect location = new Rect();
        Explosions explosions;
        Random random = new Random();

        public Demo(Context context, View view) {
            this.view = view;
            copyright = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.copyright));
            copyright_pos = -copyright.getMinimumWidth();
            instructions = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.instructions));
            tournaments = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.play_with_friends));
            instruction_pos = -instructions.getMinimumWidth();
            logo = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
            explosions = new Explosions(35, 35);
        }

        @Override
        public boolean tick() {
            if ( random.nextInt(35) == 1 )
                explosions.reset(
                    random.nextInt(view.getWidth()),
                    random.nextInt(view.getHeight())
                );
            explosions.tick();
            instruction_pos -= 1;
            if ( instruction_pos <= -instructions.getMinimumWidth() )
                instruction_pos = view.getWidth() + 5;
            copyright_pos -= 2;
            if ( copyright_pos <= -copyright.getMinimumWidth() )
                copyright_pos = view.getWidth() + 5;
            return true;
        }

        @Override
        public void draw(Canvas c, Layer layer) {
            if ( layer == Layer.BACKGROUND ) {

                location.left = copyright_pos;
                location.right = location.left + copyright.getMinimumWidth();
                location.top = view.getHeight() - copyright.getMinimumHeight();
                location.bottom = location.top + copyright.getMinimumHeight();
                copyright.setBounds(location);
                copyright.draw(c);
            } else if ( layer == Layer.CHROME ) {
                location.left = (view.getWidth() - logo.getMinimumWidth() ) /2;
                location.right = view.getWidth() - location.left;
                location.bottom = Math.max(view.getHeight() / 3, logo.getMinimumHeight());
                location.top = location.bottom - logo.getMinimumHeight();
                logo.setBounds(location);
                logo.draw(c);
                
                location.left = instruction_pos;
                location.right = location.left + instructions.getMinimumWidth();
                location.top = view.getHeight() / 2;
                location.bottom = location.top + instructions.getMinimumHeight();
                instructions.setBounds(location);
                instructions.draw(c);
                
                location.left = (view.getWidth() - tournaments.getMinimumWidth()) / 2;
                location.right = location.left + tournaments.getMinimumWidth();
                location.top = getTournamentsTop();
                location.bottom = location.top + tournaments.getMinimumHeight();
                tournaments.setBounds(location);
                tournaments.draw(c);
            }
            explosions.draw(c, layer);
        }
        
        private int getTournamentsTop() {
        	int h = getHeight(), instructionsHeight = instructions.getBounds().height();
        	
        	return h / 2 + instructionsHeight 
        			+ (h / 2 
	        			- copyright.getBounds().height() 
	        			- tournaments.getBounds().height() 
	        			- instructionsHeight) / 2;
        }
    };
    Demo demo;
    
    


    WeakReference<MIntercept> mintercept;
    public Title(MIntercept mintercept) {
        super(mintercept);
        this.mintercept = new WeakReference<MIntercept>(mintercept);
        demo = new Demo(mintercept, this);
        draw(demo);
        setOverlay(new Overlay(mintercept, this));
    }
    
    @Override
    public void reset() {
    	
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_DPAD_UP ) {
            overlay.deactivate();
            mintercept.get().startGame();
            return true;
        } else if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            toggleOverlay();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( !overlay.onTouchEvent(event) ) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            	 int top = demo.getTournamentsTop();
            	 if (event.getY() > top && event.getY() < top + 48) {
            		 Skillz.startSkillzActivity(mintercept.get());
            	 } else {
            	     mintercept.get().startGame();
            	 }
                 return true;
            } else {
                return false;
            }
        } else
            // The overlay handled the event
            return true;
    }
}

package com.skillzgames.mintercept;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.skillzgames.mintercept.common.Sounds;
import com.skillzgames.mintercept.common.Vibrator;
import com.skillzgames.mintercept.views.Level;
import com.skillzgames.mintercept.views.Scene;
import com.skillzgames.mintercept.views.Title;

public class MIntercept extends Activity {
	
	public boolean skillzGame;
	public boolean gameStarted;
	
    private Handler handler = new Handler();
    private Runnable runner = new Runnable() {
        public void run() {
            view.invalidate();
            handler.postDelayed(runner, 50);
        }
    };

    Title title;
    Level level;
    View view;

    public Vibrator vibrator;
    public Sounds sounds;

    public MIntercept() {
        vibrator = new Vibrator(this);
        sounds = new Sounds(this);
    }

    public void startGame() {
    	gameStarted = true;
        sounds.play(R.raw.blip);
        setView(level);
    }
    
    public void endGame() {
        setView(title);
    }

    private void setView(Scene scene) {
        scene.reset();
        if (scene == level) {
        	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        	} else {
        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        	}
        } else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        view = scene;
        setContentView(scene);
        view.requestFocus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        title = new Title(this);
        level = new Level(this);
        // Load the sounds
        sounds.load(R.raw.blip); // This first sound is used for the toggle noise
        sounds.load(R.raw.city_destroyed);
        sounds.load(R.raw.missile_destroyed);
        sounds.load(R.raw.missile_launch);
        sounds.load(R.raw.player_error);
        sounds.load(R.raw.player_launch);
        // Make full screen without title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set the view to title, but don't use the setView API for this as it does too much
        setView(title);
    }
    
    @Override
    public void onNewIntent(Intent intent) {
    	setIntent(intent);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runner);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runner);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
}

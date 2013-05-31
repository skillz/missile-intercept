package com.skillzgames.mintercept;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.skillz.android.client.BasicGameBroadcastReceiver;
import com.skillz.android.client.Skillz;
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
        
        //Skillz Integration - Init
        Skillz.init(this);
        
        //Skillz Integration - GCM
        if (!Skillz.isGCMRegistered(this)) {
        	Skillz.registerGCM(this);
        }
        
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
        
        handleSkillz();
    }
    
    @Override
    public void onNewIntent(Intent intent) {
    	setIntent(intent);
    	
    	handleSkillz();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runner);
        Skillz.onResume(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runner);
        Skillz.onPause(this);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		//Skillz Integration - Abort Game
    		if (gameStarted && skillzGame) {
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setTitle("Are you sure?");
    			builder.setMessage("You will forfeit if you leave this screen");
    			builder.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Skillz.abortGame(MIntercept.this);
						finish();
					}
				});
    			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
        		builder.show();
        		return false;
        	} else {
        		return super.onKeyDown(keyCode, event);
        	}
    	}

    	return super.onKeyDown(keyCode, event);
    }
    
    /**
     * Skillz Integration - Start a tournament game
     */
    private void handleSkillz() {
    	if (getIntent().getBooleanExtra(BasicGameBroadcastReceiver.START_GAME, false)) {
    		HashMap<String, String> gameParams = (HashMap<String, String>) getIntent().getSerializableExtra(BasicGameBroadcastReceiver.GAME_RULES);
    		
        	skillzGame = true;
        	startGame();
        }
    }
}

package com.skillzgames.mintercept.common;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


public class Sounds implements Setting {
    private class Effect {
        private SoundPool pool;
        private int soundid;
        private int streamid;
        public Effect(Context context, SoundPool pool, int resid) {
            this.pool = pool;
            soundid = pool.load(context, resid, 1);
            streamid = 0;
        }
        void play(int loop) {
            if ( streamid > 0 )
                pool.stop(streamid);
            streamid = pool.play(soundid, 1f, 1f, 0, loop, 1f);
        }
    };
    private Context context;
    private SoundPool pool;
    private boolean on;
    private int toggle;
    private HashMap<Integer, Effect> sounds = new HashMap<Integer, Effect>();

    public Sounds(Context contact) {
        this.context = contact;
        pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        toggle = 0;
        set(true);
    }

    /**
     * Load a sound at the default priority
     */
    public void load(int resid) {
        sounds.put(resid, new Effect(context, pool, resid));
        if ( toggle == 0 )
            toggle = resid;
    }

    public void play(int resid) {
        play(resid, 0);
    }
    public void play(int resid, int loop) {
        if ( on && sounds.containsKey(resid) )
            sounds.get(resid).play(loop);
    }

    @Override
    public boolean get() {
        return on;
    }

    @Override
    public void set(boolean newValue) {
        on = newValue;
    }

    @Override
    public boolean toggle() {
        on = !on;
        play(toggle);
        return on;
    }
}

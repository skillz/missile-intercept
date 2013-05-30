package com.skillzgames.mintercept.common;

import android.content.Context;


public class Vibrator implements Setting {
    private Context context;
    private boolean active;
    private android.os.Vibrator vibrator;

    public Vibrator(Context c) {
        context = c;
        set(true);
    }

    public boolean vibrate(int length) {
        if ( vibrator == null )
            vibrator = (android.os.Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        if ( active && vibrator != null ) {
            vibrator.vibrate(length);
            return true;
        } else
            return false;
    }

    @Override
    public boolean get() {
        return active;
    }
    @Override
    public void set(boolean newValue) {
        active = newValue;
    }
    @Override
    public boolean toggle() {
        active = !active;
        vibrate(80);
        return active;
    }
}

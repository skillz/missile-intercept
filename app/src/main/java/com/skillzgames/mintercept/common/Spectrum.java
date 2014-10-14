package com.skillzgames.mintercept.common;

import android.graphics.Color;


/**
   This class handles moving through a colour spectrum.
   All movement is along the hue axis of the colour space.
*/
public final class Spectrum {
    float [] hls = new float [3];
    
    /**
        The only constructor
        This is the only constructor available. The parameter values are
        for the hue, luminance and saturation of the required colour. These
        values must all be between 0. and 1.
    */
    public Spectrum( float ch, float cl, float cs ) {
        hls[0] = ch;
        hls[1] = cl;
        hls[2] = cs;
    }

    /**
        Return next colour.
        The parameter passed in specifies how much of the spectrum should
        be stepped. For example a value of 100 would move one hundredth of
        the way through the spectrum and 20 would move one twentieth through
        the spectrum.
        It is only ever the hue that is traversed.
    */
    public int next( int steps ) {
        hls[0] += 360.0f / steps;
        hls[0] %= 360.0f;
        return Color.HSVToColor( hls );
    }
}



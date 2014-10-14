package com.skillzgames.mintercept;

import android.graphics.Canvas;

import com.skillzgames.mintercept.common.Element;


public class Explosions extends Element {
    private Explosion[] explosions;

    public Explosions(int number, int size) {
        explosions = new Explosion [number];
        for ( int i = 0; i != explosions.length; ++i )
            explosions[i] = new Explosion(size, Layer.EXPLOSIONS);
    }

    public Explosions(Game game, int number, int size) {
        explosions = new Explosion [number];
        for ( int i = 0; i != explosions.length; ++i )
            explosions[i] = new Explosion(game, size, Layer.EXPLOSIONS);
    }
    
    public Explosion reset(float x, float y) {
        if ( explosions[0].reset(x, y) ) {
            Explosion e = explosions[0];
            for ( int i = 0; i != explosions.length-1; ++i )
                explosions[i] = explosions[i+1];
            explosions[explosions.length-1] = e;
            return e;
        } else
            return null;
    }

    @Override
    public boolean tick() {
        for ( Explosion explosion : explosions )
            explosion.tick();
        return true;
    }

    @Override
    public void draw(Canvas c, Layer layer) {
        for ( Explosion explosion : explosions )
            explosion.draw(c, layer);
    }
}

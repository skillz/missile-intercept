package com.skillzgames.mintercept;

import android.graphics.Canvas;
import android.view.View;

import com.skillzgames.mintercept.common.Element;


public class Player extends Element {
    private static final int BASE_EXPLOSION_SIZE = 35;
    private MIntercept context;
    private View view;
    private int hitbonus;
    private Game game;
    private Explosions shots;
    private Cities cities;

    public Player(MIntercept context, View view, Game game) {
        this.context = context;
        this.view = view;
        this.game = game;
        shots = new Explosions(game, 10, 35);
        cities = new Cities(game, context, view);
        reset();
    }

    public void reset() {
        cities.reset();
    }

    public City struckCity(float x, float y) {
        return cities.hasStruck(x, y);
    }

    /**
     * The player has exploded a missile.
     */
    public void hit() {
        if ( !game.isOver() ) {
            context.vibrator.vibrate(40 * hitbonus);
            game.award(5 * game.level.getValue() * hitbonus++);
        }
    }

    public boolean tap(float x, float y) {
        int height = view.getHeight();
        if ( !game.isOver() && y < height - 40 ) {
            Explosion explosion = shots.reset(x, y);
            if ( explosion != null ) {
                int scale_height = height * 2 / 3 - 40;
                if ( y > view.getHeight() * 2 / 3 )
                    explosion.setSize(BASE_EXPLOSION_SIZE - ((int)y - scale_height) * BASE_EXPLOSION_SIZE / (scale_height));
                else
                    explosion.setSize(BASE_EXPLOSION_SIZE);
                hitbonus = 1;
                game.award(-1);
                context.sounds.play(R.raw.player_launch);
                return true;
            } else
                context.sounds.play(R.raw.player_error);
        }
        return false;
    }

    @Override
    public boolean tick() {
        if ( cities.tick() )
            game.over();
        shots.tick();
        return !game.isOver();
    }

    @Override
    public void draw(Canvas c, Layer layer) {
        cities.draw(c, layer);
        shots.draw(c, layer);
    }
}

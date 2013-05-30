package com.skillzgames.mintercept;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.skillzgames.mintercept.common.Element;
import com.skillzgames.mintercept.common.NumberPanel;

public class Game extends Element {
    private MIntercept context;
    private View view;

    private boolean isover;
    private Player player;
    private Opponent opponent;
    private LinkedList<Explosion> explosions = new LinkedList<Explosion>();

    private NumberPanel score;
    public NumberPanel level, missiles;

    private BitmapDrawable gameover;
    private Rect location = new Rect();
    private Explosion bigbang;

    public Game(MIntercept context, View view) {
    	
        this.context = context;
        this.view = view;

        score = new NumberPanel(context, 8, R.drawable.score_prolog, R.drawable.score_numbers, Layer.CHROME);
        missiles = new NumberPanel(context, 6, R.drawable.missiles_prolog, R.drawable.missiles_numbers, Layer.CHROME);

        int prolog = Math.max(score.getPrologHeight(), missiles.getPrologHeight());
        score.setNumberOffset(prolog);
        missiles.setNumberOffset(prolog);

        level = new NumberPanel(context, 4, R.drawable.level_prolog, R.drawable.level_numbers, Layer.BACKGROUND);

        player = new Player(context, view, this);
        opponent = new Opponent(context, view, this);

        gameover = (BitmapDrawable)context.getResources().getDrawable(R.drawable.gameover);
        bigbang = new Explosion(120, Layer.CHROME);
    }

    public void reset() {
        isover = false;
        score.reset(10);
        level.reset(1);
        player.reset();
        opponent.reset();
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Register an explosion with the game
     */
    public void explosion(Explosion e) {
        explosions.add(e);
    }
    /**
     * Return true if the location is inside an explosion
     */
    public boolean inExplosion(float x, float y) {
        for ( Explosion e : explosions )
            if ( e.inside(x, y) )
                return true;
        return false;
    }
    /**
     * When called the game is over
     */
    public void over() {
    	context.gameStarted = false;
        isover = true;
    }
    /**
     * Allows us to determine if the game is over.
     */
    public boolean isOver() {
        return isover;
    }
    /**
     * Award (or subtract) points from the player
     */
    public boolean award(int points) {
        if ( !isOver() && score.alter(points) <= 0 )
            over();
        
        return isOver();
    }

    @Override
    public boolean tick() {
        if ( level.alpha > 0 )
            --level.alpha;
        boolean opponent_running = opponent.tick();
        if ( !opponent_running ) {
            if ( isOver() ) {
                if ( bigbang.reset(view.getWidth()/2, view.getHeight()/2) )
                    context.sounds.play(R.raw.city_destroyed);
            } else {
                level.alpha = 255;
                level.alter(1);
                opponent.reset();
            }
        }
        player.tick();
        return !isOver() || opponent_running || bigbang.tick();
    }

    @Override
    public void draw(Canvas c, Layer layer) {
        if ( isover && !bigbang.pastZenith() && layer == Layer.BACKGROUND ) {
            location.left = view.getWidth() / 2 - gameover.getMinimumWidth() / 2;
            location.top = view.getHeight() / 2 - gameover.getMinimumHeight();
            location.right = view.getWidth() / 2 + gameover.getMinimumWidth() / 2;
            location.bottom = view.getHeight() / 2;
            gameover.setBounds(location);
            gameover.draw(c);
        }

        score.draw(c, layer);
        missiles.setLeft(view.getWidth() - missiles.getWidth());
        missiles.draw(c, layer);
        level.setLeft((view.getWidth() - level.getWidth()) / 2);
        level.setTop(view.getHeight() / 3);
        level.draw(c, layer);

        opponent.draw(c, layer);
        player.draw(c, layer);

        bigbang.draw(c, layer);
    }
}

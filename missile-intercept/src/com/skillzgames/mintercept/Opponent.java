package com.skillzgames.mintercept;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.skillzgames.mintercept.common.Element;


public class Opponent extends Element {
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Random random = new Random();

    private class Missile extends Element {
        private MIntercept context;
        private Opponent opponent;
        private Game game;
        private View view;
        private boolean inuse, exploding;
        private int fade;
        private float sx, sy, cx, cy, dx, dy, tx;
        private Explosion explosion;

        public Missile(Opponent opponent, Game game, MIntercept context, View view) {
            this.context = context;
            this.opponent = opponent;
            this.game = game;
            this.view = view;
            inuse = false; exploding = false;
            dx = 0; dy = 0;
            explosion = new Explosion(game, 10, Layer.MISSILES);
        }

        public boolean reset() {
            if ( !inuse ) {
                fade = 8;
                dy = 0; dx = 0;
                inuse = true;
                exploding = false;
                return true;
            }
            return false;
        }

        @Override
        public boolean tick() {
            if ( inuse ) {
                if ( dy == 0 && !exploding ) {
                    dx = 0; dy = 2f + game.level.getValue() / 5f;
                    sx = random.nextInt(view.getWidth());
                    sy = game.missiles.getTotalHeight() + 1;
                    if ( random.nextInt(6) == 1 ) {
                        for ( Missile m : opponent.getMissiles() )
                            if ( m != this && m.inuse && !m.exploding ) {
                                sx = m.cx; sy = m.cy;
                                dy *= 1.5;
                                break;
                            }
                    }
                    cx = sx; cy = sy;
                    tx = random.nextInt(view.getWidth());
                    dx = dy * ( tx - sx ) / ( view.getHeight() - sy );
                    context.sounds.play(R.raw.missile_launch);
                }
                cx += dx; cy += dy;
                if ( !exploding && cy >= view.getHeight() - 20 ) {
                    City struck = game.getPlayer().struckCity(cx, cy);
                    if ( struck != null ) {
                        exploding = true;
                        explosion.reset(cx, cy);
                    } else if ( cy >= view.getHeight() - 3 ) {
                        dy = 0; dx = 0;
                        exploding = true;
                        explosion.reset(cx, cy);
                        game.award(-3 * game.level.getValue());
                        context.sounds.play(R.raw.missile_destroyed);
                    }
                } else if ( !exploding && game.inExplosion(cx, cy) ) {
                    exploding = true;
                    explosion.reset(cx, cy);
                    game.getPlayer().hit();
                    context.sounds.play(R.raw.missile_destroyed);
                }
                if ( exploding )
                    inuse = explosion.tick();
            }
            return inuse;
        }

        @Override
        public void draw(Canvas c, Layer layer) {
            if ( inuse ) {
                if (layer == Layer.TRAILS) {
                    if ( !exploding || fade-- == 8 ) {
                        paint.setColor(0xff808080);
                        c.drawLine(sx, sy, cx, cy, paint);
                    } else if ( fade > 0 )  {
                        paint.setColor(0x808080 + fade * 0x02000000 + fade * 0x20000000);
                        c.drawLine(sx, sy, cx, cy, paint);
                        --fade;
                    }
                } else if (layer == Layer.MISSILES && !exploding) {
                    paint.setColor(0xffffffff);
                    c.drawCircle(cx, cy, 2, paint);
                }
                explosion.draw(c, layer);
            }
        }
    };

    private Game game;
    private Missile [] missiles;
    private int timer;

    public Opponent(MIntercept context, View view, Game game) {
        this.game = game;
        missiles = new Missile [10];
        for ( int i = 0; i != missiles.length; ++i )
            missiles[i] = new Missile(this, game, context, view);
        reset();
    }

    public Missile [] getMissiles() {
        return missiles;
    }

    public void reset() {
        timer = 5;
        game.missiles.reset(2 * game.level.getValue() + 3);
    }

    @Override
    public boolean tick() {
        boolean inplay = game.missiles.getValue() > 0;
        if ( timer <= 0 && inplay ) {
            for ( Missile m : missiles )
                if ( m.reset() ) {
                    game.missiles.alter(-1);
                    timer = random.nextInt(Math.max(80 - 8 * game.level.getValue(), 5)) + 2;
                    break;
                }
        } else
            --timer;
        for ( Missile missile : missiles )
            if ( missile.tick() )
                inplay = true;
        return inplay;
    }

    @Override
    public void draw(Canvas c, Layer layer) {
        for ( Missile m : missiles )
            m.draw(c, layer);
    }
}

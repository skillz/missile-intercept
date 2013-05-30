package com.skillzgames.mintercept;

import android.graphics.Canvas;
import android.view.View;

import com.skillzgames.mintercept.common.Element;


public class Cities extends Element {
    private MIntercept context;
    private City [] cities;
    private Explosions explosions;

    public Cities(Game game, MIntercept c, View view) {
        context = c;
        cities = new City [3];
        for ( int n = 0; n != cities.length; ++n )
            cities[n] = new City(game, context, view, n, cities.length);
        explosions = new Explosions(game, cities.length, 0);
    }

    public void reset() {
        for ( City c : cities )
            c.reset();
    }

    public City hasStruck(float x, float y) {
        for ( City c : cities )
            if ( c.hasStruck(x) ) {
                c.explode(explosions.reset(x, y));
                context.sounds.play(R.raw.city_destroyed);
                return c;
            }
        return null;
    }

    @Override
    public boolean tick() {
        boolean alldead = true;
        for ( City city : cities )
            if ( city.tick() )
                alldead = false;
        explosions.tick();
        return alldead;
    }

    @Override
    public void draw(Canvas c, Layer layer) {
        for ( City city : cities )
            city.draw(c, layer);
        explosions.draw(c, layer);
    }
}

package com.krecior.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krecior.game.GameScreen;
import com.krecior.game.objects.effect.Effect;

import java.util.concurrent.CopyOnWriteArrayList;

public class EffectSystem {
    //===========================================================
    //Constants
    //===========================================================



    //===========================================================
    //Fields
    //===========================================================

    private CopyOnWriteArrayList<Effect> effects;

    private SpriteBatch spriteBatch;

    private GameScreen game;

    //===========================================================
    //Constructors
    //===========================================================

    public EffectSystem(GameScreen mGame) {
        game = mGame;

        create();
    }

    //===========================================================
    //Getter & Setter
    //===========================================================



    //===========================================================
    //Methods for/from SuperClass/Interfaces
    //===========================================================



    //===========================================================
    //Methods
    //===========================================================

    public void add(Effect effect) {
        effects.add(effect);
    }

    public void remove(Effect effect) {
        effects.remove(effect);
    }

    private void create() {
        spriteBatch = new SpriteBatch();

        effects = new CopyOnWriteArrayList<Effect>();
    }

    public void render() {
        spriteBatch.begin();

        for(Effect e : effects)
            e.draw(spriteBatch);

        spriteBatch.end();
    }

    //===========================================================
    //Inner and Anonymous Classes
    //===========================================================
}

package com.krecior.game.systems;

import com.krecior.game.GameScreen;
import com.krecior.game.objects.RubberLine;

import java.util.concurrent.CopyOnWriteArrayList;

public class RubberSystem {

    public CopyOnWriteArrayList<RubberLine> lines;

    private GameScreen game;

    public RubberSystem(GameScreen mGame) {
        game = mGame;
        lines = new CopyOnWriteArrayList<RubberLine>();
    }

    public void onTouchDown() {
        RubberLine r = new RubberLine(this);
        r.start();
        lines.add(r);
    }

    public void onTouchUp() {
        for(RubberLine r : lines)
            r.stop();
    }

    public void draw() {
        for(RubberLine r : lines)
            r.draw();
    }
}

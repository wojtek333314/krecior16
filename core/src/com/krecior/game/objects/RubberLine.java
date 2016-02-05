package com.krecior.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.krecior.game.GameScreen;
import com.krecior.game.systems.RubberSystem;

import java.util.concurrent.CopyOnWriteArrayList;

public class RubberLine {
    public static final float THICKNESS = 0.04f * GameScreen.W;

    private RubberSystem system;

    private ShapeRenderer shape;

    private CopyOnWriteArrayList<Vector2> pos;

    private float time = 0f;

    private boolean drawing = false;

    public RubberLine(RubberSystem system) {
        this.system = system;

        pos = new CopyOnWriteArrayList<Vector2>();
        shape = new ShapeRenderer();
    }

    public void start() {
        drawing = true;
    }

    public void draw() {
        time += GameScreen.TIME_STEP;

        if(drawing) {
            pos.add(new Vector2(GameScreen.pGame.getTouchProcessor().getTouchX(),
                    GameScreen.H - GameScreen.pGame.getTouchProcessor().getTouchY()));
        }

        for (int i = 1; i < pos.size(); i++) {
            shape.setColor(Color.WHITE);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.rectLine(pos.get(i - 1), pos.get(i), THICKNESS);
            shape.end();

            shape.setColor(Color.WHITE);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.circle(pos.get(i).x, pos.get(i).y, THICKNESS / 2);
            shape.end();
        }

        if(time >= 0.25f / pos.size() && pos.size() >= 1) {
            time = 0f;

            pos.remove(0);
        }

        if(pos.size() < 1) {
            system.lines.remove(this);
        }
    }

    public void stop() {
        drawing = false;
    }
}

package com.krecior.utils;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.Manager;
import com.krecior.game.GameScreen;

import jdk.nashorn.internal.ir.ContinueNode;

public class MsgBox extends Group {
    public static final float WIDTH = 0.6f * GameScreen.W;
    public static final float HEIGHT = WIDTH / 2;

    private String description;

    private TextLabel textLabel;
    private Image label;

    public MsgBox(String description) {
        this.description = description;

        create();
    }

    private void create() {
        label = new Image(Container.getTextureRegion("gfx/msgBox/score_label.png"));
        label.setSize(WIDTH, HEIGHT);
        label.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onTouchDown();
                hide();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        addActor(label);

        textLabel = new TextLabel(Container.getFont(10), description);
        addActor(textLabel);
    }

    public void setPosition(float x, float y) {
        label.setPosition(x, y);
        textLabel.setPosition(label.getX() + label.getWidth() / 2 - textLabel.getWidth() / 2
                , label.getY() + label.getHeight() / 2 + textLabel.getHeight() / 2);
    }

    public void show() {
        setPosition(GameScreen.W / 2 - label.getWidth() / 2
                , GameScreen.H / 2 - label.getHeight() / 2);
    }

    public void hide() {
        setPosition(GameScreen.W, GameScreen.H);
    }

    public void onTouchDown() { }
}

package com.krecior.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.game.GameScreen;
import com.krecior.utils.Container;

import java.util.ArrayList;

public class DMHearts extends Group {
    public static final int HEARTS = 20;
    public static final float SIZE = 0.066f * GameScreen.W;
    public static final float WALL_GAP = 0.02f * GameScreen.W;
    public static final float TOP_Y = 0.6f * GameScreen.H - SIZE;

    private GameScreen gameScreen;
    private ArrayList<Image> heart;

    public DMHearts(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        create();
    }

    private void create() {
        heart = new ArrayList<Image>();

        for (int i = 0; i < HEARTS; i++) {
            Image image = new Image(Container.heart);
            image.setSize(SIZE, SIZE);
            if (i < HEARTS / 2) image.setPosition(WALL_GAP, -image.getHeight() * i);
            else image.setPosition(GameScreen.W - WALL_GAP - image.getWidth()
                    , -image.getHeight() * (i - HEARTS / 2));
            addActor(image);
            heart.add(image);
        }
    }

    public void refresh(float lives) {
        if(lives >= 0) {
            int integer = (int) lives;
            for (int i = 0; i < integer; i++) {
                Color c = heart.get(i).getColor();
                c.a = 1f;
                heart.get(i).setColor(c);
            }

            for (int i = integer; i < HEARTS; i++) {
                Color c = heart.get(i).getColor();
                float l = lives - i;
                if (l <= 0) c.a = 0;
                else c.a = l;
                heart.get(i).setColor(c);
            }
        } else {
            for (int i = 0; i < HEARTS; i++) {
                Color c = heart.get(i).getColor();
                c.a = 0;
                heart.get(i).setColor(c);
            }
        }
    }
}

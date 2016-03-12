package com.krecior.menu.ranking;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.Manager;
import com.krecior.game.GameScreen;
import com.krecior.menu.ScreenType;
import com.krecior.utils.Container;
import com.krecior.utils.TextLabel;

public class Rank extends Group {
    public static float SIZE = 0.95f * GameScreen.H;
    public static float ENTER_W = 0.55f * GameScreen.W;
    public static float ENTER_H = 0.1f * GameScreen.H;

    private GameScreen gameScreen;

    private Image ball;
    private Image enterLabel;

    private TextLabel enter;
    private TextLabel rank;

    public Rank(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        create();
    }

    private void create() {
        ball = new Image(Container.pEmptyButton);
        ball.setSize(SIZE, SIZE);
        ball.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
                return false;
            }
        });
        addActor(ball);

        enterLabel = new Image(Container.enter_label);
        enterLabel.setSize(ENTER_W, ENTER_H);
        enterLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO wprowadzanie imienia.
                return false;
            }
        });
        addActor(enterLabel);

        enter = new TextLabel(Container.getFont(10), "ENTER");
        addActor(enter);

        rank = new TextLabel(Container.getFont(10), "RANK:");
        addActor(rank);
    }

    public void popUp() { setPosition(GameScreen.W / 2 - SIZE / 2, GameScreen.H / 2 - SIZE / 2); }

    public void hide() { setPosition(GameScreen.W, GameScreen.H); }

    public void setPosition(float x, float y) {
        ball.setPosition(x, y);
        enterLabel.setPosition(ball.getX() + (ball.getWidth() - enterLabel.getWidth()) / 2
            , ball.getY() + ball.getHeight() / 2 - 0.39f * ball.getHeight());
        enter.setPosition(ball.getX() + (ball.getWidth() - enter.getWidth()) / 2
            , enterLabel.getY() + (enterLabel.getHeight() + enter.getHeight()) / 2);
        rank.setPosition(ball.getX() + (ball.getWidth() - rank.getWidth()) / 2
            , ball.getY() + ball.getHeight() * 0.88f);
    }
}

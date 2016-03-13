package com.krecior.game.hud;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.Manager;
import com.krecior.game.GameScreen;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.ServerRequestListener;
import com.krecior.utils.TextLabel;

public class DMEnd extends Group{
    private GameScreen gameScreen;
    private final float SIZE = 0.72f * GameScreen.W;
    private final float BUTTON_SIZE = 0.2f * SIZE;
    private final float BUTTON_ON_BALL_RADIUS = 0.6f * SIZE / 2;
    private final float ANGLE = 0.5f;

    private Image ball;
    private Image replay;
    private Image rank;

    private TextLabel score;
    private TextLabel bestScore;
    private TextLabel scoreValue;
    private TextLabel bestScoreValue;

    private int scores;

    public DMEnd(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        create();
    }

    private void create() {
        ball = new Image(Container.pEmptyButton);
        ball.setSize(SIZE, SIZE);
        ball.setPosition(0, 0);
        addActor(ball);

        rank = new Image(Container.top_position);
        rank.setSize(BUTTON_SIZE, BUTTON_SIZE);
        rank.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Manager.rankingFacade.isOnline()) {
                    gameScreen.getHud().popUpRank();
                    hide();
                } else {
                    gameScreen.getHud().popUpNotInternetAccess();
                }
                return false;
            }
        });
        addActor(rank);

        replay = new Image(Container.pRepeat);
        replay.setSize(BUTTON_SIZE, BUTTON_SIZE);
        replay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.repeatLevel();
                return false;
            }
        });
        addActor(replay);

        score = new TextLabel(Container.getFont(13), "SCORE");
        addActor(score);

        scoreValue = new TextLabel(Container.getFont(13), Integer.toString(scores));
        addActor(scoreValue);

        bestScore = new TextLabel(Container.getFont(13), "BEST SCORE");
        addActor(bestScore);

        bestScoreValue = new TextLabel(Container.getFont(13), Integer.toString(Data.getBestGame()));
        addActor(bestScoreValue);
    }

    public void popUp(int scores) {
        this.scores = scores;
        setScoreValue(scores);
        if(scores > Data.getBestGame()) Data.setBestGame(scores);
        setBestScoreValue(Data.getBestGame());
        setPosition(GameScreen.W / 2 - SIZE / 2, GameScreen.H / 2 - SIZE / 2);
    }

    public void hide() {
        setPosition(GameScreen.W, GameScreen.H);
    }

    public void setScoreValue(int score) {
        scoreValue.setText(Integer.toString(scores));
    }

    public void setBestScoreValue(int score) {
        bestScoreValue.setText(Integer.toString(score));
    }

    public void setPosition(float x, float y) {
        ball.setPosition(x, y);
        rank.setPosition(x + SIZE / 2 - rank.getWidth() / 2
                + BUTTON_ON_BALL_RADIUS * (float)Math.cos(-Math.PI / 2 + ANGLE)
                , y + SIZE / 2 - rank.getHeight() / 2
                + BUTTON_ON_BALL_RADIUS * (float)Math.sin(-Math.PI / 2 + ANGLE));
        replay.setPosition(x + SIZE / 2 - rank.getWidth() / 2
                + BUTTON_ON_BALL_RADIUS * (float)Math.cos(-Math.PI / 2 - ANGLE)
                , y + SIZE / 2 - rank.getHeight() / 2
                + BUTTON_ON_BALL_RADIUS * (float)Math.sin(-Math.PI / 2 - ANGLE));
        score.setPosition(ball.getX() + SIZE / 2 - score.getWidth() / 2
                , 0.65f * GameScreen.H);
        scoreValue.setPosition(ball.getX() + SIZE / 2 - scoreValue.getWidth() / 2
                , score.getY() - scoreValue.getHeight() - 0.01f * GameScreen.H);
        bestScore.setPosition(ball.getX() + SIZE / 2 - bestScore.getWidth() / 2
                , scoreValue.getY() - bestScore.getHeight() - 0.01f * GameScreen.H);
        bestScoreValue.setPosition(ball.getX() + SIZE / 2 - bestScoreValue.getWidth() / 2
                , bestScore.getY() - bestScoreValue.getHeight() - 0.01f * GameScreen.H);

    }
}

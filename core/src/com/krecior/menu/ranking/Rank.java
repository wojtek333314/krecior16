package com.krecior.menu.ranking;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.Manager;
import com.krecior.game.GameScreen;
import com.krecior.menu.ScreenType;
import com.krecior.utils.Container;
import com.krecior.utils.ServerRequestListener;
import com.krecior.utils.TextLabel;

import java.lang.annotation.ElementType;
import java.util.ArrayList;

public class Rank extends Group {
    public static float SIZE = 0.95f * GameScreen.H;
    public static float ENTER_W = 0.55f * GameScreen.W;
    public static float ENTER_H = 0.1f * GameScreen.H;
    public static float WALL_GAP = 0.16f * GameScreen.W;

    public String name = "d ";

    private GameScreen gameScreen;
    private Image ball;
    private Image enterLabel;
    private TextLabel enter;
    private TextLabel rank;
    private TextInput listener;

    private ArrayList<RankElement> rankElements;

    public Rank(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        listener = new TextInput();
        rankElements = new ArrayList<RankElement>();

        create();
        createRankElements();
    }

    private void getRanking(){
        Manager.rankingFacade.getPlayersRankingDependsOnNick("wojtaus", 10, new ServerRequestListener() {
            @Override
            public void onSuccess(String json) {
                System.out.println(json);//wyswietlam odpowiedz serwera
            }

            @Override
            public void onError(int code, String description) {
                System.out.println("kurwa blad o kodzie:" + code + "/info:" + description);
            }
        });
    }

    private void createRankElements() {
        for(int i = 0; i < 11; i++ ) {
            RankElement element = new RankElement(i+1, "gracz", 10 * (11-i));
            element.setPosition(0, GameScreen.H / 2);
            addActor(element);
            rankElements.add(element);
        }
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
                Gdx.input.getTextInput(listener, "What is your name?", "Name", " ");
                return false;
            }
        });
        addActor(enterLabel);

        enter = new TextLabel(Container.getFont(10), "ENTER");
        addActor(enter);

        rank = new TextLabel(Container.getFont(20), "RANK:");
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

        for(int i = 0; i < rankElements.size(); i++) {
            rankElements.get(i).setPosition(WALL_GAP, rank.getY() - rank.getHeight()
                    - (rankElements.get(i).getHeight() + 0.01f * GameScreen.H) * (1 + i));
        }
    }

    private class TextInput implements Input.TextInputListener {
        @Override
        public void input (String text) {
            name = text;
        }

        @Override
        public void canceled () {
        }
    }

    private class RankElement extends Actor {
        private final float WIDTH = 0.68f * GameScreen.W;

        public String name;
        public int score;

        private TextLabel nameText;
        private TextLabel scoreText;
        private int index;

        public RankElement(int index, String name, int score) {
            this.index = index;
            this.name = name;
            this.score = score;

            create();
        }

        private void create() {
            nameText = new TextLabel(Container.getFont(13), Integer.toString(index) + ". " + name);
            addActor(nameText);

            scoreText = new TextLabel(Container.getFont(13), Integer.toString(score));
            addActor(scoreText);
        }

        public void setPosition(float x, float y) {
            nameText.setPosition(x, y);
            scoreText.setPosition(x + WIDTH - scoreText.getWidth(), y);
        }

        public float getHeight() { return nameText.getHeight(); }
    }
}

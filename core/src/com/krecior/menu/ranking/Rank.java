package com.krecior.menu.ranking;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Rank extends Group {
    public static final float SIZE = 0.95f * GameScreen.H;
    public static final float ENTER_W = 0.55f * GameScreen.W;
    public static final float ENTER_H = 0.1f * GameScreen.H;
    public static final float WALL_GAP = 0.16f * GameScreen.W;
    public static final int LENGTH = 11;

    public String name = "d ";

    private GameScreen gameScreen;
    private Image ball;
    private Image enterLabel;
    private TextLabel enter;
    private TextLabel rank;
    private TextInput listener;

    private ArrayList<RankElement> rankElements;

    private JSONObject jsonObject;
    private boolean first = true;

    public Rank(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        listener = new TextInput();
        rankElements = new ArrayList<RankElement>();

        create();
        refreshRanking(gameScreen.getScoreManager().getPoints());
    }

    public void refreshRanking(int k) {
        Manager.rankingFacade.getRanking(k, new ServerRequestListener() {
            @Override
            public void onSuccess(String JSON) {
                jsonObject = new JSONObject(JSON);
                createRankElements();
            }

            @Override
            public void onError(int code, String description) {

            }
        });
    }

    private void createRankElements() {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for(int i = 0; i < LENGTH; i++ ) {
            JSONObject object = jsonArray.getJSONObject(i);
            RankElement element = new RankElement(i, object);
            element.setPosition(GameScreen.W, GameScreen.H);
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
                if(first) {
                    Gdx.input.getTextInput(listener, "What is your name?", "Name", " ");
                    first = false;
                }
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
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject object = jsonArray.getJSONObject(LENGTH-1);
            object.put("nick", name);
            object.put("points", gameScreen.getScoreManager().getPoints());
            rankElements.get(LENGTH-1).jsonObject = object;
            rankElements.get(LENGTH-1).refreshText();


            Manager.rankingFacade.registerPoints(name
                    , gameScreen.getScoreManager().getPoints(), new ServerRequestListener() {
                @Override
                public void onSuccess(String json) {
                    System.out.println("poprawnie");
                }

                @Override
                public void onError(int code, String description) {
                    System.out.println("niepoprawnie");
                }
            });
        }

        @Override
        public void canceled () { }
    }

    private class RankElement extends Actor {
        private final float WIDTH = 0.68f * GameScreen.W;

        private TextLabel nameText;
        private TextLabel scoreText;
        private JSONObject jsonObject;

        public float value;

        public RankElement(int i, JSONObject jsonObject) {
            this.jsonObject = jsonObject;

            create();
        }

        private void create() {
            nameText = new TextLabel(Container.getFont(13),
                    jsonObject.getString("position") + ". " + jsonObject.getString("nick"));
            addActor(nameText);

            scoreText = new TextLabel(Container.getFont(13), jsonObject.getString("points"));
            addActor(scoreText);
        }

        public void setPosition(float x, float y) {
            nameText.setPosition(x, y);
            scoreText.setPosition(x + WIDTH - scoreText.getWidth(), y);
        }

        public void refreshText() {
            value = Integer.parseInt(jsonObject.getString("points"));
            nameText.setText(jsonObject.getString("position") + ". " + jsonObject.getString("nick"));
            scoreText.setText(jsonObject.getString("points"));
        }

        public float getHeight() { return nameText.getHeight(); }
    }
}

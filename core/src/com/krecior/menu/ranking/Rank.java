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
import com.krecior.utils.MsgBox;
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
    boolean msgPoped = false;

    public Rank(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        listener = new TextInput();
        rankElements = new ArrayList<RankElement>();
        create();
    }

    public void refreshRanking(int k) {
        Manager.rankingFacade.getRanking(k, new ServerRequestListener() {
            @Override
            public void onSuccess(String JSON) {
                jsonObject = new JSONObject(JSON);
                createRankElements();
                setPosition(GameScreen.W / 2 - SIZE / 2, GameScreen.H / 2 - SIZE / 2);
            }

            @Override
            public void onError(int code, String description) {

            }

            @Override
            public void onConnectionError() {

            }
        });
    }

    private void createRankElements() {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for(int i = 0; i < LENGTH; i++ ) {
            JSONObject object = jsonArray.getJSONObject(i);
            RankElement element = new RankElement(object);
            element.setPosition(GameScreen.W, GameScreen.H);
            addActor(element);

            rankElements.add(element);
        }
    }

    private int getActualPos() {
        int k = 0;
        for(int i = 0; i < LENGTH; i++)
            if(rankElements.get(i).value == gameScreen.getScoreManager().getPoints())
                k = i+1;

        return k;
    }

    private void create() {
        ball = new Image(Container.pEmptyButton);
        ball.setSize(SIZE, SIZE);
        ball.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!msgPoped) Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
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

        enter = new TextLabel(Container.getFont(10), "ENTER NAME");
        addActor(enter);

        rank = new TextLabel(Container.getFont(16), "RANK:");
        addActor(rank);
    }

    public void popUp() {
        setPosition(GameScreen.W / 2 - SIZE / 2, GameScreen.H / 2 - SIZE / 2);
        refreshRanking(gameScreen.getScoreManager().getPoints());
    }

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
            if(text.equals("Enter name")) {
                msgPoped = true;
                MsgBox msgBox = new MsgBox("Invalid name!") {
                    @Override
                    public void onTouchDown() {
                        Gdx.input.getTextInput(listener, "What is your name?", "Name", " ");
                        msgPoped = false;
                        super.onTouchDown();
                    }
                };
                msgBox.setPosition(GameScreen.W / 2 - MsgBox.WIDTH / 2
                        , GameScreen.H / 2 - MsgBox.HEIGHT / 2);
                addActor(msgBox);
            } else if(text.length() > 3) {
                if(name.length() >= 10) name = text.substring(0, 10);

                name = name.replaceAll("'","'");

                for(RankElement r : rankElements)
                    if(r.name.equals("Enter name")) {
                        r.name = name;
                        r.refreshText();
                    }

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

                    @Override
                    public void onConnectionError() {

                    }
                });
                Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
            } else {
                msgPoped = true;
                MsgBox msgBox = new MsgBox("Too short!") {
                    @Override
                    public void onTouchDown() {
                        Gdx.input.getTextInput(listener, "What is your name?", "Name", " ");
                        msgPoped = false;
                        super.onTouchDown();
                    }
                };
                msgBox.setPosition(GameScreen.W / 2 - MsgBox.WIDTH / 2
                        , GameScreen.H / 2 - MsgBox.HEIGHT / 2);
                addActor(msgBox);
            }
        }

        @Override
        public void canceled () { }
    }

    private class RankElement extends Actor {
        private final float WIDTH = 0.68f * GameScreen.W;

        private TextLabel nameText;
        private TextLabel scoreText;
        private JSONObject jsonObject;

        private float x = 0;
        private float y = 0;

        public int position;
        public int value;
        public String name;

        public RankElement(JSONObject jsonObject) {
            this.jsonObject = jsonObject;

            create();
        }

        private void create() {
            nameText = new TextLabel(Container.getFont(13),
                    jsonObject.getString("position") + ". " + jsonObject.getString("nick"));
            addActor(nameText);
            name = jsonObject.getString("nick");
            position = Integer.parseInt(jsonObject.getString("position"));

            scoreText = new TextLabel(Container.getFont(13), jsonObject.getString("points"));
            addActor(scoreText);
            value = Integer.parseInt(jsonObject.getString("points"));
        }

        public void setPosition(float x, float y) {
            this.x = x;
            this.y = y;
            nameText.setPosition(x, y);
            scoreText.setPosition(x + WIDTH - scoreText.getWidth(), y);
        }

        public void refreshText() {
            nameText.setText(position + ". " + name);
            scoreText.setText(Integer.toString(value));
            setPosition(x, y);
        }

        public float getHeight() { return nameText.getHeight(); }
    }
}

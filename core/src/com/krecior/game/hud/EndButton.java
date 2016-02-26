package com.krecior.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.krecior.Manager;
import com.krecior.game.GameScreen;
import com.krecior.menu.ScreenType;
import com.krecior.menu.achievements.system.Achievement;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.TextLabel;

public class EndButton {
    private static final int MAX_DIAMONDS = 3;

    private static final float LABEL_START_SIZE = 0.1f * GameScreen.W;
    private static final float LABEL_END_SIZE = 0.8f * GameScreen.W;
    private static final float DIAMOND_SCALE = 0.35f;
    private static final float DIAMOND_ANGLE = 45f;
    private static final float DELTA_TIME_SCALE = 2f;

    private GameScreen game;

    private SpriteBatch spriteBatch;
    private Stage mainStage = new Stage();
    private TextLabel textLabel;
    private Sprite label;
    private Image play;
    private Image repeatLevel;
    private Image chooseLevel;

    private Sprite[] diamonds;

    private int scoredDiamonds;

    private boolean upScale = false;
    private boolean onRoundEndLaunched;

    public EndButton(GameScreen mGame, int mDiamonds) {
        game = mGame;
        scoredDiamonds = mDiamonds + 1;

        create();
    }

    private void create() {
        spriteBatch = new SpriteBatch();

        label = new Sprite(Container.pEmptyButton);
        label.setSize(LABEL_START_SIZE, LABEL_START_SIZE);
        label.setOriginCenter();
        label.setPosition(GameScreen.W, GameScreen.H);

        textLabel = new TextLabel(TextLabel.Font.ROBOTO,"");
        textLabel.setAlign(Align.center);

        diamonds = new Sprite[MAX_DIAMONDS];

        float radius = LABEL_START_SIZE * 0.35f;
        float actualAngle = (float) (-DIAMOND_ANGLE * Math.PI / 180f);

        for(int i = 0; i < diamonds.length; i++) {
            diamonds[i] = new Sprite(Container.pPowers[0]);
            diamonds[i].setSize(LABEL_START_SIZE * DIAMOND_SCALE, LABEL_START_SIZE * DIAMOND_SCALE);
            diamonds[i].setOriginCenter();
            diamonds[i].setPosition(label.getX() + label.getWidth() / 2 + (float) Math.sin(actualAngle) * radius - diamonds[i].getWidth() / 2,
                    label.getY() + label.getHeight() / 2 + (float) Math.cos(actualAngle) * radius - diamonds[i].getHeight() / 2);
            diamonds[i].setRotation((float) (-actualAngle * 180f / Math.PI));
            actualAngle += (float) (DIAMOND_ANGLE * Math.PI / 180f);
        }

        play = new Image(Container.pButton[0]);
        play.setSize(LABEL_START_SIZE / 5, LABEL_START_SIZE / 5);
        play.setOrigin(play.getWidth() / 2, play.getHeight() / 2);
        play.setPosition(label.getX() + (label.getWidth() - play.getWidth()) / 2 + (label.getWidth() - play.getWidth()) / 3,
                label.getY() + label.getHeight() / 4);

        repeatLevel = new Image(Container.pRepeat);
        repeatLevel.setSize(LABEL_START_SIZE / 5, LABEL_START_SIZE / 5);
        repeatLevel.setOrigin(repeatLevel.getWidth() / 2, repeatLevel.getHeight() / 2);
        repeatLevel.setPosition(label.getX() + (label.getWidth() - repeatLevel.getWidth()) / 2
                , label.getY() + label.getHeight() / 2);

        chooseLevel = new Image(Container.pChoose);
        chooseLevel.setSize(LABEL_START_SIZE / 5, LABEL_START_SIZE / 5);
        chooseLevel.setOrigin(chooseLevel.getWidth() / 2, chooseLevel.getHeight() / 2);
        chooseLevel.setPosition(label.getX() + (label.getWidth() - chooseLevel.getWidth()) / 3, label.getY() + label.getHeight() / 2);

        play.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.nextLevel();
                Manager.getAdMobPlugin().getAdMobPluginListener().showAd();
                return true;
            }
        });

        chooseLevel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Manager.manager.changeScreen(ScreenType.CHOOSE_STAGE);
                Gdx.input.setInputProcessor(Manager.inputMultiplexer);
                return true;
            }
        });

        repeatLevel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.repeatLevel();
                return true;
            }
        });
        mainStage.addActor(repeatLevel);
        mainStage.addActor(chooseLevel);
        mainStage.addActor(play);
        mainStage.addActor(textLabel);
        game.getpInputMultiplexer().addProcessor(mainStage);
    }

    public void sizeUp(int diamonds) {
        scoredDiamonds = diamonds;

        label.setPosition((GameScreen.W - label.getWidth()) / 2,
                (GameScreen.H - label.getWidth()) / 2);

        upScale = true;

        if(label.getWidth() >= LABEL_END_SIZE) {
            upScale = false;
            if(!onRoundEndLaunched) {
                onRoundEnd(scoredDiamonds);
                onRoundEndLaunched = true;
            }
        }
    }

    private void onRoundEnd(int scoredDiamonds){
        Achievement.addValue(Achievement.ACHIEVED_1M_SCORES, game.getScoreManager().getPoints());
        Achievement.addValue(Achievement.COLLECT_DIAMONDS_12K, game.getScoreManager().getDiamonds());
        Achievement.addValue(Achievement.ELIMINATED_MOLES_10K,game.getScoreManager().getDestroyedTargets());
        System.out.println("onRoundEnd:" + game.getScoreManager().getDestroyedTargets());
        System.out.println("onRoundEnd:"+Achievement.getValue(Achievement.ELIMINATED_MOLES_10K));


        Data.rateLevel(game.getMap().getActualLevel(), scoredDiamonds);
        if(scoredDiamonds >= 1) {
            Data.setLevelUnlocked(game.getMap().getActualLevel() + 1);
            Data.addDiamonds(game.getScoreManager().getDiamonds());
            Data.addPoints(game.getScoreManager().getPoints());
            textLabel.setText("Complete!\nNext level!");
        }else{
            textLabel.setText("Too little score!\nTry again!");
        }
    }

    public void draw() {
        spriteBatch.begin();

        if(upScale) {
            float sizeUp = DELTA_TIME_SCALE * GameScreen.TIME_STEP
                    * (LABEL_END_SIZE - LABEL_START_SIZE);

            label.setSize(label.getWidth() + sizeUp, label.getHeight() + sizeUp);
            label.setPosition((GameScreen.W - label.getWidth()) / 2,
                    (GameScreen.H - label.getWidth()) / 2);

            float radius = label.getWidth() * 0.35f;
            float actualAngle = (float) (-DIAMOND_ANGLE * Math.PI / 180f);

            for(int i = 0; i < scoredDiamonds; i++) {
                diamonds[i].setSize(diamonds[i].getWidth() + sizeUp * DIAMOND_SCALE,
                        diamonds[i].getHeight() + sizeUp * DIAMOND_SCALE);
                diamonds[i].setOriginCenter();
                diamonds[i].setPosition(label.getX() + label.getWidth() / 2 + (float) Math.sin(actualAngle) * radius - diamonds[i].getWidth() / 2,
                        label.getY() + label.getHeight() / 2 + (float) Math.cos(actualAngle) * radius - diamonds[i].getHeight() / 2);
                diamonds[i].setRotation((float) (-actualAngle * 180f / Math.PI));
                actualAngle += (float) (DIAMOND_ANGLE * Math.PI / 180f);
            }

            if(scoredDiamonds <= 0) {
                repeatLevel.setSize(label.getWidth() / 4, label.getHeight() / 4);
                repeatLevel.setOrigin(repeatLevel.getWidth()/2,repeatLevel.getHeight()/2);
                repeatLevel.setPosition(label.getX() + (label.getWidth() - repeatLevel.getWidth())*0.75f
                        , label.getY() + label.getHeight() / 2);

                play.setPosition(GameScreen.W, GameScreen.H);
                chooseLevel.setSize(label.getWidth() / 4, label.getHeight() / 4);
                chooseLevel.setOrigin(chooseLevel.getWidth() / 2, chooseLevel.getHeight() / 2);
                chooseLevel.setPosition(label.getX() + (label.getWidth() - chooseLevel.getWidth())/4, label.getY() + label.getHeight() / 2);
            } else {
                play.setSize(label.getWidth() / 5, label.getHeight() / 5);
                play.setOrigin(play.getWidth() / 2, play.getHeight() / 2);

                repeatLevel.setSize(label.getWidth() / 5, label.getHeight() / 5);
                repeatLevel.setOrigin(repeatLevel.getWidth() / 2, repeatLevel.getHeight() / 2);
                repeatLevel.setPosition(label.getX() + (label.getWidth() - repeatLevel.getWidth()) / 2
                        , label.getY() + label.getHeight() / 2);

                play.setPosition(repeatLevel.getX() + repeatLevel.getWidth() * 1.25f,
                        label.getY() + label.getHeight() / 3);

                chooseLevel.setSize(label.getWidth() / 5, label.getHeight() / 5);
                chooseLevel.setOrigin(chooseLevel.getWidth() / 2, chooseLevel.getHeight() / 2);
                chooseLevel.setPosition(repeatLevel.getX() - repeatLevel.getWidth()*1.25f, label.getY() + label.getHeight() / 3);
            }
            textLabel.setPosition(label.getX() + label.getWidth()/2 - textLabel.getWidth()/2, chooseLevel.getY());
            sizeUp(scoredDiamonds);
        }

        label.draw(spriteBatch);

        for(Sprite s : diamonds)
            s.draw(spriteBatch);
        spriteBatch.end();
        mainStage.draw();  
    }
}

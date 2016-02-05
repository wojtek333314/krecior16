package com.krecior.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.PowerType;
import com.krecior.game.hud.TextLabel.Font;
import com.krecior.game.objects.Mole;
import com.krecior.utils.Container;

import java.util.concurrent.CopyOnWriteArrayList;

public class Hud extends Group {
    //===========================================================
    //Constants
    //===========================================================

    public static final float LEFT_LABEL_W = 0.25f * GameScreen.W;
    public static final float RIGHT_LABEL_W = 0.4f * GameScreen.W;
    public static final float LABEL_HEIGHT = 0.1f * GameScreen.H;
    public static final float LEFT_ICON_SIZE = 0.1f * GameScreen.W;
    public static final float MIDDLE_ICON_SIZE = 0.1f * GameScreen.W;
    public static final float RIGHT_ICON_SIZE = 0.08f * GameScreen.W;
    public static final float PAUSE_SIZE = 0.1f * GameScreen.W;
    public static final float DIAMOND_SIZE = 0.08f * GameScreen.W;

    public static final int BUTTONS = 4;

    //===========================================================
    //Fields
    //===========================================================

    private SpriteBatch pSpriteBatch;

    private Sprite pLeftLabel;
    private Sprite pMiddleLabel;
    private Sprite pRightLabel;
    private Sprite pLeftIcon;
    private Sprite pMiddleIcon;
    private Sprite pRightIcon;
    private Sprite pPause;
    private Sprite pPowerCounterLabel;

    private Stage stage;

    private TextLabel pMoles;
    private TextLabel pPoints;
    private TextLabel pScore;
    private TextLabel pKilledMoles;
    private TextLabel pDiamonds;
    private TextLabel pPowerCounter;
    private TextLabel[] values;

    private GameScreen pGame;

    private EndButton pEndButton;
    private PauseButtons pPauseButtons;

    private TextureRegion[] pPauseTextures;
    private Sprite[] diamonds;

    private CopyOnWriteArrayList<PowerIcon> pPowers;

    private boolean isIconsTouchable = true;
    private boolean isCounterAppearing = false;
    private boolean isCounterDisappering = false;
    private boolean isPauseDisappearing = false;
    private boolean isPauseAppearing = false;
    private boolean isEnd = false;

    private float counterAlpha = 0f;
    private float pauseAlpha = 1f;
    //===========================================================
    //Constructors
    //===========================================================

    public Hud(GameScreen mGame) {
        pGame = mGame;

        pSpriteBatch = new SpriteBatch();
        pEndButton = new EndButton(mGame, 3);
        pPauseTextures = new TextureRegion[BUTTONS];
        pPauseTextures[0] = Container.pButton[0];
        pPauseTextures[1] = Container.pChoose;
        pPauseTextures[2] = Container.pRepeat;
        pPauseTextures[3] = Container.pButton[6];

        diamonds = new Sprite[3];

        stage = new Stage();
        pPauseButtons = new PauseButtons(pGame, BUTTONS, 0.2f * GameScreen.W, 0.3f * GameScreen.W, pPauseTextures);
        pPauseButtons.setPosition(GameScreen.W * 2, GameScreen.H * 2);
        stage.addActor(pPauseButtons);

        createSolidSprites();
        createCounters();
        createPowerIcons();

        values = new TextLabel[3];
        if(pGame.isDeathmatch())
            createObjectValues();
    }

    //===========================================================
    //Getter & Setter
    //===========================================================

    public void setIconsTouchable(boolean mIconsTouchable) {
        isIconsTouchable = mIconsTouchable;
    }

    public Stage getStage() { return stage; }

    public boolean isEnd() { return isEnd; }

    //===========================================================
    //Methods for/from SuperClass/Interfaces
    //===========================================================

    public void onTouchDown(float mX, float mY) {
        if(pGame.TIME_STEP != 0) {
            if (isIconsTouchable)
                for (PowerIcon p : pPowers) {
                    float distance = (float) Math.sqrt(Math.pow(mX - p.getCenterX(), 2) +
                            Math.pow(GameScreen.H - mY - p.getCenterY(), 2));

                    if (distance <= PowerIcon.SIZE / 2 && p.getNumber() > 0) {
                        isIconsTouchable = false;
                        p.decrease();
                        pGame.getPowerManager().setActualPower(p.getType());
                        pGame.getPowerManager().startPower();
                        hidePowerBar();
                        System.out.println(p.getType());
                        break;
                    }
                }
        }

        float distance = (float) Math.sqrt(Math.pow(mX - pPause.getX() - pPause.getWidth() / 2, 2) +
                Math.pow(GameScreen.H - mY - pPause.getY() - pPause.getHeight() / 2, 2));

        if(distance <= pPause.getWidth() && pauseAlpha == 1f) {
            if(GameScreen.physicSimulation) {
                showPauseButtons();
                hideAll();
            }
        }
    }

    public void onTouchDrag(float mX, float mY) {

    }

    public void onTouchUp(float mX, float mY) {

    }

    //===========================================================
    //Methods
    //===========================================================

    private void createObjectValues() {
        for(int i = 0; i < values.length; i++) {
            values[i] = new TextLabel(TextLabel.Font.ROBOTO, " ", 0, 0);
            values[i].setPosition(0, -0.2f * GameScreen.H + GameScreen.H - i * 0.05f * GameScreen.H);
        }
    }

    private void refreshValues() {
        Array<String> string = new Array<String>();
        string.add("speed: " + Mole.WALK_SPEED);
        string.add("spawn: " + Mole.SPAWN_TIME);
        string.add("lives: " + pGame.getDeathmatchManager().lives);
        drawValues(string);
    }

    private void drawValues(Array<String> string) {
        for(int i = 0; i < string.size; i++) {
            values[i].setText(string.get(i));
            values[i].setPosition(0, GameScreen.H * (0.8f - i * 0.025f));
            values[i].draw(pSpriteBatch, 1f);
        }
    }

    private void createSolidSprites() {
        pLeftLabel = new Sprite(Container.pLeftLabel);
        pLeftLabel.setSize(LEFT_LABEL_W, LABEL_HEIGHT);
        pLeftLabel.setOrigin(0, 0);
        pLeftLabel.setPosition(0, GameScreen.H - pLeftLabel.getHeight());

        pLeftIcon = new Sprite(Container.pMoleFrame[0]);
        pLeftIcon.setSize(LEFT_ICON_SIZE, LEFT_ICON_SIZE);
        pLeftIcon.setOrigin(0, 0);
        pLeftIcon.setPosition(pLeftLabel.getX() + pLeftLabel.getWidth() / 2 - pLeftIcon.getWidth() / 2,
                pLeftLabel.getY() + pLeftLabel.getHeight() * 0.75f - pLeftIcon.getHeight() / 2);

        pMiddleLabel = new Sprite(Container.pLeftLabel);
        pMiddleLabel.setSize(LEFT_LABEL_W, LABEL_HEIGHT);
        pMiddleLabel.setOrigin(0, 0);
        pMiddleLabel.setPosition(pLeftLabel.getWidth(), GameScreen.H - pMiddleLabel.getHeight());

        pMiddleIcon = new Sprite(Container.killedMoleIcon);
        pMiddleIcon.setSize(MIDDLE_ICON_SIZE, MIDDLE_ICON_SIZE);
        pMiddleIcon.setOrigin(0, 0);
        pMiddleIcon.setPosition(pMiddleLabel.getX() + pMiddleLabel.getWidth() * 0.66f - pMiddleIcon.getWidth() / 2,
                pMiddleLabel.getY() + pMiddleLabel.getHeight() * 0.75f - pMiddleIcon.getHeight() / 2);

        for(int i = 0; i < diamonds.length; i++) {
            diamonds[i] = new Sprite(Container.pPowers[0]);
            diamonds[i].setSize(DIAMOND_SIZE, DIAMOND_SIZE);
            diamonds[i].setOrigin(0, 0);
            diamonds[i].setPosition(pMiddleLabel.getX() + (i+1) * (
                    + pMiddleLabel.getWidth() / (diamonds.length+1)) -diamonds[i].getWidth() / 2,
                    pMiddleLabel.getY() + pMiddleLabel.getHeight() * 0.25f - diamonds[i].getHeight() / 2);
        }

        pRightLabel = new Sprite(Container.pRightLabel);
        pRightLabel.setSize(RIGHT_LABEL_W, LABEL_HEIGHT);
        pRightLabel.setOrigin(0, 0);
        pRightLabel.setPosition(GameScreen.W - pRightLabel.getWidth(), GameScreen.H - pRightLabel.getHeight());

        pRightIcon = new Sprite(Container.pLittleDiamond);
        pRightIcon.setSize(RIGHT_ICON_SIZE, RIGHT_ICON_SIZE * Container.pLittleDiamond.getRegionHeight() /
                Container.pLittleDiamond.getRegionWidth());
        pRightIcon.setOrigin(0, 0);
        pRightIcon.setPosition(pRightLabel.getX() + pRightLabel.getWidth() / 4 - pRightIcon.getWidth() / 2,
                pRightLabel.getY() + pRightLabel.getHeight() * 0.25f - pRightIcon.getHeight() / 2);

        pPause = new Sprite(Container.pPause);
        pPause.setSize(PAUSE_SIZE, PAUSE_SIZE);
        pPause.setOrigin(0, 0);
        pPause.setPosition(GameScreen.W - pPause.getWidth() * 1.1f, pPause.getHeight() * 0.1f);
    }

    private void createCounters() {
        pMoles = new TextLabel(Font.ROBOTO, Integer.toString(pGame.getMoleManager().getAllMoles()
                - pGame.getScoreManager().getBorn()), 0, 0);
        pMoles.setPosition(pLeftLabel.getX() + pLeftLabel.getWidth() / 2 - pMoles.getWidth() / 2,
                pLeftLabel.getY() + pLeftLabel.getHeight() * 0.25f + pMoles.getHeight() / 2);

        pScore = new TextLabel(Font.ROBOTO, "Score:", 0, 0);
        pScore.setPosition(pScore.getWidth() + pRightLabel.getX() + pRightLabel.getWidth() * 0.05f,
                -pScore.getHeight() / 2 + pRightLabel.getY() + pRightLabel.getHeight() * 0.75f + pScore.getHeight() / 2);

        pKilledMoles = new TextLabel(Font.ROBOTO, " ", 0, 0);
        pKilledMoles.setPosition(-pKilledMoles.getWidth() / 2 + pMiddleLabel.getX() + pMiddleLabel.getWidth() * 0.33f,
                pMiddleLabel.getY() + pMiddleLabel.getHeight() * 0.75f);

        pPoints = new TextLabel(Font.ROBOTO, Integer.toString(pGame.getScoreManager().getPoints()), 0, 0);
        pPoints.setPosition(pPoints.getWidth() + pRightLabel.getX() + pRightLabel.getWidth() * 0.95f - pMoles.getWidth(),
                pPoints.getHeight() + pRightLabel.getY() + pRightLabel.getHeight() * 0.75f + pMoles.getHeight() / 2);

        pDiamonds = new TextLabel(Font.ROBOTO, Integer.toString(pGame.getScoreManager().getDiamonds()), 0, 0);
        pDiamonds.setPosition(pRightLabel.getX() + pRightLabel.getWidth() * 0.95f - pDiamonds.getWidth(),
                pRightLabel.getY() + pRightLabel.getHeight() * 0.25f + pDiamonds.getHeight() / 2);


        pPowerCounterLabel = new Sprite(Container.pEmptyButton);
        pPowerCounterLabel.setSize(PAUSE_SIZE * 0.4f, PAUSE_SIZE * 0.4f);
        pPowerCounterLabel.setPosition(GameScreen.W - pPowerCounterLabel.getWidth(),
                pRightLabel.getY() - pPowerCounterLabel.getHeight());
        pPowerCounterLabel.setAlpha(counterAlpha);

        pPowerCounter = new TextLabel(Font.ROBOTO, Integer.toString(pGame.getScoreManager().getDiamonds()), 0, 0);
        pPowerCounter.setPosition(pPowerCounterLabel.getX() + (pPowerCounterLabel.getWidth() - pPowerCounter.getWidth()) / 2,
                pPowerCounterLabel.getY() + (pPowerCounterLabel.getHeight() + pPowerCounter.getHeight()) / 2);
    }

    protected void showPauseButtons() {
        GameScreen.stopPhysicSimulation();
        pPauseButtons.setPosition(GameScreen.W / 2, GameScreen.H / 2);
        pPauseButtons.updateSoundButton();
    }

    public boolean isTouchOnAnyIcon() {
        boolean d = false;

        for (PowerIcon p : pPowers) {
            float distance = (float) Math.sqrt(Math.pow(pGame.getTouchProcessor().getTouchX() - p.getCenterX(), 2) +
                    Math.pow(GameScreen.H - pGame.getTouchProcessor().getTouchY() - p.getCenterY(), 2));

            if (distance <= PowerIcon.SIZE / 2)
                d = true;
        }
        return d;
    }

    protected void hidePauseButtons() {
        GameScreen.startPhysicSimulation();
        pPauseButtons.setPosition(GameScreen.W * 2, GameScreen.H * 2);
    }

    private void createPowerIcons() {
        pPowers = new CopyOnWriteArrayList<PowerIcon>();
    }

    public void increasePower(PowerType mPowerType) {
        for(PowerIcon p : pPowers)
            if(p.getType() == mPowerType)
                p.addPower();
    }

    public void addPower(PowerType mPowerType) {
        boolean b = false;

        for(PowerIcon p : pPowers)
            if(p.getType() == mPowerType)
                b = true;

        if(b) {
            increasePower(mPowerType);
        } else {
            pPowers.add(new PowerIcon(mPowerType));
        }
    }

    public void refreshPowerIconPositions() {
        for(int i = 0; i < pPowers.size(); i++) {
            float h = i % 2 == 0 ? PowerIcon.SIZE: PowerIcon.SIZE / 4;
            float w = 0.75f * PowerIcon.SIZE * i;

            pPowers.get(i).setPosition(0.01f * GameScreen.W + w, h);
        }
    }

    private void refreshPowerCounterPosition() {
        pPowerCounter.setPosition(pPowerCounterLabel.getX() + (pPowerCounterLabel.getWidth() - pPowerCounter.getWidth()) / 2,
                pPowerCounterLabel.getY() + (pPowerCounterLabel.getHeight() + pPowerCounter.getHeight()) / 2);
    }

    public void setPowerCounter(float f) {
        String s = Float.toString(f);
        String s1 = "";

        int k = 0;

        for(int i = 0; i < s.length(); i++)
            if(s.toCharArray()[i] == '.')
                k = i;

        for(int i = 0; i < k+2; i++)
            s1 += s.toCharArray()[i];

        if(!pGame.getPowerManager().isTimePower() || (pGame.getPowerManager().getActualPower() ==
                PowerType.FIRE && pGame.getPowerManager().getUse() > 0)) {
            s1 = "";

            for(int i = 0; i < k; i++)
                s1 += s.toCharArray()[i];
        }

        pPowerCounter.setText(s1);
        refreshPowerCounterPosition();
    }

    public void render() {
        pSpriteBatch.begin();

        pLeftLabel.draw(pSpriteBatch);
        pLeftIcon.draw(pSpriteBatch);

        pMiddleLabel.draw(pSpriteBatch);
        pMiddleIcon.draw(pSpriteBatch);

        for(int i = 0; i < this.diamonds.length; i++)
            if(i < pGame.getScoreManager().getEndDiamonds())
                this.diamonds[i].draw(pSpriteBatch);

        pRightLabel.draw(pSpriteBatch);
        pRightIcon.draw(pSpriteBatch);
        pPause.draw(pSpriteBatch);

        if(pGame.isDeathmatch())
            pMoles.setText(Integer.toString(pGame.getScoreManager().getBorn()));
        else
            pMoles.setText(Integer.toString(pGame.getMoleManager().getAllMoles() - pGame.getScoreManager().getBorn()));
        pMoles.setPosition(pLeftLabel.getX() + pLeftLabel.getWidth() / 2 - pMoles.getWidth() / 2,
                pLeftLabel.getY() + pLeftLabel.getHeight() * 0.25f + pMoles.getHeight() / 2);
        pMoles.draw(pSpriteBatch, 1f);

        pScore.setPosition(pRightLabel.getX() + pRightLabel.getWidth() * 0.05f,
                pRightLabel.getY() + pRightLabel.getHeight() * 0.75f + pScore.getHeight() / 2);
        pScore.draw(pSpriteBatch, 1f);

        pKilledMoles.setText(Integer.toString(pGame.getScoreManager().getKilledMoles()
                + pGame.getScoreManager().getDestroyedTargets()));
        pKilledMoles.setPosition(-pKilledMoles.getWidth() / 2 + pMiddleLabel.getX() + pMiddleLabel.getWidth() * 0.33f,
                pMiddleLabel.getY() + pMiddleLabel.getHeight() * 0.75f - pKilledMoles.getHeight() / 4);
        pKilledMoles.draw(pSpriteBatch, 1f);

        pPoints.setText(Integer.toString(pGame.getScoreManager().getPoints()));
        pPoints.setPosition(pRightLabel.getX() + pRightLabel.getWidth() * 0.95f - pPoints.getWidth(),
                -pPoints.getHeight() / 2 + pRightLabel.getY() + pRightLabel.getHeight() * 0.75f + pPoints.getHeight() / 2);
        pPoints.draw(pSpriteBatch, 1f);

        pDiamonds.setText(Integer.toString(pGame.getScoreManager().getDiamonds()));
        pDiamonds.setPosition(pRightLabel.getX() + pRightLabel.getWidth() * 0.95f - pDiamonds.getWidth(),
                pRightLabel.getY() + pRightLabel.getHeight() * 0.25f + pDiamonds.getHeight() / 2);
        pDiamonds.draw(pSpriteBatch, 1f);

        if(pGame.isDeathmatch())
            refreshValues();

        if(isCounterDisappering) {
            counterAlpha -= Gdx.graphics.getDeltaTime();

            if(counterAlpha <= 0f)
                counterAlpha = 0f;

            pPowerCounterLabel.setAlpha(counterAlpha);

            disappearPowerCounter();
        }

        if(isCounterAppearing) {
            counterAlpha += Gdx.graphics.getDeltaTime();

            if(counterAlpha >= 1f)
                counterAlpha = 1f;

            pPowerCounterLabel.setAlpha(counterAlpha);

            appearPowerCounter();
        }

        if(isPauseDisappearing) {
            pauseAlpha -= Gdx.graphics.getDeltaTime();

            if(pauseAlpha <= 0f)
                pauseAlpha = 0f;

            pPause.setAlpha(pauseAlpha);

            disappearPause();
        }

        if(isPauseAppearing) {
            pauseAlpha += Gdx.graphics.getDeltaTime();

            if(pauseAlpha >= 1f)
                pauseAlpha = 1f;

            pPause.setAlpha(pauseAlpha);

            appearPause();
        }

        pPowerCounterLabel.draw(pSpriteBatch);
        pPowerCounter.draw(pSpriteBatch, counterAlpha);

        pSpriteBatch.end();

        for(int i = 0; i < pPowers.size(); i++) {
            if(pPowers.get(i).isToRemove()) {
                pPowers.remove(pPowers.get(i));
                continue;
            }

            refreshPowerIconPositions();
            pPowers.get(i).draw();
        }

        pEndButton.draw();
        stage.act();
        stage.draw();

        pPauseButtons.rotate(Gdx.graphics.getDeltaTime(), 20);
    }

    private void hidePowerBar() {
        for(PowerIcon p: pPowers)
            p.disappear();
    }

    public void showEnd() {
        isEnd = true;

        pEndButton.sizeUp(pGame.getScoreManager().getEndDiamonds());
        pGame.getTouchProcessor().setGrabMoles(false);
        pGame.getPowerManager().setActualPower(PowerType.NORMAL);
        hideAll();
    }

    public void showPowerBar() {
        for(PowerIcon p: pPowers) {
            if(p.getNumber() > 0)
                p.appear();
        }
    }

    public void appearPowerCounter() {
        if(pGame.getPowerManager().getActualPower() != PowerType.NORMAL) {
            isCounterAppearing = true;
            isCounterDisappering = false;
        }

        if(counterAlpha >= 1f) {
            counterAlpha = 1f;
            pPowerCounterLabel.setAlpha(counterAlpha);
            isCounterAppearing = false;
        }
    }

    public void disappearPowerCounter() {
        isCounterDisappering = true;
        isCounterAppearing = false;

        if(counterAlpha <= 0f) {
            counterAlpha = 0f;
            pPowerCounterLabel.setAlpha(0);
            isCounterDisappering = false;
        }
    }

    public void disappearPause() {
        isPauseDisappearing = true;
        isPauseAppearing = false;
        if(pauseAlpha <= 0f) {
            pauseAlpha = 0f;
            pPause.setAlpha(pauseAlpha);
            isPauseDisappearing = false;
        }
    }

    public void appearPause() {
        isPauseAppearing = true;
        isPauseDisappearing = false;
        if(pauseAlpha >= 1f) {
            pauseAlpha = 1f;
            pPause.setAlpha(pauseAlpha);
            isPauseAppearing = false;
        }
    }

    private void hideAll() {
        disappearPowerCounter();
        hidePowerBar();
        disappearPause();
    }

    public void showAll() {
        appearPowerCounter();
        showPowerBar();
        appearPause();
    }

    //===========================================================
    //Inner and Anonymous Classes
    //===========================================================
}

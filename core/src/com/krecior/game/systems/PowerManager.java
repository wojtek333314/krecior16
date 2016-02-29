package com.krecior.game.systems;

import com.badlogic.gdx.math.Vector2;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.MoleState;
import com.krecior.game.enums.PowerType;
import com.krecior.game.objects.Hill;
import com.krecior.game.objects.Mole;
import com.krecior.game.objects.effect.Effect;
import com.krecior.utils.Animation;
import com.krecior.utils.Container;
import com.krecior.sound.SoundManager;

import java.util.concurrent.CopyOnWriteArrayList;

public class PowerManager {
    //===========================================================
    //Constants
    //===========================================================

    public static float FIRE_SIZE = 0.2f * GameScreen.W;
    public static float HAMMER_SIZE = 0.2f * GameScreen.W;
    public static float SCREWDRIVER_SIZE = 0.2f * GameScreen.W;
    public static float BOLT_WIDTH = 0.1f * GameScreen.W;
    public static float WATER_SIZE = 0.2f * GameScreen.W;

    public static int SCREWDRIVERS = 3;
    public static int HAMMERS = 1;

    //===========================================================
    //Fields
    //===========================================================

    private GameScreen pGame;

    private PowerType pPowerType = PowerType.NORMAL;

    private CopyOnWriteArrayList<Effect> touchFollowers;

    private RubberSystem rubberSystem;

    private float pPowerTime = -1;
    private float pTime = 0;

    private int pUse = 0;
    private int erasedHills = 0;
    private int killedMoles = 0;

    //===========================================================
    //Constructors
    //===========================================================

    public PowerManager(GameScreen mGameScreen) {
        pGame = mGameScreen;

        createTouchFollowers();

        rubberSystem = new RubberSystem(pGame);
    }

    //===========================================================
    //Getter & Setter
    //===========================================================

    public PowerType getActualPower() {
        return pPowerType;
    }

    public void setActualPower(PowerType mActualPower) {
        pPowerType = mActualPower;
    }

    public void addPower(PowerType mPowerType) {
        pGame.getHud().addPower(mPowerType);
        pGame.getHud().refreshPowerIconPositions();
    }

    public float getTimeToEndOfPower() {
        return pPowerTime - pTime;
    }

    public boolean isTimePower() {
        return pPowerType == PowerType.FIRE || pPowerType == PowerType.ERASE;
    }

    public int getUse() {
        return pUse;
    }

    //===========================================================
    //Methods for/from SuperClass/Interfaces
    //===========================================================



    //===========================================================
    //Methods
    //===========================================================

    private void createTouchFollowers() {
        touchFollowers = new CopyOnWriteArrayList<Effect>();
    }

    public void startPower() {
        pGame.getTouchProcessor().setGrabMoles(false);
        switch(getActualPower()) {
            case DIAMOND: {

                break; }
            case FIRE: {
                pUse = 1;
                pGame.getHud().appearPowerCounter();
                break; }
            case HAMMER: {
                pUse = 2;
                pGame.getHud().appearPowerCounter();
                break; }
            case NORMAL: {
                break; }
            case GUN: {
                pUse = 5;
                pGame.getHud().appearPowerCounter();
                break; }
            case POISON: {
                pUse = 1;
                pGame.getHud().appearPowerCounter();
                break; }
            case ERASE: {
                pUse = 1;
                pGame.getHud().appearPowerCounter();
                break; }
            case SCREWDIVER: {
                pUse = 2;
                pGame.getHud().appearPowerCounter();
                break; }
            case LIGHTBOLT: {
                pUse = 3;
                pGame.getHud().appearPowerCounter();
                break; }
            case WATER: {
                pUse = 1;
                pGame.getHud().appearPowerCounter();
                break; }
        }
    }

    public void render() {
        pTime += GameScreen.TIME_STEP;

        rubberSystem.draw();

        if(isTimePower() && pUse <= 0)
            pGame.getHud().setPowerCounter(getTimeToEndOfPower());
        else
            pGame.getHud().setPowerCounter((float) pUse);

        if(pTime >= pPowerTime && pPowerTime > 0 && isTimePower()) {
            stopPower();
            pPowerTime = -1;
        } else {
            for(Effect e : touchFollowers)
                e.setPosition(pGame.getTouchProcessor().getTouchX() - e.getSprite().getWidth() / 2, GameScreen.H - pGame.getTouchProcessor().getTouchY() - e.getSprite().getHeight() / 2);
        }
    }

    public void onTouchDown() {
        if(!pGame.getHud().isTouchOnAnyIcon())
            switch(pPowerType) {
                case DIAMOND:
                    break;
                case FIRE:
                    if(pUse > 0) {
                        pTime = 0;
                        pPowerTime = 4f;

                        final Effect effect = new Effect(Container.pFireFrame[14]);
                        effect.setSize(FIRE_SIZE, FIRE_SIZE);
                        effect.setAnimation(new Animation(effect.getSprite(), pPowerTime / 4, 4, Container.pFireFrame, 0, 9, 0) {
                            @Override
                            public void onAnimationFinished() {
                                touchFollowers.remove(effect);
                                pGame.getEffectSystem().remove(effect);
                            }
                        });
                        touchFollowers.add(effect);
                        pGame.getEffectSystem().add(effect);
                        SoundManager.play(SoundManager.fire_final);

                        pUse--;
                    }
                    break;
                case HAMMER:
                    if(pUse > 0) {
                        hammerEffect(new Vector2(pGame.getTouchProcessor().getTouchX(),
                                pGame.getTouchProcessor().getTouchY()), HAMMERS);

                        pUse--;
                    }
                    break;
                case NORMAL:

                    break;
                case GUN:
                    if(pUse > 0) {
                        powerTouchOnMoleResponse();
                        SoundManager.play(SoundManager.shot_pistol_final);
                        pUse--;

                        if(pUse <= 0) {
                            stopPower();
                        }
                    }
                    break;
                case POISON:
                    if(pUse > 0) {
                        powerTouchOnHillResponse();
                        SoundManager.play(SoundManager.poison_project);
                        pUse--;
                    }

                    if(pUse <= 0)
                        stopPower();
                    break;
                case ERASE:
                    if(pUse > 0) {
                        pTime = 0;
                        pPowerTime = 1.5f;
                        pUse--;
                        SoundManager.play(SoundManager.eraser_final);
                    }
                    rubberSystem.onTouchDown();
                    break;
                case SCREWDIVER:
                    if(pUse > 0) {
                        screwdriverEffect(new Vector2(pGame.getTouchProcessor().getTouchX(),
                                pGame.getTouchProcessor().getTouchY()), SCREWDRIVERS);

                        pUse--;
                    }
                    break;
                case LIGHTBOLT:
                    if(pUse > 0) {
                        powerTouchOnMoleResponse();
                        SoundManager.play(SoundManager.thunder_final);
                        pUse--;

                        final Effect effect = new Effect(Container.pBoltFrame[0]);
                        effect.setSize(BOLT_WIDTH, BOLT_WIDTH * 4);
                        effect.setPosition(pGame.getTouchProcessor().getTouchX() - effect.getSprite().getWidth() / 2,
                                GameScreen.H - pGame.getTouchProcessor().getTouchY());
                        effect.setAnimation(new Animation(effect.getSprite(), 0.25f, 1, Container.pBoltFrame, 0, 2, 0) {
                            @Override
                            public void onAnimationFinished() {
                                pGame.getEffectSystem().remove(effect);
                            }
                        });
                        pGame.getEffectSystem().add(effect);

                        if(pUse <= 0) {
                            stopPower();
                        }
                    }
                    break;
                case WATER:
                    if(pUse > 0) {
                        powerTouchOnHillResponse();
                        SoundManager.play(SoundManager.water_serving_final);
                        pUse--;
                    }

                    if(pUse <= 0)
                        stopPower();
                    break;
            }
    }


    public void onTouchDrag() {
        switch (pPowerType) {
            case DIAMOND:
                break;
            case FIRE:
                powerTouchOnMoleResponse();
                break;
            case HAMMER:
                break;
            case NORMAL:
                break;
            case GUN:
                break;
            case POISON:
                powerTouchOnMoleResponse();
                break;
            case ERASE:
                powerTouchOnMoleResponse();

                if(erasedHills == 0)
                    powerTouchOnHillResponse();
                break;
            case SCREWDIVER:
                break;
            case LIGHTBOLT:
                break;
            case WATER:
                break;
        }
    }

    public void onTouchUp() {
        switch(pPowerType) {
            case DIAMOND:
                break;
            case FIRE:
                break;
            case HAMMER:
                killedMoles = 0;
                break;
            case NORMAL:
                break;
            case GUN:
                break;
            case POISON:
                break;
            case ERASE:
                rubberSystem.onTouchUp();
                break;
            case SCREWDIVER:
                break;
            case LIGHTBOLT:
                break;
            case WATER:
                break;
        }
    }

    private void stopPower() {
        pGame.getHud().showPowerBar();
        pGame.getHud().setIconsTouchable(true);

        if(pPowerType == PowerType.ERASE) {
            rubberSystem.onTouchUp();
            erasedHills = 0;
        }

        setActualPower(PowerType.NORMAL);
        pGame.getHud().disappearPowerCounter();
        pGame.getTouchProcessor().setGrabMoles(true);
    }

    private void powerTouchOnMoleResponse() {
        for(Mole m : pGame.getMoles()) {
            float distance = (float) Math.sqrt(Math.pow(pGame.getTouchProcessor().getTouchX() -
                    m.getPosition().x / GameScreen.METER_W * GameScreen.W, 2) +
                    Math.pow((GameScreen.H - pGame.getTouchProcessor().getTouchY()
                            - m.getPosition().y / GameScreen.METER_H * GameScreen.H), 2));


            if(distance <= PowerType.getTouchDistance(pPowerType)) {
                m.setState(MoleState.POWER_EFFECT);
                m.setPowerType(pPowerType);

                if(pPowerType == PowerType.HAMMER && killedMoles == 0)
                    pGame.getScoreManager().addDiamonds(3);
                killedMoles++;
                System.out.println("that_k: " + killedMoles);
            }
        }
    }

    private void powerTouchOnHillResponse() {
        for(Hill h : pGame.getHills()) {
            float distance = (float) Math.sqrt(Math.pow(pGame.getTouchProcessor().getTouchX() -
                    h.getPosition().x / GameScreen.METER_W * GameScreen.W, 2) +
                    Math.pow((GameScreen.H - pGame.getTouchProcessor().getTouchY()
                            - h.getPosition().y / GameScreen.METER_H * GameScreen.H), 2));

            if(distance <= PowerType.getTouchDistance(pPowerType)) {
                h.setPower(pPowerType);

                if(pPowerType == PowerType.POISON)
                    SoundManager.play(SoundManager.poison_project);

                if(pPowerType == PowerType.ERASE)
                    erasedHills++;

                break;
            }
        }
    }

    private void hammerEffect(final Vector2 position, final int leftEffects) {
        final Effect effect = new Effect(PowerType.getTexture(PowerType.HAMMER)) {
            @Override
            public void onRotationFinished() {
                SoundManager.play(SoundManager.hammer_hit_final);
                powerTouchOnMoleResponse();
            }

            @Override
            public void onDisappearingFinished() {
                if(pUse <= 0) {
                    stopPower();
                }

                if(leftEffects > 1) {
                    hammerEffect(position, leftEffects-1);
                }
            }
        };
        effect.setSize(PowerManager.HAMMER_SIZE, PowerManager.HAMMER_SIZE);
        pGame.getEffectSystem().add(effect);
        effect.setPosition(position.x - effect.getSprite().getWidth() * 0.75f,
                GameScreen.H - position.y - effect.getSprite().getHeight() / 2
                        + Mole.GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.H / 2);
        effect.disappear();
        effect.setRotate(-80f);
    }

    private void screwdriverEffect(final Vector2 position, final int leftEffects) {
        Effect effect = new Effect(PowerType.getTexture(PowerType.SCREWDIVER)) {
            @Override
            public void onRotationMoveFinished() {
                SoundManager.play(SoundManager.hammer_hit_final);
                powerTouchOnMoleResponse();

                if(pUse <= 0) {
                    stopPower();
                }

                if(leftEffects > 1) {
                    screwdriverEffect(position, leftEffects-1);
                }
            }
        };
        effect.setSize(PowerManager.SCREWDRIVER_SIZE, PowerManager.SCREWDRIVER_SIZE);
        pGame.getEffectSystem().add(effect);
        effect.setRotation(180f + (pGame.getRandom().nextFloat() - 0.5f) * 90f);
        effect.setPosition(position.x - effect.getSprite().getWidth() / 2
                        + (float) Math.sin(Math.toRadians(effect.getSprite().getRotation())) * effect.getSprite().getWidth(),
                GameScreen.H - position.y
                        - (float) Math.cos(Math.toRadians(effect.getSprite().getRotation())) * effect.getSprite().getWidth()
                        - effect.getSprite().getHeight() / 2);
        effect.disappear();
        effect.setRotationMove(0.2f, -SCREWDRIVER_SIZE / 2);

    }

    //===========================================================
    //Inner and Anonymous Classes
    //===========================================================
}

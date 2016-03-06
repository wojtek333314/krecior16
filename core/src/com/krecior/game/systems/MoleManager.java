package com.krecior.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.MoleState;
import com.krecior.game.enums.PowerType;
import com.krecior.game.objects.Hill;
import com.krecior.game.objects.Mole;
import com.krecior.game.objects.effect.Effect;
import com.krecior.sound.SoundManager;
import com.krecior.utils.Animation;
import com.krecior.utils.Container;

import java.util.concurrent.CopyOnWriteArrayList;

public class MoleManager {
    public static float WATER_THROW_SPEED = 4f;

    private CopyOnWriteArrayList<Mole> pMoles;
    private GameScreen pGame;
    private SpriteBatch pSpriteBatch;

    private int pAllMoles;

    public MoleManager(GameScreen mGameScreen) {
        pGame = mGameScreen;
        pAllMoles = Container.pLvlsData[pGame.getMap().getActualLevel()].moles;

        pMoles = new CopyOnWriteArrayList<Mole>();
        pSpriteBatch = new SpriteBatch();

        for(int i = 0; i < Container.pLvlsData[pGame.getMap().getActualLevel()].moles; i++)
            pMoles.add(new Mole(i));

    }

    public int getGrabbedMole() {
        for(int i = 0; i < getMoles().size(); i++)
            if(getMoles().get(i).getState() == MoleState.GRABBED)
                return i;

        return -1;
    }

    public int getAllMoles() { return pAllMoles; }

    public CopyOnWriteArrayList<Mole> getMoles() {
        return pMoles;
    }

    private boolean molesOnMap() {
        for(Mole m : pMoles) {
            boolean x_ = isInSection((1f - GameScreen.PLAY_FIELD_WIDTH) * GameScreen.W / 2
                    , GameScreen.W / 2 + GameScreen.PLAY_FIELD_WIDTH * GameScreen.W / 2,
                    m.getPosition().x / GameScreen.METER_W * GameScreen.W);
            boolean y_ = isInSection(GameScreen.PLAY_FIELD_BOT_GAP * GameScreen.H
                    , (GameScreen.PLAY_FIELD_TOP_GAP + GameScreen.PLAY_FIELD_BOT_GAP) * GameScreen.H
                    , m.getPosition().y / GameScreen.METER_H * GameScreen.H);

            return x_ && y_;
        }
        return false;
    }

    private boolean isInSection(float limA, float limB, float value) {
        return value >= limA && value <= limB;
    }

    public void manage() {
        if(!pGame.isDeathmatch())
        if(!molesOnMap() && (pGame.getScoreManager().getKilledMoles()
                + pGame.getScoreManager().getDeserters()
                + pGame.getScoreManager().getDestroyedTargets() >= pAllMoles &&
                !pGame.getHud().isEnd()) || pGame.getHillManager().getHills().size() < 1) {
            pGame.getHud().showEnd();
        }

        for(final Mole m : pMoles) {
            switch(m.getState()) {
                case NOT_USED:
                    m.getSprite().setAlpha(0f);
                    m.getSprite().setOriginCenter();
                    m.getSprite().setSize(1, 1);
                    m.setLinearVelocity(0, 0);
                    m.setGrabClause(true);
                    m.setKilled(false);

                    if(pGame.getHills().size() >= 1) {
                        int x = pGame.getRandom().nextInt(pGame.getHills().size());
                        final Hill h = pGame.getHills().get(x);

                        if(h.getTime() >= Hill.TIME_TO_OPEN &&
                                ((pAllMoles - pGame.getScoreManager().getBorn() > 0) || pGame.isDeathmatch())) {
                            m.setPosition(h.getPosition().x, h.getPosition().y - 0.1f * Mole.GRAPHIC_SIZE, 0);
                            m.getSprite().setOriginCenter();
                            m.setPowerType(h.getPower());

                            if(h.getPower() == PowerType.WATER) {
                                final Effect effect = new Effect(Container.pWaterFrame[0]);
                                SoundManager.play(SoundManager.splash_final);
                                effect.setSize(PowerManager.WATER_SIZE, PowerManager.WATER_SIZE);
                                effect.setPosition(h.getPosition().x / GameScreen.METER_W
                                                * GameScreen.W - PowerManager.WATER_SIZE / 2,
                                        h.getPosition().y / GameScreen.METER_H * GameScreen.H);
                                effect.setAnimation(new Animation(effect.getSprite(), 1f
                                        , 1, Container.pWaterFrame, 0, 14, 0) {
                                    @Override
                                    public void onAnimationFinished() {
                                        pGame.getEffectSystem().remove(effect);
                                        h.removePower();
                                    }
                                });
                                pGame.getEffectSystem().add(effect);
                                m.setState(MoleState.POWER_EFFECT);
                                h.setTime(-10f);
                            }  else{
                                m.setState(MoleState.BORN);
                            }

                            if(h.getTime() > 0)
                                h.setTime(0);

                            pGame.getScoreManager().increaseBorn();
                        }
                    }
                    break;

                case BORN:
                    m.getSprite().setAlpha(1f);
                    m.setLinearVelocity(0, 0.05f);
                    m.setAngularVelocity(0);
                    m.getBody().setActive(true);

                    if(m.getAnimation() == null) {
                        m.setAnimation(new Animation(m.getSprite()
                                , 0.1351f * Math.abs((1.85f - (Mole.SPAWN_TIME - 1.85f))), 1,
                                Container.pMoleFrame, 0, 2, m.getAngle()) {
                            @Override
                            public void onAnimationRunning() {
                                float size = (pTime / pTimeOfLoop) / (Mole.SPAWN_TIME)
                                        * Mole.GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W;

                                m.getSprite().setSize(size, size);
                                m.updateSpritePosition();
                                m.getSprite().setOriginCenter();
                            }

                            @Override
                            public void onAnimationFinished() {
                                System.out.println("PIERWSZA ZAKONCZONA");
                                m.setAnimation(new Animation(m.getSprite()
                                        , 0.054f * (1.85f - (Mole.SPAWN_TIME - 1.85f)), 1
                                        , Container.pMoleFrame, 0, 2, m.getAngle()) {
                                    @Override
                                    public void onAnimationRunning() {
                                        float size = (1f + 0.054f * Mole.SPAWN_TIME - pTime)
                                                * Mole.GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W;

                                        m.getSprite().setSize(size, size);
                                        m.updateSpritePosition();
                                        m.getSprite().setOriginCenter();
                                    }

                                    @Override
                                    public void onAnimationFinished() {
                                        if(m.getPowerType() == PowerType.POISON)
                                            m.setState(MoleState.POWER_EFFECT);
                                        else
                                            m.setState(MoleState.STAY);

                                        m.stopAnimation();
                                    }
                                });
                            }
                        });
                    }
                    break;

                case STAY:
                    m.setLinearVelocity(0, 0);
                    m.setAngularVelocity(0);

                    if(m.getAnimation() == null) {
                        m.setAnimation(new Animation(m.getSprite(), 0.81f / Mole.SPAWN_TIME, 1
                                , Container.pMoleFrame, 0, 2, m.getAngle()) {
                            @Override
                            public void onAnimationFinished() {
                                m.updateSpritePosition();
                                m.setLinearVelocity(0, 0);
                                m.setAngularVelocity(0);
                                m.setWalkDirection(pGame.getRandom().nextInt(2));
                                m.setState(MoleState.WALK);

                                m.stopAnimation();
                            }
                        });
                    }
                    break;

                case WALK:
                    if(m.getGrabClause() || m.pDisappearing) {
                        if(m.getTime() >= Mole.MOLE_STEP_TIME / Mole.WALK_SPEED) {
                            m.startWalk();
                            m.setTime(0f);
                        }
                    } else {
                        if(Math.abs(m.getLinearVelocity().x) <= Mole.WALK_SPEED / 2)
                            m.goAway();
                    }
                    break;

                case GRABBED:
                    int a = getGrabbedMole();
                    if(a >= 0) {
                        pMoles.get(a).setPosition(pGame.getTouchProcessor().getTouchX()
                                / GameScreen.W * GameScreen.METER_W,
                                (GameScreen.H - pGame.getTouchProcessor().getTouchY())
                                / GameScreen.H * GameScreen.METER_H, 0);

                        if(m.getAnimation() == null) {
                            m.setAnimation(new Animation(m.getSprite(), Mole.GRAB_TIME
                                    , 5, Container.pMoleFrame, 8, 9, m.getAngle()) {
                                @Override
                                public void onAnimationFinished() {
                                    m.stopAnimation();
                                }
                            });
                        }
                    }

                    break;
                case FLY:
                    if(Math.abs(m.getLinearVelocity().x) <= Mole.MIN_SPEED
                            && Math.abs(m.getLinearVelocity().y) <= Mole.MIN_SPEED && !m.pScaling) {
                        m.setState(MoleState.STAY);
                    }
                    break;

                case HIT:
                    m.disappear();

                    m.stopAnimation();
                    m.getSprite().setRegion(Container.pMoleFrame[7]);

                    break;

                case POWER_EFFECT:
                    powerEffect(m);
                    break;
            }
        }
        renderMoles();
    }

    private void powerEffect(final Mole m) {
        switch(m.getPowerType()) {
            case DIAMOND:
                break;
            case FIRE:
                m.stopAnimation();
                m.getSprite().setRegion(Container.pMoleFrame[10]);
                m.disappear();
                m.resetSpriteRotation();
                giveDiamondForKill(m);
                pGame.getScoreManager().killMole(m);
                break;
            case HAMMER:
                m.stopAnimation();
                m.getSprite().setRegion(Container.pHammerScrewdriver[0]);
                m.resetSpriteRotation();
                m.setLinearVelocity(0, 0);
                m.setAnimation(new Animation(m.getSprite(), 1f, 1, Container.pHammerScrewdriver
                        , 0, 4, m.getAngle()) {
                    @Override
                    public void onAnimationFinished() {
                        m.stopAnimation();
                        m.getSprite().setRegion(Container.pHammerScrewdriver[3]);
                        m.disappear();
                        m.resetSpriteRotation();
                    }
                });
                m.setPowerType(PowerType.NORMAL);
                pGame.getScoreManager().killMole(m);
                break;
            case NORMAL:
                break;
            case GUN:
                m.stopAnimation();
                m.getSprite().setRegion(Container.pMoleFrame[11]);
                m.disappear();
                m.resetSpriteRotation();
                giveDiamondForKill(m);
                pGame.getScoreManager().killMole(m);
                break;
            case POISON:
                m.setAnimation(new Animation(m.getSprite(), 0.81f * Mole.SPAWN_TIME, 1
                        , Container.pMoleFrame, 12, 12, m.getAngle()) {
                    @Override
                    public void onAnimationFinished() {
                        m.stopAnimation();
                        m.getSprite().setRegion(Container.pMoleFrame[13]);
                        m.disappear();
                        m.resetSpriteRotation();
                    }
                });
                m.setPowerType(PowerType.NORMAL);
                giveDiamondForKill(m);
                pGame.getScoreManager().killMole(m);
                break;
            case ERASE:
                m.stopAnimation();
                m.getSprite().setRegion(Container.pMoleFrame[7]);
                m.disappear();
                m.resetSpriteRotation();
                giveDiamondForKill(m);
                pGame.getScoreManager().killMole(m);
                break;
            case SCREWDIVER:
                m.stopAnimation();
                m.getSprite().setRegion(Container.pHammerScrewdriver[5]);
                m.resetSpriteRotation();
                m.setLinearVelocity(0, 0);
                m.setAnimation(new Animation(m.getSprite(), 0.6f
                        , 1, Container.pHammerScrewdriver, 5, 7, m.getAngle()) {
                    @Override
                    public void onAnimationFinished() {
                        m.stopAnimation();
                        m.getSprite().setRegion(Container.pHammerScrewdriver[7]);
                        m.disappear();
                        m.resetSpriteRotation();
                    }
                });
                m.setPowerType(PowerType.NORMAL);
                giveDiamondForKill(m);
                pGame.getScoreManager().killMole(m);
                break;
            case LIGHTBOLT:
                m.stopAnimation();
                m.getSprite().setRegion(Container.pMoleFrame[10]);
                m.disappear();
                m.resetSpriteRotation();
                giveDiamondForKill(m);
                pGame.getScoreManager().killMole(m);
                break;
            case WATER:
                m.getSprite().setAlpha(1f);
                m.getSprite().setSize(Mole.GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W,
                        Mole.GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W);
                m.getSprite().setOriginCenter();
                m.stopAnimation();
                m.getSprite().setRegion(Container.pMoleFrame[7]);
                m.resetSpriteRotation();
                m.setLinearVelocity(0, WATER_THROW_SPEED);
                m.setPowerType(PowerType.NORMAL);
                m.disappear();
                giveDiamondForKill(m);
                pGame.getScoreManager().killMole(m);
                break;
        }
    }

    private void giveDiamondForKill(Mole m) {
        if(!m.isKilled())
            pGame.getScoreManager().addDiamonds(1);
    }

    private void renderMoles() {
        pSpriteBatch.begin();

        for(Mole m : pMoles) {
            m.draw(pSpriteBatch);
        }

        pSpriteBatch.end();
    }
}

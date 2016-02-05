package com.krecior.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.krecior.Manager;
import com.krecior.game.GameScreen;
import com.krecior.menu.ScreenType;
import com.krecior.sound.SoundManager;
import com.krecior.utils.Container;

public class PauseButtons extends Group {
    //===========================================================
    //Constants
    //===========================================================


    //===========================================================
    //Fields
    //===========================================================

    private Image[] circleButton;

    private TextureRegion[] pTextureRegion;
    private Image selected;

    public int numberOfButtons;

    public float pButtonSize;
    public float pGroupRadius;
    public float pActualRadian = 0;

    private GameScreen game;

    //===========================================================
    //Constructors
    //===========================================================

    /**
     * @param mNumberOfButtons
     * @param mTextureRegion
     */
    public PauseButtons(GameScreen mGame, int mNumberOfButtons, float mButtonSize, float mGroupRadius, TextureRegion[] mTextureRegion) {
        game = mGame;
        numberOfButtons = mNumberOfButtons;
        pTextureRegion = mTextureRegion;
        pButtonSize = mButtonSize;
        pGroupRadius = mGroupRadius;

        circleButton = new Image[numberOfButtons];

        showButtons();
    }

    //===========================================================
    //Getter & Setter
    //===========================================================


    //===========================================================
    //Methods for/from SuperClass/Interfaces
    //===========================================================


    //===========================================================
    //Methods
    //===========================================================

    private void showButtons() {
        float actualRadian = 0;
        for (int i = 0; i < numberOfButtons; i++) {
            final int k = i;
            circleButton[i] = new Image(pTextureRegion[i]);
            circleButton[i].addListener(new InputListener() {

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    circleButton[k].setScale(1f);
                    selected = null;
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    selected = circleButton[k];
                    circleButton[k].setScale(0.85f);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (selected == null)
                        return;
                    selected.setScale(1f);
                    PauseButtons.this.onClick(k);
                }
            });
            circleButton[i].setSize(pButtonSize, pButtonSize);
            circleButton[i].setOrigin(circleButton[i].getWidth()/2, circleButton[i].getHeight()/2);
            circleButton[i].setPosition((float) Math.sin(actualRadian) * pGroupRadius - pButtonSize / 2, (float) Math.cos(actualRadian) * pGroupRadius - pButtonSize / 2);
            this.addActor(circleButton[i]);

            if (i > 0) {
                actualRadian += (360f / (numberOfButtons - 1)) * Math.PI / 180f;
            }
        }

        circleButton[0].setSize(pButtonSize * 2f, pButtonSize * 2f);
        circleButton[0].setOrigin(circleButton[0].getWidth()/2, circleButton[0].getHeight()/2);
        circleButton[0].setPosition(-circleButton[0].getWidth() / 2, -circleButton[0].getHeight() / 2);
    }

    public void updateSoundButton(){
        if(SoundManager.isMuted) {
            circleButton[3].setDrawable(new TextureRegionDrawable(Container.soundOff));
        } else {
            circleButton[3].setDrawable(new TextureRegionDrawable(Container.soundOn));
        }
    }

    private void onClick(int mButtonNumber) {
        switch (mButtonNumber) {
            case 0:
                game.getHud().hidePauseButtons();
                game.getHud().showAll();
                break;
            case 1:
                Manager.manager.changeScreen(ScreenType.CHOOSE_STAGE);
                Gdx.input.setInputProcessor(Manager.inputMultiplexer);
                break;
            case 2:
                Manager.manager.startLevel(game.getMap().getActualLevel());
                break;
            case 3:
                soundManage();
                break;
        }
    }




    /**
     * @param pDeltaTime
     * @param pDuration  of one lap
     */
    public void rotate(float pDeltaTime, float pDuration) {
        for (int i = 1; i < numberOfButtons; i++) {
            pActualRadian += pDeltaTime / pDuration;
            circleButton[i].setPosition((float) Math.sin(i * (360f / (numberOfButtons - 1)) * Math.PI / 180f + pActualRadian) * pGroupRadius - pButtonSize / 2,
                    (float) Math.cos(i * (360f / (numberOfButtons - 1)) * Math.PI / 180f + pActualRadian) * pGroupRadius - pButtonSize / 2);
        }
    }

    private void soundManage(){
        if(SoundManager.isMuted) {
            circleButton[3].setDrawable(new TextureRegionDrawable(Container.soundOn));
            SoundManager.unMute();
        } else {
            circleButton[3].setDrawable(new TextureRegionDrawable(Container.soundOff));
            SoundManager.mute();
        }
    }




    //===========================================================
    //Inner and Anonymous Classes
    //===========================================================


}

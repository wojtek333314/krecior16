package com.krecior.menu.objects;

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
import com.krecior.menu.MainScreen;
import com.krecior.menu.ScreenType;
import com.krecior.sound.SoundManager;
import com.krecior.utils.Container;

public class CircleButtons extends Group {
    private Image[] circleButton;

    private TextureRegion[] pTextureRegion;
    private Image selected;

    public int numberOfButtons;

    public float pButtonSize;
    public float pGroupRadius;
    public float pActualRadian = 0;

    /**
     * @param mNumberOfButtons
     * @param mTextureRegion
     */
    public CircleButtons(int mNumberOfButtons, float mButtonSize, float mGroupRadius, TextureRegion[] mTextureRegion) {
        numberOfButtons = mNumberOfButtons;
        pTextureRegion = mTextureRegion;
        pButtonSize = mButtonSize;
        pGroupRadius = mGroupRadius;

        circleButton = new Image[numberOfButtons];

        showButtons();
    }

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
                    CircleButtons.this.changeScreen(k);
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
        if(SoundManager.isMuted)
            circleButton[6].setDrawable(new TextureRegionDrawable(Container.soundOff));
        else
            circleButton[6].setDrawable(new TextureRegionDrawable(Container.soundOn));

        circleButton[0].setSize(pButtonSize * 2f, pButtonSize * 2f);
        circleButton[0].setOrigin(circleButton[0].getWidth()/2, circleButton[0].getHeight()/2);
        circleButton[0].setPosition(-circleButton[0].getWidth() / 2, -circleButton[0].getHeight() / 2);
    }


    private void changeScreen(int mButtonNumber) {
        switch (mButtonNumber) {
            case 0:
                MainScreen.choose.setPosition(GameScreen.W / 2, GameScreen.H / 2);
                break;
            case 1:
                Manager.manager.changeScreen(ScreenType.ITEMSHOP);
                break;
            case 3:
                Manager.manager.changeScreen(ScreenType.COINSHOP);
                break;
            case 4:
                Gdx.app.exit();
                break;
            case 6:
                soundManage();
                break;
            case 5:
                //Manager.manager.changeScreen(ScreenType.SOCIAL_NETWORK);
                Manager.facebookPlugin.getActivityListener().onRunCommand();
                break;
            case 2:
                Manager.manager.changeScreen(ScreenType.ACHIEVEMENTS);
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
        if(SoundManager.isMuted)
        {
            circleButton[6].setDrawable(new TextureRegionDrawable(Container.soundOn));
            SoundManager.unMute();
        }
        else
        {
            circleButton[6].setDrawable(new TextureRegionDrawable(Container.soundOff));
            SoundManager.mute();
        }
    }
}

package com.krecior.game.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.PowerType;
import com.krecior.utils.Container;

public class PowerIcon {
    //===========================================================
    //Constants
    //===========================================================

    public static final float SIZE = 0.13f * GameScreen.W;
    public static final float POWER_SCALE = 0.8f;
    public static final float NUMBER_LABEL_SCALE = 0.4f;

    //===========================================================
    //Fields
    //===========================================================

    private Sprite pPower;
    private Sprite pLabel;
    private Sprite pNumberLabel;

    private SpriteBatch pSpriteBatch;

    private PowerType pType;

    private TextLabel pNumber;

    private int number = 1;

    private boolean pDisappearing = false;
    private boolean pAppearing = true;
    private boolean toRemove = false;

    private float alpha = 0;

    //===========================================================
    //Constructors
    //===========================================================

    public PowerIcon(PowerType mPowerType) {
        pType = mPowerType;
        createSprite();
    }

    //===========================================================
    //Getter & Setter
    //===========================================================
    public int getNumber() {
        return number;
    }

    public PowerType getType() {
        return pType;
    }

    public float getCenterX() {
        return pLabel.getX() + pLabel.getWidth() / 2;
    }

    public float getCenterY() {
        return pLabel.getY() + pLabel.getHeight() / 2;
    }

    public float getX() { return pLabel.getX(); }

    public float getY() { return pLabel.getY(); }

    public boolean isToRemove() { return toRemove; }

    public void setPosition(float mX, float mY) {
        pLabel.setPosition(mX, mY);
        pPower.setPosition(pLabel.getX() + pLabel.getWidth() / 2 - pPower.getWidth() / 2,
                pLabel.getY() + pLabel.getHeight() / 2 - pPower.getHeight() /2);
        pNumberLabel.setPosition(pLabel.getX() + (pLabel.getWidth() - pNumberLabel.getWidth()) / 2,
                pLabel.getY() + pLabel.getHeight() - pNumberLabel.getHeight() / 2);
        pNumber.setPosition(pNumberLabel.getX() + (pNumberLabel.getWidth() - pNumber.getWidth()) / 2,
                pNumberLabel.getY() + (pNumberLabel.getHeight() + pNumber.getHeight()) / 2);
    }

    //===========================================================
    //Methods for/from SuperClass/Interfaces
    //===========================================================



    //===========================================================
    //Methods
    //===========================================================

    private void createSprite() {
        pSpriteBatch = new SpriteBatch();

        pLabel = new Sprite(Container.pEmptyButton);
        pLabel.setSize(SIZE, SIZE);
        pLabel.setOriginCenter();
        pLabel.setAlpha(0f);

        pPower = new Sprite(PowerType.getTexture(getType()));
        pPower.setSize(SIZE * POWER_SCALE, SIZE * POWER_SCALE);
        pPower.setPosition(pLabel.getX() + pLabel.getWidth() / 2 - pPower.getWidth() / 2,
                pLabel.getY() + pLabel.getHeight() / 2 - pPower.getHeight() / 2);
        pPower.setOriginCenter();
        pPower.setAlpha(0f);

        pNumberLabel = new Sprite(Container.pEmptyButton);
        pNumberLabel.setSize(SIZE * NUMBER_LABEL_SCALE, SIZE * NUMBER_LABEL_SCALE);
        pNumberLabel.setOriginCenter();
        pNumberLabel.setAlpha(0f);

        pNumber = new TextLabel(TextLabel.Font.ROBOTO, Integer.toString(number), 0, 0);

        show();
    }

    public void draw() {
        pSpriteBatch.begin();

        if(pDisappearing) {
            alpha -= Gdx.graphics.getDeltaTime();
            pLabel.setAlpha(alpha);
            pPower.setAlpha(alpha);

            if(number > 1)
                pNumberLabel.setAlpha(alpha);

            if(alpha < 0) {
                alpha = 0;
                pLabel.setAlpha(alpha);
                pPower.setAlpha(alpha);
                pNumberLabel.setAlpha(alpha);
            }
                disappear();
            }

        if(pAppearing) {
            alpha += Gdx.graphics.getDeltaTime();
            pLabel.setAlpha(alpha);
            pPower.setAlpha(alpha);

            if(number > 1)
                pNumberLabel.setAlpha(alpha);
            else
                pNumberLabel.setAlpha(0);

            if(alpha > 1f) {
                alpha = 1f;
                pLabel.setAlpha(alpha);
                pPower.setAlpha(alpha);

                if(number > 1)
                    pNumberLabel.setAlpha(alpha);
            }
            appear();
        }

        pLabel.draw(pSpriteBatch);
        pPower.draw(pSpriteBatch);

        if(number > 1 || pDisappearing) {
            pNumberLabel.draw(pSpriteBatch);
            pNumber.draw(pSpriteBatch, pNumberLabel.getColor().a);
        }

        pSpriteBatch.end();
    }

    public void showNumber() {
        alpha = 1f;
        pNumberLabel.setAlpha(alpha);
        pNumber.setColor(pNumber.getColor().r, pNumber.getColor().g, pNumber.getColor().b, alpha);
    }

    public void hideNumber() {
        pNumberLabel.setAlpha(0);
        pNumber.setColor(pNumber.getColor().r, pNumber.getColor().g, pNumber.getColor().b, 0);
    }

    public void disappear() {
        pAppearing = false;
        pDisappearing = true;

        if(alpha == 0) {
            pDisappearing = false;

            if(number <= 0)
                toRemove = true;
        }
    }

    public void appear() {
        pDisappearing = false;
        pAppearing = true;

        if(alpha == 1f)
            pAppearing = false;
    }

    public void show() {
        pDisappearing = false;
        pAppearing = true;
    }

    public void addPower() {
        number++;
        pNumber.setText(Integer.toString(number));
        showNumber();
    }

    public void decrease() {
        number--;

        pNumber.setText(Integer.toString(number));

        if(number <= 1) {
            hideNumber();
        }
    }

    public void hideAll() {
        pDisappearing = false;
        pAppearing = false;

        pLabel.setAlpha(0);
        pPower.setAlpha(0);
        alpha = 0;
        pNumberLabel.setAlpha(0);
    }

    //===========================================================
    //Inner and Anonymous Classes
    //===========================================================
}

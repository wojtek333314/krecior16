package com.krecior.menu.chooseScreen.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.krecior.Manager;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.TextLabel;

/**
 * Created by Wojciech Osak on 2015-11-07.
 */
public class LevelButton extends Actor{
    private Image background;
    private TextLabel textLabel;
    private BitmapFont bitmapFont = null;
    private int level;
    private Image[] diamonds;
    private float actualAngle = (float) (-45f * Math.PI / 180f);
    private float radius = 0;

    public LevelButton(final int level,BitmapFont bitmapFont){
        this.level = level;
        this.bitmapFont = bitmapFont;
        if(Data.isLevelUnlocked(level))
            this.background = new Image(Container.pEmptyButton);
        else
            this.background = new Image(Container.pButton[1]);
        this.background.setSize(Gdx.graphics.getWidth() * 0.22f, Gdx.graphics.getWidth() * 0.22f);
        radius = 0.4f * background.getWidth() - background.getWidth()*0.12f/2;

        textLabel = new TextLabel(Container.getFont(10),Integer.toString(level+1),2);
        diamonds = new Image[Data.getLevelRate(level)];
        float sizeOfDiamond = background.getWidth()*0.17f;

        for(int i=0; i < diamonds.length;i++){
            diamonds[i] = new Image(Container.pLittleDiamond);
            diamonds[i].setSize(sizeOfDiamond * Container.pLittleDiamond.getRegionWidth() / Container.pLittleDiamond.getRegionHeight(), sizeOfDiamond);
            diamonds[i].setOrigin(diamonds[i].getWidth() / 2, diamonds[i].getHeight() / 2);
            diamonds[i].setRotation((float) (-actualAngle * 180f / Math.PI));
            actualAngle += (float) (45f * Math.PI / 180f);
        }
        actualAngle = (float) (-45f * Math.PI / 180f);

        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("TOUCH");
                if(Data.isLevelUnlocked(level))
                    Manager.manager.startLevel(level);
                return true;
            }
        });
    }

    @Override
    public void setPosition(float x, float y) {
        background.setPosition(x,y);
        textLabel.setPosition(x+background.getWidth()/2 - textLabel.getWidth()/2
                                ,y+background.getHeight()/2 + textLabel.getHeight()/2);
        for (Image diamond : diamonds) {
            diamond.setPosition(x + background.getWidth() / 2 + (float) Math.sin(actualAngle) * radius - diamond.getWidth() / 2,
                    y + background.getHeight() / 2 + (float) Math.cos(actualAngle) * radius - diamond.getHeight() / 2);
            actualAngle += (float) (45f * Math.PI / 180f);
        }
        actualAngle = (float) (-45f * Math.PI / 180f);
    }

    @Override
    public float getX() {
        return background.getX();
    }

    @Override
    public float getY() {
        return background.getY();
    }

    @Override
    public float getWidth() {
        return background.getWidth();
    }

    @Override
    public float getHeight() {
        return background.getHeight();
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setBounds(getX(), getY(), getWidth(), getHeight());
        background.draw(batch, parentAlpha);
        if(Data.isLevelUnlocked(level))
        {
            textLabel.draw(batch,parentAlpha);
            for (Image diamond : diamonds)
                diamond.draw(batch,parentAlpha);
        }

    }
}

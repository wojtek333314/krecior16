package com.krecior.menu.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.krecior.Manager;
import com.krecior.menu.achievements.system.Achievement;
import com.krecior.utils.Container;
import com.krecior.utils.TextLabel;

/**
 * Created by Wojciech Osak on 2015-10-07.
 */
public class AchievementItem extends Actor{
    private TextLabel label;
    private Image background;
    private Image diamonds[];
    private Achievement achievementType;

    public AchievementItem(Achievement achievementType,float y) {
        this.achievementType = achievementType;

        defineImages();
        defineLabel();
        setPosition(Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2, y);
    }

    private void defineImages(){
        TextureRegion backgroundTexture = Container.getTextureRegion("gfx/msgBox/score_label.png");
        background = new Image(backgroundTexture);
        background.setSize(Gdx.app.getGraphics().getWidth() * 0.65f, Gdx.app.getGraphics().getWidth() * 0.325f * 0.59f);

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("tap core");
              //  Manager.facebookPlugin.share();
                return true;
            }
        });

        if(Achievement.isReached(achievementType))
        {
            diamonds = new Image[4];
            TextureRegion diamondsTexRegion = Container.getTextureRegion("gfx/itemShop/icon_diamond.png");
            for(int i=0;i<diamonds.length;i++)
            {
                diamonds[i] = new Image(diamondsTexRegion);
                diamonds[i].setSize(background.getWidth() / 8, background.getWidth() / 8);
            }
        }
    }

    private void setDiamondsPosition(){
        diamonds[0].setPosition(background.getX(),background.getY());
        diamonds[1].setPosition(background.getX() + background.getWidth() - diamonds[1].getWidth()
                , background.getY());
        diamonds[2].setPosition(background.getX()
                ,background.getY()+background.getHeight()-diamonds[2].getHeight());
        diamonds[3].setPosition(background.getX() + background.getWidth() - diamonds[3].getWidth()
                , background.getY() + background.getHeight() - diamonds[3].getHeight());
    }

    private void defineLabel(){
        label = new TextLabel(TextLabel.Font.ROBOTO,Achievement.getDescription(achievementType));
        label.setAlign(Align.center);
    }

    @Override
    public void setPosition(float x,float y){
        background.setPosition(x, y);
        label.setPosition(x + background.getWidth() / 2
                ,background.getY() + background.getHeight()/2 + label.getHeight()/2);
        if(Achievement.isReached(achievementType))
            setDiamondsPosition();

        super.setPosition(x, y);
    }



    @Override
    public float getHeight() {
        return background.getHeight();
    }

    @Override
    public float getWidth() {
        return background.getWidth();
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
    public void setOrigin(float originX, float originY) {
        super.setOrigin(originX, originY);
        background.setOrigin(originX, originY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        background.setScale(scaleX, scaleY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setBounds(getX(), getY(), getWidth(), getHeight());
        background.draw(batch,parentAlpha);
        label.draw(batch,parentAlpha);

        if(Achievement.isReached(achievementType))
            for(Image diamond : diamonds)
                diamond.draw(batch,parentAlpha);
    }
}

package com.krecior.menu.coinShop.coinItem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.menu.coinShop.coinItem.enums.CoinItemType;
import com.krecior.utils.Container;
import com.krecior.utils.TextLabel;

/**
 * Created by Wojciech Osak on 2015-09-24.
 */
public class CoinItem extends Actor{
    public static float size = Gdx.graphics.getWidth() * 0.32f;
    private Image background;
    private Image diamonds;
    private CoinItemType itemType;
    private String  description;
    private TextLabel textLabel;

    public CoinItem(CoinItemType itemType){
        this.itemType = itemType;
        description = CoinItemType.getDescription(itemType);
        textLabel = new TextLabel(TextLabel.Font.ROBOTO,description,1f);
        textLabel.setOrigin(textLabel.getWidth()/2,textLabel.getHeight()/2);
        defineTextureRegionAndSprite();
        addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!onTouch())
                {
                    buy();
                    CoinItem.this.setScale(1f,1f);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                CoinItem.this.setScale(1f,1f);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setScale(0.88f,0.88f);
                return true;
            }
        });
    }



    public boolean onTouch(){ return false;}//to override

    void defineTextureRegionAndSprite(){
        Texture t;
        //background:
        t = new Texture(Gdx.files.internal("gfx/empty_button.png"));
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion backgroundTextureRegion = new TextureRegion(t, 0, 0, 250, 250);
        background = new Image(backgroundTextureRegion);
        background.setOrigin(0, 0);
        background.setSize(size, size);
        //diamond:
        switch (itemType){
            case COINS_12000:
                diamonds = new Image(Container.getTextureRegion("gfx/coinShop/diamond3.png"));
                break;
            case COINS_3000:
                diamonds = new Image(Container.getTextureRegion("gfx/coinShop/diamond1.png"));
                break;
            case COINS_6000:
                diamonds = new Image(Container.getTextureRegion("gfx/coinShop/diamond2.png"));
                break;
        }
        diamonds.setSize(size*0.5f,size*0.5f);
        diamonds.setOrigin(diamonds.getWidth()/2,diamonds.getHeight()/2);
    }

    public String getText() {
        return CoinItemType.getDescription(itemType);
    }

    public void buy(){
    }

    @Override
    public void setPosition(float x,float y){
        background.setPosition(x, y);
        diamonds.setPosition(x+background.getWidth()/2 - diamonds.getWidth()/2
                                ,y+background.getHeight()/2 - diamonds.getHeight()/4);
        textLabel.setPosition(x+background.getWidth()/2 - textLabel.getWidth()/2
                ,diamonds.getY());
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

    public CoinItemType getItemType() {
        return itemType;
    }


    @Override
    public float getX() {
        return background.getX();
    }

    @Override
    public float getY() {
        return background.getY();
    }

    public String getDescription() {
        return description;
    }

    public TextLabel getTextLabel() {
        return textLabel;
    }

    @Override
    public void setOrigin(float originX, float originY) {
        super.setOrigin(originX, originY);
        background.setOrigin(originX, originY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        background.setScale(scaleX,scaleY);
        diamonds.setScale(scaleX,scaleY);
        textLabel.setOrigin(textLabel.getWidth() / 2, textLabel.getHeight()/2);
        textLabel.setScale(scaleX,scaleY);
        textLabel.setPosition(background.getX() + background.getWidth()/2 - textLabel.getWidth()/2
                ,diamonds.getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setBounds(getX(), getY(), getWidth(), getHeight());
        background.draw(batch,parentAlpha);
        diamonds.draw(batch,parentAlpha);
        textLabel.draw(batch,parentAlpha);
    }
}
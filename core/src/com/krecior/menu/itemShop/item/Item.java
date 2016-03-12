package com.krecior.menu.itemShop.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.krecior.game.enums.PowerType;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.TextLabel;

/**
 * Created by Wojciech Osak on 2015-09-16.
 */
public class Item extends Actor {
    public static float size = Gdx.graphics.getWidth() * 0.23f;

    private Image sprite;
    private Image background;
    private Image diamond;
    private PowerType itemType;
    private TextLabel   textLabel;
    private String description;
    private String textureFileName[] ={ "gfx/itemShop/power_icon_water.png",
                                        "gfx/itemShop/power_icon_fire.png",
                                        "gfx/itemShop/power_icon_hammer.png",
                                        "gfx/itemShop/power_icon_lightbolt.png",
                                        "gfx/itemShop/power_icon_poison.png",
                                        "gfx/itemShop/power_icon_screwdiver.png",
                                        "gfx/itemShop/power_icon_eraser.png",
                                        "gfx/itemShop/power_icon_pistol.png"};
    private boolean isBuyed = false;
    private int cost = -1;
    private InputListener inputListener;

    public Item(PowerType itemType){
        this.itemType = itemType;
        cost = PowerType.getCost(itemType);
        textLabel = new TextLabel(Container.getFont(10),Integer.toString(cost));
        isBuyed = Data.isPowerAvaliable(getItemType());
        description = PowerType.getDescription(itemType);

        defineTextureRegionAndSprite();
        addListener(inputListener = new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    buy();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Item.this.setScale(1f);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Item.this.setScale(0.85f);
                return true;
            }
        });
    }



    void defineTextureRegionAndSprite(){
        TextureRegion t;
        //background:
        if(isBuyed)
            t = Container.getTextureRegion("gfx/empty_button.png");
        else
            t = Container.getTextureRegion("gfx/empty_button_shadow.png");

        background = new Image(t);
        background.setOrigin(0, 0);
        background.setSize(size,size);
        //diamond:
        t = Container.getTextureRegion("gfx/itemShop/icon_diamond.png");
        diamond = new Image(t);
        diamond.setOrigin(0,0);
        diamond.setSize(size,size);

        //item:
        switch(itemType){
            case ERASE:
                t = Container.getTextureRegion(getTexturePath(textureFileName[6]));
                break;
            case FIRE:
                t = Container.getTextureRegion(getTexturePath(textureFileName[1]));
                break;
            case GUN:
                t = Container.getTextureRegion(getTexturePath(textureFileName[7]));
                break;
            case HAMMER:
                t = Container.getTextureRegion(getTexturePath(textureFileName[2]));
                break;
            case LIGHTBOLT:
                t = Container.getTextureRegion(getTexturePath(textureFileName[3]));
                break;
            case POISON:
                t = Container.getTextureRegion(getTexturePath(textureFileName[4]));
                break;
            case SCREWDIVER:
                t = Container.getTextureRegion(getTexturePath(textureFileName[5]));
                break;
            case WATER:
                t = Container.getTextureRegion(getTexturePath(textureFileName[0]));
                break;
        }

        sprite = new Image(t);
        sprite.setOrigin(0, 0);
        sprite.setSize(size, size);

        if(itemType==PowerType.GUN || itemType==PowerType.HAMMER || itemType==PowerType.SCREWDIVER || itemType==PowerType.POISON)
        {
            sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
            sprite.setRotation(45);
        }
    }

    private void refreshItemAfterBuy(){
        TextureRegion textureRegion = Container.getTextureRegion("gfx/empty_button.png");
        background.setDrawable(new TextureRegionDrawable(textureRegion));
        background.setOrigin(background.getWidth()/2,background.getHeight()/2);
        background.setSize(size, size);

        switch(itemType){
            case ERASE:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[6]));
                break;
            case FIRE:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[1]));
                break;
            case GUN:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[7]));
                break;
            case HAMMER:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[2]));
                break;
            case LIGHTBOLT:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[3]));
                break;
            case POISON:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[4]));
                break;
            case SCREWDIVER:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[5]));
                break;
            case WATER:
                textureRegion = Container.getTextureRegion(getTexturePath(textureFileName[0]));
                break;
        }
        sprite.setDrawable(new TextureRegionDrawable(textureRegion));
        sprite.setSize(size, size);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight()/2);

        if(itemType==PowerType.GUN || itemType==PowerType.HAMMER || itemType==PowerType.SCREWDIVER || itemType==PowerType.POISON)
        {
            sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
            sprite.setRotation(45);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setIsBuyed(boolean isBuyed) {
        Data.setItemBuyed(getItemType());
        this.isBuyed = isBuyed;
        refreshItemAfterBuy();
    }

    @Override
    public void setOrigin(float originX, float originY) {
        super.setOrigin(originX, originY);
        sprite.setOrigin(originX, originY);
        background.setOrigin(originX, originY);
        diamond.setOrigin(originX, originY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        sprite.setScale(scaleX, scaleY);
        background.setScale(scaleX, scaleY);
        diamond.setScale(scaleX, scaleY);
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        sprite.setScale(scaleXY);
        background.setScale(scaleXY);
        diamond.setScale(scaleXY);
    }

    public boolean isBuyed() {
        return isBuyed;
    }

    public void buy(){

    }

    public TextLabel getTextLabel() {
        return textLabel;
    }

    @Override
    public void setPosition(float x,float y){
        background.setPosition(x,y);
        sprite.setPosition(x, y);
        diamond.setPosition(x, y + diamond.getHeight() / 9);
        textLabel.setPosition(x+getWidth()/2 - textLabel.getWidth()/2
                            ,y + textLabel.getHeight()*2);
        super.setPosition(x,y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setBounds(getX(), getY(), getWidth(), getHeight());

        background.draw(batch, parentAlpha);
        sprite.draw(batch,parentAlpha);
        if(!isBuyed)
            diamond.draw(batch,parentAlpha);
        textLabel.draw(batch,parentAlpha);
    }

    @Override
    public float getHeight() {
        return sprite.getHeight();
    }

    @Override
    public float getWidth() {
        return sprite.getWidth();
    }

    public PowerType getItemType() {
        return itemType;
    }


    @Override
    public float getX() {
        return sprite.getX();
    }

    @Override
    public float getY() {
        return sprite.getY();
    }

    public int getCost() {
        return cost;
    }

    private String getTexturePath(String path){
        if(isBuyed)
            return path;
        else
            return path.substring(0,path.length()-4)+"_bl.png";
    }

    public Image getBackground() {
        return background;
    }
}

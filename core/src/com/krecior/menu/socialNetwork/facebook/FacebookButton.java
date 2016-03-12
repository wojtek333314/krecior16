package com.krecior.menu.socialNetwork.facebook;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.krecior.Manager;
import com.krecior.menu.objects.MenuMsgBox;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.TextLabel;

/**
 * Created by Wojciech Osak on 2015-10-20.
 */
public class FacebookButton extends Actor {
    private TextLabel label;
    private Image diamond,image;
    private FacebookButtonType facebookButtonType;
    int W = Gdx.app.getGraphics().getWidth();

    public FacebookButton(final FacebookButtonType facebookButtonType){
        this.facebookButtonType = facebookButtonType;

        boolean isDone = facebookButtonType==FacebookButtonType.LIKE ? Data.facebookIsLiked()
                : Data.facebookIsShared();//czy byl juz klikniety kiedy like lub share


        if(isDone){
            String text = facebookButtonType==FacebookButtonType.LIKE ? "Thanks for like!" : "Thanks for sharing!";
            label = new TextLabel(Container.getFont(10),text);
        }else
        {
            diamond = new Image((Container.getTextureRegion("gfx/itemShop/icon_diamond.png")));
            diamond.setSize(W * 0.1f, W * 0.1f);

            label = new TextLabel(Container.getFont(10),facebookButtonType==FacebookButtonType.SHARE ? "+500" : "+100");
            image = facebookButtonType==FacebookButtonType.LIKE ? new Image(Container.getTextureRegion("gfx/socialNetwork/like_fb.png")) :
                    new Image(Container.getTextureRegion("gfx/socialNetwork/share_fb.png"));
            image.setSize(W * 0.36f, W * 0.09f);

            addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(!Manager.facebookPlugin.isConnectionAvaliable()){
                        final MenuMsgBox info = new MenuMsgBox(getStage(),"No network connection!","OK");
                        info.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                            @Override
                            public void onClick(int button) {
                                info.hide();
                            }
                        });
                        info.show();
                        return true;
                    }
                    if (facebookButtonType == FacebookButtonType.SHARE)
                        Manager.facebookPlugin.share();
                    if (facebookButtonType == FacebookButtonType.LIKE)
                        Manager.facebookPlugin.like();
                    return true;
                }
            });
        }
    }


    public void setDone(){
        if(facebookButtonType==FacebookButtonType.LIKE)
        {
            Data.facebookSetLiked();
        }
        if(facebookButtonType==FacebookButtonType.SHARE)
        {
            Data.facebookSetShared();
        }

        float x = getX(),y = getY();
        image.remove();
        diamond.remove();
        image = null;
        diamond = null;

        String text = facebookButtonType==FacebookButtonType.LIKE ? "You liked this app!" : "You shared this app!";
        label.setText(text);
        setPosition(x, y);
    }

    @Override
    public void setPosition(float x, float y) {
        if(image!=null){
            image.setPosition(x,y);
            label.setPosition(image.getX()+image.getWidth(),image.getY() + image.getHeight()/2 + label.getHeight()/2);
            diamond.setPosition(label.getX() + label.getWidth(), label.getY() - diamond.getHeight()/2 - label.getHeight()/2);
        }else
            label.setPosition(x,y + label.getHeight()*2f);
    }

    @Override
    public boolean remove() {
        if (image != null) {
            diamond.remove();
            image.remove();
        }
        label.remove();
        return super.remove();
    }

    @Override
    public float getX() {
        return image!=null ? image.getX() : label.getX();
    }

    @Override
    public float getY() {
        return image!=null ? image.getY() : label.getY();
    }

    @Override
    public float getWidth() {
        return image!=null ? image.getWidth() : label.getWidth();
    }

    @Override
    public float getHeight() {
        return image!=null ? image.getHeight() : label.getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setBounds(getX(), getY(), getWidth(), getHeight());
        if(image!=null){
            image.draw(batch,parentAlpha);
            diamond.draw(batch,parentAlpha);
        }
        label.draw(batch,parentAlpha);
    }

    public enum FacebookButtonType{
        LIKE,
        SHARE
    }
}

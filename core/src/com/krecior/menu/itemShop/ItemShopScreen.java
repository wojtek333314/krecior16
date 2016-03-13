package com.krecior.menu.itemShop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.krecior.Manager;
import com.krecior.game.enums.PowerType;
import com.krecior.menu.ScreenType;
import com.krecior.menu.itemShop.item.Item;
import com.krecior.menu.objects.MenuMsgBox;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.TextLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wojciech Osak on 2015-09-16.
 */
public class ItemShopScreen extends Stage implements Screen {
    private static final float BACK_BUTTON_SIZE = Gdx.graphics.getWidth() * 0.2f;
    private static final float TOP_ICON_SIZE = Gdx.graphics.getWidth() * 0.2f;

    private Image pBackground;
    private Image pBackgroundClouds;
    private Image pBackgroundBorder;
    private Image topIcon;
    private Image coinIcon;
    private Stage mainStage;
    private Image pBackButton;

    private SpriteBatch screenBatch;
    private TextLabel coinsLabel;
    private List<Item> itemList = new ArrayList<Item>();

    private int W;
    private int H;

    @Override
    public void show() {
        W = Gdx.graphics.getWidth();
        H = Gdx.graphics.getHeight();

        mainStage = new Stage();
        screenBatch = new SpriteBatch();
        coinsLabel = new TextLabel(Container.getFont(10), Integer.toString(Data.getDiamonds()));


        showBackground();
        showStaticButtons();
        showTextFields();
        showItems();
        showTopIconAndWallet();

        Manager.inputMultiplexer.addProcessor(mainStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(58 / 256f, 136 / 256f, 231 / 256f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.act(delta);
        mainStage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void showItems(){
        PowerType values[] = PowerType.getValuesForItemShop();
        float x = 0, y = 0;


        for (PowerType value : values)
        {
            Item tmp = new Item(value){
                @Override
                public void buy() {
                    if(isBuyed()){
                        final MenuMsgBox msgBox = new MenuMsgBox(ItemShopScreen.this.mainStage,getDescription(),"OK");
                        msgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                            @Override
                            public void onClick(int button) {
                                msgBox.hide();
                            }
                        });
                        msgBox.show();
                        return;
                    }

                    final MenuMsgBox msgBox = new MenuMsgBox(ItemShopScreen.this.mainStage,getDescription()+"\n"+"Would you buy it?","Cancel","Buy");
                    msgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                        @Override
                        public void onClick(int button) {
                            if(button==0)//left button
                                msgBox.hide();
                            if(button==1){//right button

                                if(Data.getDiamonds()  < getCost()){//not enough coins
                                    msgBox.setDescription("Not enough diamonds\nWould you like buy more?");
                                    msgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                                        @Override
                                        public void onClick(int button) {
                                            if(button==0)
                                                msgBox.hide();
                                            else
                                            {
                                                msgBox.hide();
                                                Gdx.app.postRunnable(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Manager.manager.changeScreen(ScreenType.COINSHOP);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else{//BUY ITEM ACTION
                                    Data.addDiamonds(-getCost());

                                    coinsLabel.setText(Integer.toString(Data.getDiamonds()));
                                    coinsLabel.setPosition(coinIcon.getX() - coinsLabel.getWidth()
                                            , coinIcon.getY()+coinIcon.getHeight()/2 + coinsLabel.getHeight()/2);

                                    msgBox.hide();
                                    setIsBuyed(true);
                                }
                            }
                        }
                    });
                    msgBox.show();
                }
            };
            mainStage.addActor(tmp);
            itemList.add(tmp);

            if(x==0)
                tmp.setPosition(Gdx.app.getGraphics().getWidth()/2 - tmp.getWidth() * 1.45f
                            ,Gdx.app.getGraphics().getHeight()*0.9f - ((y+1) * tmp.getHeight() * 1.2f));
            else
                tmp.setPosition(Gdx.app.getGraphics().getWidth()/2 + tmp.getWidth() * 0.45f
                            ,Gdx.app.getGraphics().getHeight()*0.9f - ((y+1) * tmp.getHeight() * 1.2f));
            tmp.setOrigin(tmp.getWidth()/2,tmp.getHeight()/2);

            x+=1;
            if(x==2){
                y++;
                x = 0;
            }
        }
    }


    private void showBackground() {
        pBackground = new Image(Container.pLandBlur);
        pBackground.setSize(W, W * Container.pLandBlur.getRegionHeight() / Container.pLandBlur.getRegionWidth());
        pBackground.setOrigin(0, 0);
        pBackground.setPosition(0, 0);

        pBackgroundClouds = new Image(Container.pCloudsBlur);
        pBackgroundClouds.setSize(W, W * Container.pCloudsBlur.getRegionHeight() / Container.pCloudsBlur.getRegionWidth());
        pBackgroundClouds.setOrigin(0, 0);
        pBackgroundClouds.setPosition(pBackground.getX(), pBackground.getY() + pBackground.getHeight());

        pBackgroundBorder = new Image(Container.pBorder);
        pBackgroundBorder.setSize(W, H);
        pBackgroundBorder.setOrigin(0, 0);
        pBackgroundBorder.setPosition(0, 0);

        mainStage.addActor(pBackground);
        mainStage.addActor(pBackgroundClouds);
        mainStage.addActor(pBackgroundBorder);
    }

    private void showStaticButtons() {
        pBackButton = new Image(Container.pBackButton);
        pBackButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
                return true;
            }
        });
        pBackButton.setSize(BACK_BUTTON_SIZE, BACK_BUTTON_SIZE);

        mainStage.addActor(pBackButton);
    }

    private void showTextFields() {
         mainStage.addActor(coinsLabel);
        for (Item item : itemList){
            mainStage.addActor(item.getTextLabel());
        }
    }

    private void showTopIconAndWallet(){
        Texture t = new Texture(Gdx.files.internal("gfx/itemShop/unlock_top_icon.png"));
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(t, 0, 0, 250, 125);
        topIcon = new Image(textureRegion);
        topIcon.setOrigin(0, 0);
        topIcon.setSize(TOP_ICON_SIZE * 2, TOP_ICON_SIZE);
        topIcon.setPosition(Gdx.app.getGraphics().getWidth() / 2 - topIcon.getWidth() / 2, Gdx.app.getGraphics().getHeight() - topIcon.getHeight());

        t = new Texture(Gdx.files.internal("gfx/itemShop/icon_diamond.png"));
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textureRegion = new TextureRegion(t, 0, 0, 256, 256);

        coinIcon = new Image(textureRegion);
        coinIcon.setOrigin(0, 0);
        coinIcon.setSize(W * 0.25f, W * 0.25f);
        coinIcon.setPosition(Gdx.app.getGraphics().getWidth() - coinIcon.getWidth() * 1.05f, -coinIcon.getHeight() / 4);

        coinsLabel.setPosition(coinIcon.getX() - coinsLabel.getWidth(), coinIcon.getY()+coinIcon.getHeight()/2 + coinsLabel.getHeight()/2);

        mainStage.addActor(coinIcon);
        mainStage.addActor(topIcon);

    }
}

package com.krecior.menu.coinShop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.krecior.BaseScreen;
import com.krecior.Manager;
import com.krecior.menu.ScreenType;
import com.krecior.menu.coinShop.coinItem.CoinItem;
import com.krecior.menu.coinShop.coinItem.enums.CoinItemType;
import com.krecior.menu.objects.MenuMsgBox;
import com.krecior.utils.Container;

/**
 * Created by Wojciech Osak on 2015-09-24.
 */
public class CoinShopScreen  extends BaseScreen {
    private static final float BACK_BUTTON_SIZE = Gdx.graphics.getWidth() * 0.2f;
    private static final float TOP_ICON_SIZE = Gdx.graphics.getWidth() * 0.2f;

    private Stage  mainStage;
    private Image topIcon;

    private int W;
    private int H;

    public CoinShopScreen() {
        mainStage = new Stage();
        setMainStage(mainStage);
    }

    @Override
    public void show() {
        W = Gdx.graphics.getWidth();
        H = Gdx.graphics.getHeight();

        showBackground();
        showStaticButtons();
        showTopIcon();
        showItems();
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
        final CoinItem[] coinItems = new CoinItem[CoinItemType.values().length];
        final MenuMsgBox menuMsgBox = new MenuMsgBox(this.mainStage,"Descriptiooooon","Cancel","Ok");
        menuMsgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
            @Override
            public void onClick(int button) {
                menuMsgBox.hide();
            }
        });

        for(int i=0;i< coinItems.length;i++){
            final int finalI = i;
            coinItems[i] = new CoinItem(CoinItemType.values()[finalI]){
                @Override
                public boolean onTouch() {
                    //menuMsgBox.show();
                    switch(coinItems[finalI].getItemType()){
                        case COINS_6000:
                            Manager.getPlatformResolver().requestPurchase(Manager.diamonds2);
                            break;
                        case COINS_3000:
                            Manager.getPlatformResolver().requestPurchase(Manager.diamonds1);
                            break;
                        case COINS_12000:
                            Manager.getPlatformResolver().requestPurchase(Manager.diamonds3);
                            break;
                    }

                    return true;
                }
            };
            coinItems[i].setOrigin(coinItems[i].getWidth()/2, coinItems[i].getHeight()/2);
            coinItems[i].setPosition(Gdx.app.getGraphics().getWidth() / 2 - coinItems[i].getWidth() / 2
                    , topIcon.getY() - topIcon.getHeight() * 1.9f - i * coinItems[i].getHeight() * 1.2f);

            mainStage.addActor(coinItems[i]);
        }
    }

    private void showBackground() {
        Image pBackground = new Image(Container.pLandBlur);
        pBackground.setSize(W, W * Container.pLandBlur.getRegionHeight() / Container.pLandBlur.getRegionWidth());
        pBackground.setOrigin(0, 0);
        pBackground.setPosition(0, 0);

        Image pBackgroundClouds = new Image(Container.pCloudsBlur);
        pBackgroundClouds.setSize(W, W * Container.pCloudsBlur.getRegionHeight() / Container.pCloudsBlur.getRegionWidth());
        pBackgroundClouds.setOrigin(0, 0);
        pBackgroundClouds.setPosition(pBackground.getX(), pBackground.getY() + pBackground.getHeight());

        Image pBackgroundBorder = new Image(Container.pBorder);
        pBackgroundBorder.setSize(W, H);
        pBackgroundBorder.setOrigin(0, 0);
        pBackgroundBorder.setPosition(0, 0);

        mainStage.addActor(pBackground);
        mainStage.addActor(pBackgroundBorder);
        mainStage.addActor(pBackgroundClouds);
    }

    private void showStaticButtons() {
        Image backButton = new Image(Container.pBackButton);
        backButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
                return true;
            }
        });
        backButton.setSize(BACK_BUTTON_SIZE, BACK_BUTTON_SIZE);
        backButton.setPosition(0.025f * W, 0.025f * W);

        mainStage.addActor(backButton);
    }



    private void showTopIcon(){
        Texture t = new Texture(Gdx.files.internal("gfx/coinShop/shop_top_icon.png"));
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(t, 0, 0, 250, 125);
        topIcon = new Image(textureRegion);
        topIcon.setOrigin(0, 0);
        topIcon.setSize(TOP_ICON_SIZE * 2, TOP_ICON_SIZE);
        topIcon.setPosition(Gdx.app.getGraphics().getWidth() / 2 - topIcon.getWidth() / 2, Gdx.app.getGraphics().getHeight() - topIcon.getHeight());

        mainStage.addActor(topIcon);
    }

}
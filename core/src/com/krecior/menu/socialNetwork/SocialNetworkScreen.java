package com.krecior.menu.socialNetwork;

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
import com.krecior.menu.socialNetwork.facebook.FacebookButton;
import com.krecior.menu.socialNetwork.facebook.FacebookPluginListener;
import com.krecior.utils.Container;
import com.krecior.utils.Data;


/**
 * Created by Wojciech Osak on 2015-09-30.
 */
public class SocialNetworkScreen  extends BaseScreen {
    private static final float BACK_BUTTON_SIZE = Gdx.graphics.getWidth() * 0.2f;
    private static final float TOP_ICON_SIZE = Gdx.graphics.getWidth() * 0.2f;

    private Stage  mainStage;
    private Image topIcon;
    private FacebookButton fbLike,fbShare;

    private int W;
    private int H;

    public SocialNetworkScreen() {
        mainStage = new Stage();
        setMainStage(mainStage);
    }

    @Override
    public void show() {
        W = Gdx.graphics.getWidth();
        H = Gdx.graphics.getHeight();
        createFacebookListeners();

        showBackground();
        showStaticButtons();
        showTopIcon();
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


    private void createFacebookListeners(){
        Manager.facebookPlugin.setFacebookShareListener(new FacebookPluginListener.FacebookShareListener() {
            @Override
            public void onShareSuccess() {
                Data.facebookSetShared();
                fbShare.setDone();
            }
        });

        Manager.facebookPlugin.setFacebookLikeListener(new FacebookPluginListener.FacebookLikeListener() {
            @Override
            public void onLikeSuccess() {
                Data.facebookSetLiked();
                fbLike.setDone();
            }
        });
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

        Image fb_icon = new Image(Container.getTextureRegion("gfx/socialNetwork/icon_fb.png"));
        fb_icon.setSize(W * 0.25f, W * 0.25f);
        fb_icon.setPosition(W * 0.1f, H * 0.35f);

        Image tw_icon = new Image(Container.getTextureRegion("gfx/socialNetwork/twitter_icon.png"));
        tw_icon.setSize(W * 0.25f, W * 0.25f);
        tw_icon.setPosition(W * 0.1f, H * 0.2f);


        fbLike = new FacebookButton(FacebookButton.FacebookButtonType.LIKE);
        fbShare = new FacebookButton(FacebookButton.FacebookButtonType.SHARE);

        fbLike.setPosition(fb_icon.getX()+fb_icon.getWidth(),fb_icon.getY()+fb_icon.getHeight()-fbLike.getHeight());
        fbShare.setPosition(fb_icon.getX()+fb_icon.getWidth(),fb_icon.getY());

        mainStage.addActor(pBackground);
        mainStage.addActor(pBackgroundBorder);
        mainStage.addActor(pBackgroundClouds);
       // mainStage.addActor(tw_icon);
        mainStage.addActor(fb_icon);
        mainStage.addActor(fbLike);
        mainStage.addActor(fbShare);
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
        Texture t = new Texture(Gdx.files.internal("gfx/socialNetwork/socialnetwork_topicon.png"));
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(t, 0, 0, 250, 125);
        topIcon = new Image(textureRegion);
        topIcon.setOrigin(0, 0);
        topIcon.setSize(TOP_ICON_SIZE * 2, TOP_ICON_SIZE);
        topIcon.setPosition(Gdx.app.getGraphics().getWidth() / 2 - topIcon.getWidth() / 2, Gdx.app.getGraphics().getHeight() - topIcon.getHeight());

        mainStage.addActor(topIcon);
    }

}
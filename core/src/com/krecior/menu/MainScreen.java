package com.krecior.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.krecior.BaseScreen;
import com.krecior.Manager;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.PowerType;
import com.krecior.menu.objects.CircleButtons;
import com.krecior.menu.objects.MenuMsgBox;
import com.krecior.menu.socialNetwork.ChooseGameMode;
import com.krecior.sound.SoundManager;
import com.krecior.utils.Container;
import com.krecior.utils.Data;

public class MainScreen extends BaseScreen {
	private final static int BUTTONS = 7;
    private Stage mainStage;
	private CircleButtons pCircleButtons;
    public static ChooseGameMode choose;

    private Image pLogo;

	private int W;
	private int H;

	private float pLogoSize;
    private boolean pause = false;

    public MainScreen() {
        mainStage = new Stage();
        setMainStage(mainStage);
        if(!SoundManager.mainSound.isPlaying())
            SoundManager.play(SoundManager.mainSound);

        Manager.inputMultiplexer.addProcessor(mainStage);
    }

	private void showBackground() {
        Image pBackground = new Image(Container.pLandBlur);
		pBackground.setSize(W, W * Container.pLandBlur.getRegionHeight() / Container.pLandBlur.getRegionWidth());
		pBackground.setOrigin(0, 0);
		pBackground.setPosition(0, 0);

        Image pBackgroundClouds = new Image(Container.pCloudsBlur);
		pBackgroundClouds.setSize(W, W * Container.pCloudsBlur.getRegionHeight() / Container.pCloudsBlur.getRegionWidth());
		pBackgroundClouds.setPosition(pBackground.getX(), pBackground.getY() + pBackground.getHeight());

        Image pBackgroundBorder = new Image(Container.pBorder);
		pBackgroundBorder.setSize(W, H);
		pBackgroundBorder.setPosition(0, 0);
        pBackgroundBorder.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                choose.setPosition(GameScreen.W * 2, GameScreen.H * 2);
                return false;
            }
        });

		pLogo = new Image(Container.pMolesWarLogo);
		pLogo.setSize(pLogoSize, pLogoSize * Container.pMolesWarLogo.getRegionHeight() / Container.pMolesWarLogo.getRegionWidth());

        mainStage.addActor(pBackground);
        mainStage.addActor(pBackgroundClouds);
        mainStage.addActor(pBackgroundBorder);
        mainStage.addActor(pLogo);
        Gdx.input.setCatchBackKey(true);
	}


	private void showButtons() {
		pCircleButtons = new CircleButtons(BUTTONS, 0.2f * W, 0.3f * W, Container.pButton);
		pCircleButtons.setPosition(W / 2, H / 2);

        mainStage.addActor(pCircleButtons);

        if (Manager.DEVELOPER_VERSION) {
            Image redownload = new Image(Container.getTextureRegion("gfx/redownload.png"));
            redownload.setSize(W * 0.25f, W * 0.125f);
            redownload.setPosition(0, H - redownload.getHeight());
            redownload.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Manager.levelDownloadListener.reDownload();
                    final MenuMsgBox menuMsgBox = new MenuMsgBox(mainStage,"REDOWNLOADED MAPS ","OK");
                    menuMsgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                        @Override
                        public void onClick(int button) {
                            menuMsgBox.hide();
                        }
                    });
                    menuMsgBox.show();
                    return true;
                }
            });
            mainStage.addActor(redownload);

            Image clearpowers = new Image(Container.getTextureRegion("gfx/clearpowers.png"));
            clearpowers.setSize(W * 0.25f, W * 0.125f);
            clearpowers.setPosition(W / 2 - clearpowers.getWidth()/2, H - clearpowers.getHeight());
            clearpowers.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    for (PowerType powerType : PowerType.values())
                        Data.setItemNOTBuyed(powerType);

                    final MenuMsgBox menuMsgBox = new MenuMsgBox(mainStage,"POWERS CLEARED","OK");
                    menuMsgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                        @Override
                        public void onClick(int button) {
                            menuMsgBox.hide();
                        }
                    });
                    menuMsgBox.show();
                    return true;
                }
            });
            mainStage.addActor(clearpowers);

            Image clearlvls = new Image(Container.getTextureRegion("gfx/clearlvls.png"));
            clearlvls.setSize(W * 0.25f, W * 0.125f);
            clearlvls.setPosition(W - clearlvls.getWidth(), H - clearlvls.getHeight());
            clearlvls.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    for (int i = 1; i < 46; i++)
                        Data.setLevelLocked(i);

                    final MenuMsgBox menuMsgBox = new MenuMsgBox(mainStage,"LEVELS CLEARED","OK");
                    menuMsgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                        @Override
                        public void onClick(int button) {
                            menuMsgBox.hide();
                        }
                    });
                    menuMsgBox.show();
                    return true;
                }
            });
            mainStage.addActor(clearlvls);

            Image adddiamonds = new Image(Container.getTextureRegion("gfx/adddiamonds.png"));
            adddiamonds.setSize(W * 0.25f, W * 0.125f);
            adddiamonds.setPosition(W/2 - adddiamonds.getWidth()/2, 0);
            adddiamonds.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Data.addDiamonds(1000);

                    final MenuMsgBox menuMsgBox = new MenuMsgBox(mainStage,"DIAMONDS ADDED","OK");
                    menuMsgBox.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                        @Override
                        public void onClick(int button) {
                            menuMsgBox.hide();
                        }
                    });
                    menuMsgBox.show();
                    return true;
                }
            });
            mainStage.addActor(adddiamonds);
        }
	}

	@Override
	public void show() {
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();

		pLogoSize = 0.75f * W;

		showBackground();
		showButtons();

        choose = new ChooseGameMode();
        mainStage.addActor(choose);
        choose.setPosition(GameScreen.W * 2, GameScreen.H * 2);

		pLogo.setPosition(W / 2 - pLogo.getWidth() / 2, (H + pCircleButtons.getY() + pCircleButtons.getHeight()) / 2);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(58 / 256f, 136 / 256f, 231 / 256f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(delta);
        mainStage.act(delta);
        mainStage.draw();

        if(!pause){
            pCircleButtons.rotate(Gdx.graphics.getDeltaTime(), 20);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            Gdx.app.exit();
        }

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
        pause = true;
	}

	@Override
	public void resume() {
		pause = false;
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}

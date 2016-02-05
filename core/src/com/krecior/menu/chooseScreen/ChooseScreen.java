package com.krecior.menu.chooseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.krecior.Manager;
import com.krecior.menu.ScreenType;
import com.krecior.menu.chooseScreen.elements.LevelTab;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.MoveAnimationSystem;
import com.krecior.utils.SimpleDirectionGestureDetector;
import com.krecior.utils.TextLabel;

public class ChooseScreen implements Screen {
	private static final float TOP_LABEL_SIZE = Gdx.graphics.getWidth() * 0.5f;
	private static final float BACK_BUTTON_SIZE = Gdx.graphics.getWidth() * 0.17f;
	private static final float TABLE_ELEMENT_SIZE = Gdx.graphics.getWidth() * 0.19f;

    private Image pBackButton;
	private Image pTopLabel;
    private Image[] background;

    private Stage mainStage = new Stage();
    private LevelTab[] levelTab;
    private MoveAnimationSystem backgroundGroupAnimation;
    private Group backgroundGroup = new Group();
    private int selectedPage = 0;
	private TextLabel landLabel;
	private int W;
	private int H;
	
	public static int actualLand = 1;

    public ChooseScreen() {
        super();
        W = Gdx.graphics.getWidth();
        H = Gdx.graphics.getHeight();

        showBackground();
        showStaticButtons();
        showTable();
        showLandName();
        Manager.getAdMobPlugin().getAdMobPluginListener().loadAd();
        Manager.inputMultiplexer.addProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.SwipeProcessor() {
            @Override
            //w lewo:
            public void onMove(float offset) {

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onChangeScreenRight() {
                System.out.println("LEFT");
                if(backgroundGroupAnimation.isMoving())return;
                selectedPage++;
                if(selectedPage<=3)
                    changeBackground(true);
                else
                    selectedPage = 3;
            }

            @Override
            public void onChangeScreenLeft() {
                System.out.println("RIGHT");
                if(backgroundGroupAnimation.isMoving())return;
                selectedPage--;
                if(selectedPage >= 0)
                    changeBackground(false);
                else
                    selectedPage = 0;
            }
        }));

        Manager.inputMultiplexer.addProcessor(mainStage);
    }

    @Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(58 / 256f, 136 / 256f, 231 / 256f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	    mainStage.act(Gdx.graphics.getDeltaTime());
        mainStage.draw();
        backgroundGroupAnimation.onAnimationDraw(Gdx.graphics.getDeltaTime());
		
		if(Gdx.input.isKeyJustPressed(Keys.BACK))
			Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	private void showBackground() {
        background = new Image[4];
         for(int i=0;i<background.length;i++){
            background[i] = new Image(Container.chooseGameBlurBackground[i]);
            background[i].setSize(W, H);
            background[i].setOrigin(0, 0);
            background[i].setPosition(i * background[i].getWidth(), 0);
            backgroundGroup.addActor(background[i]);
        }
        backgroundGroup.setWidth(4 * W);
    }

    private void changeBackground(boolean leftMove){
        backgroundGroupAnimation.startAnimation(leftMove, 0.75f);
        landLabel.setText("Land" + (selectedPage + 1));
        landLabel.setPosition(W / 2 - landLabel.getWidth() / 2, H - TOP_LABEL_SIZE * 0.35f / 4 );
     }

	private void showStaticButtons() {
		pTopLabel = new Image(Container.pEmptyButton);
		pTopLabel.setSize(TOP_LABEL_SIZE, TOP_LABEL_SIZE);
		pTopLabel.setPosition(W / 2 - pTopLabel.getWidth() / 2, H - pTopLabel.getHeight() * 0.35f);

		pBackButton = new Image(Container.pBackButton);
		pBackButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
                return true;
            }
        });
        pBackButton.setSize(BACK_BUTTON_SIZE, BACK_BUTTON_SIZE);
        pBackButton.setPosition(0.025f * W, 0.025f * W);
	}

	
	private void showTable() {
        levelTab = new LevelTab[3];
        for(int i=0;i<levelTab.length;i++){
            levelTab[i] = new LevelTab(i, (pTopLabel.getY() - pBackButton.getY() - pBackButton.getHeight() - Data.TABLE_ROW * TABLE_ELEMENT_SIZE) / (Data.TABLE_ROW + 1),
                    pBackButton.getY()+pBackButton.getHeight());
            backgroundGroup.addActor(levelTab[i]);
        }
        mainStage.addActor(backgroundGroup);
        backgroundGroupAnimation = new MoveAnimationSystem(backgroundGroup);
        mainStage.addActor(pTopLabel);
        mainStage.addActor(pBackButton);
	}

	private void showLandName() {
	    landLabel = new TextLabel(TextLabel.Font.ROBOTO,"Land 1",2f);
        landLabel.setPosition(W / 2 - landLabel.getWidth() / 2, H - TOP_LABEL_SIZE * 0.35f / 4 );
        mainStage.addActor(landLabel);
	}


}

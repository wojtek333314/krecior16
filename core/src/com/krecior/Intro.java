package com.krecior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krecior.menu.ScreenType;
import com.krecior.utils.Container;

public class Intro implements Screen {
	//===========================================================
	//Constants
	//===========================================================

	private static final float pDuration = 9f;
    private boolean firstRender = true;
	//===========================================================
	//Fields
	//===========================================================

	private SpriteBatch pPowerBatch;
	private Sprite pLogo;
    private Sprite background;
	
	private float pTime = 0;
    private float alfa = 0;


	//===========================================================
	//Constructors
	//===========================================================



	//===========================================================
	//Getter & Setter
	//===========================================================



	//===========================================================
	//Methods for/from SuperClass/Interfaces
	//===========================================================

	@Override
	public void show() {
		pPowerBatch = new SpriteBatch();

        background = new Sprite(Container.getTextureRegion("gfx/background_logo.png"));
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        pLogo = new Sprite(Container.getTextureRegion("gfx/blow_mind_logo.png"));
		pLogo.setSize(0.75f * Gdx.graphics.getWidth(),
                0.75f * Gdx.graphics.getWidth() * pLogo.getRegionHeight() / pLogo.getRegionWidth());
		pLogo.setPosition(Gdx.graphics.getWidth() / 2 - pLogo.getWidth() / 2, Gdx.graphics.getHeight() / 2 - pLogo.getHeight() / 2);
        pLogo.setAlpha(0);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(52 / 255f, 52 / 255f, 52 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pTime += Gdx.graphics.getDeltaTime();

		pPowerBatch.begin();
        background.draw(pPowerBatch);
        pLogo.draw(pPowerBatch);
		pPowerBatch.end();
		

       if(!firstRender)
       {
           alfa = (pTime/7f)<=1 ? (pTime/7f) : 1f;//przez 4 sek. ma sie pojawiac 1s ma wisiec
           alfa = alfa<0? 0 : alfa;
           pLogo.setAlpha(alfa);
       }
		if(pTime >= pDuration) {
			Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
			dispose();
		}
		
		if(Gdx.input.isTouched()) {
			Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
			dispose();
		}
        firstRender = false;
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
		pPowerBatch.dispose();
	}

	//===========================================================
	//Methods
	//===========================================================



	//===========================================================
	//Inner and Anonymous Classes
	//===========================================================
}

package com.krecior.game.systems;

import com.badlogic.gdx.InputProcessor;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.MoleState;
import com.krecior.game.objects.Mole;

public class TouchProcessor implements InputProcessor {
	//===========================================================
	//Constants
	//===========================================================



	//===========================================================
	//Fields
	//===========================================================

	private GameScreen pGame;
	
	private float pTouchX = 0;
	private float pTouchY = 0;

    private boolean pGrabMoles = true;

	//===========================================================
	//Constructors
	//===========================================================

	public TouchProcessor(GameScreen mGameScreen) {
		pGame = mGameScreen;
	}

	//===========================================================
	//Getter & Setter
	//===========================================================

	public float getTouchX() {
		return pTouchX;
	}
	
	public float getTouchY() {
		return pTouchY;
	}

    public void setGrabMoles(boolean mBoolean) {
        pGrabMoles = mBoolean;
    }

	//===========================================================
	//Methods for/from SuperClass/Interfaces
	//===========================================================



	//===========================================================
	//Methods
	//===========================================================



	//===========================================================
	//Inner and Anonymous Classes
	//===========================================================
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(pGame.TIME_STEP != 0 && pGame.canTouchMole) {
            pTouchX = screenX;
            pTouchY = screenY;

            pGame.getPowerManager().onTouchDown();

			for (int i = 0; i < pGame.getMoles().size(); i++) {
				if (pGrabMoles)
					if ((pGame.getMoles().get(i).getState() == MoleState.WALK || pGame.getMoles().get(i).getState() == MoleState.STAY) &&
							pGame.getMoles().get(i).getGrabClause()) {

						float distance = (float) Math.sqrt(Math.pow(screenX - pGame.getMoles().get(i).getPosition().x /
								GameScreen.METER_W * GameScreen.W, 2) +
								Math.pow((GameScreen.H - screenY - pGame.getMoles().get(i).getPosition().y /
										GameScreen.METER_H * GameScreen.H), 2));

						if (distance <= Mole.TOUCH_SIZE / GameScreen.METER_W * GameScreen.W / 2) {
							pGame.getMoles().get(i).setState(MoleState.GRABBED);

							pGame.getMoleThrow().startPositioning(pGame.getMoles().get(i));
							pGame.getMoles().get(i).stopAnimation();
							break;
						}
					}
			}
		}

		pGame.getHud().onTouchDown(screenX, screenY);

		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pGame.TIME_STEP != 0f) {
			pGame.getPowerManager().onTouchUp();

			int m = pGame.getMoleManager().getGrabbedMole();

			pTouchX = screenX;
			pTouchY = screenY;

			if (m >= 0) {
				pGame.getMoles().get(m).stopAnimation();

				pGame.getMoleThrow().stopPositioning();
			}

			pGame.getHud().onTouchUp(screenX, screenY);

		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pGame.TIME_STEP != 0f) {
            pTouchX = screenX;
            pTouchY = screenY;

            pGame.getHud().onTouchDrag(screenX, screenY);
            pGame.getPowerManager().onTouchDrag();
        }


        return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

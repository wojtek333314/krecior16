package com.krecior.game.systems;

import java.util.concurrent.CopyOnWriteArrayList;

import com.krecior.game.GameScreen;
import com.krecior.game.enums.MoleState;
import com.krecior.game.objects.Mole;
import com.krecior.utils.Container;

public class MoleThrow {
	//===========================================================
	//Constants
	//===========================================================

	public static final float POSITION_SAMPLING = 0.025f;
	public static final float GRAB_MOLE_MAX_RADIUS = 0.2f * GameScreen.W;
	
	public static final int MIN_POSITIONS = 2;

	//===========================================================
	//Fields
	//===========================================================

	public CopyOnWriteArrayList<Float> pX;
	public CopyOnWriteArrayList<Float> pY;
	
	private Mole pMole;
	
	private boolean pPositioning = false;
	
	private float pClock = 0;
	
	private float pAngle = 0;
	private float pSpeed = 0;
	private float pRange = 0;

	//===========================================================
	//Constructors
	//===========================================================

	public MoleThrow() {
		pX = new CopyOnWriteArrayList<Float>();
		pY = new CopyOnWriteArrayList<Float>();
	}
	
	// ==========================================================
	// Getter & Setter
	// ==========================================================

	public float getLastAngle() {
		if(pX.size() < 2)
			return 0;

		return (float) Math.atan2(pY.get(pY.size() - MIN_POSITIONS) - pY.get(pY.size()-1),
				pX.get(pX.size() - MIN_POSITIONS) - pX.get(pX.size()-1));
	}

	public float getAngle() {
		return pAngle;
	}
	
	public boolean getPositioning() {
		return pPositioning;
	}

	public void setPositioning(boolean mBoolean) {
		pPositioning = mBoolean;
	}
	
	public void setVelocity() {
		pMole.setLinearVelocity(-(float) Math.cos(pAngle) * pSpeed * Mole.VELOCITY, (float) Math.sin(-pAngle) * pSpeed * Mole.VELOCITY);
		pMole.setState(MoleState.FLY);
		pMole.stopAnimation();
		pMole.getSprite().setRegion(Container.pMoleFrame[6]);
		pMole.getSprite().setRotation((float) Math.toDegrees(pAngle) + 90f);
	}
	
	public void setAngle() {
		pMole.setPosition(pMole.getPosition(), pAngle);
	}
	

	//===========================================================
	//Methods
	//===========================================================
	
	public void startPositioning(Mole mMole) {
		pMole = mMole;
		pPositioning = true;
		pClock = 0;
	}
	
	public void clock() {
		if(pPositioning) {
			pClock += GameScreen.TIME_STEP;
			
			if(pClock >= POSITION_SAMPLING) {
				pClock = 0;
				pX.add(pMole.getPosition().x);
				pY.add(pMole.getPosition().y);
			}
			calculateRange();
		}
	}
	
	public void stopPositioning() {
		if(pX.size() >= MIN_POSITIONS) {
			pPositioning = false;
			
			calculateSpeed();
			calculateAngle();
			setAngle();
			setVelocity();
			
			if(pRange >= GRAB_MOLE_MAX_RADIUS)
				pMole.setGrabClause(false);
		}
		clear();
	}

	public void calculateSpeed() {	
		pSpeed = (float) Math.sqrt(Math.pow(pX.get(pX.size() - MIN_POSITIONS) / GameScreen.METER_W * GameScreen.W - 
									        pX.get(pX.size()-1) / GameScreen.METER_W * GameScreen.W, 2) +
							  	   Math.pow(pY.get(pY.size() - MIN_POSITIONS) / GameScreen.METER_H * GameScreen.H - 
							  			    pY.get(pY.size()-1) / GameScreen.METER_H * GameScreen.H, 2));	
		
		pSpeed = pSpeed / (MIN_POSITIONS * POSITION_SAMPLING) / GameScreen.MAX_MOLE_SPEED;

		if(pSpeed / (MIN_POSITIONS * POSITION_SAMPLING) > GameScreen.MAX_MOLE_SPEED)
			pSpeed = GameScreen.MAX_MOLE_SPEED;
	}
	
	public void calculateAngle() {
		pAngle = (float) Math.atan2(pY.get(pY.size() - MIN_POSITIONS) - pY.get(pY.size()-1),
				pX.get(pX.size() - MIN_POSITIONS) - pX.get(pX.size()-1));
	}
	
	public void clear() {
		pX.clear();
		pY.clear();
		pAngle = 0;
		pSpeed = 0;
		pRange = 0;
		pMole.setState(MoleState.FLY);
		pMole.stopAnimation();
	}
	
	public void calculateRange() {
		if(pY.size() > MIN_POSITIONS)
			pRange = (float) Math.sqrt(Math.pow(pX.get(pX.size() - 1) / GameScreen.METER_W * GameScreen.W - 
										        pX.get(0) / GameScreen.METER_W * GameScreen.W, 2) +
									   Math.pow(pY.get(pY.size() - 1) / GameScreen.METER_H * GameScreen.H - 
										        pY.get(0) / GameScreen.METER_H * GameScreen.H, 2));
			
		if(pRange >= GRAB_MOLE_MAX_RADIUS)
			stopPositioning();
	}	
}

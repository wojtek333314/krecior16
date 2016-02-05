package com.krecior.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.FieldType;
import com.krecior.utils.Container;

public class Field {
	//===========================================================
	//Constants
	//===========================================================



	//===========================================================
	//Fields
	//===========================================================
	
	private float pX;
	private float pY;
	private float pBackgroundHeight = GameScreen.W * Container.pLand[0].getRegionHeight() / Container.pLand[0].getRegionWidth();
	
	private int ID;
	
	private FieldType pType = FieldType.NORMAL;

	//===========================================================
	//Constructors
	//===========================================================

	public Field(int mID, FieldType mFieldType) {
		ID = mID;
		pType = mFieldType;
		
		pX = GameScreen.W * (1 - GameScreen.PLAY_FIELD_WIDTH)/2 + (ID % GameScreen.COLUMNS) * 
			 GameScreen.W * (GameScreen.PLAY_FIELD_WIDTH / GameScreen.COLUMNS);
		
		pY = GameScreen.PLAY_FIELD_TOP_GAP * pBackgroundHeight - (ID / GameScreen.COLUMNS) *
			 pBackgroundHeight * (GameScreen.PLAY_FIELD_HEIGHT / GameScreen.ROWS) -
			 1f/GameScreen.ROWS * GameScreen.PLAY_FIELD_HEIGHT * pBackgroundHeight;
	}

	//===========================================================
	//Getter & Setter
	//===========================================================

	public int getID() {
		return ID;
	}
	
	/**
	 * Return Center of field.
	 * 
	 * @return Vector2 position for Box2d tools.
	 */
	public Vector2 getPosition() {
		return new Vector2((pX + GameScreen.PLAY_FIELD_WIDTH * GameScreen.W / GameScreen.COLUMNS / 2) / GameScreen.W * GameScreen.METER_W, 
						   (pY + GameScreen.PLAY_FIELD_HEIGHT * GameScreen.H / GameScreen.ROWS / 2) / GameScreen.H * GameScreen.METER_H);
	}
	
	public float getX() {
		return pX;
	}
	
	public float getY() {
		return pY;
	}
	
	public FieldType getType() {
		return pType;
	}
	
	
	public void setType(FieldType mType) {
		pType = mType;
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
}

package com.krecior.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.PhysicObject;
import com.krecior.game.enums.PowerType;
import com.krecior.utils.Container;
import com.krecior.utils.UserData;

public class Target {
	//===========================================================
	//Constants
	//===========================================================

	public static final float GRAPHIC_SIZE = 1.9f * GameScreen.PLAY_FIELD_WIDTH * GameScreen.METER_W / GameScreen.COLUMNS;
	public static final float PHYSIC_SIZE = 0.6f * GRAPHIC_SIZE;
	public static final float POWER_X = 0.6f;
	public static final float POWER_Y = 0.68f;
	public static final float POWER_SIZE = 140f / 256f;
	public static final float DISAPPEARING_TIME = 0.5f;
	public static final float SCALING_TIME = 0.5f;

	//===========================================================
	//Fields
	//===========================================================

	private Body pBody;
	private Sprite pSprite;
	private Sprite pPowerSprite;
	private PowerType pPowerType;
	private Vector2 pPosition;
	
	private int ID = -1;
	private int pFieldID = -1;

	private float pAlpha = 1f;

	private boolean pState = false;
	private boolean pDisappearing = false;

	//===========================================================
	//Constructors
	//===========================================================

	public Target(int mID, Vector2 mPosition, PowerType mPowerType) {
		pPosition = mPosition;
        pPowerType = mPowerType;
		ID = mID;
		
		createBody();
		createSprite();
		
		setPosition(mPosition);
	}

	//===========================================================
	//Getter & Setter
	//===========================================================

	public void setAngularVelocity(float mAngularVelocity) {
		pBody.setAngularVelocity(mAngularVelocity);
	}
	
	public void setLinearVelocity(float vX, float vY) {
		pBody.setLinearVelocity(vX, vY);
	}
	
	public void setPosition(Vector2 mVector2) {
		pPosition = mVector2;
		pBody.setTransform(mVector2, 0);
	}
	
	public void setState(boolean mState) {		
		pState = mState;
	}
	
	public void setFieldID(int mFieldID) {
		pFieldID = mFieldID;
	}

	public void setTexture(TextureRegion texture) {
		pPowerSprite.setRegion(texture);
	}


	public Texture getTexture() {
		return pPowerSprite.getTexture();
	}

	public Body getBody() {
		return pBody;
	}
	
	public int getID() {
		return ID;
	}
	
	public PowerType getPower() {
		return pPowerType;
	}
	
	public Vector2 getPosition() {
		return pPosition;	
	}
	
	public boolean getState() {
		return pState;
	}
	
	public int getFieldID() {
		return pFieldID;
	}

	//===========================================================
	//Methods for/from SuperClass/Interfaces
	//===========================================================



	//===========================================================
	//Methods
	//===========================================================

	private void createBody() {	
		BodyDef pBodyDef = new BodyDef();
		pBodyDef.type = BodyDef.BodyType.KinematicBody;
		pBodyDef.position.set(0, 0);
		pBodyDef.fixedRotation = true;
		
		pBody = GameScreen.pWorld.createBody(pBodyDef);
		
		CircleShape pCircleShape = new CircleShape();
		pCircleShape.setRadius(PHYSIC_SIZE / 2);
		pCircleShape.setPosition(new Vector2(0.13f * GRAPHIC_SIZE, 0.17f * GRAPHIC_SIZE));
			
		FixtureDef pFixtureDef = new FixtureDef();
		pFixtureDef.shape = pCircleShape;
		pFixtureDef.density = 1f;
		pFixtureDef.restitution = 0.1f;
		pFixtureDef.friction = 1f;
		pFixtureDef.filter.categoryBits = Container.CATEGORY_TARGET;
		pFixtureDef.filter.maskBits = Container.MASK_TARGET;
			
		pBody.createFixture(pFixtureDef);
		pCircleShape.dispose();
		
		UserData ud = new UserData(ID, PhysicObject.TARGET);
		pBody.setUserData(ud);
	}
	
	private void createSprite() {	
		pSprite = new Sprite(Container.pTarget);
		pSprite.setSize(GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W, GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W);
		pSprite.setOriginCenter();
		pSprite.setRotation(0);
		
		pPowerSprite = new Sprite(PowerType.getTexture(pPowerType));
		pPowerSprite.setSize(GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W * POWER_SIZE, GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W * POWER_SIZE);
		pPowerSprite.setOriginCenter();
		pPowerSprite.setRotation(0);
		pPowerSprite.setAlpha(1f);
				
		pState = true;
		
		updateSpritePosition();
	}
	
	
	public void draw(SpriteBatch mSpriteBatch) {	
		updateSpritePosition();
		
		pSprite.draw(mSpriteBatch);
		pPowerSprite.draw(mSpriteBatch);
		
		if(pDisappearing) {
			pAlpha -= GameScreen.TIME_STEP * 2;
			disappear();
		}
	}
	
	public void hit() {
		disappear();
	}

	public void reUse(Vector2 mPosition) {
		pAlpha = 1f;
		pBody.setActive(true);
		pSprite.setSize(GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W,
				GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W);
		pPowerSprite.setSize(GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W * POWER_SIZE,
				GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W * POWER_SIZE);
        pPowerType = GameScreen.pGame.getTargetManager().getAvailablePowerType();
        pPowerSprite.setRegion(PowerType.getTexture(pPowerType));

		pSprite.setAlpha(1f);
		pPowerSprite.setAlpha(1f);

		setPosition(mPosition);
		updateSpritePosition();
		setState(true);
	}

	public void disappear() {
		pDisappearing = true;

		if(pAlpha <= 0) {
			pDisappearing = false;
			pAlpha = 0f;
			setState(false);
		}
		pSprite.setAlpha(pAlpha);
		pPowerSprite.setAlpha(pAlpha);
	}
	
	public void updateSpritePosition() {
		pSprite.setPosition(pPosition.x / GameScreen.METER_W * GameScreen.W - pSprite.getWidth() / 2, 
						    pPosition.y / GameScreen.METER_H * GameScreen.H - pSprite.getHeight() / 2);
		
		pPowerSprite.setPosition(pSprite.getX() + pSprite.getWidth() * POWER_X - pPowerSprite.getWidth() / 2,
								 pSprite.getY() + pSprite.getHeight() * POWER_Y - pPowerSprite.getHeight() / 2);
	}
	
	

	//===========================================================
	//Inner and Anonymous Classes
	//===========================================================
}

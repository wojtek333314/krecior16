package com.krecior.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.MoleState;
import com.krecior.game.enums.PhysicObject;
import com.krecior.game.enums.PowerType;
import com.krecior.utils.Animation;
import com.krecior.utils.Container;
import com.krecior.utils.UserData;

public class Mole {
	public static float SPAWN_TIME =
            Container.pLvlsData[GameScreen.pGame.getMap().getActualLevel()].spawnSpeed;
	public static final float GRAB_TIME = 1.25f;
	public static final float MIN_SPEED = 0.02f;
	public static final float GRAPHIC_SIZE = 1.2f * GameScreen.PLAY_FIELD_WIDTH * GameScreen.METER_W / GameScreen.COLUMNS;
	public static final float PHYSIC_SIZE = GRAPHIC_SIZE * 0.66f;
	public static final float TOUCH_SIZE = GRAPHIC_SIZE * 1.5f;
	public static final float VELOCITY = 10f;
	public static float WALK_SPEED =
            Container.pLvlsData[GameScreen.pGame.getMap().getActualLevel()].runSpeed;
	public static final float MOLE_STEP_TIME = 1f;

	private Body pBody;
	
	private Sprite pSprite;
	
	private MoleState pState = MoleState.NOT_USED;
	
	private Animation pAnimation;

	private PowerType pPowerType;
	
	private int pWalkDirection = -1;
	private int pTwistDirection = -1;
	
	private boolean pGrabClause = true;
	private boolean isKilled = false;

	private float time = 0f;
	
	public boolean pDisappearing = false;
	public boolean pScaling = false;
	
	public int ID = 0;

	public Mole(int mID) {
		ID = mID;

		createBody();
		createSprite();
	}

	public void setAngularVelocity(float mAngularVelocity) {
		pBody.setAngularVelocity(mAngularVelocity);
	}
	
	public void setAnimation(Animation mAnimation) {
		pAnimation = mAnimation;
	}
	
	public void setGrabClause(boolean mGrabClause) {
		pGrabClause = mGrabClause;
	}
	
	public void setLinearVelocity(float vX, float vY) {
		pBody.setLinearVelocity(vX, vY);
	}
	
	public void setPosition(float mX, float mY, float mAngle) {
		pBody.setTransform(mX, mY, mAngle);
	}
	
	public void setPosition(Vector2 mVector2, float mAngle) {
		pBody.setTransform(mVector2, mAngle);
	}

	public void setPowerType(PowerType mPowerType) {pPowerType = mPowerType; }
	
	public void setState(MoleState mState) {
		pState = mState;
	}
	
	/** 
	 * @param pAngle in radians
	 */
	public void setRotation(float pAngle) {
		pBody.setTransform(pBody.getPosition(), pAngle);
		pSprite.setRotation((float) (pAngle * 180f / Math.PI) - 90f);
	}
	
	public void setTwistDirection(int mDirection) {
		pTwistDirection = mDirection;
	}

	public void setTime(float time) {
		this.time = time;
	}
	
	public void setWalkDirection(int mDirection) {
		pWalkDirection = mDirection;
	}
	
	
	
	public float getAngle() {
		return pBody.getAngle();	
	}
	
	public Animation getAnimation() {
		return pAnimation;
	}

	public Body getBody() { return pBody; }
	
	public boolean getGrabClause() {
		return pGrabClause;
	}
	
	public Vector2 getLinearVelocity() {
		return pBody.getLinearVelocity();
	}
	
	public Vector2 getPosition() {
		return pBody.getPosition();	
	}

	public PowerType getPowerType() { return pPowerType; }
	
	public Sprite getSprite() {
		return pSprite;
	}
	
	public MoleState getState() {
		return pState;
	}

	public float getTime() { return time; }
	
	public int getWalkDirection() {
		return pWalkDirection;
	}

	public boolean isKilled() { return isKilled; }

	public void setKilled(boolean b) { isKilled = b; }

	private void createBody() {	
		BodyDef pBodyDef = new BodyDef();
		pBodyDef.type = BodyDef.BodyType.DynamicBody;
		pBodyDef.position.set(0, 0);
		pBodyDef.fixedRotation = true;
		pBodyDef.bullet = true;
		pBodyDef.linearDamping = 1.5f;
		
		pBody = GameScreen.pWorld.createBody(pBodyDef);
		
		CircleShape pCircleShape = new CircleShape();
		pCircleShape.setRadius(PHYSIC_SIZE / 2);
			
		FixtureDef pFixtureDef = new FixtureDef();
		pFixtureDef.shape = pCircleShape;
		pFixtureDef.density = 1f;
		pFixtureDef.restitution = 0f;
		pFixtureDef.friction = 1f;
		pFixtureDef.filter.categoryBits = Container.CATEGORY_MOLE;
		pFixtureDef.filter.maskBits = Container.MASK_MOLE;
			
		pBody.createFixture(pFixtureDef);
		pCircleShape.dispose();

		UserData ud = new UserData(ID, PhysicObject.MOLE);
		pBody.setUserData(ud);
		
		setPosition(GameScreen.METER_W, GameScreen.METER_H, 1f);
	}
	
	private void createSprite() {	
		pSprite = new Sprite(Container.pMoleFrame[3]);
		pSprite.setSize(GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W, GRAPHIC_SIZE / GameScreen.METER_W * GameScreen.W);
		pSprite.setOriginCenter();
		pSprite.setRotation((float) (pBody.getAngle() * 180f / Math.PI) - 90f);
		pSprite.setAlpha(0);
	}
	
	public void disappear() {
		pDisappearing = true;
		
		setGrabClause(false);
		
		if(pSprite.getColor().a == 0) {
			pDisappearing = false;
			setState(MoleState.NOT_USED);
            GameScreen.pGame.addBodyToDispose(getBody());
		}
	}
	
	public void draw(SpriteBatch mSpriteBatch) {
		time += GameScreen.TIME_STEP;

		updateSpritePosition();
		
		if(pAnimation != null)
			pAnimation.animate();
		
		if(pDisappearing) {
			getSprite().setAlpha(getSprite().getColor().a - 0.01f);
			disappear();
		}

		if(pScaling) {
			getSprite().setSize(getSprite().getWidth() * 0.97f, getSprite().getHeight() * 0.97f);
			getSprite().setOriginCenter();
			updateSpritePosition();
			scaling();
		}
		
		pSprite.draw(mSpriteBatch);
	}
	
	public void startWalk() {
		if(!pDisappearing)
			if(pWalkDirection == 0) {
				pBody.setLinearVelocity(-WALK_SPEED, 0);
				setRotation((float) Math.toRadians(-180));

				TextureRegion[] tex = {Container.pMoleFrame[4], Container.pMoleFrame[3]};

				pAnimation = new Animation(pSprite, MOLE_STEP_TIME / WALK_SPEED, 1, tex, 0, 1, (float) (Math.toRadians(-180) * 180f / Math.PI)) {
					@Override
					public void onAnimationFinished() {
						stopAnimation();
					}
				};
			} else {
				pBody.setLinearVelocity(WALK_SPEED, 0);
				setRotation(0);

				TextureRegion[] tex = {Container.pMoleFrame[4], Container.pMoleFrame[3]};

				pAnimation = new Animation(pSprite, MOLE_STEP_TIME / WALK_SPEED, 1, tex, 0, 1, 0) {
					@Override
					public void onAnimationFinished() {
						stopAnimation();
					}
				};
			}
	}
	
	public void goAway() {
		if(getPosition().x <= GameScreen.METER_W / 2) {
			pBody.setLinearVelocity(-WALK_SPEED * 1.5f, 0);
			setRotation((float) Math.toRadians(-180));
			
			TextureRegion[] tex = {Container.pMoleFrame[4], Container.pMoleFrame[3]};
			
			pAnimation = new Animation(pSprite, MOLE_STEP_TIME / WALK_SPEED / 2, 1, tex, 0, 1, (float) (Math.toRadians(-180) * 180f / Math.PI)) {
				@Override
				public void onAnimationFinished() {
					stopAnimation();
				}
			};
		} else {
			pBody.setLinearVelocity(WALK_SPEED * 1.5f, 0);
			setRotation(0);
			
			TextureRegion[] tex = {Container.pMoleFrame[4], Container.pMoleFrame[3]};
			
			pAnimation = new Animation(pSprite, MOLE_STEP_TIME / WALK_SPEED / 2, 1, tex, 0, 1, 0) {
				@Override
				public void onAnimationFinished() {
					stopAnimation();
				}
			};
		}
	}

    public void resetSpriteRotation() {
        pSprite.setRotation(0);
    }
	
	public void stopAnimation() {
		pAnimation = null; // TODO delete Animation;
	}
	
	public void scaling() {
		pScaling = true;
		
		pSprite.setRotation(pSprite.getRotation() + 5 * pTwistDirection);
		setGrabClause(false);
		
		
		if(pSprite.getWidth() <= 0.01f * GameScreen.W) {
			pScaling = false;
			setState(MoleState.NOT_USED);
		}
	}
	
	public void updateSpritePosition() {
			pSprite.setPosition(pBody.getPosition().x / GameScreen.METER_W * GameScreen.W - pSprite.getWidth() / 2,
								pBody.getPosition().y / GameScreen.METER_H * GameScreen.H - pSprite.getHeight() / 2);
	}
}

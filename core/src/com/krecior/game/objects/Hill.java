package com.krecior.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.PowerType;
import com.krecior.utils.Animation;
import com.krecior.utils.Container;

public class Hill {
	public static final float SIZE = 3f * GameScreen.PLAY_FIELD_WIDTH * GameScreen.METER_W / GameScreen.COLUMNS;
    public static final float POWER_SCALE = 0.25f;
    public static float TIME_TO_OPEN = 2f / Mole.SPAWN_TIME;

	private Animation pAnimation;
	
	private Sprite pSprite;
	private Sprite powerSprite;
	
	private Vector2 pPosition;

	private PowerType power = PowerType.NORMAL;

	private float pTime = 0;
    private float powerAlpha = 0;
    private float spriteAlpha = 1f;

    private boolean isPowerDisappearing = false;
    private boolean isPowerAppearing = false;
    private boolean isSpriteDisappearing = false;

	public Hill(Vector2 mPosition) {
		pPosition = mPosition;
		
		createSprite();
		setAnimation(new Animation(pSprite, 1f, 1, Container.pMolehill, 0, 14, 0));
	}

	public Vector2 getPosition() { return pPosition; }

	public PowerType getPower() { return power; }
	
	public Sprite getSprite() {
		return pSprite;
	}

	public float getTime() {
		return pTime;
	}


	public void setPower(PowerType mPower) {
        power = mPower;
        powerSprite.setRegion(PowerType.getTexture(power));

        if(power == PowerType.ERASE) {
            spriteDisappear();
        } else {
            powerAppear();
        }
    }

    public void removePower() {
        power = PowerType.NORMAL;
        powerDisappear();
    }

	public void setSprite(Sprite mSprite) {
		pSprite = mSprite;
	}

	public void setTime(float mTime) {
		pTime = mTime;
	}

	private void createSprite() {
		pSprite = new Sprite(Container.pMolehill[15]);
		pSprite.setSize(SIZE / GameScreen.METER_W * GameScreen.W, SIZE / GameScreen.METER_W * GameScreen.W);
		pSprite.setOriginCenter();
		pSprite.setRotation(0);
		pSprite.setPosition(pPosition.x / GameScreen.METER_W * GameScreen.W - pSprite.getWidth() * 0.5f,
                pPosition.y / GameScreen.METER_H * GameScreen.H - pSprite.getHeight() * 0.65f);

        powerSprite = new Sprite(Container.pPowers[10]);
        powerSprite.setSize(pSprite.getWidth() * POWER_SCALE, pSprite.getHeight() * POWER_SCALE);
        powerSprite.setAlpha(powerAlpha);
        powerSprite.setPosition(pSprite.getX() + (pSprite.getWidth() - powerSprite.getWidth()) / 2,
                pSprite.getY() + pSprite.getHeight() / 2 - powerSprite.getHeight() / 2);
	}
	
	private void setAnimation(Animation mAnimation) {
		pAnimation = mAnimation;
	}
	
	public void draw(SpriteBatch mSpriteBatch) {
		if(pAnimation != null)
			pAnimation.animate();

        if(isSpriteDisappearing) {
            spriteAlpha -= GameScreen.TIME_STEP;
            pSprite.setAlpha(spriteAlpha);

            spriteDisappear();
        }

        if(isPowerDisappearing) {
            powerAlpha -= GameScreen.TIME_STEP;
            powerSprite.setAlpha(powerAlpha);

            powerDisappear();
        }

        if(isPowerAppearing) {
            powerAlpha += GameScreen.TIME_STEP;
            powerSprite.setAlpha(powerAlpha);

            if(power == PowerType.ERASE)
                pSprite.setAlpha(powerAlpha);

            powerAppear();
        }

		pSprite.draw(mSpriteBatch);
        powerSprite.draw(mSpriteBatch);
	}

    public void powerAppear() {
        isPowerAppearing = true;

        if(powerAlpha >= 1f) {
            isPowerAppearing = false;
            powerAlpha = 1f;
            powerSprite.setAlpha(powerAlpha);
        }
    }

    public void powerDisappear() {
        isPowerDisappearing = true;

        if(powerAlpha <= 0f) {
            isPowerDisappearing = false;
            powerAlpha = 0f;
            powerSprite.setAlpha(powerAlpha);
        }
    }

    public void spriteDisappear() {
        isSpriteDisappearing = true;

        if(spriteAlpha <= 0f) {
            isSpriteDisappearing = false;
            spriteAlpha = 0f;
            pSprite.setAlpha(spriteAlpha);
            GameScreen.pGame.getHillManager().getHills().remove(this);
        }
    }
}

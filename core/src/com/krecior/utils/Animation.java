package com.krecior.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.krecior.game.GameScreen;

public class Animation {
	//===========================================================
	//Constants
	//===========================================================



	//===========================================================
	//Fields
	//===========================================================

	private Sprite pSprite;
	
	private TextureRegion[] pTextureRegions;
	
	private boolean isEndless = false;

	protected float pTimeOfLoop;
	private float pCurrentTime = 0;
	private float pRotation = 0;
	
	private int pLoops = 1;
	private int pStartFrame;
	private int pEndFrame;
	private int pCurrentLoop = 0;
	private int pCurrentFrame = 0;
	
	protected float pTime = 0;

	//===========================================================
	//Constructors
	//===========================================================

	public Animation(Sprite mSprite, float mTimeOfLoop, int mLoops, TextureRegion[] mTextureRegions, int mStartFrame, int mEndFrame, float mRotation) {
		pSprite = mSprite;
		pTimeOfLoop = mTimeOfLoop;
		pLoops = mLoops;
		pTextureRegions = mTextureRegions;
		pStartFrame = mStartFrame;
		pEndFrame = mEndFrame;
		pCurrentFrame = mStartFrame;
		pRotation = mRotation;
		
		pSprite.setRotation(pRotation);
	}
	
	
	public Animation(Sprite mSprite, float mTimeOfLoop, TextureRegion[] mTextureRegions, int mStartFrame, int mEndFrame, float mRotation) {
		pSprite = mSprite;
		pTimeOfLoop = mTimeOfLoop;
		pTextureRegions = mTextureRegions;
		pStartFrame = mStartFrame;
		pEndFrame = mEndFrame;
		pCurrentFrame = mStartFrame;
		pRotation = mRotation;
		isEndless = true;
		
		pSprite.setRotation(pRotation);
	}

	//===========================================================
	//Getter & Setter
	//===========================================================



	//===========================================================
	//Methods for/from SuperClass/Interfaces
	//===========================================================



	//===========================================================
	//Methods
	//===========================================================

	public void animate() {
		pCurrentTime += GameScreen.TIME_STEP;
		pTime += GameScreen.TIME_STEP;
		
		onAnimationRunning();
		
		if(pCurrentTime >= pTimeOfLoop / (pEndFrame - pStartFrame +1)) {
			pCurrentTime = 0;
			if(pCurrentLoop < pLoops) {
				if(pCurrentFrame < pEndFrame) {	
					pSprite.setRotation(pRotation);
					
					pCurrentFrame++;
				} else {
					pSprite.setRotation(pRotation);
					
					pCurrentFrame = pStartFrame;
					
					if(!isEndless) {
						pCurrentLoop++;
						
						if(pCurrentLoop == pLoops)
							pCurrentFrame = pEndFrame;
					}
				}
			}
		}
		
		TextureRegion t = pTextureRegions[pCurrentFrame];

		if(pRotation == -180 && !t.isFlipY())
			t.flip(false, true);
		
		if(pRotation == 0 && t.isFlipY())
			t.flip(false, true);
			
		pSprite.setRegion(t);
		
		if(pCurrentLoop >= pLoops && !isEndless)
			onAnimationFinished();
	}
	
	public void onAnimationFinished() {
		
	}
	
	public void onAnimationRunning() {
		
	}

	//===========================================================
	//Inner and Anonymous Classes
	//===========================================================
}

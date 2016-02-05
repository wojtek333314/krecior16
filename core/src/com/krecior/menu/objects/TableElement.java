package com.krecior.menu.objects;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.krecior.Manager;
import com.krecior.utils.Container;
import com.krecior.utils.Data;

public class TableElement extends Group {
	//===========================================================
	//Constants
	//===========================================================

	private static final float DIAMOND_ANGLE = 45f;

	//===========================================================
	//Fields
	//===========================================================

	private Image pImage;
	private Image[] pDiamond;
	
	private int ID;
	private int pDiamonds;
	
	private float pSize;
	private float pSizeOfDiamond;
	
	private boolean isUnlocked;

	//===========================================================
	//Constructors
	//===========================================================

	public TableElement(float mX, float mY, float mSize, int mID) {
		pSize = mSize;
		pSizeOfDiamond = 0.15f * pSize;
		ID = mID;
		
		getData();
		
		if(isUnlocked)
			pImage = new Image(Container.pEmptyButton);
		else
			pImage = new Image(Container.pButton[1]);
		pImage.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if(isUnlocked)
		    		Manager.manager.startLevel(ID);
		        return true;
		    }
		});
		pImage.setSize(pSize, pSize);
		pImage.setPosition(mX, mY);
		addActor(pImage);
	
		createDiamonds();
	}

	//===========================================================
	//Getter & Setter
	//===========================================================

	private void getData() {
		isUnlocked = Data.isLevelUnlocked(ID);
		pDiamonds = Data.getLevelRate(ID);
	}

	public boolean isUnlocked() {
		return isUnlocked;
	}

	//===========================================================
	//Methods for/from SuperClass/Interfaces
	//===========================================================
	
	@Override
	public float getX() {
		return pImage.getX();
	}
	
	@Override
	public float getY() {
		return pImage.getY();
	}

	//===========================================================
	//Methods
	//===========================================================

	private void createDiamonds() {
		pDiamond = new Image[pDiamonds];
		
		float radius = 0.4f * pImage.getWidth() - pSizeOfDiamond/2;
		float actualAngle = (float) (-DIAMOND_ANGLE * Math.PI / 180f);
		
		for(int i = 0; i < pDiamonds; i++) {
			pDiamond[i] = new Image(Container.pLittleDiamond);
			pDiamond[i].setSize(pSizeOfDiamond * Container.pLittleDiamond.getRegionWidth() / Container.pLittleDiamond.getRegionHeight(), pSizeOfDiamond);
			pDiamond[i].setPosition(pImage.getX() + pImage.getWidth()/2 + (float)Math.sin(actualAngle) * radius - pDiamond[i].getWidth()/2, 
									pImage.getY() + pImage.getHeight()/2 + (float)Math.cos(actualAngle) * radius + pDiamond[i].getHeight()/2);
			pDiamond[i].setRotation((float) (-actualAngle * 180f / Math.PI));
			addActor(pDiamond[i]);
			
			actualAngle += (float) (DIAMOND_ANGLE * Math.PI / 180f);
		}
	}

	//===========================================================
	//Inner and Anonymous Classes
	//===========================================================
}

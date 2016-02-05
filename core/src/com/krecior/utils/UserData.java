package com.krecior.utils;

import java.io.Serializable;

import com.krecior.game.enums.PhysicObject;

public class UserData implements Serializable 
{
	//===========================================================
	//Constants
	//===========================================================

    private static final long serialVersionUID = 1L;

	//===========================================================
	//Fields
	//===========================================================

    private PhysicObject pType;
    
    private int pID;

	//===========================================================
	//Constructors
	//===========================================================

    public UserData(int mID, PhysicObject mType) {
    	pID = mID;
    	pType = mType;
    }

	//===========================================================
	//Getter & Setter
	//===========================================================

    public int getID() {
    	return pID;
    }
    
    public PhysicObject getType() {
    	return pType;
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

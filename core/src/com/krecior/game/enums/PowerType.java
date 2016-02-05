package com.krecior.game.enums;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.krecior.game.GameScreen;
import com.krecior.game.objects.Hill;
import com.krecior.game.objects.Mole;
import com.krecior.game.systems.PowerManager;
import com.krecior.utils.Container;
import com.krecior.utils.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum PowerType {
    ERASE,
	WATER,
    LIGHTBOLT,
	FIRE,
	HAMMER,
    SCREWDIVER,
	POISON,
    GUN,
	DIAMOND,
	NORMAL;

	public static TextureRegion getTexture(PowerType mPowerType) {
		switch(mPowerType) {
			case DIAMOND: return Container.pPowers[0];
			case FIRE: return Container.pPowers[5];
			case HAMMER: return Container.pPowers[3];
			case NORMAL: return Container.pPowers[14];
			case GUN: return Container.pPowers[2];
			case POISON: return Container.pPowers[7];
			case ERASE: return Container.pPowers[4];
			case SCREWDIVER: return Container.pPowers[8];
			case LIGHTBOLT: return Container.pPowers[6];
			case WATER: return Container.pPowers[9];
			default: return Container.pPowers[10];
		}
	}

	public static PowerType[] getValuesForItemShop(){
		return new PowerType[]{ERASE,WATER,HAMMER,SCREWDIVER,POISON,GUN,LIGHTBOLT,FIRE};
	}

	public static boolean canUse(PowerType powerType){
		return Data.isPowerAvaliable(powerType);
	}

    //arguementem jest poziom zeby wiedziec o rozkladzie procentowym danego levelu
    public static PowerType random(int level) {
        Random pRandom = new Random();
        List<PowerType> powerTypes = new ArrayList<PowerType>();
        PowerType[] p = {FIRE, HAMMER,GUN, POISON, ERASE, SCREWDIVER, LIGHTBOLT, WATER};

        int chance = 0;
        for (PowerType aP : p)
            if (canUse(aP) && Container.getPowerChanceOnLevel(level, aP)>0) {
                powerTypes.add(aP);
                chance += Container.getPowerChanceOnLevel(level, aP);
            }

        if(chance<100){
            powerTypes.add(DIAMOND);
            powerTypes.add(NORMAL);
        }

        int rozklad[] = new int[100];
        int offset = 0;
        for(int i=0;i<powerTypes.size();i++){
            int powerTypeChance = Container.getPowerChanceOnLevel(level, powerTypes.get(i));
            if(powerTypes.get(i).equals(DIAMOND) || powerTypes.get(i).equals(NORMAL))
                powerTypeChance = (100-chance)/2;
            for(int j=0;j<powerTypeChance;j++)
                rozklad[offset+j] = i;
            offset+=powerTypeChance;
        }
        int random = pRandom.nextInt(100);
        return powerTypes.get(rozklad[random]);
    }

	public static float getTouchDistance(PowerType mPowerType) {
		switch(mPowerType) {
			case DIAMOND: return 0;
			case FIRE: return PowerManager.FIRE_SIZE / 2;
			case HAMMER: return 1.5f * Mole.TOUCH_SIZE / GameScreen.METER_W * GameScreen.W / 2;
			case NORMAL: return 0;
			case GUN: return Mole.TOUCH_SIZE / GameScreen.METER_W * GameScreen.W / 2;
			case POISON: return Hill.SIZE / GameScreen.METER_W * GameScreen.W / 2;
			case ERASE: return Mole.TOUCH_SIZE / GameScreen.METER_W * GameScreen.W / 2;
			case SCREWDIVER: return  1.5f * Mole.TOUCH_SIZE / GameScreen.METER_W * GameScreen.W / 2;
			case LIGHTBOLT: return Mole.TOUCH_SIZE / GameScreen.METER_W * GameScreen.W / 2;
			case WATER: return Hill.SIZE / GameScreen.METER_W * GameScreen.W / 2;
			default: return 0;
		}
	}

	public static int getID(PowerType mPowerType) {
        if(mPowerType!=null)
		switch(mPowerType) {
			case ERASE: return 0;
			case WATER: return 1;
			case LIGHTBOLT: return 2;
			case FIRE: return 3;
			case HAMMER: return 4;
			case SCREWDIVER: return 5;
			case POISON: return 6;
			case GUN: return 7;
			case DIAMOND: return 8;
			case NORMAL: return 9;
		}
        return -1;
	}


	public static int getCost(PowerType itemType){
		switch(itemType){
			case ERASE:
				return 50;
			case WATER:
				return 100;
			case LIGHTBOLT:
				return 3200;
			case FIRE:
				return 7200;
			case HAMMER:
				return 200;
			case SCREWDIVER:
				return 400;
			case POISON:
				return 800;
			case GUN:
				return 1600;
			default:
				return -1;
		}
	}

	public static String getDescription(PowerType itemType){
		switch(itemType){
			case ERASE:
				return "Hold and move finger in different\ndirection to erase mole hole and moles";
			case WATER:
				return "Tap and hold to mole hole\nWater can blocked moles and throw into up";
			case LIGHTBOLT:
				return "Tap three times mold to receive 3 diamonds.";
			case FIRE:
				return "Tap screen and burn all the moles.\nYou have 3 seconds.";
			case HAMMER:
				return "Tap three times mold to receive 3 diamonds";
			case SCREWDIVER:
				return "Tap three times mold to receive 3 diamonds.";
			case POISON:
				return "Tap and hold mole hole.\nMoles will be poison and die";
			case GUN:
				return "You can 5 times tap differents mole\nand eliminated them";
			default:
				return "default itemType description";
		}
	}
}

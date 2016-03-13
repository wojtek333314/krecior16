package com.krecior.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.krecior.game.enums.PowerType;
import com.krecior.menu.achievements.system.Achievement;

public class Data {
	public static final int TABLE_ROW = 5;		//
	public static final int TABLE_COLUMN = 3;	//	table in ChooseStage screen.
	public static final int TABLE_PAGES = 1;	//
	private static String KEY = "krecior";

	public Data() {
		if(getString("fp").equals("null"))
			firstGameDeclare();
	}

    public static boolean isTutorialShowed(){
        return getBoolean("tutorial");
    }

    public static void setTutorialShowed(){
        putBoolean("tutorial",true);
    }

	private static String getString(String key) {
		return getPreferences().getString(key, "null");
	}

	private static int getInt(String key) {
		return getPreferences().getInteger(key);
	}

	private static float getFloat(String key) {
		return getPreferences().getFloat(key);
	}

	private static boolean getBoolean(String key) {
		return getPreferences().getBoolean(key, false);
	}

	private static void putString(String key, String data) {
		Preferences tmp = getPreferences();
		tmp.putString(key, data);
		tmp.flush();
	}

	public static void putInt(String key, int mInt) {
		Preferences tmp = getPreferences();
		tmp.putInteger(key, mInt);
		tmp.flush();
	}

	private static void putFloat(String key, float mFloat) {
		Preferences tmp = getPreferences();
		tmp.putFloat(key, mFloat);
		tmp.flush();
	}

	private static void putBoolean(String key, boolean mBoolean) {
		Preferences tmp = getPreferences();
		tmp.putBoolean(key, mBoolean);
		tmp.flush();
	}

	private static boolean getBoolean(SocialNetworkKey socialNetworkKey) {
		return getPreferences().getBoolean(socialNetworkKey.name());
	}
	private static void putBoolean(SocialNetworkKey socialNetworkKey, boolean mBoolean) {
		Preferences tmp = getPreferences();
		tmp.putBoolean(socialNetworkKey.name(), mBoolean);
		tmp.flush();
	}

	public static int getDiamonds(){
		return getInt("diamonds");
	}

	public static void incrementDiamonds(){
		putInt("diamonds", getInt("diamonds") + 1);
	}

	private static Preferences getPreferences() {
		return Gdx.app.getPreferences(KEY);
	}

	private void firstGameDeclare() {
		putString("fp", "false");

		for(int i = 0; i < TABLE_ROW * TABLE_COLUMN * TABLE_PAGES; i++) {
			putBoolean("level" + i + "unlocked", false);
			putInt("level" + i + "diamonds", 0);
		}
		putBoolean("level0unlocked", true);
		putInt("diamonds", 0);
		putInt("BEST_GAME", 0);

		for(PowerType itemType : PowerType.values()){
			putBoolean("ITEM_ISBUYED_"+itemType.name(),false);
		}
	}

	public static void setBestGame(int points) {
		putInt("BEST_GAME", points);
	}

	public static int getBestGame() { return getInt("BEST_GAME"); }

	public static int getAchievementValue(Achievement achievement){
		return getInt("Achievement_" + achievement.name());
	}

	public static void setAchievementValue(Achievement achievement,int value){
		putInt("Achievement_"+achievement.name(),value);
	}

	public static boolean isAppMuted(){
		return Data.getBoolean("soundIsMuted");
	}

	public static int getLevelRate(int level){
		return Data.getInt("level" + level + "diamonds");
	}


	public static boolean isLevelUnlocked(int level){
		return Data.getBoolean("level" + level + "unlocked");
	}

	public static void setLevelUnlocked(int level) {
		Data.putBoolean("level" + level + "unlocked",true);
	}

    public static void setLevelLocked(int level) {
        Data.putBoolean("level" + level + "unlocked",false);
    }

	public static void setAppMuted(boolean value){
		Data.putBoolean("soundIsMuted", value);
	}

	public static void setItemBuyed(PowerType itemType){
		putBoolean("ITEM_ISBUYED_" + itemType.name(), true);
	}

    public static void setItemNOTBuyed(PowerType itemType){
        putBoolean("ITEM_ISBUYED_" + itemType.name(), false);
    }

	public static boolean isPowerAvaliable(PowerType powerType){
        if(powerType!=null)
		    return getBoolean("ITEM_ISBUYED_"+powerType.name());
	    return false;
    }

	public static void rateLevel(int level,int rate){
		if(getLevelRate(level)<rate)
			putInt("level" + level + "diamonds", rate);
	}

	public static void addDiamonds(int count){
		putInt("diamonds",getInt("diamonds")+count);
	}

    public static boolean isAnyPowerAvaliable(){
        PowerType[] powers = PowerType.getValuesForItemShop();
        for (PowerType power : powers) {
            if (Data.isPowerAvaliable(power))
                return true;
        }
        return false;
    }

	public static boolean facebookIsShared(){
		return getBoolean(SocialNetworkKey.FB_SHARE);
	}

	public static void facebookSetShared(){
        if(!facebookIsShared()){
            putBoolean(SocialNetworkKey.FB_SHARE, true);
            addDiamonds(500);
        }

	}

	public static boolean facebookIsLiked(){
		return getBoolean(SocialNetworkKey.FB_LIKE);
	}

	public static void facebookSetLiked(){
        if(!facebookIsLiked()) {
            putBoolean(SocialNetworkKey.FB_LIKE, true);
            addDiamonds(100);
        }
	}

    public static void setLevelDataJSON(int level, String  data){
        putString("level"+Integer.toString(level)+"json",data);
    }

    public static String getLevelDataJSON(int level){
        return getString("level"+Integer.toString(level)+"json");
    }

    public static void addPoints(int points){
        putInt("points",getInt("points")+points);
    }

    public static int getPoints(){
        return getInt("points");
    }

	public enum SocialNetworkKey{
		FB_SHARE,
		FB_LIKE
	}
}

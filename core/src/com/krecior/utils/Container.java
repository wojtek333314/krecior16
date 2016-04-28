package com.krecior.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.krecior.game.enums.PowerType;
import com.krecior.sound.SoundManager;
import com.krecior.utils.enums.FontType;

import java.util.HashMap;

public class Container {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final short CATEGORY_BORDER = 0x0001;
	public static final short CATEGORY_MOLE = 0x0002;
	public static final short CATEGORY_TARGET = 0x0004;

	public static final short MASK_BORDER = 0x0002;
	public static final short MASK_MOLE = (0x0001 | 0x0004);
	public static final short MASK_TARGET = 0x0002;

	// ===========================================================
	// Fields
	// ===========================================================

	public static TextureRegion pEmptyButton;
	public static TextureRegion pLittleDiamond;
	public static TextureRegion	pBackButton;
	public static TextureRegion pLandBlur;
	public static TextureRegion pCloudsBlur;
	public static TextureRegion pClouds;
	public static TextureRegion pBorder;
	public static TextureRegion pMolesWarLogo;
	public static TextureRegion	pTarget;
	public static TextureRegion soundOn;
	public static TextureRegion soundOff;
	public static TextureRegion pLeftLabel;
	public static TextureRegion pRightLabel;
	public static TextureRegion pPause;
	public static TextureRegion pRepeat;
	public static TextureRegion pChoose;
	public static TextureRegion heart;
	public static TextureRegion top_position;
	public static TextureRegion enter_label;

    public static TextureRegion[] chooseGameBlurBackground;
	public static TextureRegion[] pButton;
	public static TextureRegion[] pLand;
	public static TextureRegion[] pMoleFrame;
	public static TextureRegion[] pMolehill;
	public static TextureRegion[] pPowers;
	public static TextureRegion[] pFireFrame;
	public static TextureRegion[] pBoltFrame;
	public static TextureRegion[] pWaterFrame;
	public static TextureRegion[] pHammerScrewdriver;
    public static TextureRegion killedMoleIcon;

	public static LandsData pLandsData;
	public static Lvl[] pLvlsData;
	private static HashMap<FontKey, BitmapFont> fonts = new HashMap<FontKey, BitmapFont>();
	// ===========================================================
	// Constructors
	// ===========================================================

	public Container() {
		pEmptyButton = getTextureRegion("gfx/empty_button.png");
		pLittleDiamond = getTextureRegion("gfx/little_diamond.png");
		pBackButton = getTextureRegion("gfx/back.png");
		pLandBlur = getTextureRegion("gfx/land01blur.png");
		pCloudsBlur = getTextureRegion("gfx/cloudsBlur.png");
		pClouds = getTextureRegion("gfx/clouds.png");
		pBorder = getTextureRegion("gfx/border.png");
		pMolesWarLogo = getTextureRegion("gfx/logo_moles_war.png");
		pTarget = getTextureRegion("gfx/target.png");
		pLeftLabel = getTextureRegion("gfx/left_label.png");
		pRightLabel = getTextureRegion("gfx/right_label.png");
		pPause = getTextureRegion("gfx/pause.png");
		pRepeat = getTextureRegion("gfx/repeat.png");
		pChoose = getTextureRegion("gfx/choose.png");
        killedMoleIcon = getTextureRegion("gfx/killed_mole.png");
		heart = getTextureRegion("gfx/heart.png");
		top_position = getTextureRegion("gfx/top_position.png");
		enter_label = Container.getTextureRegion("gfx/msgBox/score_label.png");

		soundOff = getTextureRegion("gfx/sound_icon_off.png");
		soundOn = getTextureRegion("gfx/sound_icon_on.png");

		pButton = new TextureRegion[7];
		for(int i = 0; i < 7; i++)
			if(i!=6)
				pButton[i] = getTextureRegion("gfx/" + i + ".png");
			else
				pButton[i] = SoundManager.isMuted ? soundOff : soundOn;

		readJSONs();

		pLand = new TextureRegion[3];
        chooseGameBlurBackground = new TextureRegion[4];
		for(int i = 0; i < pLand.length; i++)
			pLand[i] = getTextureRegion("gfx/land" + Integer.toString(i)+".png");

        for(int i = 0; i < chooseGameBlurBackground.length; i++)
            chooseGameBlurBackground[i] = getTextureRegion("gfx/land" + Integer.toString(i)+"_blur.png");
        pLandBlur = chooseGameBlurBackground[0];

		pMoleFrame = getTiledTextureRegion("gfx/mole_animation.png", 4, 4);
		pMolehill = getTiledTextureRegion("gfx/molehill_animation.png", 4, 4);
		pPowers = getTiledTextureRegion("gfx/powers.png", 4, 4);
		pFireFrame = getTiledTextureRegion("gfx/fire_animation.png", 4, 4);
		pBoltFrame = getTiledTextureRegion("gfx/bolt_animation.png", 1, 4);
		pWaterFrame = getTiledTextureRegion("gfx/water_animation.png", 4, 4);
		pHammerScrewdriver = getTiledTextureRegion("gfx/hammer_screwdriver.png", 4, 4);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	public static TextureRegion getTextureRegion1(String pName) {
		Texture t = new Texture(Gdx.files.internal(pName));
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		return new TextureRegion(t, 0, 0, t.getWidth(), t.getHeight());
	}
	 **/

	private static HashMap<String,TextureRegion> textureRegionList = new HashMap<String, TextureRegion>();
	public static TextureRegion getTextureRegion(String filePath) {
		TextureRegion ret = textureRegionList.get(filePath);
		if(ret != null){
			return ret;
		}else
		{
			Texture t = new Texture(Gdx.files.internal(filePath));
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			ret = new TextureRegion(t, 0, 0, t.getWidth(), t.getHeight());
			textureRegionList.put(filePath,ret);
		}

		return ret;
	}


	/**
	 * Zwraca czcionke ale nie dodaje jej do hashMapy
	 *
	 * @param font
	 * @param size
	 * @return
	 */
	public static BitmapFont getTemporalyFont(FontType font, int size) {
		String fontPath = FontType.getFontPath(font);
		for (FontKey key : fonts.keySet()) {
			if (key.fontPath.equals(fontPath) && key.fontSize == size)
				return fonts.get(key);
		}

		System.out.println("create temporaly font:" + fontPath);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.getFontPath(font)));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.FIREBRICK;
		parameter.size = size * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight());
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		BitmapFont ret = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		return ret;
	}

	public static BitmapFont getFont(FontType font, int size, Color color) {
		String fontPath = FontType.getFontPath(font);
		FontKey fontKey = new FontKey(fontPath, size);
		for (FontKey key : fonts.keySet()) {
			if (key.fontPath.equals(fontPath) && key.fontSize == size)
				return fonts.get(key);
		}

		System.out.println("create font:" + fontPath);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.getFontPath(font)));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = color;
		parameter.size = size * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight());
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		BitmapFont ret = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		fonts.put(fontKey, ret);
		return ret;
	}

	public static BitmapFont getFont(FontType font, int size) {
		String fontPath = FontType.getFontPath(font);
		FontKey fontKey = new FontKey(fontPath, size);
		for (FontKey key : fonts.keySet()) {
			if (key.fontPath.equals(fontPath) && key.fontSize == size)
				return fonts.get(key);
		}
		float ratio = ((((float) Gdx.graphics.getWidth()) * size / 4000));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.getFontPath(font)));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = new Color(213f/255f,16f/255f,16f/255f,255);
		parameter.size = (int) (size * ratio);
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		BitmapFont ret = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		fonts.put(fontKey, ret);
		return ret;
	}

	public static BitmapFont getFont(int size) {
		String fontPath = FontType.getFontPath(FontType.ROBOTO_REGULAR);
		FontKey fontKey = new FontKey(fontPath, size);
		for (FontKey key : fonts.keySet()) {
			if (key.fontPath.equals(fontPath) && key.fontSize == size)
				return fonts.get(key);
		}
		float ratio = ((((float) Gdx.graphics.getWidth()) * size / 2500));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.getFontPath(FontType.ROBOTO_REGULAR)));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = new Color(1,1,1,1);
		parameter.size = (int) (size * ratio);
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		BitmapFont ret = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		fonts.put(fontKey, ret);
		return ret;
	}

	private TextureRegion[] getTiledTextureRegion(String pName, int numberOfRows, int numberOfColumns) {
		Texture t = new Texture(Gdx.files.internal(pName));
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion[][] p2DTextureRegion = TextureRegion.split(t, t.getWidth() / numberOfColumns, t.getWidth() / numberOfRows);
		TextureRegion[] p1DTextureRegion = new TextureRegion[numberOfRows * numberOfColumns];

		for(int i = 0; i < numberOfRows; i++)
			System.arraycopy(p2DTextureRegion[i], 0, p1DTextureRegion, i * numberOfColumns, numberOfColumns);

		return p1DTextureRegion;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================



	// ===========================================================
	// Methods
	// ===========================================================

	private void readJSONs() {
		Json pLandsJSON = new Json();
		pLandsJSON.setTypeName(null);
		pLandsJSON.setUsePrototypes(false);
		pLandsJSON.setIgnoreUnknownFields(true);
		pLandsJSON.setOutputType(OutputType.json);

		pLandsData = pLandsJSON.fromJson(LandsData.class, Gdx.files.internal("json/lands.json"));

		pLvlsData = new Lvl[45+1]; //dodatkowy indeks dla mapy deatmatch
		for(int i = 0; i < pLvlsData.length-1; i++) // dodatkowy indeks dla mapy deatmatch
        {
            pLvlsData[i] = pLandsJSON.fromJson(Lvl.class, Gdx.files.internal("json/level" + (i+1) + ".json"));
            Array<PowerJSON> tab = pLvlsData[i].powers;
            for(PowerJSON powerJSON : tab){
                powerJSON.setType();
            }
            PowerJSON.typeIndex = 0;
        }
		pLvlsData[pLvlsData.length-1] = pLandsJSON.fromJson(Lvl.class, Gdx.files.internal("json/level_deathmatch.json"));
		Array<PowerJSON> tab = pLvlsData[pLvlsData.length-1].powers;
		for(PowerJSON powerJSON : tab){
			powerJSON.setType();
		}
		PowerJSON.typeIndex = 0;
	}

    public void readLevelsFromServer() {
        Json pLandsJSON = new Json();
        pLandsJSON.setTypeName(null);
        pLandsJSON.setUsePrototypes(false);
        pLandsJSON.setIgnoreUnknownFields(true);
        pLandsJSON.setOutputType(OutputType.json);

		try {
			pLvlsData = new Lvl[46];
			for (int i = 0; i < pLvlsData.length - 1; i++) {
				pLvlsData[i] = pLandsJSON.fromJson(Lvl.class, Data.getLevelDataJSON(i + 1));
				Array<PowerJSON> tab = pLvlsData[i].powers;
				for (PowerJSON powerJSON : tab) {
					powerJSON.setType();
				}
				PowerJSON.typeIndex = 0;
			}
			pLvlsData[pLvlsData.length - 1] = pLandsJSON.fromJson(Lvl.class, Gdx.files.internal("json/level_deathmatch.json"));
			Array<PowerJSON> tab = pLvlsData[pLvlsData.length - 1].powers;
			for (PowerJSON powerJSON : tab) {
				powerJSON.setType();
			}
			PowerJSON.typeIndex = 0;
		}catch (Exception e){
			e.printStackTrace();
			readJSONs();
		}
    }

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

    /**
     * jeśli level==0 to plik level1.json!!! level==1 to plik level2.json itd.
     * @param level
     * @param powerType
     * @return
     */
    public static int getPowerChanceOnLevel(int level, PowerType powerType){
        for(int i=0;i<pLvlsData[level].powers.size;i++)
            if(pLvlsData[level].powers.get(i).getPowerType().equals(powerType))
                return pLvlsData[level].powers.get(i).getValue();
        return 0;
    }

    public static HashMap<PowerType,Integer> getLvlPowers(int level){
        HashMap<PowerType,Integer> map = new HashMap<PowerType, Integer>();
        for(int i=0;i<pLvlsData[level].powers.size;i++)
            map.put(pLvlsData[level].powers.get(i).getPowerType(),pLvlsData[level].powers.get(i).getValue());
        return map;
    }


    public static class LandsData {
		Land[] lands;
	}

	public static class Land {
		String texture_name;
	}

	public static class Lvl {
		public int[] molehills_fields;
		public int[] targets_fields;
        public Array<PowerJSON> powers;
		public int columns;
		public int rows;
		public int molehills;
		public int moles;
		public int spawnSpeed;
		public int runSpeed;
		public int maxShieldCount;
        public int mapType;
	}


    public static class PowerJSON{
        public int ERASE;
        public int WATER;
        public int LIGHTBOLT;
        public int FIRE;
        public int HAMMER;
        public int SCREWDIVER;
        public int POISON;
        public int GUN;

        public PowerType type;
        private static int typeIndex = 0;
        private PowerType[] types = {PowerType.ERASE,PowerType.WATER,PowerType.LIGHTBOLT
                    ,PowerType.FIRE,PowerType.HAMMER,PowerType.SCREWDIVER,PowerType.POISON,PowerType.GUN};

        public void setType(){
            if(ERASE>0)type=PowerType.ERASE;
            if(WATER>0)type=PowerType.WATER;
            if(LIGHTBOLT>0)type=PowerType.LIGHTBOLT;
            if(FIRE>0)type=PowerType.FIRE;
            if(HAMMER>0)type=PowerType.HAMMER;
            if(SCREWDIVER>0)type=PowerType.SCREWDIVER;
            if(POISON>0)type=PowerType.POISON;
            if(GUN>0)type=PowerType.GUN;

            //nie zostala ustalona wartosc dla zadnego wiec:
            if(type==null){
                type = getNextType();
                typeIndex++;
                if(typeIndex>types.length)
                    typeIndex = 0;
            }
        }

        private PowerType getNextType(){
            return types[typeIndex];
        }

        public int getValue(){
            if(type.equals(PowerType.ERASE))return ERASE;
            if(type.equals(PowerType.WATER))return WATER;
            if(type.equals(PowerType.LIGHTBOLT))return LIGHTBOLT;
            if(type.equals(PowerType.FIRE))return FIRE;
            if(type.equals(PowerType.HAMMER))return HAMMER;
            if(type.equals(PowerType.SCREWDIVER))return SCREWDIVER;
            if(type.equals(PowerType.POISON))return POISON;
            if(type.equals(PowerType.GUN))return GUN;
            return 0;
        }

        public PowerType getPowerType(){
            return type;
        }
    }

	private static class FontKey {
		public String fontPath;
		public int fontSize;

		public FontKey(String fontPath, int fontSize) {
			this.fontPath = fontPath;
			this.fontSize = fontSize;
		}
	}
}

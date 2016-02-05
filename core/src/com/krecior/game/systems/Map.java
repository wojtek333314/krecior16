package com.krecior.game.systems;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.PhysicObject;
import com.krecior.utils.Container;
import com.krecior.utils.UserData;

public class Map {
	public static final float GAP = 0.05f * GameScreen.METER_W;
	public static final float SECOND_GAP = 0.085f * GameScreen.METER_W;

	private GameScreen gameHandle;
	private Image background;
    private Stage mainStage;
	private float backgroundHeight;
	private int actualLevel = 0;

	public Map(GameScreen mGameScreen,int level) {
		gameHandle = mGameScreen;
        actualLevel = level;
		createBackground();
		createBorders();
	}

    public int getActualLevel() {
        return actualLevel;
    }

	private void createBorders() {
		for (int i = 0; i < 32; i++) {
			Body pBorderBody;
			BodyDef pBorderBodyDef = new BodyDef();
			pBorderBodyDef.type = BodyDef.BodyType.StaticBody;

			int modulo = i % 4;

			switch (modulo) {
				case 0:
					pBorderBodyDef.position.set(GameScreen.METER_W / 2, (GAP + ((i-1) / 4) * SECOND_GAP) + GameScreen.PLAY_FIELD_TOP_GAP * backgroundHeight /
							GameScreen.H * GameScreen.METER_H + GameScreen.BORDER_PHYSIC_SIZE);
					break;
				case 1:
					pBorderBodyDef.position.set((GAP + i / 4 * SECOND_GAP) + ((1 + GameScreen.PLAY_FIELD_WIDTH) * GameScreen.METER_W) / 2  + GameScreen.BORDER_PHYSIC_SIZE,
							(GameScreen.PLAY_FIELD_TOP_GAP * backgroundHeight / GameScreen.H -
									GameScreen.PLAY_FIELD_HEIGHT / 2) * GameScreen.METER_H);
					break;
				case 2:
					pBorderBodyDef.position.set(GameScreen.METER_W / 2, -(GAP + i / 4 * SECOND_GAP) + GameScreen.PLAY_FIELD_BOT_GAP * backgroundHeight /
							GameScreen.H * GameScreen.METER_H - GameScreen.BORDER_PHYSIC_SIZE);
					break;
				case 3:
					pBorderBodyDef.position.set(-(GAP + i / 4 * SECOND_GAP) + ((1 - GameScreen.PLAY_FIELD_WIDTH) * GameScreen.METER_W ) / 2 - GameScreen.BORDER_PHYSIC_SIZE,
							(GameScreen.PLAY_FIELD_TOP_GAP * backgroundHeight / GameScreen.H -
									GameScreen.PLAY_FIELD_HEIGHT / 2) * GameScreen.METER_H);
					break;
			}

			pBorderBodyDef.fixedRotation = true;
			pBorderBody = gameHandle.getPhysicWorld().createBody(pBorderBodyDef);

			PolygonShape pBorderPolygonShape = new PolygonShape();

			switch (modulo) {
			case 0: pBorderPolygonShape.setAsBox(GameScreen.METER_W * i / 4 * (1f+SECOND_GAP) * (float)Math.sqrt(2),
					GameScreen.BORDER_PHYSIC_SIZE); break;
			case 1: pBorderPolygonShape.setAsBox(GameScreen.BORDER_PHYSIC_SIZE,
					GameScreen.METER_H * i / 4 * (1f+SECOND_GAP) * (float)Math.sqrt(2)); break;
			case 2: pBorderPolygonShape.setAsBox(GameScreen.METER_W * i / 4 * (1f+SECOND_GAP) * (float)Math.sqrt(2),
					GameScreen.BORDER_PHYSIC_SIZE); break;
			case 3: pBorderPolygonShape.setAsBox(GameScreen.BORDER_PHYSIC_SIZE,
					GameScreen.METER_H * i / 4 * (1f+SECOND_GAP) * (float)Math.sqrt(2)); break;
			}

			FixtureDef pWeaponFixtureDef = new FixtureDef();
			pWeaponFixtureDef.shape = pBorderPolygonShape;
			pWeaponFixtureDef.density = 1f;
			pWeaponFixtureDef.filter.categoryBits = Container.CATEGORY_BORDER;
			pWeaponFixtureDef.filter.maskBits = Container.MASK_BORDER;
			pWeaponFixtureDef.isSensor = true;

			pBorderBody.createFixture(pWeaponFixtureDef);
			pBorderPolygonShape.dispose();

			UserData ud = new UserData(0, PhysicObject.BORDER);
			
			switch (modulo) {
			case 0: ud = new UserData(i, PhysicObject.BORDER); break;
			case 1: ud = new UserData(i, PhysicObject.BORDER); break;
			case 2: ud = new UserData(i, PhysicObject.BORDER); break;
			case 3: ud = new UserData(i, PhysicObject.BORDER); break;
			}
			
			pBorderBody.setUserData(ud);
		}
	}
	
	public void render() {
		mainStage.draw();
	}

	private void createBackground() {
		mainStage = new Stage();

        background = new Image(Container.pLand[Container.pLvlsData[getActualLevel()].mapType]);
        backgroundHeight = GameScreen.W * background.getHeight() / background.getWidth();
        mainStage.addActor(background);
		background.setSize(GameScreen.W, backgroundHeight);
		background.setOrigin(0, 0);
		background.setPosition(0, 0);
	}
}

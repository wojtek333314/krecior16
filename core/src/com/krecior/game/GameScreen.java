package com.krecior.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.krecior.Manager;
import com.krecior.game.enums.PowerType;
import com.krecior.game.hud.Hud;
import com.krecior.game.objects.Field;
import com.krecior.game.objects.Hill;
import com.krecior.game.objects.Mole;
import com.krecior.game.objects.Target;
import com.krecior.game.systems.DeathmatchManager;
import com.krecior.game.systems.EffectSystem;
import com.krecior.game.systems.FieldManager;
import com.krecior.game.systems.HillManager;
import com.krecior.game.systems.Map;
import com.krecior.game.systems.MoleManager;
import com.krecior.game.systems.MoleThrow;
import com.krecior.game.systems.PhysicsContacts;
import com.krecior.game.systems.PowerManager;
import com.krecior.game.systems.ScoreManager;
import com.krecior.game.systems.TargetManager;
import com.krecior.game.systems.TouchProcessor;
import com.krecior.menu.ScreenType;
import com.krecior.menu.objects.MenuMsgBox;
import com.krecior.utils.Container;
import com.krecior.utils.Data;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameScreen implements Screen {
	public static final float METER_W = Gdx.graphics.getWidth() * 0.02f;
	public static final float METER_H = Gdx.graphics.getHeight() * 0.02f;
	public static final float W = Gdx.graphics.getWidth();
	public static final float H = Gdx.graphics.getHeight();
	public static final float PLAY_FIELD_WIDTH = 0.6597222f;
	public static final float PLAY_FIELD_HEIGHT = 0.498046875f;
	public static final float PLAY_FIELD_TOP_GAP = 0.638671875f;
	public static final float PLAY_FIELD_BOT_GAP = 0.140625f;
	public static final float MAX_MOLE_SPEED = (float) Math.sqrt(Math.pow(Gdx.graphics.getWidth(), 2) +
											   		   Math.pow(Gdx.graphics.getHeight(), 2));
	public static final float AWAY_FROM_MAP_SPEED = 2f;
	public static final float BORDER_PHYSIC_SIZE = METER_W * 0.02f;

	public static GameScreen pGame;
	public static OrthographicCamera pCamera;
	public static World pWorld;
	public static Box2DDebugRenderer b2dr;
	private Random pRandom;
	private Hud pHud;
	private DeathmatchManager deathmatchManager;
	private MoleThrow pMoleThrow;
	private TouchProcessor pTouchProcessor;
	private PhysicsContacts pPhysicsContacts;
	private Map pMap;
	private MoleManager pMoleManager;
	private HillManager pHillManager;
	private TargetManager pTargetManager;
	private FieldManager pFieldManager;
	private PowerManager pPowerManager;
    private EffectSystem pEffectSystem;
	private ScoreManager scoreManager;
	private InputMultiplexer pInputMultiplexer;
	private CopyOnWriteArrayList<Body> pBodyToDispose;

    private int actualLevel;
	public static boolean physicSimulation = false;
	private boolean deathmatch = false;
	public boolean canTouchMole = true;
	public static float TIME_STEP;
	public static int COLUMNS;
	public static int ROWS;

	public GameScreen(int level) {
        actualLevel = level;
		if(actualLevel < Container.pLvlsData.length-1)
			deathmatch = false;
		else
			deathmatch = true;
        System.out.println(Container.pLvlsData.length + "/:/"+actualLevel);
        Gdx.input.setCatchBackKey(true);
    }

    public int getActualLevel() {
        return actualLevel;
    }

    public CopyOnWriteArrayList<Mole> getMoles() {
		return pMoleManager.getMoles();
	}
	
	public CopyOnWriteArrayList<Target> getTargets() {
		return pTargetManager.getTargets();
	}
	
	public CopyOnWriteArrayList<Field> getFields() {
		return pFieldManager.getFields();
	}
	
	public CopyOnWriteArrayList<Hill> getHills() {
		return pHillManager.getHills();
	}
	
	public World getPhysicWorld() {
		return pWorld;
	}
	
	public Random getRandom() {
		return pRandom;
	}
	
	public Map getMap() {
		return pMap;
	}
	
	public MoleManager getMoleManager() {
		return pMoleManager;
	}
	
	public PhysicsContacts getPhysicContacts() {
		return pPhysicsContacts;
	}
	
	public MoleThrow getMoleThrow() {
		return pMoleThrow;
	}
	
	public TouchProcessor getTouchProcessor() {
		return pTouchProcessor;
	}
	
	public HillManager getHillManager() {
		return pHillManager;
	}

	public ScoreManager getScoreManager() {
		return scoreManager;
	}
	
	public TargetManager getTargetManager() {
		return pTargetManager;
	}
	
	public FieldManager getFieldManager() { return pFieldManager; }

	public PowerManager getPowerManager() { return pPowerManager;}

	public EffectSystem getEffectSystem() { return pEffectSystem; }

	public DeathmatchManager getDeathmatchManager() { return deathmatchManager; }

	public Hud getHud() { return pHud; }

	public boolean isDeathmatch() { return deathmatch; }

    public void addBodyToDispose(Body b) {
        pBodyToDispose.add(b);
    }

	public static void startPhysicSimulation() {
		physicSimulation = true;
		onPhysicSimulation();
	}

	private static void onPhysicSimulation() {
		TIME_STEP = Gdx.graphics.getDeltaTime();
	}

	public static void stopPhysicSimulation() {
		physicSimulation = false;
		TIME_STEP = 0;
	}

    public void nextLevel() {
        Manager.manager.startLevel(pMap.getActualLevel() + 1);
    }

    public void repeatLevel() {
        Manager.manager.startLevel(pMap.getActualLevel());
    }

	@Override
	public void show() {
		pRandom = new Random();
		pGame = this;

		pCamera = new OrthographicCamera(METER_W, METER_H);
		pCamera.position.set(METER_W / 2, METER_H / 2, 0);
		pCamera.update();
		
		pWorld = new World(new Vector2(0, 0), false);
		
		b2dr = new Box2DDebugRenderer();

		pMap = new Map(this, actualLevel);
		COLUMNS = Container.pLvlsData[pMap.getActualLevel()].columns;
		ROWS = Container.pLvlsData[pMap.getActualLevel()].rows;
        pInputMultiplexer = new InputMultiplexer();

		pFieldManager = new FieldManager();
		pMoleThrow = new MoleThrow();
		pPhysicsContacts = new PhysicsContacts(this);
		pPowerManager = new PowerManager(this);
		pMoleManager = new MoleManager(this);
		scoreManager = new ScoreManager(this);
		pHud = new Hud(this);
		pHillManager = new HillManager(this);
		pTargetManager = new TargetManager(this);
        pEffectSystem = new EffectSystem(this);

		if(deathmatch)
			deathmatchManager = new DeathmatchManager(this);


		pBodyToDispose = new CopyOnWriteArrayList<Body>();

        //if(deathmatch)
		//for(int j = 0; j < 2; j++)
		//	for(int i = 0; i < PowerType.values().length-2; i++)
		//		getPowerManager().addPower(PowerType.values()[i]);

	    pInputMultiplexer.addProcessor(pTouchProcessor = new TouchProcessor(this));
		pInputMultiplexer.addProcessor(pHud.getStage());

	    Gdx.input.setInputProcessor(pInputMultiplexer);

        pGame = this;

        if(Data.isTutorialShowed())
		    startPhysicSimulation();
        else
        {
            final MenuMsgBox tutorial = new MenuMsgBox(getHud().getStage(),"Tap mole, hold\nand throw him to shield","OK");
            tutorial.setButtonClickListener(new MenuMsgBox.onButtonClickedListener() {
                @Override
                public void onClick(int button) {
                    Data.setTutorialShowed();
                    System.out.println("click");
                    tutorial.hide();
                    tutorial.setButtonClickListener(null);
                    startPhysicSimulation();
                }
            });
            tutorial.show(pInputMultiplexer);
        }
	}

    public InputMultiplexer getpInputMultiplexer() {
        return pInputMultiplexer;
    }

    @Override
	public void render(float delta) {
		Gdx.gl.glClearColor(58 / 256f, 136 / 256f, 231 / 256f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pMap.render();
		pHillManager.manage();
		pTargetManager.manage();
		pMoleManager.manage();
		pPowerManager.render();
        pEffectSystem.render();
		pHud.render();
		scoreManager.manage();

		if(deathmatch) {
			deathmatchManager.manage();
		}

		pMoleThrow.clock();

		disposeBody();

		if (physicSimulation)
			onPhysicSimulation();

		pWorld.step(TIME_STEP, 8, 3);
		//b2dr.render(pWorld, pCamera.combined);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
			if(isDeathmatch())
				Manager.manager.changeScreen(ScreenType.MAIN_SCREEN);
			else
				Manager.manager.changeScreen(ScreenType.CHOOSE_STAGE);
		}
	}

	@Override
	public void dispose() {
		pWorld.dispose();
		b2dr.dispose();
	}


	private void disposeBody() {
		for(Body b : pBodyToDispose) {
			if(!pWorld.isLocked()) {
				b.setTransform(METER_W, METER_H, 0);
                b.setActive(false);
                pBodyToDispose.remove(b);
			}
		}
	}

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }
}
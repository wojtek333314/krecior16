package com.krecior.game.systems;

import com.krecior.game.GameScreen;
import com.krecior.game.enums.FieldType;
import com.krecior.game.objects.Hill;
import com.krecior.game.objects.Mole;
import com.krecior.utils.Container;

public class DeathmatchManager {
    public static final float MOLE_AWAY_LIVES = 1f;
    public static final float MOLE_SPEED_RISE = 1f / 300f;
    public static final float MOLE_SPAWN_RISE = 1f / 100f;
    public static final float NEW_HILL_TIME = 10f;

    private GameScreen gameScreen;

    private float time = 0;
    private float hillTime = 10;
    private boolean afterEnd = false;

    public float lives = 20f;

    public DeathmatchManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }


    public void manage() {
        time += GameScreen.TIME_STEP;

        if(time >= 1f) {
            Mole.SPAWN_TIME += MOLE_SPAWN_RISE;
            Mole.WALK_SPEED += MOLE_SPEED_RISE;
            Hill.TIME_TO_OPEN = 2f / Mole.SPAWN_TIME;

            hillTime += 1f;
            time = 0f;
        }

        if(hillTime >= NEW_HILL_TIME) {
            createNextHill();

            hillTime = 0f;
        }

        if(lives <= 0f && !afterEnd) {
            gameScreen.getHud().popUpDMend();
            afterEnd = true;
            gameScreen.canTouchMole = false;
        }
    }


    private void createNextHill() {
        int a = Container.pLvlsData[gameScreen.getMap().getActualLevel()].molehills_fields.length;
        if(gameScreen.getHillManager().getNumberOfHills() <  a) {
            gameScreen.getHills().add(
                    new Hill(gameScreen.getFields().get(
                            Container.pLvlsData[gameScreen.getActualLevel()].molehills_fields[gameScreen.getHillManager()
                                    .getNumberOfHills()]).getPosition()));
            gameScreen.getFields().get(a).setType(FieldType.MOLEHILL);
            gameScreen.getHillManager().increamentNumberOfHills();
        }
    }
}

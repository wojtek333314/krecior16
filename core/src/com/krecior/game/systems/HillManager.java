package com.krecior.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.FieldType;
import com.krecior.game.objects.Hill;
import com.krecior.utils.Container;

import java.util.concurrent.CopyOnWriteArrayList;

public class HillManager {
    public static final float TIME = 2f;

    private CopyOnWriteArrayList<Hill> pHills;
    private GameScreen pGame;
    private SpriteBatch pSpriteBatch;

    private float pTime = 0;
    private int pMolehills = 0;




    public HillManager(GameScreen mGame) {
        pGame = mGame;
        pSpriteBatch = new SpriteBatch();
        pHills = new CopyOnWriteArrayList<Hill>();
        createHills();
    }

    public CopyOnWriteArrayList<Hill> getHills() {
        return pHills;
    }

    public void increamentNumberOfHills() {
        pMolehills++;
    }

    public int getNumberOfHills() { return pMolehills; }


    private void createHills() {
        if(!pGame.isDeathmatch()) {
            int k = pGame.getRandom().nextInt(Container.pLvlsData[pGame.getMap().getActualLevel()].molehills_fields.length);
            int a = Container.pLvlsData[pGame.getMap().getActualLevel()].molehills_fields[k];

            pHills.add(new Hill(pGame.getFields().get(a).getPosition()));
            pGame.getFields().get(a).setType(FieldType.MOLEHILL);
            pMolehills++;
        }
    }

    public void manage() {
        if(!pGame.isDeathmatch()) {
            int k = pGame.getRandom().nextInt(Container.pLvlsData[pGame.getMap().getActualLevel()].molehills_fields.length);
            int a = Container.pLvlsData[pGame.getMap().getActualLevel()].molehills_fields[k];

            if (pTime >= Container.pLvlsData[pGame.getMap().getActualLevel()].spawnSpeed
                    && pGame.getFields().get(a).getType() != FieldType.MOLEHILL
                    && pMolehills < Container.pLvlsData[pGame.getMap().getActualLevel()].molehills) {
                pHills.add(new Hill(pGame.getFields().get(a).getPosition()));
                pGame.getFields().get(a).setType(FieldType.MOLEHILL);
                pMolehills++;
                pTime = 0;
            }
        }

        renderHills();
        pTime += GameScreen.TIME_STEP;
    }

    private void renderHills() {
        pSpriteBatch.begin();

        for (Hill h : pHills) {
            h.draw(pSpriteBatch);
            h.setTime(h.getTime() + GameScreen.TIME_STEP);
        }

        pSpriteBatch.end();
    }
}

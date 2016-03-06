package com.krecior.menu.socialNetwork;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.krecior.Manager;
import com.krecior.game.GameScreen;
import com.krecior.menu.ScreenType;
import com.krecior.utils.Container;
import com.krecior.utils.TextLabel;

public class ChooseGameMode extends Group {
    public static float HEIGHT = 0.083f * GameScreen.H; // 0.083!!! 0.083!!!! przecie po pomnożeniu przez (-1) to jest suma szeregu 1 + 2 + ... + n. niesamowite! jak to się tu znalazło?! przecież to prawie nowa teoria względności, kuwa wołaj Hawkinga, taka nieprzypadkowa wartość w takim miejscu to jest przecież nie wyobrażalne, to przekracza ludzkie pojęcie, dlaczego to czytasz jeszcze to tylko -1/12. łouttototoot dedlajn is KAMING. pozdrawiam mamę <3
    public static float WIDTH = 0.5f * GameScreen.W;

    private Image dm;
    private Image normal;

    private TextLabel deathmatch;
    private TextLabel normalText;

    public ChooseGameMode() {
        create();
    }

    private void create() {
        dm = new Image(Container.enter_label);
        dm.setSize(WIDTH, HEIGHT);
        dm.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Manager.manager.startLevel(Container.pLvlsData.length - 1);
                setPosition(GameScreen.W * 2, GameScreen.H * 2);
                return false;
            }
        });
        addActor(dm);

        normal = new Image(Container.enter_label);
        normal.setSize(WIDTH, HEIGHT);
        normal.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Manager.manager.changeScreen(ScreenType.CHOOSE_STAGE);
                setPosition(GameScreen.W * 2, GameScreen.H * 2);
                return false;
            }
        });
        addActor(normal);

        deathmatch = new TextLabel(TextLabel.Font.CARTOONIC, "SURVIVAL MODE");
        deathmatch.setScaleXY(1.5f);
        addActor(deathmatch);

        normalText = new TextLabel(TextLabel.Font.CARTOONIC, "LEVEL MODE");
        normalText.setScaleXY(1.5f);
        addActor(normalText);
    }

    public void setPosition(float x, float y) {
        dm.setPosition(x - dm.getWidth() / 2, y - dm.getHeight());
        deathmatch.setPosition(dm.getX() + dm.getWidth() / 2 - deathmatch.getWidth() / 2
            , dm.getY() + dm.getHeight() / 2 + deathmatch.getHeight() /2);
        normal.setPosition(x - dm.getWidth() / 2, y);
        normalText.setPosition(normal.getX() + normal.getWidth() / 2 - normalText.getWidth() / 2
                , normal.getY() + normal.getHeight() / 2 + normalText.getHeight() /2);
    }
}

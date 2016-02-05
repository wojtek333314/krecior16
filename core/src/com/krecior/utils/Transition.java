package com.krecior.utils;

import com.badlogic.gdx.Game;
import com.krecior.BaseScreen;

/**
 * Created by Wojciech Osak on 2015-09-29.
 */
public class Transition {
    private BaseScreen from,to;
    private Game game;

    public Transition(Game gameHandle, BaseScreen from, BaseScreen to) {
        this.from = from;
        this.to = to;
        game = gameHandle;
    }

    public void start(){
        game.setScreen(to);
        /* from.getMainStage().addAction(Actions.sequence(Actions.alpha(0.5f, 1f), Actions.after(new Action() {
            @Override
            public boolean act(float delta) {
                System.out.println("A");
                to.getMainStage().addAction(Actions.sequence(Actions.fadeIn(1f)));
                game.setScreen(to);
                return true;
            }
        })));*/

    }
}

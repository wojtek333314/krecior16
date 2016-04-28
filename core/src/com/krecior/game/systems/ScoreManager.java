package com.krecior.game.systems;

import com.krecior.game.GameScreen;
import com.krecior.game.objects.Mole;
import com.krecior.menu.achievements.system.Achievement;

public class ScoreManager {
    private GameScreen game;

    private boolean checking = true;

    private int killedMoles = 0;
    private int destroyedTargets = 0;
    private int diamonds = 0;
    private int endDiamonds = 0;
    private int points = 0;
    private int deserters = 0;
    private int born = 0;

    private float result = 0;

    public ScoreManager(GameScreen gameScreen) { this.game = gameScreen; }

    public int getPoints() { return points; }

    public int getDiamonds() { return diamonds; }

    public int getKilledMoles() { return killedMoles; }

    public int getDestroyedTargets() { return destroyedTargets; }

    public int getDeserters() { return deserters; }

    public int getEndDiamonds() { return endDiamonds; }

    public int getBorn() { return born; }

    public void increaseBorn() { born++; }

    public void increaseDeserters() { deserters++; }

    public void increaseDestroyedTargets() { destroyedTargets++; }

    public void increaseKilledMoles() { killedMoles++;
        Achievement.addValue(Achievement.ELIMINATED_MOLES_10K, 1);
    }

    public void addPoints(int mPointsToAdd) {
        points += mPointsToAdd;
    }

    public void addDiamonds(int mDiamondsToAdd) { diamonds += mDiamondsToAdd; }

    public void manage() {
        System.out.println("killed: " + killedMoles);
        System.out.println("deserters: " + deserters);
        System.out.println("targets: " + destroyedTargets);
        if(checking) {
            result = (float)(killedMoles + destroyedTargets) / game.getMoleManager().getAllMoles();
            if (result >= 0.4f) endDiamonds = 1;
            if (result >= 0.6f) endDiamonds = 2;
            if (result >= 0.8f) endDiamonds = 3;
            if (result >= 0.4f && game.getHillManager().getHills().size() == 0) endDiamonds = 1;
        }
    }

    public void killMole(Mole m) {
        if(!m.isKilled())
            increaseKilledMoles();
        m.setKilled(true);
    }

    public void addDeserterMole(Mole m) {
        if(!m.isKilled())
            increaseDeserters();
        m.setKilled(true);

        if(game.isDeathmatch()) {
            game.getDeathmatchManager().lives -= 1f;
        }
    }

    public void addBadThrowMole(Mole m) {
        if (!m.isKilled())
            increaseDeserters();
        m.setKilled(true);
        points += 25;


        if (game.isDeathmatch()) {
            game.getDeathmatchManager().lives -= 0.1f;
        }
    }

    public void stop() {
        checking = false;
    }
}

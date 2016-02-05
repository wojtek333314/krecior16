package com.krecior.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.FieldType;
import com.krecior.game.enums.PowerType;
import com.krecior.game.objects.Target;
import com.krecior.utils.Container;

import java.util.concurrent.CopyOnWriteArrayList;

public class TargetManager {
	private CopyOnWriteArrayList<Target> pTargets;
	private GameScreen pGame;
	private SpriteBatch pSpriteBatch;


	public TargetManager(GameScreen mGame) {
		pGame = mGame;
        pSpriteBatch = new SpriteBatch();

		pTargets = new CopyOnWriteArrayList<Target>();
		createTargets();
	}

	public CopyOnWriteArrayList<Target> getTargets() {
		return pTargets;
	}

	public PowerType getAvailablePowerType() {
		PowerType p;
		p = PowerType.random(pGame.getActualLevel());
		return p;
	}

	private void createTargets() {
		pTargets = new CopyOnWriteArrayList<Target>();

		for(int i = 0; i < Container.pLvlsData[pGame.getMap().getActualLevel()].targets_fields.length; i++) {
			int x = pGame.getRandom().nextInt(Container.pLvlsData[pGame.getMap().getActualLevel()].targets_fields.length);
			int a = Container.pLvlsData[pGame.getMap().getActualLevel()].targets_fields[x];

			if(pGame.getFields().get(a).getType() == FieldType.TARGET) {
				i--;
				continue;
			}

			PowerType powerType = pGame.isDeathmatch() ? PowerType.NORMAL : getAvailablePowerType();
			pTargets.add(new Target(i, pGame.getFields().get(a).getPosition(), powerType));
			if(pGame.isDeathmatch()) {
				if (x == 0 || x == Container.pLvlsData[pGame.getMap().getActualLevel()].targets_fields.length - 1)
					pTargets.get(pTargets.size() - 1).setTexture(Container.pPowers[13]);

				if (x == 1 || x == Container.pLvlsData[pGame.getMap().getActualLevel()].targets_fields.length - 2)
					pTargets.get(pTargets.size() - 1).setTexture(Container.pPowers[12]);

				if (x == 3 || x == Container.pLvlsData[pGame.getMap().getActualLevel()].targets_fields.length - 3)
					pTargets.get(pTargets.size() - 1).setTexture(Container.pPowers[14]);
			}
			pTargets.get(pTargets.size()-1).setFieldID(a);
			pGame.getFields().get(a).setType(FieldType.TARGET);
		}
	}

	public void manage() {
		for(int i = 0; i < pTargets.size(); i++) {
			if(!pTargets.get(i).getState() && !pGame.isDeathmatch()) {
					pTargets.get(i).reUse((pGame.getFields().get(pTargets.get(i).getFieldID())).getPosition());
					pTargets.get(i).setState(true);
				}
			}
		renderTargets();
	}

	private void renderTargets() {
		pSpriteBatch.begin();

		for(Target t : pTargets) {
			t.draw(pSpriteBatch);
		}

		pSpriteBatch.end();
	}
}

package com.krecior.game.systems;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.krecior.game.GameScreen;
import com.krecior.game.enums.FieldType;
import com.krecior.game.enums.MoleState;
import com.krecior.game.enums.PhysicObject;
import com.krecior.game.enums.PowerType;
import com.krecior.game.objects.Mole;
import com.krecior.game.objects.Target;
import com.krecior.utils.Container;
import com.krecior.utils.UserData;

public class PhysicsContacts {
    private GameScreen pGame;

	public PhysicsContacts(GameScreen mGame) {
		pGame = mGame;
		
		createContactListener();
	}

	private void createContactListener() {
		pGame.getPhysicWorld().setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();


				if(isCollide(fixtureA, fixtureB, PhysicObject.BORDER, PhysicObject.MOLE)) {//wyrzucenie za plansze
					Mole mole = getMole(fixtureA, fixtureB);

					if(mole.getState() == MoleState.FLY) {
						mole.scaling();
								
						int k = pGame.getRandom().nextInt(2);

						if(k == 0)
							k = -1;

						mole.setTwistDirection(k);
						mole.setLinearVelocity((float) -Math.cos(getMole(fixtureA, fixtureB).getAngle()) * 2f,
								(float) -Math.sin(getMole(fixtureA, fixtureB).getAngle()) * 2f);

						pGame.getScoreManager().addBadThrowMole(mole);
					}
					
					if(mole.getState() == MoleState.GRABBED) {
						float angle = pGame.getMoleThrow().getLastAngle() - (float)Math.toRadians(180);
						pGame.getMoleThrow().setPositioning(false);
						pGame.getMoleThrow().clear();
						
						mole.scaling();
						
						int k = pGame.getRandom().nextInt(2);
								
						if(k == 0)
							k = -1;

						mole.setTwistDirection(k);

						if(angle ==  -(float)Math.toRadians(180)) {
							mole.setLinearVelocity((float) Math.cos((Math.toRadians(getID(fixtureA, fixtureB) - 1) * 90f))
											* GameScreen.AWAY_FROM_MAP_SPEED,
									(float) Math.sin((Math.toRadians(getID(fixtureA, fixtureB) - 1) * 90f))
											* GameScreen.AWAY_FROM_MAP_SPEED);
						} else {
							mole.setLinearVelocity((float) Math.cos(angle) * GameScreen.AWAY_FROM_MAP_SPEED,
									(float) Math.sin(angle) * GameScreen.AWAY_FROM_MAP_SPEED);
						}

						pGame.getScoreManager().addBadThrowMole(mole);
					}
					
								
					if(getID(fixtureA, fixtureB) == 1 || getID(fixtureA, fixtureB) == 3)
						if(mole.getState() == MoleState.WALK) {
							mole.disappear();

							pGame.getScoreManager().addDeserterMole(mole);
						}
				}
				
				
				if(isCollide(fixtureA, fixtureB, PhysicObject.MOLE, PhysicObject.TARGET)) {
					Target target = getTarget(fixtureA, fixtureB);
					Mole mole = getMole(fixtureA, fixtureB);

					if(mole.getState() == MoleState.FLY || mole.getState() == MoleState.GRABBED ||
							mole.getState() == MoleState.WALK) {
						if(!pGame.isDeathmatch()) {
							target.setState(false);
							target.hit();
							pGame.addBodyToDispose(target.getBody());
						}
						mole.setState(MoleState.HIT);
						targetResponse(target, target.getPower());
						pGame.getScoreManager().increaseDestroyedTargets();
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
			}

		});
	}

	private UserData getUserData(Fixture mFixture) {
		return ((UserData) mFixture.getBody().getUserData());
	}

	private int getID(Fixture fixtureA, Fixture fixtureB) {
		return ((UserData) getFixtureOfPhysicObject(fixtureA, fixtureB,
				PhysicObject.BORDER).getBody().getUserData()).getID();
	}

	private Fixture getFixtureOfPhysicObject(Fixture fixtureA, Fixture fixtureB,
											 PhysicObject mPhysicObject) {
		return getUserData(fixtureA).getType().equals(mPhysicObject) ? fixtureA : fixtureB;
	}
	
	private Mole getMole(Fixture fixtureA, Fixture fixtureB) {
		return pGame.getMoles().get(((UserData) getFixtureOfPhysicObject(fixtureA, fixtureB,
				PhysicObject.MOLE).getBody().getUserData()).getID());
	}
	
	private Target getTarget(Fixture fixtureA, Fixture fixtureB) {
		return pGame.getTargets().get(((UserData) getFixtureOfPhysicObject(fixtureA, fixtureB,
				PhysicObject.TARGET).getBody().getUserData()).getID());
	}

	private boolean isCollide(Fixture fixtureA, Fixture fixtureB,
							  PhysicObject mFirstObject, PhysicObject mSecondObject) {
		PhysicObject mFirst = getUserData(fixtureA).getType();
		PhysicObject mSecond = getUserData(fixtureB).getType();

		return (mFirstObject.equals(mFirst) && mSecondObject.equals(mSecond)) || 
			   (mFirstObject.equals(mSecond) && mSecondObject.equals(mFirst));
	}
	
	public void targetResponse(Target t, PowerType mPowerType) {
		switch(mPowerType) {
		case DIAMOND:
			pGame.getScoreManager().addDiamonds(1);
			break;
		case FIRE:
            addPower(mPowerType);
			break;
		case HAMMER:
            addPower(mPowerType);
			break;
		case NORMAL:
			if(pGame.isDeathmatch()) {
				if (t.getFieldID() == 0 || t.getFieldID() == 10)
					pGame.getScoreManager().addPoints(500);

				if (t.getFieldID() == 2 || t.getFieldID() == 8)
					pGame.getScoreManager().addPoints(250);

				if (t.getFieldID() == 4 || t.getFieldID() == 6)
					pGame.getScoreManager().addPoints(100);
			} else
				pGame.getScoreManager().addPoints(100);
			break;
        case GUN:
            addPower(mPowerType);
			break;
		case POISON:
            addPower(mPowerType);
            break;
        case ERASE:
            addPower(mPowerType);
            break;
        case SCREWDIVER:
            addPower(mPowerType);
            break;
		case LIGHTBOLT:
            addPower(mPowerType);
            break;
		case WATER:
            addPower(mPowerType);
            break;
        default:
			break;
		
		}
	}

    private void addPower(PowerType mPowerType) {
        pGame.getPowerManager().addPower(mPowerType);
    }

	//===========================================================
	//Inner and Anonymous Classes
	//===========================================================
}

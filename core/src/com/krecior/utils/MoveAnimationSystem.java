package com.krecior.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Wojciech Osak on 2015-11-09.
 */
public class MoveAnimationSystem {
    private Actor actor;
    private float time;
    private boolean isMoving = false;
    private float wholeTime;
    private boolean moveToLeft;
    private float distance;
    private float destinationX;

    public MoveAnimationSystem(Actor actor) {
        this.actor = actor;
    }

    public void startAnimation(boolean moveToLeft, float time){
        this.time = time;
        this.moveToLeft = moveToLeft;
        isMoving = true;
        wholeTime = 0f;
        this.distance = Gdx.app.getGraphics().getWidth();
        if(moveToLeft)
            this.destinationX = actor.getX() - this.distance;
        else
            this.destinationX = actor.getX() + this.distance;
    }

    public void onAnimationDraw(float delta){
        wholeTime+=delta;

        if(moveToLeft && isMoving){
            if(actor.getX() > destinationX)
            {
                actor.setPosition(actor.getX() - (delta/time) * distance,0);
                isMoving = true;
            }else{
                isMoving = false;
                actor.setX(destinationX);
            }
        }

        if(!moveToLeft && isMoving){
            if(actor.getX() < destinationX)
            {
                actor.setPosition(actor.getX() + (delta/time) * distance,0);
                isMoving = true;
            }else{
                isMoving = false;
                actor.setX(destinationX);
            }
        }
    }

    public boolean isMoving() {
        return isMoving;
    }
}

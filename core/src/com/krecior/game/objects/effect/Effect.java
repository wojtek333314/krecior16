package com.krecior.game.objects.effect;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.krecior.game.GameScreen;
import com.krecior.utils.Animation;

public class Effect {
    //===========================================================
    //Constants
    //===========================================================



    //===========================================================
    //Fields
    //===========================================================

    private Sprite sprite;

    private Animation animation;

    private boolean disappearing = false;
    private boolean rotating = false;
    private boolean moving = false;
    private boolean rotationMoving = false;

    private float alpha = 1f;
    private float rotation;
    private float endRotation;
    private float distance;
    private float moveTime;

    private Vector2 startPosition = new Vector2();
    private Vector2 endPosition = new Vector2();

    //===========================================================
    //Constructors
    //===========================================================

    public Effect(TextureRegion textureRegion) {
        createSprite(textureRegion);
    }

    //===========================================================
    //Getter & Setter
    //===========================================================

    public Sprite getSprite() {
        return sprite;
    }

    public void setRotate(float rotation) {
        this.rotation = rotation;
        endRotation = sprite.getRotation() + rotation;
        rotate();
    }

    public void setRotation(float degrees) {
        sprite.setRotation(degrees);
        sprite.setOriginCenter();
    }

    public void setMove(float x, float y) {
        startPosition = new Vector2(sprite.getX(), sprite.getY());
        endPosition = new Vector2(x, y);

        move();
    }

    public void setRotationMove(float duration, float distance) {
        moveTime = duration;
        this.distance = distance;
        startPosition = new Vector2(sprite.getX(), sprite.getY());

        rotateMove();
    }

    public void setAnimation(Animation mAnimation) {
        animation = mAnimation;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
    }

    public void removeAnimation() {
        GameScreen.pGame.getEffectSystem().remove(this);
    }

    //===========================================================
    //Methods for/from SuperClass/Interfaces
    //===========================================================



    //===========================================================
    //Methods
    //===========================================================

    private void createSprite(TextureRegion textureRegion) {
        sprite = new Sprite(textureRegion);
        sprite.setOriginCenter();
    }

    public void disappear() {
        disappearing = true;

        if(alpha <= 0) {
            disappearing = false;
            alpha = 0f;

            onDisappearingFinished();
            removeAnimation();
        }
        sprite.setAlpha(alpha);
    }

    public void rotate() {
        rotating = true;

        if(rotation > 0f) {
            if (sprite.getRotation() >= endRotation) {
                rotating = false;
                sprite.setRotation(rotation);
                onRotationFinished();
            }
        } else {
            if (sprite.getRotation() <= endRotation) {
                rotating = false;
                sprite.setRotation(rotation);
                onRotationFinished();
            }
        }
    }

    public void move() {
        moving = true;
        rotationMoving = false;

        float offsetX = sprite.getX() - endPosition.x;
        float offsetY = sprite.getY() - endPosition.y;

        if(offsetX > 0) {
            if(sprite.getX() < endPosition.x) {
                sprite.setX(startPosition.x + endPosition.x);
                moving = false;
                onMoveFinished();
            }
        } else {
            if(sprite.getX() > endPosition.x) {
                sprite.setX(startPosition.x + endPosition.x);
                moving = false;
                onMoveFinished();
            }
        }

        if(offsetY > 0) {
            if(sprite.getY() < endPosition.y) {
                sprite.setY(startPosition.y + endPosition.y);
                moving = false;
                onMoveFinished();
            }
        } else {
            if(sprite.getY() > endPosition.y) {
                sprite.setY(startPosition.y + endPosition.y);
                moving = false;
                onMoveFinished();
            }
        }
    }

    public void rotateMove() {
        rotationMoving = true;
        moving = false;

        if(Math.sqrt(Math.pow(startPosition.x - sprite.getX(), 2) +
                Math.pow(startPosition.y - sprite.getY(), 2)) > Math.abs(distance)) {
            onRotationMoveFinished();
            rotationMoving = false;
        }
    }

    /**
     * Remember spriteBatch.begin() before using and spriteBatch.end() after.
     * @param spriteBatch
     */
    public void draw(SpriteBatch spriteBatch) {
        if (animation != null)
            animation.animate();

        if (disappearing) {
            alpha -= GameScreen.TIME_STEP * 2;
            disappear();
        }

        if(rotating) {
            sprite.setRotation(sprite.getRotation() + 8f * GameScreen.TIME_STEP * rotation);
            sprite.setOriginCenter();
            rotate();
        }

        if(moving) {
            sprite.setPosition(sprite.getX() + endPosition.x * GameScreen.TIME_STEP,
                    sprite.getY() + endPosition.y * GameScreen.TIME_STEP);
            move();
        }

        if(rotationMoving) {
            sprite.setPosition(sprite.getX() + (float) Math.cos(Math.toRadians(sprite.getRotation() - 90f))
                            * distance * GameScreen.TIME_STEP * (1f / moveTime),
                    sprite.getY() + (float) Math.sin(Math.toRadians(sprite.getRotation() - 90f))
                            * distance * GameScreen.TIME_STEP * (1f / moveTime));

            rotateMove();
        }

        sprite.draw(spriteBatch);
    }

    public void onDisappearingFinished() {

    }

    public void onRotationFinished() {

    }

    public void onMoveFinished() {

    }

    public void onRotationMoveFinished() {

    }

    //===========================================================
    //Inner and Anonymous Classes
    //===========================================================
}

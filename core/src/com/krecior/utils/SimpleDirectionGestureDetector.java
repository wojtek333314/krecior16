package com.krecior.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class SimpleDirectionGestureDetector implements InputProcessor {
    private float startX,endX;
    private boolean processing = false;
    private SwipeProcessor swipeProcessor;

    public interface SwipeProcessor{
        void onMove(float offset);
        void onChangeScreenLeft();
        void onChangeScreenRight();
        void onCancel();
    }

    public SimpleDirectionGestureDetector(SwipeProcessor swipeProcessor) {
        this.swipeProcessor = swipeProcessor;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        startX = screenX;
        processing = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        swipeProcessor.onCancel();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!processing)
            return false;
        endX = screenX;
        swipeProcessor.onMove(startX-endX);
        if(Math.abs(startX-endX)> Gdx.app.getGraphics().getWidth()*0.5f){
            if(startX - endX < 0)
            {
                swipeProcessor.onChangeScreenLeft();
                processing = false;
            }
            if(startX - endX > 0)
            {
                swipeProcessor.onChangeScreenRight();
                processing = false;
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
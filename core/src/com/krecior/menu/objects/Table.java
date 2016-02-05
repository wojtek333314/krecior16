package com.krecior.menu.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.krecior.menu.chooseScreen.elements.LevelButton;

import java.util.ArrayList;
import java.util.List;

public class Table  extends Actor {
	private int pRow;
	private int pColumn;
    private LevelButton[] levelButton;
    private int tabId;
    float pColumnGap;
    float pRowGap;

    public Table(int mRow, int mColumn, int tabId) {
        this.tabId = tabId;
		pRow = mRow;
		pColumn = mColumn;
        levelButton = new LevelButton[pRow * pColumn];
        int index;
        for(int i = 0; i < pRow; i++)
            for(int j = 0; j < pColumn; j++) {
                index = i * pColumn + j;
             //   levelButton[index] = new LevelButton(index+tabId*(pRow*pColumn));
            }
        pColumnGap = levelButton[0].getWidth()*0.12f;
        pRowGap = levelButton[0].getWidth()*0.12f;
	}

    public List<EventListener> getTouchListeners(){
        List<EventListener> ret = new ArrayList<EventListener>();
        int index = 0;
        for(int i = 0; i < pRow; i++)
            for(int j = 0; j < pColumn; j++) {
                index = i * pColumn + j;
                for(int k=0;k<levelButton[index].getListeners().size;k++)
                    ret.add(levelButton[index].getListeners().get(k));
            }
        return ret;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int index;
        for(int i = 0; i < pRow; i++)
            for(int j = 0; j < pColumn; j++) {
                index = i * pColumn + j;
                levelButton[index].draw(batch,parentAlpha);
            }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        int index;

        for(int i = 0; i < pRow; i++)
            for(int j =  0; j < pColumn; j++) {
                index = i * pColumn + j;
                levelButton[index].setPosition(
                        x+ ((j+1) * pColumnGap + j * levelButton[index].getWidth())
                        ,y+ ((pRow) * pRowGap + (pRow -1) * levelButton[index].getHeight()
                                - (i+1) * pRowGap - i * levelButton[index].getWidth()));
            }
    }

    @Override
    public float getWidth() {
        return pColumn*levelButton[0].getWidth() + (1+pColumn)*pColumnGap;
    }
}

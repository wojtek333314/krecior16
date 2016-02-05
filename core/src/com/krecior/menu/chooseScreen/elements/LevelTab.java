package com.krecior.menu.chooseScreen.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.krecior.utils.Data;
import com.krecior.utils.TextLabel;

/**
 * Created by Wojciech Osak on 2015-11-07.
 */
public class LevelTab extends Group {
  //  private Table table;
    private float startX,startY;
    private int pRow;
    private int pColumn;
    private LevelButton[] levelButton;
    private int tabId;
    float pColumnGap;
    float pRowGap;
    int mRow;
    int mColumn;

    public LevelTab(int tabId,float x,float y){
      //  table = new Table(Data.TABLE_ROW, Data.TABLE_COLUMN,tabId);
        startX = x;
        startY = y;
       // table.setPosition(tabId * Gdx.app.getGraphics().getWidth(), y);

        mRow = Data.TABLE_ROW;
        mColumn = Data.TABLE_COLUMN;
        this.tabId = tabId;
        pRow = mRow;
        pColumn = mColumn;
        levelButton = new LevelButton[pRow * pColumn];
        int index;
        BitmapFont bitmapFontForLabels = TextLabel.createFontOnce("roboto");

        for(int i = 0; i < pRow; i++)
            for(int j = 0; j < pColumn; j++) {
                index = i * pColumn + j;
                levelButton[index] = new LevelButton(index+tabId*(pRow*pColumn), bitmapFontForLabels);
                addActor(levelButton[index]);
            }
        pColumnGap = levelButton[0].getWidth()*0.12f;
        pRowGap = levelButton[0].getWidth()*0.12f;
        float widthOfButtonsTab = pColumn*levelButton[0].getWidth() + (1+pColumn)*pColumnGap;

        for(int i = 0; i < pRow; i++)
            for(int j =  0; j < pColumn; j++) {
                index = i * pColumn + j;
                float btnX = (tabId * Gdx.app.getGraphics().getWidth()) +Gdx.app.getGraphics().getWidth()/2 - widthOfButtonsTab/2;
                levelButton[index].setPosition(
                        btnX + ((j+1) * pColumnGap + j * levelButton[index].getWidth())
                        ,y+ ((pRow) * pRowGap + (pRow -1) * levelButton[index].getHeight()
                                - (i+1) * pRowGap - i * levelButton[index].getWidth()));
            }

        //addactor
    }


}

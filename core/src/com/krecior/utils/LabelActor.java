package com.krecior.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;


/**
 * Created by Wojciech Osak on 2016-02-06.
 */
public class LabelActor extends Actor {
    private BitmapFont bitmapFont;
    private String text;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private float x, y;
    private boolean wrap = false;
    private int lineWidth;
    private int align = Align.left;
    private float height;

    public LabelActor(BitmapFont bitmapFont, String text) {
        this.bitmapFont = bitmapFont;
        this.text = text;
    }

    public LabelActor(BitmapFont bitmapFont, String text, int lineWidth) {
        this.bitmapFont = bitmapFont;
        this.text = text;
        this.wrap = true;
        this.lineWidth = lineWidth;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), lineWidth, Align.left, true, null);
    }

    public LabelActor(BitmapFont bitmapFont, String text, int lineWidth, int align) {
        this.bitmapFont = bitmapFont;
        this.text = text;
        this.wrap = true;
        this.align = align;
        this.lineWidth = lineWidth;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), lineWidth, align, true, null);
    }

    public void setAlign(int align) {
        this.align = align;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), 0, align, true, null);
    }

    public void setText(String text) {
        setOrigin(x,-getHeight());
        this.text = text;
        if (!wrap)
            glyphLayout.setText(bitmapFont, text);
        else
            glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), lineWidth, align, true, null);
    }

    public String getText() {
        return text;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        bitmapFont.setColor(bitmapFont.getColor().r, bitmapFont.getColor().g, bitmapFont.getColor().b, parentAlpha);
        bitmapFont.draw(batch, glyphLayout, x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getWidth() {
        if(wrap)
            return lineWidth;
        return glyphLayout.width;
    }


    @Override
    public float getHeight() {
        return (glyphLayout.height);
    }

    @Override
    public float getY() {
        return this.y;
    }

}

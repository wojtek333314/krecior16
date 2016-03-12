package com.krecior.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Wojciech Osak on 2015-09-26.
 * Tworzy label do rysowania tekstow ktory przystosowuje sie do rozmiaru ekranu.
 * Wspierane rozdzielczosci: 160dpi, 320dpi, 480dpi, jesli wiekszy ustawia ten dla 480dpi font.
 *
 * Wymagania: pliki dla wszystkich dpi wspieranych .fnt i .png o nazwie NAZWAFONTU_ROZDZIELCZOSC.ROZSZERZENIE w folderze assets/fonts/
 * Np. dla czcionki roboto:
 * roboto_hdpi (pliki .fnt i .png), roboto_xhdpi (-=-) i roboto_xxhdpi (-=-)
 *
 * Pliki tworzymy w programie Hiero wg pliku ustawien dodanego w folderze Assets/fonts o nazwie "hieroConfigFonts"
 * PAMIETAC O ZAZNACZENIU GLYPH CASE !!!!!!!
 *
 * Rozmiary dla poszczegolnych DPI (w Hiero jest to FontSize):
 * hdpi: 18
 * xhdpi: 28
 * xxhdpi: 36
 *
 * Nastepnie pliki wklejamy do assets/fonts. Sposób uzycia klasy:
 * TextLabel label = new TextLabel(TextLabel.Font.Roboto,"jakis tekst do wyswietlenia");
 * wyrysowanie:
 * label.draw(batch);
 *
 * Rozmiar fonta mozemy skalowac przez:
 * label.setScale(x,y);
 */
public class TextLabel extends Actor {
    private BitmapFont bitmapFont;
    private String text;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private float x, y;
    private boolean wrap = false;
    private int lineWidth;
    private int align = Align.left;
    private float height;

    public TextLabel(BitmapFont bitmapFont, String text) {
        this.bitmapFont = bitmapFont;
        this.text = text;
        glyphLayout.setText(bitmapFont, text);
    }

    public TextLabel(BitmapFont bitmapFont, String text, int lineWidth) {
        this.bitmapFont = bitmapFont;
        this.text = text;
        this.wrap = true;
        this.lineWidth = lineWidth;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), lineWidth, Align.left, true, null);
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
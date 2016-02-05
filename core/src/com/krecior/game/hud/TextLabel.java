package com.krecior.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

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
public class TextLabel extends Actor{
    private static ArrayList<BitmapFont> fontCache = new ArrayList<BitmapFont>();
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private float x,y;//position
    private String text;
    private String fntPath;
    private TextLabelSize textLabelSize;

    public TextLabel(Font textLabelFont,String textToShow) {
        super();
        this.fntPath = Font.getFontPath(textLabelFont);
        bitmapFont = createFont(fntPath);
        text = textToShow;
        glyphLayout.setText(bitmapFont, textToShow);
    }


    public TextLabel(Font textLabelFont,String textToShow,float x,float y) {
        super();
        this.fntPath = Font.getFontPath(textLabelFont);
        setPosition(x, y);
        bitmapFont = createFont(fntPath);
        text = textToShow;
        glyphLayout.setText(bitmapFont, textToShow);
    }



    /**
     * Tworzy czcionke, fntFile musi miec taka sama nazwe jak plik .fnt i plik .png i musza znajdowac sie w assets/fonts/
     * @param fntFile
     * @return
     */
    private BitmapFont createFont(String fntFile){
        for(int i=0;i<fontCache.size();i++)
        {
            if(fontCache.get(i).getData().fontFile.file().getName().equals(getFontFileByScreenDpi(fntFile)+".fnt")){
                fontCache.get(i).dispose();
                return fontCache.get(i);
            }
        }
        Texture texture = new Texture(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(fntFile) + ".png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(fntFile) + ".fnt"), new TextureRegion(texture), false);
        bitmapFont.dispose();
        fontCache.add(bitmapFont);
        return bitmapFont;
    }

    /**
     * Towrzy od nowa czcionke (uzywane przy zmianie rozmiaru czcionki)
     * @param
     * @return
     */
    private BitmapFont recreateFontBySize(String file){
        for(int i=0;i<fontCache.size();i++)
        {
            if(fontCache.get(i).getData().fontFile.file().getName().equals(getFontFileByTextLabelType(file)+".fnt"))
            {
                fontCache.get(i).dispose();
                return fontCache.get(i);
            }
        }
        Texture texture = new Texture(Gdx.files.internal("fonts/" + getFontFileByTextLabelType(file) + ".png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("fonts/" + getFontFileByTextLabelType(file) + ".fnt"), new TextureRegion(texture), false);
        bitmapFont.dispose();
        fontCache.add(bitmapFont);
        return bitmapFont;
    }

    /**
     * Tworzy Font omijajac pobranie fontu z Cache. Uzywany przy skalowaniu ( po to aby nie zeskalowac wszystkich fontow ktore sa oparte na tym  z cache).
     * @param fntFile
     * @return
     */
    private BitmapFont createFontAvoidCache(String fntFile){
        Texture texture = new Texture(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(fntFile) + ".png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(fntFile) + ".fnt"), new TextureRegion(texture), false);
        bitmapFont.dispose();
        return bitmapFont;
    }

    private String getFontFileByScreenDpi(String fntFile){
        if(Gdx.app.getGraphics().getDensity()*160 < 260)
        {
            textLabelSize = TextLabelSize.HDPI;
            return fntFile+"_hdpi";
        }
        if(Gdx.app.getGraphics().getDensity()*160 >= 260 && Gdx.app.getGraphics().getDensity()*160 < 420)
        {
            textLabelSize = TextLabelSize.XHDPI;
            return fntFile+"_xhdpi";
        }
        if(Gdx.app.getGraphics().getDensity()*160 >= 420)
        {
            textLabelSize = TextLabelSize.XXHDPI;
            return fntFile+"_xxhdpi";
        }
        return fntFile+"_xxhdpi";
    }

    private String getFontFileByTextLabelType(String fntFile){
        switch(textLabelSize){
            case HDPI:
                return fntFile+"_hdpi";
            case XHDPI:
                return fntFile+"_xhdpi";
            case XXHDPI:
                return fntFile+"_xxhdpi";
        }
        return null;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        bitmapFont.setColor(bitmapFont.getColor().r, bitmapFont.getColor().g, bitmapFont.getColor().b, parentAlpha);
        bitmapFont.draw(batch, text, x, y);
    }

    public void draw(SpriteBatch batch, float parentAlpha) {
        bitmapFont.setColor(bitmapFont.getColor().r, bitmapFont.getColor().g, bitmapFont.getColor().b, parentAlpha);
        bitmapFont.draw(batch, text, x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /*public void draw(Batch batch,float x, float y) {
        bitmapFont.draw(batch, text, x, y);
    }*/

    public void setColor(float r,float g,float b,float alpha) {
        bitmapFont.setColor(r, g, b, alpha);
    }

    public void setPosition(float x,float y){
        this.x = x;
        this.y = y;
    }

    public void setScale(float scaleX,float scaleY){
        if(scaleX > bitmapFont.getData().scaleX || scaleY > bitmapFont.getData().scaleY)
            setTextLabelSize(TextLabelSize.getHigherSize(textLabelSize));

        bitmapFont = createFontAvoidCache(fntPath);
        bitmapFont.getData().setScale(scaleX, scaleY);
    }

    public void setScaleXY(float scaleXY){
        bitmapFont = createFontAvoidCache(fntPath);
        bitmapFont.getData().setScale(scaleXY);
        glyphLayout.setText(bitmapFont, text);
    }

    public float getWidth(){
        return glyphLayout.width;
    }

    public float getHeight(){
        int lines = 1;
        for(int i=0;i<text.length();i++)
            if(text.charAt(i)=='\n' && i!=text.length()-1)
                lines+=1;
        return glyphLayout.height * lines;
    }

    public void setText(String text) {
        this.text = text;
        glyphLayout.setText(bitmapFont,text);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    private void setTextLabelSize(TextLabelSize textLabelSize) {
        this.textLabelSize = textLabelSize;
        bitmapFont = recreateFontBySize(fntPath);
        glyphLayout.setText(bitmapFont,text);
    }

    /**
     * Do skalowania i zachowania ladniejszej czcionki jesli skalujemy w górę
     */
    private enum TextLabelSize{
        HDPI,
        XHDPI,
        XXHDPI;

        private static TextLabelSize getHigherSize(TextLabelSize size){
            switch (size){
                case HDPI:
                    return XXHDPI;
                case XHDPI:
                    return XXHDPI;
                case XXHDPI:
                    return XXHDPI;
                default:
                    return XXHDPI;
            }
        }
    }

    public enum Font{
        CARTOONIC,
        CARTOONIC_WACKY,
        CARTOONIC_3D,
        CARTOONIC_COLLAGE,
        ROBOTO;

        public static String getFontPath(Font textLabelFont){
            switch(textLabelFont){
                case CARTOONIC:
                    return "cartoonic";
                case CARTOONIC_3D:
                    return "cartoonic_3d";
                case CARTOONIC_COLLAGE:
                    return "cartoonic_collage";
                case CARTOONIC_WACKY:
                    return "cartoonic_wacky";
                case ROBOTO:
                    return "roboto";
                default:
                    return "roboto";
            }
        }
    }
}

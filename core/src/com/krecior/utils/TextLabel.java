package com.krecior.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

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
    private float maxWidth;
    private int align;
    private boolean wrap = true;

    public TextLabel(Font textLabelFont,String textToShow) {
        super();
        this.fntPath = Font.getFontPath(textLabelFont);
        bitmapFont = createFont(fntPath);
        bitmapFont.dispose();
        text = textToShow;
        align = Align.left;
        glyphLayout.setText(bitmapFont, text,0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
        setOrigin(0, glyphLayout.height);
        System.gc();
    }

    public TextLabel(Font textLabelFont,String textToShow,float scaleXY) {
        super();
        this.fntPath = Font.getFontPath(textLabelFont);
        bitmapFont = createFontAvoidCache(fntPath,scaleXY);
        bitmapFont.getData().setScale(scaleXY);
        bitmapFont.dispose();
        text = textToShow;
        align = Align.left;
        glyphLayout.setText(bitmapFont, text,0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
        setOrigin(0, glyphLayout.height);
        System.gc();
    }

    public TextLabel(Font textLabelFont,String textToShow,float x,float y) {
        super();
        this.fntPath = Font.getFontPath(textLabelFont);
        setPosition(x, y);
        bitmapFont = createFont(fntPath);
        text = textToShow;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
        System.gc();
    }

    public TextLabel(BitmapFont bitmapFont,String textToShow) {
        super();
        this.bitmapFont = bitmapFont;
        text = textToShow;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
        System.gc();
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
    }

    public void setAlign(int align) {
        this.align = align;
        glyphLayout.setText(bitmapFont, text, 0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
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

    public static BitmapFont createFontOnce(String fntFile){
        Texture texture = new Texture(Gdx.files.internal("fonts/" + getFontFileByScreenDpiStatic(fntFile) + ".png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("fonts/" + getFontFileByScreenDpiStatic(fntFile) + ".fnt"), new TextureRegion(texture), false);
        bitmapFont.dispose();
        fontCache.add(bitmapFont);
        return bitmapFont;
    }

    /**
     * Tworzy od nowa czcionke o danym rozmiarze jesli takiej nie ma, jesli jest z takim rozmiarem to uzywa
     * @param file
     * @param size
     * @return
     */
    private BitmapFont recreateFontBySize(String file,float size){
        for(int i=0;i<fontCache.size();i++)
        {
            if(fontCache.get(i).getData().fontFile.file().getName().equals(getFontFileByTextLabelType(file)+".fnt")
                    && fontCache.get(i).getData().scaleX==size && fontCache.get(i).getData().scaleY==size)
            {
                fontCache.get(i).dispose();
                return fontCache.get(i);
            }
        }

        Texture texture = new Texture(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(file) + ".png"), true); // true enables mipmaps
        System.out.println(file+":"+getFontFileByScreenDpi(file));
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(file) + ".fnt"), new TextureRegion(texture), false);
        bitmapFont.dispose();
        bitmapFont.getData().setScale(size);
        fontCache.add(bitmapFont);
        return bitmapFont;
    }


    /**
     * Tworzy Font omijajac pobranie fontu z Cache. Uzywany przy skalowaniu ( po to aby nie zeskalowac wszystkich fontow ktore sa oparte na tym  z cache).
     * @param fntFile
     * @return
     */
    private BitmapFont createFontAvoidCache(String fntFile,float size){
      /*  for(int i=0;i<fontCache.size();i++)
        {
            if(fontCache.get(i).getData().fontFile.file().getName().equals(getFontFileByTextLabelType(fntFile)+".fnt")
                    && fontCache.get(i).getData().scaleX==size && fontCache.get(i).getData().scaleY==size)
            {
                System.out.println("same size");
                fontCache.get(i).dispose();
                return fontCache.get(i);
            }
        }
*/
        Texture texture = new Texture(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(fntFile) + ".png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("fonts/" + getFontFileByScreenDpi(fntFile) + ".fnt"), new TextureRegion(texture), false);
        bitmapFont.dispose();
        return bitmapFont;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    private String getFontFileByScreenDpi(String fntFile){
        if(Gdx.app.getGraphics().getDensity()*160 < 260)
        {
            this.textLabelSize = TextLabelSize.HDPI;
            return fntFile+"_hdpi";
        }
        if(Gdx.app.getGraphics().getDensity()*160 >= 260 && Gdx.app.getGraphics().getDensity()*160 < 420)
        {
            this.textLabelSize = TextLabelSize.XHDPI;
            return fntFile+"_xhdpi";
        }
        if(Gdx.app.getGraphics().getDensity()*160 >= 420)
        {
            this.textLabelSize = TextLabelSize.XXHDPI;
            return fntFile+"_xxhdpi";
        }
        return fntFile+"_xxhdpi";
    }

    private static String getFontFileByScreenDpiStatic(String fntFile){
        if(Gdx.app.getGraphics().getDensity()*160 < 260)
        {
            return fntFile+"_hdpi";
        }
        if(Gdx.app.getGraphics().getDensity()*160 >= 260 && Gdx.app.getGraphics().getDensity()*160 < 420)
        {
            return fntFile+"_xhdpi";
        }
        if(Gdx.app.getGraphics().getDensity()*160 >= 420)
        {
            return fntFile+"_xxhdpi";
        }
        return fntFile+"_xxhdpi";
    }


    private String getFontFileByTextLabelType(String fntFile){
        if(textLabelSize==null)
            getFontFileByScreenDpi(fntFile);
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
        bitmapFont.draw(batch, glyphLayout, x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void setColor(float r,float g,float b,float alpha) {
        bitmapFont.setColor(r, g, b, alpha);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    public void setScale(float scaleX,float scaleY){
        bitmapFont = createFontAvoidCache(fntPath,bitmapFont.getData().scaleX);
        bitmapFont.getData().setScale(scaleX, scaleY);
        glyphLayout.setText(bitmapFont, text,0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
    }

    public void setScaleXY(float scaleXY){
        bitmapFont = createFontAvoidCache(fntPath,bitmapFont.getData().scaleX);
        bitmapFont.getData().setScale(scaleXY);
        glyphLayout.setText(bitmapFont, text,0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
    }

    @Override
    public float getWidth(){
        return glyphLayout.width;
    }


    @Override
    public float getHeight(){
       /* int lines = 1;    //ten kod jest teoretycznie zbyteczny :D w fazie testow, jak bedzie widoczny w listopadzie - do wyjebania
        for(int i=0;i<text.length();i++)
            if(text.charAt(i)=='\n' && i!=text.length()-1)
                lines+=1;*/
        return (glyphLayout.height);
    }

    public void setText(String text) {
        this.text = text;
        glyphLayout.setText(bitmapFont, text,0, text.length(), bitmapFont.getColor(), maxWidth, align, wrap, null);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
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

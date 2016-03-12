package com.krecior.utils.enums;

/**
 * Created by Wojciech Osak on 2016-02-06.
 */
public enum FontType {
    ROBOTO_REGULAR,
    ROBOTO_ITALIC,
    ROBOTO_CONDENSED,
    ROBOTO_THIN,
    ARIAL,
    ROBOTO_MEDIUM;

    public static String getFontPath(FontType type){
        switch(type){
            case ROBOTO_REGULAR:
                return "fonts/RobotoRegular.ttf";
            case ROBOTO_ITALIC:
                return "fonts/RobotoItalic.ttf";
            case ROBOTO_CONDENSED:
                return "fonts/RobotoMedium.ttf";
            case ROBOTO_THIN:
                return "fonts/RobotoThin.ttf";
            case ARIAL:
                return "fonts/arial.ttf";
            case ROBOTO_MEDIUM:
                return "fonts/RobotoMedium.ttf";

        }
        return null;
    }
}

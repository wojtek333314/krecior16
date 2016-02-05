package com.krecior.menu.coinShop.coinItem.enums;

/**
 * Created by Wojciech Osak on 2015-09-24.
 */
public enum  CoinItemType {
    COINS_3000,
    COINS_6000,
    COINS_12000;

    public static float getCost(CoinItemType type){
        switch(type){
            case COINS_3000:
                return 1.99f;
            case COINS_6000:
                return 2.99f;
            case COINS_12000:
                return 3.99f;
            default:
                return -1;
        }
    }

    public static String getDescription(CoinItemType type){
        switch(type){
            case COINS_3000:
                return "3000 - " +  Float.toString(getCost(type))+"$\n     NO ADS";
            case COINS_6000:
                return "6000 - " +  Float.toString(getCost(type))+"$\n     NO ADS";
            case COINS_12000:
                return "12000 - " +  Float.toString(getCost(type))+"$\n     NO ADS";
            default:
                return "";
        }
    }

    public static int getDiamondsCount(CoinItemType type){
        switch(type){
            case COINS_3000:
                return 3;
            case COINS_6000:
                return 6;
            case COINS_12000:
                return 9;
            default:
                return -1;
        }
    }
}

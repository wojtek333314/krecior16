package com.krecior.menu.achievements.system;

import com.badlogic.gdx.Gdx;
import com.krecior.utils.Data;

/**
 * Created by Wojciech Osak on 2015-10-07.
 */
public enum Achievement {
    ELIMINATED_MOLES_10K,
    COLLECT_DIAMONDS_12K,
    ACHIEVED_1M_SCORES;

    public static String getDescription(Achievement achievement){
        switch(achievement){
            case ELIMINATED_MOLES_10K:
                return "Eliminated "+getValue(achievement)+"/"+getGoal(achievement)+"\n"+"moles";
            case COLLECT_DIAMONDS_12K:
                return "Collected "+getValue(achievement)+"/"+getGoal(achievement)+"\n"+"diamonds";
            case ACHIEVED_1M_SCORES:
                return "Achieved "+getValue(achievement)+"/"+getGoal(achievement)+"\n"+"scores";
            default:
                return "no description";
        }
    }

    public static boolean isReached(Achievement achievement){
        return (getValue(achievement)/getGoal(achievement)) >= 1;
    }

    public static int getValue(Achievement achievement){
        return Data.getAchievementValue(achievement);
    }

    private static int getValueForDescription(Achievement achievement){
        return isReached(achievement) ? getGoal(achievement) : getValue(achievement);
    }

    public static void setValue(Achievement achievement,int value){
        Data.setAchievementValue(achievement,value);
    }

    public static void addValue(Achievement achievement,int value){
        Data.setAchievementValue(achievement,getValue(achievement)+value);
    }

    public static void increaseValue(Achievement achievement){
        Data.setAchievementValue(achievement,Data.getAchievementValue(achievement)+1);
    }

    public static int getGoal(Achievement achievement){
        switch(achievement){
            case ELIMINATED_MOLES_10K:
                return 10000;
            case COLLECT_DIAMONDS_12K:
                return 12000;
            case ACHIEVED_1M_SCORES:
                return 1000000;
            default:
                Gdx.app.error("ACHIEVEMENT", "This Goal does not have goal described!/wojtas");
                return 0;
        }
    }
}

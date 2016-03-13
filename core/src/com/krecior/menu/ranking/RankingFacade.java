package com.krecior.menu.ranking;

import com.krecior.utils.ServerRequestListener;

/**
 * Created by Wojtek on 2016-02-28.
 */
public interface RankingFacade {
    void registerPoints(String nick,int points,ServerRequestListener listener);
    void getRanking(int points, ServerRequestListener listener);
    boolean isOnline();
}

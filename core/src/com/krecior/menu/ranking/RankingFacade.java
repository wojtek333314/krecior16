package com.krecior.menu.ranking;

import com.krecior.utils.ServerRequestListener;

/**
 * Created by Wojtek on 2016-02-28.
 */
public interface RankingFacade {
    void registerNick(String nick,ServerRequestListener listener);
    void registerPoints(String nick,int points,ServerRequestListener listener);
    void getPlayersRankingDependsOnNick(String nick,int listSize, ServerRequestListener listener);
}

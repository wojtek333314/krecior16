package ranking;

import android.content.Context;

import com.krecior.utils.ServerRequestListener;

import ranking.operations.GetList;
import ranking.operations.RegisterPoints;

/**
 * Created by Wojtek on 2016-02-28.
 */
public class RankingFacadeImpl {
    private Context context;

    public RankingFacadeImpl(Context context) {
        this.context = context;
    }

    /**
     * Ustawia w bazie danych dla podanego nicku daną ilość punktów
     * @param nick
     * @param points
     * @param listener
     */
    public void registerPoints(String nick, int points, ServerRequestListener listener) {
        new RegisterPoints(nick, points, listener);
    }

    /**
     * Zwraca listę 10 nickow obok nicku gracza wraz z punktami w formie json
     * @param listener
     */
    public void getPlayersListDependOnNick(int points,ServerRequestListener listener){
        new GetList(String.valueOf(points),listener);
    }
}

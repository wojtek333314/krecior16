package ranking;

import com.krecior.utils.ServerRequestListener;

import ranking.operations.GetList;
import ranking.operations.RegisterNick;
import ranking.operations.RegisterPoints;

/**
 * Created by Wojtek on 2016-02-28.
 */
public class RankingFacadeImpl {

    /**
     * Rejestruje nick w bazie danych lub powiadamia o jego istnieniu w onError listenera(chyba w onError).
     * @param nick
     * @param listener
     */
    public void registerNick(String nick, ServerRequestListener listener) {
        new RegisterNick(nick, listener);
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
     * Zwraca listę 5 nickow obok nicku gracza wraz z punktami w formie json
     * @param nick
     * @param listener
     */
    public void getPlayersListDependOnNick(String nick,ServerRequestListener listener){
        new GetList(nick,listener);
    }
}

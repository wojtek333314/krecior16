package ranking.operations;


import com.krecior.utils.ServerRequestListener;

import ranking.ServerRequest;
import ranking.enums.ServiceType;
import ranking.parameters.Parameters;

/**
 * Created by Wojciech Osak on 2016-01-27.
 */
public class GetList {

    public GetList(String nick, ServerRequestListener serverRequestListener){
        new ServerRequest(ServiceType.GET_LIST,
                new Parameters().addParam("points",nick))
                .setServerRequestListener(serverRequestListener)
                .execute();
    }
}

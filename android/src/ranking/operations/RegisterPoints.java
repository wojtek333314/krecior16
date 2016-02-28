package ranking.operations;


import com.krecior.utils.ServerRequestListener;

import ranking.ServerRequest;
import ranking.enums.ServiceType;
import ranking.parameters.Parameters;

/**
 * Created by Wojciech Osak on 2016-01-27.
 */
public class RegisterPoints {

    public RegisterPoints(String nick,int points, ServerRequestListener serverRequestListener){
        new ServerRequest(ServiceType.REGISTER_POINTS,
                new Parameters()
                        .addParam("nick",nick)
                        .addParam("points", Integer.toString(points)))
                .setServerRequestListener(serverRequestListener).execute();
    }
}

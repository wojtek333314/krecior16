package ranking.operations;


import com.krecior.utils.ServerRequestListener;

import ranking.ServerRequest;
import ranking.enums.ServiceType;
import ranking.parameters.Parameters;

/**
 * Created by Wojciech Osak on 2016-01-27.
 */
public class RegisterNick {
    public RegisterNick(String nick, ServerRequestListener serverRequestListener){
        new ServerRequest(ServiceType.REGISTER_NICK,
                new Parameters().addParam("nick",nick))
                .setServerRequestListener(serverRequestListener)
                .execute();
    }

}

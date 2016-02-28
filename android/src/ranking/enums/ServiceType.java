package ranking.enums;


public enum ServiceType {
    GET_LIST,
    REGISTER_NICK,
    REGISTER_POINTS;

    private static final String SERVER_PATH = "http://www.datastore.waw.pl/MolesWar/queueSystem/";

    public static String getURL(ServiceType serviceType){
        switch(serviceType){

            case GET_LIST:
                return SERVER_PATH + "getList.php";
            case REGISTER_NICK:
                return SERVER_PATH + "registerNick.php";
            case REGISTER_POINTS:
                return SERVER_PATH + "updatePoints.php";
        }
        return "Service path is invalid";
    }
}

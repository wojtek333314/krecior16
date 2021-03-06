package mapsUpdater;

import com.krecior.utils.ServerRequestListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request extends android.os.AsyncTask<Param,Void,Void> {
	private static final String SERVER_PATH = "http://datastore.waw.pl/MolesWar";
	private static String SERVICE = "";//name of service
	private ServerRequestListener requestListener;

	public Request(String serviceName, ServerRequestListener requestListener){
		this.SERVICE = serviceName;
		this.requestListener = requestListener;
    }

	public void call(Param params){
		HttpURLConnection connection = null;
		  try {
		    //Create connection
		    URL url = new URL(SERVER_PATH+"/"+SERVICE+params.getQuery());
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
		    //Get Response
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
		    String line;
		    while((line = rd.readLine()) != null) {
		      response.append(line);
		    }
		    rd.close();
		    requestListener.onSuccess(response.toString());
		  } catch (Exception e) {
		    e.printStackTrace();
		    requestListener.onError(600,"null response!");
		  } finally {
		    if(connection != null) {
		      connection.disconnect();
		    }
		  }
	}

    @Override
    protected Void doInBackground(Param... voids) {
        call(voids[0]);
        return null;
    }
}

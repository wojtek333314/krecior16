package ranking;

import android.os.AsyncTask;

import com.krecior.utils.ServerRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import ranking.enums.ServiceType;
import ranking.parameters.Parameters;


public class ServerRequest extends AsyncTask<Void, Void, String> {

    private ServiceType serviceType;
    private Parameters parameters;
    private ServerRequestListener serverRequestListener;
    private int code;
    private String description;

    public ServerRequest(ServiceType serviceType, Parameters parameters) {
        this.serviceType = serviceType;
        this.parameters = parameters;
    }

    public ServerRequest setServerRequestListener(ServerRequestListener serverRequestListener) {
        this.serverRequestListener = serverRequestListener;
        return this;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... urls) {

        try {
            URL url = new URL(ServiceType.getURL(serviceType));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            HashMap<String,String> params = new HashMap<String,String>();
            for (String key : parameters.getParameters().keySet()) {
                params.put(key, parameters.getParameters().get(key));
            }
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            return streamToString(in);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String getQuery(HashMap<String,String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String key : params.keySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject status = jsonObject.getJSONObject("result");
            code = status.getInt("code");
            description = status.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (serverRequestListener != null) {
            if (code != 100)
                serverRequestListener.onError(code, description);
            else
                serverRequestListener.onSuccess(result);
        }
    }

    public static String streamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

}

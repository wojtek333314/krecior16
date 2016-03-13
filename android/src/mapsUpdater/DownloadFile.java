package mapsUpdater;

import com.krecior.utils.ServerRequestListener;

public class DownloadFile {
    private Param param;
    private Request request;
    private ServerRequestListener requestListener;
    private boolean isEnd = false;
    private String fileNumber;

    public DownloadFile(String fileNumber) {
        this.fileNumber = fileNumber;
        request = new Request("download.php", new ServerRequestListener() {

            @Override
            public void onSuccess(String json) {
                isEnd = true;
                if (json.length() > 0 && json.charAt(0) == ' ')
                    json = json.substring(1);
                onEnd(json);
                if (requestListener != null)
                    requestListener.onSuccess(json);
            }

            @Override
            public void onError(int code, String description) {
                isEnd = true;
                onEnd(description);
                if (requestListener != null)
                    requestListener.onError(601, description);
            }

            @Override
            public void onConnectionError() {

            }

        });

        param = new Param("number", fileNumber);
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setRequestListener(ServerRequestListener rl) {
        this.requestListener = rl;
    }

    public boolean isEnd() {
        return isEnd;
    }


    public void start() {
        request.execute(param);
    }

    public void onEnd(String out) {

    }

}

package mapsUpdater;

import com.krecior.utils.ServerRequestListener;

import java.util.ArrayList;

public class ServerMultiTaskManager {
    private ArrayList<DownloadFile> downloadList = new ArrayList<DownloadFile>();
    private boolean uploadingFinished = true;
    private boolean downloadingFinished = false;

    public ServerMultiTaskManager() {

    }

    public void addDownloadTask(DownloadFile df) {
        downloadList.add(df);
    }

    public void startDownloading() {
        System.out.println("start downloading");
        for (DownloadFile df : downloadList) {
            System.out.println("start downloading" + df.getFileNumber());
            df.setRequestListener(new ServerRequestListener() {

                @Override
                public void onSuccess(String json) {
                    checkStatus();
                }

                @Override
                public void onError(int code, String description) {
                    checkStatus();
                }

                @Override
                public void onConnectionError() {

                }

            });
            df.start();
        }
    }

    public void updateProgress(float progress) {

    }

    private void checkStatus() {
        int tasksCompleted = 0;
        for (int i = 0; i < downloadList.size(); i++) {
            if (!downloadList.get(i).isEnd())
                break;
            else {
                tasksCompleted++;
                if (i == downloadList.size() - 1 && downloadList.get(i).isEnd())
                    downloadingFinished = true;
            }
        }

        if (uploadingFinished && downloadingFinished) {
            onEndTasks();
            return;
        }

        if (downloadList.size() == 0 && uploadingFinished)
            onEndTasks();

        int tasks = downloadList.size();
        updateProgress(tasksCompleted / (float) tasks);
    }


    public void onEndTasks() {

    }

}

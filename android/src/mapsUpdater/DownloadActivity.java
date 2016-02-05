package mapsUpdater;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.krecior.android.R;

/**
 * Created by Wojciech Osak on 2015-12-30.
 */
public class DownloadActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        final ProgressDialog progress = ProgressDialog.show(this, "Downloading levels",
                "In progress...", true);
        progress.setIndeterminate(true);
        progress.setProgress(10);


    }
}

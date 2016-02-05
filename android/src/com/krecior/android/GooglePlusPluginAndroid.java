package com.krecior.android;

import android.app.Activity;


/**
 * Created by Wojciech Osak on 2015-10-20.
 */
public class GooglePlusPluginAndroid {
    private Activity context;

    public GooglePlusPluginAndroid(Activity context) {
        this.context = context;

    }

    public void share(){
        /*Intent shareIntent = new PlusShare.Builder(context)
                .setType("text/plain")
                .setText("Welcome to the Google+ platform.")
                .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                .getIntent();

        context.startActivityForResult(shareIntent, 0);*/
    }
}

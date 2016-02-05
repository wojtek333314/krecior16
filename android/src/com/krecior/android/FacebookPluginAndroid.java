package com.krecior.android;

import android.app.Activity;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.internal.LikeContent;
import com.facebook.share.internal.LikeDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.krecior.menu.socialNetwork.facebook.FacebookPluginListener;

/**
 * Created by Wojciech Osak on 2015-10-16.
 */
public class FacebookPluginAndroid {
    private Activity context;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private LikeDialog likeDialog;

    public FacebookPluginAndroid(Activity context) {
        this.context = context;
        this.callbackManager = CallbackManager.Factory.create();
    }

    public void share(final FacebookPluginListener facebookPluginListener) {
        //  context.startActivityForResult(new Intent(context,FacebookActivityAndroid.class),10);
        //  if(true)
        //      return;



        shareDialog = new ShareDialog(context);
        final ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com/Blow-Mind-1512776739042777/"))
                .build();



        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                System.out.println("share SHARED " + result.getPostId() + ":" + result.toString());

                if (result.getPostId() != null)
                    facebookPluginListener.getFacebookShareListener().onShareSuccess();
            }

            @Override
            public void onCancel() {
                System.out.println("share CANCEL");
            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("share ERROR");
            }


        }, 121);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                shareDialog.show(content);
            }
        });

    }

    public void like(final FacebookPluginListener facebookPluginListener) {
        // context.startActivityForResult(new Intent(context,FacebookActivityAndroid.class),10);
        // if(true)
        //     return;

        likeDialog = new LikeDialog(context);
        final LikeContent content = new LikeContent.Builder()
                .setObjectId("https://www.facebook.com/Blow-Mind-1512776739042777/")
                .build();


        likeDialog.registerCallback(callbackManager, new FacebookCallback<LikeDialog.Result>() {
            @Override
            public void onSuccess(LikeDialog.Result result) {
                System.out.println("like success! android LIKE" + result.getData());

                if (result.getData().getBoolean("object_is_liked"))
                    facebookPluginListener.getFacebookLikeListener().onLikeSuccess();
            }

            @Override
            public void onCancel() {
                System.out.println("like cancel! android LIKE");
            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("like error! android LIKE");
            }


        }, 123);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                likeDialog.show(content);
            }
        });
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

}

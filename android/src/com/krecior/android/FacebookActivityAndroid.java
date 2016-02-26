package com.krecior.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.internal.LikeContent;
import com.facebook.share.internal.LikeDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.krecior.utils.Data;

/**
 * Created by Wojciech Osak on 2015-12-21.
 */
public class FacebookActivityAndroid extends Activity {
    TextView t1, t2, info_share, info_like;
    ImageView v1, v2;
    Button likeView;
    LoginButton authButton;
    Button shareButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook);
        FacebookSdk.setApplicationId("189158638085478");
        FacebookSdk.setClientToken("8bf8cc44ca84fe649e5368a15b73c394");

        ImageView back = (ImageView)findViewById(R.id.imageView5);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        likeView = (Button) findViewById(R.id.likebutton);
        final LikeDialog likeDialog = new LikeDialog(this);
        final LikeContent likeContent = new LikeContent.Builder()
                .setObjectId("https://www.facebook.com/Blow-Mind-1512776739042777/")
                .build();
        callbackManager = CallbackManager.Factory.create();


        likeDialog.registerCallback(callbackManager, new FacebookCallback<LikeDialog.Result>() {
            @Override
            public void onSuccess(LikeDialog.Result result) {
                System.out.println("like success! android LIKE" + result.getData());

                if (result.getData().getBoolean("object_is_liked")) {
                    Data.facebookSetLiked();
                    updateButtonsVisibility();
                }
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

        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        likeDialog.show(likeContent);
                    }
                });
            }
        });


        authButton = (LoginButton) findViewById(R.id.view);
        authButton.setPublishPermissions("publish_actions");
        authButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                updateButtonsVisibility();
            }

            @Override
            public void onCancel() {
                updateButtonsVisibility();
            }

            @Override
            public void onError(FacebookException error) {
                updateButtonsVisibility();
            }
        });

        t1 = (TextView) findViewById(R.id.textView);
        t2 = (TextView) findViewById(R.id.textView2);
        v1 = (ImageView) findViewById(R.id.imageView2);
        v2 = (ImageView) findViewById(R.id.imageView3);
        info_like = (TextView) findViewById(R.id.textView4);
        info_share = (TextView) findViewById(R.id.textView3);


        final ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.krecior.android"))
                .build();


        shareButton = (Button) findViewById(R.id.sharebutton);
        final ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                System.out.println("share SHARED " + result.getPostId());
                Data.facebookSetShared();
                updateButtonsVisibility();
            }

            @Override
            public void onCancel() {
                System.out.println("share CANCEL");
                updateButtonsVisibility();
            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("share ERROR");
                updateButtonsVisibility();
            }


        }, 121);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareDialog.show(content);
                    }
                });

            }
        });

        updateButtonsVisibility();
    }

    private void updateButtonsVisibility() {
       /* Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            // user has logged in
            likeView.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
        } else {
            // user has not logged in
            likeView.setVisibility(View.INVISIBLE);
            shareButton.setVisibility(View.INVISIBLE);
            authButton.setVisibility(View.VISIBLE);
            t1.setVisibility(View.INVISIBLE);
            t2.setVisibility(View.INVISIBLE);
            v1.setVisibility(View.INVISIBLE);
            v2.setVisibility(View.INVISIBLE);
        }

     */
        shareButton.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        info_share.setVisibility(View.INVISIBLE);
        likeView.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        v1.setVisibility(View.VISIBLE);
        info_like.setVisibility(View.INVISIBLE);

        if (Data.facebookIsShared()) {
            shareButton.setVisibility(View.INVISIBLE);
            t2.setVisibility(View.INVISIBLE);
            v1.setVisibility(View.INVISIBLE);
            info_share.setVisibility(View.VISIBLE);
        }

        if (Data.facebookIsLiked()) {
            likeView.setVisibility(View.INVISIBLE);
            t1.setVisibility(View.INVISIBLE);
            v2.setVisibility(View.INVISIBLE);
            info_like.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (String key : data.getExtras().keySet()) {
            Object value = data.getExtras().get(key);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

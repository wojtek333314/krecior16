package com.krecior.menu.socialNetwork.twitter;

/**
 * Created by Wojciech Osak on 2015-10-20.
 */
public class TwitterPluginListener {
    private TwitterShareListener twitterShareListener;
    private TwitterLikeListener twitterLikeListener;

    public void tweet(){}
    public void like(){}



    public interface TwitterShareListener{
        void onShareSuccess();
    }

    public interface TwitterLikeListener{
        void onLikeSuccess();
    }
}

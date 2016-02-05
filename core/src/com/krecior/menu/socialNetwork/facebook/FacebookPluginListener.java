package com.krecior.menu.socialNetwork.facebook;

/**
 * Created by Wojciech Osak on 2015-10-15.
 */
public class FacebookPluginListener {
    private FacebookShareListener facebookShareListener;
    private FacebookLikeListener facebookLikeListener;
    private FacebookActivityListener activityListener;

    public FacebookPluginListener() {

    }

    public void startFacebookActivity(){

    }

    public void share(){}
    public void like(){}
    public boolean isConnectionAvaliable(){return false;}

    public FacebookActivityListener getActivityListener() {
        return activityListener;
    }

    public void setActivityListener(FacebookActivityListener activityListener) {
        this.activityListener = activityListener;
    }

    public FacebookShareListener getFacebookShareListener() {
        return facebookShareListener;
    }

    public void setFacebookShareListener(FacebookShareListener facebookShareListener) {
        this.facebookShareListener = facebookShareListener;
    }

    public FacebookLikeListener getFacebookLikeListener() {
        return facebookLikeListener;
    }

    public void setFacebookLikeListener(FacebookLikeListener facebookLikeListener) {
        this.facebookLikeListener = facebookLikeListener;
    }

    public interface FacebookShareListener{
        void onShareSuccess();
    }

    public interface FacebookLikeListener{
        void onLikeSuccess();
    }

    public interface FacebookActivityListener{
        void onRunCommand();
    }
}

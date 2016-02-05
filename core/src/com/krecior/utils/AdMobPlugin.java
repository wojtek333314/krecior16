package com.krecior.utils;

/**
 * Created by Wojciech Osak on 2015-12-01.
 */
public class AdMobPlugin {
    private AdMobPluginListener adMobPluginListener;

    public AdMobPlugin(AdMobPluginListener adMobPluginListener) {
        this.adMobPluginListener = adMobPluginListener;
    }

    public AdMobPluginListener getAdMobPluginListener() {
        return adMobPluginListener;
    }

    public interface AdMobPluginListener{
        void hideAd();
        void showAd();
        void loadAd();
    }
}

package com.krecior.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.krecior.Manager;
import com.krecior.menu.socialNetwork.facebook.FacebookPluginListener;
import com.krecior.utils.AdMobPlugin;
import com.krecior.utils.Data;
import com.krecior.utils.LevelDownloadListener;

import mapsUpdater.DownloadFile;
import mapsUpdater.ServerMultiTaskManager;

public class AndroidLauncher extends AndroidApplication {
    public static FacebookPluginListener facebookPluginListener;

    FacebookPluginAndroid facebookPluginAndroid;
    InterstitialAd interstitialAd;
    Manager manager;
    AndroidApplicationConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        facebookPluginAndroid = new FacebookPluginAndroid(this);
        facebookPluginListener = new FacebookPluginListener() {
            @Override
            public void share() {
                facebookPluginAndroid.share(this);
            }

            @Override
            public void like() {
                facebookPluginAndroid.like(this);
            }

            @Override
            public boolean isConnectionAvaliable() {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni == null) {
                    return false;
                } else
                    return true;
            }
        };
        facebookPluginListener.setActivityListener(new FacebookPluginListener.FacebookActivityListener() {
            @Override
            public void onRunCommand() {
                startActivity(new Intent(AndroidLauncher.this, FacebookActivityAndroid.class));
            }
        });

        manager = new Manager(facebookPluginListener, new AdMobPlugin(new AdMobPlugin.AdMobPluginListener() {
            @Override
            public void hideAd() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("hide ad");
                    }
                });
            }

            @Override
            public void showAd() {
                System.out.println("show ad");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (interstitialAd.isLoaded())
                            interstitialAd.show();
                        else
                            loadAd();
                    }
                });

            }

            @Override
            public void loadAd() {
                AndroidLauncher.this.loadAd();
            }
        })


                , new LevelDownloadListener() {
            @Override
            public void reDownload() {
                if (Manager.DEVELOPER_VERSION) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final ProgressDialog progress = ProgressDialog.show(AndroidLauncher.this, "Downloading levels",
                                    "In progress...", true);
                            progress.setIndeterminate(true);
                            progress.setProgress(10);

                            ServerMultiTaskManager serverMultiTaskManager = new ServerMultiTaskManager() {

                                @Override
                                public void updateProgress(float value) {
                                    progress.setProgress((int) (value * 100));
                                    System.out.println(value);
                                }

                                @Override
                                public void onEndTasks() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();
                                            Manager.LEVELS_DOWNLOADED = true;
                                            while (Manager.container == null) {
                                                try {
                                                    Thread.sleep(50);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Manager.container.readLevelsFromServer();
                                        }
                                    });

                                }
                            };
                            for (int i = 1; i < 46; i++) {
                                final int q = i;
                                serverMultiTaskManager.addDownloadTask(new DownloadFile(Integer.toString(i)) {
                                    @Override
                                    public void onEnd(String out) {
                                        System.out.println("level " + q + ":" + out);
                                        Data.setLevelDataJSON(q, out);
                                    }
                                });
                            }
                            serverMultiTaskManager.startDownloading();
                        }
                    });
                }
            }
        });
        config = new AndroidApplicationConfiguration();
        ServerMultiTaskManager serverMultiTaskManager = null;

        if (Manager.DEVELOPER_VERSION) {
            final ProgressDialog progress = ProgressDialog.show(this, "Downloading levels",
                    "In progress...", true);
            progress.setIndeterminate(true);
            progress.setProgress(10);

            serverMultiTaskManager = new ServerMultiTaskManager() {

                @Override
                public void updateProgress(float value) {
                    progress.setProgress((int) (value * 100));
                    System.out.println(value);
                }

                @Override
                public void onEndTasks() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Manager.LEVELS_DOWNLOADED = true;
                            while (Manager.container == null) {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Manager.container.readLevelsFromServer();
                        }
                    });

                }
            };
        }

        initialize(manager, config);
        setupAds();
        loadAd();

        if (manager.getAppStore() == Manager.APPSTORE_GOOGLE) {
            Manager.setPlatformResolver(new GooglePlayResolver(manager));
            Manager.getPlatformResolver().installIAP();
        }



        if (Manager.DEVELOPER_VERSION && serverMultiTaskManager!=null) {
            for (int i = 1; i < 46; i++) {
                final int q = i;
                serverMultiTaskManager.addDownloadTask(new DownloadFile(Integer.toString(i)) {
                    @Override
                    public void onEnd(String out) {
                        System.out.println("level " + q + ":" + out);
                        Data.setLevelDataJSON(q, out);
                    }
                });
            }
            //startActivityForResult(download,55);
            serverMultiTaskManager.startDownloading();
        }
    }

    public void loadAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdRequest.Builder builder = new AdRequest.Builder();
                //builder.setRequestAgent("android_studio:ad_template");
                //builder.addTestDevice("1A7EBB23525E7CFC369398DAD14C4F9A");
                AdRequest ad = builder.build();
                interstitialAd.loadAd(ad);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        FacebookSdk.sdkInitialize(this);
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RESULT:" + data);
        for (String key : data.getExtras().keySet()) {
            Object value = data.getExtras().get(key);
            System.out.println(key + "|" + value.toString() + "|" + value.getClass().getName());
        }
        System.out.println("-------------------------------------");
        Bundle b = data.getExtras().getBundle("com.facebook.platform.protocol.RESULT_ARGS");
        for (String key : b.keySet()) {
            Object value = b.get(key);
            System.out.println(key + "|" + value.toString() + "|" + value.getClass().getName());
        }
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println((data.getExtras().getBundle("com.facebook.platform.protocol.RESULT_ARGS")));
        System.out.println((data.getExtras().get("com.facebook.platform.protocol.PROTOCOL_ACTION")));
        facebookPluginAndroid.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    public void setupAds() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3133542870799246/7186170212");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                System.out.println("ad loaded");
            }

            @Override
            public void onAdClosed() {
                System.out.println("ad closed");
                loadAd();
            }
        });
    }
}

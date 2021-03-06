package com.krecior.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.krecior.menu.ranking.RankingFacade;
import com.krecior.menu.socialNetwork.facebook.FacebookPluginListener;
import com.krecior.utils.AdMobPlugin;
import com.krecior.utils.Data;
import com.krecior.utils.LevelDownloadListener;
import com.krecior.utils.ServerRequestListener;

import mapsUpdater.DownloadFile;
import mapsUpdater.ServerMultiTaskManager;
import ranking.RankingFacadeImpl;

public class AndroidLauncher extends AndroidApplication {
    public static FacebookPluginListener facebookPluginListener;

    private RankingFacadeImpl rankingFacadeImplementation;
    FacebookPluginAndroid facebookPluginAndroid;
    InterstitialAd interstitialAd;
    Manager manager;
    RankingFacade rankingFacade;
    AndroidApplicationConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        facebookPluginAndroid = new FacebookPluginAndroid(this);
        rankingFacadeImplementation = new RankingFacadeImpl(this);
        rankingFacade = new RankingFacade() {
            @Override
            public void registerPoints(String nick, int points, ServerRequestListener listener) {
                if (isOnline())
                    rankingFacadeImplementation.registerPoints(nick, points, listener);
                else {
                    showOfflineMsgbox(listener);
                }
            }

            @Override
            public void getRanking(int points, ServerRequestListener listener) {
                if (isOnline())
                    rankingFacadeImplementation.getPlayersListDependOnNick(points, listener);
                else {
                    showOfflineMsgbox(listener);
                }
            }

            @Override
            public boolean isOnline() {
                return AndroidLauncher.this.isOnline();
            }
        };
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
                return ni != null;
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
        }), new LevelDownloadListener() {
            @Override
            public void reDownload() {
                if (isOnline()) {
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
                                        Data.setLevelDataJSON(q, out);
                                    }
                                });
                            }
                            serverMultiTaskManager.startDownloading();
                        }
                    });
                }
            }
        }, rankingFacade);
        config = new AndroidApplicationConfiguration();

        initialize(manager, config);
        setupAds();
        loadAd();

        if (manager.getAppStore() == Manager.APPSTORE_GOOGLE) {
            Manager.setPlatformResolver(new GooglePlayResolver(manager));
            Manager.getPlatformResolver().installIAP();
        }


        if (isOnline()) {
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
                                Data.setLevelDataJSON(q, out);
                            }
                        });
                    }
                    serverMultiTaskManager.startDownloading();
                }
            });
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

    private void showOfflineMsgbox(final ServerRequestListener serverRequestListener) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("You have no Internet connection!");
        dlgAlert.setTitle("Warning!");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                serverRequestListener.onConnectionError();
            }
        });
        dlgAlert.setCancelable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dlgAlert.create().show();
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
        for (String key : data.getExtras().keySet()) {
            Object value = data.getExtras().get(key);
        }
        Bundle b = data.getExtras().getBundle("com.facebook.platform.protocol.RESULT_ARGS");
        if (b != null && b.keySet() != null)
            for (String key : b.keySet()) {
                Object value = b.get(key);
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    protected boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

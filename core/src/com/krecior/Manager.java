package com.krecior;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.krecior.game.GameScreen;
import com.krecior.menu.MainScreen;
import com.krecior.menu.ScreenType;
import com.krecior.menu.achievements.AchievementsScreen;
import com.krecior.menu.chooseScreen.ChooseScreen;
import com.krecior.menu.coinShop.CoinShopScreen;
import com.krecior.menu.coinShop.appbiling.PlatformResolver;
import com.krecior.menu.itemShop.ItemShopScreen;
import com.krecior.menu.ranking.RankingFacade;
import com.krecior.menu.socialNetwork.SocialNetworkScreen;
import com.krecior.menu.socialNetwork.facebook.FacebookPluginListener;
import com.krecior.sound.SoundManager;
import com.krecior.utils.AdMobPlugin;
import com.krecior.utils.Container;
import com.krecior.utils.Data;
import com.krecior.utils.LevelDownloadListener;

public class Manager extends Game {
    public static  RankingFacade rankingFacade;
    public static LevelDownloadListener levelDownloadListener;
    // ----- app stores -------------------------
    public static boolean DEVELOPER_VERSION = false;
    public static boolean LEVELS_DOWNLOADED = false;
    public static final int APPSTORE_UNDEFINED = 0;
    public static final int APPSTORE_GOOGLE = 1;

    private int isAppStore = APPSTORE_UNDEFINED;
    public final static String diamonds1 = "diamonds1",
            diamonds2 = "diamonds2",
            diamonds3 = "diamonds3";
    static PlatformResolver m_platformResolver;

    private ScreenType actualScreenType;
    private ScreenType lastScreenType;

    private static AdMobPlugin adMobPlugin;
    public static FacebookPluginListener facebookPlugin;
    public static InputMultiplexer inputMultiplexer;
    public static Manager manager;
    public static SoundManager soundManager;
    public static Container container;
    public static Data data;
    public PurchaseManagerConfig purchaseManagerConfig;

    public PurchaseObserver purchaseObserver = new PurchaseObserver() {
        @Override
        public void handleRestore(Transaction[] transactions) {
            for (int i = 0; i < transactions.length; i++) {
                if (checkTransaction(transactions[i].getIdentifier(), true)) break;
            }
        }

        @Override
        public void handleRestoreError(Throwable e) {
            //throw new GdxRuntimeException(e);
        }

        @Override
        public void handleInstall() {
        }

        @Override
        public void handleInstallError(Throwable e) {
            Gdx.app.log("ERROR", "PurchaseObserver: handleInstallError!: " + e.getMessage());
            //throw new GdxRuntimeException(e);
        }

        @Override
        public void handlePurchase(Transaction transaction) {
            checkTransaction(transaction.getIdentifier(), false);
        }

        @Override
        public void handlePurchaseError(Throwable e) {    //--- Amazon IAP: this will be called for cancelled
            //throw new GdxRuntimeException(e);
        }

        @Override
        public void handlePurchaseCanceled() {    //--- will not be called by amazonIAP
        }
    };

    protected boolean checkTransaction(String ID, boolean isRestore) {
        boolean returnbool = false;

        if (diamonds1.equals(ID)) {
            Data.addDiamonds(3000);
            returnbool = true;
        }
        if (diamonds2.equals(ID)) {
            Data.addDiamonds(6000);
            returnbool = true;
        }
        if (diamonds3.equals(ID)) {
            Data.addDiamonds(12000);
            returnbool = true;
        }
        return returnbool;
    }

    public Manager(FacebookPluginListener facebookPlugin, AdMobPlugin adMobPlugin, LevelDownloadListener levelDownloadListener, RankingFacade rankingFacade) {
        Manager.levelDownloadListener = levelDownloadListener;
        Manager.facebookPlugin = facebookPlugin;
        Manager.adMobPlugin = adMobPlugin;
        Manager.rankingFacade = rankingFacade;
        setAppStore(APPSTORE_GOOGLE);    // change this if you deploy to another platform

        // ---- IAP: define products ---------------------
        purchaseManagerConfig = new PurchaseManagerConfig();
        purchaseManagerConfig.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(diamonds1));
        purchaseManagerConfig.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(diamonds2));
        purchaseManagerConfig.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(diamonds3));
    }

    @Override
    public void create() {
        manager = this;
        soundManager = new SoundManager();
        container = new Container();
        data = new Data();
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        lastScreenType = ScreenType.MAIN_SCREEN;
        setScreen(new Intro());
        //getPlatformResolver().requestPurchaseRestore();    // check for purchases in the past
    }

    public void startLevel(int level) {
        Manager.inputMultiplexer.getProcessors().clear();
        lastScreenType = actualScreenType;

        setScreen(new GameScreen(level));

        actualScreenType = ScreenType.GAME_SCREEN;
    }

    public static AdMobPlugin getAdMobPlugin() {
        return adMobPlugin;
    }

    public void changeScreen(ScreenType mScreen) {
        Gdx.input.setInputProcessor(Manager.inputMultiplexer);
        Manager.inputMultiplexer.getProcessors().clear();
        if (mScreen != ScreenType.MAIN_SCREEN)
           ;// Manager.adMobPlugin.getAdMobPluginListener().hideAd();

        switch (mScreen) {
            case MAIN_SCREEN:
                setScreen(screen = new MainScreen());
                actualScreenType = mScreen;
                break;

            case CHOOSE_STAGE:
                lastScreenType = actualScreenType;
                setScreen(new ChooseScreen());
                actualScreenType = mScreen;
                break;

            case ITEMSHOP:
                lastScreenType = actualScreenType;
                setScreen(new ItemShopScreen());
                actualScreenType = mScreen;
                break;

            case COINSHOP:
                lastScreenType = actualScreenType;
                setScreen(new CoinShopScreen());
                actualScreenType = mScreen;
                break;

            case SOCIAL_NETWORK:
                lastScreenType = actualScreenType;
                setScreen(new SocialNetworkScreen());
                actualScreenType = mScreen;
                break;
            case ACHIEVEMENTS:
                lastScreenType = actualScreenType;
                setScreen(new AchievementsScreen());
                actualScreenType = mScreen;
                break;
        }
    }

    public static PlatformResolver getPlatformResolver() {
        return m_platformResolver;
    }

    public static void setPlatformResolver(PlatformResolver platformResolver) {
        m_platformResolver = platformResolver;
    }

    public int getAppStore() {
        return isAppStore;
    }

    public void setAppStore(int isAppStore) {
        this.isAppStore = isAppStore;
    }
}

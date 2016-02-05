package com.krecior.android;

import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.krecior.Manager;
import com.krecior.menu.coinShop.appbiling.PlatformResolver;

/**
 * Created by Wojciech Osak on 2015-11-03.
 */
public class GooglePlayResolver extends PlatformResolver {
    private final static String GOOGLEKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwBkWvDWdr/GUfAc5FUrjOFz+v0wZtTxtYOxiP0s0TCDvqXAfbDpYuw1DqzXmxAePNXAsgJ65jNT3xvrCeGYKCJGQmadhnkPtKg8yj6GjWwMnkMmbFu0GAk1O+L73PQm3pK3QX5Rc1oJwzAaFIg2y+BOeC7Q9fBR5ER/euLerdNsG5qQTTzenbNWHw5ctZk/pH6V4OHED76bUWgsY0KW2VQFA5tdLO6+c4Z0jjgf7kmT8pMaK39IXkog7D0/YygcMIqxgzidl7ZL6gM1emRnoFcYeZ/d8jDSAPx2Fc9WIG+1hsSsBS03zs5+2gim9B6udYlqp8a/j8vrfzDJpQU8uHQIDAQAB";
    static final int RC_REQUEST = 10001;	// (arbitrary) request code for the purchase flow

    public GooglePlayResolver(Manager game) {
        super(game);

        PurchaseManagerConfig config = game.purchaseManagerConfig;
        config.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE, GOOGLEKEY);
        initializeIAP(null, game.purchaseObserver, config);
    }
}
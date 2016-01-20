package com.base;

import android.app.Application;
import android.content.Intent;
import android.util.Log;


/**
 * Created by Thinkpad on 2015/11/10.
 */
public class WApplication extends Application {
    private static final String TAG = "WApplication";
    private boolean IsUsePin = false;
    private WApplication context;
    /** The server information **/
 //   protected ServerInfo serverInfo = null;
    /** The singleton object of NetworkManager **/
   // private static NetworkManager network = null;
    public int newServiceCount = 0;
    /**
     * Returns the singleton of NetworkManager.
     *
     * @return
     */

    /** The server name **/
    public static final String DEFAULT_SERVER_NAME = "corpay server";
    /** The URL for CorPay server **/
    public static final String DEFAULT_PORT = "8086";//
    public static final String DEFAULT_SERVER_ADDRESS = "192.168.1.113";// 测试;Development

    public static final String DEFAULT_SERVER_IP = "http://" + DEFAULT_SERVER_ADDRESS;
    public static final String DEFAULT_SERVER_URL = DEFAULT_SERVER_IP + ":"  +
            DEFAULT_PORT + "/lstBiz/resources";

    /** The singleton object of SPAppManager **/
    //private static SPServiceManager spApp = null;
    /** The singleton object of SettingManager **/
  //  private static SettingManager setting = null;




    /**
     * Returns the singleton of NetworkManager.
     *
     * @return
     */
//    public NetworkManager getNetworkManager() {
//        if (network == null) {
//            network = new NetworkManager(this, new ServerInfo(
//                    DEFAULT_SERVER_NAME, DEFAULT_SERVER_URL));
//            network.startNetworkMonitor();
//        }
//        return network;
//    }

    /**
     * @return
     */
//    public SPServiceManager getSpServiceManager() {
//        if (spApp == null) {
//            spApp = new SPServiceManager(this);
//        }
//        return spApp;
//    }

    /**
     * Returns the singleton of SettingManager.
     *
     * @return
     */
//        public SettingManager getSettingManager() {
//        if (setting == null) {
//            setting = new SettingManager(this);
//        }
//        return setting;
//    }

    /**
     * <pre>
     * application change to PIN screen to verify PIN.
     * PIN verification activity must have intent filter.
     * <li> action : cn.unicompay.wallet.intent.action.PIN_VERIFICATION
     * <li> category : cn.unicompay.wallet.intent.category.DEFAULT
     * </pre>
     */
    public void moveToPinScreen() {
        Log.d(TAG, "moveToPinScreen>> ");
        // if UsePin = false then return;
        if (!IsUsePin)
            return;
        Intent intent = new Intent(
                "cn.unicompay.wallet.intent.ACTION_PIN_VERIFICATION");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }





}

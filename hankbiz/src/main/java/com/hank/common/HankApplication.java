package com.hank.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.LayoutInflater;

import com.base.MApplication;

/**
 * Created by Thinkpad on 2015/11/10.
 */
public class HankApplication extends MApplication {

    protected static HankApplication application;
    private static LayoutInflater inflater;
    private Handler optionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application=this;
    }

    public Handler getOptionHandler() {
        return optionHandler;
    }

    public void setOptionHandler(Handler optionHandler) {
        this.optionHandler = optionHandler;
    }

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


    public static void setVersionName(Context context) {
        setHank_VersionName(getVersionName(context));
    }
    public static String getVersionName(Context context) {
        //注意这个地方，为了保险起见，最好是写上当前的版本号
        String version = "1.3.0";

        try{
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }




}

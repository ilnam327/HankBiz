package com.hank.base;

/**
 * Created by Thinkpad on 2015/11/10.
 */
public class MyApplication extends WApplication {
    /**
     * Log or request TAG
     */
    public static final String TAG = MyApplication.class.getSimpleName();
    public static long lastTime = System.currentTimeMillis();
    public static String Unicom_VersionName = null;//手机钱包当前版本
    public static MyApplication myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized MyApplication getInstance() {
        return myApp;
    }


    public static String getUnicom_VersionName() {
        return Unicom_VersionName;
    }
    public static void setUnicom_VersionName(String unicom_VersionName) {
        Unicom_VersionName = unicom_VersionName;
    }
}

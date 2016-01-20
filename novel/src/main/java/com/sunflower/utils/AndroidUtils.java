package com.sunflower.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Thinkpad on 2015/4/27.
 */
public class AndroidUtils {

    public static String getSharedPreferences(Context ctx, String groupkey, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(groupkey, 0);
        return pref.getString(key, "");
    }


    public static void setSharedPreferences(Context ctx, String groupkey, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(groupkey, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

}

package com.sunflower.utils;

import com.google.gson.Gson;

/**
 * Created by Thinkpad on 2016/1/11.
 */
public class JsonUtils {

    private static Gson gson = null;
    //静态工厂方法
    public static Gson getInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }


}

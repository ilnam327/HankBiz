package com.sunflower.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class NetRequest {

    private static NetRequest netRequest = null;
    private static final int TIMEOUT = 10000;
    private static final int TIMEOUT_SOCKET = 15000;

    // 图片加载
    private Context context;
    /**
     * 上下文
     */
    private Context content;

    /**
     * LRU缓存的数量。为了防止溢出，一般不要超过60
     */
    final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);

    // 网路请求
    private AsyncHttpClient client;


    /* 构造函数 */
    // 图片加载构造函数
    private NetRequest(Context context) {
        this.content = context;
    }

    // 网路请求构造函数
    private NetRequest(Context context, String request) {
        this.client = new AsyncHttpClient();
        this.client.setTimeout(TIMEOUT_SOCKET);
        this.content = context;
    }

    /**
     * 单例模式
     */
    // 图片单例
    public static NetRequest sharedNetRequestWithImage(Context context) {
        if (netRequest == null) {
            synchronized (NetRequest.class) {
                if (netRequest == null) {
                    netRequest = new NetRequest(context);
                }
            }
        }
        return netRequest;
    }

    // 数据请求单例
    public static NetRequest shareNetRequestWithRequest(Context context) {
        if (netRequest == null) {
            synchronized (NetRequest.class) {
                if (netRequest == null) {
                    netRequest = new NetRequest(context, "request");
                }
            }
        }
        return netRequest;
    }
    /**
     * post 请求
     */
    public void post_request_async(String urlStr, RequestParams params, JsonHttpResponseHandler res) {
        this.client.post(this.context, urlStr, params, res);
    }

    public void post_request_json(String urlStr, RequestParams params, JsonHttpResponseHandler res) {
        this.client.post(this.context, urlStr, params, res);

    }
    public void post_json(String urlStr,Header[] headers,HttpEntity httpEntity,
                          String contentType,JsonHttpResponseHandler res){
        this.client.post(this.context,urlStr,headers,httpEntity,contentType,res);

    }

}
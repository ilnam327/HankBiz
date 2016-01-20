package com.base;

import android.app.Activity;
import android.content.Context;

import com.android.http.RequestManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sunflower.network.NetImageCache;
import com.sunflower.tools.ToolNetwork;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Thinkpad on 2015/11/10.
 */
public class MApplication extends WApplication {
    /**
     * Log or request TAG
     */
    public static final String TAG = MApplication.class.getSimpleName();
    public static long lastTime = System.currentTimeMillis();

    public static String getHank_VersionName() {
        return Hank_VersionName;
    }

    public static void setHank_VersionName(String hank_VersionName) {
        Hank_VersionName = hank_VersionName;
    }

    //手机钱包当前版本
    public static String Hank_VersionName = null;

    public static MApplication myApp;
    /**对外提供整个应用生命周期的Context**/
    private static Context instance;
    /***寄存整个应用Activity**/
    private final Stack<WeakReference<Activity>> activities =
            new Stack<WeakReference<Activity>>();
    /**整个应用全局可访问数据集合**/
    private static Map globalData = new HashMap<String, Object>();
    /***volley提供的异步图片缓存**/
    private final NetImageCache imageCacheMap = new NetImageCache();
    /***volley提供的异步图片Loader**/
    private static ImageLoader mImageLoader = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        myApp=this;

        //初始化请求队列
        RequestManager.getInstance().init(this);
        RequestQueue requestQueue=RequestManager.getInstance().getRequestQueue();
        mImageLoader=new ImageLoader(requestQueue,imageCacheMap);
        //初始化图片加载器
        initImageLoader(getApplicationContext());
    }

    /**
     * 对外提供Application Context
     * @return
     */
    public static Context gainContext() {
        return instance;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized MApplication getInstance() {
        return myApp;
    }
    /**
     * 获取图片异步加载器
     * @return ImageLoader
     */
    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 获取网络是否已连接
     * @return
     */
    public static boolean isNetworkReady(){
        return ToolNetwork.getInstance().init(instance).isConnected();
    }

    /**
     * 将Activity压入Application栈
     * @param task 将要压入栈的Activity对象
     */
    public  void pushTask(WeakReference<Activity> task) {
        activities.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     * @param task
     */
    public  void removeTask(WeakReference<Activity> task) {
        activities.remove(task);
    }

    /**
     * 根据指定位置从栈中移除Activity
     * @param taskIndex Activity栈索引
     */
    public  void removeTask(int taskIndex) {
        if (activities.size() > taskIndex)
            activities.remove(taskIndex);
    }

    /**
     * 将栈中Activity移除至栈顶
     */
    public  void removeToTop() {
        int end = activities.size();
        int start = 1;
        for (int i = end - 1; i >= start; i--) {
            if (!activities.get(i).get().isFinishing()) {
                activities.get(i).get().finish();
            }
        }
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    public  void removeAll() {
        //finish所有的Activity
        for (WeakReference<Activity> task : activities) {
            if (!task.get().isFinishing()) {
                task.get().finish();
            }
        }
    }

    /**
     * 往Application放置数据（最大不允许超过5个）
     * @param strKey 存放属性Key
     * @param strValue 数据对象
     */
    public static void assignData(String strKey, Object strValue) {
        if (globalData.size() > 5) {
            throw new RuntimeException("超过允许最大数");
        }
        globalData.put(strKey, strValue);
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }


}

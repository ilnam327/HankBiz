package com.hank.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;

import com.sunflower.utils.AndroidUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Thinkpad on 2015/11/10.
 */
public class HankApplication extends MyApplication {

    protected static HankApplication application;

    private static LayoutInflater inflater;
    private ImageLoader imageLoader;
    private Handler optionHandler;

    public Handler getOptionHandler() {
        return optionHandler;
    }

    public void setOptionHandler(Handler optionHandler) {
        this.optionHandler = optionHandler;
    }




    @Override
    public void onCreate() {
        super.onCreate();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application=this;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            initImageLoader();
            // initDisplayImageOptions();
        }
        return imageLoader;
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        // File cacheDir = StorageUtils.getOwnCacheDirectory(this, "LwxWallet");
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(720, 1280)
                        // default = device screen dimensions
                        // .discCacheExtraOptions(120, 120, CompressFormat.JPEG, 75,
                        // null)
                .threadPoolSize(5)
                        // default
                .threadPriority(Thread.MIN_PRIORITY + 3)
                        // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(1048576 * 10)
                        // default
                .discCache(new UnlimitedDiscCache(cacheDir))
                        // default
                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .defaultDisplayImageOptions(options) // default
                .build();

        imageLoader.init(config);
    }

    public static String getVersionName(Context context) {
        String version = "1.3.0";//注意这个地方，为了保险起见，最好是写上当前的版本号

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

    public static void setVersionName(Context context) {
        setUnicom_VersionName(getVersionName(context));
    }

    public LayoutInflater getInflater() {
        return inflater;
    }


}

package com.sunflower.utils;

import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.base.MApplication;
import com.sunflower.tools.ToolImage;

/**
 * Created by Thinkpad on 2016/1/13.
 */
public class ImageUtils {

    //异步加载图片防止错位方法一： Volley framework
    public  static  void volleyDisplayImage(String imgUrl,ImageView imageView,
            int defaultIcon, int failIcon){
        ImageLoader mImageLoader = MApplication.getImageLoader();
        ImageLoader.ImageListener mImageListener =
                mImageLoader.getImageListener(imageView, defaultIcon, failIcon);
        mImageLoader.get(imgUrl, mImageListener);
    }

    //universalimageloader
    public static  void universalDisplayImage(String imgUrl, ImageView imageView,
        int loadingResId,int errorResId ,int nullResId ){
        com.nostra13.universalimageloader.core.ImageLoader mImageLoader;
        mImageLoader=ToolImage.initImageLoader(MApplication.getInstance());
        mImageLoader.displayImage(imgUrl, imageView, ToolImage.getFadeOptions(
                loadingResId, errorResId, nullResId));
       // mImageLoader.clearDiscCache();
        //mImageLoader.clearMemoryCache();

    }




}

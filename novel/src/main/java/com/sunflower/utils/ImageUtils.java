package com.sunflower.utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Thinkpad on 2016/1/6.
 */
public class ImageUtils {

    private final static String CACHE = "/css";
    private final Map<String, Drawable> cache = new HashMap();

    public static void saveImage(Bitmap bitmap, String imageName)
            throws Exception {
        String filePath = isExistsFilePath();
        FileOutputStream fos = null;
        File file = new File(filePath, imageName);
        try {
            fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImage( String imageName){
        String filePath = isExistsFilePath();
        FileOutputStream fos = null;
        File file = new File(filePath, imageName);
        if(file.exists()){
            file.delete();
        }


    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist =
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        } else {
            Log.e("ERROR", "eeeee");
        }
        return sdDir.toString();
    }

    private static String isExistsFilePath() {
        String filePath = getSDPath() + CACHE;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    public static Bitmap getImageFromSDCard(String imageName) {
        String filepath = getSDPath() + CACHE + "/" + imageName;
        File file = new File(filepath);
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            return bm;
        }
        return null;
    }

    public static String getImagePath(String iamgeName) {
        return getSDPath() + CACHE + "/" + iamgeName;
    }

    public void loadImage(final ImageView imageView, final String urlString) {
        loadImage(imageView, urlString, true);
    }

    public void loadImage(final ImageView imageView, final String urlString, boolean useCache) {
        if (useCache && cache.containsKey(urlString)) {
            imageView.setImageDrawable(cache.get(urlString));
        }

        //You may want to show a "Loading" image here
        //imageView.setImageResource(R.mipmap.image_indicator);
        Log.d(this.getClass().getSimpleName(), "Image url:" + urlString);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageView.setImageDrawable((Drawable) message.obj);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        };

        Runnable runnable = new Runnable() {
            public void run() {
                Drawable drawable = null;
                try {
                    InputStream is = download(urlString);
                    drawable = Drawable.createFromStream(is, "src");

                    if (drawable != null) {
                        cache.put(urlString, drawable);
                    }
                } catch (Exception e) {
                    Log.e(this.getClass().getSimpleName(), "Image download failed", e);
                    //Show "download fail" image
                    //drawable = imageView.getResources().getDrawable(R.mipmap.image_fail);
                }

                //Notify UI thread to show this image using Handler
                Message msg = handler.obtainMessage(1, drawable);
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();

    }

    /**
     * Download image from given url.
     * Make sure you have "android.permission.INTERNET" permission set in AndroidManifest.xml.
     *
     * @param urlString
     * @return
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    private InputStream download(String urlString) throws IOException {
        InputStream inputStream = (InputStream) new URL(urlString).getContent();
        return inputStream;
    }


    /**
     * 用当前时间给取得的图片命名
     *
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat( "'IMG'_yyyy-MM-dd");
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String temp = str.substring(0, 8) + str.substring(9, 13) +
                str.substring(14, 18) + str.substring(19, 23) +
                str.substring(24);
        // String iconName = temp.substring(0, 10) + ".png";
        return dateFormat.format(date)+"_"+getIconName();

    }


    public static String getIconName() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String temp = str.substring(0, 8) + str.substring(9, 13) +
                str.substring(14, 18) + str.substring(19, 23) +
                str.substring(24);
        String iconName = temp.substring(0, 10) + ".jpg";
        return iconName;
    }




}

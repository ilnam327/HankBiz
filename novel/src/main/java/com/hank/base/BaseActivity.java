package com.hank.base;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.sunflower.conf.Define;
import com.sunflower.network.NetUtil;
import com.sunflower.utils.AndroidUtils;
import com.sunflower.utils.ImageUtils;
import com.hank.R;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Thinkpad on 2015/12/25.
 */
public class BaseActivity extends FragmentActivity {

    protected  String CLASS_TAG;
    protected static HankApplication application;
    /*用来标识请求照相功能的activity*/
    protected static final int CAMERA_WITH_DATA = 3023;
    /*用来标识请求gallery的activity*/
    protected static final int PHOTO_PICKED_WITH_DATA = 3021;
    /*拍照的照片存储位置*/
    protected static final File PHOTO_DIR =
            new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    protected File mCurrentPhotoFile;
    protected String userIconName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (HankApplication) this.getApplication();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public  HttpEntity setEntity(JSONObject jo){
        String params="";
        if(jo!=null){
            params=jo.toString();
            Log.i(CLASS_TAG, "Request json===>>>\n" + NetUtil.format(jo.toString()));
        }
        HttpEntity httpEntity= new StringEntity(params, "UTF-8");

        return httpEntity;
    }

    public void toastMessage(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    protected Boolean checkLogin(Context context){
        Boolean flag=false;
        String userId= AndroidUtils.getSharedPreferences(this,
                Define.STRING, Define.USER_ID);
        if(userId.equals("")){
          //  ToastMessage(context,"请先登录!");
            flag=true;
        }
        return flag;
    }

    protected String getUserIcon(Context context){
        return AndroidUtils.getSharedPreferences(context,
                Define.STRING,Define.USER_ICON);
    }

    protected String getUserId(Context context){
        return AndroidUtils.getSharedPreferences(context,
                Define.STRING,Define.USER_ID);
    }

    protected void setUserIcon(Context context,String userIcon){
        AndroidUtils.setSharedPreferences(context,
                Define.STRING, Define.USER_ICON, userIcon);
    }
    protected void setUserId(Context context,String userId){
        AndroidUtils.setSharedPreferences(context,
                Define.STRING, Define.USER_ID, userId);
    }



    //拍照获取图片
    protected void doTakePhoto() {
        try {
            // Launch camera to take photo for selected contact
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            userIconName= ImageUtils.getIconName();
            mCurrentPhotoFile = new File(PHOTO_DIR, userIconName);// 给新照的照片文件命名
            //final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.photoPickerNotFoundText,Toast.LENGTH_LONG).show();
        }
    }

    // 请求Gallery程序
    protected void doPickPhotoFromGallery() {
        try {
            // Launch picker to choose photo for selected contact
            // final Intent intent = getPhotoPickIntent();
            final Intent intent =  new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 80);
            intent.putExtra("outputY", 80);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.photoPickerNotFoundText1,Toast.LENGTH_LONG).show();
        }
    }

    protected void doCropPhoto(File f) {
        try {
            // 启动gallery去剪辑这个照片
            //final Intent intent = getCropImageIntent(Uri.fromFile(f));
            //Constructs an intent for image cropping. 调用图片剪辑程序
            final Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(f), "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 80);
            intent.putExtra("outputY", 80);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (Exception e) {
            Toast.makeText(this, R.string.photoPickerNotFoundText,
                    Toast.LENGTH_LONG).show();
        }
    }







}

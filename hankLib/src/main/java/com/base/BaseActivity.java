package com.base;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.hank.app.R;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Thinkpad on 2015/12/25.
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseActivity {

    protected  String CLASS_TAG="BaseActivity";
    protected static MApplication mApplication = null;;
    /**当前Activity的弱引用，防止内存泄露**/
    private WeakReference<Activity> context = null;
    /**当前Activity渲染的视图View**/
    private View mContextView = null;
    /**共通操作**/
    private Operation mBaseOperation = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CLASS_TAG, "BaseActivity-->onCreate()");
        //获取应用Application
        mApplication = (MApplication) this.getApplication();
        //设置渲染视图View
        mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(mContextView);
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        //实例化共通操作
        mBaseOperation = new Operation(this);
        //初始化控件
        initView(mContextView);
        //业务操作
        doBusiness(this);
        //显示VoerFlowMenu
        displayOverflowMenu(getContext());
        // 隐藏状态栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(CLASS_TAG, "BaseActivity-->onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(CLASS_TAG, "BaseActivity-->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(CLASS_TAG, "BaseActivity-->onResume()");
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(CLASS_TAG, "BaseActivity-->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(CLASS_TAG, "BaseActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(CLASS_TAG, "BaseActivity-->onDestroy()");
        destroy();
        mApplication.removeTask(context);
    }

    /**
     * 获取当前Activity
     * @return
     */
    protected Activity getContext(){
        if(null != context)
            return context.get();
        else
            return null;
    }


    /**
     * 显示Actionbar菜单图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().
                            getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);//显示
                } catch (Exception e) {
                    Log.e(CLASS_TAG, "onMenuOpened-->"+e.getMessage());
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * Actionbar点击返回键关闭事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //关闭窗体动画显示
//			    overridePendingTransition(R.anim.activity_close,R.anim.alpha_out);
                overridePendingTransition(0, R.anim.base_slide_right_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取共通操作机能
     */
    public Operation getOperation(){
        return this.mBaseOperation;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //关闭窗体动画显示
//		overridePendingTransition(R.anim.activity_close,R.anim.alpha_out);
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    /**
     * 显示OverFlowMenu按钮
     * @param mContext 上下文Context
     */
    public void displayOverflowMenu(Context mContext) {
        try {
            ViewConfiguration config = ViewConfiguration.get(mContext);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);//显示
            }
        } catch (Exception e) {
            Log.e("ActionBar", e.getMessage());
        }
    }



    /***********************************************************************************
************************拍照功能****************************************************
 **********************************************************************************/


    /*用来标识请求照相功能的activity*/
    protected static final int CAMERA_WITH_DATA = 3023;
    /*用来标识请求gallery的activity*/
    protected static final int PHOTO_PICKED_WITH_DATA = 3021;
    /*拍照的照片存储位置*/
    protected static final File PHOTO_DIR =
            new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    protected File mCurrentPhotoFile;

    //拍照获取图片
    protected void doTakePhoto(String iconName) {
        try {
            // Launch camera to take photo for selected contact
            PHOTO_DIR.mkdirs();// 创建照片的存储目录

            mCurrentPhotoFile = new File(PHOTO_DIR, iconName);// 给新照的照片文件命名
            //final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            //Toast.makeText(this, R.string.photoPickerNotFoundText,Toast.LENGTH_LONG).show();
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
            //Toast.makeText(this, R.string.photoPickerNotFoundText1,Toast.LENGTH_LONG).show();
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
            //Toast.makeText(this, R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
        }
    }

}

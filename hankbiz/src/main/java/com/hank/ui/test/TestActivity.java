package com.hank.ui.test;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.base.BaseActivity;
import com.hank.R;
import com.sunflower.tools.ToolAlert;
import com.sunflower.tools.ToolToast;

/**
 * Created by Thinkpad on 2016/1/13.
 */
public class TestActivity extends BaseActivity {
    private Context mContext;
    private Button mBtnTest1;
    private Button mBtnTest2;
    private Button mBtnTest3;
    private Button mBtnTest4;
    private Button mBtnTest5;
    private Button mBtnTest6;


    /**
     * 绑定渲染视图的布局文件
     *
     * @return 布局文件资源id
     */
    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    @Override
    public void initView(View view) {

        mBtnTest1= (Button) findViewById(R.id.btn_test_1);
        mBtnTest2= (Button) findViewById(R.id.btn_test_2);
        mBtnTest3= (Button) findViewById(R.id.btn_test_3);
        mBtnTest4= (Button) findViewById(R.id.btn_test_4);
        mBtnTest5= (Button) findViewById(R.id.btn_test_5);
        mBtnTest6= (Button) findViewById(R.id.btn_test_6);

    }

    /**
     * 业务处理操作（onCreate方法中调用）
     *
     * @param mContext 当前Activity对象
     */
    @Override
    public void doBusiness(final Context mContext) {

        mBtnTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.loading(mContext,"加载数据。。。");
            }
        });


        mBtnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolAlert.isLoading()) {
                    ToolAlert.closeLoading();
                }
            }
        });


        mBtnTest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.dialog(mContext,"dialog");
            }
        });


        mBtnTest4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.dialog(mContext,"提示","Message");
            }
        });


        mBtnTest5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.updateProgressText("updateProgressText");
            }
        });


        mBtnTest6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }

    /**
     * 暂停恢复刷新相关操作（onResume方法中调用）
     */
    @Override
    public void resume() {

    }

    /**
     * 销毁、释放资源相关操作（onDestroy方法中调用）
     */
    @Override
    public void destroy() {

    }


}

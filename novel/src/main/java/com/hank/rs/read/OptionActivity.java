package com.hank.rs.read;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hank.R;
import com.hank.base.BaseActivity;
import com.hank.base.HankApplication;

/**
 * Created by Thinkpad on 2015/12/31.
 */
public class OptionActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnNight;
    private RelativeLayout mRelative;

    private HankApplication application;
    private Handler handler;
    private static final int CHANGED = 0x0010;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_layout);
        application= (HankApplication) getApplication();
        handler=application.getOptionHandler();
        initView();
    }

    private void initView() {
        mRelative= (RelativeLayout) findViewById(R.id.relative_option);
        mBtnNight= (Button) findViewById(R.id.btn_night);
        mRelative.getBackground().setAlpha(10);

        mBtnNight.setOnClickListener(this);
        mRelative.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){
            case R.id.btn_night:
                handler.sendEmptyMessage(CHANGED);
                break;
            case R.id.relative_option:
                finish();
                break;
        }

    }
}

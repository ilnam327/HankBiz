package com.hank.ui.actionbar;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;

import com.base.BaseActivity;
import com.hank.R;

import java.lang.reflect.Field;


/**
 * Created by Thinkpad on 2016/1/15.
 */
public class ActionBarActivity extends BaseActivity {
    private Context mContext;
    @Override
    public int bindLayout() {
        return R.layout.activity_actionbar;
    }

    @Override
    public void initView(View view) {
//        ActionBar actionBar=getActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("返回");

//        actionBar.setLogo(R.mipmap.log_icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mContext=getApplicationContext();
        getMenuInflater().inflate(R.menu.listview_activity_menu, menu);
        //forceShowOverflowMenu();
        return true;
    }
    private void forceShowOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }
}

package com.hank.common.widget;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hank.R;

/**
 * Created by Thinkpad on 2016/1/14.
 */
public class ActionBarManager {

    /**
     * 设置居中标题
     * @param mContext 上下文
     * @param actionBar actionbar
     * @param strCenterTitle 中间居中显示标题
     */
    public static void initTitleCenterActionBar(Context mContext,
            ActionBar actionBar,String strCenterTitle){
        LayoutInflater inflator = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View centerTitle = inflator.inflate(R.layout.view_actionbar_title, null);
        TextView title = (TextView) centerTitle.findViewById(R.id.actionbar_title);
        title.setText(strCenterTitle);
        ActionBar.LayoutParams layoutParams =
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(centerTitle,layoutParams);
    }

    /**
     * 更新ActionBar中间标题
     * @param mContext 上下文
     * @param actionBar actionbar
     * @param strCenterTitle 中间居中显示标题
     */
    public static void updateActionCenterTitle(Context mContext,
          ActionBar actionBar,String strCenterTitle){
        ((TextView)actionBar.getCustomView().
                findViewById(R.id.actionbar_title)).setText(strCenterTitle);
    }

    /**
     * 订制一个返回+标题的标题栏
     * @param mContext
     * @param actionBar
     * @param strCenterTitle
     */
    public static void initMenuListTitle(Context mContext,ActionBar actionBar,String strCenterTitle){
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setLogo(R.mipmap.ic_info_white_48dp);
        actionBar.setDisplayUseLogoEnabled(true);
        initTitleCenterActionBar(mContext, actionBar, strCenterTitle);
    }

    /**
     * 订制一个返回+标题的标题栏
     * @param mContext
     * @param actionBar
     * @param strCenterTitle
     */
    public static void initBackTitle(Context mContext,ActionBar actionBar,String strCenterTitle){
        actionBar.setTitle("返回");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        initTitleCenterActionBar(mContext,actionBar,strCenterTitle);
    }


    /**
     * 初始化【提交】右侧按钮菜单
     * @param mOptionsMenu
     */
    public static void initActionBarSubmitButton(Menu mOptionsMenu){
        final MenuItem aboutItem = mOptionsMenu.findItem(R.id.action_about);
        if(null != aboutItem){
            aboutItem.setVisible(false);
        }

        final MenuItem searchItem = mOptionsMenu.findItem(R.id.action_search);
        if(null != searchItem){
            searchItem.setVisible(false);
        }

        final MenuItem favItem = mOptionsMenu.findItem(R.id.action_fav);
        if(null != favItem){
            favItem.setVisible(false);
        }

        final MenuItem settingItem = mOptionsMenu.findItem(R.id.action_settings);
        if(null != settingItem){
            settingItem.setVisible(false);
        }

        final MenuItem shareItem = mOptionsMenu.findItem(R.id.action_share);
        if(null != shareItem){
            shareItem.setVisible(false);
        }

        final MenuItem submitItem = mOptionsMenu.findItem(R.id.action_submit);
        if(null != submitItem){
            submitItem.setVisible(true);
        }
    }
}

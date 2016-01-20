package com.hank.ui;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.base.BaseActivity;
import com.base.MApplication;
import com.hank.R;
import com.hank.ui.actionbar.ActionBarActivity;
import com.hank.ui.listview.ListViewSampleActivity;
import com.hank.ui.login.LoginActivity;
import com.hank.ui.menu.dto.Menu;
import com.hank.ui.menu.service.MenuService;
import com.hank.ui.test.TestActivity;
import com.sunflower.tools.ToolToast;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Thinkpad on 2016/1/12.
 */
public class MenuActivity extends BaseActivity {

    private ListView mListView;
    private ListAdapter mAdapter;

    long waitTime = 2000;
    long touchTime = 0;


    /**
     * 绑定渲染视图的布局文件
     *
     * @return 布局文件资源id
     */
    @Override
    public int bindLayout() {
        return R.layout.menu_layout;
    }
    /**
     * 初始化控件
     *
     * @param view
     */
    @Override
    public void initView(View view) {
        mListView= (ListView) findViewById(R.id.list_menu);

    }

    /**
     * 业务处理操作（onCreate方法中调用）
     *
     * @param mContext 当前Activity对象
     */
    @Override
    public void doBusiness(Context mContext) {
        try {
            mAdapter=new ListAdapter(mContext,R.layout.menu_list_item);
            InputStream inputStream =this.getResources().
                    openRawResource(R.raw.menu);
            List<Menu> menuList= MenuService.getMenus(inputStream);
            mListView.setAdapter(mAdapter);
            mAdapter.addAll(menuList);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {

            e.printStackTrace();
        }

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


    class ListAdapter extends ArrayAdapter<Menu>{
        private LayoutInflater mInflater;
        private int resource;

        public ListAdapter(Context context, int resource) {
            super(context, resource);
            this.mInflater=LayoutInflater.from(context);
            this.resource=resource;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Menu item=getItem(position);
            if (convertView==null) {
                convertView = mInflater.inflate(resource, null);
            }
            RelativeLayout relative= (RelativeLayout) convertView.findViewById(R.id.relative_menu);
            TextView title= (TextView) convertView.findViewById(R.id.text_menu_title);
            TextView date= (TextView) convertView.findViewById(R.id.text_menu_date);
            if(item!=null){
                final int code=Integer.parseInt(item.getValue());
                String sTitle=item.getName();
               // String sValue=item.getValue();
                title.setText(sTitle);
                date.setText(item.getDate());
                relative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (code){
                            case 100:
                                startActivity(new Intent(getContext(), ListViewSampleActivity.class));
                                break;
                            case 101:
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                break;
                            case 102:
                                startActivity(new Intent(getContext(), TestActivity.class));
                                break;
                            case 103:
                                startActivity(new Intent(getContext(), ActionBarActivity.class));
                                break;
                        }
                    }
                });


            }

            return convertView;
        }

    }


    /**
     * 监听[返回]键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回键
        if (KeyEvent.KEYCODE_BACK == keyCode) {

            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                ToolToast.showShort(getContext(),"再按一次，退出程序");
                touchTime = currentTime;
            } else {
                ((MApplication) getApplicationContext()).removeAll();
            }

            return true;
        }
        return false;
    }
}

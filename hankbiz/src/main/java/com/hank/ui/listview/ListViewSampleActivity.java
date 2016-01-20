package com.hank.ui.listview;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.BaseActivity;
import com.google.gson.reflect.TypeToken;
import com.hank.R;
import com.hank.common.conf.Variables;
import com.hank.common.widget.ActionBarManager;
import com.hank.common.network.NetUtil;
import com.hank.ui.listview.dto.Book;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunflower.tools.ToolImage;
import com.sunflower.tools.ToolToast;
import com.sunflower.utils.ImageUtils;
import com.sunflower.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thinkpad on 2016/1/13.
 */
public class ListViewSampleActivity extends BaseActivity {

    private ListView mListView;
    private ListViewAdapter adapter;
    private List<Book> mListBook;
    private String imageURLs[] = new String[]{
            "http://www.daqianduan.com/wp-content/uploads/2014/12/kanjian.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/11/capinfo.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/11/mi-2.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/10/dxy.cn_.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/10/xinhua.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/09/job.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2013/06/ctrip.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/09/ideabinder.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/05/ymatou.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/03/west_house.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/03/youanxianpin.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/02/jd.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2013/11/wealink.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/09/exmail.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2013/09/alipay.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/08/huaqiangbei.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/06/ctrip.png",
            "http://www.daqianduan.com/static/img/thumbnail.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/06/bingdian.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/04/ctrip-wireless.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/12/kanjian.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/11/capinfo.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/11/mi-2.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/10/dxy.cn_.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/10/xinhua.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/09/job.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2013/06/ctrip.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/09/ideabinder.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/05/ymatou.png",
            "http://www.daqianduan.com/wp-content/uploads/2014/03/west_house.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/03/youanxianpin.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2014/02/jd.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2013/11/wealink.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/09/exmail.jpg",
            "http://www.daqianduan.com/wp-content/uploads/2013/09/alipay.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/08/huaqiangbei.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/06/ctrip.png",
            "http://www.daqianduan.com/static/img/thumbnail.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/06/bingdian.png",
            "http://www.daqianduan.com/wp-content/uploads/2013/04/ctrip-wireless.png"
    };

    private String titles[] = new String[]{
            "看见网络科技（上海）有限公司招前端开发工程师",
            "首都信息发展股份有限公司招Web前端工程师(北京-海淀)",
            "小米邀靠谱前端一起玩，更关注用户前端体验(北京)",
            "丁香园求多枚Web前端工程师(杭州滨江 8-15K)",
            "新华网招中高级Web前端开发工程师（北京 8-20K）",
            "好声音母公司梦响强音文化传播招前端、交互和UI设计师(上海)",
            "携程网国际业务部招靠谱前端(HTML+CSS+JS)(上海总部)",
            "ideabinder招聘Web前端开发工程师（JS方向 北京 6-12K）",
            "海外购物公司洋码头招Web前端开发工程师（上海）",
            "金山软件-西山居(珠海)招募前端开发工程师、PHP开发工程师",
            "优安鲜品招Web前端开发工程师(上海)",
            "京东招聘Web前端开发工程师(中/高/资深) 8-22K",
            "若邻网(上海)急聘资深前端工程师",
            "腾讯广州研发线邮箱部门招聘前端开发工程师（内部直招）",
            "支付宝招募资深交互设计师、视觉设计师（内部直招）",
            "华强北商城招聘前端开发工程师",
            "携程(上海)框架研发部招开发工程师(偏前端)",
            "阿里巴巴中文站招聘前端开发",
            "多途网络科技 15K 招聘前端开发工程师",
            "携程无线前端团队招聘 直接内部推荐（携程上海总部）"
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_image_listview;
    }
    @Override
    public void initView(View view) {
        CLASS_TAG=getClass().getSimpleName();
        mListView= (ListView) findViewById(R.id.lv_list);

    }


    @Override
    public void doBusiness(Context mContext) {

        mListBook=new ArrayList();
        adapter=new ListViewAdapter(mContext,R.layout.activity_image_listview_item);
        mListView.setAdapter(adapter);
       // getTopicListData(1,"date","up");

        //构造数据
        for (int i = 0; i < 40; i++) {
           Book book=new Book();
            book.setIcon(imageURLs[i]);
            //book.setBooksTitle(titles[i]);
            mListBook.add(book);
        }
        adapter.clear();
        adapter.addAll(mListBook);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void resume() {

    }

    @Override
    public void destroy() {
        //清除缓存
        ToolImage.clearCache();

    }

    private void getTopicListData(int page,String praise,String rule) {
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("sort", praise);
            reqJo.put("rule",  rule);
            reqJo.put("page", page);
            reqJo.put("userId", "");
        }catch (JSONException e){
            Log.d(CLASS_TAG, e.getMessage());
        }
        NetUtil.postWithJson(getContext(), "getBooksList.do",
                reqJo, new NetUtil.RequestCallBackJson() {
                    @Override
                    public void requestBackResult(JSONObject jsonObject) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (jsonObject.getString("success"));
                            if (success) {
                                String bookList=jsonObject.getString("booksList");
                                mListBook=JsonUtils.getInstance().fromJson(bookList,
                                        new TypeToken<List<Book>>(){}.getType());
                            }
                            adapter.clear();
                            adapter.addAll(mListBook);
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            ToolToast.showShort(CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });
    }


    class ListViewAdapter extends ArrayAdapter<Book>{

        private int resource;
        private LayoutInflater mInflater;

        public ListViewAdapter(Context context, int resource) {
            super(context, resource);
            this.mInflater=LayoutInflater.from(context);
            this.resource=resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Book item=getItem(position);

            if(convertView==null){
                convertView=mInflater.inflate(resource,null);
                TextView content= (TextView) convertView.findViewById(R.id.tv_title);
                ImageView icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                String url=item.getIcon();
              //  String url="http://www.daqianduan.com/wp-content/uploads/2014/12/kanjian.jpg";
//                ImageUtils.universalDisplayImage(url, icon, Variables.FAIL_ICON,
//                        Variables.FAIL_ICON, Variables.FAIL_ICON);
                ImageUtils.universalDisplayImage(url, icon, Variables.FAIL_ICON,
                        Variables.FAIL_ICON, Variables.FAIL_ICON);

            }
            return convertView;
        }
    }
}

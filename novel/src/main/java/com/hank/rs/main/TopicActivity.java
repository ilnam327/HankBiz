package com.hank.rs.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.sunflower.widget.photo.AsyncImageLoader;
import com.sunflower.widget.photo.CircleImageView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hank.R;
import com.hank.base.BaseActivity;

import com.hank.rs.login.LoginActivity;
import com.hank.rs.main.dto.Topic;

import com.hank.rs.personal.OtherPerActivity;
import com.hank.rs.personal.PersonActivity;
import com.hank.rs.write.WriteActivity;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import com.sunflower.conf.*;
import com.sunflower.utils.*;
import com.sunflower.network.*;

/**
 * Created by Thinkpad on 2015/12/25.
 */
public class TopicActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;
    private PullToRefreshListView mListView;
    private TopicListAdapter topicListAdapter;
    private ArrayList<Topic> mTopicListData;

    private Button btnGoodSort;
    private Button btnTimeSort;
    private TextView tvLogin;
    private ImageView ivWrite;
    private CircleImageView ciUserIcon;
    private LinearLayout user_title;

    private int currentPage=1;
    private String lsSort="date";
    private String lsRule="down";
    private Boolean sortFlag=false;
    private int REQ_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        CLASS_TAG=getClass().getName();
        setContentView(R.layout.topic_main);
        initView();
        setUserIcon(getUserIcon(mContext));
        getTopicListData(currentPage, lsSort, lsRule);
        // IMEI
        if(AndroidUtils.getSharedPreferences(mContext,
                Define.STRING,Define.IMEI).equals("")){
            AndroidUtils.setSharedPreferences(mContext,
                    Define.STRING,Define.IMEI,NetUtil.getIMEI(mContext));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
     //   toastMessage(mContext,"onResume");

    }

    private void initView() {

        ciUserIcon= (CircleImageView) findViewById(R.id.ci_user_icon);
        ivWrite= (ImageView) findViewById(R.id.iv_write);
        user_title= (LinearLayout) findViewById(R.id.user_title);
        tvLogin= (TextView) findViewById(R.id.tv_login);
        btnGoodSort= (Button) findViewById(R.id.btn_sort_zan);
        btnTimeSort= (Button) findViewById(R.id.btn_sort_time);

        btnGoodSort.setBackgroundResource(R.mipmap.on_zan);
        btnTimeSort.setBackgroundResource(R.mipmap.off_time);

        btnGoodSort.setOnClickListener(this);
        btnTimeSort.setOnClickListener(this);
        ivWrite.setOnClickListener(this);
        user_title.setOnClickListener(this);

        mTopicListData= new ArrayList<Topic>();

        mListView= (PullToRefreshListView)findViewById(R.id.lv_topic);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        topicListAdapter= new TopicListAdapter(mContext,
                R.layout.topic_list_item,mListView);
        mListView.setAdapter(topicListAdapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            // 下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                getTopicListData(currentPage, lsSort, lsRule);
            }

            // 上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = currentPage + 1;
                getTopicListData(currentPage, lsSort, lsRule);
            }

        });

      //  userId=AndroidUtils.getSharedPreferences(mContext,Define.STRING,Define.USER_ID);
    }

    @Override
    public void onClick(View v) {
        int resId= v.getId();

        mTopicListData.clear();
        switch (resId){
            case R.id.btn_sort_time:
                btnGoodSort.setBackgroundResource(R.mipmap.off_zan);
                btnTimeSort.setBackgroundResource(R.mipmap.on_time);
                lsSort="date";
                if(sortFlag){
                    sortFlag=false;
                    lsRule="up";
                }else {
                    sortFlag=true;
                    lsRule="down";
                }

                currentPage=1;
                getTopicListData(currentPage,lsSort,lsRule);
                break;

            case R.id.btn_sort_zan:
                btnTimeSort.setBackgroundResource(R.mipmap.off_time);
                btnGoodSort.setBackgroundResource(R.mipmap.on_zan);

                lsSort="praise";
                if(sortFlag){
                    sortFlag=false;
                    lsRule="up";
                }else {
                    sortFlag=true;
                    lsRule="down";
                }

                currentPage=1;
                getTopicListData(currentPage, lsSort, lsRule);
                break;

            case R.id.iv_write:
                if(checkLogin(mContext)){
                    toastMessage(mContext, "请先登录！");
                    return;
                }

                Intent intent1=new Intent(mContext,WriteActivity.class);
                intent1.putExtra("chapterNo","");
                intent1.putExtra("write_flag","BOOK");
                startActivity(intent1);


                break;

            case R.id.user_title:
                if(checkLogin(mContext)){
                    Intent intent=new Intent(mContext, LoginActivity.class);
                    startActivityForResult(intent, REQ_CODE);
                }else {
                    Intent intent=new Intent(mContext, PersonActivity.class);
                    startActivityForResult(intent, REQ_CODE);
                }

                break;

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(CLASS_TAG, "requestCode="+requestCode+","+"resultCode="+resultCode);

        if (requestCode == REQ_CODE && resultCode == Variables.RESULT_OK){

            AndroidUtils.setSharedPreferences(mContext,
                        Define.STRING, Define.USER_ID, data.getStringExtra("userId"));
            AndroidUtils.setSharedPreferences(mContext,
                        Define.STRING, Define.USER_ICON, data.getStringExtra("userIcon"));
            AndroidUtils.setSharedPreferences(mContext,
                    Define.STRING, Define.USER_NAME, data.getStringExtra("userName"));
            setUserIcon(data.getStringExtra("userIcon"));
        }else if(requestCode == REQ_CODE && resultCode == Variables.PER_CODE){
            String sUpd=data.getStringExtra("PER_UPD");

            if(sUpd.equals("Y")){

                setUserIcon(getUserIcon(mContext));
                getTopicListData(currentPage, lsSort, lsRule);
            }

        }
    }
    private void setUserCollection(String worksId, String actionUrl){
        JSONObject reqJo = new JSONObject();

        try {
            reqJo.put("worksId", worksId);
            reqJo.put("type", "books");
            reqJo.put("userId", getUserId(mContext));
        }catch (JSONException e){

        }
        NetUtil.post_json(mContext, actionUrl,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));

                            if (success) {


                            } else {
                                toastMessage(mContext, resJsonObj.getString("errMsg"));
                            }
                        } catch (Exception e) {

                        }

                    }
                });


    }

    private void getTopicListData(int page,String praise,String rule) {

        JSONObject reqJo = new JSONObject();

        try {
            reqJo.put("sort", praise);
            reqJo.put("rule",  rule);
            reqJo.put("page", page);
            reqJo.put("userId", getUserId(mContext));
        }catch (JSONException e){
            Log.d(CLASS_TAG,e.getMessage());
        }
        NetUtil.post_json(mContext, Action.GET_BOOK_LST,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject jsonObject) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (jsonObject.getString("success"));
                            if (success) {
                                ObjectMapper mapper = new ObjectMapper();
                                mTopicListData = mapper.readValue(jsonObject.getString("booksList"),
                                        new TypeReference<ArrayList<Topic>>() {});

                                if (mTopicListData.size() > 0 && currentPage > 1) {
                                    mTopicListData.addAll(mTopicListData);
                                } else if (mTopicListData.size() == 0) {
                                    if (currentPage == 1) {
                                        mTopicListData.addAll(null);
                                    } else {
                                        mListView.onRefreshComplete();
                                        return;
                                    }

                                }
                            }
                            topicListAdapter.clear();
                            topicListAdapter.addAll(mTopicListData);
                            topicListAdapter.notifyDataSetChanged();
                            mListView.onRefreshComplete();

                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });


    }


    class TopicListAdapter extends ArrayAdapter<Topic> {
        private LayoutInflater mInflater;
        private int resource;
        private AsyncImageLoader asyncImageLoader;
        private PullToRefreshListView listView;

        public TopicListAdapter(Context context, int resource ,PullToRefreshListView listView) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
            asyncImageLoader=new AsyncImageLoader();
            this.listView=listView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Topic topic=getItem(position);
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=mInflater.inflate(resource,null);

                viewHolder.author= (TextView) convertView.
                        findViewById(R.id.text_topic_author);
                viewHolder.icon= (ImageView) convertView.
                        findViewById(R.id.img_topic_icon);
                viewHolder.title= (TextView) convertView.
                        findViewById(R.id.text_topic_title);
                viewHolder.content= (TextView) convertView.
                        findViewById(R.id.text_topic_content);
                viewHolder.time= (TextView) convertView.
                        findViewById(R.id.text_topic_time);
                viewHolder.goodCnt= (TextView) convertView.
                        findViewById(R.id.text_topic_praisecnt);
                viewHolder.track= (TextView) convertView.
                        findViewById(R.id.tv_track_cnt);
                viewHolder.collect= (Button) convertView.
                        findViewById(R.id.btn_topic_collect);
                viewHolder.chapterCnt= (TextView)convertView.
                        findViewById(R.id.text_topic_chapter_cnt);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

           // NetUtil.displayImage(mContext,viewHolder.icon,topic.getIcon());
            viewHolder.author.setText(topic.getCreatorName());
            viewHolder.time.setText(topic.getCreateDate());
            viewHolder.content.setText(topic.getBooksProfile());
            viewHolder.goodCnt.setText(topic.getPraiseNum());
            viewHolder.title.setText(topic.getBooksTitle());
            viewHolder.track.setText(topic.getClickNum());
            String chapterCnt="更新至"+topic.getChapterNum();
            viewHolder.chapterCnt.setText(chapterCnt);


            final String imageUrl =topic.getIcon();

            viewHolder.icon.setTag(imageUrl);
            Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,
                    new AsyncImageLoader.ImageCallback() {

                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    if(imageUrl.equals("")) return;
                    ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
                    if (imageViewByTag != null) {
                        imageViewByTag.setImageDrawable(imageDrawable);
                    }
                }
            });
            if (cachedImage == null) {
                viewHolder.icon.setImageResource(R.mipmap.default_icon);
            }else{
                viewHolder.icon.setImageDrawable(cachedImage);
            }


            if(topic.getCollectionFlag().equals("Y")){
                viewHolder.collect.setBackgroundResource(R.mipmap.on_collect);
            }else {
                viewHolder.collect.setBackgroundResource(R.mipmap.off_collect);
            }
//
//
            viewHolder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, ChapterActivity.class);
                    intent.putExtra("bookName",topic.getBooksTitle());
                    intent.putExtra("booksTitle",topic.getBooksTitle());
                    intent.putExtra("booksId", topic.getBooksId());
                    startActivity(intent);
                }
            });

            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, OtherPerActivity.class);
                    intent.putExtra("creatorId",topic.getCreatorId());
                    startActivity(intent);
                }
            });
//
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(checkLogin(mContext)){
                        toastMessage(mContext, "请先登录！");
                        return;
                    };


                    if(topic.getCollectionFlag().equals("Y")){
                        topic.setCollectionFlag("N");
                        finalViewHolder.collect.setBackgroundResource(R.mipmap.off_collect);
                        setUserCollection(topic.getBooksId(), Action.CANCEL_COLLECT_WORKS);
                    }else {
                        topic.setCollectionFlag("Y");
                        finalViewHolder.collect.setBackgroundResource(R.mipmap.on_collect);
                        setUserCollection(topic.getBooksId(), Action.COLLECT_WORKS);
                    }


                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView author;
            TextView title;
            TextView content;
            TextView goodCnt;
            TextView time;
            TextView track;
            Button  collect;
            TextView chapterCnt;

        }
    }

    private void setUserIcon(String iconName){
        if(checkLogin(mContext)){
            return;
        }
        ImageView iv = (ImageView) ciUserIcon;
        NetUtil.displayImage(mContext,iv,iconName);

        if(!getUserId(mContext).equals("")){
            tvLogin.setVisibility(View.GONE);
        }else {
            tvLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}

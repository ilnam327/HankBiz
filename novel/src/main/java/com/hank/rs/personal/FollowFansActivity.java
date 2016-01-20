package com.hank.rs.personal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sunflower.network.NetUtil;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;
import com.hank.rs.personal.dto.Fan;
import com.hank.rs.personal.dto.Follow;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/12/29.
 */
public class FollowFansActivity extends BaseActivity {

    private Context mContext;
    private ListView mListView;
    private TextView mTxtTitle;

    private ArrayList<Fan> mFansListData;
    private ArrayList<Follow> mFollowListData;
    private TextView mTextBack;

    private FansAdapter mFansAdapter;
    private FollowAdapter mFollowAdapter;
    private int currentPage=1;

    private String lsFlagFollowFans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_fans_layout);
        mContext=this;
        lsFlagFollowFans=getIntent().getStringExtra("FLAG");
        initView();
    }

    private void initView() {

        mListView= (ListView) findViewById(R.id.list_follow);
        mTxtTitle= (TextView) findViewById(R.id.text_follow_title);
        mTextBack= (TextView) findViewById(R.id.text_follow_back);
        mTextBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(lsFlagFollowFans.equals("FAN")){
            mTxtTitle.setText("粉丝");
            mFansListData=new ArrayList<Fan>();
            mFansAdapter=new FansAdapter(mContext,
                    R.layout.follow_fans_list_item);
            mListView.setAdapter(mFansAdapter);
            getList(currentPage, Action.FANS_LIST,"fansList");
        }else if(lsFlagFollowFans.equals("FOLLOW")){
            mTxtTitle.setText("关注");
            mFollowListData=new ArrayList<Follow>();
            mFollowAdapter=new FollowAdapter(mContext,
                    R.layout.follow_fans_list_item);
            mListView.setAdapter(mFollowAdapter);
            getList(currentPage, Action.FOLLOW_LIST,"followList");
        }

    }


    private void getList(int page,String actionUrl, final String lstFlag){

        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("page", page);
            reqJo.put("userId", getUserId(mContext));
        }catch (JSONException e){
            Log.d(CLASS_TAG, e.getMessage());
        }
        NetUtil.post_json(mContext, actionUrl,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject jsonObject) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (jsonObject.getString("success"));
                            if (success) {
                                ObjectMapper mapper = new ObjectMapper();
                                if(lstFlag.equals("followList")){
                                    mFollowListData.clear();
                                    mFollowListData = mapper.readValue(jsonObject.getString("followList"),
                                            new TypeReference<ArrayList<Follow>>() {});
                                    mFollowAdapter.addAll(mFollowListData);
                                    mFollowAdapter.notifyDataSetChanged();

                                }else if(lstFlag.equals("fansList")){
                                    mFansListData.clear();
                                    mFansListData = mapper.readValue(jsonObject.getString("fansList"),
                                            new TypeReference<ArrayList<Fan>>() {});
                                    mFansAdapter.addAll(mFansListData);
                                    mFansAdapter.notifyDataSetChanged();
                                }
                            }


                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });


    }

    private void setFollow(String actionUrl,String targetUserId){

        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("targetUserId", targetUserId);
            reqJo.put("userId", getUserId(mContext));
        }catch (Exception e){

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



    class FansAdapter extends ArrayAdapter<Fan>{

        private LayoutInflater mInflater;
        private int resource;
        public FansAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Fan fan=getItem(position);
            final ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=mInflater.inflate(resource,null);

                viewHolder.icon= (ImageView)
                        convertView.findViewById(R.id.img_follow_icon);
                viewHolder.name= (TextView)
                        convertView.findViewById(R.id.text_follow_name);
                viewHolder.follow= (Button)
                        convertView.findViewById(R.id.btn_follow);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
           // viewHolder.icon
            viewHolder.name.setText(fan.getUserName());
            NetUtil.displayImage(mContext,viewHolder.icon,fan.getIcon());
            if(fan.getFollowFlag().equals("Y")){
                viewHolder.follow.setBackgroundResource(R.mipmap.on_follow);
            }else if(fan.getFollowFlag().equals("N")) {
                viewHolder.follow.setBackgroundResource(R.mipmap.off_follow);

            }

            viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fan.getFollowFlag().equals("Y")){
                        fan.setFollowFlag("N");
                        setFollow(Action.CANCEL_FOLLOW, fan.getUserId());

                        viewHolder.follow.setBackgroundResource(R.mipmap.off_follow);

                    }else if(fan.getFollowFlag().equals("N")){
                        fan.setFollowFlag("Y");
                        setFollow(Action.FOLLOW,fan.getUserId());
                        viewHolder.follow.setBackgroundResource(R.mipmap.on_follow);

                    }

                }
            });



            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView name;
            Button follow;

        }


    }

    class FollowAdapter extends ArrayAdapter<Follow> {

        private LayoutInflater mInflater;
        private int resource;

        public FollowAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Follow follow = getItem(position);
            final ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = mInflater.inflate(resource, null);
                viewHolder.icon = (ImageView)
                        convertView.findViewById(R.id.img_follow_icon);
                viewHolder.name = (TextView)
                        convertView.findViewById(R.id.text_follow_name);
                viewHolder.follow = (Button)
                        convertView.findViewById(R.id.btn_follow);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(follow.getUserName());
            NetUtil.displayImage(mContext, viewHolder.icon, follow.getIcon());
            if (follow.getFollowFlag().equals("Y")) {
                viewHolder.follow.setBackgroundResource(R.mipmap.on_follow);
            } else if (follow.getFollowFlag().equals("N")) {
                viewHolder.follow.setBackgroundResource(R.mipmap.off_follow);

            }
            viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(follow.getFollowFlag().equals("Y")){
                        setFollow(Action.CANCEL_FOLLOW, follow.getUserId());
                        viewHolder.follow.setBackgroundResource(R.mipmap.off_follow);
                        follow.setFollowFlag("N");
                    }else if(follow.getFollowFlag().equals("N")){
                        setFollow(Action.FOLLOW,follow.getUserId());
                        viewHolder.follow.setBackgroundResource(R.mipmap.on_follow);
                        follow.setFollowFlag("Y");
                    }
                }
            });


            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView name;
            Button follow;

        }
    }
}

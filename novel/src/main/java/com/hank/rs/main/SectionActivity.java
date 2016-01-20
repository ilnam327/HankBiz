package com.hank.rs.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunflower.conf.Action;
import com.sunflower.conf.Variables;
import com.sunflower.network.NetUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hank.R;
import com.hank.base.BaseActivity;
;
import com.hank.rs.main.dto.Section;
import com.hank.rs.read.ReadActivity;
import com.hank.rs.write.WriteActivity;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SKCC on 2015/11/25.
 */
public class SectionActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;
    private ArrayList<Section> mListSectionData;
    private PullToRefreshListView mListView;
    private SectionListAdapter sectionListAdapter;
    private RelativeLayout mRyWrite;
    private  TextView textBack;

    private String lsBookIds;
    private String lsChapterNo;
    private String IsWrite="";
    private String lsSort="date";
    private String lsRule="down";
    private int currentPage=1;

    public  final int REQ_SEC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_main);
        mContext=this;
        CLASS_TAG=getClass().getName();
        initView();
        getSectionList(currentPage,lsSort,lsRule);
    }

    private void initView() {

        lsBookIds=getIntent().getStringExtra("booksId");
        lsChapterNo=getIntent().getStringExtra("chapterNo");
        IsWrite=getIntent().getStringExtra("IsWrite");

        mListSectionData= new ArrayList<Section>();
        sectionListAdapter=new SectionListAdapter(mContext,R.layout.section_list_item);

        mListView= (PullToRefreshListView) findViewById(R.id.lv_content);
        mListView.setAdapter(sectionListAdapter);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            // 下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                getSectionList(currentPage, lsSort, lsRule);
            }

            // 上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

        });



        mRyWrite= (RelativeLayout) findViewById(R.id.rl_sec_write);

        textBack= (TextView) findViewById(R.id.text_section_back);

        if(IsWrite.equals("Y")){
            mRyWrite.setVisibility(View.VISIBLE);
        }else {
            mRyWrite.setVisibility(View.GONE);
        }

        mRyWrite.setOnClickListener(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SEC && resultCode  == Variables.RESULT_OK){
            getSectionList(currentPage,lsSort,lsRule);
        }
    }


    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){

            case R.id.rl_sec_write:
                Intent intent=new Intent(mContext, WriteActivity.class);
                intent.putExtra("write_flag", "CHAPTER");
                intent.putExtra("chapterNo", processString(lsChapterNo));
                intent.putExtra("booksId", lsBookIds);
                intent.putExtra("UPD", "N");
                startActivityForResult(intent, REQ_SEC);
                break;

            case R.id.text_section_back:
                finish();
                break;



        }

    }


    private void getSectionList(int page,String praise,String rule){
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("booksId", lsBookIds);
            reqJo.put("chapterNo", lsChapterNo);
            reqJo.put("sort", praise);
            reqJo.put("rule",  rule);
            reqJo.put("page", page);
            reqJo.put("userId", getUserId(mContext));

        }catch (JSONException e){
            Log.d(CLASS_TAG,e.getMessage());
        }
        NetUtil.post_json(mContext, Action.GET_ARTICLE_LIST,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject jsonObject) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (jsonObject.getString("success"));
                            if (success) {
                                ObjectMapper mapper = new ObjectMapper();
                                mListSectionData = mapper.readValue(jsonObject.getString("articleList"),
                                        new TypeReference<ArrayList<Section>>() {});
                               // int cnt=mListSectionData.size();

                                sectionListAdapter.clear();
                                sectionListAdapter.addAll(mListSectionData);
                                sectionListAdapter.notifyDataSetChanged();
                                mListView.onRefreshComplete();

                            }


                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });



    }


    class SectionListAdapter extends ArrayAdapter<Section> {
        private LayoutInflater mInflater;
        private int resource;
        public SectionListAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Section item=getItem(position);
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=mInflater.inflate(resource,null);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.iv_section_authoricon);
                viewHolder.title= (TextView) convertView.findViewById(R.id.tv_section_title);
                viewHolder.content= (TextView) convertView.findViewById(R.id.tv_section_content);
                viewHolder.author= (TextView) convertView.findViewById(R.id.tv_section_author);
                viewHolder.trackCnt= (TextView) convertView.findViewById(R.id.tv_track_cnt);
                viewHolder.praiseCnt= (TextView) convertView.findViewById(R.id.tv_section_praise);
                viewHolder.time= (TextView) convertView.findViewById(R.id.tv_secion_time);
                viewHolder.chapterNo= (TextView) convertView.findViewById(R.id.text_section_chapter);
                viewHolder.sectionNo= (TextView) convertView.findViewById(R.id.text_section_ser);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            NetUtil.displayImage(mContext, viewHolder.icon, item.getIcon());
            viewHolder.title.setText(item.getArticleTitle());
            viewHolder.author.setText(item.getCreatorName());
            viewHolder.time.setText(item.getCreateDate());
            viewHolder.content.setText(item.getContent());
            viewHolder.praiseCnt.setText(item.getPraiseNum());
            viewHolder.chapterNo.setText(item.getChapterNo());
            viewHolder.sectionNo.setText(item.getNodeNo());

            viewHolder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ReadActivity.class);
                    intent.putExtra("FLAG","SECTION" );
                    intent.putExtra("articleId",item.getArticleId() );
                    intent.putExtra("listData", (Serializable) mListSectionData);
                    intent.putExtra("index",String.valueOf(position));
                    startActivity(intent);

                }
            });


            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView author;
            TextView title;
            TextView content;
            TextView praiseCnt;
            TextView time;
            TextView trackCnt;
            TextView chapterNo;
            TextView sectionNo;
        }
    }

    private String processString(String value){
        int chapterNo;
        if(value==null|| value.equals("")){
            chapterNo=1;
        }else {
            chapterNo=Integer.valueOf(value);
        }

        return String.valueOf(chapterNo);
    }



}

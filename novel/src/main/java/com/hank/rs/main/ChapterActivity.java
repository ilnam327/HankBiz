package com.hank.rs.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunflower.conf.Action;
import com.sunflower.network.NetUtil;
import com.hank.R;
import com.hank.base.BaseActivity;

import com.hank.rs.common.SpinnerItem;
import com.hank.rs.common.SpinnerPopWindow;
import com.hank.rs.main.dto.Chapter;
import com.hank.rs.personal.OtherPerActivity;
import com.hank.rs.read.ReadActivity;


import org.codehaus.jackson.map.ObjectMapper;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ChapterActivity extends BaseActivity implements
        SpinnerPopWindow.IOnItemSelectListener,View.OnClickListener {

    private Context mContext;

    private ArrayList<Chapter> mListChapterData;
    private ListView mListView;
    private ChapterListAdapter chapterListAdapter;

    private SpinnerPopWindow mSpinnerPopWindow;
    private List<SpinnerItem> spinnerItems;

    private TextView tvBookName;
    private RelativeLayout rlWrite;
    private TextView textPage;
    private TextView textBack;


    private String lsBookIds;
    private String lsBookName;
    private String lsChapterCnt;

    private int page=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_main);
        mContext=this;
        CLASS_TAG=getClass().getName();
        initView();
        getChapterList(page);
    }

    private void initView() {

        lsBookIds= getIntent().getStringExtra("booksId");
        lsBookName=getIntent().getStringExtra("bookName");

        textPage= (TextView) findViewById(R.id.btn_page);
        tvBookName= (TextView) findViewById(R.id.tv_bookName);
        rlWrite= (RelativeLayout) findViewById(R.id.rl_chapter_write);
        textBack= (TextView) findViewById(R.id.text_chapter_back);

        spinnerItems=new ArrayList<SpinnerItem>();
        mListChapterData=new ArrayList<Chapter>() ;
        chapterListAdapter=new ChapterListAdapter(mContext,
                R.layout.chapter_list_item);
        mListView= (ListView) findViewById(R.id.lv_content);
        mListView.setAdapter(chapterListAdapter);

        tvBookName.setText(lsBookName);

        rlWrite.setOnClickListener(this);
        textBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int resId= v.getId();
        switch (resId){
            case R.id.text_chapter_back:
                finish();
                break;
            case R.id.rl_chapter_write:

                Intent intent = new Intent(mContext, SectionActivity.class);
                intent.putExtra("booksId", lsBookIds);
                intent.putExtra("chapterNo", processString(lsChapterCnt));
                intent.putExtra("IsWrite", "Y");
                startActivity(intent);

                break;
            case R.id.btn_page:
                showSpinnerWindow();
                break;


        }

    }

    @Override
    public void onItemClick(int pos) {
        setOtherBtnValue(pos);
    }
    private void setOtherBtnValue(int pos){

        Toast.makeText(mContext,String.valueOf(pos),Toast.LENGTH_SHORT).show();
        getChapterList(pos);
    }


    private void showSpinnerWindow(){
        Log.e(CLASS_TAG, "showSpinWindow");
        WindowManager wm = this.getWindowManager();
        mSpinnerPopWindow.setWidth(wm.getDefaultDisplay().getWidth());
        mSpinnerPopWindow.showAsDropDown(textPage);
    }

    private void getChapterList(int currentPage){
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("booksId", lsBookIds);
            reqJo.put("page", currentPage);
            reqJo.put("userId", getUserId(mContext));
            reqJo.put("pageSize", 50);
        }catch (JSONException e){
            Log.d(CLASS_TAG,e.getMessage());
        }
        NetUtil.post_json(mContext, Action.GET_CHAPTER_LIST,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject jsonObject) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (jsonObject.getString("success"));
                            lsChapterCnt=jsonObject.getString("chapterNum");

                            if (success) {
                                ObjectMapper mapper = new ObjectMapper();
                                mListChapterData = mapper.readValue(jsonObject.getString("chapterList"),
                                        new TypeReference<ArrayList<Chapter>>() {});
                                int chapterCnt=mListChapterData.size();
                                if(chapterCnt>0){
                                    if(chapterCnt<20){
                                        textPage.setVisibility(View.GONE);
                                    }else {
                                        textPage.setVisibility(View.VISIBLE);
                                        updPageSize();
                                    }

                                }else {

                                }
                            }
                            chapterListAdapter.addAll(mListChapterData);
                            chapterListAdapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });



    }


    class ChapterListAdapter extends ArrayAdapter<Chapter> {
        private LayoutInflater mInflater;
        private int resource;

        public ChapterListAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Chapter item=getItem(position);
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=mInflater.inflate(resource,null);

                viewHolder.author= (TextView) convertView.findViewById(R.id.tv_author);
                viewHolder.title= (TextView) convertView.findViewById(R.id.tv_sm_title);
                viewHolder.time= (TextView) convertView.findViewById(R.id.tv_sm_time);
                viewHolder.zanCnt= (TextView) convertView.findViewById(R.id.tv_sm_good_cnt);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.ciAuthorIcon);
                viewHolder.chapterNo= (TextView) convertView.findViewById(R.id.tv_chapter_num);
                viewHolder.foodmark= (TextView) convertView.findViewById(R.id.tv_footmark_cnt);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            NetUtil.displayImage(mContext, viewHolder.icon, item.getIcon());
            viewHolder.author.setText(item.getCreatorName());
            viewHolder.title.setText(item.getChapterTitle());
            viewHolder.time.setText(item.getCreateDate());
            viewHolder.zanCnt.setText(item.getPraiseNum());
            viewHolder.title.setText(item.getChapterTitle());
            viewHolder.chapterNo.setText(item.getChapterNo());
            viewHolder.foodmark.setText(item.getReadNum());

            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReadActivity.class);
                    intent.putExtra("articleId", item.getArticleId());
                    intent.putExtra("listData", (Serializable) mListChapterData);
                    intent.putExtra("index", String.valueOf(position));
                    intent.putExtra("FLAG", "CHAPTER");
                    startActivity(intent);
                    finish();

                }
            });

            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, OtherPerActivity.class);
                    intent.putExtra("creatorId", item.getCreatorId());
                    startActivity(intent);
                }
            });


            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView chapterNo;
            TextView author;
            TextView title;
            TextView zanCnt;
            TextView time;
            TextView foodmark;
        }
    }


    private void updPageSize(){

        int page=Integer.valueOf(lsChapterCnt);
        if(page>20 && page<=40){
            spinnerItems=new ArrayList<SpinnerItem>();
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_21_40)));

        }else if(page>40 && page<=60){
            spinnerItems=new ArrayList<SpinnerItem>();
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_21_40)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_41_60)));

        }else if(page>60 && page<=80){
            spinnerItems=new ArrayList<SpinnerItem>();
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_21_40)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_41_60)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_61_80)));

        }else if(page>80 && page<=120){
            spinnerItems=new ArrayList<SpinnerItem>();
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_21_40)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_41_60)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_61_80)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_101_120)));

        }else if(page>120 && page<=140){
            spinnerItems=new ArrayList<SpinnerItem>();
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_21_40)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_41_60)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_61_80)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_101_120)));
            spinnerItems.add(new SpinnerItem("YYY", getResources().getString(R.string.page_121_140)));

        }else if(page>140 && page<160){
            spinnerItems=new ArrayList<SpinnerItem>();
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_21_40)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_41_60)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_61_80)));
            spinnerItems.add(new SpinnerItem("YYY",getResources().getString(R.string.page_101_120)));
            spinnerItems.add(new SpinnerItem("YYY", getResources().getString(R.string.page_121_140)));
            spinnerItems.add(new SpinnerItem("YYY", getResources().getString(R.string.page_141_160)));
        }

        mSpinnerPopWindow = new SpinnerPopWindow(this);
        mSpinnerPopWindow.refreshData(spinnerItems, 0);
        mSpinnerPopWindow.setItemListener(this);
    }

    private String processString(String value){
        int chapterNo;
        if(value==null|| value.equals("")){
            chapterNo=1;
        }else {
            chapterNo=Integer.valueOf(value)+1;
        }

        return String.valueOf(chapterNo);
    }






}

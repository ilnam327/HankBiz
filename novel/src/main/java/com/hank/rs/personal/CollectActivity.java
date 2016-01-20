package com.hank.rs.personal;

import android.content.Context;
import android.content.Intent;
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
import com.sunflower.utils.ReflectUtils;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;

import com.hank.rs.main.ChapterActivity;
import com.hank.rs.personal.dto.CollectBook;
import com.hank.rs.personal.dto.CollectChapter;
import com.hank.rs.read.ReadActivity;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/12/10.
 */
public class CollectActivity extends BaseActivity implements  View.OnClickListener{

    private Context mContext;
    private ListView mListViewNovel;
    private ListView mListViewChapter;
    private ArrayList<CollectBook> mListBook;
    private ArrayList<CollectChapter> mListChapter;

    private CollectNovelAdapter collectNovelAdapter;
    private CollectChapterAdapter collectChapterAdapter;

    private TextView back;
    private Button btnNovel;
    private Button btnChapter;


    private int page=1;
    private String lsCollectFlag="booksList";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);
        mContext=this;
        CLASS_TAG=getClass().getName();
        initView();
        getCollectNovel(page,Action.BOOK_COLLECT_LIST,"booksList");
    }

    private void initView() {

        mListBook=new ArrayList<CollectBook>();
        collectNovelAdapter=new CollectNovelAdapter(mContext,
                R.layout.collect_list_item);
        mListViewNovel= (ListView) findViewById(R.id.list_collect_novel);
        mListViewNovel.setAdapter(collectNovelAdapter);


        mListChapter=new ArrayList<CollectChapter>();
        collectChapterAdapter=new CollectChapterAdapter(mContext,
                R.layout.collect_list_item);
        mListViewChapter= (ListView) findViewById(R.id.list_collect_chapter);
        mListViewChapter.setAdapter(collectChapterAdapter);

        back= (TextView) findViewById(R.id.text_collect_back);
        btnNovel= (Button) findViewById(R.id.btn_collect_novel);
        btnChapter= (Button) findViewById(R.id.btn_collect_chapter);

        btnNovel.setBackgroundResource(R.mipmap.tab_collect_novel_on);
        btnChapter.setBackgroundResource(R.mipmap.tab_collect_chapter_off);



        btnNovel.setOnClickListener(this);
        btnChapter.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){
            case R.id.btn_collect_novel:
                btnNovel.setBackgroundResource(R.mipmap.tab_collect_novel_on);
                btnChapter.setBackgroundResource(R.mipmap.tab_collect_chapter_off);
                getCollectNovel(page, Action.BOOK_COLLECT_LIST, "booksList");
                mListViewNovel.setVisibility(View.VISIBLE);
                mListViewChapter.setVisibility(View.GONE);
                break;
            case R.id.btn_collect_chapter:
                btnNovel.setBackgroundResource(R.mipmap.tab_collect_novel_off);
                btnChapter.setBackgroundResource(R.mipmap.tab_collect_chapter_on);
                getCollectNovel(page, Action.ARTICLE_COLLECT_LIST, "articleList");

                mListViewChapter.setVisibility(View.VISIBLE);
                mListViewNovel.setVisibility(View.GONE);


                break;
            case R.id.text_collect_back:
                finish();
                break;
        }

    }


    private void getCollectNovel(int page, String actionUrl, final String lstName){

        lsCollectFlag=lstName;
        JSONObject reqJo = new JSONObject();

        try {
            reqJo.put("page", page);
            reqJo.put("userId", getUserId(mContext));
        }catch (JSONException e){
            Log.d(CLASS_TAG,e.getMessage());
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
                                if (lsCollectFlag.equals("booksList")) {
                                    mListBook.clear();
                                    mListBook = mapper.readValue(jsonObject.getString("booksList"),
                                            new TypeReference<ArrayList<CollectBook>>() {});
                                    collectNovelAdapter.clear();
                                    collectNovelAdapter.addAll(mListBook);
                                    collectNovelAdapter.notifyDataSetChanged();

                                }else if(lsCollectFlag.equals("articleList")){
                                    mListChapter.clear();
                                    mListChapter = mapper.readValue(jsonObject.getString("articleList"),
                                            new TypeReference<ArrayList<CollectChapter>>() {});
                                    collectChapterAdapter.clear();
                                    collectChapterAdapter.addAll(mListChapter);
                                    collectChapterAdapter.notifyDataSetChanged();
                                }


                            }


                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });


    }

    class CollectNovelAdapter extends ArrayAdapter<CollectBook>{

        private LayoutInflater mInflater;
        private int resource;

        public CollectNovelAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CollectBook collectBook=getItem(position);
            ViewHolder viewHolder;

            if (convertView==null){
                viewHolder=new ViewHolder();

                convertView=mInflater.inflate(resource,null);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.img_collect_icon);
                viewHolder.title= (TextView) convertView.findViewById(R.id.text_collect_title);
                viewHolder.time= (TextView) convertView.findViewById(R.id.text_collect_timelog);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.icon.setBackgroundResource(R.mipmap.novel_icon);
            viewHolder.time.setText(collectBook.getCreateDate());
            viewHolder.title.setText(collectBook.getBooksTitle());
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent intent =new Intent(mContext,ChapterActivity.class);
                        intent.putExtra("booksId", collectBook.getBooksId());
                        intent.putExtra("bookName", collectBook.getBooksTitle());
                        intent.putExtra("booksTitle", collectBook.getBooksTitle());

                        startActivity(intent);
                        finish();
                }
            });



            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView title;
            TextView time;

        }


    }

    class CollectChapterAdapter extends ArrayAdapter<CollectChapter>{

        private LayoutInflater mInflater;
        private int resource;
        public CollectChapterAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CollectChapter collectChapter=getItem(position);
            ViewHolder viewHolder;

            if (convertView==null){
                viewHolder=new ViewHolder();

                convertView=mInflater.inflate(resource,null);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.img_collect_icon);
                viewHolder.title= (TextView) convertView.findViewById(R.id.text_collect_title);
                viewHolder.time= (TextView) convertView.findViewById(R.id.text_collect_timelog);


                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.icon.setBackgroundResource(R.mipmap.chapter_icon);
            viewHolder.time.setText(collectChapter.getCreateDate());
            viewHolder.title.setText(collectChapter.getArticleTitle());
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext,ReadActivity.class);
                    intent.putExtra("articleId",collectChapter.getArticleId() );
                    intent.putExtra("listData", (Serializable) mListChapter);
                    intent.putExtra("index",String.valueOf(position));
                    intent.putExtra("FLAG","CHAPTER");

                    startActivity(intent);
                }
            });



            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView title;
            TextView time;

        }


    }



}

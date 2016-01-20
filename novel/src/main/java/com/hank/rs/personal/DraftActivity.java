package com.hank.rs.personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunflower.network.NetUtil;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;
import com.hank.rs.personal.dto.DraftBook;
import com.hank.rs.personal.dto.DraftChapter;
import com.hank.rs.write.WriteActivity;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/12/10.
 */
public class DraftActivity extends BaseActivity implements  View.OnClickListener{

    private Context mContext;
    private SwipeMenuListView mListViewNovel;
    private SwipeMenuListView mListViewChapter;
    private ArrayList<DraftBook> mListBook;
    private ArrayList<DraftChapter> mListChapter;

    private CollectNovelAdapter collectNovelAdapter;
    private CollectChapterAdapter collectChapterAdapter;

    private TextView back;
    private TextView mTextTitle;
    private Button btnNovel;
    private Button btnChapter;



    private int page=1;
    private String lsCollectFlag="draftList";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);
        mContext=this;
        CLASS_TAG=getClass().getName();
        initView();
        initSwipe();
        getCollectNovel(page,Action.BOOK_DRAFT_LIST,"draftList");
    }

    private void initSwipe() {

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(14);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
// set creator
        mListViewNovel.setMenuCreator(creator);
        mListViewChapter.setMenuCreator(creator);;


        // step 2. listener item click event
        mListViewNovel.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                DraftBook item = mListBook.get(position);
                switch (index) {
                    case 0:
                        deleteListItem(item.getBooksId(), "books", position);
                        break;
                }
                return false;
            }
        });

        // step 2. listener item click event
        mListViewChapter.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                DraftChapter item = mListChapter.get(position);
                switch (index) {
                    case 0:
                        deleteListItem(item.getDraftId(), "article", position);
                        break;
                }
                return false;
            }
        });


    }

    private void initView() {

        mListBook=new ArrayList<DraftBook>();
        collectNovelAdapter=new CollectNovelAdapter(mContext,
                R.layout.collect_list_item);
        mListViewNovel= (SwipeMenuListView) findViewById(R.id.list_collect_novel);
        mListViewNovel.setAdapter(collectNovelAdapter);
        mTextTitle= (TextView) findViewById(R.id.text_title);

        mListChapter=new ArrayList<DraftChapter>();
        collectChapterAdapter=new CollectChapterAdapter(mContext,
                R.layout.collect_list_item);
        mListViewChapter= (SwipeMenuListView) findViewById(R.id.list_collect_chapter);
        mListViewChapter.setAdapter(collectChapterAdapter);

        back= (TextView) findViewById(R.id.text_collect_back);
        btnNovel= (Button) findViewById(R.id.btn_collect_novel);
        btnChapter= (Button) findViewById(R.id.btn_collect_chapter);

        btnNovel.setBackgroundResource(R.mipmap.tab_collect_novel_on);
        btnChapter.setBackgroundResource(R.mipmap.tab_collect_chapter_off);



        btnNovel.setOnClickListener(this);
        btnChapter.setOnClickListener(this);
        back.setOnClickListener(this);
        mTextTitle.setText("我的草稿");

    }

    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){
            case R.id.btn_collect_novel:
                btnNovel.setBackgroundResource(R.mipmap.tab_collect_novel_on);
                btnChapter.setBackgroundResource(R.mipmap.tab_collect_chapter_off);
                getCollectNovel(page, Action.BOOK_DRAFT_LIST, "draftList");
                mListViewNovel.setVisibility(View.VISIBLE);
                mListViewChapter.setVisibility(View.GONE);

                break;

            case R.id.btn_collect_chapter:
                btnNovel.setBackgroundResource(R.mipmap.tab_collect_novel_off);
                btnChapter.setBackgroundResource(R.mipmap.tab_collect_chapter_on);
                getCollectNovel(page, Action.ARTICLE_DRAFT_LIST, "articleDraftList");
                mListViewChapter.setVisibility(View.VISIBLE);
                mListViewNovel.setVisibility(View.GONE);

                break;
            case R.id.text_collect_back:
                finish();
                break;
        }

    }

    private void deleteListItem(String draftId, final String type, final int position){


        JSONObject reqJo = new JSONObject();

        try {
            reqJo.put("draftId", draftId);
            reqJo.put("type", type);
        }catch (JSONException e){
            Log.d(CLASS_TAG,e.getMessage());
        }
        NetUtil.post_json(mContext, Action.DELETE_DRAFT,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject jsonObject) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (jsonObject.getString("success"));
                            if (success) {
                                if(type.equals("books")){
                                    mListBook.remove(position);
                                    collectNovelAdapter.clear();
                                    collectNovelAdapter.addAll(mListBook);
                                    collectNovelAdapter.notifyDataSetChanged();

                                }else if(type.equals("article")){
                                    mListChapter.remove(position);
                                    collectChapterAdapter.clear();
                                    collectChapterAdapter.addAll(mListChapter);
                                    collectChapterAdapter.notifyDataSetChanged();
                                }
                                toastMessage(mContext,"删除成功！");

                            }

                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });

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
                                if (lsCollectFlag.equals("draftList")) {
                                    mListBook.clear();
                                    mListBook = mapper.readValue(jsonObject.getString("draftList"),
                                            new TypeReference<ArrayList<DraftBook>>() {});
                                    collectNovelAdapter.clear();
                                    collectNovelAdapter.addAll(mListBook);
                                    collectNovelAdapter.notifyDataSetChanged();

                                }else if(lsCollectFlag.equals("articleDraftList")){
                                    mListChapter.clear();
                                    mListChapter = mapper.readValue(jsonObject.getString("articleDraftList"),
                                            new TypeReference<ArrayList<DraftChapter>>() {});
                                    collectChapterAdapter.clear();
                                    collectChapterAdapter.addAll(mListChapter);
                                    collectChapterAdapter.notifyDataSetChanged();
                                }

                            }else {
                                toastMessage(mContext, CLASS_TAG + "===>>>" + jsonObject.getString("errMsg"));
                            }


                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });


    }

    class CollectNovelAdapter extends ArrayAdapter<DraftBook>{

        private LayoutInflater mInflater;
        private int resource;

        public CollectNovelAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final DraftBook draftBook=getItem(position);
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
            viewHolder.time.setText(draftBook.getCreateDate());
            viewHolder.title.setText(draftBook.getDraftTitle());
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                Intent intent =new Intent(mContext,WriteActivity.class);

                intent.putExtra("title",draftBook.getDraftTitle());
                intent.putExtra("content",draftBook.getDraftProfile());
                intent.putExtra("write_flag","BOOK");
                intent.putExtra("UPD","Y" );

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

    class CollectChapterAdapter extends ArrayAdapter<DraftChapter>{

        private LayoutInflater mInflater;
        private int resource;
        public CollectChapterAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final DraftChapter draftChapter=getItem(position);
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
            viewHolder.time.setText(draftChapter.getCreateDate());
            viewHolder.title.setText(draftChapter.getDraftTitle());

            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext,WriteActivity.class);

                    intent.putExtra("title",draftChapter.getDraftTitle());
                    intent.putExtra("content",draftChapter.getContent());
                    intent.putExtra("write_flag","CHAPTER");
                    intent.putExtra("UPD","Y" );

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

    private  void  updateUserInfo(){

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}

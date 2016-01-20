package com.hank.rs.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sunflower.conf.Action;
import com.sunflower.network.NetUtil;
import com.hank.R;
import com.hank.base.BaseActivity;

import com.hank.rs.main.ChapterActivity;
import com.hank.rs.personal.dto.Book;
import com.hank.rs.personal.dto.Chapter;
import com.hank.rs.read.ReadActivity;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/12/30.
 */
public class ProductActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;
    private ListView mListViewNovel;
    private ListView mListViewChapter;

    private ArrayList<Book> mArrayListBook;
    private ArrayList<Chapter> mArrayListChapter;

    private ProdBookAdapter prodBookAdapter;
    private ProdChapterAdapter prodChapterAdapter;

    private Button btnNovel;
    private Button btnChapterDel;
    private Button btnChapter;
    private TextView back;
    private String lsUserId;
    private int currentPage=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CLASS_TAG=getClass().getSimpleName();
        mContext=this;
        setContentView(R.layout.product_layout);
        lsUserId=getIntent().getStringExtra("userId");
        initView();
        getProdList(currentPage, Action.USER_BOOK_LIST, "NOVEL", null);
    }

    private void initView() {

        mArrayListBook=new ArrayList<Book>();
        mArrayListChapter=new ArrayList<Chapter>();
        prodBookAdapter=new ProdBookAdapter(mContext,R.layout.product_list_item);
        prodChapterAdapter=new ProdChapterAdapter(mContext,R.layout.product_list_item);
        mListViewNovel= (ListView) findViewById(R.id.list_prod_novel);
        mListViewChapter= (ListView) findViewById(R.id.list_prod_chapter);
        mListViewNovel.setAdapter(prodBookAdapter);
        mListViewChapter.setAdapter(prodChapterAdapter);

        btnNovel= (Button) findViewById(R.id.btn_prod_novel);
        btnChapter= (Button) findViewById(R.id.btn_prod_chapter);
        btnChapterDel= (Button) findViewById(R.id.btn_prod_chapter_del);
        back= (TextView) findViewById(R.id.text_prod_back);

        btnNovel.setOnClickListener(this);
        btnChapterDel.setOnClickListener(this);
        btnChapter.setOnClickListener(this);
        back.setOnClickListener(this);

        mListViewNovel.setVisibility(View.VISIBLE);
        mListViewChapter.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View view) {
        int resId=view.getId();

        switch (resId){
            case R.id.btn_prod_novel:
                btnNovel.setBackgroundResource(R.mipmap.tab_on_novel);
                btnChapterDel.setBackgroundResource(R.mipmap.tab_chapter_del_off);
                btnChapter.setBackgroundResource(R.mipmap.tab_chapter_off);

                getProdList(currentPage, Action.USER_BOOK_LIST, "NOVEL", null);

                mListViewNovel.setVisibility(View.VISIBLE);
                mListViewChapter.setVisibility(View.GONE);
                break;

            case R.id.btn_prod_chapter:

                btnChapter.setBackgroundResource(R.mipmap.tab_chapter_on);
                btnChapterDel.setBackgroundResource(R.mipmap.tab_chapter_del_off);
                btnNovel.setBackgroundResource(R.mipmap.tab_off_novel);
                getProdList(currentPage, Action.USER_ARTICLE_LIST, "CHAPTER", "N");

                mListViewNovel.setVisibility(View.GONE);
                mListViewChapter.setVisibility(View.VISIBLE);
             
                break;

            case R.id.btn_prod_chapter_del:

                btnChapterDel.setBackgroundResource(R.mipmap.tab_chapter_del_on);
                btnChapter.setBackgroundResource(R.mipmap.tab_chapter_off);
                btnNovel.setBackgroundResource(R.mipmap.tab_off_novel);
                getProdList(currentPage, Action.USER_ARTICLE_LIST, "CHAPTER", "Y");

                mListViewNovel.setVisibility(View.GONE);
                mListViewChapter.setVisibility(View.VISIBLE);
                break;

            case R.id.text_prod_back:
                finish();
                break;

        }

    }

    private void getProdList(int page, String actionUrl, final String reqFlag,String determine){
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("userId",lsUserId);
            reqJo.put("page", page);
            reqJo.put("sort","date");
            reqJo.put("rule", "up");
            if(reqFlag.equals("CHAPTER")){
                reqJo.put("derermine",determine);
            }
        }catch (Exception e){

        }
        NetUtil.post_json(mContext, actionUrl,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));
                            if(success){
                                ObjectMapper mapper = new ObjectMapper();
                                if(reqFlag.equals("NOVEL")){
                                    mArrayListBook = mapper.readValue(resJsonObj.getString("booksList"),
                                            new TypeReference<ArrayList<Book>>() {});
                                    prodBookAdapter.clear();
                                    prodBookAdapter.addAll(mArrayListBook);
                                    prodBookAdapter.notifyDataSetChanged();
                                }else if(reqFlag.equals("CHAPTER")){
                                    mArrayListChapter = mapper.readValue(resJsonObj.getString("articleList"),
                                            new TypeReference<ArrayList<Chapter>>() {});
                                    prodChapterAdapter.clear();
                                    prodChapterAdapter.addAll(mArrayListChapter);
                                    prodChapterAdapter.notifyDataSetChanged();
                                }

                            }

                        }catch (Exception e){
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }

                    }
                });
    }



    class ProdBookAdapter extends ArrayAdapter<Book>{
        private LayoutInflater mInflater;
        private int resource;

        public ProdBookAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Book book=getItem(position);
            ViewHolder viewHolder;

            if (convertView==null){
                viewHolder=new ViewHolder();

                convertView=mInflater.inflate(resource,null);
                viewHolder.title = (TextView)
                        convertView.findViewById(R.id.text_prod_novel);
                viewHolder.time = (TextView)
                        convertView.findViewById(R.id.text_prod_time);
                viewHolder.icon= (ImageView)
                        convertView.findViewById(R.id.img_prod_icon);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.icon.setBackgroundResource(R.mipmap.novel_icon);
            viewHolder.time.setText(book.getCreateDate());
            viewHolder.title.setText(book.getBooksTitle());


            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, ChapterActivity.class);
                    intent.putExtra("booksId", book.getBooksId());
                    intent.putExtra("bookName", book.getBooksTitle());
                    intent.putExtra("booksTitle", book.getBooksTitle());

                    startActivity(intent);
                   // finish();
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

    class ProdChapterAdapter extends ArrayAdapter<Chapter>{
        private LayoutInflater mInflater;
        private int resource;

        public ProdChapterAdapter(Context context, int resource) {
            super(context, resource);
            this.resource = resource;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Chapter chapter=getItem(position);
            ViewHolder viewHolder;

            if (convertView==null){
                viewHolder=new ViewHolder();

                convertView=mInflater.inflate(resource,null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.text_prod_novel);
                viewHolder.time = (TextView) convertView.findViewById(R.id.text_prod_time);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.img_prod_icon);
                
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.icon.setBackgroundResource(R.mipmap.chapter_icon);
            viewHolder.time.setText(chapter.getCreateDate());
            viewHolder.title.setText(chapter.getArticleTitle());
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, ReadActivity.class);
                    intent.putExtra("articleId", chapter.getArticleId());
                    intent.putExtra("FLAG", "CHAPTER");
                    intent.putExtra("index",  String.valueOf(position));
                    intent.putExtra("listData", mArrayListChapter);

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

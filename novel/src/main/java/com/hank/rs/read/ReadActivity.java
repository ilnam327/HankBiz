package com.hank.rs.read;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunflower.network.NetUtil;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.hank.base.HankApplication;
import com.sunflower.conf.Action;
import com.hank.rs.main.dto.Chapter;
import com.hank.rs.main.dto.Section;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SKCC on 2015/10/21.
 */
public class ReadActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;

    private TextView tvContent;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvAuthor;
    private TextView mTxtBack;

    private ImageButton ivPre;
    private ImageButton ivNext;


    private  ImageView icon;

    private Button mBtnPraise;
    private Button mBtnCollect;
    private Button mBtnMore;
    private LinearLayout mLinerLayout;

    private String lsFlagPraise;
    private String lsFlagCollect;

    private String lsArticleId;
    ArrayList<Section> mListSection=null;
    ArrayList<Chapter> mListChapter=null;
    ArrayList listData=null;
    private String lsFlag;
    private  int index=0;

    private HankApplication application;
    private static final int CHANGED = 0x0010;
    private MyHandler handler;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);
        mContext=this;
        application= (HankApplication) getApplication();
        handler=new MyHandler();
        CLASS_TAG=getClass().getName();

        lsArticleId=getIntent().getStringExtra("articleId");
        index=Integer.parseInt(getIntent().getStringExtra("index"));
        lsFlag=getIntent().getStringExtra("FLAG");
        listData=(ArrayList<Chapter>)
                getIntent().getSerializableExtra("listData");
        if(lsFlag.equals("CHAPTER")){

            mListChapter=listData;

        }else if(lsFlag.equals("SECTION")){
            mListSection=listData;
        }

        initView();
        getArticleInfo(lsArticleId);

    }

    private void initView() {
        tvContent= (TextView) findViewById(R.id.tv_read_content);
        mLinerLayout= (LinearLayout) findViewById(R.id.liner_read);
        ivPre= (ImageButton) findViewById(R.id.img_read_pre);
        ivNext= (ImageButton) findViewById(R.id.img_read_next);

        mTxtBack= (TextView) findViewById(R.id.tv_read_back);
        mBtnMore= (Button) findViewById(R.id.btn_read_more);
        mBtnPraise= (Button) findViewById(R.id.btn_read_zan);
        mBtnCollect= (Button) findViewById(R.id.btn_read_collect);

        tvContent= (TextView) findViewById(R.id.tv_read_content);
        tvTitle= (TextView) findViewById(R.id.tv_read_title);
        tvTime= (TextView) findViewById(R.id.tv_read_timelog);
        tvAuthor= (TextView) findViewById(R.id.tv_read_author);

        ivPre.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        mTxtBack.setOnClickListener(this);
        mBtnPraise.setOnClickListener(this);
        mBtnCollect.setOnClickListener(this);
        mBtnMore.setOnClickListener(this);


        icon= (ImageView) findViewById(R.id.img_read_icon);

        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){
            case R.id.img_read_pre:
                ivNext.setClickable(true);
                index=index-1;
                if(index<0){
                    index=index+1;
                    ivPre.setClickable(false);
                    return;
                }else {
                    if(lsFlag.equals("CHAPTER")){
                        lsArticleId= mListChapter.get(index).getArticleId();
                    }else if((lsFlag.equals("SECTION"))){
                        lsArticleId= mListSection.get(index).getArticleId();
                    }

                    getArticleInfo(lsArticleId);
                }



                break;

            case R.id.img_read_next:
                ivPre.setClickable(true);
                index=index+1;
                if(index==listData.size()){
                    index=index-1;
                    ivNext.setClickable(false);
                    //  Toast.makeText(mContext,"到头",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Log.d("INDEX", String.valueOf(index));

                    if(lsFlag.equals("CHAPTER")){
                        lsArticleId= mListChapter.get(index).getArticleId();
                    }else if((lsFlag.equals("SECTION"))){
                        lsArticleId= mListSection.get(index).getArticleId();
                    }

                    getArticleInfo(lsArticleId);
                }

                break;

            case R.id.tv_read_back:
                finish();
                break;

            case R.id.btn_read_zan:
                String sActionPraise="";
                if(lsFlagPraise.equals("Y")){
                    lsFlagPraise="N";
                    sActionPraise=Action.CANCEL_PRAISE;
                    mBtnPraise.setBackgroundResource(R.mipmap.off_zhan);
                }else if(lsFlagPraise.equals("N")){
                    lsFlagPraise="Y";
                    sActionPraise=Action.PRAISE;
                    mBtnPraise.setBackgroundResource(R.mipmap.on_zhan);
                }
                setPraiseCollect(sActionPraise, "article");
                break;

            case R.id.btn_read_collect:
                String sActionCollect="";
                if(lsFlagCollect.equals("Y")){
                    lsFlagCollect="N";
                    sActionCollect=Action.CANCEL_COLLECT_WORKS;
                    mBtnCollect.setBackgroundResource(R.mipmap.collect_read_off);
                }else if(lsFlagCollect.equals("N")){
                    lsFlagCollect="Y";
                    sActionCollect=Action.COLLECT_WORKS;
                    mBtnCollect.setBackgroundResource(R.mipmap.collect_read_on);
                }
                setPraiseCollect(sActionCollect, "article");

                break;

            case R.id.btn_read_more:
                application.setOptionHandler(handler);
                Intent intent=new Intent(mContext, OptionActivity.class);
                startActivity(intent);
                break;

        }
    }


    private void getArticleInfo(String articleId){
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("userId", getUserId(mContext));
            reqJo.put("articleId", articleId);
        }catch (JSONException e){
            Log.d(CLASS_TAG,e.getMessage());
        }
        NetUtil.post_json(mContext, Action.GET_ARTICLE_INFO,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject jsonObject) {
                        try {
                            String tile=jsonObject.getString("articleTitle");
                            String content=jsonObject.getString("content");
                            String createName=jsonObject.getString("creatorName");
                            String timeLag= jsonObject.getString("timeLag");
                            lsFlagPraise=jsonObject.getString("praiseFlag");
                            lsFlagCollect=jsonObject.getString("collectionFlag");

                            if(lsFlagPraise.equals("Y")){
                                mBtnPraise.setBackgroundResource(R.mipmap.on_zhan);
                            }else  if(lsFlagPraise.equals("N")){
                                mBtnPraise.setBackgroundResource(R.mipmap.off_zhan);
                            }

                            if(lsFlagCollect.equals("Y")){
                                mBtnCollect.setBackgroundResource(R.mipmap.collect_read_on);
                            }else  if(lsFlagCollect.equals("N")){
                                mBtnCollect.setBackgroundResource(R.mipmap.collect_read_off);
                            }

                            tvContent.setText(content);
                            tvTitle.setText(tile);
                            tvTime.setText(timeLag);
                            tvAuthor.setText(createName);

                          //  NetUtil.displayImage(mContext, icon, );
                        } catch (Exception e) {
                            toastMessage(mContext, CLASS_TAG + "===>>>" + e.getMessage());
                        }
                    }
                });



    }

   private void setPraiseCollect(String actionUrl,String type){

       JSONObject reqJo = new JSONObject();
       try {
         reqJo.put("worksId", lsArticleId);
           reqJo.put("type", type);
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

    public void  setNightMode(){
        mLinerLayout.setBackgroundResource(R.color.black);
        tvContent.setTextColor(Color.parseColor("#8b898a"));
    }

    /**
     * 自己实现 Handler 处理消息更新UI
     *
     * @author mark
     */
    final class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == CHANGED) { // 更新UI
                setNightMode();
            }
        }
    }



}

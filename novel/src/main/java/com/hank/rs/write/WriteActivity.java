package com.hank.rs.write;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunflower.conf.Variables;
import com.sunflower.network.NetUtil;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/12/30.
 */
public class WriteActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;

    private TextView mTxChapterNo;
    private EditText mEdtTitle;
    private EditText mEdtContent;
    private Button mBtnSave;
    private Button mBtnTempSave;
    private TextView back;

    private String lsTitle;

    private String lsChapterNo;
    private String lsWriteFlag;
    private String lsBookId;
    private TextView mTxtTitle;


    private Button yuyin ;
    private BaiduASRDigitalDialog mDialog = null;
    private String lsUPD = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_layout);
        mContext = this;
        CLASS_TAG = getClass().getSimpleName();
        initView();
    }

    private void initView() {
        lsWriteFlag = getIntent().getStringExtra("write_flag");
        lsBookId = getIntent().getStringExtra("booksId");
        lsTitle = getIntent().getStringExtra("title");
        lsChapterNo = getIntent().getStringExtra("chapterNo");
        lsUPD = getIntent().getStringExtra("UPD");

        back = (TextView) findViewById(R.id.text_write_back);
        mTxtTitle = (TextView) findViewById(R.id.text_write_title);
        mTxChapterNo = (TextView) findViewById(R.id.tv_write_chapterno);
        mEdtTitle = (EditText) findViewById(R.id.edt_write_title);
        yuyin = (Button) findViewById(R.id.yuyin);
        mEdtContent = (EditText) findViewById(R.id.edt_write_content);
        mBtnSave = (Button) findViewById(R.id.btn_write_save);
        mBtnTempSave = (Button) findViewById(R.id.btn_write_tempsave);

        yuyin.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnTempSave.setOnClickListener(this);

        if (lsWriteFlag.equals("BOOK")) {
            mTxtTitle.setText("写书籍简介");
            mTxChapterNo.setVisibility(View.GONE);
            if(lsUPD.equals("Y")){
                mEdtTitle.setText(getIntent().getStringExtra("title"));
                mEdtContent.setText(getIntent().getStringExtra("content"));
            }else {
                mTxChapterNo.setVisibility(View.GONE);
                mEdtTitle.setHint(R.string.write_book_hint);
                mEdtContent.setHint(R.string.write_bookprofile_hint);
            }

        } else if (lsWriteFlag.equals("CHAPTER")) {
            mTxtTitle.setText("写文章");
            mTxChapterNo.setVisibility(View.VISIBLE);
            if(lsUPD.equals("Y")){

                mEdtTitle.setText(getIntent().getStringExtra("title"));
                mEdtContent.setText(getIntent().getStringExtra("content"));
            }else {
                mEdtTitle.setHint(R.string.write_chapter_hint);
                mEdtContent.setHint(R.string.write_chapter_profile_hint);
                mTxChapterNo.setText(lsChapterNo);
            }



        }


    }

    final DialogRecognitionListener mRecognitionListener=new DialogRecognitionListener(){
        @Override
        public void onResults(Bundle results){
            //在 Results 中获取 Key 为 DialogRecognitionListener .RESULTS_RECOGNITION 的
            // StringArrayList，可能为空。获取到识别结果后执行相应的业务逻辑即可，此回调会在主线程调用。
            ArrayList<String> rs = results != null ? results
                    .getStringArrayList(RESULTS_RECOGNITION) : null;
            if (rs != null && rs.size() > 0) {
                mEdtContent.setText(mEdtContent.getText()+rs.get(0));
            }
            // hello.setText(results.getString("raw_text"));
        }
    };

    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){
            case R.id.yuyin:
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                Bundle params = new Bundle();
                params.putString(BaiduASRDigitalDialog.PARAM_API_KEY,
                        "gUE1RH3BF17CmRMxeaS6SYEA");
                params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY,
                        "31da3e787ba57ce1c7aa6fa3daf1f94e");

                mDialog = new BaiduASRDigitalDialog(WriteActivity.this, params);
                mDialog.setDialogRecognitionListener(mRecognitionListener);

                mDialog.show();

                break;
            case R.id.btn_write_save:

                if(lsWriteFlag.equals("BOOK")){
                    save(Action.ADD_BOOK,"book");
                }else if(lsWriteFlag.equals("CHAPTER")){
                    save(Action.ADD_ARTICLE,"chapter");
                }

                break;
            case R.id.btn_write_tempsave:

                if(lsWriteFlag.equals("BOOK")){
                    save(Action.ADD_BOOK_DRAFT,"draft_book");
                }else if(lsWriteFlag.equals("CHAPTER")){
                    save(Action.ADD_ARTICLE_DRAFT,"draft_chapter");
                }

                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog!=null){
            mDialog.dismiss();

        }

    }

    private void save(String actionUrl, String saveFlag) {
        String title=mEdtTitle.getText().toString().trim();;
        String content=mEdtContent.getText().toString().trim();

        if (title.equals("")) {
           toastMessage(mContext, "标题不能为空");
            return;
        } else if (content.equals("")) {
            toastMessage(mContext, "内容不能为空！");
            return;
        }

        JSONObject reqJo = new JSONObject();

        try {

            if(saveFlag.equals("book")){
                reqJo.put("booksTitle", title);
                reqJo.put("booksProfile", content);
                reqJo.put("creatorId", getUserId(mContext));
            }else if(saveFlag.equals("draft_book")){
                reqJo.put("draftTitle", title);
                reqJo.put("draftProfile", content);
                reqJo.put("userId", getUserId(mContext));

            }else if(saveFlag.equals("chapter")){
                reqJo.put("booksId", lsBookId);
                reqJo.put("chapterNo", lsChapterNo);
                reqJo.put("articleTitle", title);
                reqJo.put("content", content);
                reqJo.put("creatorId", getUserId(mContext));

            }else if(saveFlag.equals("draft_chapter")){
                reqJo.put("booksId", lsBookId);
                reqJo.put("chapterNo", lsChapterNo);
                reqJo.put("draftTitle", title);
                reqJo.put("content", content);
                reqJo.put("userId", getUserId(mContext));

            }

        } catch (JSONException e) {

        }

        NetUtil.post_json(mContext, actionUrl,
                setEntity(reqJo), new NetUtil.Request_call_back() {

                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try{
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));
                            if(success){
                                toastMessage(mContext,"保存成功！");
                                setResult(Variables.RESULT_OK);
                                finish();
                            }else {
                                toastMessage(mContext, resJsonObj.getString("errMsg"));
                            }
                        }catch (JSONException e){
                            toastMessage(mContext, e.getMessage());
                        }

                    }
                });


    }
}
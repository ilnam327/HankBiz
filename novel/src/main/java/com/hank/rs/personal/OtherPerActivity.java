package com.hank.rs.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunflower.conf.Variables;
import com.sunflower.network.NetUtil;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thinkpad on 2015/12/31.
 */
public class OtherPerActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;


    private TextView mTextProdCnt;
    private ImageView mImgOtherIcon;
    private TextView mTextOtherName;
    private TextView mTextOtherSign;
    private Button mBtnOtherFollow;
    private TextView mTextBack;

    private RelativeLayout mRelativeProdCnt;
    private String lsFollowFlag;
    private String lsTargetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_per_layout);
        mContext=this;
        CLASS_TAG=getClass().getSimpleName();

        initView();

      //  getUserInfo();
        lsTargetId=getIntent().getStringExtra("creatorId");
        getOtherPerInfo(lsTargetId);
    }

    private void initView(){
        mTextProdCnt= (TextView) findViewById(R.id.text_other_prod_cnt);
        mImgOtherIcon= (ImageView) findViewById(R.id.img_other_uesricon);
        mTextOtherSign= (TextView) findViewById(R.id.text_other_sign);
        mTextOtherName= (TextView) findViewById(R.id.text_other_username);
        mBtnOtherFollow= (Button) findViewById(R.id.btn_other_follow);
        mRelativeProdCnt= (RelativeLayout) findViewById(R.id.relative_other_prod);
        mTextBack= (TextView) findViewById(R.id.text_other_back);

        mBtnOtherFollow.setOnClickListener(this);
        mRelativeProdCnt.setOnClickListener(this);
        mTextBack.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){
            case R.id.btn_other_follow:
                if(lsFollowFlag.equals("Y")){
                    lsFollowFlag="N";
                    setFollow(Action.CANCEL_FOLLOW,lsTargetId,lsFollowFlag);
                    mBtnOtherFollow.setText("关注");
                }else if(lsFollowFlag.equals("N")){
                    lsFollowFlag="Y";
                    setFollow(Action.FOLLOW,lsTargetId,lsFollowFlag);
                    mBtnOtherFollow.setText("取消关注");
                }

                break;

            case R.id.relative_other_prod:
                Intent intent=new Intent(mContext,ProductActivity.class);
                intent.putExtra("userId",lsTargetId);
                startActivity(intent);
                break;

            case R.id.text_other_back:
               finish();
                break;

        }

    }

    private void getOtherPerInfo(String targetId){
        JSONObject reqJo = new JSONObject();

        try {
            reqJo.put("userId", getUserId(mContext));
            reqJo.put("targetUserId", targetId);

        }catch (JSONException e){

        }
        NetUtil.post_json(mContext, Action.GET_TARGET_INFO,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString(Variables.SUCCESS));

                            if (success) {

                                mTextOtherSign.setText(resJsonObj.getString("sign"));
                                mTextOtherName.setText(resJsonObj.getString("userName"));
                                mTextProdCnt.setText(resJsonObj.getString("workNum"));
                                NetUtil.displayImage(mContext, mImgOtherIcon, resJsonObj.getString("icon"));
                                lsFollowFlag=resJsonObj.getString("followFlag");
                                if(lsFollowFlag.equals("Y")){
                                    mBtnOtherFollow.setText("取消关注");
                                }else if(lsFollowFlag.equals("N")){
                                    mBtnOtherFollow.setText("关注");
                                }

                            } else {
                                toastMessage(mContext, resJsonObj.getString("errMsg"));
                            }
                        } catch (Exception e) {

                        }

                    }
                });
    }

    private void setFollow(String actionUrl,String targetId, final String flag){

        JSONObject reqJo = new JSONObject();

        try {
            reqJo.put("userId", getUserId(mContext));
            reqJo.put("targetUserId", targetId);

        }catch (JSONException e){

        }
        NetUtil.post_json(mContext, actionUrl,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString(Variables.SUCCESS));

                            if (success) {
                                if(flag.equals("Y")){
                                    toastMessage(mContext,"关注成功！");
                                }else if(flag.equals("N")){
                                    toastMessage(mContext,"取消关注成功！");
                                }

                            } else {
                                toastMessage(mContext, resJsonObj.getString("errMsg"));
                            }
                        } catch (Exception e) {

                        }

                    }
                });

    }
}

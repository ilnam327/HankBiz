package com.hank.rs.personal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunflower.conf.Define;
import com.sunflower.conf.Variables;
import com.sunflower.network.NetUtil;
import com.sunflower.utils.AndroidUtils;
import com.sunflower.utils.ImageUtils;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;
import com.hank.rs.main.TopicActivity;

import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SKCC on 2015/10/22.
 */
public class PersonActivity extends BaseActivity implements View.OnClickListener{
  
    private Context mContext;

    private LinearLayout mLinerCollect;
    private LinearLayout mLinerProduct;
    private LinearLayout mLinerFollow;
    private LinearLayout mLinerFans;

    private EditText mTxtUserName;
    private EditText mEdtUserSign;
    private ImageView mImgUserIcon;
    private TextView mTextSave;

    private TextView mTxtCollectCnt;
    private TextView mTxtProdCnt;
    private TextView mTxtFansCnt;
    private TextView mTxtFollowCnt;

    private RelativeLayout mRelativeTemp;
    private RelativeLayout mLinerUpdPw;
    private RelativeLayout mLinerClear;

    private Button mBtnLogout;
    private Button mTxtBack;
    private String lsUPD="N";
    private String lsUserName;
    private String lsUserSign;
    private String lsUserIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_layout);
        mContext=this;
        CLASS_TAG=getClass().getSimpleName();
      
        initView();
        getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String userName=mTxtUserName.getText().toString().trim();
        String userSign=mEdtUserSign.getText().toString().trim();

        if(!userName.equals(lsUserName)|| !userSign.equals(lsUserSign)){
            updUserInfo(lsUserIcon);
        }

    }

    private void initView() {

        mLinerCollect= (LinearLayout) findViewById(R.id.liner_per_collect);
        mLinerProduct= (LinearLayout) findViewById(R.id.liner_per_prod);
        mLinerFollow= (LinearLayout) findViewById(R.id.liner_per_follow);
        mLinerFans= (LinearLayout) findViewById(R.id.liner_per_fans);
        mTxtUserName= (EditText) findViewById(R.id.text_per_username);
        mEdtUserSign= (EditText) findViewById(R.id.text_per_sign);
        mImgUserIcon= (ImageView) findViewById(R.id.img_per_uesricon);
        mTxtBack= (Button) findViewById(R.id.text_per_back);
        mBtnLogout= (Button) findViewById(R.id.btn_per_logout);
        mLinerUpdPw= (RelativeLayout) findViewById(R.id.liner_per_updpw);
        mRelativeTemp= (RelativeLayout) findViewById(R.id.relative_per_temp);
        mTextSave= (TextView) findViewById(R.id.text_per_save);

        mTxtFansCnt= (TextView) findViewById(R.id.text_per_fanscnt);
        mTxtFollowCnt= (TextView) findViewById(R.id.text_per_followcnt);
        mTxtCollectCnt= (TextView) findViewById(R.id.text_per_collectcnt);
        mTxtProdCnt= (TextView) findViewById(R.id.text_per_prodcnt);

        mLinerCollect.setOnClickListener(this);
        mLinerProduct.setOnClickListener(this);
        mLinerFollow.setOnClickListener(this);
        mLinerFans.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        mLinerUpdPw.setOnClickListener(this);
        mImgUserIcon.setOnClickListener(this);
        mTxtBack.setOnClickListener(this);
        mTextSave.setOnClickListener(this);
        mRelativeTemp.setOnClickListener(this);

        NetUtil.displayImage(mContext, mImgUserIcon, getUserIcon(mContext));
    }


    @Override
    public void onClick(View v) {

        int resId=v.getId();
        switch (resId) {
            case R.id.liner_per_collect:
                Intent intent1 = new Intent(mContext, CollectActivity.class);
                startActivity(intent1);
                break;

            case R.id.liner_per_follow :
                Intent intent3=new Intent(mContext,FollowFansActivity.class);
                intent3.putExtra("FLAG","FOLLOW");
                startActivity(intent3);
                break;

            case R.id.liner_per_fans :
                Intent intent4=new Intent(mContext,FollowFansActivity.class);
                intent4.putExtra("FLAG","FAN");
                startActivity(intent4);
                break;

            case R.id.relative_per_temp :
                Intent intent_6=new Intent(mContext,DraftActivity.class);
                startActivity(intent_6);
                break;


            case R.id.btn_per_logout :
                AndroidUtils.setSharedPreferences(mContext, Define.JSON, Define.USER_INFO, "");
                AndroidUtils.setSharedPreferences(mContext,Define.STRING, Define.USER_ID,"");
                AndroidUtils.setSharedPreferences(mContext,Define.STRING, Define.USER_ICON,"");

                Intent intent_2=new Intent(mContext, TopicActivity.class);
                startActivity(intent_2);
                finish();
                break;

            case R.id.liner_per_prod :
                Intent intent2=new Intent(mContext,ProductActivity.class);
                intent2.putExtra("userId",getUserId(mContext));
                startActivity(intent2);
                break;

             case R.id.img_per_uesricon :
                 doPickPhotoAction();
                break;

            case R.id.text_per_back:
                Intent intent0=new Intent();
                intent0.putExtra("PER_UPD",lsUPD);
                setResult(Variables.PER_CODE,intent0);
                finish();
                break;
            case R.id.liner_per_updpw:
                startActivity(new Intent(mContext, UpdatePwActivity.class));
                break;

        }


    }


    private void getUserInfo(){
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("userId", getUserId(mContext));

        }catch (JSONException e){

        }
        NetUtil.post_json(mContext, Action.USER_INFO_INIT,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));

                            if (success) {
                                mTxtProdCnt.setText(resJsonObj.getString("workNum"));
                                mTxtCollectCnt.setText(resJsonObj.getString("collectionNum"));
                                mTxtFansCnt.setText(resJsonObj.getString("fanNum"));
                                mTxtFollowCnt.setText(resJsonObj.getString("followNum"));
                                lsUserName=resJsonObj.getString("userName");
                                lsUserSign=resJsonObj.getString("sign");
                                lsUserIcon=resJsonObj.getString("icon");
                                mEdtUserSign.setText(resJsonObj.getString("sign"));
                                mTxtUserName.setText(resJsonObj.getString("userName"));


                            } else {
                                toastMessage(mContext, resJsonObj.getString("errMsg"));
                            }
                        } catch (Exception e) {

                        }

                    }
                });


    }

    private void  updUserInfo(String userIcon){

        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("userId", getUserId(mContext));
            reqJo.put("userName", mTxtUserName.getText().toString().trim());
            reqJo.put("sign", mEdtUserSign.getText().toString().trim());
            reqJo.put("icon", userIcon);
            reqJo.put("sex", "");
            reqJo.put("city","");
            reqJo.put("remind", "");

        }catch (JSONException e){

        }
        NetUtil.post_json(mContext, Action.MODIFY_USER_INFO,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));

                            if (success) {
                                mEdtUserSign.setText(resJsonObj.getString("sign"));
                                mTxtUserName.setText(resJsonObj.getString("userName"));
                                setUserIcon(mContext, resJsonObj.getString("icon"));
                                lsUPD="Y";

                            } else {
                                toastMessage(mContext, resJsonObj.getString("errMsg"));
                            }
                        } catch (Exception e) {

                        }

                    }
                });



    }

/*
*************************************** 拍照*******************************************
 */
    private void doPickPhotoAction() {
        // Wrap our context to inflate list items using correct theme
        final Context dialogContext =
                new ContextThemeWrapper(mContext, android.R.style.Theme_Light);
        String cancel="取消";
        String[] choices=new String[2];
        choices[0] = getString(R.string.take_photo);  //拍照
        choices[1] = getString(R.string.pick_photo);  //从相册中选择
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
                android.R.layout.simple_list_item_1, choices);

        final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setTitle(R.string.attachToContact);
        builder.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:{
                                String status=Environment.getExternalStorageState();
                                if(status.equals(Environment.MEDIA_MOUNTED)){//判断是否有SD卡
                                    doTakePhoto();// 用户点击了从照相机获取
                                }
                                else{
                                   // showToast("没有SD卡");
                                    toastMessage(mContext,"没有SD卡");
                                }
                                break;
                            }
                            case 1:
                                doPickPhotoFromGallery();// 从相册中去获取
                                break;
                        }
                    }
                });
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder.create().show();
    }


    // 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            // 调用Gallery返回的
            case PHOTO_PICKED_WITH_DATA: {
                try {
                    final Bitmap photo = data.getParcelableExtra("data");
                    userIconName=ImageUtils.getIconName();
                    NetUtil.upload_file(mContext, photo, userIconName, new NetUtil.Request_call_back() {
                        @Override
                        public void request_back_result(JSONObject resJsonObj) {
                            try {
                                lsUserIcon=resJsonObj.getString("icon");
                                updUserInfo(lsUserIcon);
                                setUserIcon(mContext,lsUserIcon);
                                toastMessage(mContext, "头像上传成功！");
                                mImgUserIcon.setImageBitmap(photo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            // 照相机程序返回的,再次调用图片剪辑程序去修剪图片
            case CAMERA_WITH_DATA: {
                doCropPhoto(mCurrentPhotoFile);
                break;
            }
        }
    }


}




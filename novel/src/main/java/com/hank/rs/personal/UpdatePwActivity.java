package com.hank.rs.personal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import com.sunflower.conf.Action;
import com.sunflower.network.NetUtil;

import com.hank.R;
import com.hank.base.BaseActivity;



import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thinkpad on 2016/1/4.
 */
public class UpdatePwActivity extends BaseActivity  {

    private Context mContext;

    private Button mBtnUpdatePW;
    private EditText mEdtOldPassword;
    private EditText mEdtNewPassword;
    private TextView mTextBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updpw_layout);
        CLASS_TAG=getClass().getName();
        mContext=this;
        initView();
    }
    private void initView() {
        mBtnUpdatePW= (Button) findViewById(R.id.btn_password);

        mEdtOldPassword= (EditText) findViewById(R.id.edt_old_password);
        mEdtNewPassword= (EditText) findViewById(R.id.edt_new_password);
        mTextBack= (TextView) findViewById(R.id.text_upd_back);

        mTextBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mBtnUpdatePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sOldPw=mEdtOldPassword.getText().toString().trim();
                String sNewPw=mEdtNewPassword.getText().toString().trim();

                if(sOldPw==null||sOldPw.equals("")){
                    Toast.makeText(mContext, "旧密码不能空！", Toast.LENGTH_SHORT);
                    return;
                }

                if(sNewPw==null || sNewPw.equals("")){
                    Toast.makeText(mContext,"新密码不能空！",Toast.LENGTH_SHORT);
                    return;
                }
                updatePassword(sOldPw, sNewPw);
            }
        });


    }

    private void updatePassword(String oldPw,String newPw) {
        JSONObject reqJo = new JSONObject();

        try {
            reqJo.put("password", oldPw);
            reqJo.put("newPassword",  newPw);
            reqJo.put("userId", getUserId(mContext));

        }catch (JSONException e){

        }
        NetUtil.post_json(mContext, Action.CHANGE_PW,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));

                            if (success) {
                                toastMessage(mContext, "修改密码成功！");
                                finish();

                            } else {
                                toastMessage(mContext, resJsonObj.getString("errMsg"));
                            }
                        } catch (Exception e) {

                        }

                    }
                });
    }
}


package com.hank.rs.register;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sunflower.network.NetUtil;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thinkpad on 2015/12/30.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private Button btnRegister;
    private Button btnGetCode;
    private EditText edUserId;
    private EditText edPassword;
    private EditText edCode;

    private TextView mTxtBack;

    private String lsUserId = "";
    private String lsPassword = "";
    private String lsVerifyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        mContext = this;
        initView();
     //   edUserId.setText("xurinan@skccchina.com");
        lsUserId = edUserId.getText().toString().trim();

    }

    private void initView() {

        btnGetCode = (Button) findViewById(R.id.btn_getcode);
        btnRegister = (Button) findViewById(R.id.btn_register);
        mTxtBack = (TextView) findViewById(R.id.text_register_back);

        edUserId = (EditText) findViewById(R.id.et_userid);
        edPassword = (EditText) findViewById(R.id.et_password);
        edCode = (EditText) findViewById(R.id.et_code);

        btnRegister.setOnClickListener(this);
        mTxtBack.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int resId = v.getId();
        switch (resId) {
            case R.id.btn_getcode:
                String userId=edUserId.getText().toString().trim();
                if(userId.equals("")){
                    toastMessage(mContext,"用户ID 不能空！");
                    return;
                }
                getCode(userId);

                break;

            case R.id.btn_register:

                lsUserId = edUserId.getText().toString().trim();
                lsPassword = edPassword.getText().toString().trim();
                lsVerifyCode=edCode.getText().toString().trim();

                if (!lsUserId.equals("") || !lsPassword.equals("")||lsVerifyCode.equals("")) {
                    register(lsUserId,lsPassword,lsVerifyCode);
                } else {
                    Toast.makeText(mContext, "null", Toast.LENGTH_SHORT);
                    return;
                }

                break;

            case R.id.text_register_back:
                finish();
                break;


        }
    }

    private void register(String id, String pw, String code) {
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("userId", id);
            reqJo.put("password", pw);
            reqJo.put("verifyCode", code);
            reqJo.put("userName", "");
            reqJo.put("sex", "");
            reqJo.put("birthday", "");
            reqJo.put("icon", "");
            reqJo.put("province", "");
            reqJo.put("city", "");
        } catch (JSONException e) {
            toastMessage(mContext, e.getMessage());
        }

        NetUtil.post_json(mContext, Action.REGISTER,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {

                    }
                });
    }


    private void getCode(String userId) {

        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("userId", userId);

        } catch (JSONException e) {
            toastMessage(mContext, e.getMessage());
        }

        NetUtil.post_json(mContext, Action.VERIFY_CODE,
                setEntity(reqJo), new NetUtil.Request_call_back() {

                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));

                            if(success){

                                toastMessage(mContext,"验证码发送成功");
                            }else {
                                toastMessage(mContext, "验证码发送失败!");
                            }


                        }catch (Exception e){
                            toastMessage(mContext, e.getMessage());
                        }

                    }
                });
    }
}

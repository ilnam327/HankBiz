package com.hank.rs.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sunflower.conf.Define;
import com.sunflower.conf.Variables;
import com.sunflower.network.NetUtil;
import com.sunflower.utils.AndroidUtils;
import com.hank.R;
import com.hank.base.BaseActivity;
import com.sunflower.conf.Action;
import com.hank.rs.register.RegisterActivity;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SKCC on 2015/11/4.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private Button btnLogin;
    private Button btnRegister;
    private EditText edUsername;
    private EditText edPassword;
    private TextView tvForget;
    private TextView mTextBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mContext=this;
        initView();

       // edUsername.setText("xurinan@skccchina.com");
    }


    private void initView() {
        btnLogin= (Button) findViewById(R.id.btn_login);
        btnRegister= (Button) findViewById(R.id.btn_register);
        edUsername= (EditText) findViewById(R.id.et_username);
        edPassword= (EditText) findViewById(R.id.et_password);
        tvForget= (TextView) findViewById(R.id.tv_forget);
        mTextBack= (TextView) findViewById(R.id.text_login_back);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        mTextBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int resId=v.getId();
        switch (resId){
            case R.id.btn_login:
                String sPassword=edPassword.getText().toString().trim();
                String sUserId=edUsername.getText().toString().trim();

                if(sPassword==null||sPassword.equals("")){
                    Toast.makeText(mContext,"密码不能空！",Toast.LENGTH_SHORT);
                    return;
                }

                if(sUserId==null || sUserId.equals("")){
                    Toast.makeText(mContext,"用户名不能空！",Toast.LENGTH_SHORT);
                    return;
                }
                login(sUserId,sPassword);
                break;

            case R.id.btn_register:
                Intent intent=new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tv_forget:

                break;

            case R.id.text_login_back:
                finish();
                break;

        }

    }


    private void login(final String id, String password){
        JSONObject reqJo = new JSONObject();
        try {
            reqJo.put("userId", id);
            reqJo.put("password",  password);
            reqJo.put("os", "android");
            reqJo.put("imei", AndroidUtils.getSharedPreferences(mContext,
                    Define.STRING,Define.IMEI));
            reqJo.put("iosToken", "1");
            reqJo.put("baiduUserID", "baidu");
            reqJo.put("baiduChannelId", "baidu");

        }catch (JSONException e){

        }
        NetUtil.post_json(mContext, Action.LOGIN,
                setEntity(reqJo), new NetUtil.Request_call_back() {
                    @Override
                    public void request_back_result(JSONObject resJsonObj) {
                        try {
                            Boolean success = Boolean.parseBoolean
                                    (resJsonObj.getString("success"));

                            if (success) {
                                Intent intent = new Intent();
                                intent.putExtra("userId", resJsonObj.getString("userId"));
                                intent.putExtra("userIcon", resJsonObj.getString("icon"));
                                intent.putExtra("userName", resJsonObj.getString("userName"));

                                setResult(Variables.RESULT_OK, intent);
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

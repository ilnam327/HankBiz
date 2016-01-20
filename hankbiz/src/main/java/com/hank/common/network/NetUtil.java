package com.hank.common.network;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sunflower.conf.Define;
import com.sunflower.model.ReqData;
import com.sunflower.model.ResResult;
import com.sunflower.network.NetRequest;
import com.sunflower.utils.JsonUtils;
import com.sunflower.utils.LocalUtils;

import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;


/**
 * Created by Thinkpad on 2015/12/23.
 */
public class NetUtil {

    public final static String TAG="NetUtil";

    //public final static String IP="http://192.120.233.102";
    private final static String IP="http://192.168.1.113";
    private final static String PORT="8086";
    public final static String BIZ_SERVER_URL=IP+":"+PORT+"/lstBiz/service";

    private final static String PATH="http://61.161.213.55:8080";
    public final static String SK_SERVER_URL=PATH + "/appServer/";
    public final static String SK_SERVER_IMG=PATH + "/icon/";
    public final static String SK_UPLOAD_FILE=PATH + "/appServer/upload.do";

    public static  final String CONTENT_TYPE="application/json;charset=UTF-8";

    public interface RequestCallBackJson {
        //请求返回的结果
        //public void request_back_result(RequestResultModel model);
        void requestBackResult(JSONObject resJsonObj);
    }

    public interface ReqBizCallBack{
        void reqBizResult(ResResult resResult);
    }

    /**
     * 防止工具类被实例化
     */
    public void NetUtil() {
        throw new AssertionError("不能实例化对象，数据请求NetUtil");
    }


    public static void  postBizServer(final Context context,String actionId,String serviceId,
        Object req,final ReqBizCallBack reqBizCallBack){
        NetRequest netRequest = NetRequest.shareNetRequestWithRequest(context);
        netRequest.post_json(BIZ_SERVER_URL,setBizHeaders(context),
                setBizReq(actionId, serviceId, req),
                CONTENT_TYPE, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        ResResult resResult= JsonUtils.getInstance().
                                fromJson(response.toString(), ResResult.class);
                        reqBizCallBack.reqBizResult(resResult);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                }
        );
    }


    public static  void postWithJson(final Context context, String urlStr
               , JSONObject req,final RequestCallBackJson requestCallBackJson)  {
        NetRequest netRequest = NetRequest.shareNetRequestWithRequest(context);
        netRequest.post_json(SK_SERVER_URL + urlStr, setHeaders(context, urlStr),setJsonToEntity(req),
                CONTENT_TYPE, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (statusCode == 200) {
                            requestCallBackJson.requestBackResult(response);
                        } else {

                        }
                        Log.i(TAG, "statusCode=" + String.valueOf(statusCode));
                        printHeaders(headers, "Response");
                        Log.i(TAG, format(response.toString()));

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(context,responseString.toString(),Toast.LENGTH_LONG).show();
                        Log.i(TAG, "statusCode=" + String.valueOf(statusCode));
                        Log.i(TAG, responseString);
                        printHeaders(headers, "response");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.i(TAG, throwable.getMessage());
                        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    public static  void post_json(final Context context, String urlStr
            , final HttpEntity httpEntity,final RequestCallBackJson requestCallBackJson)  {
        NetRequest netRequest = NetRequest.shareNetRequestWithRequest(context);
        netRequest.post_json(SK_SERVER_URL + urlStr, setHeaders(context, urlStr), httpEntity,
                CONTENT_TYPE, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (statusCode == 200) {
                            requestCallBackJson.requestBackResult(response);
                        } else {

                        }
                        Log.i(TAG, "statusCode=" + String.valueOf(statusCode));
                        printHeaders(headers, "Response");
                        Log.i(TAG, format(response.toString()));

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(context,responseString.toString(),Toast.LENGTH_LONG).show();
                        Log.i(TAG, "statusCode=" + String.valueOf(statusCode));
                        Log.i(TAG, responseString);
                        printHeaders(headers, "Response");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.i(TAG, throwable.getMessage());
                        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    public  static HttpEntity setBizReq(String  actionId, String serviceId,Object obj){
        HttpEntity httpEntity=null;
        ReqData reqData=new ReqData();
        reqData.setActionId(actionId);
        reqData.setServiceId(serviceId);
        reqData.setChannelId("mobile");

        if(obj instanceof Map){
            reqData.setParams((Map<String, Object>) obj);
        }else  if(obj instanceof  String ){
            reqData.setBizData(obj);
        }

        String entity= JsonUtils.getInstance().toJson(reqData);
        Log.i("Request JSON===>>>", format(entity.toString()));
        httpEntity= new StringEntity(entity, "UTF-8");

        return httpEntity;
    }


    public static Header[] setBizHeaders(Context context ){
        String userId= LocalUtils.getSharedPreferences(context,
                Define.STRING, Define.USER_ID);
        String imei= LocalUtils.getSharedPreferences(context,
                Define.STRING,Define.IMEI);
        Header[] headers=new Header[3];
        headers[0] = new BasicHeader("userId",userId);
        headers[1] = new BasicHeader("os","Android");
        headers[2] = new BasicHeader("imei",imei);
        printHeaders(headers, "request");
        return headers;

    }

    public static Header[] setHeaders(Context context,String serviceId){
//        String userId= LocalUtils.getSharedPreferences(context,
//                Define.STRING, Define.USER_ID);
//        String device= LocalUtils.getSharedPreferences(context,
//                Define.STRING, Define.DEVICE);
//        String imei= LocalUtils.getSharedPreferences(context,
//                Define.STRING,Define.IMEI);
        Header[] headers=new Header[5];
        headers[0] = new BasicHeader("serviceId",serviceId);
        headers[1] = new BasicHeader("appId","Android");
        headers[2] = new BasicHeader("userId","");
        headers[3] = new BasicHeader("device","12334");
        headers[4] = new BasicHeader("imei","1234");
        printHeaders(headers, "request");
        return headers;

    }

    public  static HttpEntity setJsonToEntity(JSONObject jo){
        String params="";
        if(jo!=null){
            params=jo.toString();
            Log.i("Request json===>>>", format(jo.toString()));
        }
        HttpEntity httpEntity= new StringEntity(params, "UTF-8");

        return httpEntity;
    }

    private static void printHeaders( Header[] headers,String val){
        StringBuffer sb=new StringBuffer();
        for(int i=0; i<headers.length; i++){
            sb.append("[" +headers[i].getName()+":"+ headers[i].getValue()+"]\n");
        }
        Log.i(TAG, val + " Headers===>> \n" + sb.toString());
    }

    public static String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for(int i=0;i<jsonStr.length();i++){
            char c = jsonStr.charAt(i);
            if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c+"\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c+"\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();

    }



    private static String getLevelStr(int level){
        StringBuffer levelStr = new StringBuffer();
        for(int levelI = 0;levelI<level ; levelI++){
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    public static String getIMEI(Context context){
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        return  imei;
    }


}

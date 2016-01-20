package com.sunflower.network;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.sunflower.utils.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.hank.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sunflower.conf.*;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;


/**
 * Created by Thinkpad on 2015/12/23.
 */
public class NetUtil {

    //public final static String IP="http://mo.skccchina.com";
    public final static String IP="http://101.200.149.160";

    public final static String PORT="8080";

    public final static String SERVER_URL=IP+":"+PORT+"/appServer/";
    public final static String SERVER_IMG=IP+":"+PORT+"/zhongshuoIcon/";
    public final static String UPLOAD_FILE=IP+":"+PORT+"/appServer/upload.do";

    //public final static String UPLOAD_FILE="http://192.168.1.113:8086/lstBiz/fileUpload";
    // public final static String UPLOAD_FILE="http://192.120.233.101:8086/lstBiz/fileUpload";

    private static final int TIMEOUT = 10000;
    private static final int TIMEOUT_SOCKET = 15000;

    public static final String CTWAP = "ctwap";
    public static final String CMWAP = "cmwap";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    /** @Fields TYPE_NET_WORK_DISABLED : 网络不可用 */
    public static final int TYPE_NET_WORK_DISABLED = 0;
    /** @Fields TYPE_CM_CU_WAP : 移动联通wap10.0.0.172 */
    public static final int TYPE_CM_CU_WAP = 4;
    /** @Fields TYPE_CT_WAP : 电信wap 10.0.0.200 */
    public static final int TYPE_CT_WAP = 5;
    /** @Fields TYPE_OTHER_NET : 电信,移动,联通,wifi 等net网络 */
    public static final int TYPE_OTHER_NET = 6;
    public final static String TAG="NetUtil";
    public static Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");


    public static  final String CONTENT_TYPE="application/json;charset=UTF-8";

    public interface Request_call_back {

        //请求返回的结果
        //public void request_back_result(RequestResultModel model);
        void request_back_result(JSONObject resJsonObj);
    }

    /**
     * 防止工具类被实例化
     */
    public void NetUtil() {
        throw new AssertionError("不能实例化对象，数据请求NetUtil");
    }

    /*
   * 图片加载
   * */
    public static ImageLoader.ImageContainer loafImage(Context content, String imageUrl,
        ImageView imageV, final int defaultImageResId, final int errorImageResId) {

        NetRequest netRequest = NetRequest.sharedNetRequestWithImage(content);
        return netRequest.getImageLoader().get(imageUrl,
                ImageLoader.getImageListener(imageV, defaultImageResId, errorImageResId));
    }




    public static void displayImage(Context context, ImageView imageV,String imageName){
        if(checkNetworkType(context)==TYPE_NET_WORK_DISABLED){
            Toast.makeText(context,"网络不可用！",Toast.LENGTH_LONG).show();
            return;
        }

//        ImageView imageView = (ImageView)this.findViewById(R.id.image_view);
        RequestQueue mQueue = Volley.newRequestQueue(context);
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(
                imageV,R.mipmap.default_icon, R.mipmap.default_icon);
        imageLoader.get(SERVER_IMG+imageName, listener);


        //指定图片允许的最大宽度和高度
        //imageLoader.get("http://developer.android.com/images/home/aw_dac.png",listener, 200, 200);
//        NetRequest netRequest = NetRequest.sharedNetRequestWithImage(content);
//        netRequest.getImageLoader().get("http://developer.android.com/images/home/aw_dac.png", listener);

    }

    public static void  upload_file(final Context context,Bitmap bitmap,
                                    String iconName, final Request_call_back call_back) throws Exception {
        NetRequest netRequest = NetRequest.shareNetRequestWithRequest(context);

        ImageUtils.saveImage(bitmap, iconName);
        String path=ImageUtils.getImagePath(iconName);
        final File file = new File(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        baos.close();
        byte[] buffer = baos.toByteArray();
        Log.i("图片的大小：",String.valueOf(buffer.length));

        if (file.exists() && file.length() > 0) {
            RequestParams params = new RequestParams();
            params.put("upload_file", file);
            netRequest.post_request_async(UPLOAD_FILE,params,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            if(statusCode==200){
                                call_back.request_back_result(response);
                                file.delete();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);

                        }

                        @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                        int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                        // 上传进度显示
                        Log.e("上传 Progress>>>>>", bytesWritten + " / " + totalSize);
                    }
            });
        }

    }


    public static  void post_json(final Context context, String urlStr
               , final HttpEntity httpEntity,final Request_call_back request_call_back)  {
        if(checkNetworkType(context)==TYPE_NET_WORK_DISABLED){
            Toast.makeText(context,"网络不可用！",Toast.LENGTH_LONG).show();
            return;
        }

        NetRequest netRequest = NetRequest.shareNetRequestWithRequest(context);
        netRequest.post_json(SERVER_URL + urlStr, setHeaders(context, urlStr), httpEntity,
                CONTENT_TYPE, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (statusCode == 200) {
                            request_call_back.request_back_result(response);
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

    public static Header[] setHeaders(Context context,String serviceId){
        String userId= AndroidUtils.getSharedPreferences(context,
                Define.STRING, Define.USER_ID);
        String device= AndroidUtils.getSharedPreferences(context,
                Define.STRING, Define.DEVICE);
        String imei= AndroidUtils.getSharedPreferences(context,
                Define.STRING,Define.IMEI);

        Header[] headers=
                new Header[5];
        headers[0] = new BasicHeader("serviceId",serviceId);
        headers[1] = new BasicHeader("appId","Android");
        headers[2] = new BasicHeader("userId",userId);
        headers[3] = new BasicHeader("device",device);
        headers[4] = new BasicHeader("imei",imei);
        printHeaders(headers, "Request");
        return headers;

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

    /***
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     *
     * */
    public static int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                Log.i("", "=====================>无网络");
                return TYPE_NET_WORK_DISABLED;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = networkInfo.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    Log.i("", "=====================>wifi网络");
                    return TYPE_OTHER_NET;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！
                    final Cursor c = mContext.getContentResolver().query(
                            PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c
                                .getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user)) {
                            Log.i(
                                    "",
                                    "=====================>代理："
                                            + c.getString(c
                                            .getColumnIndex("proxy")));
                            if (user.startsWith(CTWAP)) {
                                Log.i("", "=====================>电信wap网络");
                                return TYPE_CT_WAP;
                            }
                        }
                    }
                    c.close();

                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断
                    String netMode = networkInfo.getExtraInfo();
                    Log.i("", "netMode ================== " + netMode);
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase();
                        if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
                                || netMode.equals(UNIWAP)) {
                            Log.i("", "=====================>移动联通wap网络");
                            return TYPE_CM_CU_WAP;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER_NET;
        }
        return TYPE_OTHER_NET;
    }
}

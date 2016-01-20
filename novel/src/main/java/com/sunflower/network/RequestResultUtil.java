package com.sunflower.network;


import org.json.JSONObject;

/**
 * Created by Thinkpad on 2015/12/23.
 */
public class RequestResultUtil {
    public RequestResultUtil(){
        throw new AssertionError("不能实例化对象");
    }
    public static RequestResultModel request_handle_result(JSONObject response, String reqeuestTag,int statusCode) {
//        if (reqeuestTag.equals(DataRequestMacro.kTagHomePageRoll)){
//            return homepageRollModel(response,reqeuestTag,statusCode);
//        }
        return null;
    }
    /*
    * 首页轮播数据model处理
    * */
//    private static RequestResultModel homepageRollModel(JSONObject response, String requestTag,int statusCode){
//        RequestResultModel model = new RequestResultModel();
//        model.requestTag = requestTag;
//        model.resultCode = statusCode;
//        if (statusCode == DataRequestMacro.kResultSucceess){
//            JSONObject resMap = response;
//            JSONArray srollList = null;
//            JSONObject statisticsMap = null;
//            String stutas = null;
//            HomepageGross gross = new HomepageGross();
//            ArrayList list = new ArrayList();
//            HashMap map = new HashMap();
//            try {
//                srollList = (JSONArray)resMap.get("scroll");
//                statisticsMap = (JSONObject)resMap.get("statistics");
//                stutas = (String)resMap.get("status");
//                gross.clinic = (String)statisticsMap.get("clinic");
//                gross.doctor = (String)statisticsMap.get("doctor");
//                gross.groupon = (String)statisticsMap.get("groupon");
//                gross.goods = (String)statisticsMap.get("goods");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            for (int i=0;i<srollList.length();i++){
//                JSONObject scrollMap = null;
//                HomepageRollAndData rollAndData = new HomepageRollAndData();
//                try {
//                    scrollMap = (JSONObject)srollList.get(i);
//                    rollAndData.src = (String)scrollMap.get("src");
//                    rollAndData.href = (String)scrollMap.get("href");
//                }catch (JSONException e){
//                }
//                list.add(rollAndData);
//            }
//            map.put("status", stutas);
//            map.put("scroll", list);
//            map.put("statistics",gross);
//            model.data = map;
//        }
//        return model;
//    }
    /*
    * 有页面社区
    * */
    public static RequestResultModel homepageComModel(JSONObject response, String requestTag,int statusCode){
        return null;
    }
}

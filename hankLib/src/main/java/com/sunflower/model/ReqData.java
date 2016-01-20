package com.sunflower.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Thinkpad on 2016/1/11.
 */
public class ReqData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String actionId;
    /**
     * 用于定义服务ID, 在后台判断调用某个Action
     */
    private String serviceId;
    /**
     * 渠道号
     */
    private String channelId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Object getBizData() {
        return bizData;
    }

    public void setBizData(Object bizData) {
        this.bizData = bizData;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    // 后台 java 对应 json 对象
    private Object bizData;

    // 传 条件检索，删除主键参数
    private Map<String, Object> params =
            new LinkedHashMap<String, Object>();

}

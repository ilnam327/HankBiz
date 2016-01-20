package com.sunflower.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thinkpad on 2016/1/11.
 */
public class ResResult implements Serializable {

    private int statusCode;
    private String errorCode;
    private String errorText;
    private Object result;
    private Map<String, Object> parameters =
            new HashMap<String, Object>();


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }




}

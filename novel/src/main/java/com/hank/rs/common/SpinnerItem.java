package com.hank.rs.common;

/**
 * Created by SKCC on 2015/9/17.
 */
public class SpinnerItem {

    private String value;
    private String key;

    public SpinnerItem(String key,String value){
        this.value=value;
        this.key=key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

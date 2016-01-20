package com.hank.rs.personal.dto;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2015/12/15.
 */
public class Book implements Serializable {

    private String booksId;
    private String booksTitle;
    private String booksProfile;
    private String createDate;
    private String endFlag;
    private String praiseNum;

    public String getBooksId() {
        return booksId;
    }

    public void setBooksId(String booksId) {
        this.booksId = booksId;
    }

    public String getBooksTitle() {
        return booksTitle;
    }

    public void setBooksTitle(String booksTitle) {
        this.booksTitle = booksTitle;
    }

    public String getBooksProfile() {
        return booksProfile;
    }

    public void setBooksProfile(String booksProfile) {
        this.booksProfile = booksProfile;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }






}

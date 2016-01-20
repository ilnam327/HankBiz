package com.hank.ui.listview.dto;

import java.io.Serializable;

/**
 * Created by SKCC on 2015/10/21.
 */
public class Book implements Serializable {


    public String getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(String chapterNum) {
        this.chapterNum = chapterNum;
    }

    private String chapterNum;
    private String icon;
    private String booksId;
    private String booksProfile;
    private String booksTitle;
    private String createDate;
    private String creatorId;
    private String creatorName;
    private String endFlag;
    private String praiseNum;

    public String getCollectionFlag() {
        return collectionFlag;
    }

    public void setCollectionFlag(String collectionFlag) {
        this.collectionFlag = collectionFlag;
    }

    private String collectionFlag;

    public String getPraiseFlag() {
        return praiseFlag;
    }

    public void setPraiseFlag(String praiseFlag) {
        this.praiseFlag = praiseFlag;
    }

    private String praiseFlag;

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    private String clickNum;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getBooksId() {
        return booksId;
    }

    public void setBooksId(String booksId) {
        this.booksId = booksId;
    }

    public String getBooksProfile() {
        return booksProfile;
    }

    public void setBooksProfile(String booksProfile) {
        this.booksProfile = booksProfile;
    }

    public String getBooksTitle() {
        return booksTitle;
    }

    public void setBooksTitle(String booksTitle) {
        this.booksTitle = booksTitle;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

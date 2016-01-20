package com.hank.rs.personal.dto;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2015/12/23.
 */
public class DraftChapter implements Serializable {


    private String userId;
    private String booksId;
    private String draftId;
    private String draftTitle;
    private String content;

    public String getChapterno() {
        return chapterno;
    }

    public void setChapterno(String chapterno) {
        this.chapterno = chapterno;
    }

    private String chapterno;
    private String createDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBooksId() {
        return booksId;
    }

    public void setBooksId(String booksId) {
        this.booksId = booksId;
    }

    public String getDraftId() {
        return draftId;
    }

    public void setDraftId(String draftId) {
        this.draftId = draftId;
    }

    public String getDraftTitle() {
        return draftTitle;
    }

    public void setDraftTitle(String draftTitle) {
        this.draftTitle = draftTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }



}

package com.hank.rs.personal.dto;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2015/12/23.
 */
public class DraftBook  implements Serializable {


    private String userId;
    private String booksId;

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

    public String getDraftTitle() {
        return draftTitle;
    }

    public void setDraftTitle(String draftTitle) {
        this.draftTitle = draftTitle;
    }

    public String getDraftProfile() {
        return draftProfile;
    }

    public void setDraftProfile(String draftProfile) {
        this.draftProfile = draftProfile;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    private String draftTitle;
    private String draftProfile;
    private String createDate;

}

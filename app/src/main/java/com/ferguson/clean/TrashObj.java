package com.ferguson.clean;


import java.io.Serializable;

public class TrashObj implements Serializable {

    private String trashId;
    private String longLocation;
    private String latLocation;
    private long timeStamp;
    private String imgUrl;
    private String userId;
    private String comment;

    public TrashObj(){}


    public TrashObj(String trashId, String longLocation, String latLocation, long timeStamp, String imgUrl, String userId, String comment) {
        this.trashId = trashId;
        this.longLocation = longLocation;
        this.latLocation = latLocation;
        this.timeStamp = timeStamp;
        this.imgUrl = imgUrl;
        this.userId = userId;
        this.comment = comment;
    }

    public String getTrashId() {
        return trashId;
    }

    public void setTrashId(String trashId) {
        this.trashId = trashId;
    }

    public String getLongLocation() {
        return longLocation;
    }

    public void setLongLocation(String longLocation) {
        this.longLocation = longLocation;
    }

    public String getLatLocation() {
        return latLocation;
    }

    public void setLatLocation(String latLocation) {
        this.latLocation = latLocation;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

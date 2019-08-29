package com.ferguson.clean.Objects;


import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class TrashObj implements Serializable {

    private String trash_id;
    private @ServerTimestamp Date time_stamp;
    private String img_url;
    private String user_id;
    private String comment;
    private String geo_point_id;
    private GeoPoint gl;
    private String place_name;

    public TrashObj(){}


    public TrashObj(String trash_id, String img_url, String user_id, String comment, String geo_point_id, GeoPoint gl, String place_name) {
        this.trash_id = trash_id;
        this.time_stamp = time_stamp;
        this.img_url = img_url;
        this.user_id = user_id;
        this.comment = comment;
        this.geo_point_id = geo_point_id;
        this.gl = gl;
        this.place_name = place_name;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public GeoPoint getGl() {
        return gl;
    }

    public void setGl(GeoPoint gl) {
        this.gl = gl;
    }

    public String getGeo_point_id() {
        return geo_point_id;
    }

    public void setGeo_point_id(String geo_point_id) {
        this.geo_point_id = geo_point_id;
    }

    public String getTrash_id() {
        return trash_id;
    }

    public void setTrash_id(String trash_id) {
        this.trash_id = trash_id;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

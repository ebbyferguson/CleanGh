package com.ferguson.clean.objects;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

@IgnoreExtraProperties
public class GeoObj implements Serializable {
    GeoPoint gl;

    public GeoObj(){}

    public GeoObj(GeoPoint gl) {
        this.gl = gl;
    }

    public GeoPoint getGl() {
        return gl;
    }

    public void setGl(GeoPoint gl) {
        this.gl = gl;
    }
}

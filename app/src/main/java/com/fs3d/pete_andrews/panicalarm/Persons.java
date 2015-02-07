package com.fs3d.pete_andrews.panicalarm;

import java.io.Serializable;

/**
 * Created by peteb_000 on 07/02/2015.
 */
public class Persons implements Serializable {
    public static final String TAG = "Persons";
    private static final long sVerUID = -435786512349876L;

    private long mId;
    private String mConId;
    private String mDispName;
    private String mPhotoId;

    public Persons() {
        // Empty Constructor
    }

    public Persons(String conId, String dispName, String photoId) {
        this.mConId = conId;
        this.mDispName = dispName;
        this.mPhotoId = photoId;
    }


    // All of the Getters and Setters are below.
    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getContactId() {
        return mConId;
    }

    public void setContactId(String mConId) {
        this.mConId = mConId;
    }

    public String getName() {
        return mDispName;
    }

    public void setName(String mDispName) {
        this.mDispName = mDispName;
    }

    public String getPhotoId() {
        return mPhotoId;
    }

    public void setPhotoId(String mPhotoId) {
        this.mPhotoId = mPhotoId;
    }
}

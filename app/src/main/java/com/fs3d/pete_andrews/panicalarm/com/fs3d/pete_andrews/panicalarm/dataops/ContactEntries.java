package com.fs3d.pete_andrews.panicalarm.com.fs3d.pete_andrews.panicalarm.dataops;

import java.io.Serializable;

/**
 * Created by peteb_000 on 07/02/2015.
 */
public class ContactEntries implements Serializable {
    public static final String TAG = "ContactEntries";
    private static final long sVerUID = -243578651234876L;

    private long mId;
    private String mConId;
    private int mEnabled;
    private String mDataVal;
    private String mDataType;
    private String mDataLabel;
    private Persons mContact;

    public ContactEntries() {
        // Empty constructor.
    }

    public ContactEntries(String conId, int enabled, String dataVal, String dataType, String dataLabel) {
        this.mConId = conId;
        this.mEnabled = enabled;
        this.mDataVal = dataVal;
        this.mDataType = dataType;
        this.mDataLabel = dataLabel;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public int getEnabled() {
        return mEnabled;
    }

    public void setEnabled(int enabled) {
        this.mEnabled = enabled;
    }

    public String getConId() {
        return mConId;
    }

    public void setConId(String mConId) {
        this.mConId = mConId;
    }

    public String getData() {
        return mDataVal;
    }

    public void setData(String mDataVal) {
        this.mDataVal = mDataVal;
    }

    public String getType() {
        return mDataType;
    }

    public void setType(String mDataType) {
        this.mDataType = mDataType;
    }

    public String getLabel() {
        return mDataLabel;
    }

    public void setLabel(String mDataLabel) {
        this.mDataLabel = mDataLabel;
    }

    public Persons getContact() {
        return mContact;
    }

    public void setContact(Persons mContact) {
        this.mContact = mContact;
    }

}

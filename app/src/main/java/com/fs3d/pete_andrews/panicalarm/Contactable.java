package com.fs3d.pete_andrews.panicalarm;

/**
 * Created by FS3D on 07/04/2015.
 */
public class Contactable {

    private int uid;
    private String data_value;
    private String data_label;
    private int data_type;
    private int data_categ;
    private boolean data_switch;

    public Contactable() {
        this.uid = 0;
        this.data_value = "empty";
        this.data_type = 0;
        this.data_label = "empty";
        this.data_categ = 0;
        this.data_switch = false;
    }

    public Contactable(int uid, int datacat, int datatyp, String datalbl, String dataVal, boolean toggle) {
        this.uid = uid;
        this.data_categ = datacat;
        this.data_type = datatyp;
        this.data_label = datalbl;
        this.data_value = dataVal;
        this.data_switch = toggle;
    }
}

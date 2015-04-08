package com.fs3d.pete_andrews.panicalarm;

import java.util.ArrayList;

/**
 * Created by FS3D on 07/04/2015.
 */
public class Contact {
    private String display_name;
    private int uid;
    private ArrayList<Contactable> cdata;

    public Contact(int uid, String display_name) {
        this.uid = uid;
        this.display_name = display_name;
        this.cdata = new ArrayList<>();
    }

    public boolean addEntry(int datacat, int datatyp, String datalbl, String dataval, boolean togl) {
        cdata.add(new Contactable(uid, datacat, datatyp, datalbl, dataval, togl));
        return true;
    }
}

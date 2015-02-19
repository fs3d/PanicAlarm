package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;

import java.util.ArrayList;

/**
 * Created by peteb_000 on 18/02/2015.
 */
public class dataManager {
    private static final String TAG = "dataManager";
    protected Context ctxt;
    private String conName, conId;
    private Cursor crsr;

    public dataManager(Context ctxt, String conId, String conName) {
        this.conName = conName;
        this.conId = conId;
        this.ctxt = ctxt.getApplicationContext();
    }

    public void connectDatabase() {
        // Open connection to database
    }

    public void closeDatabase() {
        // Close and exit database
    }

    public void modifyData(String dataType, String dataValue, boolean activated) {
        // Access entry ands modify its' content (currently this only switches modes)
    }

    public void addData(String dataType, String dataValue) {
        // Append a new entry to the database
    }

    public String[][] pullData(int mode) {
        // Retrieve Contact Manager data from Android's built-in Contact Database
        // Private variables.
        ArrayList<String[]> tempData = new ArrayList<String[]>();
        int cv, ct, cl, recCount;
        String ci_v, ci_t, ci_l, sel, dV, dT, dL;
        String[] selArgs;
        String[][] composite;
        Uri conUri;
        /*  The following switch statements set up the variables for retrieval.
         * integer switches are: 1=Phone, 2=Email, 3=IM
         * */
        switch (mode) {
            case 1:
                // Phone retrieval mode.
                ci_t = CommonDataKinds.Phone.TYPE;
                ci_l = CommonDataKinds.Phone.LABEL;
                ci_v = CommonDataKinds.Phone.NORMALIZED_NUMBER;
                conUri = CommonDataKinds.Phone.CONTENT_URI;
                sel = CommonDataKinds.Phone.CONTACT_ID + " = ?";
                selArgs = new String[]{conId};
                break;
            case 2:
                // Email retrieval mode.
                ci_t = CommonDataKinds.Email.TYPE;
                ci_l = CommonDataKinds.Email.LABEL;
                ci_v = CommonDataKinds.Email.ADDRESS;
                conUri = CommonDataKinds.Email.CONTENT_URI;
                sel = CommonDataKinds.Email.CONTACT_ID + " = ?";
                selArgs = new String[]{conId};
                break;
            case 3:
                // IM retrieval mode.
                ci_t = CommonDataKinds.Im.PROTOCOL;
                ci_l = CommonDataKinds.Im.CUSTOM_PROTOCOL;
                ci_v = CommonDataKinds.Im.DATA;
                conUri = Data.CONTENT_URI;
                sel = Data.CONTACT_ID + " = ? AND " + Data.MIMETYPE + " = ?";
                selArgs = new String[]{conId, CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                break;
            default:
                // This should never be called.
                ci_l = null;
                ci_t = null;
                ci_v = null;
                conUri = null;
                sel = null;
                selArgs = null;
                break;
        }
        // Iterate over data.
        if (sel != null) {
            crsr = ctxt.getContentResolver().query(conUri, null, sel, selArgs, null);
            cl = crsr.getColumnIndex(ci_l);
            ct = crsr.getColumnIndex(ci_t);
            cv = crsr.getColumnIndex(ci_v);
            recCount = crsr.getCount();
            composite = new String[recCount][2];
            int ctInt;
            if (crsr.moveToFirst()) {
                for (int i = 0; i < recCount; i++) {
                    // From Contact Database
                    ctInt = crsr.getInt(ct);
                    dT = getDataType(ctInt);
                    dV = crsr.getString(cv);
                    // To String array
                    composite[i][0] = dT;
                    composite[i][1] = dV;
                }
            }
        } else {
            // If sel is null, something went wrong.
        }
        crsr.close();
        crsr = null;
        // Return results
        return new String[][]{
                {conId, conName, "three", "four"},
                {conId, conName, "five", "six!"}};
    }

    public String getDataType(int conType) {

        return "Test";
    }
}

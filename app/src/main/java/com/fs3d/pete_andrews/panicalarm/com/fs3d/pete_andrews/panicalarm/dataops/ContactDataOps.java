package com.fs3d.pete_andrews.panicalarm.com.fs3d.pete_andrews.panicalarm.dataops;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fs3d.pete_andrews.panicalarm.StorageHelper;

/**
 * Created by peteb_000 on 07/02/2015.
 */
public class ContactDataOps {

    private static final String TAG = "ContactDataOps";
    private Context mCtxt;
    private SQLiteDatabase mDBase;
    private StorageHelper mHelper;
    private String[] mColumns = new String[]{StorageHelper.COL_CON_ID, StorageHelper.COL_DATA_VAL, StorageHelper.COL_DATA_TYPE, StorageHelper.COL_DATA_LABEL};

    public ContactDataOps() {
        // Empty Constructor
    }

    public ContactDataOps(Context ctxt) {
        this.mCtxt = ctxt;
        try {
            open();
        } catch (SQLException ee) {
            Log.e(TAG, ee.getMessage());
        }
    }

    public void open() throws SQLException {
        mDBase = mHelper.getWritableDatabase();
    }

    public void close() {
        mHelper.close();
    }

    public ContactEntries addData(String conId, String data, String type, String label) {
        ContentValues values = new ContentValues();
        values.put(StorageHelper.COL_CON_ID, conId);
        values.put(StorageHelper.COL_DATA_VAL, data);
        values.put(StorageHelper.COL_DATA_TYPE, type);
        values.put(StorageHelper.COL_DATA_LABEL, label);
        long insId = mDBase.insert(StorageHelper.TABLE_CONTACT_DATA, null, values);
        Cursor crsr = mDBase.query(StorageHelper.TABLE_CONTACT_DATA, mColumns, StorageHelper.COL_UID + " = " + insId, null, null, null, null);
        crsr.moveToFirst();
        ContactEntries newEntry = locateData(crsr);
        crsr.close();
        return newEntry;
    }

    public ContactEntries locateData(Cursor crsr) {
        ContactEntries record = new ContactEntries();
        record.setId(crsr.getLong(0));
        record.setEnabled(crsr.getInt(2));
        record.setData(crsr.getString(3));
        record.setType(crsr.getString(4));
        record.setLabel(crsr.getString(5));
        long persId = crsr.getLong(1);
        ContactPersonOps dao = new ContactPersonOps(mCtxt);
        Persons person = dao.getContact(persId);
        if (person != null) {
            record.setContact(person);
        }
        return record;
    }
}

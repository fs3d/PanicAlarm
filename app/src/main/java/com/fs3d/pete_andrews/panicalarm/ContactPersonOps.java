package com.fs3d.pete_andrews.panicalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by peteb_000 on 07/02/2015.
 */
public class ContactPersonOps {
    public static final String TAG = "ContactPersonOps";

    private SQLiteDatabase mDB;
    private StorageHelper mDbHelper;
    private Context mCtxt;
    private String[] mColumns = {StorageHelper.COL_UID,
            StorageHelper.COL_CON_ID,
            StorageHelper.COL_DISP_NAME,
            StorageHelper.COL_IMG_URI};

    public ContactPersonOps(Context ctxt) {
        this.mCtxt = ctxt;
        mDbHelper = new StorageHelper(ctxt);
        try {
            open();
        } catch (SQLException ee) {
            Log.e(TAG, "SQLException caught: " + ee.getMessage());
            ee.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDB = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Persons addContact(String conId, String dispName, String photoId) {
        ContentValues values = new ContentValues();
        values.put(StorageHelper.COL_CON_ID, conId);
        values.put(StorageHelper.COL_DISP_NAME, dispName);
        values.put(StorageHelper.COL_IMG_URI, photoId);
        long insertId = mDB.insert(StorageHelper.TABLE_PERSON, null, values);
        Cursor pCurs = mDB.query(StorageHelper.TABLE_PERSON, mColumns, StorageHelper.COL_UID + " = " + insertId, null, null, null, null);
        pCurs.moveToFirst();
        Persons newContact = locateContact(pCurs);
        return newContact;
    }

    public Persons locateContact(Cursor crsr) {
        Persons entry = new Persons();
        entry.setId(crsr.getLong(0));
        entry.setContactId(crsr.getString(1));
        entry.setName(crsr.getString(2));
        entry.setPhotoId(crsr.getString(3));
        return entry;
    }
}

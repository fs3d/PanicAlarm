package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by peteb_000 on 24/02/2015.
 * Intended to provide standardised functionality for creation and maintenance of the internal
 * SQLite table used to keep contact data for the app's functionality.
 */
public class mDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_CONTACTS = "table_contacts";
    public static final String COLUMN_CONTACT_ID = "_id";
    public static final String COLUMN_DISPLAY_NAME = "display_name";
    public static final String COLUMN_DATA_VALUE = "data_value";
    public static final String COLUMN_DATA_TYPE = "data_type";
    public static final String COLUMN_DATA_LABEL = "data_label";
    public static final String COLUMN_ACTIVE = "flag_active";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS
            + "(" + COLUMN_CONTACT_ID + " text not null, "
            + COLUMN_DISPLAY_NAME + " text not null, "
            + COLUMN_DATA_VALUE + " text not null, "
            + COLUMN_DATA_TYPE + " text not null, "
            + COLUMN_DATA_LABEL + " text, "
            + COLUMN_ACTIVE + " text not null);";
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public mDBHelper(Context ctxt) {
        super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Database with execSQL statement
		db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Up/Downgrade database with execSQL statement
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}

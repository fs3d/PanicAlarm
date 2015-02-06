package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by peteb_000 on 29/01/2015.
 */
public class StorageHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EmergencyContacts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String STORE_CONTACT_TABLE = "contact_list";
    private static final String SQL_DELETE_PERSONS =
            "DROP TABLE IF EXISTS " + PersonEntry.TABLE_NAME;
    private static final String SQL_DELETE_CONTACTS =
            "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;

    public StorageHelper(Context ctxt) {
        super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
    }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create the database if it does not exist.
            db.execSQL("CREATE TABLE IF NOT EXISTS " + PersonEntry.TABLE_NAME +
                    "(" + PersonEntry.COLUMN_NAME_UID + "INT PRIMARY KEY," +
                    PersonEntry.COLUMN_NAME_CONTACT_ID + " TEXT," +
                    PersonEntry.COLUMN_DISPLAY_NAME + " TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + ContactEntry.TABLE_NAME +
                    "(" + ContactEntry.COLUMN_NAME_CONTACT_ID + " TEXT," +
                    ContactEntry.COLUMN_DATA_CATEGORY + " TEXT," +
                    ContactEntry.COLUMN_DATA_FIELD + " TEXT)");
        }

    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Currently this method does nothing, although it needs to be declared in the Helper class.
        }

    public void onOpen(SQLiteDatabase db) {
        // More needed here, obviously.
        }

    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contactables";
        public static final String COLUMN_NAME_CONTACT_ID = "contact_id";
        public static final String COLUMN_DATA_CATEGORY = "data_category";
        public static final String COLUMN_DATA_FIELD = "data_entry";
    }

    public static abstract class PersonEntry implements BaseColumns {
        public static final String TABLE_NAME = "persons";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_CONTACT_ID = "contact_id";
        public static final String COLUMN_DISPLAY_NAME = "display_name";
    }
}

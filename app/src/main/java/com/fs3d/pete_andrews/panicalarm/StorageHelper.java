package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by peteb_000 on 29/01/2015.
 */
public class StorageHelper {
    private static final String STORE_CONTACT_TABLE = "contact_list";

    public StorageHelper() {
        // Deliberately empty so that it cannot be accidentally called elsewhere.
    }

    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_CONTACT_ID = "contact_id";
        public static final String COLUMN_DISPLAY_NAME = "display_name";
        public static final String COLUMN_DATA_FIELD = "data";
        public static final String COLUMN_DATA_CATEGORY = "data_category";
    }

    public class StorageDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "EmergencyContacts.db";
        private static final int DATABASE_VERSION = 2;

        public StorageDbHelper(Context ctxt) {
            super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
        }

        private static final String STORE_CONTACT_TABLE_CREATE =
                "CREATE TABLE " + STORE_CONTACT_TABLE + " (" +
                        ContactEntry.COLUMN_NAME_UID + " INTEGER PRIMARY KEY, " +
                        ContactEntry.COLUMN_NAME_CONTACT_ID + " TEXT, " +
                        ContactEntry.COLUMN_DISPLAY_NAME + " TEXT, " +
                        ContactEntry.COLUMN_DATA_FIELD + " TEXT, " +
                        ContactEntry.COLUMN_DATA_CATEGORY + " TEXT);";

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create the database if it does not exist.
            db.execSQL(STORE_CONTACT_TABLE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            // Currently this method does nothing, although it needs to be declared in the Helper class.
        }

        public void onOpen(SQLiteDatabase db) {
            // More needed here, obviously.
        }


    }
}

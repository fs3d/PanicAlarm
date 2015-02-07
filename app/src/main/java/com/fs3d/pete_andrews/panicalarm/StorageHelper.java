package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by peteb_000 on 29/01/2015.
 */
public class StorageHelper extends SQLiteOpenHelper {

    /* The below in this comment describes the intended table structure for this project.
     * Person (table)
        _id INT PRIMARY KEY
        contact_id varchar(10)
        display_name varchar(50)
        contact_image uri (varchar(255))

     * ContactData
        _id INT PRIMARY KEY
        _contact_id varchar(10) FOREIGN KEY
        _enabled boolean
        data_value varchar(255)
        data_type undetermined (may be varchar or int depending on how it is stored in the OS contact database)
        data_label varchar(20)
     **/
    // All of the following public statements control the column entries for the Persons table.
    public static final String TABLE_PERSON = "Person";
    public static final String COL_UID = "_id"; // This one is declared once although it is used by both tables.
    public static final String COL_CON_ID = "con_id"; // Same with this one.
    public static final String COL_DISP_NAME = "display_name";
    public static final String COL_IMG_URI = "contact_image";
    // The following declaration is for creating the SQL Database Persons table.
    private static final String SQL_CREATE_TABLE_PERSONS = "CREATE TABLE " + TABLE_PERSON + "("
            + COL_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CON_ID + " TEXT NOT NULL, "
            + COL_DISP_NAME + " TEXT NOT NULL, "
            + COL_IMG_URI + " TEXT);";
    // All of the following public statements control the column entries for the ContactData table.
    public static final String TABLE_CONTACT_DATA = "ContactData";
    public static final String COL_ENABLED = "_enabled";
    public static final String COL_DATA_VAL = "_value";
    public static final String COL_DATA_TYPE = "_type";
    public static final String COL_DATA_LABEL = "_label";
    // This one is for the ContactData Table.
    private static final String SQL_CREATE_TABLE_CDATA = "CREATE TABLE " + TABLE_CONTACT_DATA + "("
            + COL_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CON_ID + " INTEGER NOT NULL, "
            + COL_ENABLED + " BOOLEAN NOT NULL, "
            + COL_DATA_VAL + " TEXT NOT NULL, "
            + COL_DATA_TYPE + " TEXT NOT NULL, "
            + COL_DATA_LABEL + " TEXT);";
    // The two below lines are internal database declarations to control the file name and version
    // and are not exposed to the rest of the project.
    private static final String DATABASE_NAME = "EContacts.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public StorageHelper(Context ctxt) {
        super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Required methods 1
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database if it does not exist.
        db.execSQL(SQL_CREATE_TABLE_CDATA);
        db.execSQL(SQL_CREATE_TABLE_PERSONS);
    }

    // Required methods 2
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Currently this method does nothing, although it needs to be declared in the Helper class.
        Log.w("StorageHelper", "Crossgrading DB from v." + oldV + " to v." + newV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        // Recreate the tables
        onCreate(db);
    }

    // Optional method currently not used but may be if necessary
    public void onOpen(SQLiteDatabase db) {
        // More needed here, obviously.
        Log.i("StorageHelper", "onOpen called on " + db.toString());
    }
}

package com.fs3d.pete_andrews.panicalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by peteb_000 on 18/02/2015.
 * DataManager class. This is intended to handle transactions to and from the internal database
 * via the mDBHelper class.
 */
public class dataManager {
    private static final String TAG = "dataManager";
    protected Context ctxt;
    private String conName, conId;
    private Cursor crsr;
    private mDBHelper data;
    private SQLiteDatabase db;

    public dataManager(Context ctxt, String conId, String conName) {
        this.conName = conName;
        this.conId = conId;
        this.ctxt = ctxt.getApplicationContext();
    }

    public void connectDatabase() {
        // Open connection to database
        data = new mDBHelper(ctxt);
        db = data.getWritableDatabase();
    }

    public void closeDatabase() {
        // Close and exit database
        if (db.isOpen())
            db.close();
        data.close();
    }

    public boolean checkDataExists(String dataType, String dataValue, String activated) {
        /* Access entry for its' existence.
         * This will return a boolean value to determine if a given record exists. */
        String sel = mDBHelper.COLUMN_CONTACT_ID + " = ? AND " +
                mDBHelper.COLUMN_DISPLAY_NAME + " = ? AND " +
                mDBHelper.COLUMN_DATA_VALUE + " = ?";
        String selArgs[] = {mDBHelper.COLUMN_CONTACT_ID, mDBHelper.COLUMN_DISPLAY_NAME, mDBHelper.COLUMN_DATA_VALUE};
        Cursor crsr = db.query(mDBHelper.TABLE_CONTACTS, null, sel, selArgs, null, null, null);
        if (crsr.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public void modifyData(String dataType, String dataValue, String activated) {
        /* Access entry ands modify its' content.
         * Argument 1 (String) is the type of data in String format from the resource provided by the current locale.
         *            This will contain a string value for the label if it is custom.
         * Argument 2 (String) is the value of the data in normalised or raw form, depending on the type.
         * Argument 3 (boolean) is the switch to determine if the data will be used by the Service.
         *            0 = Dormant. 1 = Active.
         */
        if (!db.isOpen()) {
        } else {
            // Check for existing records matching the above criteria...
            String sel = mDBHelper.COLUMN_CONTACT_ID + " LIKE ? AND " + mDBHelper.COLUMN_DATA_VALUE + " LIKE ? AND "
                    + mDBHelper.COLUMN_DATA_TYPE + " LIKE ? AND " + mDBHelper.COLUMN_ACTIVE + " LIKE ?";
            String[] selArgs = new String[]{conId, dataValue, dataType, String.valueOf(activated)};
            Cursor crsr = db.query(mDBHelper.TABLE_CONTACTS, null, sel, selArgs, null, null, null);
            if (crsr.moveToFirst()) {
                // Exists already. We can proceed.
                ContentValues cv = new ContentValues();
                cv.put(data.COLUMN_CONTACT_ID, conId);
                cv.put(data.COLUMN_DISPLAY_NAME, conName);
                cv.put(data.COLUMN_DATA_VALUE, dataValue);
                cv.put(data.COLUMN_DATA_TYPE, dataType);
                cv.put(data.COLUMN_ACTIVE, activated);
                db.update(mDBHelper.TABLE_CONTACTS, cv, " WHERE " + mDBHelper.COLUMN_CONTACT_ID + " = ? AND " + mDBHelper.COLUMN_DATA_VALUE + " = ?", new String[]{conId, dataValue});
            }
            crsr.close();
        }
    }

    public int addData(String dataType, String dataValue, String activated, boolean override) {
        // Append a new entry to the database
        if (db.isOpen()) {
            if (!db.isReadOnly()) {
                // Can write to database.
                ContentValues cv = new ContentValues();
                cv.put(data.COLUMN_CONTACT_ID, conId);
                cv.put(data.COLUMN_DISPLAY_NAME, conName);
                cv.put(data.COLUMN_DATA_VALUE, dataValue);
                cv.put(data.COLUMN_DATA_TYPE, dataType);
                cv.put(data.COLUMN_ACTIVE, String.valueOf(activated));
                if (checkDataExists(dataType, dataValue, activated)) {
                    // If the boolean result of this call is true, then we need to decide what to do next.
                    if (!override) {
                        // No override. Fail code 1.
                        return 1;
                    } else {
                        // Override. Modify record.
                        modifyData(dataType, dataValue, activated);
                        return 0;
                    }
                }
                db.insert(data.TABLE_CONTACTS, null, cv);
            } else {
                Log.e(TAG, "addData method execution failure: Database is opened as read only.");
                return 2;
            }
        } else {
            Log.e(TAG, "addData method execution failure: Database has not been opened.");
            return 3;
        }
        return 0;
    }

    public String[][] pullData(int mode) {
        // Retrieve Contact Manager data from Android's built-in Contact Database
        // Private variables.
        ArrayList<String[]> tempData = new ArrayList<String[]>();
        int cv, ct, recCount;
        String ci_v, ci_t, sel, dV, dT, swtch;
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
                ci_v = CommonDataKinds.Phone.NORMALIZED_NUMBER;
                conUri = CommonDataKinds.Phone.CONTENT_URI;
                sel = CommonDataKinds.Phone.CONTACT_ID + " = ?";
                selArgs = new String[]{conId};
                swtch = "1";
                break;
            case 2:
                // Email retrieval mode.
                ci_t = CommonDataKinds.Email.TYPE;
                ci_v = CommonDataKinds.Email.ADDRESS;
                conUri = CommonDataKinds.Email.CONTENT_URI;
                sel = CommonDataKinds.Email.CONTACT_ID + " = ?";
                selArgs = new String[]{conId};
                swtch = "1";
                break;
            case 3:
                // IM retrieval mode.
                ci_t = CommonDataKinds.Im.PROTOCOL;
                ci_v = CommonDataKinds.Im.DATA;
                conUri = Data.CONTENT_URI;
                sel = Data.CONTACT_ID + " = ? AND " + Data.MIMETYPE + " = ?";
                selArgs = new String[]{conId, CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                swtch = "0";
                break;
            default:
                // This should never be called.
                ci_t = null;
                ci_v = null;
                conUri = null;
                sel = null;
                selArgs = null;
                swtch = "0";
                break;
        }
        // Iterate over data.
        if (sel != null) { // sel has data. We will try to conduct a search with the value.
            crsr = ctxt.getContentResolver().query(conUri, null, sel, selArgs, null); // Return cursor at Android Contacts DB selection criteria
            ct = crsr.getColumnIndex(ci_t); // Return column index for type or protocol
            cv = crsr.getColumnIndex(ci_v); // Return column index for data value
            recCount = crsr.getCount(); // Return record count (the number of entries for the selected contact ID)
            composite = new String[recCount][5]; // Re-initialise String array for storing of values (using the correct record count).
            int ctInt;
            if (crsr.moveToFirst()) {
                for (int i = 0; i < recCount; i++) {
                    // From Contact Database
                    ctInt = crsr.getInt(ct); // Integer value of Data Type
                    dT = getDataType(ctInt); // Method to convert Integer into a String Label from Localised data (supports International labels)
                    dV = crsr.getString(cv); // String value of Data Value (Phone number, email address, IM username etc)
                    // To String array
                    composite[i][0] = conId;   // Contact ID from Phone._ID
                    composite[i][1] = conName; // Display Name from Phone.DISPLAY_NAME
                    composite[i][2] = dT;      // Localised String literal as passed from getDataType
                    composite[i][3] = dV;      // Data Value as passed above
                    composite[i][4] = swtch;   // Whether active (1) or dormant (0)
                }
            } else {
                composite = new String[][]{{"EMPTY", "", "", "", "0"}};
            }
        } else {
            // If sel is null, something went wrong.
            composite = new String[][]{{"EMPTY", "", "", "", "0"}};
        }
        crsr.close();
        crsr = null;
        // Return results
        return composite;
    }

    public String getDataType(int conType) {
        // Get String Label from integer argument
        String conLabel = "Test";
        return conLabel;
    }
}

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
    private Cursor crsr, ecrsr;
    private mDBHelper data;
    private SQLiteDatabase db;
	private boolean rw = false;
	
	public dataManager(Context ctxt){
		this.ctxt = ctxt.getApplicationContext();
		rw=false;
	}

    public dataManager(Context ctxt, String conId, String conName) {
        this.conName = conName;
        this.conId = conId;
        this.ctxt = ctxt.getApplicationContext();
		rw=true;
    }

    public void connectDatabase() {
        // Open connection to database
        data = new mDBHelper(ctxt);
		if (rw)
        	db = data.getWritableDatabase();
		else
			db = data.getReadableDatabase();
    }

    public void closeDatabase() {
        // Close and exit database
        if (db.isOpen())
            db.close();
        data.close();
    }

    public boolean checkDataExists(String dataType, String dataLabel, String dataValue, String activated) {
        /* Access entry for its' existence.
         * This will return a boolean value to determine if a given record exists. */
        String sel = mDBHelper.COLUMN_CONTACT_ID + " LIKE ? AND " + mDBHelper.COLUMN_DATA_VALUE + " LIKE ? AND "
                + mDBHelper.COLUMN_DATA_TYPE + " LIKE ? AND " + mDBHelper.COLUMN_DATA_LABEL + " LIKE ? AND "
                + mDBHelper.COLUMN_ACTIVE + " LIKE ?";
        String[] selArgs = new String[]{conId, dataValue, dataType, dataLabel, String.valueOf(activated)};
        Cursor crsr = db.query(mDBHelper.TABLE_CONTACTS, null, sel, selArgs, null, null, null);
        if (crsr.moveToFirst()) {
            Log.w(TAG, "Cursor has returned results. This is because the data exists exactly as matched and already exists in the database.");
            return true;
        } else {
            Log.d(TAG, "Cursor is empty. Records not found. This is to be expected if you are trying to add this data and it's not present yet.");
            return false;
        }
    }

    public void modifyData(String dataType, String dataLabel, String dataValue, String activated) {
        /* Access entry ands modify its' content.
         * Argument 1 (String) is the type of data in String format from the resource provided by the current locale.
         *            This will contain a string value for the label if it is custom.
         * Argument 2 (String) is the value of the data in normalised or raw form, depending on the type.
         * Argument 3 (boolean) is the switch to determine if the data will be used by the Service.
         *            0 = Dormant. 1 = Active.
         */
		if (rw){
        if (!db.isOpen()) {
        } else {
            // Check for existing records matching the above criteria...
            String sel = mDBHelper.COLUMN_CONTACT_ID + " LIKE ? AND " + mDBHelper.COLUMN_DATA_VALUE + " LIKE ? AND "
                    + mDBHelper.COLUMN_DATA_TYPE + " LIKE ? AND " + mDBHelper.COLUMN_DATA_LABEL + " LIKE ? AND " + mDBHelper.COLUMN_ACTIVE + " LIKE ?";
            String[] selArgs = new String[]{conId, dataValue, dataType, dataLabel, String.valueOf(activated)};
            Cursor crsr = db.query(mDBHelper.TABLE_CONTACTS, null, sel, selArgs, null, null, null);
            if (crsr.moveToFirst()) {
                // Exists already. We can proceed.
                ContentValues cv = new ContentValues();
                cv.put(data.COLUMN_CONTACT_ID, conId);
                cv.put(data.COLUMN_DISPLAY_NAME, conName);
                cv.put(data.COLUMN_DATA_TYPE, dataType);
                cv.put(data.COLUMN_DATA_LABEL, dataLabel);
                cv.put(data.COLUMN_DATA_VALUE, dataValue);
                cv.put(data.COLUMN_ACTIVE, activated);
                db.update(mDBHelper.TABLE_CONTACTS, cv, mDBHelper.COLUMN_CONTACT_ID + " = ? AND " + mDBHelper.COLUMN_DATA_VALUE + " = ? AND "
                                + mDBHelper.COLUMN_DATA_TYPE + " = ? AND " + mDBHelper.COLUMN_DATA_LABEL + " = ?",
                        new String[]{conId, dataValue, dataType, dataLabel});
            }
            crsr.close();
        }
		}
    }

    public String addData(String dataType, String dataLabel, String dataValue, String activated, boolean override) {
        if ((dataType.equals("_") && dataLabel.equals("_") && dataValue.equals("_")))
            return "INVALID_DATA";
        // Append a new entry to the database
        if (dataValue == null)
            return "INVALID_DATA";
        if (db.isOpen()) {
            if (db.isReadOnly())
                return "READ_ONLY";
            // Can write to database.
            ContentValues cv = new ContentValues();
            cv.put(data.COLUMN_CONTACT_ID, conId);
            cv.put(data.COLUMN_DISPLAY_NAME, conName);
            cv.put(data.COLUMN_DATA_TYPE, dataType);
            cv.put(data.COLUMN_DATA_LABEL, dataLabel);
            cv.put(data.COLUMN_DATA_VALUE, dataValue);
            cv.put(data.COLUMN_ACTIVE, String.valueOf(activated));
            if (checkDataExists(dataType, dataLabel, dataValue, activated)) {
                // If the boolean result of this call is true, then we need to decide what to do next.
                if (!override) {
                    // No override. Fail code 1.
                    return "EXISTS";
                } else {
                    // Override. Modify record.
                    modifyData(dataType, dataLabel, dataValue, activated);
                    return "EXISTS_OK";
                }
            }
            db.insert(data.TABLE_CONTACTS, null, cv);
        } else {
            Log.e(TAG, "addData method execution failure: Database has not been opened.");
            return "NO_DB";
        }
        return "OK";
    }

    public String[][] pullData(int mode) {
        // Retrieve Contact Manager data from Android's built-in Contact Database
        // Private variables
        int cv, ct, recCount;
        String ci_v, ci_t, sel, dV, dT, dL, swtch;
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
                ci_v = CommonDataKinds.Phone.NUMBER;
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
                // This is to be implemented when IM support comes along (ie when I write it)
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
            ecrsr = ctxt.getContentResolver().query(conUri, null, sel, selArgs, null); // Return cursor at Android Contacts DB selection criteria
            ct = ecrsr.getColumnIndex(ci_t); // Return column index for type or protocol
            cv = ecrsr.getColumnIndex(ci_v); // Return column index for data value
            recCount = ecrsr.getCount(); // Return record count (the number of entries for the selected contact ID)
            composite = new String[recCount][6]; // Re-initialise String array for storing of values (using the correct record count).
            int ctInt;
            if (ecrsr.moveToFirst()) {
                for (int i = 0; i < recCount; i++) {
                    // From Contact Database
                    ctInt = ecrsr.getInt(ct); // Integer value of Data Type
                    String[] dtypes = getDataType(ctInt, mode); // Method to convert Integer into a String Label from Localised data (supports International labels)
                    dT = dtypes[0];
                    dL = dtypes[1];
                    if (dT == null) dT = "";
                    if (dL == null) dL = "";
                    dV = ecrsr.getString(cv); // String value of Data Value (Phone number, email address, IM username etc)
                    // To String array
                    composite[i][0] = conId;   // Contact ID from Phone._ID
                    composite[i][1] = conName; // Display Name from Phone.DISPLAY_NAME
                    composite[i][2] = dT;      // Localised String literal as passed from getDataType
                    composite[i][3] = dL;
                    composite[i][4] = dV;      // Data Value as passed above
                    composite[i][5] = swtch;   // Whether active (1) or dormant (0)
                    ecrsr.moveToNext();
                }
            } else {
                composite = new String[][]{{"EMPTY", "", "", "", "0"}};
            }
        } else {
            // If sel is null, something went wrong.
            composite = new String[][]{{"EMPTY", "", "", "", "0"}};
        }
        ecrsr.close();
        ecrsr = null;
        // Return results
        return composite;
    }

    public String[] getDataType(int conType, int mode) {
        // Get String Label from integer argument
        String conLabel ,clabel;
        String[] reslt = new String[2];
        switch(mode){
			case 1:
				clabel = ecrsr.getString(ecrsr.getColumnIndex(CommonDataKinds.Phone.LABEL));
                conLabel = (String) CommonDataKinds.Phone.getTypeLabel(ctxt.getResources(), conType, "Custom");
                break;
			case 2:
				clabel = ecrsr.getString(ecrsr.getColumnIndex(CommonDataKinds.Email.LABEL));
                conLabel = (String) CommonDataKinds.Phone.getTypeLabel(ctxt.getResources(), conType, "Custom");
                break;
			default:
				clabel = ecrsr.getString(ecrsr.getColumnIndex(CommonDataKinds.Im.CUSTOM_PROTOCOL));
                conLabel = (String) CommonDataKinds.Im.getProtocolLabel(ctxt.getResources(), conType, "Custom");
        }
        Log.i(TAG, "Result of data Type fetch:  " + conLabel);
        Log.i(TAG, "Result of data Label fetch: " + clabel);
        reslt[0] = conLabel;
        reslt[1] = clabel;
        return reslt;
    }
	
	public String[] getContactNames(){
		ArrayList<String> aList = new ArrayList<>();
		String[] tmp = new String[2];
		if(db.isOpen()){
			String[] sqlcols = new String[]{mDBHelper.COLUMN_CONTACT_ID,mDBHelper.COLUMN_DISPLAY_NAME};
			crsr = db.query(true,
			mDBHelper.TABLE_CONTACTS,
			sqlcols,
			null,
			null,
			mDBHelper.COLUMN_CONTACT_ID,
			null,
			null,
			null);
			if (crsr.moveToFirst()){
				do {
				tmp[0] = crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_CONTACT_ID));
				tmp[1] = crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_DISPLAY_NAME));
					String build = "["+tmp[0]+"] "+tmp[1];
					aList.add(build);
				} while (crsr.moveToNext());
			}
		}
		tmp = aList.toArray(new String[aList.size()]);
		return tmp;
	}
	
	public String[] getContactData(String _id){
		// new ArrayList store
		ArrayList<String> aList = new ArrayList<>();
		String[] tmp = new String[2];
		String sel = mDBHelper.COLUMN_CONTACT_ID + " LIKE ?";
		String[] selArgs = new String[]{_id};
		if(db.isOpen()){
            String[] sqlcols = new String[]{mDBHelper.COLUMN_CONTACT_ID, mDBHelper.COLUMN_DISPLAY_NAME, mDBHelper.COLUMN_DATA_TYPE, mDBHelper.COLUMN_DATA_LABEL, mDBHelper.COLUMN_DATA_VALUE, mDBHelper.COLUMN_ACTIVE};
            crsr = db.query(mDBHelper.TABLE_CONTACTS,sqlcols,sel,selArgs,null,null,null);
			if (crsr.moveToFirst()){
                tmp = new String[6];
                do {
					tmp[0] = crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_CONTACT_ID));
					tmp[1] = crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_DISPLAY_NAME));
					tmp[2] = crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_DATA_TYPE));
                    tmp[3] = crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_DATA_LABEL));
                    tmp[4] = crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_DATA_VALUE));
                    if (crsr.getString(crsr.getColumnIndex(mDBHelper.COLUMN_ACTIVE)).equals("1")){
                        tmp[5] = "Active";
                    } else {
                        tmp[5] = "";
                    }
                    Log.d(TAG, "Debug: [" + tmp[0] + ":" + tmp[1] + ":" + tmp[2] + ":" + tmp[3] + ":" + tmp[4] + ":" + tmp[5]);
                    String build = "[" + tmp[0] + "] " + tmp[1] + ", " + tmp[2] + " [" + tmp[3] + "/" + tmp[4] + "] " + tmp[5];
                    aList.add(build);
				} while (crsr.moveToNext());
			}
			// Restore complete.
		} else {
			// Database not opened.
			aList.add("EMPTY");
		}
		tmp = aList.toArray(new String[aList.size()]);
		return tmp;
	}
}

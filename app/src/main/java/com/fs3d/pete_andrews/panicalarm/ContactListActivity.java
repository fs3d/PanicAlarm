package com.fs3d.pete_andrews.panicalarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class ContactListActivity extends ActionBarActivity {

    public ArrayList<ArrayList<String>> SecureContactList = new ArrayList<>();

    public String id, contact_id, display_name, data_category, data_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        /* Retrieve Intent Data in case there are arguments to be processed.
         * If no arguments are passed, leave Contacts Manager open for user input.
         * If an Add argument is passed, process the incoming data and then return control to
         * the activity that started this one.
         **/
        OpenDatabase();
        String args = getIntent().getStringExtra("args");
        if (args.equals("add")) {
            FetchContact();
            commitData();
            finish();
        } else if (args.equals("mgr")) {
            // Manage contacts from here.
            ManageContacts();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Options to be included in menu to manipulate contact list.
        // Methods to be added below this line.

        return super.onOptionsItemSelected(item);
    }

    public void FetchContact() {
        Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        startActivityForResult(i, 101);
    }

    public String[] PullContactID(Uri contactData) {
        String[] con_id = new String[2];
        Cursor con = null; // For retrieval of ID from Contact Picker Activity
        try {
            // The following line retrieves the contact supplied from the Contacts Picker as a query result.
            // This is done using Reflection. It can also be done using projection.
            con = getContentResolver().query(contactData, null, null, null, null);
            if (con.moveToFirst()) {
                con_id[0] = con.getString(con.getColumnIndexOrThrow(Contacts._ID));
                con_id[1] = con.getString(con.getColumnIndex(Contacts.DISPLAY_NAME));
            }
        } catch (Exception e) {
            // No op. If this has come back as a result of a valid contact selection, then something
            // has seriously gone wrong somewhere. We need a Stack Trace.
            e.printStackTrace();
            con_id[0] = "NO_DATA";
        } finally {
            if (con != null) {
                con.close(); // Close the connection to the Contact List so we don't leak memory.
            }
        }
        return con_id;
    }

    public String[] PullPhoneNumbers(String contact_id) {
        Cursor phonesearch = null; // For search and access of phone data
        String phoneNumber;
        String[] phoneNumbers = new String[]{"NO_DATA"};
        try {
            String[] projection = new String[]{
                    CommonDataKinds.Phone.DISPLAY_NAME,
                    CommonDataKinds.Phone.NUMBER,
                    CommonDataKinds.Phone.MIMETYPE
            };
            Log.i("PullPhoneNumbers", "String passed to contact_id is " + contact_id);
            phonesearch = getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    CommonDataKinds.Phone.CONTACT_ID + " = " + contact_id,
                    null, null);
            if (phonesearch.moveToFirst()) {
                int phnumber = phonesearch.getColumnIndex(CommonDataKinds.Phone.NUMBER);
                phoneNumbers = new String[phonesearch.getCount()];
                int i = 0;
                do {
                    // Add phone number to String array
                    phoneNumber = phonesearch.getString(phnumber);
                    Log.w("Data Retrieval", "Phone Number: " + phoneNumber);
                    phoneNumbers[i] = phoneNumber;
                    i++;
                } while (phonesearch.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            phonesearch.close();
            return phoneNumbers;
        }
    }

    public String[] PullEmailAddresses(String contact_id) {
        Cursor mailsearch = null; // For search and access of phone data
        String email;
        String[] emails = new String[]{"NO_DATA"};
        try {
            String[] projection = new String[]{
                    CommonDataKinds.Phone.DISPLAY_NAME,
                    CommonDataKinds.Phone.NUMBER,
                    CommonDataKinds.Phone.MIMETYPE
            };
            Log.i("PullPhoneNumbers", "String passed to contact_id is " + contact_id);
            mailsearch = getContentResolver().query(CommonDataKinds.Email.CONTENT_URI,
                    projection,
                    CommonDataKinds.Email.CONTACT_ID + " = " + contact_id,
                    null, null);
            if (mailsearch.moveToFirst()) {
                int address = mailsearch.getColumnIndex(CommonDataKinds.Email.ADDRESS);
                emails = new String[mailsearch.getCount()];
                int i = 0;
                do {
                    // Add phone number to String array
                    email = mailsearch.getString(address);
                    Log.w("Data Retrieval", "" + "Email Address: " + email);
                    emails[i] = email;
                    i++;
                } while (mailsearch.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailsearch.close();
            return emails;
        }
    }

    public void OpenDatabase() {
        // Opens the DB in read-only mode. An ArrayAdapter will be built directly from this.
        StorageHelper mHelper = new StorageHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String SQL_DELETE_PERSONS =
                "DROP TABLE IF EXISTS " + StorageHelper.PersonEntry.TABLE_NAME;
        String SQL_DELETE_CONTACTS =
                "DROP TABLE IF EXISTS " + StorageHelper.ContactEntry.TABLE_NAME;
        Log.i("ContactListActivity", "Executing delete statements (Debugging purposes)");
        db.execSQL(SQL_DELETE_PERSONS);
        db.execSQL(SQL_DELETE_CONTACTS);
        Log.i("ContactListActivity", "Tables deleted. Creating new ones...");
        String sortOrder = StorageHelper.PersonEntry.COLUMN_NAME_UID + " DESC";
        Cursor cc, cp;
        try {
            // Person entry first.
            cc = db.query(StorageHelper.PersonEntry.TABLE_NAME, null, null, null, null, null, sortOrder);
            cc.moveToFirst();
            cp = db.query(StorageHelper.ContactEntry.TABLE_NAME, null, null, null, null, null, null);
            cp.moveToFirst();
        } catch (SQLiteException e) {
            // Exception thrown trying to query database.
            Log.i("ContactListActivity", "Trying to open the database as Read-Only failed. Trying again as Read-Write.");
            mHelper.onCreate(db);
            db = mHelper.getWritableDatabase();
            // createTables();
            cc = db.query(StorageHelper.PersonEntry.TABLE_NAME, null, null, null, null, null, null);
            cc.moveToFirst();
            cp = db.query(StorageHelper.ContactEntry.TABLE_NAME, null, null, null, null, null, null);
            cp.moveToFirst();
        }

        int locindx = 0;
        int topindx = 0;
        String name = "";
        String prevtype = "_NAME";
        ArrayList<ArrayList<String>> contacts = new ArrayList<>();
        do {
            try {
                int itemId = cp.getInt(cp.getColumnIndexOrThrow(StorageHelper.PersonEntry.COLUMN_NAME_UID));
                String per_id = cp.getString(cp.getColumnIndex(StorageHelper.PersonEntry.COLUMN_NAME_CONTACT_ID));
                String dispname = cp.getString(cp.getColumnIndex(StorageHelper.PersonEntry.COLUMN_DISPLAY_NAME));
                // The above lines reference the persons table. The top level ArrayList is built from this.
                do {
                    String con_id = cc.getString(cc.getColumnIndex(StorageHelper.ContactEntry.COLUMN_NAME_CONTACT_ID));
                    String dataval = cc.getString(cc.getColumnIndex(StorageHelper.ContactEntry.COLUMN_DATA_FIELD));
                    String datatype = cc.getString(cc.getColumnIndex(StorageHelper.ContactEntry.COLUMN_DATA_CATEGORY));
                    // The above 3 lines reference the contactable table. The 2nd dimension ArrayList is built from this.
                } while (cc.moveToNext());
            } catch (Exception xx) {
                // Exception thrown. Let's see what the issue is.
                xx.printStackTrace();
                Log.i("ContactList Exception", "Attempting to read column data for records in one of the tables has thrown the above Exception.");
            }
        } while (cp.moveToNext());
    }

    public void AddToContactList(String contact_id, String[] numbers, String[] emails) {
        // Append the latest contact to the Array List.
        int rec = SecureContactList.size();
        SecureContactList.add(new ArrayList<String>());
        SecureContactList.get(rec).add(contact_id);
        SecureContactList.get(rec).add("_NUMBERS");
        for (int i = 0; i < numbers.length; i++) {
            SecureContactList.get(rec).add(numbers[i]);
        }
        SecureContactList.get(rec).add("_EMAILS");
        for (int i = 0; i < emails.length; i++) {
            SecureContactList.get(rec).add(emails[i]);
        }
    }

    public void ManageContacts() {
        // Contact management - This is intended to populate the Layout with the current
        // ArrayList of contacts.
    }

    public void createTables() {
        // Creates the contact tables if they don't already exist. Person Entry first.
        StorageHelper mHelper = new StorageHelper(this);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Log.i("ContactListActivity", "Attempting removal of extraneous tables...");
        db.execSQL("DROP TABLE IF EXISTS '" + StorageHelper.PersonEntry.TABLE_NAME + "'");
        Log.i("ContactListActivity", "Attempting to create table '" + StorageHelper.PersonEntry.TABLE_NAME + "'");
        db.execSQL("CREATE TABLE '" + StorageHelper.PersonEntry.TABLE_NAME + "' ('" +
                StorageHelper.PersonEntry.COLUMN_NAME_UID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '" +
                StorageHelper.PersonEntry.COLUMN_NAME_CONTACT_ID + "' TEXT, '" +
                StorageHelper.PersonEntry.COLUMN_DISPLAY_NAME + "' TEXT)");
        Log.i("ContactListActivity", "Attempting removal of extraneous tables...");
        db.execSQL("DROP TABLE IF EXISTS '" + StorageHelper.ContactEntry.TABLE_NAME + "'");
        Log.i("ContactListActivity", "Attempting to create table '" + StorageHelper.ContactEntry.TABLE_NAME + "'");
        db.execSQL("CREATE TABLE '" + StorageHelper.ContactEntry.TABLE_NAME + "' ('" +
                StorageHelper.ContactEntry.COLUMN_NAME_CONTACT_ID + "' TEXT, '" +
                StorageHelper.ContactEntry.COLUMN_DATA_CATEGORY + "' TEXT, '" +
                StorageHelper.ContactEntry.COLUMN_DATA_FIELD + "' TEXT)");
        Log.i("ContactListActivity", "Trying to verify existence of data in tables...");
        String pathway = db.getPath().toString();
        String vers = String.valueOf(db.getVersion());
        Log.i("ContactListActivity", "Reported DB is " + pathway + " (V" + vers + ")");
    }

    public void commitData() {
        // This method will commit the data changes to the Database tables.
        // First, the Person table. This will only need to be updated with additions and deletions
        // from the Contact List. Changes to contact data for each person does not affect this table.

        // Now the Contactables table. This will need updating for every contactable change
        // (e.g. adding or removing a contact type like an email or phone number).
    }
    /*
     * Below this line is the onActivityResult method to determine what to do when a system-based
     * activity returns data to be processed. Currently it only houses the handling code for
     * data returned by the contact picker activity, which is all that's needed in this activity.
     */

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (101):
                if (resultCode == Activity.RESULT_OK) {
                    Uri cData = data.getData();
                    // We need the ID for the selected contact to perform subsequent searches.
                    String[] con_id = PullContactID(cData);
                    if (!con_id[0].equals("NO_DATA")) {
                        // If the ID is not in the Database, we will add it now.

                        // Otherwise, we will select the existing one.

                        // Now we have the ID for the selected contact, we can use it to pull in the other database entries.
                        Log.i("Activity Result", "Contact name returned is " + con_id[1]);
                        String[] numberList = PullPhoneNumbers(con_id[0]);
                        if (numberList[0].equals("NO_DATA")) {
                            Log.i("Activity Result", "No phone number entries for this contact.");
                        } else {
                            Log.i("Activity Result", "There are " + String.valueOf(numberList.length) + " entries for this contact.");
                            // If there are phone numbers, they can be added to the Database.
                        }
                        // Now we will use the ID to pull the email addresses.
                        String[] emailList = PullEmailAddresses(con_id[0]);
                        if (emailList[0].equals("NO_DATA")) {
                            Log.i("Activity Result", "No email address entries for this contact.");
                        } else {
                            Log.i("Activity Result", "There are " + String.valueOf(emailList.length) + " entries for this contact.");
                            // If there are email addresses, they can be added to the Database.
                        }
                        // Now we have all the information we need, it's time to populate the
                        // Contact Manager.
                        Log.i("Activity Result", "Calling AddToContactList...");
                        AddToContactList(con_id[1], numberList, emailList);
                    } else {
                        Log.e("Activity Result", "There was a problem retrieving the contact.");
                    }
                }
        }
    }

    // The last method in this class is the exit method to close the manager on user request.

    public void exitContactMgr(View v) {
        finish();
    }
}

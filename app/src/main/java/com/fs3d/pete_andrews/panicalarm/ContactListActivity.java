package com.fs3d.pete_andrews.panicalarm;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public ArrayList<ArrayList<String>> SecureContactList = new ArrayList<ArrayList<String>>();

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
        Cursor c = null; // For retrieval of ID from Contact Picker Activity
        try {
            // The following line retrieves the contact supplied from the Contacts Picker as a query result.
            // This is done using Reflection. It can also be done using projection.
            c = getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                con_id[0] = c.getString(c.getColumnIndexOrThrow(Contacts._ID));
                con_id[1] = c.getString(c.getColumnIndex(Contacts.DISPLAY_NAME));
            }
        } catch (Exception e) {
            // No op.
            e.printStackTrace();
            con_id[0] = "NO_DATA";
        } finally {
            if (c != null) {
                c.close();
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
        // Opens the DB in read-only mode to populate an ArrayList of contacts.
        StorageHelper mHelper = new StorageHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sortOrder = StorageHelper.ContactEntry.COLUMN_NAME_CONTACT_ID + " DESC";

        Cursor c = db.query(StorageHelper.ContactEntry.TABLE_NAME, null, null, null, null, null, sortOrder);
        c.moveToFirst();
        int locindx = 0;
        int topindx = 0;
        String name = "";
        String prevtype = "_NAME";
        ArrayList<String> nums = new ArrayList<String>();
        ArrayList<String> mails = new ArrayList<String>();
        do {
            String itemId = c.getString(c.getColumnIndexOrThrow(StorageHelper.ContactEntry.COLUMN_NAME_UID));
            String con_id = c.getString(c.getColumnIndexOrThrow(StorageHelper.ContactEntry.COLUMN_NAME_CONTACT_ID));
            String dispname = c.getString(c.getColumnIndexOrThrow(StorageHelper.ContactEntry.COLUMN_DISPLAY_NAME));
            String dataval = c.getString(c.getColumnIndexOrThrow(StorageHelper.ContactEntry.COLUMN_DATA_FIELD));
            String datatype = c.getString(c.getColumnIndexOrThrow(StorageHelper.ContactEntry.COLUMN_DATA_CATEGORY));
            if (datatype.equals("NUMBER")) {
                if (prevtype.equals("_NAME")) {
                    nums.add("_NUMBERS");
                    prevtype = "_NUMBERS";
                }
                nums.add(dataval);
            } else if (datatype.equals("EMAIL")) {
                if (prevtype.equals("_NUMBERS")) {
                    mails.add("_MAILS");
                }
                mails.add(dataval);
            }
            locindx++;
            if (!dispname.equals(name)) {
                // Names are different, time to make a new entry.
                name = dispname;
                locindx = 0;
                topindx++;
            }
        } while (c.moveToNext());
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

    public void commitData() {
        // This method will commit the data changes to the Database, overwriting the old one.
        // We can do this because this activity pulls the existing DB changes and populates an
        // ArrayList with the current data before any changes are made.
        StorageHelper mHelper = new StorageHelper(this);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StorageHelper.ContactEntry.COLUMN_NAME_UID, id);
        values.put(StorageHelper.ContactEntry.COLUMN_NAME_CONTACT_ID, contact_id);
        values.put(StorageHelper.ContactEntry.COLUMN_DISPLAY_NAME, display_name);
        values.put(StorageHelper.ContactEntry.COLUMN_DATA_CATEGORY, data_category);
        values.put(StorageHelper.ContactEntry.COLUMN_DATA_FIELD, data_value);

        long newRowId;
        newRowId = db.insert(StorageHelper.ContactEntry.TABLE_NAME,
                StorageHelper.ContactEntry.COLUMN_NAME_CONTACT_ID,
                values);
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
                    // Now we have the ID for the selected contact, we can use it to pull in the other database entries.
                    if (!con_id[0].equals("NO_DATA")) {
                        Log.i("Activity Result", "Contact name returned is " + con_id[1]);
                        String[] numberList = PullPhoneNumbers(con_id[0]);
                        if (numberList[0].equals("NO_DATA")) {
                            Log.i("Activity Result", "No phone number entries for this contact.");
                        } else {
                            Log.i("Activity Result", "There are " + String.valueOf(numberList.length) + " entries for this contact.");
                        }
                        // Now we will use the ID to pull the email addresses.
                        String[] emailList = PullEmailAddresses(con_id[0]);
                        if (emailList[0].equals("NO_DATA")) {
                            Log.i("Activity Result", "No email address entries for this contact.");
                        } else {
                            Log.i("Activity Result", "There are " + String.valueOf(emailList.length) + " entries for this contact.");
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
        commitData();
        finish();
    }
}

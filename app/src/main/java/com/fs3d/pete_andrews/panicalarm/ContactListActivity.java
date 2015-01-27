package com.fs3d.pete_andrews.panicalarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ContactListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        /* Retrieve Intent Data in case there are arguments to be processed.
         * If no arguments are passed, leave Contacts Manager open for user input.
         * If an Add argument is passed, process the incoming data and then return control to
         * the activity that started this one.
         **/
        String args = getIntent().getStringExtra("args");
        if (args.equals("add")) {
            FetchContact();
        } else if (args.equals("mgr")) {
            // Manage contacts from here.
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
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, 101);
    }

    /*
     * Below this line is the onActivityResult method to determine what to do when a system-based
     * activity returns data to be processed. Currently it only houses the handling code for
     * data returned by the contact picker activity, which is all that's needed in this activity.
     */

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String name = "";
        switch (reqCode) {
            case (101):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = null;
                    try {
                        // The following line retrieves the contact supplied from the Contacts Picker as a query result.
                        // This is done using Reflection. It can also be done using projection.
                        c = getContentResolver().query(contactData, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
                        if (c.moveToFirst()) {
                            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            String phonenum = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            int numentries = c.getCount();
                            Log.i("ContactManagement", "returned " + String.valueOf(numentries) + " top-level records.");
                            Log.i("ContactManagement", "returned " + phonenum);
                            int numcols = c.getColumnCount();
                            Log.i("ContactManagement", "returned " + String.valueOf(numcols) + " columns in current record.");
                        }
                    } catch (Exception e) {
                        // No op.
                        e.printStackTrace();
                        name = "[EXCEPTION CAUGHT] " + e.toString();
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                        Log.w("Contact Retrieval", "Retrieved: " + name);
                    }
                    break;
                }
        }
    }

    // The last method in this class is the exit method to close the manager on user request.

    public void exitContactMgr(View v) {
        finish();
    }
}

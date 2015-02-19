package com.fs3d.pete_andrews.panicalarm;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactListActivity extends ActionBarActivity {

    private static final String TAG = "ContactListActivity";
    public ArrayList<String[]> SecureConList = new ArrayList<>();
    public ArrayList<ArrayList<String>> SecureContactList = new ArrayList<>();
    public String id, contact_id, display_name, data_category, data_value;
    private dataManager dmgr;
    private ListView mListViewPeople;

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

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        // This entire If block encapsulates an OK result for all requests.
        if (resultCode == RESULT_OK) {
            // This nested If block checks for a request linked to the contact picker activity.
            if (reqCode == 101) {
                // Declare all private variables for this method.
                ArrayList<String[]> dataList = new ArrayList<String[]>();
                // First, we find and then pull the details from the Contact Picker data.
                Uri cdat = data.getData();
                Cursor crsr = getContentResolver().query(cdat, null, null, null, null);
                crsr.moveToFirst(); // This should be guaranteed to work unless the Contact Picker is bugged.
                // Next, we extract the Contact ID and Display Name.
                String conId = crsr.getString(crsr.getColumnIndexOrThrow(Contacts._ID));
                String displayName = crsr.getString(crsr.getColumnIndexOrThrow(Contacts.DISPLAY_NAME));
                // Now, we close the Contact Picker cursor.
                crsr.close();
                // We call our custom Data Manager class to handle retrieving all other information.
                dmgr = new dataManager(this, conId, displayName);
                // Now we call a pullData method to find all the phone numbers.
                dataList.add(dmgr.pullData(1));
                // Now we do the same for email addresses.
                dataList.add(dmgr.pullData(2));
                // Now we do the same with all the IM accounts.
                dataList.add(dmgr.pullData(3));
                // Finally, we add everything to the internal database.

                //All done. We can go now.
            }
        }
    }

    // The last method in this class is the exit method to close the manager on button tap.

    public void exitContactMgr(View v) {
        finish();
    }
}

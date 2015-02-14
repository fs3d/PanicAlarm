package com.fs3d.pete_andrews.panicalarm;

import android.content.Intent;
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
    public ArrayList<ArrayList<String>> SecureContactList = new ArrayList<>();
    public String id, contact_id, display_name, data_category, data_value;
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
                // To-do: Put the relevant Contact information into the internal database.

            }
        }
    }

    // The last method in this class is the exit method to close the manager on user request.

    public void exitContactMgr(View v) {
        finish();
    }
}

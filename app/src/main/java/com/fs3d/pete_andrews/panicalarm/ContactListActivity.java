package com.fs3d.pete_andrews.panicalarm;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ContactListActivity extends ActionBarActivity {

    private static final String TAG = "ContactListActivity";
    public String id;
    public String contact_id;
    public String display_name;
    public String data_category;
	TextView tvDebug;
	ListView lvContacts;
	ArrayList conList;
	String[] conArray;
    private dataManager dmgr;

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
		lvContacts = (ListView) findViewById(R.id.contactLV);
		tvDebug = (TextView) findViewById(R.id.tvDebug);
        if (args.equals("add")) {
			Log.d(TAG,"Call to Contact Picker");
            FetchContact();
        } else {
			// Populate from existing data.
			Log.d(TAG,"Populating ListView...");
			PopulateList();
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

    void FetchContact() {
		tvDebug.setText("FetchContact calling Contact Picker...\n");
        Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        startActivityForResult(i, 101);
    }
	
	void PopulateList() {
		// Populate ListView with data from internal database.
		lvContacts = (ListView) findViewById(R.id.contactLV);
		dataManager dmanage = new dataManager(this);
		dmanage.connectDatabase();
		conArray = dmanage.getContactNames();
		tvDebug.setText("Returned "+conArray.length+" records.");
		ArrayAdapter adpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,conArray);
		lvContacts.setAdapter(adpt);
		lvContacts.invalidate();
	}

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        // This entire If block encapsulates an OK result for all requests.
		tvDebug.append("FetchContact returned result...\n");
        if (resultCode == RESULT_OK) {
            // This nested If block checks for a request linked to the contact picker activity.
            if (reqCode == 101) {
                // Declare all private variables for this method.
                String status = "_NULL";
                ArrayList<String[]> dataList = new ArrayList<>();
				tvDebug.append("New ArrayList...\n");
                // First, we find and then pull the details from the Contact Picker data.
                Uri cdat = data.getData();
				tvDebug.append("Call to GetData...\n");
                dataList = processContact(cdat); // Pass the URI to processContact method.
				if (dataList.isEmpty()){
					// Failed to get data.
                    status = "_EMPTY"; // Send Empty response back to prior activity
                } else {
                    tvDebug.append("Call to processContact...\n");
                    dataManager dmanage = new dataManager(this, dataList.get(0)[0], dataList.get(0)[1]);
                    dmanage.connectDatabase();
                    tvDebug.append("New dataManager...\n");
                    for (int i = 0; i < dataList.size(); i++) {
                        // Array locations: 0=ID, 1=Name, 2=Type, 3=Value, 4=State
                        tvDebug.append("[" + dataList.get(i)[0] + "] "
                                + dataList.get(i)[1] + " <<"
                                + dataList.get(i)[2] + ">> "
                                + dataList.get(i)[3] + " / "
                                + dataList.get(i)[4] + " [Entry " + String.valueOf(i)
                                + "]... ");
                        if (!dataList.get(i)[0].equals("EMPTY")) {
                            status = dmanage.addData(dataList.get(i)[2], dataList.get(i)[3], dataList.get(i)[4], dataList.get(i)[5], true);
                            if (status.equals("INVALID_DATA")) {
                                tvDebug.append("\nInvalid data returned. Exiting...\n");
                                break;
                            }
                            if (status.equals("EXISTS_OK")) {
                                tvDebug.append(" Record exists and override specified.\n");
                            }
                            if (status.equals("EXISTS")) {
                                tvDebug.append(" Record exists. No override. Exiting...\n");
                                break;
                            }
                            if (status.equals("READ_ONLY")) {
                                tvDebug.append(" ERROR\nThe database connection has been opened READ ONLY.\nCheck your code.\n\n");
                                break;
                            }
                            if (status.equals("NO_DB")) {
                                tvDebug.append(" ERROR\nThere is no connection to the database.\nCheck your code and ensure read-write specified.\n\n");
                                break;
                            }
                            if (status.equals("OK")) {
                                tvDebug.append(" New record added OK.");
                            }
                        } else {
                            status = dataList.get(i)[0];
                            tvDebug.append("Not a valid entry.\n");
                        }
                    }
                    Intent reply = new Intent();
                    reply.putExtra("recent_name", contact_id);
                    reply.putExtra("add_status", status);
                    setResult(101, reply);
                }
            }
        }
    }

    public ArrayList<String[]> processContact(Uri uri) {
        // This method will process the contact data for the selected contact ID.
        Cursor crsr = getContentResolver().query(uri, null, null, null, null);
        crsr.moveToFirst(); // This should be guaranteed to work unless the Contact Picker is bugged.
        // Next, we extract the Contact ID and Display Name.
        String conId = crsr.getString(crsr.getColumnIndexOrThrow(Contacts._ID));
        String displayName = crsr.getString(crsr.getColumnIndexOrThrow(Contacts.DISPLAY_NAME));
        // Now, we close the Contact Picker cursor.
        crsr.close();

        // We call our custom Data Manager class to handle retrieving all other information.
        dmgr = new dataManager(this, conId, displayName);
        ArrayList<String[]> aList = new ArrayList<>();
        // We call a pullData method to find all the phone numbers.
        String[][] phonenums = dmgr.pullData(1);
        if (!phonenums[0][0].equals("EMPTY"))
            aList.addAll(Arrays.asList(phonenums));
        // Now we do the same for email addresses.
        String[][] mailaddrs = dmgr.pullData(2);
        if (!mailaddrs[0][0].equals("EMPTY"))
            aList.addAll(Arrays.asList(mailaddrs));
        // Now we do the same with all the IM accounts.
        String[][] instantms = dmgr.pullData(3);
        if (!instantms[0][0].equals("EMPTY"))
            aList.addAll(Arrays.asList(instantms));
        //All done. We can pass the completed ArrayList back to the calling method now.
        return aList;
    }

    // The last method in this class is the exit method to close the manager on button tap.

    public void exitContactMgr(View v) {
        // Send data back to processing activity for status reporting purposes.
        Intent reply = new Intent();
        reply.putExtra("distinct_contacts", 0);
        reply.putExtra("total_records", 0);
        setResult(102, reply);
        finish();
    }
}

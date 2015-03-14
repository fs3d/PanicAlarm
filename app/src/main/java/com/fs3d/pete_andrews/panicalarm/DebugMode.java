package com.fs3d.pete_andrews.panicalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DebugMode extends ActionBarActivity {

    // Reference variables for views in activity
    TextView statusBox;
    Button btn_repAcc, btn_repGPS, btn_repMic, btn_repCam, btn_repSMS, btn_repMail, btn_repHeadSet, btn_repPwr, btn_startSvc, btn_killSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_mode);
        // Status Box reference
        statusBox = (TextView) findViewById(R.id.status_view);
        // All button references
        btn_repGPS = (Button) findViewById(R.id.btn_getGpsStatus);
        btn_repAcc = (Button) findViewById(R.id.btn_getSensors);
        btn_repCam = (Button) findViewById(R.id.btn_getCamStatus);
        btn_repMic = (Button) findViewById(R.id.btn_getCamStatus);
        btn_repSMS = (Button) findViewById(R.id.btn_getMicStatus);
        btn_repMail = (Button) findViewById(R.id.btn_other1);
        btn_repPwr = (Button) findViewById(R.id.btn_testPwrStatus);
        btn_repHeadSet = (Button) findViewById(R.id.btn_other2);
        btn_startSvc = (Button) findViewById(R.id.btn_triggerSvcTest);
        btn_killSvc = (Button) findViewById(R.id.btn_killService);
        // Set onClickListener to handle long press on the status text.
        statusBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                statusBox.setText("Cleared by long press.");
                return false;
            }
        });
        // Preference handling routines go here.

        // Service Status will be picked up here.

    }

    // Options Menu setup goes here. Currently it only has a dummy Settings menu.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug_mode, menu);
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

        return super.onOptionsItemSelected(item);
    }

    // Additional methods below e.g. onClick XML redirects go here.

    public void reportGPSStatus(View v) {
        statusBox.append("\nGPS Status Button touched.\nGPS Status is [TEST].");
    }

    public void launchService(View v) {
        // This will launch the main Panic Service in test mode.
        Intent intent = new Intent(this.getApplicationContext(), PanicService.class);
        intent.putExtra("args", new String[]{"start_service"});
        startService(intent);
    }
}
package com.fs3d.pete_andrews.panicalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class InitActivity extends ActionBarActivity {

    // Activity Variables
    TextView splashView;
    TextView copyrightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        splashView = (TextView) findViewById(R.id.splashText);
        copyrightView = (TextView) findViewById(R.id.copyrightText);
        copyrightView.setText("Copyright (C) 2015 Peter Andrews. All rights specified under" +
                " applicable copyright law and international treaties are hereby reserved.\n\n" +
                "Unauthorised duplication, distribution, broadcast and transmission constitutes a " +
                "civil and/or criminal violation and may result in prosecution under applicable laws " +
                "in either your home country, the author's home country, or the " +
                "United States of America, as defined in applicable copyright law.");
        try {
            String splashline = "Panic Alarm v" + BuildConfig.VERSION_NAME;
            splashView.setText(splashline);
        } catch (Exception e) {
            String splashline = "Panic Alarm (Unable to parse version information)";
            e.printStackTrace();
            splashView.setText(splashline);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_init, menu);
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
            // Launch Activity with Preferences Fragments
            Intent intent = new Intent(this, PrefsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goDebug(View v) {
        Intent intnt = new Intent(this, DebugMode.class);
        startActivity(intnt);
    }

    public void goSecure(View v) {
        Intent intnt = new Intent(this, ServiceControl.class);
        startActivity(intnt);
    }
}

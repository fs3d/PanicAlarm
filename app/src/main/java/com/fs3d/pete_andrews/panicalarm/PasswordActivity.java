package com.fs3d.pete_andrews.panicalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class PasswordActivity extends Activity {

    Boolean serv_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Bundle incoming = getIntent().getExtras();
        if (incoming != null) {
            Log.i("PasswordActivity", "Intent data discovered");
            serv_stat = incoming.getBoolean("passworded");
            if (serv_stat) {
                Log.i("PasswordActivity", "serv_stat incoming was true. Now set to false.");
                serv_stat = false;
            } else {
                Log.i("PasswordActivity", "serv_stat incoming was false. Now set to true.");
                serv_stat = true;
            }
        } else {
            Log.i("PasswordActivity", "No intent data discovered.");
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void finish() {
        Intent feedback = new Intent();
        if (serv_stat) {
            Log.i("Called finish()", "serv_stat is true. Sending password data back to PreferenceFragment");
            feedback.putExtra("Password", "AAAAAA");
            feedback.putExtra("CryptKey", "123456");
        } else {
            Log.i("Called finish()", "serv_stat is false. Sending Unset CryptKey message back to PreferenceFragment");
            feedback.putExtra("Password", "");
            feedback.putExtra("CryptKey", "Unset");
        }
        setResult(RESULT_OK, feedback);
        super.finish();
    }
}

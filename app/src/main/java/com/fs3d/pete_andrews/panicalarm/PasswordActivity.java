package com.fs3d.pete_andrews.panicalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class PasswordActivity extends Activity {

    Boolean serv_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Bundle incoming = getIntent().getExtras();
        if (incoming != null) {
            serv_stat = incoming.getBoolean("passworded");
            if (serv_stat) {
                serv_stat = false;
            } else {
                serv_stat = true;
            }
        } else {
            serv_stat = true;
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
            feedback.putExtra("Password", "AAAAAA");
            feedback.putExtra("CryptKey", "123456");
        } else {
            feedback.putExtra("Password", "");
            feedback.putExtra("CryptKey", "Unset");
        }
        setResult(RESULT_OK, feedback);
        super.finish();
    }
}

package com.fs3d.pete_andrews.panicalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PasswordActivity extends Activity {

    Boolean serv_stat;
    String passwd;
    TextView tvPass1, tvPass2, tvTTL;
    Button btnAccept, btnCancel;

    // A
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Dialog);
        setContentView(R.layout.activity_password);
        btnCancel = (Button) findViewById(R.id.btnCancelPass);
        btnAccept = (Button) findViewById(R.id.btnAcceptPass);
        tvPass1 = (TextView) findViewById(R.id.etPassword1);
        tvPass2 = (TextView) findViewById(R.id.etPassword2);
        tvTTL = (TextView) findViewById(R.id.tvPassTitle);
        setFinishOnTouchOutside(false);
        Bundle incoming = getIntent().getExtras();
        if (incoming != null) {
            Log.i("PasswordActivity", "Intent data discovered");
            serv_stat = incoming.getBoolean("passworded");
            if (serv_stat) {
                // Password Status has been set. Require existing password first.
                tvTTL.setText("Pref Boolean Set to True on entry.");
            } else {
                // Password Status has not been set. New password can be set up.
                tvTTL.setText("Pref Boolean Set to False on entry.");
            }
        } else {
            Log.i("PasswordActivity", "No intent data discovered.");
        }
    }

    public void acceptPassword(View v) {
        // Password accept tapped. Read information from fields.
        serv_stat = true;
        passwd = (String) tvPass1.getText();
        finish();
    }

    public void cancelPassword(View v) {
        // Cancel tapped. Close dialog and send Unset to prior activity.
        serv_stat = false;
        finish();
    }

    @Override
    public void finish() {
        Intent feedback = new Intent();
        if (serv_stat) {
            Log.i("Called finish()", "serv_stat is true. Sending password data back to PreferenceFragment");
            feedback.putExtra("Password", passwd);
            feedback.putExtra("CryptKey", passwd);
        } else {
            Log.i("Called finish()", "serv_stat is false. Sending Unset CryptKey message back to PreferenceFragment");
            feedback.putExtra("Password", "");
            feedback.putExtra("CryptKey", "Unset");
        }
        setResult(RESULT_OK, feedback);
        super.finish();
    }
}

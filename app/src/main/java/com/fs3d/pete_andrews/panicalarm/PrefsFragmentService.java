package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

/** //
 * Created by peteb_000 on 03/01/2015. Further edits pending
 */
public class PrefsFragmentService extends PreferenceFragment {

    Preference checkPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        final Context ctxt = this.getActivity();
        // Check for password preference so we can launch a dedicated Password Activity from it.
        checkPassword = findPreference("check_settings_passwd");
        checkPassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Boolean passActivityArg = checkPassword.getSharedPreferences().getBoolean("check_settings_passwd", true);
                Intent intnt = new Intent(ctxt, PasswordActivity.class);
                intnt.putExtra("passworded", passActivityArg);
                startActivityForResult(intnt, 1);
                return false;
            }
        });
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        String passcd = data.getStringExtra("Password");
        String CryptKey = data.getStringExtra("CryptKey");
        String resultStr = "RESULT: Passcode: " + passcd + " - CryptKey:" + CryptKey;
        Log.i("PrefsFragmentService", resultStr);
        if (CryptKey.equals("Unset")) {
            resultStr = "RESULT: Service password unset.";
        }
        checkPassword.setSummary(resultStr);
    }
}
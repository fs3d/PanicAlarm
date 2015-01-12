package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.prefs.PreferenceChangeListener;

/** //
 * Created by peteb_000 on 03/01/2015. Further edits pending
 */
public class PrefsFragmentService extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_PASSWD = "pref_key_passwd";

    Preference checkPassword;
    PreferenceChangeListener chgListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        PreferenceManager.setDefaultValues(this.getActivity(), R.xml.pref_general, false);
        final Context ctxt = this.getActivity();
        // Check for password preference so we can launch a dedicated Password Activity from it.
        checkPassword = findPreference(KEY_PREF_PASSWD);
        checkPassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // checkPassword.setSummary(findPreference("pref_key_passwd").getSharedPreferences().getString("pref_key_passwd","0000"));
                return false;
            }
        });
        checkPassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                checkPassword.setSummary("Preference change detected: " + newValue.toString());
                return false;
            }
        });
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_PASSWD)) {
            Preference key_pass = findPreference(key);
            key_pass.setSummary(sharedPreferences.getString(key, ""));
        }
    }
}

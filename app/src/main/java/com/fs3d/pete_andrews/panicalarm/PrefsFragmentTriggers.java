package com.fs3d.pete_andrews.panicalarm;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by peteb_000 on 03/01/2015. Further edits pending
 */
public class PrefsFragmentTriggers extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_triggers);
    }
}

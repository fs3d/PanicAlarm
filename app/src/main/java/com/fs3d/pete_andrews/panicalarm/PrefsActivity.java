package com.fs3d.pete_andrews.panicalarm;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by peteb_000 on 03/01/2015. Further edits pending
 */
public class PrefsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragmentComms()).commit();

    }
}

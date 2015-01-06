package com.fs3d.pete_andrews.panicalarm;

import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by peteb_000 on 03/01/2015. Further edits pending
 */
public class PrefsActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }
}

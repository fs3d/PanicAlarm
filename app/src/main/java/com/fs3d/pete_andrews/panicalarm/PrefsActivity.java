package com.fs3d.pete_andrews.panicalarm;

import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by peteb_000 on 03/01/2015. Further edits pending
 */
public class PrefsActivity extends PreferenceActivity {

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragmentComms()).commit();

    }*/

    @Override
    protected boolean isValidFragment(String fragmentName) {
        boolean rslt;
        if (PrefsFragmentComms.class.getName().equals(fragmentName)) {
            rslt = true;
        } else {
            if (PrefsFragmentCapture.class.getName().equals(fragmentName)) {
                rslt = true;
            } else {
                if (PrefsFragmentLocation.class.getName().equals(fragmentName)) {
                    rslt = true;
                } else {
                    if (PrefsFragmentTriggers.class.getName().equals(fragmentName)) {
                        rslt = true;
                    } else {
                        if (PrefsFragmentWarnings.class.getName().equals(fragmentName)) {
                            rslt = true;
                        } else {
                            rslt = false;
                        }
                    }
                }
            }
        }
        ;
        return rslt;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }
}

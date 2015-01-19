package com.fs3d.pete_andrews.panicalarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


public class PrefsActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static int fragselect;
    SectionsPagerAdapter mSectionsPagerAdapter;
    List contactList;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prefs, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleSwitch(View v){
        TextView tvdbg = (TextView) findViewById(R.id.tv_debug_report);
        switch (v.getId()) {
            case R.id.btn_enable_gps:
                // Enable GPS switch.
                tvdbg.setText("User tapped enable GPS");
                break;
            case R.id.btn_gps_standby:
                // Enable GPS in vigilance mode (allows quicker location fix in event of a panic)
                tvdbg.setText("User tapped enable GPS Vigilance");
                break;
            case R.id.btn_gps_move:
                // Enable GPS update via move.
                // In vigilance mode, the user sets the distance interval.
                // In panic mode, the interval is updated if any change is detected no matter how small.
                tvdbg.setText("User tapped GPS Distance Check");
                break;
            default:
                // This is for any condition I have not written a case statement for yet.
                tvdbg.setText("User tapped an unallocated control");
        }
        ;
    }

    // Below follow 5 fragments, 1 for each page of settings.

    public void pickContact(View v){
        Intent intnt = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intnt, 1);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);
        String name = "";
        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = null;
                    try {
                        // The following line retrieves the contact supplied from the Contacts Picker as a query result.
                        // This is done using Reflection.
                        c = getContentResolver().query(contactData, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
                        if (c.moveToFirst()) {
                            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        }
                    } catch (Exception e) {
                        // No op.
                        name = "Failed to retrieve contact.";
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                        Log.w("Contact Retrieval", "Retrieved: " + name);
                        TextView reportContact = (TextView) findViewById(R.id.tv_pager_status);
                        reportContact.setText("Retrieved: " + name);
                    }
                    break;
                }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(fragselect, container, false);
            return rootView;
        }
    }

    public static class AlertsFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public AlertsFragment() {
        }

        public static AlertsFragment newInstance(int sectionNumber) {
            AlertsFragment fragment = new AlertsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_pref_1alerts, container, false);
            return rootView;
        }
    }

    public static class LocationFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public LocationFragment() {
        }

        public static LocationFragment newInstance(int sectionNumber) {
            LocationFragment fragment = new LocationFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_pref_2location, container, false);
            return rootView;
        }
    }

    public static class TriggerFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public TriggerFragment() {
        }

        public static TriggerFragment newInstance(int sectionNumber) {
            TriggerFragment fragment = new TriggerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_pref_3triggers, container, false);
            return rootView;
        }
    }

    // Methods connected to clickables goes here. Some are to be combined to handle multiple click events.

    public static class CaptureFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public CaptureFragment() {
        }

        public static CaptureFragment newInstance(int sectionNumber) {
            CaptureFragment fragment = new CaptureFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_pref_4capture, container, false);
            return rootView;
        }
    }

    public static class CommsFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public CommsFragment() {
        }

        public static CommsFragment newInstance(int sectionNumber) {
            CommsFragment fragment = new CommsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_pref_5comms, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new AlertsFragment();
                case 1:
                    return new LocationFragment();
                case 2:
                    return new TriggerFragment();
                case 3:
                    return new CaptureFragment();
                case 4:
                    return new CommsFragment();
            }
            // Below line to be replaced with placeholder fragment if none has been returned yet.
            return null;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
            }
            return null;
        }
    }
}

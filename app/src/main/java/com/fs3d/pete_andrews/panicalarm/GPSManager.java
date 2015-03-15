package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by peteb_000 on 14/03/2015.
 * This class manages all GPS related functions including threaded functions such as retrieving
 * street addresses and the like (declared at the end of this class).
 */
public class GPSManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // Variable and constant declarations.
    private static final String TAG = "GPSManager";
    private Context ctxt;
    private int interval, dist, accuracy;
    private double longitude, latitude;
    private GoogleApiClient mGAPIClient;
    private LocationManager locMgr;
    private Handler xHndl;

    // Constructor calls
    public GPSManager() {
        // Empty declaration. Declare default values.
        this.interval = 10000;
        this.dist = 5;
        this.accuracy = 1;
    }

    public GPSManager(Context ctxt) {
        // Context-only declaration. Declare default values.
        this.ctxt = ctxt;
        this.interval = 10000;
        this.dist = 5;
        this.accuracy = 1;
    }

    // The following methods are mandatory for the purposes of this service and deal with
    // connection-related tasks.

    @Override
    public void onConnected(Bundle bundle) {
        // Connection established.
        Location lastLoc = LocationServices.FusedLocationApi.getLastLocation(mGAPIClient);
        latitude = lastLoc.getLatitude();
        longitude = lastLoc.getLongitude();
        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longitude);
        Log.d(TAG, "Initial Play Store Connection. Last known GPS Coordinates: " + lon + "/" + lat);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Location changed. Update needed to app values.
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d(TAG, "Update received.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Connection failed.
        Log.d(TAG, "Connection failure: " + connectionResult.toString());
    }

    @Override
    public void onProviderEnabled(String provider) {
        // GPS Provider enabled. Setup initial variables.
        Log.d(TAG, "Provider " + provider + " has now been enabled. It is now possible to connect.");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Disabled provider.
        Log.d(TAG, "Provider " + provider + " is disabled. No connection possible.");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Change of status call
        Log.d(TAG, "Provider " + provider + " status has changed.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Connection suspended.
        Log.d(TAG, "Connection " + String.valueOf(i) + " has been suspended.");
    }

    // The following methods are intended to be called from the primary service for the purposes of
    // initializing a GPS location listener and get a series of location updates based on a series of criteria.

    public double[] getLastKnownGPS() {
        return new double[]{longitude, latitude};
    }

    public void enableGPSUpdates(Handler mHndlr, String arg) {
        // This method is passed a handler so that it can send back GPS updates to wherever
        // it has been called from and the calling Activity's Handler can use the data sent.
        // Arguments: get_coords will send back the currently known Lon and Lat data once the GPS Manager is enabled.
        //            log_coords will save the GPS updates to a text file internal to the app. This will be used to periodically send location history to a designated contact.
        //            silent (a blank String will also suffice) will request enabling the GPS to update silently.
        this.xHndl = mHndlr;
    }
}

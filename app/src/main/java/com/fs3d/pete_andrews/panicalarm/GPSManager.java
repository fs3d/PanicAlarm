package com.fs3d.pete_andrews.panicalarm;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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
    private Context ctxt;
    private int interval, dist, accuracy;
    private GoogleApiClient mGAPIClient;
    private LocationManager locMgr;

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

    @Override
    public void onConnected(Bundle bundle) {
        // Connection established.
        Location lastLoc = LocationServices.FusedLocationApi.getLastLocation(mGAPIClient);
        String lat = String.valueOf(lastLoc.getLatitude());
        String lon = String.valueOf(lastLoc.getLongitude());
    }

    @Override
    public void onLocationChanged(Location location) {
        // Location changed. Update needed to app values.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Connection failed.
    }

    @Override
    public void onProviderEnabled(String provider) {
        // GPS Provider enabled. Setup initial variables.
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Disabled provider.
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Change of status call
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Connection suspended.
    }
}

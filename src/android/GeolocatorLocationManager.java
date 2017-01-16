package com.cgi.cordova.geolocator;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public final class GeolocatorLocationManager implements LocationListener {

    private static final String TAG = GeolocatorLocationManager.class.getSimpleName();

    private final Context mContext;
    GeolocatorListener mGeolocatorListener;

    // flag for GPS status
    public boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    Location mLocation; // location

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 seconds

    protected Timer timerTimeout = null;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GeolocatorLocationManager(Context context, GeolocatorListener geolocatorListener) {
        this.mContext = context;
        this.mGeolocatorListener = geolocatorListener;
    }

    /**
     * Function to get the user's current location
     * 
     * @return
     */
    public void getLocation(int timeout) {
        this.mLocation = null;

        startTimer(timeout);

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled == false && isNetworkEnabled == false) {
                // no network provider is enabled
                stopRequesting();
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startTimer(int timeInMs) {
        stopTimer();
        timerTimeout = new Timer();
        timerTimeout.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "getLocation timeout");
                stopRequesting();
            }
        }, timeInMs);
    }

    private void stopTimer() {
        if (timerTimeout != null) {
            timerTimeout.cancel();
            timerTimeout.purge();
            timerTimeout = null;
        }
    }

    private void stopRequesting() {
        if (timerTimeout != null) {
            stopTimer();
            if (locationManager != null) {
                locationManager.removeUpdates(GeolocatorLocationManager.this);
            }
            if (this.mLocation != null) {
                this.mGeolocatorListener.onGeolocationSuccess(this.mLocation);
            } else {
                this.mGeolocatorListener.onGeolocationError(-1);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged - provider: "+location.getProvider());
        this.mLocation = location;
        if (location.getProvider().equals("gps")) {
            stopRequesting();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
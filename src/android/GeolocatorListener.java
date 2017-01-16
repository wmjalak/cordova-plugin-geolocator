package com.cgi.cordova.geolocator;

import android.location.Location;

public interface GeolocatorListener {
    void onGeolocationSuccess(Location location);
    void onGeolocationError(int code);
}

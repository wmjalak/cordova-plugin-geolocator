package com.cgi.cordova.geolocator;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

import android.location.Location;
import android.util.Log;
import org.apache.cordova.CordovaPlugin;


public class GeolocatorPlugin extends CordovaPlugin implements GeolocatorListener {

    private static final String TAG = GeolocatorPlugin.class.getSimpleName();
    CallbackContext mCallbackContext;

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) {

        Log.i(TAG, "action: " + action);
        try {
            if (action.equals("getCurrentPosition")) {
                mCallbackContext = callbackContext;

                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getCurrentPosition(args);
                    }
                });

                return true;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
        return false;

    }

    private void getCurrentPosition(JSONArray args) {
        int timeout = 10000; // default timeout

        try {
            JSONObject obj = args.getJSONObject(0);

            if (obj.has("timeout")) {
                timeout = obj.optInt("timeout", timeout);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error", e);
        }

        Context context=this.cordova.getActivity().getApplicationContext();

        GeolocatorLocationManager locationManager = new GeolocatorLocationManager(context, this);
        locationManager.getLocation(timeout);

    }

    public void onGeolocationSuccess(Location location) {
        Log.i(TAG, "onGeolocationSuccess");
        JSONObject jsonObject = new JSONObject();
        JSONObject responseJSON = new JSONObject();
        try {
            jsonObject.put("accuracy", location.getAccuracy());
            jsonObject.put("latitude", location.getLatitude());
            jsonObject.put("longitude", location.getLongitude());
            responseJSON.put("coords", jsonObject);
            responseJSON.put("timestamp", location.getTime());
            responseJSON.put("provider", location.getProvider());
        } catch (JSONException e) {
            Log.e(TAG, "Error", e);
        }
        mCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, responseJSON));
    }

    public void onGeolocationError(int code) {
        Log.i(TAG, "onGeolocationError");
        mCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, code));

    }

}

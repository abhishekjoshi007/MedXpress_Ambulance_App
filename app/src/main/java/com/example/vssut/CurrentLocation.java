package com.example.vssut;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocation {

    public static int LOCATION_REQUEST_CODE = 10;

    public static Task<Pair<Boolean, LatLng>> getCurrentLocation(Activity activity) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = new SettingsClient(activity);
        return client.checkLocationSettings(builder.build())
                .continueWithTask(locationSettingsResponseTask -> {
                    if (locationSettingsResponseTask.isSuccessful() && locationSettingsResponseTask.getResult() != null) {
                        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
                                return Tasks.forResult(new Pair<>(false, null));
                            }
                        }
                        return locationProviderClient.getLastLocation().continueWith(locationTask -> {
                            if (locationTask.isSuccessful() && locationTask.getResult() != null) {
                                LatLng latLng = new LatLng(locationTask.getResult().getLatitude(), locationTask.getResult().getLongitude());
                                return new Pair<>(true, latLng);
                            } else return new Pair<>(false, null);
                        });
                    }
                    return Tasks.forResult(new Pair<>(false, null));
                });
    }

    public static Address getAddressFromGeocode(@NonNull LatLng latLng, Activity activity) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                return addressList.get(0);
            } else {
                Log.e("Geocode address", "no address found");
                return new Address(Locale.getDefault());
            }
        } catch (IOException e) {
            Log.e("FetchAddress", "Service not available", e);
            return new Address(Locale.getDefault());
        } catch (IllegalArgumentException e) {
            Log.e("FetchAddress", "Invalid lat, long:" + latLng.latitude + "," + latLng.longitude, e);
            return new Address(Locale.getDefault());
        }
    }
}

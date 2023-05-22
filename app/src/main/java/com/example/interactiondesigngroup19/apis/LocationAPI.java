package com.example.interactiondesigngroup19.apis;

import android.content.pm.PackageManager;
import android.location.Location;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationAPI {
    private static FusedLocationProviderClient locationClient;
    private static ActivityResultLauncher<String[]> requestPermissionLauncher;

    public static void ensureLocationClientAndRequester(FragmentActivity activity) {
        if (locationClient == null) {
            locationClient = LocationServices.getFusedLocationProviderClient(activity);
        }
        if (requestPermissionLauncher == null) {
            requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
            });
        }
    }

    public static void requestLocation(FragmentActivity activity, OnSuccessListener<Location> success, OnFailureListener failure) {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
        if (locationClient == null) {
            failure.onFailure(new NullPointerException());
        } else {
            locationClient.getCurrentLocation(100, null).addOnSuccessListener(success).addOnFailureListener(failure);
        }
    }
}

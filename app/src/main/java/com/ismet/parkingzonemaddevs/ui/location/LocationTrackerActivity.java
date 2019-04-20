package com.ismet.parkingzonemaddevs.ui.location;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;


public class LocationTrackerActivity extends Service implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String LOG_SERVICE_UPDATE = "LOCATION_SERVICE";

    private static final int UPDATE_INTERVAL = 10000;
    private static final int FASTEST_UPDATE_INTERVAL = 5000;

    private LocationRequest mLocationRequest = null;

    private GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    public void onCreate() {
        super.onCreate();
        connectGoogleApiClient();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void connectGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdate();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_SERVICE_UPDATE, "CONNECTED");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_SERVICE_UPDATE, "CONNECTION_SUSPENDED " + i);
    }

    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent("LOCATION CHANGED");
        intent.putExtra("location", location);
        sendBroadcast(intent);
        Log.i(LOG_SERVICE_UPDATE, "LOCATION CHANGED" + location);
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdate() {
        setLocationRequestParams();

        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void setLocationRequestParams() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            onLocationChanged(locationResult.getLastLocation());
        }
    };

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null) {
            stopRequestingUpdates();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void stopRequestingUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSIONS) {
            turnDutyStatus(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    protected void checkLocationPermission() {
        if (PermissionUtils.checkLocationServices(this)) {
            turnDutyStatus(PermissionUtils.checkLocationPermissions(this));
            return;
        }

        showServicesDisabledDialog();
    }

    @SuppressLint("MissingPermission")
    private void turnDutyStatus(boolean isAllowed) {
        onDutyStatusChanged(isAllowed);
    }*/
}

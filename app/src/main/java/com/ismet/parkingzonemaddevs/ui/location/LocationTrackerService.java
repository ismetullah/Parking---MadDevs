package com.ismet.parkingzonemaddevs.ui.location;

import android.annotation.SuppressLint;
import android.app.Notification;
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
import com.google.android.gms.maps.model.LatLng;
import com.ismet.parkingzonemaddevs.R;
import com.ismet.parkingzonemaddevs.data.DataManager;
import com.ismet.parkingzonemaddevs.data.model.CurrentParking;
import com.ismet.parkingzonemaddevs.data.model.ParkingZone;
import com.ismet.parkingzonemaddevs.utils.NotificationHelper;
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider;
import dagger.android.AndroidInjection;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import java.io.Serializable;

import static com.ismet.parkingzonemaddevs.ui.location.LocationNotificationReceiver.BROADCAST_DATA;
import static com.ismet.parkingzonemaddevs.ui.location.LocationNotificationReceiver.BROADCAST_REQUEST;


public class LocationTrackerService extends Service implements GoogleApiClient.ConnectionCallbacks, LocationListener, LocationTrackerContract.View {
    @Inject
    DataManager dataManager;
    @Inject
    SchedulerProvider schedulerProvider;
    private LocationTrackerPresenter presenter;

    //For NOTIFICATION
    private static final int NOTIFICATION_ID_TO_STOP_PARKING = 1100;
    private static final int NOTIFICATION_ID_TO_PARK = 1101;
    public static final String REQUEST_TO_STOP_PARKING = "ask_to_stop_parking";
    public static final String REQUEST_TO_PARK = "ask_to_park";
    public static final String BROADCAST_TO_STOP_PARKING = "broadcast_to_stop_parking";
    public static final String BROADCAST_TO_PARK = "broadcast_to_park";

    private NotificationHelper notificationHelper;

    private static final String LOG_SERVICE_UPDATE = "LOCATION_SERVICE";

    private static final int UPDATE_INTERVAL = 10000;
    private static final int FASTEST_UPDATE_INTERVAL = 5000;

    private LocationRequest mLocationRequest = null;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper = new NotificationHelper(this);
        performDependencyInjection();
        initPresenter();
        connectGoogleApiClient();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void initPresenter() {
        presenter = new LocationTrackerPresenter(dataManager, schedulerProvider, this);
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
        presenter.onLocationChanged(new LatLng(location.getLatitude(), location.getLongitude()));
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

    private void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    @Override
    public void notifyToPark(@NotNull ParkingZone parkingZone) {
        Notification.Builder builder = notificationHelper.getNotificationToPark(
                getString(R.string.ask_to_park),
                parkingZone.getName(),
                REQUEST_TO_PARK,
                parkingZone
        );
        notificationHelper.notify(NOTIFICATION_ID_TO_PARK, builder);
        sendNotificationBroadcast(BROADCAST_TO_PARK, parkingZone);
    }

    @Override
    public void notifyToStopParking(@NotNull CurrentParking currentParking) {
        Notification.Builder builder = notificationHelper.getNotificationToStopParking(
                getString(R.string.ask_to_stop_parking),
                currentParking.getParkingZoneName(),
                REQUEST_TO_STOP_PARKING,
                currentParking
        );
        notificationHelper.notify(NOTIFICATION_ID_TO_STOP_PARKING, builder);
        sendNotificationBroadcast(BROADCAST_TO_STOP_PARKING, currentParking);
    }

    private void sendNotificationBroadcast(String request, Serializable data) {
        Intent intent = new Intent("NOTIFICATION RECEIVED");
        intent.putExtra(BROADCAST_REQUEST, request);
        intent.putExtra(BROADCAST_DATA, data);
        sendBroadcast(intent);
    }
}

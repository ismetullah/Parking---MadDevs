package com.ismet.parkingzonemaddevs.ui.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ismet.parkingzonemaddevs.data.model.CurrentParking;
import com.ismet.parkingzonemaddevs.data.model.ParkingZone;

import static com.ismet.parkingzonemaddevs.ui.location.LocationTrackerService.BROADCAST_TO_PARK;
import static com.ismet.parkingzonemaddevs.ui.location.LocationTrackerService.BROADCAST_TO_STOP_PARKING;

public class LocationNotificationReceiver extends BroadcastReceiver {
    public static final String BROADCAST_DATA = "BROADCAST DATA";
    public static final String BROADCAST_REQUEST = "BROADCAST REQUEST";

    private OnNotificationReceived listener = null;

    public void setListener(OnNotificationReceived listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction().equals("NOTIFICATION RECEIVED")) {
            String request = intent.getStringExtra(BROADCAST_REQUEST);
            if (request == null) return;
            if (request.equals(BROADCAST_TO_PARK)) {
                ParkingZone zone = (ParkingZone) intent.getSerializableExtra(BROADCAST_DATA);
                if (zone != null)
                    listener.askToPark(zone);
            }
            if (request.equals(BROADCAST_TO_STOP_PARKING)) {
                CurrentParking currParking = (CurrentParking) intent.getSerializableExtra(BROADCAST_DATA);
                if (currParking != null)
                    listener.askToStopParking(currParking);
            }
        }
    }

    public interface OnNotificationReceived {
        void askToPark(ParkingZone zone);

        void askToStopParking(CurrentParking currParking);
    }
}

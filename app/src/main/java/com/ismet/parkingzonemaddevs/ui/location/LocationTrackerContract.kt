package com.ismet.parkingzonemaddevs.ui.location

import com.google.android.gms.maps.model.LatLng
import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.ParkingZone

interface LocationTrackerContract {
    interface View {
        fun notifyToStopParking(currentParking: CurrentParking)
        fun notifyToPark(parkingZone: ParkingZone)
    }

    interface Presenter {
        fun onLocationChanged(latLng: LatLng?)
    }
}
package com.ismet.parkingzonemaddevs.ui.parkingzone

import androidx.databinding.ObservableField
import com.ismet.parkingzonemaddevs.data.model.ParkingZone


class ParkingZonesItemViewModel(private val zone: ParkingZone, val mListener: ItemViewModelListener) {

    val parkingZoneName: ObservableField<String> = ObservableField("Zone: " + zone.name)

    val capacity: ObservableField<String> = ObservableField("Capacity: " + zone.capacity)

    val parkedCarNumber: ObservableField<String> = ObservableField("Parked car number: " + zone.parkedCarNumber)

    fun onItemClick() {
        mListener.onItemClick(zone)
    }

    interface ItemViewModelListener {
        fun onItemClick(zone: ParkingZone)
    }
}

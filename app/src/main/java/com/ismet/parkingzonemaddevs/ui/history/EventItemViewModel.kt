package com.ismet.parkingzonemaddevs.ui.history

import androidx.databinding.ObservableField
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.utils.CommonUtils.timeToString


class EventItemViewModel(private val event: ParkingEvent) {

    val parkingZoneName: ObservableField<String> = ObservableField("Zone: " + event.parkingZoneName)

    val entryTime: ObservableField<String> = ObservableField("Entry time: " + timeToString(event.entryTime))

    val exitTime: ObservableField<String> = ObservableField("Exit time: " + timeToString(event.exitTime))
}

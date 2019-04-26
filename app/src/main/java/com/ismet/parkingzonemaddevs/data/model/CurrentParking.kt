package com.ismet.parkingzonemaddevs.data.model

import java.io.Serializable

data class CurrentParking(var isParking: Boolean = false, var canAskToStopPark: Boolean = true):Serializable {

    var entryTime: Long = 0
    var parkingZoneName: String? = null
}
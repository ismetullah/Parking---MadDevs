package com.ismet.parkingzonemaddevs.data.model

data class CurrentParking(var isParking: Boolean = false, var canAskToUnPark: Boolean = true) {

    var entryTime: Long = 0
    var parkingZoneName: String? = null
}
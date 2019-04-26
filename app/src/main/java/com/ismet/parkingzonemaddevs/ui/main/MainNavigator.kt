package com.ismet.parkingzonemaddevs.ui.main

import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.ParkingZone

interface MainNavigator {
    fun onTrackLocationEnabled()
    fun onTrackLocationDisabled()
    fun onSuccess(id: Int)
    fun handleThrowable(m: String)
    fun showParkingZoneFragment(parkingZone: ParkingZone)
    fun askToStopParking(currentParking: CurrentParking)
}
package com.ismet.parkingzonemaddevs.ui.main

import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.ParkingZone

interface MainNavigator {
    fun handleThrowable(m: String)
    fun showParkingZoneFragment(parkingZone: ParkingZone)
    fun askToPark(parkingZone: ParkingZone)
    fun askToStopParking(currentParking: CurrentParking)
}
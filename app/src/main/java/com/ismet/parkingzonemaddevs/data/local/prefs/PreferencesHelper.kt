package com.ismet.parkingzonemaddevs.data.local.prefs

import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.LastEnteredZone

interface PreferencesHelper {
    fun getCurrentParking(): CurrentParking?
    fun saveCurrentParking(currentParking: CurrentParking)

    fun getCanAskToPark(): Boolean
    fun saveCanAskToPark(b: Boolean)

    fun saveLastEnteredZone(lastEnteredZone: LastEnteredZone)
    fun getLastEnteredZone():LastEnteredZone?
}
package com.ismet.parkingzonemaddevs.data


import com.ismet.parkingzonemaddevs.data.local.db.DbHelper
import com.ismet.parkingzonemaddevs.data.local.prefs.PreferencesHelper
import io.reactivex.Observable

interface DataManager : DbHelper, PreferencesHelper {
    fun seedParkingZones(): Observable<Boolean>
}

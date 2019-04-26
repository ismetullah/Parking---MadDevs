package com.ismet.parkingzonemaddevs.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ismet.parkingzonemaddevs.data.local.db.DbHelper
import com.ismet.parkingzonemaddevs.data.local.prefs.PreferencesHelper
import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.LastEnteredZone
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.utils.AppConstants
import com.ismet.parkingzonemaddevs.utils.CommonUtils
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(
    private val context: Context,
    private val dpHelper: DbHelper,
    private val preferenceHelper: PreferencesHelper
) : DataManager {
    override fun getIsTrackLocationEnabled(): Boolean {
        return preferenceHelper.getIsTrackLocationEnabled()
    }

    override fun saveIsTrackLocationEnabled(b: Boolean) {
        preferenceHelper.saveIsTrackLocationEnabled(b)
    }

    override fun getCurrentParking(): CurrentParking {
        return preferenceHelper.getCurrentParking()
    }

    override fun saveCurrentParking(currentParking: CurrentParking) {
        preferenceHelper.saveCurrentParking(currentParking)
    }

    override fun getCanAskToPark(): Boolean {
        return preferenceHelper.getCanAskToPark()
    }

    override fun saveCanAskToPark(b: Boolean) {
        preferenceHelper.saveCanAskToPark(b)
    }

    override fun removeEventById(event: ParkingEvent): Observable<Boolean> {
        return dpHelper.removeEventById(event)
    }

    override fun getLastEnteredZone(): LastEnteredZone? {
        return preferenceHelper.getLastEnteredZone()
    }

    override fun saveLastEnteredZone(lastEnteredZone: LastEnteredZone) {
        preferenceHelper.saveLastEnteredZone(lastEnteredZone)
    }

    override fun seedParkingZones(): Observable<Boolean> {
        return dpHelper.isParkingZonesEmpty()
            .concatMap { isEmpty ->
                if (isEmpty) {
                    val type = object : TypeToken<List<ParkingZone>>() {}.type
                    val parkingZones = Gson().fromJson<List<ParkingZone>>(
                        CommonUtils.loadJSONFromAsset(context, AppConstants.SEED_PARKING_ZONES),
                        type
                    )
                    return@concatMap saveParkingZoneList(parkingZones)
                }
                return@concatMap Observable.just(false)
            }
    }

    override fun getAllParkingEvents(): Observable<List<ParkingEvent>> {
        return dpHelper.getAllParkingEvents()
    }

    override fun getAllParkingZones(): Observable<List<ParkingZone>> {
        return dpHelper.getAllParkingZones()
    }

    override fun isParkingEventsEmpty(): Observable<Boolean> {
        return dpHelper.isParkingEventsEmpty()
    }

    override fun isParkingZonesEmpty(): Observable<Boolean> {
        return dpHelper.isParkingZonesEmpty()
    }

    override fun saveParkingEvent(parkingEvent: ParkingEvent): Observable<Boolean> {
        return dpHelper.saveParkingEvent(parkingEvent)
    }

    override fun saveParkingZone(parkingZone: ParkingZone): Observable<Boolean> {
        return dpHelper.saveParkingZone(parkingZone)
    }

    override fun saveParkingZoneList(parkingZoneList: List<ParkingZone>): Observable<Boolean> {
        return dpHelper.saveParkingZoneList(parkingZoneList)
    }
}
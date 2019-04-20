package com.ismet.parkingzonemaddevs.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.model.*
import com.ismet.parkingzonemaddevs.ui.base.BaseViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider
import java.util.*

class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MainNavigator>(
        dataManager, schedulerProvider
    ), ZoneFinder.OnZoneFoundListener {
    private var currentParking: MutableLiveData<CurrentParking> = MutableLiveData()
    private var parkingZones: MutableLiveData<List<ParkingZone>> = MutableLiveData()
    private var isStopParkingShown: MutableLiveData<Boolean> = MutableLiveData()
    private var parkingZone: MutableLiveData<ParkingZone> = MutableLiveData()

    init {
        isStopParkingShown.value = false
        loadCurrentParking()
        loadParkingZones()
    }

    fun loadCurrentParking() {
        currentParking.value = getDataManager().getCurrentParking()
        if (currentParking.value != null)
            isStopParkingShown.value = currentParking.value?.isParking
    }

    fun loadParkingZones() {
        getCompositeDisposable().add(
            getDataManager()
                .getAllParkingZones()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe({ z -> if (z != null) parkingZones.value = z },
                    { t -> getNavigator()?.handleThrowable(t.message!!) })
        )
    }

    fun findZoneByPoint(ltLng: LatLng) {
        ZoneFinder(this, ltLng, parkingZones.value).execute()
    }

    override fun onZoneFound(parkingZone: ParkingZone) {
        getNavigator()?.showParkingZoneFragment(parkingZone)
    }

    fun getParkingZones(): LiveData<List<ParkingZone>> {
        return parkingZones
    }

    fun onLocationChanged(location: Location?) {
        val lastEnteredZone = getDataManager().getLastEnteredZone()
        val currentParking = getDataManager().getCurrentParking()
        val canAskToPark = getDataManager().getCanAskToPark()
        checkLastEnteredZone(lastEnteredZone, currentParking, canAskToPark, location)
    }

    private fun checkLastEnteredZone(
        lastEnteredZone: LastEnteredZone?,
        currentParking: CurrentParking?,
        canAskToPark: Boolean,
        location: Location?
    ) {
        if (location == null) return

        val parkingZone = getParkingZoneByLocation(location)
        this.parkingZone.value = parkingZone
        if (parkingZone == null) {
            if (currentParking?.canAskToUnPark!! && currentParking.isParking) {
                getNavigator()?.askToStopParking(currentParking)
                currentParking.canAskToUnPark = false
                getDataManager().saveCurrentParking(currentParking)
                this.currentParking.value = currentParking
            }
            return
        }
        if (canAskToPark)
            checkForParking(lastEnteredZone, parkingZone)
    }

    private fun checkForParking(
        lastEnteredZone: LastEnteredZone?,
        parkingZone: ParkingZone
    ) {
        if (lastEnteredZone == null) {
            getDataManager().saveLastEnteredZone(
                LastEnteredZone(
                    Date().time,
                    parkingZone.name
                )
            )
            Log.e("_____CHECK____", "SAVED ZONE")
            return
        }

        if (lastEnteredZone.parkingZoneName != parkingZone.name) {
            getDataManager().saveLastEnteredZone(
                LastEnteredZone(
                    Date().time,
                    parkingZone.name
                )
            )
            getDataManager().saveCanAskToPark(true)
            Log.e("_____CHECK____", "CHANGED PARK ZONE")
        }

        if (hasBeenThreeMinutes(lastEnteredZone.entryTime)) {
            Log.e("_____CHECK____", "ASk to park")
            getNavigator()?.askToPark(parkingZone)
            getDataManager().saveCanAskToPark(false)
        }
    }

    private fun hasBeenThreeMinutes(entryTime: Long): Boolean {
        val diff = Date().time - entryTime
        val min = diff / 1000
        return min == 20L || min > 20L
    }

    private fun getParkingZoneByLocation(location: Location): ParkingZone? {
        val latLng = LatLng(location.latitude, location.longitude)
        parkingZones.value?.forEach {
            if (PolyUtil.containsLocation(latLng, toLatLngList(it.polygonCorners), true))
                return it
        }
        return null
    }

    private fun toLatLngList(polygonCorners: ArrayList<Corner>): MutableList<LatLng>? {
        val latLngs: MutableList<LatLng> = ArrayList()
        polygonCorners.forEach {
            latLngs.add(LatLng(it.lat, it.lng))
        }
        return latLngs
    }

    fun onClickStopParking() {
        val currPark = getDataManager().getCurrentParking()
        if (currPark == null) {
            isStopParkingShown.value = false
            return
        }
        if (!currPark.isParking) {
            isStopParkingShown.value = false
            return
        }
        getNavigator()?.askToStopParking(currPark)
    }

    fun stopParking(currentParking: CurrentParking) {
        isStopParkingShown.value = false
        val parkingEvent = ParkingEvent(currentParking.entryTime, Date().time, currentParking.parkingZoneName!!)
        val currPar = CurrentParking(false, false)
        getDataManager().saveCurrentParking(currPar)
        this.currentParking.value = currPar
        getCompositeDisposable().add(
            getDataManager()
                .saveParkingEvent(parkingEvent)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe({ b -> },
                    { t -> getNavigator()?.handleThrowable(t.message!!) })
        )
    }

    fun getIsStopParkingShown(): LiveData<Boolean> {
        return isStopParkingShown
    }

    fun getParkingZone(): LiveData<ParkingZone>{
        return parkingZone
    }
}

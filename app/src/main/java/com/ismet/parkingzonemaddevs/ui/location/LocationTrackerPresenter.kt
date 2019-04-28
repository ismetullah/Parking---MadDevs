package com.ismet.parkingzonemaddevs.ui.location

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.model.Corner
import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.LastEnteredZone
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.utils.CommonUtils.hasBeenThreeMinutes
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class LocationTrackerPresenter(
    private var dataManager: DataManager,
    private var schedulerProvider: SchedulerProvider
) :
    LocationTrackerContract.Presenter {
    private var parkingZones: List<ParkingZone> = ArrayList()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var view: LocationTrackerContract.View? = null

    init {
        loadParkingZones()
    }

    fun bindView(view: LocationTrackerContract.View) {
        this.view = view
    }

    fun getParkingZones(): List<ParkingZone> {
        return parkingZones
    }

    private fun loadParkingZones() {
        compositeDisposable.add(
            dataManager
                .getAllParkingZones()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .subscribe { z -> if (z != null) parkingZones = z }
        )
    }

    override fun onLocationChanged(latLng: LatLng?) {
        val lastEnteredZone = dataManager.getLastEnteredZone()
        val currentParking = dataManager.getCurrentParking()
        val canAskToPark = dataManager.getCanAskToPark()

        // if location and parkingZones are null, stop
        if (parkingZones.isNullOrEmpty()) return
        if (latLng == null) return

        //
        val parkingZone = getParkingZoneByLocation(latLng)
        // if left parking zone and currently parking, ask to stop parking
        if (parkingZone == null) {
            checkToStopParking(currentParking)
            return
        }
        if (canAskToPark)
            checkForParking(lastEnteredZone, parkingZone)
    }

    private fun checkToStopParking(currentParking: CurrentParking) {
        //if currently parking, then ask to stop parking
        if (currentParking.canAskToStopPark && currentParking.isParking) {
            currentParking.canAskToStopPark = false
            dataManager.saveCurrentParking(currentParking)
            view?.notifyToStopParking(currentParking)
        }
    }

    private fun checkForParking(
        lastEnteredZone: LastEnteredZone?,
        parkingZone: ParkingZone
    ) {
        //if last entered zone is empty, add new one
        if (lastEnteredZone == null) {
            dataManager.saveLastEnteredZone(
                LastEnteredZone(
                    Date().time,
                    parkingZone.name
                )
            )
            Log.e("_____CHECK____", "SAVED ZONE")
            return
        }

        // if last entered zone is not the same as this zone,
        // add new last entered zone
        if (lastEnteredZone.parkingZoneName != parkingZone.name) {
            dataManager.saveLastEnteredZone(
                LastEnteredZone(
                    Date().time,
                    parkingZone.name
                )
            )
            dataManager.saveCanAskToPark(true)
            Log.e("_____CHECK____", "CHANGED PARK ZONE")
        }

        if (hasBeenThreeMinutes(lastEnteredZone.entryTime)) {
            Log.e("_____CHECK____", "ASk to park")
            view?.notifyToPark(parkingZone)
            dataManager.saveCanAskToPark(false)
        }
    }

    private fun getParkingZoneByLocation(latLng: LatLng): ParkingZone? {
        parkingZones.forEach {
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

}
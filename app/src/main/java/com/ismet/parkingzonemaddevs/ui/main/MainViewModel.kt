package com.ismet.parkingzonemaddevs.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
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

    init {
        isStopParkingShown.value = false
        loadCurrentParking()
        loadParkingZones()
    }

    fun loadCurrentParking() {
        currentParking.value = getDataManager().getCurrentParking()
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

    fun onClickStopParking() {
        val currPark = getDataManager().getCurrentParking()

        if (!currPark.isParking) {
            isStopParkingShown.value = false
            return
        }
        getNavigator()?.askToStopParking(currPark)
    }

    fun stopParking(currentParking: CurrentParking) {
        val parkingEvent = ParkingEvent(currentParking.entryTime, Date().time, currentParking.parkingZoneName!!)
        val currPar = CurrentParking(false, false)
        getDataManager().saveCurrentParking(currPar)
        getDataManager().saveCanAskToPark(true)
        getCompositeDisposable().add(
            getDataManager()
                .saveParkingEvent(parkingEvent)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe({ b -> if (b) getNavigator()?.onSuccess(R.string.stopped_parking) },
                    { t -> getNavigator()?.handleThrowable(t.message!!) })
        )
    }

    fun startParking(parkingZone: ParkingZone) {
        var currentParking = getDataManager().getCurrentParking()
        if (currentParking.isParking) {
            getNavigator()?.onSuccess(R.string.stop_parking_first)
            return
        }
        currentParking = CurrentParking(true, true)
        currentParking.entryTime = Date().time
        currentParking.parkingZoneName = parkingZone.name
        getDataManager()
            .saveCurrentParking(currentParking)
        getNavigator()?.onSuccess(R.string.started_parking)
    }

    fun getIsStopParkingShown(): LiveData<Boolean> {
        return isStopParkingShown
    }

    fun checkIsLocationTrackEnabled(){
        val result = getDataManager().getIsTrackLocationEnabled()
        if (result)
            getNavigator()?.onTrackLocationEnabled()
        else
            getNavigator()?.onTrackLocationDisabled()
    }

}

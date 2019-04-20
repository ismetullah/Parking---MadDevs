package com.ismet.parkingzonemaddevs.ui.parkingzone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.ui.base.BaseViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider


class ParkingZonesViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ParkingZonesNavigator>(dataManager, schedulerProvider) {

    private var parkingZones: MutableLiveData<List<ParkingZone>> = MutableLiveData()

    init {
        fetchEvents()
    }

    fun fetchEvents(){
        setIsLoading(true)
        getCompositeDisposable().add(getDataManager()
            .getAllParkingZones()
            .subscribeOn(getSchedulerProvider().io())
            .observeOn(getSchedulerProvider().ui())
            .subscribe(
                { response ->
                    setIsLoading(false)
                    if (!response.isNullOrEmpty()) parkingZones.value = response
                },
                { t ->
                    setIsLoading(false)
                    getNavigator()?.handleError(t.message!!)
                }
            ))
    }

    fun getParkingZones():LiveData<List<ParkingZone>>{
        return parkingZones
    }
}
package com.ismet.parkingzonemaddevs.ui.parkingzone.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.ui.base.BaseViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider

class ParkingZoneFragmentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ParkingZoneFragmentNavigator>(dataManager, schedulerProvider) {

    private var parkingZoneName: MutableLiveData<String> = MutableLiveData()
    private var parkingZoneCapacity: MutableLiveData<String> = MutableLiveData()
    private var parkingZoneParkedCar: MutableLiveData<String> = MutableLiveData()

    fun setData(zone: ParkingZone?) {
        if (zone == null) {
            getNavigator()?.handleThrowable(R.string.failed_to_load)
            return
        }
        parkingZoneName.value = "Name: " + zone.name
        parkingZoneCapacity.value = "Capacity: " + zone.capacity
        parkingZoneParkedCar.value = "Parked car number: " + zone.parkedCarNumber
    }

    fun onNavBackClick() {
        getNavigator()?.goBack()
    }

    fun getParkingZoneName(): LiveData<String> {
        return parkingZoneName
    }

    fun getParkingZoneCapacity(): LiveData<String> {
        return parkingZoneCapacity
    }

    fun getParkingZoneParkedCar(): LiveData<String> {
        return parkingZoneParkedCar
    }

    fun onParkHereClick(){
        getNavigator()?.parkHere()
    }
}

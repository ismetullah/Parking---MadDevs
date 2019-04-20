package com.ismet.parkingzonemaddevs.ui.park

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.ui.base.BaseViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider
import java.util.*

class ParkViewHolder(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ParkNavigator>(dataManager, schedulerProvider) {

    private var parkingZone: ParkingZone? = null
    private var parkingZoneName: MutableLiveData<String> = MutableLiveData()

    fun setData(zone: ParkingZone) {
        parkingZone = zone
        parkingZoneName.value = "Name: " + zone.name
    }

    fun onCancelClick() {
        getNavigator()?.onCancel()
    }

    fun onSubmitClick() {
        if (parkingZone == null) {
            getNavigator()?.onSubmit()
            return
        }
        var currentParking = getDataManager().getCurrentParking()
        if (currentParking != null && currentParking.isParking) {
            return
        }
        currentParking = CurrentParking(true, true)
        currentParking.entryTime = Date().time
        currentParking.parkingZoneName = parkingZone!!.name
        getDataManager()
            .saveCurrentParking(currentParking)
        getNavigator()?.onSubmit()
    }

    fun getParkingZoneName(): LiveData<String> {
        return parkingZoneName
    }
}

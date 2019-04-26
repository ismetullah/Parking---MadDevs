package com.ismet.parkingzonemaddevs.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.ui.base.BaseViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider

class SettingsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SettingsNavigator>(dataManager, schedulerProvider) {
    private val isMyLocationEnabled: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadMyLocationEnabled()
    }

    private fun loadMyLocationEnabled() {
        val result = getDataManager().getIsTrackLocationEnabled()
        isMyLocationEnabled.value = result
    }

    fun onCheckedChanged(b: Boolean) {
        getDataManager().saveIsTrackLocationEnabled(b)
        isMyLocationEnabled.value = b
    }

    fun getIsMyLocationEnabled(): LiveData<Boolean> {
        return isMyLocationEnabled
    }

}
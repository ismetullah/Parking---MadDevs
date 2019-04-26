package com.ismet.parkingzonemaddevs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.ui.history.HistoryViewModel
import com.ismet.parkingzonemaddevs.ui.main.MainViewModel
import com.ismet.parkingzonemaddevs.ui.parkingzone.ParkingZonesViewModel
import com.ismet.parkingzonemaddevs.ui.parkingzone.fragment.ParkingZoneFragmentViewModel
import com.ismet.parkingzonemaddevs.ui.settings.SettingsViewModel
import com.ismet.parkingzonemaddevs.ui.splash.SplashViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject
constructor(
    private val dataManager: DataManager,
    private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingZonesViewModel::class.java)) {
            return ParkingZonesViewModel(dataManager, schedulerProvider) as T
        } else if (modelClass.isAssignableFrom(ParkingZoneFragmentViewModel::class.java)) {
            return ParkingZoneFragmentViewModel(dataManager, schedulerProvider) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataManager, schedulerProvider) as T
        } else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(dataManager, schedulerProvider) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(dataManager, schedulerProvider) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(dataManager, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
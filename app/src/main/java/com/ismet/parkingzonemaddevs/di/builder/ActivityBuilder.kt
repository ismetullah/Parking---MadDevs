package com.ismet.parkingzonemaddevs.di.builder

import com.ismet.parkingzonemaddevs.ui.history.HistoryActivity
import com.ismet.parkingzonemaddevs.ui.history.HistoryModule
import com.ismet.parkingzonemaddevs.ui.location.LocationTrackerService
import com.ismet.parkingzonemaddevs.ui.main.MainActivity
import com.ismet.parkingzonemaddevs.ui.parkingzone.ParkingZonesActivity
import com.ismet.parkingzonemaddevs.ui.parkingzone.ParkingZonesModule
import com.ismet.parkingzonemaddevs.ui.parkingzone.fragment.ParkingZoneFragmentProvider
import com.ismet.parkingzonemaddevs.ui.settings.SettingsActivity
import com.ismet.parkingzonemaddevs.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [ParkingZoneFragmentProvider::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [HistoryModule::class])
    abstract fun bindHistoryActivity(): HistoryActivity

    @ContributesAndroidInjector(modules = [ParkingZonesModule::class])
    abstract fun bindParkingZonesActivity(): ParkingZonesActivity

    @ContributesAndroidInjector
    abstract fun bindSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun bindLocationTrackerService(): LocationTrackerService
}

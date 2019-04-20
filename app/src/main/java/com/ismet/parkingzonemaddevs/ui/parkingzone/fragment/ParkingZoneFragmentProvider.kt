package com.ismet.parkingzonemaddevs.ui.parkingzone.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ParkingZoneFragmentProvider {

    @ContributesAndroidInjector
    internal abstract fun provideParkingZoneFragmentFactory(): ParkingZoneFragment
}

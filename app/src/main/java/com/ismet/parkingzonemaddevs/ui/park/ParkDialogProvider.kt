package com.ismet.parkingzonemaddevs.ui.park

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ParkDialogProvider {

    @ContributesAndroidInjector
    abstract fun provideParkDialogFactory(): ParkDialog
}

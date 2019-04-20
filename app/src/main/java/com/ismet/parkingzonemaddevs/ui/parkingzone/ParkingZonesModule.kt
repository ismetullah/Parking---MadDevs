package com.ismet.parkingzonemaddevs.ui.parkingzone

import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import java.util.*

@Module
class ParkingZonesModule {

    @Provides
    fun provideParkingZonesAdapter(): ParkingZonesAdapter {
        return ParkingZonesAdapter(ArrayList())
    }

    @Provides
    fun provideLinearLayoutManager(activity: ParkingZonesActivity): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }
}

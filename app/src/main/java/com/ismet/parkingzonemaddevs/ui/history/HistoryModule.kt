package com.ismet.parkingzonemaddevs.ui.history

import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import java.util.*

@Module
class HistoryModule {

    @Provides
    fun provideHistoryAdapter(): HistoryAdapter {
        return HistoryAdapter(ArrayList())
    }

    @Provides
    fun provideLinearLayoutManager(activity: HistoryActivity): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }
}

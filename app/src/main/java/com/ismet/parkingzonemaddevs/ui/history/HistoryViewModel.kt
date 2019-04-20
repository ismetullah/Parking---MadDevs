package com.ismet.parkingzonemaddevs.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.ui.base.BaseViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider

class HistoryViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<HistoryNavigator>(dataManager, schedulerProvider) {

    private var eventList: MutableLiveData<List<ParkingEvent>> = MutableLiveData()

    init {
        fetchEvents()
    }

    fun fetchEvents() {
        setIsLoading(true)
        getCompositeDisposable().add(
            getDataManager()
                .getAllParkingEvents()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(
                    { eventResponse ->
                        setIsLoading(false)
                        if (!eventResponse.isNullOrEmpty()) eventList.value = eventResponse
                    },
                    { t ->
                        setIsLoading(false)
                        getNavigator()?.handleError(t.message!!)
                    })
        )
    }

    fun removeItem(event: ParkingEvent, pos: Int) {
        setIsLoading(true)
        getCompositeDisposable().add(
            getDataManager()
                .removeEventById(event)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(
                    {
                        setIsLoading(false)
                        getNavigator()?.removeEventFromRecView(pos)
                    },
                    { t ->
                        setIsLoading(false)
                        getNavigator()?.handleError(t.message!!)
                    })
        )
    }

    fun getEventList(): LiveData<List<ParkingEvent>> {
        return eventList
    }

}
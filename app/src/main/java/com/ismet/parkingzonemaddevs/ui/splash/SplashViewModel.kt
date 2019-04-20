package com.ismet.parkingzonemaddevs.ui.splash

import android.util.Log
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.ui.base.BaseViewModel
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider

class SplashViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SplashNavigator>(dataManager, schedulerProvider) {
    fun startSeeding() {
        getCompositeDisposable().add(
            getDataManager()
                .seedParkingZones()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(
                    { aBoolean -> getNavigator()?.openMainActivity() },
                    { throwable -> throwableFun(throwable) })
        )

    }

    private fun throwableFun(throwable: Throwable?) {
        Log.e("______________THROWABLE____", throwable?.message)
    }
}
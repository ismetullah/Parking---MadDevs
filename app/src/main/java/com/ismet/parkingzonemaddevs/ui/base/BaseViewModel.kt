package com.ismet.parkingzonemaddevs.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


abstract class BaseViewModel<N>(
    private val dataManager: DataManager,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private var isLoading = ObservableBoolean()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var navigator: WeakReference<N>? = null

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getCompositeDisposable(): CompositeDisposable {
        return compositeDisposable
    }

    fun getDataManager(): DataManager {
        return dataManager
    }

    fun getIsLoading(): ObservableBoolean {
        return isLoading
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }

    fun getNavigator(): N? {
        return navigator?.get()
    }

    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }

    fun getSchedulerProvider(): SchedulerProvider {
        return schedulerProvider
    }
}

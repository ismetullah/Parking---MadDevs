package com.ismet.parkingzonemaddevs.di.component

import android.app.Application
import com.ismet.parkingzonemaddevs.ParkingZoneApp
import com.ismet.parkingzonemaddevs.di.builder.ActivityBuilder
import com.ismet.parkingzonemaddevs.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class])
interface AppComponent {

    fun inject(app: ParkingZoneApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

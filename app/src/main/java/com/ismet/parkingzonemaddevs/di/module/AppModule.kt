package com.ismet.parkingzonemaddevs.di.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.data.AppDataManager
import com.ismet.parkingzonemaddevs.data.DataManager
import com.ismet.parkingzonemaddevs.data.local.db.AppDatabase
import com.ismet.parkingzonemaddevs.data.local.db.AppDbHelper
import com.ismet.parkingzonemaddevs.data.local.db.DbHelper
import com.ismet.parkingzonemaddevs.data.local.prefs.AppPreferencesHelper
import com.ismet.parkingzonemaddevs.data.local.prefs.PreferencesHelper
import com.ismet.parkingzonemaddevs.di.DatabaseInfo
import com.ismet.parkingzonemaddevs.di.PreferenceInfo
import com.ismet.parkingzonemaddevs.utils.AppConstants
import com.ismet.parkingzonemaddevs.utils.rx.AppSchedulerProvider
import com.ismet.parkingzonemaddevs.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return AppConstants.DB_NAME
    }

    @Provides
    @Singleton
    fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper {
        return appDbHelper
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @PreferenceInfo
    fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    fun provideViewModelProviderFactory(
        factory: ViewModelProviderFactory
    ): ViewModelProvider.NewInstanceFactory {
        return factory
    }
}
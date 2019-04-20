package com.ismet.parkingzonemaddevs.data.local.db

import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(private val mAppDatabase: AppDatabase) :
    DbHelper {
    override fun removeEventById(event: ParkingEvent): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            mAppDatabase.parkingEventDao().removeEventById(event)
            return@fromCallable true
        }
    }

    override fun getAllParkingEvents(): Observable<List<ParkingEvent>> {
        return Observable.fromCallable<List<ParkingEvent>> { mAppDatabase.parkingEventDao().loadAll() }
    }

    override fun getAllParkingZones(): Observable<List<ParkingZone>> {
        return Observable.fromCallable<List<ParkingZone>> { mAppDatabase.parkingZoneDao().loadAll() }
    }

    override fun isParkingEventsEmpty(): Observable<Boolean> {
        return Observable.fromCallable { mAppDatabase.parkingEventDao().loadAll().isEmpty() }
    }

    override fun isParkingZonesEmpty(): Observable<Boolean> {
        return Observable.fromCallable { mAppDatabase.parkingZoneDao().loadAll().isEmpty() }
    }

    override fun saveParkingEvent(parkingEvent: ParkingEvent): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.parkingEventDao().insert(parkingEvent)
            return@fromCallable true
        }
    }

    override fun saveParkingZone(parkingZone: ParkingZone): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.parkingZoneDao().insert(parkingZone)
            return@fromCallable true
        }
    }

    override fun saveParkingZoneList(parkingZoneList: List<ParkingZone>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.parkingZoneDao().insertAll(parkingZoneList)
            return@fromCallable true
        }
    }

}

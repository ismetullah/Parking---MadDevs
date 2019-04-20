package com.ismet.parkingzonemaddevs.data.local.db

import com.ismet.parkingzonemaddevs.data.model.LastEnteredZone
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import io.reactivex.Observable

interface DbHelper {

    //get methods
    fun getAllParkingEvents(): Observable<List<ParkingEvent>>

    fun getAllParkingZones(): Observable<List<ParkingZone>>

    //isEmpty methods
    fun isParkingEventsEmpty(): Observable<Boolean>

    fun isParkingZonesEmpty(): Observable<Boolean>

    //save (set) methods
    fun saveParkingEvent(parkingEvent: ParkingEvent): Observable<Boolean>

    fun saveParkingZone(parkingZone: ParkingZone): Observable<Boolean>

    fun saveParkingZoneList(parkingZoneList: List<ParkingZone>): Observable<Boolean>

    //remove methods
    fun removeEventById(event: ParkingEvent): Observable<Boolean>
}

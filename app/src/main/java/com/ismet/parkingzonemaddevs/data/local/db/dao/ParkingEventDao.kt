package com.ismet.parkingzonemaddevs.data.local.db.dao

import androidx.room.*
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent

@Dao
interface ParkingEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parkingEvent: ParkingEvent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(parkingEvents: List<ParkingEvent>)

    @Query("SELECT * FROM parking_events ORDER BY exit_time DESC")
    fun loadAll(): List<ParkingEvent>

    @Delete
    fun removeEventById(event: ParkingEvent)
}

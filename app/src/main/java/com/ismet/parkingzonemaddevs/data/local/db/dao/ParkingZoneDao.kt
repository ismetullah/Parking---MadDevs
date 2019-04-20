package com.ismet.parkingzonemaddevs.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ismet.parkingzonemaddevs.data.model.ParkingZone

@Dao
interface ParkingZoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parkingZone: ParkingZone)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(parkingZones: List<ParkingZone>)

    @Query("SELECT * FROM parking_zones")
    fun loadAll(): List<ParkingZone>
}

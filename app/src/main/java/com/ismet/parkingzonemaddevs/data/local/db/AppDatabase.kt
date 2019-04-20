package com.ismet.parkingzonemaddevs.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ismet.parkingzonemaddevs.data.local.db.dao.ParkingEventDao
import com.ismet.parkingzonemaddevs.data.local.db.dao.ParkingZoneDao
import com.ismet.parkingzonemaddevs.data.model.LastEnteredZone
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.utils.Converters

@Database(
    entities = [ParkingZone::class, ParkingEvent::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun parkingEventDao(): ParkingEventDao

    abstract fun parkingZoneDao(): ParkingZoneDao
}
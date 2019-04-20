package com.ismet.parkingzonemaddevs.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "parking_events"
)
data class ParkingEvent(
    @ColumnInfo(name = "entry_time")
    @SerializedName("entryTime")
    val entryTime: Long,
    @ColumnInfo(name = "exit_time")
    @SerializedName("exitTime")
    val exitTime: Long,
    @ColumnInfo(name = "parking_zone_name")
    @SerializedName("parkingZoneName")
    val parkingZoneName: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    @SerializedName("uid")
    var uid: Long = 0
}
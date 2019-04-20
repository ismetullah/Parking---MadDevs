package com.ismet.parkingzonemaddevs.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity(tableName = "parking_zones")
data class ParkingZone(
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "capacity")
    @SerializedName("capacity")
    val capacity: Long,
    @ColumnInfo(name = "parked_car_number")
    @SerializedName("parkedCarNumber")
    val parkedCarNumber: Long,
    @ColumnInfo(name = "color")
    @SerializedName("color")
    val color: String,
    @ColumnInfo(name = "polygon_corners")
    @SerializedName("polygonCorners")
    val polygonCorners: ArrayList<Corner>,
    @ColumnInfo(name = "photos")
    @SerializedName("photos")
    val photos: ArrayList<String>
): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid: Long = 0
    fun isFull(): Boolean {
        return capacity - parkedCarNumber <= 0
    }
}
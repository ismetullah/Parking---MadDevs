package com.ismet.parkingzonemaddevs.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Corner(
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("lat")
    val lat: Double
) : Serializable
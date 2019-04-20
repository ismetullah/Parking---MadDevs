package com.ismet.parkingzonemaddevs.ui.main

import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.ismet.parkingzonemaddevs.data.model.Corner
import com.ismet.parkingzonemaddevs.data.model.ParkingZone

class ZoneFinder(
    private val listener: OnZoneFoundListener,
    private val ltLng: LatLng,
    private val parkingZones: List<ParkingZone>?
) : AsyncTask<Void, Void, ParkingZone>() {

    override fun doInBackground(vararg p0: Void?): ParkingZone? {
        if (parkingZones.isNullOrEmpty())
            return null
        parkingZones.forEach {
            it.polygonCorners.forEach { c ->
                if (isCornerEqualToPoint(c, ltLng)) return it
            }
        }
        return null
    }

    override fun onPostExecute(result: ParkingZone?) {
        super.onPostExecute(result)
        if (result != null)
            listener.onZoneFound(result)
    }

    private fun isCornerEqualToPoint(c: Corner, ltLng: LatLng): Boolean {
        if (c.lat != ltLng.latitude)
            return false
        if (c.lng != ltLng.longitude)
            return false
        return true
    }

    interface OnZoneFoundListener {
        fun onZoneFound(parkingZone: ParkingZone)
    }
}
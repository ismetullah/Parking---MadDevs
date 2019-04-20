package com.ismet.parkingzonemaddevs.ui.main

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.ismet.parkingzonemaddevs.data.model.ParkingZone

class DrawPolygons(
    private val listener: OnPolygonsReadyListener,
    private val list: List<ParkingZone>
) : AsyncTask<Void, Void, ArrayList<PolygonOptions>>() {
    private val polygonOptionsList: ArrayList<PolygonOptions> = ArrayList()

    override fun doInBackground(vararg p0: Void?): ArrayList<PolygonOptions> {
        for (zone in list) {
            val polygonOptions = PolygonOptions()
            zone.polygonCorners.forEach { c ->
                polygonOptions.add(LatLng(c.lat, c.lng))
            }
            polygonOptions.fillColor(Color.parseColor(zone.color))
                .strokeWidth(1f)
                .clickable(true)
            polygonOptionsList.add(polygonOptions)
        }
        return polygonOptionsList
    }

    override fun onPostExecute(result: ArrayList<PolygonOptions>?) {
        super.onPostExecute(result)
        if (result != null)
            listener.onPolygonsReady(result)
    }

    interface OnPolygonsReadyListener {
        fun onPolygonsReady(polygonOptionsList: ArrayList<PolygonOptions>)
    }
}

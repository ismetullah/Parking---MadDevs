package com.ismet.parkingzonemaddevs.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.ismet.parkingzonemaddevs.R

object GoogleMapsUtils {
    /*
         * This method is used for GoogleMaps.
         * It checks if Google Play Services are available or not.
         * If Google Play Services are not available,
         * and the @finish parameter is true,
         * then it shows a dialog with a button.
         * When the button is pressed the activity finishes.
         */
    fun isGooglePlayServicesAvailable(activity: Activity): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                val dialog = googleApiAvailability.getErrorDialog(activity, status, 9000)
                dialog.show()
            }
            return false
        }
        return true
    }

    fun isLocationEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGPSEnabled = false
        var isNetworkEnabled = false

        try {
            isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (!isGPSEnabled && !isNetworkEnabled) {
            AlertDialog.Builder(context)
                .setMessage(R.string.gps_network_not_enabled)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton(android.R.string.no, null)
                .show()
            return false
        }
        return true
    }
}
package com.ismet.parkingzonemaddevs.utils

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

object GoogleMapsUtils{
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
}
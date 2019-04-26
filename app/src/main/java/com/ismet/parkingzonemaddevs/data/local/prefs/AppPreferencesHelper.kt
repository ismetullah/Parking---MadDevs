package com.ismet.parkingzonemaddevs.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.LastEnteredZone
import com.ismet.parkingzonemaddevs.di.PreferenceInfo
import javax.inject.Inject

class AppPreferencesHelper @Inject
constructor(context: Context, @PreferenceInfo prefFileName: String) : PreferencesHelper {
    private var mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getCurrentParking(): CurrentParking {
        val json = mPrefs.getString(PREF_KEY_CURRENT_PARKING, "")
        var currentParking = Gson().fromJson<CurrentParking>(json, CurrentParking::class.java)
        if (currentParking != null) return currentParking
        currentParking = CurrentParking(false, true)
        saveCurrentParking(currentParking)
        return currentParking
    }

    override fun saveCurrentParking(currentParking: CurrentParking) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_PARKING, Gson().toJson(currentParking)).apply()
    }

    override fun getCanAskToPark(): Boolean {
        val curPark = getCurrentParking()
        val canAskToPark = mPrefs.getBoolean(PREF_KEY_CAN_ASK_TO_PARK, true)
        if (curPark.isParking)
            return false
        return canAskToPark
    }

    override fun saveCanAskToPark(b: Boolean) {
        mPrefs.edit().putBoolean(PREF_KEY_CAN_ASK_TO_PARK, b).apply()
    }

    override fun saveLastEnteredZone(lastEnteredZone: LastEnteredZone) {
        mPrefs.edit().putString(PREF_KEY_LAST_ENTERED_ZONE, Gson().toJson(lastEnteredZone)).apply()
    }

    override fun getLastEnteredZone(): LastEnteredZone? {
        val json = mPrefs.getString(PREF_KEY_LAST_ENTERED_ZONE, "")
        return Gson().fromJson<LastEnteredZone>(json, LastEnteredZone::class.java)
    }

    override fun getIsTrackLocationEnabled(): Boolean {
        return mPrefs.getBoolean(PREF_KEY_TRACK_LOCATION_ENABLED, true)
    }

    override fun saveIsTrackLocationEnabled(b: Boolean) {
        mPrefs.edit().putBoolean(PREF_KEY_TRACK_LOCATION_ENABLED, b).apply()
    }

    companion object {
        private val PREF_KEY_LAST_ENTERED_ZONE = "PREF_KEY_LAST_ENTERED_ZONE"
        private val PREF_KEY_CURRENT_PARKING = "PREF_KEY_CURRENT_PARKING"
        private val PREF_KEY_CAN_ASK_TO_PARK = "PREF_KEY_CAN_ASK_TO_PARK"
        private val PREF_KEY_TRACK_LOCATION_ENABLED = "PREF_KEY_TRACK_LOCATION_ENABLED"
    }
}

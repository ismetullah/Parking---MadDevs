package com.ismet.parkingzonemaddevs

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.SystemClock
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.ismet.parkingzonemaddevs.ui.history.HistoryAdapter
import com.ismet.parkingzonemaddevs.ui.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleInstrumentedTest {

    @get:Rule
    public val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)

    private val HISTORY = "History"

    @Test
    fun openHistoryActivity() {
        onView(withId(R.id.drawerView)).perform(open())
        onView(withText(HISTORY)).perform(click())
        Thread.sleep(500)

        onView(
            withId(R.id.recView)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<HistoryAdapter.EventViewHolder>(
                1,
                swipeLeft()
            )
        )
        Thread.sleep(10000)

    }

    @Before
    fun grantPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            with(getInstrumentation().uiAutomation) {
                executeShellCommand("appops set " + InstrumentationRegistry.getTargetContext().packageName + " android:mock_location allow")
                Thread.sleep(1000)
            }
        }
    }

    @Test
    fun testLocation() {
        val lm = getInstrumentation().context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE

        val mocLocationProvider = LocationManager.GPS_PROVIDER//lm.getBestProvider( criteria, true );

        // This gives error....
        lm.addTestProvider(
            mocLocationProvider, false, false,
            false, false, true, true, true, 0, 5
        )
        lm.setTestProviderEnabled(mocLocationProvider, true)

        val loc = Location(mocLocationProvider)
        val mockLocation = Location(mocLocationProvider) // a string
        mockLocation.latitude = 42.835114// double
        mockLocation.longitude = 74.601402
        mockLocation.altitude = loc.altitude
        mockLocation.time = System.currentTimeMillis()
        mockLocation.accuracy = 1f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
        lm.setTestProviderLocation(mocLocationProvider, mockLocation)
        Thread.sleep(15000)
    }
}

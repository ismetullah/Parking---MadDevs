package com.ismet.parkingzonemaddevs

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.ismet.parkingzonemaddevs.ui.history.HistoryAdapter
import com.ismet.parkingzonemaddevs.ui.main.MainActivity
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
            withId(R.id.recView)).perform(
                RecyclerViewActions.actionOnItemAtPosition<HistoryAdapter.EventViewHolder>(
                    1,
                    swipeLeft()
                )
            )
        Thread.sleep(10000)

    }
}

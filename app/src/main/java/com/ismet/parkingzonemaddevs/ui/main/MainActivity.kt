package com.ismet.parkingzonemaddevs.ui.main

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.ismet.parkingzonemaddevs.BR
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.data.model.CurrentParking
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.databinding.ActivityMainBinding
import com.ismet.parkingzonemaddevs.ui.about.AboutFragment
import com.ismet.parkingzonemaddevs.ui.base.BaseActivity
import com.ismet.parkingzonemaddevs.ui.history.HistoryActivity
import com.ismet.parkingzonemaddevs.ui.location.LocationNotificationReceiver
import com.ismet.parkingzonemaddevs.ui.location.LocationTrackerService
import com.ismet.parkingzonemaddevs.ui.location.LocationTrackerService.REQUEST_TO_PARK
import com.ismet.parkingzonemaddevs.ui.location.LocationTrackerService.REQUEST_TO_STOP_PARKING
import com.ismet.parkingzonemaddevs.ui.parkingzone.ParkingZonesActivity
import com.ismet.parkingzonemaddevs.ui.parkingzone.fragment.ParkingZoneFragment
import com.ismet.parkingzonemaddevs.ui.settings.SettingsActivity
import com.ismet.parkingzonemaddevs.utils.AppConstants.ACTION_NOTIFICATION_RECEIVED
import com.ismet.parkingzonemaddevs.utils.CommonUtils.timeToString
import com.ismet.parkingzonemaddevs.utils.GoogleMapsUtils
import com.ismet.parkingzonemaddevs.utils.NotificationHelper.NOTIFICATION_DATA
import com.ismet.parkingzonemaddevs.utils.NotificationHelper.NOTIFICATION_REQUEST
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject
import kotlin.reflect.KClass


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator, OnMapReadyCallback,
    DrawPolygons.OnPolygonsReadyListener, GoogleMap.OnPolygonClickListener, HasSupportFragmentInjector,
    LocationNotificationReceiver.OnNotificationReceived {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var mainViewModel: MainViewModel
    private lateinit var map: GoogleMap
    private var isActivityPaused = true

    private var polygons: ArrayList<Polygon> = ArrayList()
    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        mainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        return mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        mainViewModel.setNavigator(this)
        mainViewModel.getIsStopParkingShown().observe(this, Observer<Boolean> { t ->
            when (t) {
                true -> stopParkingButton.visibility = View.VISIBLE
                false -> stopParkingButton.visibility = View.GONE
                else -> stopParkingButton.visibility = View.GONE
            }
        })

        setupToolbar()

        val mDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerView,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                hideKeyboard()
            }
        }
        drawerView.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
        setupNavMenu()
        initMap()
        onNotificationClick()
    }

    private fun onNotificationClick() {
        val request = intent.getStringExtra(NOTIFICATION_REQUEST) ?: return
        if (request == REQUEST_TO_PARK) {
            val zone = intent.getSerializableExtra(NOTIFICATION_DATA) as ParkingZone
            if (zone != null)
                askToPark(zone)
        }
        if (request == REQUEST_TO_STOP_PARKING) {
            val currParking = intent.getSerializableExtra(NOTIFICATION_DATA) as CurrentParking
            if (currParking != null)
                askToStopParking(currParking)
        }
    }

    private fun setupToolbar() {
        toolbar.title = " "
        setSupportActionBar(toolbar)
    }

    private fun initMap() {
        if (GoogleMapsUtils.isGooglePlayServicesAvailable(this)) {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        mainViewModel.getParkingZones().observe(this,
            Observer<List<ParkingZone>> { t -> addZonesToMap(t) })

        map.setOnPolygonClickListener(this)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(42.837485, 74.604721), 12f))
        mainViewModel.checkIsLocationTrackEnabled()
    }

    override fun onTrackLocationDisabled() {
        disableTrackLocation()
    }

    override fun onTrackLocationEnabled() {
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false
            locationButton.visibility = View.VISIBLE
            locationButton.setOnClickListener {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(map.myLocation.latitude, map.myLocation.longitude), 16f)
                )
            }
            enableTrackLocation()
        }
    }

    private fun enableTrackLocation() {
        startService(Intent(this, LocationTrackerService::class.java))
        val br = LocationNotificationReceiver()
        br.setListener(this)
        registerReceiver(br, IntentFilter(ACTION_NOTIFICATION_RECEIVED))
    }

    private fun disableTrackLocation() {
        stopService(Intent(this, LocationTrackerService::class.java))
    }

    override fun onPolygonClick(p0: Polygon?) {
        if (!p0?.points.isNullOrEmpty())
            mainViewModel.findZoneByPoint(p0?.points!![0])
    }

    private fun addZonesToMap(list: List<ParkingZone>) {
        map.clear()
        polygons = ArrayList()
        DrawPolygons(this, list).execute()
    }

    override fun onPolygonsReady(polygonOptionsList: ArrayList<PolygonOptions>) {
        polygonOptionsList.forEach { polygons.add(map.addPolygon(it)) }
    }

    private fun setupNavMenu() {
        navigationView.setNavigationItemSelectedListener { item ->
            drawerView.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.navItemSettings -> {
                    openActivityForResult(SettingsActivity::class, SETTINGS_ACTIVITY_REQUEST_CODE)
                    return@setNavigationItemSelectedListener true
                }
                R.id.navItemHistory -> {
                    openHistoryActivity()
                    return@setNavigationItemSelectedListener true
                }
                R.id.navItemZones -> {
                    openActivityForResult(ParkingZonesActivity::class, PARKING_ZONES_ACTIVITY_REQUEST_CODE)
                    return@setNavigationItemSelectedListener true
                }
                R.id.navItemAbout -> {
                    showAboutFragment()
                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener false
            }
        }
    }

    private fun openActivityForResult(cls: KClass<out Activity>, requestCode: Int) {
        startActivityForResult(Intent(this, cls.java), requestCode)
    }

    private fun openHistoryActivity() {
        startActivity(Intent(this, HistoryActivity::class.java))
    }

    private fun showAboutFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
            .replace(R.id.container, AboutFragment.newInstance(), AboutFragment.TAG)
            .addToBackStack(AboutFragment.TAG)
            .commit()
    }

    override fun showParkingZoneFragment(parkingZone: ParkingZone) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_down, R.anim.slide_up)
            .replace(R.id.container, ParkingZoneFragment.newInstance(parkingZone), ParkingZoneFragment.TAG)
            .addToBackStack(ParkingZoneFragment.TAG)
            .commit()
    }

    override fun askToStopParking(currentParking: CurrentParking) {
        val m = "Zone: " + currentParking.parkingZoneName +
                "\nEntry time: " + timeToString(currentParking.entryTime) +
                "\nExit time: " + timeToString(Date().time)

        AlertDialog.Builder(this)
            .setTitle("Do you want to stop parking?")
            .setMessage(m)
            .setPositiveButton(
                android.R.string.yes
            ) { p0, p1 -> mainViewModel.stopParking(currentParking) }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(true)
            .setOnDismissListener {
                mainViewModel.loadCurrentParking()
            }
            .show()
        cancelAllNotifications()
    }

    override fun askToPark(parkingZone: ParkingZone) {
        val m = "Zone name: " + parkingZone.name

        AlertDialog.Builder(this)
            .setTitle("Do you want to park here?")
            .setMessage(m)
            .setPositiveButton(
                android.R.string.yes
            ) { p0, p1 -> mainViewModel.startParking(parkingZone) }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(true)
            .setOnDismissListener {
                mainViewModel.loadCurrentParking()
            }
            .show()
        cancelAllNotifications()
    }

    private fun cancelAllNotifications() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // if activity is on pause, do not clear notifications
        if (!isActivityPaused) notificationManager.cancelAll()
    }


    override fun onSuccess(id: Int) {
        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show()
    }

    override fun handleThrowable(m: String) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PARKING_ZONES_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (map != null && data != null) {
                    val zone: ParkingZone = data.getSerializableExtra(RESULT) as ParkingZone
                    if (zone != null) {
                        val builder = LatLngBounds.Builder()
                        zone.polygonCorners.forEach {
                            builder.include(LatLng(it.lat, it.lng))
                        }
                        val bounds = builder.build()
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    }
                }
            }
        } else if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mainViewModel.checkIsLocationTrackEnabled()
        }
    }

    override fun onResume() {
        super.onResume()
        isActivityPaused = false
    }

    override fun onPause() {
        super.onPause()
        isActivityPaused = true
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val RESULT = "result"
        private const val PARKING_ZONES_ACTIVITY_REQUEST_CODE = 2
        private const val SETTINGS_ACTIVITY_REQUEST_CODE = 3
    }
}

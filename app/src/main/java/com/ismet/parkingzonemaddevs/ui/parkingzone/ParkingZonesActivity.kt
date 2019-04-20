package com.ismet.parkingzonemaddevs.ui.parkingzone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismet.parkingzonemaddevs.BR
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.databinding.ActivityParkingZonesBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseActivity
import com.ismet.parkingzonemaddevs.ui.main.MainActivity.Companion.RESULT
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class ParkingZonesActivity : BaseActivity<ActivityParkingZonesBinding, ParkingZonesViewModel>(), ParkingZonesNavigator,
    ParkingZonesAdapter.ParkingZonesAdapterListener {
    @Inject
    lateinit var adapter: ParkingZonesAdapter

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var viewModel: ParkingZonesViewModel
    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_parking_zones
    }

    override fun getViewModel(): ParkingZonesViewModel {
        viewModel = ViewModelProviders.of(this, factory).get(ParkingZonesViewModel::class.java)
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewModel.setNavigator(this)
        adapter.setListener(this)
        setUp()
    }

    private fun setUp() {
        layoutManager.orientation = RecyclerView.VERTICAL
        recView.layoutManager = layoutManager
        recView.itemAnimator = DefaultItemAnimator()
        recView.adapter = adapter
        viewModel.getParkingZones().observe(this, Observer<List<ParkingZone>> { t ->
            adapter.setItems(t)
        })
    }

    override fun onRetryClick() {
        viewModel.fetchEvents()
    }

    override fun onItemClick(zone: ParkingZone) {
        val returnIntent = Intent()
        returnIntent.putExtra(RESULT, zone)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun handleError(m: String) {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

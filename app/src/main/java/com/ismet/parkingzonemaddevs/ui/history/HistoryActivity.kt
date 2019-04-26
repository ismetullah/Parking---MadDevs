package com.ismet.parkingzonemaddevs.ui.history

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismet.parkingzonemaddevs.BR
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.databinding.ActivityHistoryBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseActivity
import com.ismet.parkingzonemaddevs.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class HistoryActivity : BaseActivity<ActivityHistoryBinding, HistoryViewModel>(), HistoryNavigator,
    HistoryAdapter.HistoryAdapterListener {
    @Inject
    lateinit var historyAdapter: HistoryAdapter

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var historyViewModel: HistoryViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_history
    }

    override fun getViewModel(): HistoryViewModel {
        historyViewModel = ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)
        return historyViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = getString(R.string.history)
        historyViewModel.setNavigator(this)
        historyAdapter.setListener(this)
        setUp()
    }

    private fun setUp() {
        layoutManager.orientation = RecyclerView.VERTICAL
        recView.layoutManager = layoutManager
        recView.itemAnimator = DefaultItemAnimator()
        recView.adapter = historyAdapter
        historyViewModel.getEventList().observe(this, Observer<List<ParkingEvent>> {
                t -> historyAdapter.setItems(t)
        })

        enableSwipeToDeleteAndUndo()
    }

    private fun enableSwipeToDeleteAndUndo() {

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val event = historyAdapter.getItemByPos(position)
                if (event != null)
                    historyViewModel.removeItem(event, position)
            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(recView)
    }

    override fun removeEventFromRecView(pos: Int) {
        historyAdapter.removeItem(pos)
    }

    override fun onRetryClick() {
        historyViewModel.fetchEvents()
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

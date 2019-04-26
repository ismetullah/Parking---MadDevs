package com.ismet.parkingzonemaddevs.ui.settings

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ismet.parkingzonemaddevs.BR
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.databinding.ActivitySettingsBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsViewModel>(), SettingsNavigator {
    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var viewModel: SettingsViewModel
    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun getViewModel(): SettingsViewModel {
        viewModel = ViewModelProviders.of(this, factory).get(SettingsViewModel::class.java)
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = getString(R.string.settings)
        viewModel.setNavigator(this)
        viewModel.getIsMyLocationEnabled().observe(this,
            Observer<Boolean> { t -> locationSwitch.isChecked = t })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }

}
package com.ismet.parkingzonemaddevs.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ismet.parkingzonemaddevs.BR
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.databinding.ActivitySplashBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseActivity
import com.ismet.parkingzonemaddevs.ui.main.MainActivity
import javax.inject.Inject

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {
    private var mSplashViewModel: SplashViewModel? = null

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun getViewModel(): SplashViewModel {
        mSplashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
        return mSplashViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashViewModel?.setNavigator(this)
        mSplashViewModel?.startSeeding()
    }

    override fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun handleThrowable(m: String) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
    }
}

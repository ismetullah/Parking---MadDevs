package com.ismet.parkingzonemaddevs.ui.parkingzone.fragment

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ismet.parkingzonemaddevs.BR
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.databinding.FragmentParkingZoneBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseFragment
import com.ismet.parkingzonemaddevs.ui.main.MainActivity
import com.ismet.parkingzonemaddevs.utils.CommonUtils.stringToBitmap
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import javax.inject.Inject

class ParkingZoneFragment : BaseFragment<FragmentParkingZoneBinding, ParkingZoneFragmentViewModel>(),
    ParkingZoneFragmentNavigator {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var parkingZoneViewModel: ParkingZoneFragmentViewModel
    private lateinit var carouselView: CarouselView

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_parking_zone
    }

    override fun getViewModel(): ParkingZoneFragmentViewModel {
        parkingZoneViewModel = ViewModelProviders.of(this, factory).get(ParkingZoneFragmentViewModel::class.java)
        return parkingZoneViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parkingZoneViewModel.setNavigator(this)
    }

    private var parkingZone: ParkingZone? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        if (arguments != null) {
            parkingZone = arguments?.getSerializable(PARKING_ZONE) as ParkingZone?
            parkingZoneViewModel.setData(parkingZone)
            setUpCarousel(parkingZone?.photos)
        }

        return v
    }

    private fun setUpCarousel(photos: ArrayList<String>?) {
        carouselView = getViewDataBinding().carouselView
        if (photos.isNullOrEmpty()) {
            carouselView.visibility = View.GONE
            return
        }
        carouselView.pageCount = photos.size

        val imageListener =
            ImageListener { position, imageView ->
                imageView.setImageBitmap(stringToBitmap(photos[position]))
            }
        carouselView.setImageListener(imageListener)
    }

    override fun goBack() {
        getBaseActivity()?.onBackPressed()
    }

    override fun parkHere() {
        if (parkingZone != null) {
            (getBaseActivity() as MainActivity).askToPark(parkingZone!!)
        }
    }

    override fun handleThrowable(m: Int) {
        Toast.makeText(context, m, Toast.LENGTH_SHORT).show()
    }

    companion object {

        val TAG = ParkingZoneFragment::class.java.simpleName
        val PARKING_ZONE = "PARKING_ZONE"

        fun newInstance(parkingZone: ParkingZone): ParkingZoneFragment {
            val args = Bundle()
            args.putSerializable(PARKING_ZONE, parkingZone)
            val fragment = ParkingZoneFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

package com.ismet.parkingzonemaddevs.ui.park

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ViewModelProviderFactory
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.databinding.DialogParkBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ParkDialog : BaseDialog(), ParkNavigator {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var parkViewHolder: ParkViewHolder
    lateinit var listener: OnDismissListener

    override fun onSubmit() {
        dismiss()
    }

    override fun onCancel() {
        dismiss()
    }

    override fun dismiss() {
        listener.onParkDialogDismiss()
        super.dismiss()
    }

    fun setOnDismissListener(listener: OnDismissListener){
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogParkBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_park, container, false)
        val view = binding.root

        AndroidSupportInjection.inject(this)
        parkViewHolder = ViewModelProviders.of(this, factory).get(ParkViewHolder::class.java)
        binding.viewModel = parkViewHolder

        parkViewHolder.setNavigator(this)

        if (arguments != null) {
            val parkingZone = arguments!!.getSerializable(PARKING_ZONE) as ParkingZone
            if (parkingZone != null)
                parkViewHolder.setData(parkingZone)
        }

        return view
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    interface OnDismissListener{
        fun onParkDialogDismiss()
    }

    companion object {

        private val TAG = ParkDialog::class.java.simpleName

        private const val PARKING_ZONE = "PARKING_ZONE"

        fun newInstance(parkingZone: ParkingZone): ParkDialog {
            val fragment = ParkDialog()
            val bundle = Bundle()
            bundle.putSerializable(PARKING_ZONE, parkingZone)
            fragment.arguments = bundle
            return fragment
        }
    }
}

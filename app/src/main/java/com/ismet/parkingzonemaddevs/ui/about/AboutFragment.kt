package com.ismet.parkingzonemaddevs.ui.about

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.ismet.parkingzonemaddevs.R
import com.ismet.parkingzonemaddevs.ui.main.MainActivity

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        val backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener { (activity as MainActivity).onBackPressed() }
        return view
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onDetach() {
        super.onDetach()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    companion object {
        val TAG = AboutFragment::class.java.simpleName

        fun newInstance(): AboutFragment {
            val args = Bundle()
            val fragment = AboutFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

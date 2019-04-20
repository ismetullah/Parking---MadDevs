package com.ismet.parkingzonemaddevs.ui.history

class EmptyItemViewModel(private val mListener: EmptyItemViewModelListener) {

    fun onRetryClick() {
        mListener.onRetryClick()
    }

    interface EmptyItemViewModelListener {
        fun onRetryClick()
    }
}

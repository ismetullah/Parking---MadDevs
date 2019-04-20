package com.ismet.parkingzonemaddevs.ui.parkingzone

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ismet.parkingzonemaddevs.data.model.ParkingZone
import com.ismet.parkingzonemaddevs.databinding.ItemEmptyViewBinding
import com.ismet.parkingzonemaddevs.databinding.ItemParkingZonesViewBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseViewHolder
import com.ismet.parkingzonemaddevs.ui.history.EmptyItemViewModel


class ParkingZonesAdapter(private var eventList: ArrayList<ParkingZone>) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private var mListener: ParkingZonesAdapterListener? = null

    override fun getItemCount(): Int {
        return if (!eventList.isNullOrEmpty()) {
            eventList.size
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!eventList.isNullOrEmpty()) {
            VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_EMPTY
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val blogViewBinding = ItemParkingZonesViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                return ParkingZonesViewHolder(blogViewBinding)
            }
            VIEW_TYPE_EMPTY -> {
                val emptyViewBinding = ItemEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                return EmptyViewHolder(emptyViewBinding)
            }
            else -> {
                val emptyViewBinding =
                    ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return EmptyViewHolder(emptyViewBinding)
            }
        }
    }

    fun getEventList(): ArrayList<ParkingZone> {
        return eventList
    }

    fun addItems(eventList: List<ParkingZone>) {
        this.eventList.addAll(eventList)
        notifyDataSetChanged()
    }

    fun setItems(eventList: List<ParkingZone>) {
        clearItems()
        addItems(eventList)
    }

    fun clearItems() {
        eventList.clear()
        notifyDataSetChanged()
    }

    fun setListener(listener: ParkingZonesAdapterListener) {
        this.mListener = listener
    }

    fun getItemByPos(position: Int): ParkingZone? {
        if (eventList.isNullOrEmpty() || position == eventList.size || position > eventList.size)
            return null
        return eventList[position]
    }

    fun removeItem(position: Int) {
        eventList.removeAt(position)
        notifyItemRemoved(position)
    }

    interface ParkingZonesAdapterListener {
        fun onRetryClick()
        fun onItemClick(zone: ParkingZone)
    }

    inner class ParkingZonesViewHolder(private val mBinding: ItemParkingZonesViewBinding) :
        BaseViewHolder(mBinding.root),
        ParkingZonesItemViewModel.ItemViewModelListener {
        private var viewModel: ParkingZonesItemViewModel? = null

        override fun onBind(position: Int) {
            val blog = eventList[position]
            viewModel = ParkingZonesItemViewModel(blog, this)
            mBinding.viewModel = viewModel
            mBinding.executePendingBindings()
        }

        override fun onItemClick(zone: ParkingZone) {
            mListener?.onItemClick(zone)
        }

    }

    inner class EmptyViewHolder(private val mBinding: ItemEmptyViewBinding) : BaseViewHolder(mBinding.getRoot()),
        EmptyItemViewModel.EmptyItemViewModelListener {

        override fun onBind(position: Int) {
            val emptyItemViewModel = EmptyItemViewModel(this)
            mBinding.viewModel = emptyItemViewModel
        }

        override fun onRetryClick() {
            mListener?.onRetryClick()
        }
    }

    companion object {
        val VIEW_TYPE_EMPTY = 0

        val VIEW_TYPE_NORMAL = 1
    }
}
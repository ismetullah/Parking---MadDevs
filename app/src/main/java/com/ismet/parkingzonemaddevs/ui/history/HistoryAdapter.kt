package com.ismet.parkingzonemaddevs.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ismet.parkingzonemaddevs.data.model.ParkingEvent
import com.ismet.parkingzonemaddevs.databinding.ItemEmptyViewBinding
import com.ismet.parkingzonemaddevs.databinding.ItemEventViewBinding
import com.ismet.parkingzonemaddevs.ui.base.BaseViewHolder


class HistoryAdapter(private var eventList: ArrayList<ParkingEvent>) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private var mListener: HistoryAdapterListener? = null

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
                val blogViewBinding = ItemEventViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                return EventViewHolder(blogViewBinding)
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

    fun getEventList(): ArrayList<ParkingEvent> {
        return eventList
    }

    fun addItems(eventList: List<ParkingEvent>) {
        this.eventList.addAll(eventList)
        notifyDataSetChanged()
    }

    fun setItems(eventList: List<ParkingEvent>) {
        clearItems()
        addItems(eventList)
    }

    fun clearItems() {
        eventList.clear()
        notifyDataSetChanged()
    }

    fun setListener(listener: HistoryAdapterListener) {
        this.mListener = listener
    }

    fun getItemByPos(position: Int): ParkingEvent? {
        if (eventList.isNullOrEmpty() || position == eventList.size || position > eventList.size)
            return null
        return eventList[position]
    }

    fun removeItem(position: Int) {
        eventList.removeAt(position)
        notifyItemRemoved(position)
    }

    interface HistoryAdapterListener {
        fun onRetryClick()
    }

    inner class EventViewHolder(private val mBinding: ItemEventViewBinding) : BaseViewHolder(mBinding.getRoot()) {
        private var mBlogItemViewModel: EventItemViewModel? = null

        override fun onBind(position: Int) {
            val blog = eventList[position]
            mBlogItemViewModel = EventItemViewModel(blog)
            mBinding.setViewModel(mBlogItemViewModel)
            mBinding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(private val mBinding: ItemEmptyViewBinding) : BaseViewHolder(mBinding.getRoot()),
        EmptyItemViewModel.EmptyItemViewModelListener {

        override fun onBind(position: Int) {
            val emptyItemViewModel = EmptyItemViewModel(this)
            mBinding.viewModel = emptyItemViewModel
        }

        override fun onRetryClick() {
            mListener!!.onRetryClick()
        }
    }

    companion object {
        val VIEW_TYPE_EMPTY = 0

        val VIEW_TYPE_NORMAL = 1
    }
}
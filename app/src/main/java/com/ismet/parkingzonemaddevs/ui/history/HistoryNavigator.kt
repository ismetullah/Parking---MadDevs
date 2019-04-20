package com.ismet.parkingzonemaddevs.ui.history

interface HistoryNavigator{
    fun handleError(m: String)
    fun removeEventFromRecView(pos: Int)
}
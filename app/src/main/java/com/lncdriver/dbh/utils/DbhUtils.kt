package com.lncdriver.dbh.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by Siru Malayil on 29-04-2023.
 */
object DbhUtils {

    const val DBH_RIDE_DATA = "dh-ride-data"

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateAndTime(): String? {
        val todayDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm a")
        return formatter.format(todayDate)
    }
}
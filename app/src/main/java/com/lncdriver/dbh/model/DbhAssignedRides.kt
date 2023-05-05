package com.lncdriver.dbh.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DbhAssignedRides(
    val data: List<DbhAssignedRideData>,
    val status: String,
    val message: String
): Parcelable
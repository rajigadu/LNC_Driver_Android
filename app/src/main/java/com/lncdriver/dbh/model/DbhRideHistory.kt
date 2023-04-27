package com.lncdriver.dbh.model

data class DbhRideHistory(
    val data: List<DbhRideHistoryData>,
    val message: String,
    val status: String
)
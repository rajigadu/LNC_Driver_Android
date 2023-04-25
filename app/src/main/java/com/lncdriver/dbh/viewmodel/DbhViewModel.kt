package com.lncdriver.dbh.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.dbh.viewmodel.repository.DbhRepository
import okhttp3.ResponseBody

/**
 * Create by Siru Malayil on 25-04-2023.
 */
class DbhViewModel : ViewModel() {
    private val dbhRepository = DbhRepository()

    fun dbhAssignedRideList(driverId: String): MutableLiveData<Resource<ResponseBody>> {
        return dbhRepository.dbhAssignedRideList(driverId)
    }

}
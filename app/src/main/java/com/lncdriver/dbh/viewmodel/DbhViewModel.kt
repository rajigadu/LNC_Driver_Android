package com.lncdriver.dbh.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lncdriver.dbh.model.DbhAssignedRides
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.dbh.viewmodel.repository.DbhRepository

/**
 * Create by Siru Malayil on 25-04-2023.
 */
class DbhViewModel : ViewModel() {
    private val dbhRepository = DbhRepository()

    fun dbhAssignedRideList(driverId: String): MutableLiveData<Resource<DbhAssignedRides>> {
        return dbhRepository.dbhAssignedRideList(driverId)
    }

    fun dbhStartRide(rideId: String, driverId: String, time: String): MutableLiveData<Resource<DbhAssignedRides>> {
        return dbhRepository.dbhStartRide(rideId,driverId, time)
    }

    fun dbhCompleteRide(driverId: String): MutableLiveData<Resource<DbhAssignedRides>> {
        return dbhRepository.dbhAssignedRideList(driverId)
    }

}
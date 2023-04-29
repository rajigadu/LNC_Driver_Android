package com.lncdriver.dbh.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lncdriver.dbh.model.DbhAssignedRides
import com.lncdriver.dbh.model.DbhRideHistory
import com.lncdriver.dbh.model.DbhRideStart
import com.lncdriver.dbh.model.DefaultResponse
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

    fun dbhStartRide(rideId: String, driverId: String, time: String): MutableLiveData<Resource<DbhRideStart>> {
        return dbhRepository.dbhStartRide(rideId,driverId, time)
    }

    fun dbhCompleteRide(userId: String, bookingId: String, payDateTime: String, endTime: String): MutableLiveData<Resource<DefaultResponse>> {
        return dbhRepository.dbhCompleteRide(userId,bookingId,payDateTime,endTime)
    }

    fun dbhRideHistory(driverId: String): MutableLiveData<Resource<DbhRideHistory>> {
        return dbhRepository.dbhRideHistory(driverId)
    }

    fun cancelDbhRide(driverId: String, rideId: String): MutableLiveData<Resource<DefaultResponse>> {
        return dbhRepository.cancelDbhRide(driverId, rideId)
    }

}
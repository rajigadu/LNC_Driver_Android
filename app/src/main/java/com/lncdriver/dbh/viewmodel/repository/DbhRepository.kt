package com.lncdriver.dbh.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.lncdriver.dbh.model.DbhAssignedRides
import com.lncdriver.dbh.model.DbhRideHistory
import com.lncdriver.dbh.model.DefaultResponse
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.utils.ServiceApi
import com.lncdriver.utils.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Create by Siru Malayil on 25-04-2023.
 */
class DbhRepository {

    private val apiService: ServiceApi = ServiceGenerator.createService(ServiceApi::class.java)

    fun dbhAssignedRideList(driverId: String): MutableLiveData<Resource<DbhAssignedRides>> {
        val assignedRideList = MutableLiveData<Resource<DbhAssignedRides>>()
        assignedRideList.postValue(Resource.loading(null))
        apiService.dbhAssignedRideList(driverId).enqueue(object : Callback<DbhAssignedRides> {
            override fun onResponse(
                call: Call<DbhAssignedRides>,
                response: Response<DbhAssignedRides>
            ) {
                if (response.isSuccessful) {
                    assignedRideList.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    assignedRideList.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhAssignedRides>, t: Throwable) {
                // handle error
                assignedRideList.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return assignedRideList
    }
    fun dbhStartRide(rideId: String, driverId: String, time: String): MutableLiveData<Resource<DefaultResponse>> {
        val startRideResponse = MutableLiveData<Resource<DefaultResponse>>()
        startRideResponse.postValue(Resource.loading(null))
        apiService.dbhStartRide(rideId,driverId,time).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    startRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    startRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                // handle error
                startRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return startRideResponse
    }
    fun dbhCompleteRide(userId: String, bookingId: String, payDateTime: String, endTime: String):
            MutableLiveData<Resource<DbhAssignedRides>> {
        val completeRideResponse = MutableLiveData<Resource<DbhAssignedRides>>()
        completeRideResponse.postValue(Resource.loading(null))
        apiService.dbhCompleteRide(userId,bookingId,payDateTime,endTime).enqueue(object : Callback<DbhAssignedRides> {
            override fun onResponse(
                call: Call<DbhAssignedRides>,
                response: Response<DbhAssignedRides>
            ) {
                if (response.isSuccessful) {
                    completeRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    completeRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhAssignedRides>, t: Throwable) {
                // handle error
                completeRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return completeRideResponse
    }

    fun dbhRideHistory(driverId: String):
            MutableLiveData<Resource<DbhRideHistory>> {
        val dbhRideHistoryList = MutableLiveData<Resource<DbhRideHistory>>()
        dbhRideHistoryList.postValue(Resource.loading(null))
        apiService.dbhRideHistory(driverId).enqueue(object : Callback<DbhRideHistory> {
            override fun onResponse(
                call: Call<DbhRideHistory>,
                response: Response<DbhRideHistory>
            ) {
                if (response.isSuccessful) {
                    dbhRideHistoryList.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    dbhRideHistoryList.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhRideHistory>, t: Throwable) {
                // handle error
                dbhRideHistoryList.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return dbhRideHistoryList
    }
}
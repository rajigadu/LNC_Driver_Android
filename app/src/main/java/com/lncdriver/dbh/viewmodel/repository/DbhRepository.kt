package com.lncdriver.dbh.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.lncdriver.dbh.model.DbhAssignedRides
import com.lncdriver.dbh.model.DbhRideHistory
import com.lncdriver.dbh.model.DbhRideStart
import com.lncdriver.dbh.model.DefaultResponse
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.utils.ServiceApi
import com.lncdriver.utils.ServiceGenerator
import org.json.JSONObject
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
    fun dbhStartRide(rideId: String, driverId: String, time: String): MutableLiveData<Resource<DbhRideStart>> {
        val startRideResponse = MutableLiveData<Resource<DbhRideStart>>()
        startRideResponse.postValue(Resource.loading(null))
        apiService.dbhStartRide(rideId,driverId,time).enqueue(object : Callback<DbhRideStart> {
            override fun onResponse(
                call: Call<DbhRideStart>,
                response: Response<DbhRideStart>
            ) {
                if (response.isSuccessful) {
                    startRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    startRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhRideStart>, t: Throwable) {
                // handle error
                startRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return startRideResponse
    }
    fun dbhCompleteRide(jsonData: JSONObject):
            MutableLiveData<Resource<DefaultResponse>> {
        val completeRideResponse = MutableLiveData<Resource<DefaultResponse>>()
        completeRideResponse.postValue(Resource.loading(null))
        apiService.dbhCompleteRide(
            jsonData.getString("userid"),
            jsonData.getString("booking_id"),
            jsonData.getString("pay_datetime"),
            jsonData.getString("end_time"),
            jsonData.getString("d_address"),
            jsonData.getString("d_lat"),
            jsonData.getString("d_long")
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    completeRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    completeRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
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

    fun cancelDbhRide(driverId: String, rideId: String):
            MutableLiveData<Resource<DefaultResponse>> {
        val dbhRideHistoryList = MutableLiveData<Resource<DefaultResponse>>()
        dbhRideHistoryList.postValue(Resource.loading(null))
        apiService.cancelDbhRide(driverId, rideId).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    dbhRideHistoryList.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    dbhRideHistoryList.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                // handle error
                dbhRideHistoryList.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return dbhRideHistoryList
    }
}
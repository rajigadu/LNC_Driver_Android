package com.lncdriver.dbh.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.lncdriver.dbh.model.DbhAssignedRides
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
        val addCardResult = MutableLiveData<Resource<DbhAssignedRides>>()
        addCardResult.postValue(Resource.loading(null))
        apiService.dbhAssignedRideList(driverId).enqueue(object : Callback<DbhAssignedRides> {
            override fun onResponse(
                call: Call<DbhAssignedRides>,
                response: Response<DbhAssignedRides>
            ) {
                if (response.isSuccessful) {
                    addCardResult.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    addCardResult.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhAssignedRides>, t: Throwable) {
                // handle error
                addCardResult.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return addCardResult
    }
    fun dbhStartRide(rideId: String, driverId: String, time: String): MutableLiveData<Resource<DbhAssignedRides>> {
        val addCardResult = MutableLiveData<Resource<DbhAssignedRides>>()
        addCardResult.postValue(Resource.loading(null))
        apiService.dbhStartRide(rideId,driverId,time).enqueue(object : Callback<DbhAssignedRides> {
            override fun onResponse(
                call: Call<DbhAssignedRides>,
                response: Response<DbhAssignedRides>
            ) {
                if (response.isSuccessful) {
                    addCardResult.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    addCardResult.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhAssignedRides>, t: Throwable) {
                // handle error
                addCardResult.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return addCardResult
    }
}
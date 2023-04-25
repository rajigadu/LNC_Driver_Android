package com.lncdriver.dbh.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.utils.ServiceApi
import com.lncdriver.utils.ServiceGenerator
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Create by Siru Malayil on 25-04-2023.
 */
class DbhRepository {

    private val apiService: ServiceApi = ServiceGenerator.createService(ServiceApi::class.java)

    fun dbhAssignedRideList(driverId: String): MutableLiveData<Resource<ResponseBody>> {
        val addCardResult = MutableLiveData<Resource<ResponseBody>>()
        addCardResult.postValue(Resource.loading(null))
        apiService.dbhAssignedRideList(driverId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    addCardResult.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    addCardResult.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // handle error
                addCardResult.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return addCardResult
    }
}
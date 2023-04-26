package com.lncdriver.utils;

import com.lncdriver.dbh.model.DbhAssignedRides;
import com.lncdriver.model.WeekList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by narip on 2/3/2017.
 */
public interface ServiceApi {
    @POST("{login}")
    Call<ResponseBody> login(@Path("login") String postfix, @Body RequestBody params);

    @Headers("Accept: " + "application/json")
    @GET(Settings.URL_GET_BY_WEEK_PAYMENT_HISTORY)
    Call<WeekList> fetchWeekList(@Query("driver_id") String driverId);

    @POST(Settings.URL_DBH_ASSIGNED_RIDE_LIST)
    Call<DbhAssignedRides>dbhAssignedRideList(@Query("driver_id") String driverId);

    @POST(Settings.URL_DBH_START_RIDE)
    Call<DbhAssignedRides>dbhStartRide(
            @Query("rideid") String rideId,
            @Query("driverid") String driverId,
            @Query("time") String time
    );

}

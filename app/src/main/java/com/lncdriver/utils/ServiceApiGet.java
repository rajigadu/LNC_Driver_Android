package com.lncdriver.utils;

import com.lncdriver.pojo.PartnersResponce;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by narip on 2/3/2017.
 */
public interface ServiceApiGet {
    // @POST("{login}")
    // Call<ResponseBody> login(@Path("login") String postfix, @Body RequestBody params);

    @GET("{login}")
    Call<ResponseBody> login(@Path("login") String postfix, @QueryMap HashMap<String, Object> params);

    @Headers("Accept: " + "application/json")
    @GET(Settings.URL_PARTNER_LIST)
    Call<PartnersResponce> fetchPartnerList(@Query("driverid") String driverId);


    @GET("dummy-partner-future-accept-new.php")
    Call<ResponseBody> dummyPartner(@QueryMap HashMap<String, Object> params);


}

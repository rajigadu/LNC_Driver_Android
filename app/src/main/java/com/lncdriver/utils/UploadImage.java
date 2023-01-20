package com.lncdriver.utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Lenovo on 1/25/2017.
 */

public interface UploadImage
{
    @Multipart
    @POST("{image_url}")
    Call<ResponseBody> upload(@Path("image_url") String url,
                              @Part("driverid") RequestBody id,
                              @Part("fname") RequestBody fname,
                              @Part("lname") RequestBody lname,
                              @Part("mobile") RequestBody mobile,
                              @Part MultipartBody.Part file);

    @Multipart
    @POST("{image_url}")
    Call<ResponseBody> upload_vehicle(@Path("image_url") String url,
                              @Part("driverid") RequestBody id,
                              @Part("vehicle_type") RequestBody vtype,
                              @Part("vehicle_model") RequestBody vmodel,
                              @Part("vehile_making_year") RequestBody vmyear,
                              @Part MultipartBody.Part file1,
                              @Part MultipartBody.Part file2,
                              @Part MultipartBody.Part file3,
                              @Part MultipartBody.Part file4);

    @Multipart
    @POST("{image_url}")
    Call<ResponseBody> signup(@Path("image_url") String url,
                              @Part MultipartBody.Part file1,
                              @Part MultipartBody.Part file2,
                              @Part MultipartBody.Part file3,
                              @Part MultipartBody.Part file4,
                              @Part MultipartBody.Part file5,
                              @Part("json") RequestBody json);


    @Multipart
    @POST("{image_url}")
    Call<ResponseBody> uploadres(@Path("image_url") String url,
                                 @Part MultipartBody.Part file);
}

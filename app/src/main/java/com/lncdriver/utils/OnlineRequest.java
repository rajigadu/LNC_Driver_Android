package com.lncdriver.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.lncdriver.model.SavePref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by narayana on 3/27/2018.
 */

public class OnlineRequest {
    private static final String TAG = "OnlineRequest";

    public static void loginRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("emailid", map.get("emailid").toString());
        Global.mapMain.put(ConstVariable.PASSWORD, map.get("password").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_LOGIN);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Login);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void cancelUserRidRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put("ride_id", map.get("ride_id").toString());

        if (!map.get("reason").toString().equalsIgnoreCase(""))
            Global.mapMain.put("reason", map.get("reason").toString());

        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_CANCELRIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Cancel_Ride);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }


    public static void cancelFutureRideByPartner(Context mcontext, HashMap<String, Object> map) {

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", map.get("future_partner_id").toString());
        Global.mapMain.put("ride_id", map.get("id").toString());

        Global.mapMain.put(ConstVariable.URL, Settings.URL_FUTURE_RIDE_CANCEL_BY_PARTNER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Cancel_Future_Ride_By_Partner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void cancelFutureRidRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put("ride_id", map.get("ride_id").toString());

        if (map.get("partner_id").toString() != null)
            Global.mapMain.put("partner_id", map.get("partner_id").toString());

        if (!map.get("reason").toString().equalsIgnoreCase(""))
            Global.mapMain.put("reason", map.get("reason").toString());

        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_CANCELFUTURERIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.CancelFutureRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void signupRequest(Context mcontext, Uri uri, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("profilepic", map.get("profilepic").toString());
        Global.mapMain.put("vehicle_image", map.get("vehicle_image").toString());
        Global.mapMain.put("documents_image", map.get("documents_image").toString());
        Global.mapMain.put("license_image", map.get("license_image").toString());
        Global.mapMain.put("driver_abstract", map.get("driver_abstract").toString());
        Global.mapMain.put("uri_two", map.get("uri_two").toString());
        Global.mapMain.put("uri_three", map.get("uri_three").toString());
        Global.mapMain.put("uri_four", map.get("uri_four").toString());
        Global.mapMain.put("uri_five", map.get("uri_five").toString());
        Global.mapMain.put("json", map.get("json").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_REGISTRATION);

        if (Utils.isNetworkAvailable(mcontext)) {
            Utils.uploadImage(mcontext, uri, Global.mapMain, ConstVariable.SignUp);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void changePawwordRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("oldpassword", map.get("oldpassword").toString());
        Global.mapMain.put("newpassword", map.get("newpassword").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_CHANGEPASSWORD);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.ChangePassword);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void serviceTypeRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("driver_type", map.get("driver_type").toString());
        Global.mapMain.put("utype", map.get("utype").toString());
        Global.mapMain.put("default", map.get("default").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_SERVICETYPE);

        if (map.containsKey("driver_type") && map.get("driver_type").toString().equalsIgnoreCase("1")) {
            Log.e(TAG , "AAAAA "+map.get("id").toString());

            Global.mapMain.put("pname", map.get("pname").toString());
            Global.mapMain.put("pemail", map.get("pemail").toString());
            Global.mapMain.put("pmobile", map.get("pmobile").toString());
            Global.mapMain.put("id", map.get("id").toString());
        }

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.ServiceType);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void serviceTypePartnerRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("driver_type", map.get("driver_type").toString());
        Global.mapMain.put("utype", map.get("utype").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_SERVICETYPE);

        if (map.containsKey("driver_type") && map.get("driver_type").toString().equalsIgnoreCase("1")) {
            Global.mapMain.put("pname", map.get("pname").toString());
            Global.mapMain.put("pemail", map.get("pemail").toString());
            Global.mapMain.put("pmobile", map.get("pmobile").toString());
        }

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Partner_Req_Type);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void authenticatePartnerRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String rid = pref1.getRideId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("driver_type", map.get("driver_type").toString());
        Global.mapMain.put("ride_id", rid);

        if (map.containsKey("driver_type") && map.get("driver_type").toString().equalsIgnoreCase(""))
            Global.mapMain.put("type", map.get("driver_type").toString());

        Global.mapMain.put(ConstVariable.URL, Settings.URL_AUTHENTICATEPARTNER);

        if (map.containsKey("driver_type") && map.get("driver_type").toString().equalsIgnoreCase("1")) {
            Global.mapMain.put("pname", map.get("pname").toString());
            Global.mapMain.put("pemail", map.get("pemail").toString());
            Global.mapMain.put("pmobile", map.get("pmobile").toString());
        }

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.AuthenticatePartner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getRideUserDetailsRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put("ride_id", map.get("ride_id").toString());

      /*Utils.global.mapMain.put("user_id","132");
        Utils.global.mapMain.put("ride_id","703");*/

        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DRIVERFUTURERIDEDETAILS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.DriverFutureRideDetails);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getEstimateRide(Context mcontext, String rideId, String strUnPlannedSet, long posTime) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        //Utils.global.mapMain();
        // Utils.global.mapMain.put("driver_id", id);
        // Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());

        HashMap<String, Object> mapMain = new HashMap<>();

        mapMain.put("rideid", rideId);
        mapMain.put("unplanned_stops", strUnPlannedSet);
        mapMain.put("waiting_time", posTime);
        mapMain.put("driver_id", id);

//unplanned_stops,waiting_time,rideid,driver_id
        mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_ESTIMATE_PRICE);

        //Log.e("Taggging", Utils.global.mapMain().toString());

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, mapMain, ConstVariable.GetEstimatePrice);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void postWaitingTime(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("ride_id", map.get("ride_id").toString());
        Global.mapMain.put("waiting_start_time", map.get("waiting_start_time").toString());
        Global.mapMain.put("waiting_end_time", map.get("waiting_end_time").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_ADD_WAITING_TIME);
        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Add_Waiting_Time);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getAdditionalStops(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("ride_id", map.get("ride_id").toString());

      /*  Utils.global.mapMain.put("user_id","132");
        Utils.global.mapMain.put("ride_id","703");*/

        Global.mapMain.put(ConstVariable.URL, Settings.URL_UNPLANNED_STOPS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Set_Additional_Stops);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getPartnerRideUserDetailsRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put("ride_id", map.get("ride_id").toString());

      /*  Utils.global.mapMain.put("user_id","132");
        Utils.global.mapMain.put("ride_id","703");*/

        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PARTNERFUTURERIDEDETAILS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.PartnerFutureRideDetails);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getActivePartnerRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put(ConstVariable.URL, Settings.URL_ACTIVEPARTNER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.ActivePartner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getServiceActivePartnerReq(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put(ConstVariable.URL, Settings.URL_ACTIVEPARTNER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Service_Active_Partner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getDriverTypePartnerReq(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put(ConstVariable.URL, Settings.URL_ACTIVEPARTNER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Driver_Active_Partner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void contactusRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.ID, id);
        Global.mapMain.put("fullname", map.get("fullname").toString());
        Global.mapMain.put(ConstVariable.EMAIL, map.get(ConstVariable.EMAIL).toString());
        Global.mapMain.put("message", map.get("message").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_CONTACTUS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.ContactUs);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void forgotRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.ID, id);
        Global.mapMain.put("email", map.get("email").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_FORGETPASSWORD);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.ForgetPassword);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void driverOnlineStatusRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());

        String vername = "";
        try {
            PackageInfo pInfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0);
            vername = pInfo.versionName;
            // verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("datetime", df.format(c));
        Global.mapMain.put("version", vername);
        Global.mapMain.put("timezone", tz.getID());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_DRIVER_ONLINE_STATUS_REQUEST);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.OnlineStatusRequest);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }

    }

    public static void driverRoleRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String rid = pref1.getRideId();

        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("rideid", rid);
        Global.mapMain.put(ConstVariable.URL, Settings.URL_DRIVERROLE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.DriverRole);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void driverOnlineRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("status", map.get("status").toString());
        Global.mapMain.put("version", "yes");
        Global.mapMain.put(ConstVariable.URL, Settings.URL_DRIVER_ONLINE_REQUEST);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.DriverOnlineRequest);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void partnerLookingRequest(Context mcontext, HashMap<String, Object> map) {
        Utils.global.mapMain();
        Global.mapMain.put("request_id", map.get("request_id").toString());
        Global.mapMain.put("status", map.get("status").toString());
        Global.mapMain.put(ConstVariable.URL, Settings.URL_PARTNERLOOKING);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.PartnerLooking);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void deviceTokenRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put("devicetoken", map.get("devicetoken").toString());
        Global.mapMain.put("device_type", "android");
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DEVICE_TOKEN_UPDATION);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Device_Token_Update);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void addBankAccountRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("json", map.get("json").toString());
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_ADDBANKACCOUNT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.AddBankAccount);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void editPersonalInfoRequest(Context mcontext, Uri uri, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("profilepic", map.get("profilepic").toString());
        Global.mapMain.put(ConstVariable.FULL_NAME, map.get(ConstVariable.FULL_NAME).toString());
        Global.mapMain.put(ConstVariable.LAST_NAME, map.get(ConstVariable.LAST_NAME).toString());
        Global.mapMain.put(ConstVariable.MOBILE, map.get(ConstVariable.MOBILE).toString());
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_EDIT_PERSONAL_INFO);

        if (Utils.isNetworkAvailable(mcontext)) {
            Utils.uploadImage(mcontext, uri, Global.mapMain, ConstVariable.Edit_Personal_Info);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void editVehicleInfoRequest(Context mcontext, Uri uri, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("vehicle_type", map.get("vtype").toString());
        Global.mapMain.put("vehicle_model", map.get("vmodel").toString());
        Global.mapMain.put("vehile_making_year", map.get("makingyear").toString());
        Global.mapMain.put("vehicle_image", map.get("vehicle_image").toString());
        Global.mapMain.put("documents_image", map.get("documents_image").toString());
        Global.mapMain.put("license_image", map.get("license_image").toString());
        Global.mapMain.put("driver_abstract", map.get("driver_abstract").toString());
        Global.mapMain.put("uri_two", map.get("uri_two").toString());
        Global.mapMain.put("uri_three", map.get("uri_three").toString());
        Global.mapMain.put("uri_four", map.get("uri_four").toString());
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_EDIT_VEHICLE_INFO);

        if (Utils.isNetworkAvailable(mcontext)) {
            Utils.uploadImage(mcontext, uri, Global.mapMain, ConstVariable.Edit_Vehicle_Info);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void updateDriverLocationRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("latitude", map.get("latitude").toString());
        Global.mapMain.put("longitude", map.get("longitude").toString());
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DRIVERLOCATIONUPDATE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.DriverLocationUpdate);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void DriverAcceptRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put("ride_id", map.get("ride_id"));
        Global.mapMain.put("status", map.get("status"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DRIVERACCEPT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.DriverAccept);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void partnerAcceptRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put("ride_id", map.get("ride_id"));
        Global.mapMain.put("status", map.get("status"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PARTNERACCEPT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.PartnerAccept);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void rideCompleteRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("userid", map.get("userid"));
        Global.mapMain.put("driverid", map.get("driverid"));
        Global.mapMain.put("ride_id", map.get("rideid"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_COMPLETERIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.RideComplete);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void futureRideCompleteRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("userid", map.get("userid"));
        Global.mapMain.put("driverid", map.get("driverid"));
        Global.mapMain.put("ride_id", map.get("rideid"));
        Global.mapMain.put("unplanned_stops", map.get("unplanned_stops"));
        Global.mapMain.put("waiting_time", map.get("waiting_time"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_FUTURERIDECOMPLETE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.FutureRideComplete);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void futureRideStartRequest(Context mcontext, HashMap<String, Object> map) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", map.get("driverid"));
        Global.mapMain.put("rideid", map.get("rideid"));
        Global.mapMain.put("time", df.format(c));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_FUTURERIDESTART);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Future_Ride_Start);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void currentRideStartRequest(Context mcontext, HashMap<String, Object> map) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", map.get("driverid"));
        Global.mapMain.put("rideid", map.get("rideid"));
        Global.mapMain.put("time", df.format(c));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_CURRENTRIDESTART);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.CurrentRideStart);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void waitingChargesRequest(Context mcontext, HashMap<String, Object> map) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String rid = pref1.getRideId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put("rideid", rid);
        Global.mapMain.put("waiting_charges_start_time", df.format(c));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_WAITING_CHARGE_START);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Waiting_Charge_Start);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void waitingChargesFrideRequest(Context mcontext, HashMap<String, Object> map) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String rid = map.get("rideid").toString();
        Log.e(TAG, "rMapRRRR " + rid);
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put("rideid", rid);
        Global.mapMain.put("waiting_charges_start_time", df.format(c));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_WAITING_CHARGE_START);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Waiting_Charge_F_Ride_Start);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }


//
//    public static void waitingChargesAmount(Context mcontext, HashMap<String, Object> map) {
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
//
//        SavePref pref1 = new SavePref();
//        pref1.SavePref(mcontext);
//        String id = pref1.getUserId();
//        String rid = map.get("rideid").toString();
//
//        Log.logError(TAG , "rMapRRRR "+rid);
//
//        new Utils(mcontext);
//        Utils.global.mapMain();
//        Utils.global.mapMain.put("driverid", id);
//        Utils.global.mapMain.put("rideid", rid);
//        Utils.global.mapMain.put("waiting_charges_start_time", df.format(c));
//        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_WAITING_CHARGE_START);
//
//        if (Utils.isNetworkAvailable(mcontext)) {
//            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.WaitingChargeFRideStart);
//        } else {
//            Utils.showInternetErrorMessage(mcontext);
//        }
//    }
//
//


    public static void paymentRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("user_id", map.get("user_id"));
        Global.mapMain.put("booking_id", map.get("booking_id"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PAYMENTDEDUCTION);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Payment);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void futurepaymentRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("user_id", map.get("user_id"));
        Global.mapMain.put("booking_id", map.get("booking_id"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_FUTUREPAYMENTDEDUCTION);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.FuturePayment);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getStopLlocationsRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("userid", id);

        if (map.containsKey("rideid"))
            Global.mapMain.put("rideid", map.get("rideid").toString());

        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_STOPLOCATIONSLIST);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.StopLocations);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void partnerIntimationRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", map.get("driverid"));
        Global.mapMain.put("rideid", map.get("rideid"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_INTIMATIONTOPARTNER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.PartnerIntimation);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void partnerFRideIntimationRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", map.get("driverid"));
        Global.mapMain.put("rideid", map.get("rideid"));
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_FRIDEINTIMATIONTOPARTNER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.FRidePartnerIntimation);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void addPartnerRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put("pname", map.get("name").toString());
        Global.mapMain.put("pemail", map.get("email").toString());
        Global.mapMain.put("pmobile", map.get("mobile").toString());

        Global.mapMain.put(ConstVariable.URL, Settings.URL_ADDPARTNER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.AddPartner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getPartnerDetailsRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String typ = pref1.getaccepttype();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("ride_id", map.get("ride_id").toString());
        Global.mapMain.put("type", typ);
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_GETPARTNERORDRIVER);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.GetPartnerORDriver);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }
}

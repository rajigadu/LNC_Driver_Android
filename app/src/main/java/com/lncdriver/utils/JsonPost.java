package com.lncdriver.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;

import com.lncdriver.activity.ActivityDriverChat;
import com.lncdriver.activity.ActivityPartnerChat;
import com.lncdriver.activity.ActivityUserChat;
import com.lncdriver.activity.Forgot;
import com.lncdriver.activity.Navigation;
import com.lncdriver.activity.RatingActivity;
import com.lncdriver.activity.ServiceType;
import com.lncdriver.activity.StopLocationsList;
import com.lncdriver.activity.ViewCustomerFRideDetails;
import com.lncdriver.activity.ViewPartnerRideDetails;
import com.lncdriver.fragment.CurrentRides;
import com.lncdriver.fragment.DFutureRideHistory;
import com.lncdriver.fragment.DriverFutureRides;
import com.lncdriver.fragment.DriverType;
import com.lncdriver.fragment.FutureDriverRequests;
import com.lncdriver.fragment.FuturePartnerRequests;
import com.lncdriver.fragment.PFutureRideHistory;
import com.lncdriver.fragment.PartnerFutureRides;
import com.lncdriver.fragment.Partners;
import com.lncdriver.model.SavePref;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lncdriver.fragment.DriverFutureRides.isListEmpty;
import static com.lncdriver.fragment.DriverFutureRides.mcontext;

/**
 * Created by narip on 2/4/2017.
 */
public class JsonPost implements ConstVariable {
    public static String TAG = "JsonPost";
    static String events;

    private static String mainRespon = "";


    public synchronized static void getNetworkResponse(final Context context, ProgressBar pBar,
                                                       final HashMap<String, Object> nameValuePairs, final int mode) {

        Utils.logError(TAG, "send events are >>> " + nameValuePairs.get(URL).toString() + nameValuePairs);

        try {
            boolean isPop = true;
            if (mode == UserLocationUpdate)
                isPop = false;
            if (mode == Device_Token_Update)
                isPop = false;
            if (mode == OnlineStatusRequest)
                isPop = false;
            if (mode == DriverLocationUpdate)
                isPop = false;
            if (mode == DriverAccept)
                isPop = false;
            if (mode == AddPartner)
                isPop = false;
            if (mode == CCRide)
                isPop = false;
            if (mode == PartnerLooking)
                isPop = false;
            if (mode == UserChat)
                isPop = false;
            if (mode == UserChatList)
                isPop = false;
            if (mode == DriverChat)
                isPop = false;
            if (mode == DriverChatList)
                isPop = false;
            if (mode == PartnerChat)
                isPop = false;
            if (mode == PartnerChatList)
                isPop = false;
            if (mode == DriverOnlineRequest)
                isPop = false;
            try {
                if (isPop)
                    Utils.initiatePopupWindow(context, "Please wait request is in progress...");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
            //,(new JSONObject(nameValuePairs)).toString());

            ServiceApiGet service = ServiceGenerator.createService(ServiceApiGet.class);
            Call<ResponseBody> response = service.login(nameValuePairs.get(URL).toString(), nameValuePairs);

            nameValuePairs.remove(URL);
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> rawResponse) {
                    try {
                        if (Utils.progressDialog != null) {
                            Utils.progressDialog.dismiss();
                            Utils.progressDialog = null;
                        }
                        String result = rawResponse.body().string();
                        String fResult = rawResponse.body().string();
                        Log.e(TAG + "61", "RetroFit2.0 :RetroGetResult: " + result);

                        mainRespon = result;

                        switch (mode) {

                            case GetEstimatePrice:
                                try {
                                    JSONObject jObj = new JSONObject(result);

                                    Log.e(TAG , "XXXXXXXXXX "+jObj.toString());

                                    //boolean waitingStatus = jObj.optString("status").equals("1");
                                    ViewCustomerFRideDetails.Instance.getEstimateResponse(jObj);
                                    //Utils.toastTxt(jObj.optString("msg"), mcontext);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case Add_Waiting_Time:
                                try {
                                    JSONObject jObj = new JSONObject(result);
                                    boolean waitingStatus = jObj.optString("status").equals("1");
                                    ViewCustomerFRideDetails.Instance.setWaitingTimeStstus(waitingStatus);
                                    Utils.toastTxt(jObj.optString("msg"), mcontext);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case Set_Additional_Stops:

                                break;

                            case Login:
                                result = JsonHelper.getResults(result, context, mode);
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    if (Global.profiledata.containsKey(ConstVariable.ID) &&
                                            !Global.profiledata.get(ConstVariable.ID).toString()
                                                    .equalsIgnoreCase(""))
                                        pref1.setUserId(Global.profiledata.get(ConstVariable.ID).toString());
                                    if (Global.profiledata.containsKey("first_name") &&
                                            !Global.profiledata.get("first_name").toString().
                                                    equalsIgnoreCase(""))
                                        pref1.setUserFName(Global.profiledata.get("first_name").toString());
                                    if (Global.profiledata.containsKey("last_name") &&
                                            !Global.profiledata.get("last_name").toString().
                                                    equalsIgnoreCase(""))
                                        pref1.setUserLName(Global.profiledata.get("last_name").toString());
                                    if (Global.profiledata.containsKey("email") &&
                                            !Global.profiledata.get("email").toString().equalsIgnoreCase(""))
                                        pref1.setEmail(Global.profiledata.get("email").toString());
                                    if (Global.profiledata.containsKey(ConstVariable.MOBILE) &&
                                            !Global.profiledata.get(ConstVariable.MOBILE).toString()
                                                    .equalsIgnoreCase(""))
                                        pref1.setMobile(Global.profiledata.get(ConstVariable.MOBILE).toString());
                                    if (Global.profiledata.containsKey("profile_pic") &&
                                            !Global.profiledata.get("profile_pic").toString().
                                                    equalsIgnoreCase(""))
                                        pref1.setImage(Global.profiledata.get("profile_pic").toString());

                                    if (Global.profiledata.containsKey("rating") &&
                                            !Global.profiledata.get("rating").toString().
                                                    equalsIgnoreCase(""))
                                        pref1.setRating(Global.profiledata.get("rating").toString());

                                    new Utils(context);
                                    Utils.toastTxt("You are login successfully", Utils.context);
                                    Utils.startActivity(Utils.context, ServiceType.class);
                                    //com.lncdriver.activity.Login.Instance.closeActivity();//
                                    com.lncdriver.activity.Login.Instance.registrationIDtoServer();
                                } else {
                                    new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.showOkDialog(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    // Utils.showOkDialog( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case ForgetPassword:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    Utils.toastTxt(jsonObject.optString("message"), context);
                                    if (jsonObject.has("status") && jsonObject.optString("status").equals("1")) {
                                        Forgot.Instance.closeActivity();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case Registration:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "146","signup successfull====");
                                    Utils.longToastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    Utils.startActivity(context, com.lncdriver.activity.Login.class);
                                } else {
                                    new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case ContactUs:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                try {
                                    JSONObject cObj = new JSONObject(result);
                                    if (cObj.optString("status").equals("1")) {
                                        com.lncdriver.activity.ContactUs.Instance.closeactivity();
                                    }
                                    Utils.toastTxt(cObj.optString("msg"), context);
                                    //{"msg":"Thanks for contacting with us. We will get back to you as soon as possible .","status":"1"}
                                } catch (Exception e) {

                                }
                                /*result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                } else {
                                    new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }*/
                                break;
                            case ChangePassword:

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    Utils.toastTxt(jsonObject.optString("msg"), context);
                                    if (jsonObject.has("status") && jsonObject.optString("status").equals("1")) {
                                        com.lncdriver.activity.ChangePassword.Instance.closeactivity();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                               /* // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.lncdriver.activity.ChangePassword.Instance.closeActivity();
                                } else {
                                    new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }*/
                                break;
                            case DriverOnlineRequest:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    com.lncdriver.fragment.Home.Instance.updateOnloineStatus(Global.mapMain);
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case Log_Out:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    Navigation.Instance.afterLogoutStatus();
                                } else {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;

                            case Partner_Req_Type:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    Navigation.nid = 4;
                                    com.lncdriver.fragment.FutureRides.id = 0;
                                    isListEmpty = true;
                                    Intent intent = new Intent(context, Navigation.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                    //Utils.startActivity(mcontext, Navigation.class);
                                    ((Activity) context).finish();

                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;

                            case ServiceType:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);

                                    if (nameValuePairs.containsKey("utype") && nameValuePairs.get("utype").toString().equalsIgnoreCase("1")) {
                                        if (Global.mapMain.containsKey("service_type") && !Global.mapMain.get("service_type").toString().equalsIgnoreCase(""))
                                            pref1.setdrivertype(Global.mapMain.get("service_type").toString());
                                        Navigation.nid = 0;
                                        Utils.startActivity(context, Navigation.class);
                                    } else if (nameValuePairs.containsKey("utype") && nameValuePairs.get("utype").toString().equalsIgnoreCase("2")) {
                                        if (Global.mapMain.containsKey("service_type") && !Global.mapMain.get("service_type").toString().equalsIgnoreCase(""))
                                            pref1.setdrivertype(Global.mapMain.get("service_type").toString());

                                        DriverType.updateDriverType();
                                    }
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case AddBankAccount:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.lncdriver.activity.AddBankAccount.Instance.closeactivity();
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case DashBoard:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata==== " + Global.availRideslist);
                                    // Test.loadRequestsList(context,Utils.global.availRideslist,"mode");
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case DriverAccept:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "1234567", "login  mapdata===="+  Utils.global.userlist);
                                    //Home.Instance.getUserDetails( Utils.global.userlist);

                                    HashMap<String, Object> map = new HashMap<>();
                                    if (Global.userlist.size() > 0) {
                                        for (Map.Entry<String, Object> entry : Global.userlist.get(0).entrySet()) {
                                            String key = entry.getKey();
                                            Object value = entry.getValue();

                                            if (!value.toString().equalsIgnoreCase("null")) {
                                                map.put(key, value);
                                            }
                                        }
                                    }
                                    //while (map.values().remove(null));

                                    // Utils.logError("" + "map-abcd--123", "ok  mapdata===="+map);

                                    Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "2");
                                    broadcast.putExtra("status", "1");
                                    broadcast.putExtra("data", (Serializable) map);
                                    context.sendBroadcast(broadcast);
                                } else {
                                    // new Utils(context);
                                    // Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case PartnerAccept:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "1234567", "login  mapdata===="+  Utils.global.userlist);
                                    //Home.Instance.getUserDetails( Utils.global.userlist);

                                    HashMap<String, Object> map = new HashMap<>();
                                    if (Global.userlist.size() > 0) {
                                        for (Map.Entry<String, Object> entry : Global.userlist.get(0).entrySet()) {
                                            String key = entry.getKey();
                                            Object value = entry.getValue();

                                            if (!value.toString().equalsIgnoreCase("null")) {
                                                map.put(key, value);
                                            }
                                        }
                                    }
                                    //while (map.values().remove(null));

                                    // Utils.logError("" + "map-abcd--123", "ok  mapdata===="+map);

                                    Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "2");
                                    broadcast.putExtra("status", "2");
                                    broadcast.putExtra("data", (Serializable) map);
                                    context.sendBroadcast(broadcast);
                                } else {
                                    // new Utils(context);
                                    // Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);

                                  /*  if (nameValuePairs.containsKey("ride_id") && !nameValuePairs.get("ride_id").toString().equalsIgnoreCase(""))
                                    {
                                        Navigation.Instance.partnerLookingRequest(nameValuePairs.get("ride_id").toString(),"1");
                                    }*/
                                }
                                break;
                            case RideComplete:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid","3");
                                    context.sendBroadcast(broadcast);*/

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setisridestart("");

                                    /*Intent intent = new Intent(context, RatingActivity.class);
                                    intent.putExtra("map", (Serializable) Utils.global.dmap);
                                    context.startActivity(intent);*/

                                    com.lncdriver.fragment.Home.paymentRequest(context);
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case FutureRideComplete:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid","3");
                                    context.sendBroadcast(broadcast);*/

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setisfridestart("");

                                    ViewCustomerFRideDetails.paymentRequest(context);
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case Future_Ride_Start:
                                Log.e(TAG + "144", "response1111 " + result);
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null")) {
                                    Log.e(TAG + "144", "response2222 " + result);
                                    result = JsonHelper.getResults(result, context, mode);

                                    Log.e(TAG , "mainRespon::: "+mainRespon);
                                } else
                                    result = "failure";
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid","3");
                                    context.sendBroadcast(broadcast);*/

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setisfridestart("ok");

                                    ViewCustomerFRideDetails.Instance.rideStartStatus(context);
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case CurrentRideStart:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid","3");
                                    context.sendBroadcast(broadcast);*/

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setisridestart("ok");

                                    com.lncdriver.fragment.Home.Instance.statusCurrentRideStart();
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;

                            case OnlineStatusRequest:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                   /* new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    //Navigation.Instance.MainpopulateOnlineStatusFields(Utils.global.mapMain.get("online_status").toString(),Utils.global.mapMain.get(ConstVariable.MESSAGE).toString());
                                    Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid","1");
                                    broadcast.putExtra("status", Utils.global.mapMain.get("online_status").toString());
                                    broadcast.putExtra("data", Utils.global.mapMain.get(ConstVariable.MESSAGE).toString());
                                    context.sendBroadcast(broadcast);*/
                                    com.lncdriver.fragment.Home.Instance.updateOnloineStatus(Global.mapMain);
                                    //Navigation.assignServiceTime(Utils.global.extramap);
                                    // Test.Instance.populateOnlineStatusFields(Utils.global.mapMain.get("online_status").toString(),Utils.global.mapMain.get(ConstVariable.MESSAGE).toString());
                                } else {
                                    new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;

                            case CCRide:
                                Log.e(TAG + "106", "response " + result);

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.has("time_left") &&
                                            !jsonObject.optString("time_left").trim().equals("")) {
                                        //Navigation.assignServiceTime(jsonObject);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";

                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    HashMap<String, Object> map = new HashMap<>();

                                    for (Map.Entry<String, Object> entry : Global.cridelist.get(0).entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();

                                        if (!value.toString().equalsIgnoreCase("null")) {
                                            map.put(key, value);
                                        }
                                    }

                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "5");
                                    broadcast.putExtra("status", "2");
                                    broadcast.putExtra("data", (Serializable)map);
                                    context.sendBroadcast(broadcast);*/
                                    com.lncdriver.fragment.Home.Instance.getCurrentRideDetails(map, "1");

                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "6");
                                    broadcast.putExtra("status", "2");
                                    broadcast.putExtra("data", "");
                                    context.sendBroadcast(broadcast);*/

                                    com.lncdriver.fragment.Home.Instance.getCurrentRideDetails(null, "2");
                                }
                                break;

                            case NRide:
                                Log.e(TAG + "106", "response " + result);

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.has("time_left") &&
                                            !jsonObject.optString("time_left").trim().equals("")) {
                                        Navigation.assignNextServiceTime(jsonObject);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;


                            case CurrentRide:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "73", "login  mapdata===="+  Utils.global.cridelist);
                                    CurrentRides.loadRequestsList(context, Global.cridelist, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case PartnersList:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "73", "login  mapdata===="+  Utils.global.cridelist);
                                    Partners.loadRequestsList(context, Global.partnersList, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case AddPartner:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.lncdriver.activity.AddPartner.Instance.closeActivity();
                                    Navigation.nid = 3;
                                    Utils.startActivity(context, Navigation.class);
                                } else {
                                    new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case ActivatePartner:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    Utils.toastTxt(jsonObject.optString("msg"), context);
                                    if (jsonObject.optString("status").equalsIgnoreCase("1"))
                                        Partners.MyrequestsListRequest();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case GetPartnerORDriver:
                                result = JsonHelper.getResults(result, context, mode);
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.partneORDriverList);
                                    com.lncdriver.fragment.Home.Instance.showPartnerORDriverDetailsDilog
                                            (Global.partneORDriverList.get(0));
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case RideHistory:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "73", "login  mapdata===="+  Utils.global.ridelist);
                                    com.lncdriver.fragment.RideHistory.loadRequestsList(context, Global.ridelist, "mode");
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case FutureRideHistory:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "73", "login  mapdata===="+  Utils.global.ridelist);
                                    DFutureRideHistory.loadRequestsList(context, Global.fridehistorylist, "mode");
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case PartnerFutureRideHistory:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "73", "login  mapdata===="+  Utils.global.ridelist);
                                    PFutureRideHistory.loadRequestsList(context, Global.partnerfridehistorylist, "mode");
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case Payment:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.lncdriver.fragment.Home.Instance.onCompleteRideStatus();

                                    Intent intent = new Intent(context, RatingActivity.class);
                                    intent.putExtra("map", (Serializable) Global.dmap);
                                    context.startActivity(intent);

                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.lncdriver.fragment.Home.Instance.onCompleteRideStatus();
                                }
                                break;

                            case FuturePayment:
                                // Log.logError(TAG+"144", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.logError(TAG + "97","images urls list==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    Utils.logError("TagMap ", Global.mapMain.toString());
                                    Navigation.nid = 5;
                                    /*Intent intent = new Intent(context, RatingActivity.class);
                                    intent.putExtra("map", (Serializable) Utils.global.mapMain);
                                    context.startActivity(intent);*/
                                    Utils.startActivity(context, Navigation.class);
                                } else {
                                    // new Utils(context);
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    Navigation.nid = 5;
                                    Utils.startActivity(context, Navigation.class);
                                }
                                break;

                            case UserChatList:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.userchatlist);
                                    ActivityUserChat.loadRequestsList(context, Global.userchatlist, "mode");
                                } else {
                                    Global.userchatlist.clear();
                                    ActivityUserChat.loadRequestsList(context, Global.userchatlist, "mode");
                                }
                                break;
                            case DriverChatList:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.driverchatlist);
                                    ActivityDriverChat.loadRequestsList(context, Global.driverchatlist, "mode");
                                } else {
                                    Global.driverchatlist.clear();
                                    ActivityDriverChat.loadRequestsList(context, Global.driverchatlist, "mode");
                                }
                                break;
                            case PartnerChatList:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.partnerchatlist);
                                    ActivityPartnerChat.loadRequestsList(context, Global.partnerchatlist, "mode");
                                } else {
                                    Global.partnerchatlist.clear();
                                    ActivityPartnerChat.loadRequestsList(context, Global.partnerchatlist, "mode");
                                }
                                break;
                            case UserChat:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    ActivityUserChat.chatListRequest();
                                } else {
                                    Global.userchatlist.clear();
                                    ActivityUserChat.loadRequestsList(context, Global.userchatlist, "mode");
                                }
                                break;
                            case DriverChat:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    ActivityDriverChat.chatListRequest();
                                } else {
                                    Global.driverchatlist.clear();
                                    ActivityDriverChat.loadRequestsList(context, Global.driverchatlist, "mode");
                                }
                                break;
                            case PartnerChat:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    ActivityPartnerChat.chatListRequest();
                                } else {
                                    Global.partnerchatlist.clear();
                                    ActivityPartnerChat.loadRequestsList(context, Global.partnerchatlist, "mode");
                                }
                                break;
                            case DriverRole:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    Utils.logError("" + "101", "driver role mapdata====" + Global.mapMain);
                                    // ActivityUserChat.loadRequestsList(context,Utils.global.driverchatlist,"mode");
                                    com.lncdriver.fragment.Home.getDriverTypeResponce(Global.mapMain);
                                } else {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case ActivePartner:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                                        Utils.toastTxt(jsonObject.optString("msg"), mcontext);
                                    }
                                    com.lncdriver.activity.AuthenticatePartner.Instance.populatePartnerDetails(jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case Service_Active_Partner:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                                        Utils.toastTxt(jsonObject.optString("msg"), mcontext);
                                    }
                                    com.lncdriver.activity.ServiceType.Instance.populatePartnerDetails(jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;

                            case Driver_Active_Partner:

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                                        Utils.toastTxt(jsonObject.optString("msg"), DriverType.mcontext);
                                    }
                                    DriverType.Instance.populatePartnerDetails(jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;

                            case AuthenticatePartner:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    Navigation.nid = 0;
                                    Utils.startActivity(context, Navigation.class);

                                    if (Global.mapMain.containsKey("service_type")) {
                                        if (Global.mapMain.get("service_type").toString().equalsIgnoreCase("2")) {
                                            Utils.partnerLookingRequest("p", context);
                                        } else {

                                        }
                                    }
                                } else {

                                }
                                break;
                            case PartnerIntimation:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    // Utils.logError("" + "101", "driver role mapdata===="+  Utils.global.mapMain);
                                    // ActivityUserChat.loadRequestsList(context,Utils.global.driverchatlist,"mode");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                } else {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case FRidePartnerIntimation:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    // Utils.logError("" + "101", "driver role mapdata===="+  Utils.global.mapMain);
                                    // ActivityUserChat.loadRequestsList(context,Utils.global.driverchatlist,"mode");
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                } else {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case StopLocations:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.stopLocationslist);
                                    StopLocationsList.loadRequestsList(context, Global.stopLocationslist, "mode");
                                } else {

                                }
                                break;

                            case New_Payment_History:
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.paymentlist);
                                    com.lncdriver.fragment.PaymentListHistory.loadRequestsList(context, Global.paymentlist, "mode");
                                } else {

                                }
                                break;

                            case Payment_History:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "failure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.paymentlist);
                                    com.lncdriver.fragment.PaymentHistory.loadRequestsList(context, Global.paymentlist, "mode");
                                } else {

                                }
                                break;
                            case EdtBankAccount:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    com.lncdriver.activity.AddBankAccount.Instance.populateBankDetails(Global.bankdetailsList.get(0));
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case GetVehicleInfo:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    com.lncdriver.activity.EditVehicleInfo.Instance.populateVehicleDetails(Global.vehicledetailsList.get(0));
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case FutureRides:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    HashMap<String, Object> dmap = new HashMap<>();
                                    for (Map.Entry<String, Object> entry : Global.frideslist.get(0).entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();

                                        if (!value.toString().equalsIgnoreCase("null")) {
                                            dmap.put(key, value);
                                        }
                                    }
                                    Utils.logError("" + "73", "login  mapdata====" + Global.frideslist);
                                    FutureDriverRequests.loadRequestsList(context, Global.frideslist, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case DriverIncomingFutureRides:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.driverincomingfrideslist);
                                    FutureDriverRequests.loadRequestsList(context, Global.driverincomingfrideslist, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case PartnerIncomingFutureRides:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.partnerincomingfrideslist);
                                    FuturePartnerRequests.loadRequestsList(context, Global.partnerincomingfrideslist, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;

                            case DriverFutureRides:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result, context, mode);
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.driverfrideslist);
                                    com.lncdriver.fragment.DriverFutureRides.loadRequestsList(context, Global.driverfrideslist, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case PartnerFutureRides:
                                result = JsonHelper.getResults(result, context, mode);
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login  mapdata====" + Global.partnerfrideslist);
                                    com.lncdriver.fragment.PartnerFutureRides.loadRequestsList(context, Global.partnerfrideslist, "mode");
                                } else {
                                    Global.partnerfrideslist.clear();
                                   // com.lncdriver.fragment.PartnerFutureRides.Instance.loadRequestsList(context, Utils.global.partnerfrideslist, "mode");
                                }
                                break;
                            case DriverAcceptFutureRide:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    Utils.toastTxt(jsonObject.optString("message"), context);
                                    if (jsonObject.optString("default").equalsIgnoreCase("no")) {
                                        FutureDriverRequests.Instance.intimation_FutureRide();
                                    } else {
                                        Navigation.nid = 4;
                                        com.lncdriver.fragment.FutureRides.id = 0;
                                        Utils.startActivity(context, Navigation.class);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;

                            case PartnerAcceptFutureRide:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.logError("" + "73", "login  mapdata===="+  Utils.global.userchatlist);
                                    Utils.toastTxt(Global.mapMain.get("message").toString(), context);

                                    Navigation.nid = 4;
                                    com.lncdriver.fragment.FutureRides.id = 1;
                                    com.lncdriver.fragment.PartnerFutureRides.rideCount = -1;
                                    Utils.startActivity(context, Navigation.class);
                                } else {
                                    Utils.toastTxt(Global.mapMain.get("message").toString(), context);
                                }
                                break;
                            case DriverFutureRideDetails:
                                try {
                                    JSONObject jsonOb = new JSONObject(result);
                                    Log.e(TAG + "193", "responsebool====" + jsonOb);

                                    // Log.logError(TAG+"106", "response" + result.toString());
                                    if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                        result = JsonHelper.getResults(result, context, mode);
                                    else
                                        result = "failure";

                                    if (result.equalsIgnoreCase(SUCCESS)) {
                                        Utils.logError("" + "73", "login mapdata====" + Global.driverfrideDetailslist);
                                        ViewCustomerFRideDetails.Instance.getFutureRideUserDetails(Global.driverfrideDetailslist, jsonOb);
                                    } else {
                                        ViewCustomerFRideDetails.Instance.getFutureRideUserDetails(null, null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case PartnerFutureRideDetails:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.logError("" + "73", "login mapdata====" + Global.partnerfrideDetailslist);
                                    ViewPartnerRideDetails.Instance.getFutureRideUserDetails(Global.partnerfrideDetailslist);
                                } else {
                                    ViewPartnerRideDetails.Instance.getFutureRideUserDetails(null);
                                }
                                break;
                            case Cancel_Ride:
                                Log.e(TAG + "245", "response" + result);
                                result = JsonHelper.getResults(result, context, mode);
                                Log.e(TAG + "247", "response bool====" + result);
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.lncdriver.activity.CancelRide.Instance.closeActivity();
                                    Utils.startActivity(context, Navigation.class);
                                    com.lncdriver.fragment.Home.Instance.onCompleteRideStatus();

                                    /*Intent broadcast = new Intent();
                                    broadcast.putExtra("isid", "12");
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("status", "12");
                                    broadcast.putExtra("data","");*/
                                } else {
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case CancelFutureRide:
                                Log.e(TAG + "245", "response" + result);
                                result = JsonHelper.getResults(result, context, mode);
                                Log.e(TAG + "247", "response bool====" + result);
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    Navigation.nid = 0;
                                    Utils.startActivity(context, Navigation.class);

                                    /*Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "12");
                                    broadcast.putExtra("status", "12");
                                    broadcast.putExtra("data","");*/
                                } else {
                                    // Log.logError("119","response"+result.toString());
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case Waiting_Charge_Start:
                                Log.e(TAG + "245", "response" + result);
                                result = JsonHelper.getResults(result, context, mode);
                                Log.e(TAG + "247", "response bool====" + result);
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.lncdriver.fragment.Home.Instance.waitingChargesOnStart();
                                } else {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case Waiting_Charge_F_Ride_Start:
                                Log.e(TAG + "245", "response" + result);
                                result = JsonHelper.getResults(result, context, mode);
                                Log.e(TAG + "247", "response bool====" + result);
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    ViewCustomerFRideDetails.Instance.waitingChargesOnFRideStart();
                                } else {
                                    Utils.toastTxt(Global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case Pending_Current_Rides:
                                // Log.logError(TAG+"106", "response" + result.toString());
                                if (!result.equalsIgnoreCase("") &&
                                        !result.equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result, context, mode);
                                else
                                    result = "faillure";
                                // Log.logError(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.logError("" + "73", "login  mapdata===="+  Utils.global.cridelist);
                                    com.lncdriver.fragment.PendingCurrentRides.loadRequestsList(context, Global.pendingCrideslist, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;

                            case Cancel_Future_Ride_By_Partner:
                                try {
                                    JSONObject jsonObj = new JSONObject(result);
                                    int count = jsonObj.optInt("ride_count");
                                    com.lncdriver.fragment.PartnerFutureRides.rideCount = count;
                                    Utils.toastTxt(jsonObj.optString("msg"), context);
                                    ((Activity) context).finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                break;

                            case User_Feed_Back:
                                try {
                                    //JSONObject jsonObject = new JSONObject(rawResponse.body().string());
                                    Utils.toastTxt("Your feedback is submitted successfully", context);
                                    ((Activity) context).finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    if (Utils.progressDialog != null) {
                        Utils.progressDialog.dismiss();
                        Utils.progressDialog = null;
                    }
                    /*if(progressBar != null)
                    {
                        progressBar.setVisibility(View.GONE);
                    }*/
                    // other stuff...
                    //Utils.logError(TAG+"98","Exception==================Exception===================Exception"+throwable.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //Utils.logError(TAG+"104","Exception==================Exception===================Exception");
        } finally {

        }
    }

}

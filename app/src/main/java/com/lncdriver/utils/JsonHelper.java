package com.lncdriver.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by narip on 2/4/2017.
 */
public class JsonHelper implements ConstVariable {
    public static final String Response_Code = "status";
    public static final String Response_Text = "ResponseText";
    public static final String DATA = "data";
    public static final String ERROR_MSG = "errmsg";
    private static final String TAG = "JsonHelper";
    static String Tag = "jsonHelper";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static String dateFinal;
    public static int flags = DateUtils.FORMAT_ABBREV_RELATIVE;

    public static String getResults(String result, Context context, final int mode) {
        String response = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;

        if (result == null)
            return null;
        if (context == null)
            context = Global.getAppContext();
        try {
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

           /* int key = 0;
             key = jsonObject.getInt(Response_Code);
            key=1;

            if (Utils.global == null)
                Utils.global = new Global();

            Utils.logError(Tag+"55", key+""+mode);*/

            int key = 0;
            try {
                key = jsonObject.getInt(Response_Code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (com.lncdriver.utils.Utils.global == null)
                com.lncdriver.utils.Utils.global = new Global();
            // Utils.logError(Tag+"48", key+""+mode);

            switch (key) {
                case 0:
                    //response = jsonObject.optString(Response_Text);
                    response = FAILURE;
                    jsonArray = jsonObject.optJSONArray(DATA);
                    Utils.global.mapMain = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                    break;
                case 1:
                    response = ConstVariable.SUCCESS;
                    jsonArray = jsonObject.optJSONArray(DATA);
                    Utils.global.mapMain = Utils.GetJsonDataIntoMap(context, jsonArray, "");

                    if (mode == ConstVariable.OnlineStatusRequest) {
                        Utils.global.extramap.put("time_left", jsonObject.get("time_left").toString());
                    }

                    if (mode == ConstVariable.CCRide || mode == ConstVariable.DriverFutureRideDetails) {
                        Utils.global.ridePrices.clear();
                        Utils.global.ridePrices.put("ride_cost", jsonObject.get("ride_cost").toString());
                        Utils.global.ridePrices.put("waiting_charge", jsonObject.get("waiting_charge").toString());
                        Utils.global.ridePrices.put("total_cost", jsonObject.get("total_cost").toString());
                    }

                    if (mode == ConstVariable.Login) {
                        // Utils.logError(Tag + "69", "login  array==== " + jsonArray);

                        Utils.global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.SocialSignUp) {
                        // Utils.logError(Tag + "69", "login  array==== " + jsonArray);

                        Utils.global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.SocialStatus) {
                        // Utils.logError(Tag + "69", "login  array==== " + jsonArray);

                        Utils.global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.DashBoard) {
                        Utils.global.availRideslist.clear();
                        Utils.global.availRideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.availRideslist);
                    }
                    if (mode == ConstVariable.DriverAccept) {
                        Utils.global.userlist.clear();
                        Utils.global.userlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.userlist);
                    }
                    if (mode == ConstVariable.PartnerAccept) {
                        Utils.global.userlist.clear();
                        Utils.global.userlist.addAll((ArrayList<HashMap<String, Object>>)
                                (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.userlist);
                    }
                    if (mode == ConstVariable.PartnersList) {
                        Utils.global.partnersList.clear();
                        Utils.global.partnersList.addAll((ArrayList<HashMap<String, Object>>)
                                (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.partnersList);
                    }
                    if (mode == ConstVariable.GetPartnerORDriver) {
                        Utils.global.partneORDriverList.clear();
                        Utils.global.partneORDriverList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.partneORDriverList);
                    }
                    if (mode == ConstVariable.CurrentRide) {
                        Utils.global.cridelist.clear();
                        Utils.global.cridelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.cridelist);
                    }
                    if (mode == ConstVariable.Pending_Current_Rides) {
                        Utils.global.pendingCrideslist.clear();
                        Utils.global.pendingCrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.pendingCrideslist);
                    }

                    if (mode == ConstVariable.CCRide) {
                        Utils.global.cridelist.clear();
                        Utils.global.cridelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.cridelist);
                    }
                    if (mode == ConstVariable.RideHistory) {
                        Utils.global.ridelist.clear();
                        Utils.global.ridelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Utils.global.ridelist);
                    }
                    if (mode == ConstVariable.UserChatList) {
                        Utils.global.userchatlist.clear();
                        Utils.global.userchatlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.DriverChatList) {
                        Utils.global.driverchatlist.clear();
                        Utils.global.driverchatlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.PartnerChatList) {
                        Utils.global.partnerchatlist.clear();
                        Utils.global.partnerchatlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.ActivePartner) {
                        Utils.global.activepartnerlist.clear();
                        Utils.global.activepartnerlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.StopLocations) {
                        Utils.global.stopLocationslist.clear();
                        Utils.global.stopLocationslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.stopLocationslist);
                    }
                    if (mode == ConstVariable.Payment_History) {
                        Utils.global.paymentlist.clear();
                        Utils.global.paymentlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.paymentlist);
                    }
                    if (mode == ConstVariable.New_Payment_History) {
                        Utils.global.paymentlist.clear();
                        Utils.global.paymentlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.paymentlist);
                    }
                    if (mode == ConstVariable.EdtBankAccount) {
                        Utils.global.bankdetailsList.clear();
                        Utils.global.bankdetailsList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.bankdetailsList);
                    }
                    if (mode == ConstVariable.GetVehicleInfo) {
                        Utils.global.vehicledetailsList.clear();
                        Utils.global.vehicledetailsList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.vehicledetailsList);
                    }
                    if (mode == ConstVariable.Edit_Personal_Info) {
                        Utils.global.personaldetailsList.clear();
                        Utils.global.personaldetailsList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.personaldetailsList);
                    }
                    if (mode == ConstVariable.FutureRides) {
                        Utils.global.frideslist.clear();
                        Utils.global.frideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.frideslist);
                    }
                    if (mode == ConstVariable.DriverIncomingFutureRides) {
                        Utils.global.driverincomingfrideslist.clear();
                        Utils.global.driverincomingfrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.driverincomingfrideslist);
                    }
                    if (mode == ConstVariable.PartnerIncomingFutureRides) {
                        Utils.global.partnerincomingfrideslist.clear();
                        Utils.global.partnerincomingfrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.partnerincomingfrideslist);
                    }
                    if (mode == ConstVariable.DriverFutureRides) {
                        Utils.global.driverfrideslist.clear();
                        Utils.global.driverfrideslist.addAll((ArrayList<HashMap<String, Object>>)
                                (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.driverfrideslist);
                    }
                    if (mode == ConstVariable.PartnerFutureRides) {
                        Utils.global.partnerfrideslist.clear();
                        Utils.global.partnerfrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.partnerfrideslist);
                    }
                    if (mode == ConstVariable.DriverFutureRideDetails) {
                        Utils.global.driverfrideDetailslist.clear();
                        Utils.global.driverfrideDetailslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.driverfrideDetailslist);
                    }
                    if (mode == ConstVariable.PartnerFutureRideDetails) {
                        Utils.global.partnerfrideDetailslist.clear();
                        Utils.global.partnerfrideDetailslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.partnerfrideDetailslist);
                    }
                    if (mode == ConstVariable.FutureRideHistory) {
                        Utils.global.fridehistorylist.clear();
                        Utils.global.fridehistorylist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.fridehistorylist);
                    }
                    if (mode == ConstVariable.PartnerFutureRideHistory) {
                        Utils.global.partnerfridehistorylist.clear();
                        Utils.global.partnerfridehistorylist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));
                        Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.partnerfridehistorylist);
                    }
                    if (mode == ConstVariable.Future_Ride_Start) {
                        Log.e(TAG, "AAAAAAAAAAA "+jsonObject.toString());
                        Log.e(TAG, ""+jsonObject.get("ride_cost").toString());
                        Log.e(TAG, ""+jsonObject.get("waiting_charge").toString());
                        Log.e(TAG, ""+jsonObject.get("total_cost").toString());

                        Utils.global.ridePrices.clear();
                        Utils.global.ridePrices.put("ride_cost", jsonObject.get("ride_cost").toString());
                        Utils.global.ridePrices.put("waiting_charge", jsonObject.get("waiting_charge").toString());
                        Utils.global.ridePrices.put("total_cost", jsonObject.get("total_cost").toString());
                   //
                   // Utils.global.ridePrices.clear();
                  //  Utils.global.partnerfridehistorylist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));
                   // Utils.logError(Tag + "148", "array data2 ==== " + Utils.global.ridePrices);
                    }

            }
        } catch (Exception e) {
            // TODO: handle exception
            //Utils.logError(Tag + "79", "Exception=================Exception====================Exception");
            e.printStackTrace();
        } finally {
            jsonArray = null;
            jsonObject = null;
        }
        return response;
    }
}

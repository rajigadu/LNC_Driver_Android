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
                    Global.mapMain = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                    break;
                case 1:
                    response = ConstVariable.SUCCESS;
                    jsonArray = jsonObject.optJSONArray(DATA);
                    Global.mapMain = Utils.GetJsonDataIntoMap(context, jsonArray, "");

                    if (mode == ConstVariable.OnlineStatusRequest) {
                        Global.extramap.put("time_left", jsonObject.get("time_left").toString());
                    }

                    if (mode == ConstVariable.CCRide || mode == ConstVariable.DriverFutureRideDetails) {
                        Global.ridePrices.clear();
                        Global.ridePrices.put("ride_cost", jsonObject.get("ride_cost").toString());
                        Global.ridePrices.put("waiting_charge", jsonObject.get("waiting_charge").toString());
                        Global.ridePrices.put("total_cost", jsonObject.get("total_cost").toString());
                    }

                    if (mode == ConstVariable.Login) {
                        // Utils.logError(Tag + "69", "login  array==== " + jsonArray);

                        Global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.SocialSignUp) {
                        // Utils.logError(Tag + "69", "login  array==== " + jsonArray);

                        Global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.SocialStatus) {
                        // Utils.logError(Tag + "69", "login  array==== " + jsonArray);

                        Global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.DashBoard) {
                        Global.availRideslist.clear();
                        Global.availRideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.availRideslist);
                    }
                    if (mode == ConstVariable.DriverAccept) {
                        Global.userlist.clear();
                        Global.userlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.userlist);
                    }
                    if (mode == ConstVariable.PartnerAccept) {
                        Global.userlist.clear();
                        Global.userlist.addAll((ArrayList<HashMap<String, Object>>)
                                (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.userlist);
                    }
                    if (mode == ConstVariable.PartnersList) {
                        Global.partnersList.clear();
                        Global.partnersList.addAll((ArrayList<HashMap<String, Object>>)
                                (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.partnersList);
                    }
                    if (mode == ConstVariable.GetPartnerORDriver) {
                        Global.partneORDriverList.clear();
                        Global.partneORDriverList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.partneORDriverList);
                    }
                    if (mode == ConstVariable.CurrentRide) {
                        Global.cridelist.clear();
                        Global.cridelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.cridelist);
                    }
                    if (mode == ConstVariable.Pending_Current_Rides) {
                        Global.pendingCrideslist.clear();
                        Global.pendingCrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.pendingCrideslist);
                    }

                    if (mode == ConstVariable.CCRide) {
                        Global.cridelist.clear();
                        Global.cridelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.cridelist);
                    }
                    if (mode == ConstVariable.RideHistory) {
                        Global.ridelist.clear();
                        Global.ridelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "73", "login  mapdata====" + Global.ridelist);
                    }
                    if (mode == ConstVariable.UserChatList) {
                        Global.userchatlist.clear();
                        Global.userchatlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.DriverChatList) {
                        Global.driverchatlist.clear();
                        Global.driverchatlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.PartnerChatList) {
                        Global.partnerchatlist.clear();
                        Global.partnerchatlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.ActivePartner) {
                        Global.activepartnerlist.clear();
                        Global.activepartnerlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.logError(Tag + "73", "login  mapdata===="+  Utils.global.favlocationNavlist);
                    }
                    if (mode == ConstVariable.StopLocations) {
                        Global.stopLocationslist.clear();
                        Global.stopLocationslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.stopLocationslist);
                    }
                    if (mode == ConstVariable.Payment_History) {
                        Global.paymentlist.clear();
                        Global.paymentlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.paymentlist);
                    }
                    if (mode == ConstVariable.New_Payment_History) {
                        Global.paymentlist.clear();
                        Global.paymentlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.paymentlist);
                    }
                    if (mode == ConstVariable.EdtBankAccount) {
                        Global.bankdetailsList.clear();
                        Global.bankdetailsList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.bankdetailsList);
                    }
                    if (mode == ConstVariable.GetVehicleInfo) {
                        Global.vehicledetailsList.clear();
                        Global.vehicledetailsList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.vehicledetailsList);
                    }
                    if (mode == ConstVariable.Edit_Personal_Info) {
                        Global.personaldetailsList.clear();
                        Global.personaldetailsList.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.personaldetailsList);
                    }
                    if (mode == ConstVariable.FutureRides) {
                        Global.frideslist.clear();
                        Global.frideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.frideslist);
                    }
                    if (mode == ConstVariable.DriverIncomingFutureRides) {
                        Global.driverincomingfrideslist.clear();
                        Global.driverincomingfrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.driverincomingfrideslist);
                    }
                    if (mode == ConstVariable.PartnerIncomingFutureRides) {
                        Global.partnerincomingfrideslist.clear();
                        Global.partnerincomingfrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.partnerincomingfrideslist);
                    }
                    if (mode == ConstVariable.DriverFutureRides) {
                        Global.driverfrideslist.clear();
                        Global.driverfrideslist.addAll((ArrayList<HashMap<String, Object>>)
                                (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.driverfrideslist);
                    }
                    if (mode == ConstVariable.PartnerFutureRides) {
                        Global.partnerfrideslist.clear();
                        Global.partnerfrideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.partnerfrideslist);
                    }
                    if (mode == ConstVariable.DriverFutureRideDetails) {
                        Global.driverfrideDetailslist.clear();
                        Global.driverfrideDetailslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.driverfrideDetailslist);
                    }
                    if (mode == ConstVariable.PartnerFutureRideDetails) {
                        Global.partnerfrideDetailslist.clear();
                        Global.partnerfrideDetailslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.partnerfrideDetailslist);
                    }
                    if (mode == ConstVariable.FutureRideHistory) {
                        Global.fridehistorylist.clear();
                        Global.fridehistorylist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.logError(Tag + "148", "array data2 ==== " + Global.fridehistorylist);
                    }
                    if (mode == ConstVariable.PartnerFutureRideHistory) {
                        Global.partnerfridehistorylist.clear();
                        Global.partnerfridehistorylist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));
                        Utils.logError(Tag + "148", "array data2 ==== " + Global.partnerfridehistorylist);
                    }
                    if (mode == ConstVariable.Future_Ride_Start) {
                        Log.e(TAG, "AAAAAAAAAAA "+jsonObject.toString());
                        Log.e(TAG, ""+jsonObject.get("ride_cost").toString());
                        Log.e(TAG, ""+jsonObject.get("waiting_charge").toString());
                        Log.e(TAG, ""+jsonObject.get("total_cost").toString());

                        Global.ridePrices.clear();
                        Global.ridePrices.put("ride_cost", jsonObject.get("ride_cost").toString());
                        Global.ridePrices.put("waiting_charge", jsonObject.get("waiting_charge").toString());
                        Global.ridePrices.put("total_cost", jsonObject.get("total_cost").toString());
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

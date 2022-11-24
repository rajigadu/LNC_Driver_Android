package com.lncdriver.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by narip on 2/4/2017.
 */
public class Global extends Application {
    public static Activity mCurrentActivity;
    public static HashMap<String, Object> mapMain = new HashMap<String, Object>();
    public static HashMap<String, Object> ridePrices = new HashMap<String, Object>();
    public static HashMap<String, Object> dmap = new HashMap<String, Object>();
    public static HashMap<String, Object> mapData = new HashMap<String, Object>();
    public static HashMap<String, Object> eventdata = new HashMap<String, Object>();
    public static HashMap<String, Object> tempData = new HashMap<String, Object>();
    public static HashMap<String, Object> profiledata = new HashMap<String, Object>();
    public static HashMap<String, Object> extramap = new HashMap<String, Object>();

    public static List<HashMap<String, Object>> availRideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> userlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> cridelist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> partnersList = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> partneORDriverList = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> ridelist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> userchatlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> driverchatlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> partnerchatlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> activepartnerlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> stopLocationslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> paymentlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> paymentHistoryList = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> bankdetailsList = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> vehicledetailsList = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> personaldetailsList = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> frideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> driverincomingfrideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> partnerincomingfrideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> driverfrideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> partnerfrideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> driverfrideDetailslist = new ArrayList<>();
    public static List<HashMap<String, Object>> partnerfrideDetailslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> fridehistorylist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> partnerfridehistorylist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> pendingCrideslist = new ArrayList<HashMap<String, Object>>();


    private static Context context;

    public static Context getAppContext() {
        return Global.context;
    }

    public HashMap<String, Object> mapMain() {
        if (mapMain == null) {
            return mapMain = new HashMap<>();
        } else {
            mapMain.clear();
        }
        return mapMain;
    }

    public HashMap<String, Object> dmapMain() {
        if (dmap == null) {
            return dmap = new HashMap<>();
        } else {
            dmap.clear();
        }
        return dmap;
    }

    public HashMap<String, Object> eventdata() {
        if (eventdata == null) {
            return eventdata = new HashMap<String, Object>();
        } else {
            eventdata.clear();
        }
        return eventdata;
    }

    public HashMap<String, Object> profile() {
        if (profiledata == null) {
            return profiledata = new HashMap<String, Object>();
        } else {
            profiledata.clear();
        }
        return profiledata;
    }

    public HashMap<String, Object> mapData() {
        if (mapMain == null) {
            return mapData = new HashMap<String, Object>();
        } else {
            mapData.clear();
        }
        return mapData;
    }

    public HashMap<String, Object> tempData() {
        if (tempData == null) {
            return tempData = new HashMap<String, Object>();
        } else {
            tempData.clear();
        }
        return tempData;
    }

    public HashMap<String, Object> ridePriceData() {
        if (ridePrices == null) {
            return ridePrices = new HashMap<String, Object>();
        } else {
            ridePrices.clear();
        }
        return ridePrices;
    }


    public void setCurrentActivity(Activity CurrentActivity) {
        mCurrentActivity = CurrentActivity;
    }
}

package com.lncdriver.model;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lncdriver.activity.Navigation;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;

import java.util.HashMap;


/**
 * Created by narayana on 3/16/2018.
 */

public class MyApplication extends Application {
    public HashMap<String, Object> map;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
        //JobManager.create(this).addJobCreator(new DemoJobCreator());
        // initialize the AdMob app
        //MobileAds.initialize(this, getString(R.string.admob_app_id));
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // Log.logError("background123","closed now!");

            SavePref pref1 = new SavePref();
            pref1.SavePref(getApplicationContext());

            String isride = pref1.getisnewride();
            String email = pref1.getEmail();
            String cid = pref1.getcustomermap();

            if (isride.equalsIgnoreCase("newride")) {
                rideRejectCall();
            }
            if (!email.equalsIgnoreCase("")) {
                if (cid.equalsIgnoreCase(""))
                    driverOnlineRequest("2");
            }
            // Utils.toastTxt("closed now ",getApplicationContext());
            //stopService(new Intent(getBaseContext(), MyService.class));
        }
    }

    public void driverOnlineRequest(String status) {
        new Utils(getApplicationContext());
        map = new HashMap<>();
        map.put("status", status);
        OnlineRequest.driverOnlineRequest(getApplicationContext(), map);
    }

    public void rideRejectCall() {
        HashMap<String, Object> dmap = null;

        SavePref pref1 = new SavePref();
        pref1.SavePref(getApplicationContext());
        String cmap = pref1.getridemap();

        if (!cmap.equalsIgnoreCase("")) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            dmap = gson.fromJson(cmap, type);

            if (dmap.containsKey("type") && !dmap.get("type").toString().equalsIgnoreCase("driver")) {
                Navigation.DriverAcceptRequest(getApplicationContext(), dmap, "2");
            } else if (dmap.containsKey("type") && !dmap.get("type").toString().equalsIgnoreCase("partner")) {
                Navigation.PartnerAcceptRequest(getApplicationContext(), dmap, "2");
            }
        }
    }
}

package com.lncdriver.fcm;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.lncdriver.model.SavePref;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationBackgroundService extends Service {
    Location locationAA = null;
    String TAG = "LocationBackgroundService";


    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
    public Call<ResponseBody> call = null;


    public VeggsterLocationListener mVeggsterLocationListener;
    LocationManager mLocationManager = null;

    SavePref pref = new SavePref();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand Service Started");
       // startLooking();
        pref.SavePref(getApplicationContext());
       // WSContants.IS_APP_OPEN = true;
        new FetchCordinates().execute();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy Service Destroyed");
      //  WSContants.IS_APP_OPEN = false;
        if(mLocationManager != null){
            mLocationManager.removeUpdates(mVeggsterLocationListener);
            mLocationManager = null;
        }
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e(TAG, "onTaskRemoved END");
       // WSContants.IS_APP_OPEN = false;
        if(mLocationManager != null){
            mLocationManager.removeUpdates(mVeggsterLocationListener);
            mLocationManager = null;
        }
        stopSelf();
    }








    public void onCall() {
        Log.e(TAG, "onCall Service Destroyed");
        new FetchCordinates().execute();
    }




    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        // ProgressDialog progDailog = new ShowMsg().createProgressDialog(getActivity());
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (locationAA == null) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                } else {
                    mVeggsterLocationListener = new VeggsterLocationListener();
                    mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }


                    locationAA = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 0, mVeggsterLocationListener);

                    if(locationAA == null){
                        locationAA = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 0, mVeggsterLocationListener);
                    }
                }
            }else{
                mVeggsterLocationListener = new VeggsterLocationListener();
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationAA = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 0, mVeggsterLocationListener);

                if(locationAA == null){
                    locationAA = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 0, mVeggsterLocationListener);
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //progressBar.setVisibility(View.GONE);
            if(locationAA != null){
                //setMarkerANDLocation(locationAA, latLngDropOFF);
                //  callRefreshMethod();
                Log.e(TAG, "onPostExecutelocationAA "+ locationAA.getLatitude());
                Log.e(TAG, "onPostExecutelocationAA "+ locationAA.getLongitude());
                Intent intent = new Intent("OPEN_NEW_ACTIVITY1");
                Bundle bundleObject = new Bundle();
                bundleObject.putString("key" , "1");
                bundleObject.putParcelable("parcelable_extra", (Parcelable) locationAA);
                intent.putExtras(bundleObject);
                sendBroadcast(intent);

                callApiMethod(""+locationAA.getLatitude(), ""+locationAA.getLongitude());

//                pref.setLatitude(""+locationAA.getLatitude());
//                pref.setLongitude(""+locationAA.getLongitude());

            }
        }
    }





    public class VeggsterLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {

            locationAA = location;

            if(locationAA != null){
                Log.e(TAG, "onPostExecutelocationBB "+ locationAA.getLatitude());
                Log.e(TAG, "onPostExecutelocationBB "+ locationAA.getLongitude());
                Intent intent = new Intent("OPEN_NEW_ACTIVITY2");
                Bundle bundleObject = new Bundle();
                bundleObject.putString("key" , "2");
                bundleObject.putParcelable("parcelable_extra", (Parcelable) locationAA);
                intent.putExtras(bundleObject);
                sendBroadcast(intent);


                callApiMethod(""+locationAA.getLatitude(), ""+locationAA.getLongitude());
//                pref.setLatitude(""+locationAA.getLatitude());
//                pref.setLongitude(""+locationAA.getLongitude());
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("OnProviderDisabled", "OnProviderDisabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("onProviderEnabled", "onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            Log.i("onStatusChanged", "onStatusChanged");

        }

    }







    private void callApiMethod(String lat, String lon) {
        call = apiInterface.updateDriverLocation(pref.getUserId(),lat, lon,getCurrentVersion());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseCode = "";
                try{
                    if(response.body() != null) {
                    responseCode = response.body().string();

                    Log.e(TAG , "XXXXXFFFFAAAA "+responseCode);
                }

                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


}


    private String getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        return currentVersion;
    }




}

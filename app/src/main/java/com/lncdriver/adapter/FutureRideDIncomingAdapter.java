package com.lncdriver.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lncdriver.R;
import com.lncdriver.activity.Navigation;
import com.lncdriver.activity.StopLocationsList;
import com.lncdriver.fragment.FutureDriverRequests;
import com.lncdriver.fragment.ItemFutureEstimation;
import com.lncdriver.model.ModelItem;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.ParsingHelper;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class FutureRideDIncomingAdapter extends RecyclerView.Adapter {
    private static final String TAG = "FutureRideDIncomingAdapter";
    private List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public static Context mcontext;





    ArrayList<ItemFutureEstimation> arrayList = new ArrayList<>();

    public FutureRideDIncomingAdapter(Context context, List<ModelItem> students, ArrayList<ItemFutureEstimation> arrayListAA , RecyclerView recyclerView, int layout, int mode) {
        adapterList = students;
        itemLayout = layout;
        adapterMode = mode;
        mcontext = context;
        arrayList = arrayListAA;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        vh = new InboxViewHolder(v);

        return vh;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InboxViewHolder) {
            ModelItem singleTripData = (ModelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            //Utils.logError("paymentslistadapter 107", "" + detailMap);





            try {



//                if(isTrue == true){
//                    ((InboxViewHolder) holder).accept.setClickable(false);
//                }else{
//                    ((InboxViewHolder) holder).accept.setClickable(true);
//                }
                ((InboxViewHolder) holder).accept.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).stops.setTag(singleTripData.getMapMain());
                //((InboxViewHolder) holder).viewmap.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("otherdate") && !detailMap.get("otherdate").toString().equalsIgnoreCase("") && !detailMap.get("otherdate").toString().equalsIgnoreCase("")) {
                    String ss = "", ee = "";

                    ss = "Date  :" + detailMap.get("otherdate").toString();
                    ee = "Time :" + detailMap.get("time").toString();

                    String s = ss
                            + System.getProperty("line.separator")
                            + ee
                            + System.getProperty("line.separator");

                    ((InboxViewHolder) holder).date.setText(s);



                    if (detailMap.containsKey("second") && !detailMap.get("second").toString().equalsIgnoreCase("")) {

                        int value = Integer.parseInt(detailMap.get("second").toString());
                        Log.e(TAG, "value " + value);

                        SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                        String dateInString = detailMap.get("otherdate").toString() + " " + detailMap.get("time").toString();
                        Date date = sdf.parse(dateInString);

                        System.out.println(dateInString);
                        System.out.println("Date - Time in milliseconds : " + date.getTime());

                        //  long xx = 1000 * 60 * 60 * 24;
                        long oneMinute = 60 * 1000;
                        long xx = 1000 * value;
                        long millis = date.getTime() + xx +oneMinute;

                        Date expireDate = new Date(millis);


                        String dateT = sdf1.format(expireDate);
                        String timeT = sdf2.format(expireDate);


                        String ssD = "", eeT = "";

                        ssD = "Estimated DropOff Date :" + dateT;
                        eeT = "Estimated DropOff Time :" + timeT;

                        String sS = ssD
                                + System.getProperty("line.separator")
                                + eeT
                                + System.getProperty("line.separator");

                        ((FutureRideDIncomingAdapter.InboxViewHolder) holder).textViewEstimated.setText(sS);


                        if (detailMap.containsKey("estimation_time") && !detailMap.get("estimation_time").toString().equalsIgnoreCase("")) {
                            ((FutureRideDIncomingAdapter.InboxViewHolder) holder).textViewDurationTime.setText("Duration :" + detailMap.get("estimation_time").toString());
                        }
                    }



//                    Call<ResponseBody> call = null;
//                    APIInterface apiInterface  = APIClient.getClientGoogle().create(APIInterface.class);
//
//                    call = apiInterface.getEstimation(
//                            detailMap.get("pickup_lat").toString()+","+detailMap.get("pickup_long").toString(),
//                            detailMap.get("d_lat").toString()+","+detailMap.get("d_long").toString(),
//                            "AIzaSyDhDD6XROfPs4WkFmbTHvV1KqkNVP1kEmk");
//
//
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            String responseCode = "";
//                            try {
//                                if(response.body() != null) {
//                                    responseCode = response.body().string();
//                                    Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC: " + responseCode);
//
//                                    JSONObject object = new JSONObject(responseCode);
//                                    if(object.getString("status").equalsIgnoreCase("OK")){
//                                        JSONArray jsonArray = object.getJSONArray("rows");
//                                        if(jsonArray.length() > 0){
//                                            JSONObject object1 = jsonArray.getJSONObject(0);
//                                            JSONArray jsonArray2 = object1.getJSONArray("elements");
//                                            if(jsonArray2.length() > 0){
//                                                JSONObject object2 = jsonArray2.getJSONObject(0);
//                                                if(object2.getString("status").equalsIgnoreCase("OK")){
//                                                    JSONObject object3 = object2.getJSONObject("duration");
//                                                    int value = object3.getInt("value");
//                                                    Log.e(TAG , "value "+value);
//
//                                                    SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
//                                                    SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
//                                                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
//                                                    String dateInString = detailMap.get("otherdate").toString()+" "+detailMap.get("time").toString();
//                                                    Date date = sdf.parse(dateInString);
//
//                                                    System.out.println(dateInString);
//                                                    System.out.println("Date - Time in milliseconds : " + date.getTime());
//
//                                                    //  long xx = 1000 * 60 * 60 * 24;
//                                                    long xx = 1000 * value;
//                                                    long millis = date.getTime()+xx;
//
//                                                    Date expireDate = new Date(millis);
//
//
//                                                    String dateT = sdf1.format(expireDate);
//                                                    String timeT = sdf2.format(expireDate);
//
//
//                                                    String ssD = "", eeT = "";
//
//                                                    ssD = "Estimated DropOff Date :" + dateT;
//                                                    eeT = "Estimated DropOff Time :" + timeT;
//
//                                                    String sS = ssD
//                                                            + System.getProperty("line.separator")
//                                                            + eeT
//                                                            + System.getProperty("line.separator");
//
//                                                    ((FutureRideDIncomingAdapter.InboxViewHolder) holder).textViewEstimated.setText(sS);
//
//                                                    ((FutureRideDIncomingAdapter.InboxViewHolder) holder).textViewDurationTime.setText("Duration :"+object3.getString("text"));
//
//                                                }
//                                            }
//                                        }
//                                    }
//                                }else{
//                                }
//
//                                //  Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCERR: " + t.getMessage());
//                        }
//                    });





                }

                if (detailMap.containsKey("distance") && !detailMap.get("distance").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).distance.setText(String.format("%.2f", Double.valueOf(detailMap.get("distance").toString())) + " mi");
                }

                if (detailMap.containsKey("notes") && !detailMap.get("notes").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).tvRideNotes.setText(detailMap.get("notes").toString());
                } else {
                    ((InboxViewHolder) holder).tvRideNotes.setVisibility(View.GONE);
                    ((InboxViewHolder) holder).tvNotesTitle.setVisibility(View.GONE);
                }

                if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                    String s = "", ss = "", e = "", ee = "";
                    List<String> plist = new ArrayList<>();
                    List<String> elist = new ArrayList<>();

                    if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                        // plist.add(detailMap.get("pickup_street_address").toString());
                    }
                    if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                        plist.add(detailMap.get("pickup_address").toString());
                    }

                    ss = TextUtils.join(",", plist);
                    s = getColoredSpanned(ss, "#800000");

                    if (detailMap.containsKey("drop_address") && !detailMap.get("drop_address").toString().equalsIgnoreCase("")) {
                        elist.add(detailMap.get("drop_address").toString());
                    }

                    ee = TextUtils.join(",", elist);
                    e = getColoredSpanned(ee, "#F69625");


                    String PickUp = getColoredSpanned("PickUp: " , "#000000");
                    String DropOff = getColoredSpanned("DropOff: " , "#000000");

                    ((InboxViewHolder) holder).ride.setText(Html.fromHtml(PickUp+s + "<br />  to  <br />" + DropOff+e));
                }

                ((InboxViewHolder) holder).stops.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> dmap = new HashMap<>();
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }
                        }

                        if (Utils.global.mapData.size() > 0) {
                            //Utils.toastTxt("Still haven't implemented",mcontext);
                            Intent i = new Intent(mcontext, StopLocationsList.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });



               // final boolean isTrue = getIsTrue(detailMap, arrayList);
             //   Log.e(TAG, "isTrue " + isTrue);

//                if(isTrue == true){
//                    ((InboxViewHolder) holder).accept.setVisibility(View.VISIBLE);
//                }else{
//                    ((InboxViewHolder) holder).accept.setVisibility(View.GONE);
//                }




//                if(arrayList.size() > 0){
//                    for (int j = 0 ; j < arrayList.size() ; j++) {
//                        ItemFutureEstimation estimation = arrayList.get(j);
////                    Log.e(TAG, "getFirstTime " + estimation.getFirstTime());
////                    Log.e(TAG, "getSecondTime " + estimation.getSecondTime());
//
//                        SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
//
//                        String dateInString = detailMap.get("otherdate") + " " + detailMap.get("time");
//                        Date date = sdf.parse(dateInString);
//                        long yy = date.getTime();
//
//
//                        if(yy >= estimation.getFirstTime() && estimation.getSecondTime() <= yy){
//                            Log.e(TAG , "estimation True");
////                            bDDDDD = true;
//                            ((InboxViewHolder) holder).accept.setVisibility(View.GONE);
//                            //break;
//                        }else{
//                            Log.e(TAG , "estimation False");
////                            bDDDDD = false;
//                            ((InboxViewHolder) holder).accept.setVisibility(View.VISIBLE);
//                            //break;
//                        }
//
//
//
//                    }
//
//                }

              //  ((InboxViewHolder) holder).accept.setVisibility(View.VISIBLE);


                ((InboxViewHolder) holder).accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> dmap = new HashMap<String, Object>();

                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();
                       // Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");


                        callLogDrive();

//
//                        SavePref pref1 = new SavePref();
//                        pref1.SavePref(mcontext);
//                        String id = pref1.getUserId();
//
//
//                        new DriverProAsynk(Utils.global.mapData).execute(Settings.URL_MAIN_DATA+"driver_accepted_future_ride_list.php?driver_id="+id);
//


//                        try {
//                            String res = downloadUrl(Settings.URL_MAIN_DATA+"driver_accepted_future_ride_list.php?driver_id="+id);
//
//                            Log.e(TAG, "Refreshed ssSSgetIsTrueres: " + res);
////
////                        if (Utils.progressDialog != null) {
////                            Utils.progressDialog.dismiss();
////                            Utils.progressDialog = null;
////                        }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }


                        // driverApiCall(Utils.global.mapData);



//                        if(isTrue == true){
//                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mcontext);
//                            builder.setTitle(mcontext.getString(R.string.app_name));
//                            builder.setMessage("You have already another ride on same time.")
//                                    .setCancelable(false)
//                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                            android.support.v7.app.AlertDialog alert = builder.create();
//                            alert.setCancelable(false);
//                            alert.show();
//                        }else{
//                            if (Utils.global.mapData.size() > 0) {
//                                if (Utils.global.mapData.containsKey("id"))
//                                    FutureDriverRequests.Instance.acceptFutureRideRequest(Utils.global.mapData.get("id").toString());
//                            }
//                        }



                    }
                });








            } catch (Exception e) {
                // Utils.logError(Tag+"180","Exception=====Exception======Exception ::   "+logError.getMessage());
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).hotel = singleTripData;
        }
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //Notification
        public ModelItem hotel;
        public View gView;
        //***

        public TextView date, ride, distance, tvNotesTitle, tvRideNotes , textViewEstimated, textViewDurationTime;
        public Button stops, accept;
        public RelativeLayout root;

        public InboxViewHolder(View v) {
            super(v);
            try {
                date = (TextView) v.findViewById(R.id.date);
                stops = (Button) v.findViewById(R.id.stops);
                accept = (Button) v.findViewById(R.id.accept);
                ride = (TextView) v.findViewById(R.id.ride);
                distance = (TextView) v.findViewById(R.id.distance);
                root = (RelativeLayout) v.findViewById(R.id.rowitem_root);
                tvNotesTitle = v.findViewById(R.id.tv_ride_notes_title);
                tvRideNotes = v.findViewById(R.id.tv_rides_notes);

                textViewEstimated = v.findViewById(R.id.estimeted_date);
                textViewDurationTime = v.findViewById(R.id.estimeted_time);

            } catch (Exception e) {
                Utils.logError("ProfileEvent", "Exception==========Exception==========Exception====" + e.getMessage().toString());

            }
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }







    private void callLogDrive() {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
        Call<ResponseBody> call = null;
        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        call = apiInterface.logDriver(id);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();
                        Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC: " + responseCode);

                        JSONObject object = new JSONObject(responseCode);
                        if(object.getString("status").equalsIgnoreCase("1")){


                            SavePref pref1 = new SavePref();
                            pref1.SavePref(mcontext);
                            String id = pref1.getUserId();


                           // new DriverProAsynk(Utils.global.mapData).execute(Settings.URL_MAIN_DATA+"driver_accepted_future_ride_list.php?driver_id="+id);

                            // new DriverProAsynk(Utils.global.mapData).execute(id);

                            callDriverResponse(Utils.global.mapData, id);



                        }else{
                            String msg = object.getString("msg");
                            Utils.toastTxt(""+msg, mcontext);
                            Navigation.Instance.logoutRequest();
                        }

                    }else{
                    }

                    //  Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Refreshed getMessage: " + e.getMessage());
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCERR: " + t.getMessage());
            }
        });


    }

    private void callDriverResponse(HashMap<String, Object> mapData, String id) {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");

        Call<ResponseBody> call = null;
        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
        call = apiInterface.driver_accepted_future_ride_list(id);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ArrayList<ItemFutureEstimation> arrayList11 = new ArrayList<ItemFutureEstimation>();

                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();
                        arrayList11 = new ParsingHelper().getFutureEstimation(responseCode);



                    }else{

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }


                Log.e(TAG, "Refreshed ItemFutureEstimation: " + arrayList11.size());



                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }


                if(arrayList11.size() == 0){
                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mcontext);
                    String id = pref1.getUserId();

                   // new PartnerProAsynk(Utils.global.mapData).execute(Settings.URL_MAIN_DATA+"partner_accepted_future_ride_list.php?driver_id="+id);

                    callPartnerResponse(mapData, id);
                    return;
                }

                final boolean isTrue = getIsTrue(mapData, arrayList11);

                Log.e(TAG, "Refreshed ssSSgetIsTrue: " + isTrue);



                if(isTrue == true){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mcontext);
                    builder.setTitle(mcontext.getString(R.string.app_name));
                    builder.setMessage("You have already another ride on same time.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                }else{
                    if (mapData.size() > 0) {
                        if (mapData.containsKey("id"))
                            FutureDriverRequests.Instance.acceptFutureRideRequest(mapData.get("id").toString());
                    }
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }

            }
        });
    }






    private void callPartnerResponse(HashMap<String, Object> mapData, String id) {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");

        Call<ResponseBody> call = null;
        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
        call = apiInterface.partner_accepted_future_ride_list(id);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ArrayList<ItemFutureEstimation> arrayList11 = new ArrayList<ItemFutureEstimation>();

                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();
                        arrayList11 = new ParsingHelper().getPartnerFutureEstimation(responseCode);

                    }else{

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }


                Log.e(TAG, "Refreshed ItemFutureEstimationPartner: " + arrayList11.size());

                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }


                final boolean isTrue = getIsTrue(mapData, arrayList11);

                Log.e(TAG, "Refreshed ssSSgetIsTrue: " + isTrue);



                if(isTrue == true){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mcontext);
                    builder.setTitle(mcontext.getString(R.string.app_name));
                    builder.setMessage("You have already another ride on same time.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                }else{
                    if (mapData.size() > 0) {
                        if (mapData.containsKey("id"))
                            FutureDriverRequests.Instance.acceptFutureRideRequest(mapData.get("id").toString());
                    }
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }

            }
        });
    }



//    private class DriverProAsynk extends AsyncTask<String, String, ArrayList<ItemFutureEstimation>> {
//        HashMap<String, Object> mapData = new HashMap<>();
//
//        public DriverProAsynk(HashMap<String, Object> mapData) {
//            this.mapData = mapData;
//        }
//
//        @Override
//        protected ArrayList<ItemFutureEstimation> doInBackground(String... params) {
//            final ArrayList<ItemFutureEstimation>[] arrayList11 = new ArrayList[]{new ArrayList<ItemFutureEstimation>()};
//
//            call = apiInterface.driver_accepted_future_ride_list(params[0]);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    String responseCode = "";
//                    try {
//                        if(response.body() != null) {
//                            responseCode = response.body().string();
//
//                        arrayList11[0] = new ParsingHelper().getFutureEstimation(responseCode);
//
//                        }else{
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//
//                }
//            });
//
////            try {
////                String xxc = downloadUrl(params[0]);
////                arrayList11 = new ParsingHelper().getFutureEstimation(xxc);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            return arrayList11[0];
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
//        }
//
//
//        @SuppressLint("LongLogTag")
//        @Override
//        protected void onPostExecute(ArrayList<ItemFutureEstimation> result) {
//
//            if (Utils.progressDialog != null) {
//                Utils.progressDialog.dismiss();
//                Utils.progressDialog = null;
//            }
//
//            Log.e(TAG, "Refreshed ItemFutureEstimation: " + result.size());
//
////            if(result.size() == 0){
////                SavePref pref1 = new SavePref();
////                pref1.SavePref(mcontext);
////                String id = pref1.getUserId();
////
////                new PartnerProAsynk(Utils.global.mapData).execute(Settings.URL_MAIN_DATA+"partner_accepted_future_ride_list.php?driver_id="+id);
////
////                return;
////            }
////
////            final boolean isTrue = getIsTrue(mapData, result);
////
////            Log.e(TAG, "Refreshed ssSSgetIsTrue: " + isTrue);
////
////
////
////            if(isTrue == true){
////                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mcontext);
////                builder.setTitle(mcontext.getString(R.string.app_name));
////                builder.setMessage("You have already another ride on same time.")
////                        .setCancelable(false)
////                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int id) {
////                                dialog.dismiss();
////                            }
////                        });
////                android.support.v7.app.AlertDialog alert = builder.create();
////                alert.setCancelable(false);
////                alert.show();
////            }else{
////                if (mapData.size() > 0) {
////                    if (mapData.containsKey("id"))
////                        FutureDriverRequests.Instance.acceptFutureRideRequest(mapData.get("id").toString());
////                }
////            }
//
//
//        }
//
//    }
//





    private class PartnerProAsynk extends AsyncTask<String, String, ArrayList<ItemFutureEstimation>> {
        HashMap<String, Object> mapData = new HashMap<>();

        public PartnerProAsynk(HashMap<String, Object> mapData) {
            this.mapData = mapData;
        }

        @Override
        protected ArrayList<ItemFutureEstimation> doInBackground(String... params) {
            ArrayList<ItemFutureEstimation> arrayList11 = new ArrayList<>();
            try {
                String xxc = downloadUrl(params[0]);
                arrayList11 = new ParsingHelper().getPartnerFutureEstimation(xxc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrayList11;
        }


        @Override
        protected void onPreExecute() {
            Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
        }


        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(ArrayList<ItemFutureEstimation> result) {

            if (Utils.progressDialog != null) {
                Utils.progressDialog.dismiss();
                Utils.progressDialog = null;
            }

            Log.e(TAG, "Refreshed ItemFutureEstimationPartner: " + result.size());


            final boolean isTrue = getIsTrue(mapData, result);

            Log.e(TAG, "Refreshed ssSSgetIsTruePartner: " + isTrue);




            if(isTrue == true){
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mcontext);
                builder.setTitle(mcontext.getString(R.string.app_name));
                builder.setMessage("You have already another ride on same time.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                android.support.v7.app.AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }else{
                if (mapData.size() > 0) {
                    if (mapData.containsKey("id"))
                        FutureDriverRequests.Instance.acceptFutureRideRequest(mapData.get("id").toString());
                }
            }


        }

    }






    private String downloadUrl(String strUrl) throws IOException {
      //  Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();

//            if (Utils.progressDialog != null) {
//                Utils.progressDialog.dismiss();
//                Utils.progressDialog = null;
//            }

        }
        return data;
    }






    @SuppressLint("LongLogTag")
    private boolean getIsTrue(HashMap<String, Object> detailMap, ArrayList<ItemFutureEstimation> arrayList) {
        boolean b = false;


        ArrayList<Boolean> booleanArrayList = new ArrayList<>();

        Log.e(TAG, "arrayList.size() " + arrayList.size());

        try {
            if(arrayList.size() > 0){
                for (int j = 0 ; j < arrayList.size() ; j++) {
                    ItemFutureEstimation estimation = arrayList.get(j);
                    Log.e(TAG, "getFirstTime " + estimation.getFirstTime());

                    Log.e(TAG, "getSecondTime " + estimation.getSecondTime());

                        SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");

                        String dateInString = detailMap.get("otherdate") + " " + detailMap.get("time");
                        Date date = sdf.parse(dateInString);
                        long yy = date.getTime();
                        Log.e(TAG, "getBetweenTime " + yy);


                    if (yy >= estimation.getFirstTime() && yy <= estimation.getSecondTime()){
                        booleanArrayList.add(true);
                    }

                    else{
                        booleanArrayList.add(false);
                    }

                }

            }
        } catch (Exception e) {

        }

        if(booleanArrayList.contains(true)){
            b = true;
        }




        return b;
    }







}
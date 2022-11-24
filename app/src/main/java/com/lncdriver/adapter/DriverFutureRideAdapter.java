package com.lncdriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.lncdriver.R;
import com.lncdriver.activity.Navigation;
import com.lncdriver.activity.StopLocationsList;
import com.lncdriver.activity.ViewCustomerFRideDetails;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.ParsingHelper;
import com.lncdriver.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lncdriver.fragment.DriverFutureRides.isListEmpty;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class DriverFutureRideAdapter extends RecyclerView.Adapter {
    private static final String TAG = "DriverFutureRideAdapter";
    private List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public DriverFutureRideAdapter(Context context, List<ModelItem> adapterList, RecyclerView recyclerView,
                                   int layout, int mode) {
        this.adapterList = adapterList;
        itemLayout = layout;
        adapterMode = mode;
        mcontext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        vh = new InboxViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InboxViewHolder) {
            ModelItem singleTripData = (ModelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            //Utils.logError("paymentslistadapter 107", "" + detailMap);

            try {
                ((InboxViewHolder) holder).viewdetials.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).viewPartner.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).stops.setTag(singleTripData.getMapMain());
                //((InboxViewHolder) holder).viewmap.setTag(singleTripData.getMapMain());





                if (detailMap.containsKey("otherdate") && !detailMap.get("otherdate").toString().equalsIgnoreCase("") && !detailMap.get("otherdate").toString().equalsIgnoreCase("null")) {
                    String ss = "", ee = "";

                    ss = "Date :" + detailMap.get("otherdate").toString();
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

                        ((InboxViewHolder) holder).textViewEstimated.setText(sS);


                        if (detailMap.containsKey("estimation_time") && !detailMap.get("estimation_time").toString().equalsIgnoreCase("")) {
                            ((InboxViewHolder) holder).textViewDurationTime.setText("Duration :" + detailMap.get("estimation_time").toString());
                        }
                    }


//                    Call<ResponseBody> call = null;
//                    APIInterface apiInterface  = APIClient.getClientGoogle().create(APIInterface.class);
//
//                    call = apiInterface.getEstimation(
//                            detailMap.get("pickup_lat").toString()+","+detailMap.get("pickup_long").toString(),
//                            detailMap.get("d_lat").toString()+","+detailMap.get("d_long").toString(),
//                             "AIzaSyDhDD6XROfPs4WkFmbTHvV1KqkNVP1kEmk");
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
//                                                    ((InboxViewHolder) holder).textViewEstimated.setText(sS);
//
//                                                    ((InboxViewHolder) holder).textViewDurationTime.setText("Duration: "+object3.getString("text"));
//
//                                                }
//                                            }
//                                        }
//                                    }
//                                }else{
//                                }
//
//                              //  Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCERR: " + t.getMessage());
//                        }
//                    });
//
//






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

                ((InboxViewHolder) holder).viewdetials.setOnClickListener(new View.OnClickListener() {
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
                            /*AddPartnerFragment addPartnerFragment = new AddPartnerFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("map", (Serializable) dmap);
                            addPartnerFragment.setArguments(bundle);
                            replaceFragment(addPartnerFragment);*/

                            Intent i = new Intent(mcontext, ViewCustomerFRideDetails.class);

                            dmap.put("date", detailMap.get("otherdate").toString() + " " + detailMap.get("time").toString());
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });



                if(!detailMap.get("future_partner_id").toString().equalsIgnoreCase("") &&
                        !detailMap.get("future_partner_id").toString().equalsIgnoreCase("0")){
                    ((InboxViewHolder) holder).viewPartner.setVisibility(View.VISIBLE);
                }else{
                    ((InboxViewHolder) holder).viewPartner.setVisibility(View.GONE);
                }


                ((InboxViewHolder) holder).viewPartner.setOnClickListener(new View.OnClickListener() {
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
                            /*AddPartnerFragment addPartnerFragment = new AddPartnerFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("map", (Serializable) dmap);
                            addPartnerFragment.setArguments(bundle);
                            replaceFragment(addPartnerFragment);*/

//                            Intent i = new Intent(mcontext, ViewCustomerFRideDetails.class);
//
//                            dmap.put("date", detailMap.get("otherdate").toString() + " " + detailMap.get("time").toString());
//                            i.putExtra("map", (Serializable) dmap);
//                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
//                            mcontext.startActivity(i);



                            callSwipeDrive(detailMap.get("id").toString());

//                            Navigation.nid = 4;
//                            com.lncdriver.fragment.FutureRides.id = 1;
//                            Intent intent = new Intent(mcontext, Navigation.class);
//                            mcontext.startActivity(intent);
//                            ((Activity) mcontext).finish();


                        }
                    }
                });



            } catch (Exception e) {
                // Utils.logError(Tag+"180","Exception=====Exception======Exception ::   "+logError.getMessage());
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).hotel = singleTripData;
        }
    }

    private void callSwipeDrive(String id) {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
        Call<ResponseBody> call = null;
        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

        call = apiInterface.swapDriver(id, "1");
        call.enqueue(new Callback<ResponseBody>() {
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
                            String msg = object.getString("msg");
                            Utils.toastTxt(""+msg, mcontext);

                            Navigation.nid = 4;
                            com.lncdriver.fragment.FutureRides.id = 1;
                            Intent intent = new Intent(mcontext, Navigation.class);
                            mcontext.startActivity(intent);
                            ((Activity) mcontext).finish();
                        }else{
                            String msg = object.getString("msg");
                            Utils.toastTxt(""+msg, mcontext);
                        }

                    }else{
                    }

                    //  Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    /*public void replaceFragment(android.support.v4.app.Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = driverFutureRides.getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.rl_partner_container, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }*/

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //Notification
        public ModelItem hotel;
        public View gView;
        //***

        public TextView date, ride, distance, tvNotesTitle, tvRideNotes, textViewEstimated, textViewDurationTime;
        public Button stops, viewdetials, viewPartner;
        public RelativeLayout root;

        public InboxViewHolder(View v) {
            super(v);
            try {
                date = v.findViewById(R.id.date);
                stops = v.findViewById(R.id.stops);
                viewdetials = v.findViewById(R.id.viewdetails);
                viewPartner = v.findViewById(R.id.viewPartner);

                ride = v.findViewById(R.id.ride);
                distance = v.findViewById(R.id.distance);
                root = v.findViewById(R.id.rowitem_root);
                tvNotesTitle = v.findViewById(R.id.tv_notes_title);
                tvRideNotes = v.findViewById(R.id.tv_notes);

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
}
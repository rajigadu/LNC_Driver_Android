package com.lncdriver.adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.lncdriver.R;
import com.lncdriver.activity.StopLocationsList;
import com.lncdriver.activity.ViewPartnerRideDetails;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class PartnerFutureRideAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PartnerFutureRideAdapter";
    private List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public PartnerFutureRideAdapter(Context context, List<ModelItem> students, RecyclerView recyclerView, int layout, int mode) {
        adapterList = students;

        adapterMode = mode;
        mcontext = context;
        itemLayout = layout;
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
                ((InboxViewHolder) holder).viewdetials.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).stops.setTag(singleTripData.getMapMain());
                //((InboxViewHolder) holder).viewmap.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("otherdate") && !detailMap.get("otherdate").toString().equalsIgnoreCase("") && !detailMap.get("otherdate").toString().equalsIgnoreCase("null")) {
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

                        ((PartnerFutureRideAdapter.InboxViewHolder) holder).textViewEstimated.setText(sS);


                        if (detailMap.containsKey("estimation_time") && !detailMap.get("estimation_time").toString().equalsIgnoreCase("")) {
                            ((PartnerFutureRideAdapter.InboxViewHolder) holder).textViewDurationTime.setText("Duration :" + detailMap.get("estimation_time").toString());
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
//                        @SuppressLint("LongLogTag")
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
//                                                    ((PartnerFutureRideAdapter.InboxViewHolder) holder).textViewEstimated.setText(sS);
//
//                                                    ((PartnerFutureRideAdapter.InboxViewHolder) holder).textViewDurationTime.setText("Duration: "+object3.getString("text"));
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
//                        @SuppressLint("LongLogTag")
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

//                    ((InboxViewHolder) holder).ride.setText(Html.fromHtml(s + "<br />  to  <br />" + e));


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

                        if (dmap.size() > 0) {
                            //Utils.toastTxt("Still haven't implemented",mcontext);
                            Intent i = new Intent(mcontext, ViewPartnerRideDetails.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });


           //     ((InboxViewHolder) holder).linearLayoutMap.setVisibility(View.VISIBLE);

                ((InboxViewHolder) holder).buttonGoToUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                                String add = "";

                                if (detailMap.containsKey("pickup_lat") && !detailMap.get("pickup_lat").
                                        toString().equalsIgnoreCase("")) {
                                    add = "google.navigation:q=" + detailMap.get("pickup_lat").toString()
                                            + "," + detailMap.get("pickup_long").toString() + "&mode=d";

                                    Uri gmmIntentUri = Uri.parse(add);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    mcontext.startActivity(mapIntent);
                                }

                        } catch (ActivityNotFoundException ane) {
                            Toast.makeText(mcontext, "Please Install Google Maps ", Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                });


                ((InboxViewHolder) holder).GoToDestination.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                                String add = "";
                                if (detailMap.containsKey("d_lat") && !detailMap.get("d_lat").toString().equalsIgnoreCase("")) {
                                    add = "google.navigation:q=" + detailMap.get("d_lat").toString() + ","
                                            + detailMap.get("d_long").toString() + "&mode=d";
                                    Uri gmmIntentUri = Uri.parse(add);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    mcontext.startActivity(mapIntent);
                                }

                        } catch (ActivityNotFoundException anf) {
                            Toast.makeText(mcontext, "Please Install Google Maps ", Toast.LENGTH_LONG).show();
                            anf.printStackTrace();
                        } catch (Exception ex) {
                            ex.getMessage();
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

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //Notification
        public ModelItem hotel;
        public View gView;
        //***

        public TextView date, ride, distance, tvNotesTitle, tvRideNotes ,textViewEstimated, textViewDurationTime;
        public Button stops, viewdetials;
        public Button buttonGoToUser, GoToDestination;
        public RelativeLayout root;

        LinearLayout linearLayoutMap;

        public InboxViewHolder(View v) {
            super(v);
            try {
                date = (TextView) v.findViewById(R.id.date);
                stops = (Button) v.findViewById(R.id.stops);
                viewdetials = (Button) v.findViewById(R.id.viewdetails);
                ride = (TextView) v.findViewById(R.id.ride);
                distance = (TextView) v.findViewById(R.id.distance);
                root = (RelativeLayout) v.findViewById(R.id.rowitem_root);
                tvNotesTitle = v.findViewById(R.id.tv_notes_title);
                tvRideNotes = v.findViewById(R.id.tv_notes);

                buttonGoToUser = v.findViewById(R.id.viewgotouser);
                GoToDestination = v.findViewById(R.id.viewgotodestination);


                linearLayoutMap = v.findViewById(R.id.layout134);


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
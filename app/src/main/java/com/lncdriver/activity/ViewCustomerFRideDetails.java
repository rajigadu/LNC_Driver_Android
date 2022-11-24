package com.lncdriver.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lncdriver.R;
import com.lncdriver.adapter.TimeAdapter;
import com.lncdriver.adapter.UnplannedCountAdapter;
import com.lncdriver.model.AlarmReceiver;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/16/2018.
 */

public class ViewCustomerFRideDetails extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ViewCustomerFRideDetails";

    @BindView(R.id.ride_start)
    TextView ride_start;


    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.cancel)
    Button btnCancelRide;

    @BindView(R.id.gotouser)
    Button btnGoToUser;

    @BindView(R.id.ll_timer)
    LinearLayout llTimer;

    @BindView(R.id.ll_stops)
    LinearLayout llStops;

    @BindView(R.id.btn_waiting_time)
    Button btnWaitingTime;

    @BindView(R.id.tv_info_ride_start)
    TextView tvInfoRideStart;

    @BindView(R.id.tv_planned_stops)
    TextView tvPlannedStops;

    @BindView(R.id.trans)
    TextView tvTrans;



    public static ViewCustomerFRideDetails Instance;
    public static Context mcontext;
    static HashMap<String, Object> map, rMap;
    static HashMap<String, Object> smap;

    Button rComplete, btnAdditionalStops;
    TextView uname;
    TextView unumber;
    ImageView userimage;
    ImageView dnavigation;

    Button gotouser, btnGoToDestination;




    ImageView unavigation;
    ImageView phoneCall;
    ImageView msg, ivPartnerCall;
    ImageView msg_driver;
    TextView ischatmsg;
    TextView rideAddress;
    Button btnPartnerInfo, charges_Waiting;

    @BindView(R.id.card_user)
    CardView user_root;

    TextView textViewDate;
    private boolean isPartnerCall = false;


    private static final int RC_CALL_PERM = 100;
    private boolean isNotifClick = false;
    long currentDate = 0, serverDate = 0, currentTime = 0, serverTime = 0, minutes = 0,
            difference = 0, posTime = 0;

    private String COMPLETE_RIDE = "COMPLETE RIDE", START_RIDE = "START RIDE", startWaitingTime = "",
            timeSet = "", strUnPlannedSet = "", partnerNumber = "", rideId = "";
    private ArrayList<String> timesListArray, unplannedCountList;

    android.support.v7.app.AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdriver_future_ride);
        ButterKnife.bind(this);
        timesListArray = new ArrayList<>();
        unplannedCountList = new ArrayList<>();





        unplannedCountList.add(0, "Select Stops");
        for (int i = 0; i < 6; i++)
            unplannedCountList.add(i + "");

        timesListArray.add("Select Time: ");
        timesListArray.add("00:00");
        timesListArray.add("00:15");
        timesListArray.add("00:30");
        timesListArray.add("00:45");
        timesListArray.add("01:00");
        timesListArray.add("01:15");
        timesListArray.add("01:30");
        timesListArray.add("01:45");
        timesListArray.add("02:00");
        timesListArray.add("02:15");
        timesListArray.add("02:30");
        timesListArray.add("02:45");
        timesListArray.add("03:00");
        timesListArray.add("03:15");
        timesListArray.add("03:30");
        timesListArray.add("03:45");
        timesListArray.add("04:00");

        gotouser = findViewById(R.id.gotouser);
        btnGoToDestination = findViewById(R.id.btnGoToDestination);

        textViewDate = findViewById(R.id.utitle43244);
        ivPartnerCall = findViewById(R.id.iv_partner_call);
        btnAdditionalStops = findViewById(R.id.btn_additional_stops);
        btnAdditionalStops.setVisibility(View.GONE);
        userimage = findViewById(R.id.userimage);
        uname = findViewById(R.id.uname);
        unumber = findViewById(R.id.unumber);
        rideAddress = findViewById(R.id.rideaddress);
        btnPartnerInfo = findViewById(R.id.partner);
        ischatmsg = findViewById(R.id.newchatmsg);
        rComplete = findViewById(R.id.complete);
        dnavigation = findViewById(R.id.dnavigation);
        unavigation = findViewById(R.id.unavigation);
        phoneCall = findViewById(R.id.call);
        msg = findViewById(R.id.msg);
        msg_driver = findViewById(R.id.msgd);
        charges_Waiting = findViewById(R.id.waitingcharge);

        dnavigation.setVisibility(View.VISIBLE);
        unavigation.setVisibility(View.VISIBLE);
        btnGoToUser.setVisibility(View.VISIBLE);
        btnWaitingTime.setVisibility(View.GONE);

        mcontext = this;
        Instance = this;

        title.setText("User Details");
        back.setOnClickListener(this);
        btnPartnerInfo.setOnClickListener(this);
        phoneCall.setOnClickListener(this);
        msg.setOnClickListener(this);
        msg_driver.setOnClickListener(this);
        btnCancelRide.setOnClickListener(this);
        charges_Waiting.setOnClickListener(this);
        ivPartnerCall.setOnClickListener(this);

        if (getIntent() != null) {
            isNotifClick = getIntent().getExtras().getBoolean(Utils.IS_NOTIFICATION_CLICK);
            rMap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
            textViewDate.setText("Date/Time: " + rMap.get("date").toString());

            //Log.e("Taggg", rMap.get("future_partner_id").toString());

            if (isNotifClick) {
               /* Intent k = new Intent(mcontext, ActivityDriverChat.class);
                k.putExtra("map", (Serializable) rMap);
                k.putExtra(Utils.IS_NOTIFICATION_CLICK, isNotifClick);
                startActivity(k);*/
            }
        }

        btnAdditionalStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rComplete.getText().toString().trim().equalsIgnoreCase(COMPLETE_RIDE)) {
                    additionalStopsDialog();
                } else {
                    Utils.toastTxt("Ride is not started yet", ViewCustomerFRideDetails.this);
                }
            }
        });

        btnWaitingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postStartWaitingTime();
            }
        });

        rComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rComplete.getText().toString().trim().equalsIgnoreCase(COMPLETE_RIDE)) {
                    getWaitingTimesListDialog();
                } else if ((serverDate <= currentDate) && (minutes < 60)) {
                    //getWaitingTimesList();
                    rideStartRequest(mcontext, rMap);
                } else {
                    getRideUserDetailsRequest(ViewCustomerFRideDetails.this);
                }
                serverDate = 0;
                currentDate = -1;
                serverTime = 0;
                currentTime = -1;
                minutes = 0;
            }
        });


        gotouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rMap != null && rMap.size() > 0) {
                    try {
                        if (rMap != null && rMap.size() > 0) {
                            String add = "";

                            if (rMap.containsKey("pickup_lat") && !rMap.get("pickup_lat").
                                    toString().equalsIgnoreCase("")) {
                                add = "google.navigation:q=" + rMap.get("pickup_lat").toString()
                                        + "," + rMap.get("pickup_long").toString() + "&mode=d";

                                Uri gmmIntentUri = Uri.parse(add);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        }
                    } catch (ActivityNotFoundException ane) {
                        Toast.makeText(mcontext, "Please Install Google Maps ", Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            }
        });
        unavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rMap != null && rMap.size() > 0) {
                    try {
                        if (rMap != null && rMap.size() > 0) {
                            String add = "";

                            if (rMap.containsKey("pickup_lat") && !rMap.get("pickup_lat").
                                    toString().equalsIgnoreCase("")) {
                                add = "google.navigation:q=" + rMap.get("pickup_lat").toString()
                                        + "," + rMap.get("pickup_long").toString() + "&mode=d";

                                Uri gmmIntentUri = Uri.parse(add);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        }
                    } catch (ActivityNotFoundException ane) {
                        Toast.makeText(mcontext, "Please Install Google Maps ", Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            }
        });


        btnGoToDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (rMap != null && rMap.size() > 0) {
                        String add = "";
                        if (rMap.containsKey("d_lat") && !rMap.get("d_lat").toString().equalsIgnoreCase("")) {
                            add = "google.navigation:q=" + rMap.get("d_lat").toString() + ","
                                    + rMap.get("d_long").toString() + "&mode=d";
                            Uri gmmIntentUri = Uri.parse(add);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    }
                } catch (ActivityNotFoundException anf) {
                    Toast.makeText(mcontext, "Please Install Google Maps ", Toast.LENGTH_LONG).show();
                    anf.printStackTrace();
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });


         dnavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (rMap != null && rMap.size() > 0) {
                        String add = "";
                        if (rMap.containsKey("d_lat") && !rMap.get("d_lat").toString().equalsIgnoreCase("")) {
                            add = "google.navigation:q=" + rMap.get("d_lat").toString() + ","
                                    + rMap.get("d_long").toString() + "&mode=d";
                            Uri gmmIntentUri = Uri.parse(add);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    }
                } catch (ActivityNotFoundException anf) {
                    Toast.makeText(mcontext, "Please Install Google Maps ", Toast.LENGTH_LONG).show();
                    anf.printStackTrace();
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });



        //if (!isNotifClick)
        //rideStartStatus(mcontext);
        getRideUserDetailsRequest(ViewCustomerFRideDetails.this);
    }

    public void getUnplannedCount(String unplannedCount) {
        strUnPlannedSet = unplannedCount;
    }

    public void setTimeValue(String timeVal, int posT) {
        timeSet = timeVal;
        posTime = posT;
    }

    public void getWaitingTimesListDialog() {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.dialog_waiting_time, null);
        dialog = new android.support.v7.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        Button cancel = dialog.findViewById(R.id.btn_no);

        btnYes.setText("Submit");
        cancel.setText("Cancel");

        Spinner spUnPlannedCount = dialog.findViewById(R.id.sp_unplanned_count);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.adapter_time_item, R.id.tv_item_time, unplannedCountList);

        dataAdapter.setDropDownViewResource(R.layout.adapter_time_item);
        spUnPlannedCount.setAdapter(dataAdapter);
        spUnPlannedCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 1) {
                    strUnPlannedSet = unplannedCountList.get(position);
                } else if (position == 1) {
                    strUnPlannedSet = unplannedCountList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spTimesList = dialog.findViewById(R.id.sp_times_list);
        ArrayAdapter<String> timeAdaspter = new ArrayAdapter<String>(this,
                R.layout.adapter_time_item, R.id.tv_item_time, timesListArray);

        timeAdaspter.setDropDownViewResource(R.layout.adapter_time_item);
        spTimesList.setAdapter(timeAdaspter);
        spTimesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 1) {
                    timeSet = timesListArray.get(position);
                    posTime = position - 1;
                } else if (position == 1) {
                    timeSet = timesListArray.get(position);
                    posTime = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((timeSet != null && !timeSet.equals("")) || (strUnPlannedSet != null && !strUnPlannedSet.equals(""))) {
                        getEstimate();
                        /*Intent i = new Intent(mcontext, CompleteRide.class);
                        i.putExtra("map", Utils.global.ridePrices);
                        i.putExtra(Utils.TOTAL_UNPLANNED_TIME, strUnPlannedSet);
                        i.putExtra(Utils.POSITION_TIME, posTime);
                        i.putExtra(Utils.TOTAL_WAITING_TIME, timeSet);
                        startActivityForResult(i, 123);
                        posTime = 0;
                        timeSet = "";
                        strUnPlannedSet = "";*/
                        //dialog.cancel();
                    } else {
                        Utils.toastTxt("Please select unplanned stops or waiting time", mcontext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.logError("ridemap12233455===", Utils.global.ridePrices.toString());
                posTime = 0;
                timeSet = "";
                strUnPlannedSet = "";
                /*Intent i = new Intent(mcontext, CompleteRide.class);
                i.putExtra("map", (Serializable) Utils.global.ridePrices);
                i.putExtra(Utils.TOTAL_UNPLANNED_TIME, strUnPlannedSet);
                i.putExtra(Utils.POSITION_TIME, posTime);
                i.putExtra(Utils.TOTAL_WAITING_TIME, timeSet);
                startActivityForResult(i, 123);*/
                dialog.cancel();
            }
        });
        dialog.show();
        dialogLayout.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });
    }


    public void getEstimate() {

        OnlineRequest.getEstimateRide(mcontext, rideId, strUnPlannedSet, posTime * 15);
    }

    public void getEstimateResponse(JSONObject jsonObject) {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }

            Intent i = new Intent(mcontext, CompleteRide.class);
            i.putExtra("map", Utils.global.ridePrices);
            i.putExtra(Utils.TOTAL_UNPLANNED_TIME, strUnPlannedSet);
            i.putExtra(Utils.POSITION_TIME, posTime);
            i.putExtra(Utils.TOTAL_WAITING_TIME, timeSet);
            i.putExtra(Utils.ADMIN_FARE, jsonObject.getString("admin_fee"));
            i.putExtra(Utils.RIDE_FARE, jsonObject.getString("ride_fare"));
            i.putExtra(Utils.EARNINGS, jsonObject.getString("earnings"));

            startActivityForResult(i, 123);
            posTime = 0;
            timeSet = "";
            strUnPlannedSet = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void additionalStopsDialog() {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.dialog_additional_stops, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        Button cancel = dialog.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setAdditionalStopsApi();
                    dialog.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
        dialogLayout.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });
    }

    public void postStartWaitingTime() {
        map = new HashMap<>();
        if (rMap.containsKey("id")) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date dt = dateFormat.getCalendar().getTime();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());
                Utils.logError(TAG, formattedDate);

                int hours = dt.getHours();
                int minutes = dt.getMinutes();
                int seconds = dt.getSeconds();
                String curTime = "", waitEndTime = "";

                if (btnWaitingTime.getText().toString().equals("Stop Waiting Time")) {
                    if (seconds < 10)
                        waitEndTime = formattedDate + " " + hours + ":" + minutes + ":0" + seconds;
                    else {
                        waitEndTime = formattedDate + " " + hours + ":" + minutes + ":" + seconds;
                    }
                    curTime = startWaitingTime;
                } else {
                    startTimer();
                    if (seconds < 10)
                        curTime = formattedDate + " " + hours + ":" + minutes + ":0" + seconds;
                    else {
                        curTime = formattedDate + " " + hours + ":" + minutes + ":" + seconds;
                    }
                    waitEndTime = "";
                }
                map.put("ride_id", rMap.get("id").toString());
                map.put("waiting_start_time", curTime);
                map.put("waiting_end_time", waitEndTime);
                OnlineRequest.postWaitingTime(mcontext, map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(Utils.FINAL_W_TIME);
                String uStops = data.getStringExtra(Utils.FINAL_UNPLANNED);
                rideCompleteRequest(ViewCustomerFRideDetails.this, rMap, result, uStops);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mcontext, "Cancelled ride complete.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void rideStartStatus(Context context) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String isridestart = pref1.getisfridestart();
        charges_Waiting.setVisibility(View.GONE);

        if (isridestart.equalsIgnoreCase("")) {
            rComplete.setText(START_RIDE);
            rComplete.setBackgroundColor(getResources().getColor(R.color.black));
            btnCancelRide.setEnabled(false);
            btnCancelRide.getBackground().setAlpha(100);
            ride_start.setVisibility(View.GONE);
        } else {
            Intent intent = new Intent(mcontext, AlarmReceiver.class);
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            rComplete.setText(COMPLETE_RIDE);
            //   rComplete.setBackgroundColor(getResources().getColor(R.color.green));
            btnCancelRide.setEnabled(false);
            btnCancelRide.getBackground().setAlpha(100);
            btnWaitingTime.setEnabled(true);
            btnWaitingTime.setText("Start Waiting Time");
            ride_start.setVisibility(View.VISIBLE);
        }
    }

    public static void rideCompleteRequest(Context mcontext, HashMap<String, Object> tmap, String result, String uStop) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String did = pref1.getUserId();

        smap = new HashMap<>();
        smap.put("driverid", did);

        if (tmap.containsKey("user_id"))
            smap.put("userid", tmap.get("user_id").toString());
        else
            smap.put("userid", "");

        if (tmap.containsKey("id"))
            smap.put("rideid", tmap.get("id").toString());
        else
            smap.put("rideid", "");

        smap.put("waiting_time", result);
        smap.put("unplanned_stops", uStop);
        OnlineRequest.futureRideCompleteRequest(mcontext, smap);
    }

    public static void rideStartRequest(Context mcontext, HashMap<String, Object> tmap) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String did = pref1.getUserId();

        smap = new HashMap<>();
        smap.put("driverid", did);

        if (tmap.containsKey("id"))
            smap.put("rideid", tmap.get("id").toString());
        else
            smap.put("rideid", "");

        OnlineRequest.futureRideStartRequest(mcontext, smap);
    }

    public static void paymentRequest(Context mcontext) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        smap = new HashMap<>();

        if (rMap.containsKey("user_id"))
            smap.put("user_id", rMap.get("user_id").toString());
        else
            smap.put("user_id", "");

        if (rMap.containsKey("id"))
            smap.put("booking_id", rMap.get("id").toString());
        else
            smap.put("booking_id", "");

        OnlineRequest.futurepaymentRequest(mcontext, smap);
    }

    public static void partnerIntimationRequest(Context mcontext, HashMap<String, Object> tmap) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String uid = pref1.getUserId();

        smap = new HashMap<>();
        smap.put("driverid", uid);

        if (tmap.containsKey("id"))
            smap.put("rideid", tmap.get("id").toString());
        else
            smap.put("rideid", "");

        OnlineRequest.partnerFRideIntimationRequest(mcontext, smap);
    }

    public void dialogWarningMsg() {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.dialog_additional_stops, null);
        TextView tvTitle = dialogLayout.findViewById(R.id.desc);
        tvTitle.setText("Are You Sure You Want To Cancel The Ride?");
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        Button cancel = dialog.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent j = new Intent(ViewCustomerFRideDetails.this, CancelFutureRide.class);
                    if (rMap.containsKey("id"))
                        j.putExtra("rideid", rMap.get("id").toString());
                    if (rMap.containsKey("future_partner_id")) {
                        j.putExtra(PARTNER_ID, rMap.get("future_partner_id").toString());
                    }
                    startActivity(j);
                    dialog.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
        dialogLayout.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });
    }

    public static String PARTNER_ID = "partner_id";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dialogWarningMsg();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.partner:
                showPartnerORDriverDetailsDilog(Utils.global.driverfrideDetailslist.get(0));
                break;
            case R.id.call:
                isPartnerCall = false;
                showMessage("Do You Want To Call ?");
                break;
            case R.id.msg:
                HashMap<String, Object> dmap = new HashMap<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (!value.toString().equalsIgnoreCase("null")) {
                        dmap.put(key, value);
                    }
                }
                ischatmsg.setVisibility(View.GONE);
                Intent i = new Intent(mcontext, ActivityUserChat.class);
                i.putExtra("map", (Serializable) dmap);
                startActivity(i);
                break;
            case R.id.msgd:
                HashMap<String, Object> dmap1 = new HashMap<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (!value.toString().equalsIgnoreCase("null")) {
                        dmap1.put(key, value);
                    }
                }

                if (!dmap1.containsKey("partnerid")) {
                    Utils.toastTxt("Partner not existed", mcontext);
                } else {
                    Intent k = new Intent(mcontext, ActivityDriverChat.class);
                    k.putExtra("map", (Serializable) dmap1);
                    startActivity(k);
                }
                break;
            case R.id.waitingcharge:
                waitingChargesFRideRequest(mcontext);
                break;

            case R.id.iv_partner_call:
                isPartnerCall = true;
                showMessage("Do You Want To Call ?");
                /*HashMap<String, Object> dmapN = new HashMap<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (!value.toString().equalsIgnoreCase("null")) {
                        dmapN.put(key, value);
                    }
                }

                if (!dmapN.containsKey("partnerid")) {
                    Utils.toastTxt("Partner not existed", mcontext);
                } else {
                    Intent k = new Intent(mcontext, ActivityDriverChat.class);
                    k.putExtra("map", (Serializable) dmapN);
                    startActivity(k);
                }*/
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RC_CALL_PERM:
                if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    if (!isPartnerCall) {
                        getCall(unumber.getText().toString());
                    } else {
                        getCall(partnerNumber);
                    }
                }
                break;
        }
    }

    public void showMessage(String dlgText) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog6, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final Button attend = dialog.findViewById(R.id.attend);
        final Button cancel = dialog.findViewById(R.id.cancel);
        textView.setText(dlgText);
        title.setText(R.string.app_name);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                    if (!isPartnerCall)
                        callPermission(unumber.getText().toString());
                    else {
                        callPermission(partnerNumber);
                    }
                } catch (Exception e) {

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void callPermission(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                getCall(number);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, RC_CALL_PERM);
                }
            }
        } else {
            getCall(number);
        }
    }

    public void getCall(String number) {
        if (number.trim().length() > 0)
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)));
    }

    public void getRideUserDetailsRequest(Context mcontext) {
        map = new HashMap<>();

        if (rMap.containsKey("id"))
            map.put("ride_id", rMap.get("id").toString());

        rideId = rMap.get("id").toString();

        OnlineRequest.getRideUserDetailsRequest(mcontext, map);
    }


    private void setAdditionalStopsApi() {
        map = new HashMap<>();

        if (rMap.containsKey("id"))
            map.put("ride_id", rMap.get("id").toString());

        OnlineRequest.getAdditionalStops(mcontext, map);
    }

    public void waitingChargesOnFRideStart() {
        charges_Waiting.setVisibility(View.GONE);
    }

    @SuppressLint("LongLogTag")
    public static void waitingChargesFRideRequest(Context mcontext) {

        map = new HashMap<>();

        Log.e(TAG, "rMapRRRR " + rMap.get("id").toString());

        if (rMap.containsKey("id"))
            map.put("rideid", rMap.get("id").toString());

        OnlineRequest.waitingChargesFrideRequest(mcontext, map);
    }

    @SuppressLint("LongLogTag")
    public void getFutureRideUserDetails(List<HashMap<String, Object>> list, final JSONObject jsonObject) {
        map = new HashMap<>();
        if (list != null) {
            user_root.setVisibility(View.VISIBLE);
            map = list.get(0);
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            if (map.containsKey("future_ride_start")) {
                if (map.get("future_ride_start").toString().equalsIgnoreCase("1")) {
                    rComplete.setText(COMPLETE_RIDE);
                    //     rComplete.setBackgroundColor(getResources().getColor(R.color.green));
                    charges_Waiting.setVisibility(View.GONE);
                    btnCancelRide.setEnabled(false);
                    btnCancelRide.getBackground().setAlpha(100);
                    ride_start.setVisibility(View.VISIBLE);

                    Log.e(TAG , "future_ride_start11 "+map.get("future_ride_start").toString());
                } else {
                  //  ride_start.setVisibility(View.GONE);
                    charges_Waiting.setVisibility(View.GONE);
                    Log.e(TAG , "future_ride_start22 "+map.get("future_ride_start").toString());

                }
            }
            if (jsonObject.optString("planned_stops") != null) {
                if (jsonObject.optString("planned_stops").toString().trim().equalsIgnoreCase("")) {
                    tvPlannedStops.setText("Planned Stops : 0");
                } else {
                    tvPlannedStops.setText("Planned Stops : " + jsonObject.optString("planned_stops"));

                    Log.e(TAG ,"VVVVVVVVV "+jsonObject.optString("planned_address"));


                    tvPlannedStops.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ViewCustomerFRideDetails.this , AditionalStops.class);
                            intent.putExtra("key" , ""+jsonObject.optString("planned_address"));
                            startActivity(intent);
                        }
                    });



                }
            }

            Log.e(TAG ,"VVVVVVVVVSSS "+map.toString());

            if (map.containsKey("car_transmission")) {
                if (!map.get("car_transmission").toString().trim().equalsIgnoreCase("")) {
                    tvTrans.setText("Transmission: "+map.get("car_transmission").toString());
                } else {

                }
            }




            if (map.containsKey("partnerfname")) {
                if (map.get("partnerfname").toString().trim().equalsIgnoreCase("null")
                        || map.get("partnerfname").toString().trim().equalsIgnoreCase("")) {
                    btnPartnerInfo.setEnabled(false);
                    btnPartnerInfo.getBackground().setAlpha(100);
                    ivPartnerCall.setVisibility(View.GONE);
                } else {
                    partnerNumber = map.get("partnermobile").toString().trim();
                    ivPartnerCall.setVisibility(View.VISIBLE);
                }
            }
            if (map.containsKey("userpic")) {
                if (!map.get("userpic").toString().equalsIgnoreCase(""))
                    Picasso.with(mcontext).load("http://latenightchauffeurs.com/lnc-administrator/uploads/"
                            + map.get("userpic").toString()).placeholder(R.drawable.appicon).into(userimage);
            }
            StringBuilder sb = new StringBuilder();
            if (map.containsKey("userfname")) {
                if (!map.get("userfname").toString().equalsIgnoreCase(""))
                    sb.append(map.get("userfname").toString());
            }
            if (map.containsKey("userlname")) {
                if (!map.get("userlname").toString().equalsIgnoreCase("")) sb.append(" ");
                sb.append(map.get("userlname").toString());
                uname.setText(sb);
            }
            if (map.containsKey("usermobile")) {
                if (!map.get("usermobile").toString().equalsIgnoreCase(""))
                    unumber.setText(map.get("usermobile").toString());
            }

            String s = "", e = "";
            if (map.containsKey("pickup_address")) {
                if (!map.get("pickup_address").toString().equalsIgnoreCase(""))
                    s = getColoredSpanned(map.get("pickup_address").toString(), "#F69625");
            }
            if (map.containsKey("drop_address")) {
                if (!map.get("drop_address").toString().equalsIgnoreCase(""))
                    e = getColoredSpanned(map.get("drop_address").toString(), "#F69625");
                //  rideAddress.setText(Html.fromHtml("Ride from :<br />" + s + "<br />  to  <br />" + e));


                String styledTextRide1 = "<font color='#19DA2A'><b>Ride From:&nbsp; </b></font><font color='#ffffff'>" + s + "</font><br/>";

                String styledTextRide2 = "<font color='#19DA2A'><b>Ride To:&nbsp; </b></font><font color='#ffffff'>" + e + "</font>";


                rideAddress.setText(Html.fromHtml(styledTextRide1 + styledTextRide2), TextView.BufferType.SPANNABLE);
                // rideAddress.setVisibility(View.VISIBLE);


            }
            if (map.containsKey("ride_date")) {
                Calendar localCalendar = Calendar.getInstance();
                int currentDay = localCalendar.get(Calendar.DATE);
                int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
                int currentYear = localCalendar.get(Calendar.YEAR);

                String currMonth = "", currDay = "", currYr = "";
                if (currentMonth < 9) {
                    currMonth = "0" + currentMonth;
                } else {
                    currMonth = "" + currentMonth;
                }

                if (currentDay < 9) {
                    currDay = "0" + currentDay;
                } else {
                    currDay = "" + currentDay;
                }
                currYr = "" + currentYear;
                String curreDay = currMonth + "-" + currDay + "-" + currYr;
                serverDate = getDate(map.get("ride_date").toString());
                currentDate = getDate(curreDay);
                serverTime = getTime(map.get("ride_date").toString() + " " + map.get("ride_time").toString());
                currentTime = getTime(curreDay + " " + convertTime());
                difference = serverTime - currentTime;
                minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
                Utils.logError("Taggggg", serverDate + " " + currentDate + " " + minutes);
                if (serverDate <= currentDate) {
                    if (minutes < 15) {
                        rComplete.setEnabled(true);
                        charges_Waiting.setEnabled(true);
                        btnAdditionalStops.setEnabled(true);
                    } else {
                        rComplete.setEnabled(false);
                        charges_Waiting.setEnabled(false);
                        btnAdditionalStops.setEnabled(false);
                        rComplete.getBackground().setAlpha(100);
                        charges_Waiting.getBackground().setAlpha(100);
                        btnAdditionalStops.getBackground().setAlpha(100);
                    }
                } else {
                    rComplete.setEnabled(false);
                    rComplete.getBackground().setAlpha(100);
                    btnAdditionalStops.setEnabled(false);
                    btnAdditionalStops.getBackground().setAlpha(100);
                }
                if (minutes < 1) {
                    charges_Waiting.setEnabled(true);
                } else {
                    charges_Waiting.setEnabled(false);
                    charges_Waiting.getBackground().setAlpha(100);
                }
            }

        } else {
            user_root.setVisibility(View.VISIBLE);
        }

        if (rComplete.getText().toString().equals(COMPLETE_RIDE)) {
            btnWaitingTime.setEnabled(true);
            llTimer.setVisibility(View.GONE);
            charges_Waiting.setEnabled(false);
            charges_Waiting.getBackground().setAlpha(100);
            ride_start.setVisibility(View.VISIBLE);
        } else {
            btnWaitingTime.setEnabled(false);
            btnWaitingTime.getBackground().setAlpha(100);
            llTimer.setVisibility(View.GONE);
            charges_Waiting.setEnabled(true);
            ride_start.setVisibility(View.GONE);
        }

        try {
            startWaitingTime = jsonObject.opt("waiting_start_time").toString();
            if (!jsonObject.opt("waiting_status").equals("") && jsonObject.opt("waiting_status")
                    .toString().equalsIgnoreCase("start")) {
                btnWaitingTime.setText("Stop Waiting Time");
                llTimer.setVisibility(View.GONE);
                tvInfoRideStart.setText("Waiting Time Was Start: " + startWaitingTime);
                //startTimer();
            } else {
                llTimer.setVisibility(View.GONE);
                btnWaitingTime.setText("Start Waiting Time");
            }

            if (jsonObject.optInt("waiting_time_difference") >= 240) {
                //btnWaitingTime.setText("Stop Waiting Time");
                //btnWaitingTime.performClick();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        Thread myThread = null;
        Runnable runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();
    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    TextView txtCurrentTime = findViewById(R.id.tv_timer);
                    Date dt = dateFormat.getCalendar().getTime();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = "";
                    if (seconds < 10)
                        curTime = hours + ":" + minutes + ":0" + seconds;
                    else {
                        curTime = hours + ":" + minutes + ":" + seconds;
                    }
                    txtCurrentTime.setText(curTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public String convertTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void setWaitingTimeStstus(boolean waitingStatus) {
        if (waitingStatus) {
            try {
                llTimer.setVisibility(View.VISIBLE);
                btnWaitingTime.setText("Stop Waiting Time");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formattedDate = df.format(c.getTime());
                tvInfoRideStart.setText("Waiting Time Was Start: " + formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            llTimer.setVisibility(View.GONE);
            btnWaitingTime.setText("Start Waiting Time");
        }

    }

    public long getDate(String rideDate) {
        long mills = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            Date date = formatter.parse(rideDate);
            mills = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mills;
    }

    public long getTime(String rideDate) {
        long mills = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
            Date date = formatter.parse(rideDate);
            mills = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mills;
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @SuppressLint("LongLogTag")
    public void showPartnerORDriverDetailsDilog(HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View dialogLayout = inflater.inflate(R.layout.alert_dialog7, null);
        final AlertDialog dialog = new AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        TextView title = dialog.findViewById(R.id.title);
        TextView textView = dialog.findViewById(R.id.desc);
        TextView name = dialog.findViewById(R.id.name);
        TextView number = dialog.findViewById(R.id.number);
        TextView email = dialog.findViewById(R.id.email);
        Button ok = dialog.findViewById(R.id.ok);

        title.setText(R.string.app_name);
        textView.setText("Partner Details");
        Log.e(TAG, "Partner " + data.toString());

        if (data != null && data.size() > 0) {
            StringBuilder sb = new StringBuilder();
            if (data.containsKey("partnerfname")) {
                if (!data.get("partnerfname").toString().equalsIgnoreCase("") &&
                        !data.get("partnerfname").toString().equalsIgnoreCase("null"))
                    sb.append(data.get("partnerfname").toString());
                name.setText(sb);
            }

            if (data.containsKey("partnerlname")) {
                if (!data.get("partnerlname").toString().equalsIgnoreCase("") &&
                        !data.get("partnerlname").toString().equalsIgnoreCase("null")) {
                    sb.append(" ");
                    sb.append(data.get("partnerlname").toString());
                    name.setText(sb);
                }
            }

            if (data.containsKey("partnermobile") && !data.get("partnermobile").toString().equalsIgnoreCase("")
                    && !data.get("partnermobile").toString().equalsIgnoreCase("null")) {
                number.setText(data.get("partnermobile").toString());
            }

            if (data.containsKey("partneremail") && !data.get("partneremail").toString().equalsIgnoreCase("")
                    && !data.get("partneremail").toString().equalsIgnoreCase("null")) {
                email.setText(data.get("partneremail").toString());
            }
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }
}

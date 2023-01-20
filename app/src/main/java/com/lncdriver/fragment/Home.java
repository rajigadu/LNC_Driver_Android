package com.lncdriver.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lncdriver.R;
import com.lncdriver.activity.ActivityDriverChat;
import com.lncdriver.activity.ActivityPartnerChat;
import com.lncdriver.activity.ActivityUserChat;
import com.lncdriver.activity.AuthenticatePartner;
import com.lncdriver.activity.CancelRide;
import com.lncdriver.activity.CompleteRide;
import com.lncdriver.activity.Navigation;
import com.lncdriver.model.AlarmReceiver;
import com.lncdriver.model.DataParser;
import com.lncdriver.model.GPSTracker;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;
import com.squareup.picasso.Picasso;
import androidx.annotation.Nullable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    public static Home Instance;
    private static final int NOTIFICATION_PERMISSION = 255;

    SendData sd;
    private Unbinder unbinder;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    View layout_bottom ;
    TextView bottom_text_ride;
    Button rComplete;
    Button cancelRide;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String TAG = Home.class.getName();
    public static Context mcontext;
    private GoogleMap mMap;
    GPSTracker gps;
    private Marker mMarker, uMarker = null;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static OnFragmentInteractionListenerHome mListener;
    HashMap<String, Object> map;
    static HashMap<String, Object> smap;
    private static final int RC_CALL_PERM = 100;

    TextView uname;
    TextView unumber;
    ImageView userimage;

    ImageView dnavigation;
    ImageView unavigation;
    ImageView call;
    ImageView msg;

    ImageView msg_driver;
    TextView isChatMsg;
    TextView rideAddress;

    Button partner, charges_Waiting;
    TextView online;
    TextView offline;
    ImageView imgOnStatus;
    TextView dataOnStatus;

    public static Home newInstance() {
        Home navigationFragment = new Home();
        Bundle args = new Bundle();
        navigationFragment.setArguments(args);
        return navigationFragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete:
                // getBookingResponce();
                break;
            case R.id.partner:
                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                String cmap = pref1.getcustomermap();

                if (!cmap.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, Object> ssmap = gson.fromJson(cmap, type);
                    getPartnerDetails(ssmap);
                }
                break;
            case R.id.online:
                driverOnlineRequest("1");
                break;
            case R.id.logout:
                driverOnlineRequest("2");
                break;
            case R.id.call:
                SavePref pref2 = new SavePref();
                pref2.SavePref(mcontext);
                String ccmap = pref2.getcustomermap();

                if (!ccmap.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, Object> map = gson.fromJson(ccmap, type);
                    showMessage("Do You Want To Call ?", map);
                }
                break;
            case R.id.msg:
                isChatMsg.setVisibility(View.GONE);
                Utils.startActivity(mcontext, ActivityUserChat.class);
                break;
            case R.id.msgd:
                driverTypeRequest();
                break;
            case R.id.cancel:
                SavePref pref3 = new SavePref();
                pref3.SavePref(mcontext);
                String rid = pref3.getRideId();

                if (!rid.isEmpty()) {
                    Intent i = new Intent(mcontext, CancelRide.class);
                    i.putExtra("rideid", rid);
                    startActivity(i);
                }
                break;
            case R.id.waitingcharge:
                waitingChargesRequest(mcontext);
                break;
        }
    }

    public interface SendData {
        void sendData(HashMap<String, Object> smap);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));

        mcontext = getActivity();
        showNotificationPermission();

    }

    private void showNotificationPermission() {
        if (ContextCompat.checkSelfPermission(mcontext,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            showNotificationPermissionRequest();
        }
    }

    private void showNotificationPermissionRequest() {
        if (Build.VERSION.SDK_INT >= 33 &&
                !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            requestPermissions(new String[] {Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        unbinder = ButterKnife.bind(this, v);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //bottom_text_ride = v.findViewById(R.id.ride_text);
        layout_bottom = v.findViewById(R.id.bottom_sheet);

        userimage = v.findViewById(R.id.userimage);
        uname = v.findViewById(R.id.uname);
        unumber = v.findViewById(R.id.unumber);
        rideAddress = v.findViewById(R.id.rideaddress);
        partner = v.findViewById(R.id.partner);
        isChatMsg = v.findViewById(R.id.newchatmsg);
        //intimation_partner=v.findViewById(R.id.pintimation);
        rComplete = v.findViewById(R.id.complete);
        cancelRide = v.findViewById(R.id.cancel);
        dnavigation = v.findViewById(R.id.dnavigation);
        unavigation = v.findViewById(R.id.unavigation);
        call = v.findViewById(R.id.call);
        msg = v.findViewById(R.id.msg);
        msg_driver = v.findViewById(R.id.msgd);
        online = v.findViewById(R.id.online);
        offline = v.findViewById(R.id.logout);
        imgOnStatus = v.findViewById(R.id.onlinestatusimg);
        dataOnStatus = v.findViewById(R.id.onlinestatus);
        charges_Waiting = v.findViewById(R.id.waitingcharge);

        Instance = this;

        layout_bottom.setVisibility(View.GONE);
        // charges_Waiting.setVisibility(View.GONE);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        partner.setOnClickListener(this);
        rComplete.setOnClickListener(this);
        online.setOnClickListener(this);
        offline.setOnClickListener(this);
        call.setOnClickListener(this);
        msg.setOnClickListener(this);
        msg_driver.setOnClickListener(this);
        cancelRide.setOnClickListener(this);
        charges_Waiting.setOnClickListener(this);

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                /*switch (newState)
                {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                       *//* {
                        btnBottomSheet.setText("Close Sheet");
                    }*//*
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                       *//* {
                        btnBottomSheet.setText("Expand Sheet");
                    }*//*
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        sheetBehavior.setPeekHeight(0);

        rComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                String isridestart = pref1.getisridestart();
                String rid = pref1.getRideId();

                if (!rid.equalsIgnoreCase("")) {
                    if (isridestart.equalsIgnoreCase("")) {
                        currentRideStartRequest(mcontext, rid);
                    } else {
                        if (Global.ridePrices.containsKey("ride_cost")) {
                            if (!Global.ridePrices.get("ride_cost").toString().equalsIgnoreCase("0")) {
                                Utils.logError("ridemap12233455===", Global.ridePrices.toString());

                                Intent i = new Intent(mcontext, CompleteRide.class);
                                i.putExtra("map", (Serializable) Global.ridePrices);
                                startActivityForResult(i, 123);
                            } else {
                                getCurrentRideRequests();
                            }
                        }
                    }
                } else {
                    Utils.toastTxt("something wrong try again later", mcontext);
                }
            }
        });

        dnavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                Gson gson = new Gson();

                String cmap = pref1.getcustomermap();

                if (!cmap.equalsIgnoreCase("")) {
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, String> map = gson.fromJson(cmap, type);

                    try {
                        if (map != null && map.size() > 0) {
                            String add = "";

                            if (map.containsKey("d_lat") && !map.get("d_lat").equalsIgnoreCase(""))
                                add = "google.navigation:q=" + map.get("d_lat") + "," + map.get("d_long") + "&mode=d";

                            Uri gmmIntentUri = Uri.parse(add);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
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
                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                Gson gson = new Gson();
                boolean isAppDisabled = false;

                String cmap = pref1.getcustomermap();

                if (!cmap.equalsIgnoreCase("")) {
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, String> map = gson.fromJson(cmap, type);

                    if (map != null && map.size() > 0) {
                        try {
                            if (map != null && map.size() > 0) {
                                String add = "";

                                if (map.containsKey("pickup_lat") && !map.get("pickup_lat").equalsIgnoreCase(""))
                                    add = "google.navigation:q=" + map.get("pickup_lat") + "," + map.get("pickup_long") + "&mode=d";

                                Uri gmmIntentUri = Uri.parse(add);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        } catch (ActivityNotFoundException ane) {
                            Toast.makeText(mcontext, "Please Install Google Maps", Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                }
            }
        });

        Bundle b = getArguments();

        if (b != null) {
            if (b.containsKey("map")) {
                map = new HashMap<>();
                map = (HashMap<String, Object>) b.getSerializable("map");

                if (map != null) {
                    Navigation.DriverAcceptRequest(mcontext, Global.mapData, "1");
                }
            }
        }

        driverOnlineStatusRequest();
        getCurrentRideRequests();
        getNextRideRequests();
        //callApiNextRideMethod();
       /* Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

        Utils.logError("datetime======9890898=======",df.format(c));*/
    }




    private void callApiNextRideMethod() {
        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
        Call<ResponseBody> call = null;
        SavePref pref = new SavePref();
        pref.SavePref(getActivity());

        call = apiInterface.getNextRide(pref.getUserId());
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

//

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                rideCompleteRequest(mcontext);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                // String result=data.getStringExtra("result");
                Toast.makeText(mcontext, " Cancelled ride complete.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void rideCompleteRequest(Context mcontext) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String cid = pref1.getCustomerId();
        String rid = pref1.getRideId();

        map = new HashMap<>();
        map.put("userid", cid);
        map.put("driverid", id);
        map.put("rideid", rid);
        OnlineRequest.rideCompleteRequest(mcontext, map);
    }

    public void waitingChargesOnStart() {
        charges_Waiting.setVisibility(View.GONE);
    }

    public void statusCurrentRideStart() {
        rComplete.setText("COMPLETE RIDE");
        rComplete.setBackgroundColor(getResources().getColor(R.color.green));
        dnavigation.setVisibility(View.VISIBLE);
        charges_Waiting.setVisibility(View.GONE);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String isridestart = pref1.getisridestart();

        if (isridestart.equalsIgnoreCase("")) {
            rComplete.setText("START RIDE");
            // dnavigation.setVisibility(View.GONE);
            rComplete.setBackgroundColor(getResources().getColor(R.color.black));
            // unavigation.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(mcontext, AlarmReceiver.class);
            PendingIntent.getBroadcast(mcontext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();

            rComplete.setText("COMPLETE RIDE");
            rComplete.setBackgroundColor(getResources().getColor(R.color.green));
            //dnavigation.setVisibility(View.VISIBLE);
        }
    }

    public static void getCurrentRideRequests() {
        SharedPreferences pref = mcontext.getApplicationContext().getSharedPreferences("lncdrivertoken", 0);
        SharedPreferences.Editor editor = pref.edit();

        String tId = pref.getString("tokenid", null);
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String typ = pref1.getaccepttype();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put("type", typ);
        Global.mapMain.put("devicetoken", tId);
        Global.mapMain.put("device_type", "android");
        Global.mapMain.put(ConstVariable.URL, Settings.URL_CURRENTRIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.CCRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }


    public static void getNextRideRequests() {
        SharedPreferences pref = mcontext.getApplicationContext().getSharedPreferences("lncdrivertoken", 0);
        SharedPreferences.Editor editor = pref.edit();

        String tId = pref.getString("tokenid", null);
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String typ = pref1.getaccepttype();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put("type", typ);
        Global.mapMain.put("devicetoken", tId);
        Global.mapMain.put("device_type", "android");
        Global.mapMain.put(ConstVariable.URL, Settings.URL_NEXT_RIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.NRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }



    public static void paymentRequest(Context mcontext) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String cid = pref1.getCustomerId();
        String rid = pref1.getRideId();

        smap = new HashMap<>();
        smap.put("user_id", cid);
        smap.put("booking_id", rid);
        OnlineRequest.paymentRequest(mcontext, smap);
    }

    public static void currentRideStartRequest(Context mcontext, String tmap) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String did = pref1.getUserId();

        smap = new HashMap<>();
        smap.put("driverid", did);

        if (!tmap.equalsIgnoreCase(""))
            smap.put("rideid", tmap);
        else
            smap.put("rideid", "");

        OnlineRequest.currentRideStartRequest(mcontext, smap);
    }

    public static void waitingChargesRequest(Context mcontext) {
        OnlineRequest.waitingChargesRequest(mcontext, null);
    }


    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String isid = intent.getStringExtra("isid");

                Log.d(TAG, "NotificationMessageBody123: " + isid);

                if (isid.equalsIgnoreCase("2")) {
                    String status = intent.getStringExtra("status");
                    // Toast.makeText(mcontext,status,Toast.LENGTH_LONG).show();
                    HashMap<String, Object> data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    //Toast.makeText(mcontext,"jhghjghg",Toast.LENGTH_LONG).show();
                    layout_bottom.setVisibility(View.VISIBLE);
                    //isdriving=true;

                    if (data.containsKey("profile_pic")) {
                        if (!data.get("profile_pic").toString().equalsIgnoreCase(""))
                            Picasso.with(getActivity()).load("http://qwikrides.com/latenightchauffeurs/uploads/" + data.get("profile_pic").toString()).placeholder(R.drawable.appicon).into(userimage);
                        // userpic.setImageBitmap(getBitmapFromURL(image));
                    }

                    StringBuilder sb = new StringBuilder();

                    if (data.containsKey("first_name")) {
                        if (!data.get("first_name").toString().equalsIgnoreCase(""))
                            sb.append(data.get("first_name").toString());
                    }

                    if (data.containsKey("last_name")) {
                        if (!data.get("last_name").toString().equalsIgnoreCase(""))
                            sb.append(" ");
                        sb.append(data.get("last_name").toString());
                        uname.setText(sb);
                    }

                   /* if(data.containsKey("name")&&!data.get("name").toString().equalsIgnoreCase(""))
                    {
                        uname.setText(data.get("name").toString());
                    }*/

                    if (data.containsKey("mobile")) {
                        if (!data.get("mobile").toString().equalsIgnoreCase(""))
                            unumber.setText(data.get("mobile").toString());
                    }

                    String s = "", e = "";
                    if (data.containsKey("pickup")) {
                        if (!data.get("pickup").toString().equalsIgnoreCase(""))
                            s = getColoredSpanned(data.get("pickup").toString(), "#F69625");
                    }

                    if (data.containsKey("drop")) {
                        if (!data.get("drop").toString().equalsIgnoreCase(""))
                            e = getColoredSpanned(data.get("drop").toString(), "#F69625");
                        rideAddress.setText(Html.fromHtml("Ride from :<br />" + s + "<br />  to  <br />" + e));
                    }

                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mcontext);

                    if (data.containsKey("type")) {
                        if (data.get("type").toString().equalsIgnoreCase("partner")) {
                            call.setVisibility(View.GONE);
                            msg.setVisibility(View.GONE);
                            rComplete.setVisibility(View.GONE);
                            cancelRide.setVisibility(View.GONE);

                            partner.setText("Driver");
                            //intimation_partner.setVisibility(View.GONE);
                            pref1.setispartnerAuth((""));
                        } else if (data.get("type").toString().equalsIgnoreCase("driver")) {
                            call.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.VISIBLE);
                            rComplete.setVisibility(View.VISIBLE);
                            cancelRide.setVisibility(View.VISIBLE);

                            partner.setText("Partner");
                            // intimation_partner.setVisibility(View.VISIBLE);
                            pref1.setispartnerAuth(("ok"));
                        }
                    }

                    if (!data.get("userid").toString().equalsIgnoreCase(""))
                        pref1.setCustomerId(data.get("userid").toString());

                    if (!data.get("ride_id").toString().equalsIgnoreCase(""))
                        pref1.setRideId(data.get("ride_id").toString());

                    if (!data.get("type").toString().equalsIgnoreCase(""))
                        pref1.setaccepttype(data.get("type").toString());

                    pref1.setisnewride("");
                    pref1.setisridestart("");

                    Gson gson = new Gson();
                    String hashMapString = gson.toJson(data);

                    String ispartnerAuth = pref1.getispartnerAuth();

                    if (status.equalsIgnoreCase("1") && !ispartnerAuth.equalsIgnoreCase("")) {
                        Intent i = new Intent(mcontext, AuthenticatePartner.class);
                        mcontext.startActivity(i);
                    }

                    String ss = pref1.getIsNewMsg();
                    pref1.setcustomermap(hashMapString);

                    if (!ss.equalsIgnoreCase("")) {
                        isChatMsg.setVisibility(View.VISIBLE);
                    } else {
                        isChatMsg.setVisibility(View.GONE);
                    }

                    String isridestart = pref1.getisridestart();
                    if (isridestart.equalsIgnoreCase("")) {
                        rComplete.setText("START RIDE");
                        dnavigation.setVisibility(View.GONE);
                        rComplete.setBackgroundColor(getResources().getColor(R.color.black));
                        unavigation.setVisibility(View.VISIBLE);
                    } else {
                        rComplete.setText("COMPLETE RIDE");
                        rComplete.setBackgroundColor(getResources().getColor(R.color.green));
                        dnavigation.setVisibility(View.VISIBLE);
                    }

                    //isdriving=true;
                    Log.e("ride1234567,", pref1.getRideId());

                    LatLng origin = null, dest = null;

                    mMap.clear();

                    //dest=new LatLng(30.7055,76.8013);
                    // dest=new LatLng(30.7068,76.7082);

                    if (data.containsKey("driver_lat") && !data.get("driver_lat").toString().equalsIgnoreCase("null"))
                        origin = new LatLng(Double.valueOf(data.get("driver_lat").toString()), Double.valueOf(data.get("driver_long").toString()));

                    if (data.containsKey("pickup") && !data.get("pickup").toString().equalsIgnoreCase("null"))
                        dest = new LatLng(Double.valueOf(data.get("pickup_lat").toString()), Double.valueOf(data.get("pickup_long").toString()));

                    if (origin != null && dest != null) {
                        MarkerOptions options = new MarkerOptions();
                        options.position(origin);
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car38);
                        options.icon(icon);
                        mMap.addMarker(options);

                        MarkerOptions options1 = new MarkerOptions();
                        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        options1.position(dest);
                        mMap.addMarker(options1);

                        String url = getUrl(origin, dest);

                        if (!url.equalsIgnoreCase("") && !url.equalsIgnoreCase("null")) {
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);
                        }

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(origin);
                        builder.include(dest);

                        LatLngBounds bounds = builder.build();

                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.10);// offset from edges of the map 10% of screen

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                        mMap.animateCamera(cu);
                    }
                    driverOnlineStatusRequest();
                } else if (isid.equalsIgnoreCase("3")) {
                    String status = intent.getStringExtra("status");
                    //Toast.makeText(mcontext,"jhghjghg",Toast.LENGTH_LONG).show();
                } else if (isid.equalsIgnoreCase("5")) {
                    try {
                        HashMap<String, Object> data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                        //Toast.makeText(mcontext,"jhghjghg",Toast.LENGTH_LONG).show();
                        layout_bottom.setVisibility(View.VISIBLE);

                        uname.setText(data.get("name").toString());
                        unumber.setText(data.get("mobile").toString());

                        if (data.containsKey("photo") && !data.get("photo").toString().equalsIgnoreCase("")) {
                            Picasso.with(mcontext).load(data.get("photo").toString()).placeholder(R.drawable.appicon).into(userimage);
                            // userpic.setImageBitmap(getBitmapFromURL(image));
                        }

                        String s = "", e = "";

                        if (data.containsKey("pickup_address") && !data.get("pickup_address").toString().equalsIgnoreCase(""))
                            s = getColoredSpanned(data.get("pickup_address").toString(), "#F69625");

                        if (data.containsKey("drop_address") && !data.get("drop_address").toString().equalsIgnoreCase(""))
                            e = getColoredSpanned(data.get("drop_address").toString(), "#F69625");

                        rideAddress.setText(Html.fromHtml("Ride from :<br />" + s + "<br />  to  <br />" + e));

                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        pref1.setCustomerId(data.get("userid").toString());
                        pref1.setRideId(data.get("id").toString());
                        pref1.setridemap("");
                        pref1.setisnewride("");
                        pref1.setisridestart("");

                        String ss = pref1.getIsNewMsg();

                     /*   if (data.containsKey("driverid")&& !data.get("driverid").toString().equalsIgnoreCase(""))
                        {
                            String did=pref1.getUserId();

                            if(did.equalsIgnoreCase(data.get("driverid").toString()))
                            {
                                partner.setText("DRIVER");
                            }
                            else
                            {
                                partner.setText("PARTNER");
                            }
                        }*/

                        if (!ss.equalsIgnoreCase("")) {
                            isChatMsg.setVisibility(View.VISIBLE);
                        } else {
                            isChatMsg.setVisibility(View.GONE);
                        }

                        String isRideStart = pref1.getisridestart();

                        if (isRideStart.equalsIgnoreCase("")) {
                            rComplete.setText("START RIDE");
                            dnavigation.setVisibility(View.GONE);
                            rComplete.setBackgroundColor(getResources().getColor(R.color.black));
                            unavigation.setVisibility(View.VISIBLE);
                        } else {
                            rComplete.setText("COMPLETE RIDE");
                            rComplete.setBackgroundColor(getResources().getColor(R.color.green));
                            dnavigation.setVisibility(View.VISIBLE);
                        }

                        //isdriving=true;
                        Log.e("ride1234567,", pref1.getRideId());

                        Gson gson = new Gson();
                        String hashMapString = gson.toJson(data);
                        pref1.setcustomermap(hashMapString);
                    } catch (Exception e) {
                        Log.e("error123,", e.getMessage());
                    }
                    driverOnlineStatusRequest();
                } else if (isid.equalsIgnoreCase("6")) {
                    String status = intent.getStringExtra("status");

                    //Toast.makeText(mcontext,"jhghjghg",Toast.LENGTH_LONG).show();
                    //isdriving=false;
                    layout_bottom.setVisibility(View.GONE);
                }
                if (isid.equalsIgnoreCase("9")) {
                    // String status = intent.getStringExtra("status");

                    HashMap<String, Object> data = new HashMap<>();
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newusermessage")) {
                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        pref1.setisnewmsg(data.get("ride").toString());

                        // DriverChat.driversChatListRequest();
                        String ss = pref1.getIsNewMsg();

                        if (!ss.equalsIgnoreCase("")) {
                            isChatMsg.setVisibility(View.VISIBLE);
                        } else {
                            isChatMsg.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.isMyLocationEnabled();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            gps = new GPSTracker(mcontext);

            if (gps.canGetLocation()) {
                LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                //markerOptions.title("my Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                uMarker = mMap.addMarker(markerOptions);
                //uMarker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                DriverLocationUpdateRequest(mcontext, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
            } else {
                gps.showSettingsAlert();
            }
        }
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Utils.toastTxt("permissions granted",mcontext);

            gps = new GPSTracker(mcontext);

            if (gps.canGetLocation()) {
                LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                //markerOptions.title("my Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                uMarker = mMap.addMarker(markerOptions);
                // uMarker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                DriverLocationUpdateRequest(mcontext, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
            } else {
                gps.showSettingsAlert();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case NOTIFICATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showPermissionDeniedDialog();
                }
            break;
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(mcontext,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        // Utils.toastTxt("permissions granted",mcontext);

                        gps = new GPSTracker(mcontext);

                        if (gps.canGetLocation()) {
                            LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            // markerOptions.title("my Location");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            uMarker = mMap.addMarker(markerOptions);
                            // uMarker.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                            DriverLocationUpdateRequest(mcontext, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
                        } else {
                            gps.showSettingsAlert();
                        }
                    }
                } else {
                    Utils.toastTxt("need permissions for location update", mcontext);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case RC_CALL_PERM:
                if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCall(map);
                }
                break;
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle(getString(R.string.notification_permission_denied_title));
        builder.setMessage(getString(R.string.notification_permission_denied_message));
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            navigateToSettings();
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToSettings() {
        try {
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(uri);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    public void DriverLocationUpdateRequest(Context mcontext, String lat, String lng) {
        map = new HashMap<>();
        map.put("latitude", lat);
        map.put("longitude", lng);
        OnlineRequest.updateDriverLocationRequest(mcontext, map);
    }

    @Override
    public void onPause() {
        super.onPause();
        // mGoogleApiClient.stopAutoManage(getActivity());
        //mGoogleApiClient.disconnect();
    }

    public interface OnFragmentInteractionListenerHome {
        // TODO: Update argument type and name
        void onFragmentInteraction(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerHome) {
            mListener = (OnFragmentInteractionListenerHome) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        try {
            sd = (SendData) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mHandleMessageReceiver != null)
            getActivity().unregisterReceiver(mHandleMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void displayReceivedData(HashMap<String, Object> smap) {
        //txtData.setText("Data received: "+message);
    }

    public static void getBookingResponce() {
        mListener.onFragmentInteraction(ConstVariable.Home);
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public void showPartnerORDriverDetailsDilog(final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog7, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        // final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final TextView name = (TextView) dialog.findViewById(R.id.name);
        final TextView number = (TextView) dialog.findViewById(R.id.number);
        final TextView email = (TextView) dialog.findViewById(R.id.email);
        final Button ok = (Button) dialog.findViewById(R.id.ok);

        title.setText(R.string.app_name);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String typ = pref1.getaccepttype();

        if (!typ.equalsIgnoreCase("") && typ.equalsIgnoreCase("partner")) {
            textView.setText("Driver Details");
        } else if (!typ.equalsIgnoreCase("") && typ.equalsIgnoreCase("driver")) {
            textView.setText("Partner Details");
        }

        if (data != null && data.size() > 0) {
            if (data.containsKey("name") && !data.get("name").toString().equalsIgnoreCase("")) {
                name.setText(data.get("name").toString());
            }

            if (data.containsKey("phone") && !data.get("phone").toString().equalsIgnoreCase("")) {
                number.setText(data.get("phone").toString());
            }

            if (data.containsKey("email") && !data.get("email").toString().equalsIgnoreCase("")) {
                email.setText(data.get("email").toString());
            }
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    dialog.cancel();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        dialog.show();
    }

    public void getCurrentRideDetails(HashMap<String, Object> vmap, String type) {
        if (type.equalsIgnoreCase("1")) {
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            pref1.setridemap("");
            pref1.setisnewride("");

            Global.dmap = vmap;

            //Toast.makeText(mcontext,"jhghjghg",Toast.LENGTH_LONG).show();
            layout_bottom.setVisibility(View.VISIBLE);

            StringBuilder sb = new StringBuilder();

            if (Global.dmap.containsKey("current_ride_start")) {
                if (Global.dmap.get("current_ride_start").toString().equalsIgnoreCase("1")) {
                    charges_Waiting.setVisibility(View.GONE);
                } else {
                    charges_Waiting.setVisibility(View.VISIBLE);
                }
            }

            if (Global.dmap.containsKey("first_name")) {
                if (!Global.dmap.get("first_name").toString().equalsIgnoreCase(""))
                    sb.append(Global.dmap.get("first_name").toString());
            }

            if (Global.dmap.containsKey("last_name")) {
                sb.append(" ");
                if (!Global.dmap.get("last_name").toString().equalsIgnoreCase(""))
                    sb.append(Global.dmap.get("last_name").toString());
                uname.setText(sb);
            }

           /* if(Utils.global.dmap.containsKey("first_name") && !Utils.global.dmap.get("first_name").toString().equalsIgnoreCase(""))
                uname.setText(Utils.global.dmap.get("first_name").toString());
*/
            if (Global.dmap.containsKey("mobile") && !Global.dmap.get("mobile").toString().equalsIgnoreCase(""))
                unumber.setText(Global.dmap.get("mobile").toString());

            if (Global.dmap.containsKey("userpic") && !Global.dmap.get("userpic").toString().equalsIgnoreCase("")) {
                Picasso.with(mcontext).load(Settings.URLIMAGEBASE + Global.dmap.get("userpic").toString()).placeholder(R.drawable.appicon).into(userimage);
                // userpic.setImageBitmap(getBitmapFromURL(image));
            }

            String s = "", e = "";

            if (Global.dmap.containsKey("pickup_address") && !Global.dmap.get("pickup_address").toString().equalsIgnoreCase(""))
                s = getColoredSpanned(Global.dmap.get("pickup_address").toString(), "#F69625");

            if (Global.dmap.containsKey("drop_address") && !Global.dmap.get("drop_address").toString().equalsIgnoreCase(""))
                e = getColoredSpanned(Global.dmap.get("drop_address").toString(), "#F69625");

            rideAddress.setText(Html.fromHtml("Ride from :<br />" + s + "<br />  to  <br />" + e));
            String typ = pref1.getaccepttype();

            if (!typ.equalsIgnoreCase("") && typ.equalsIgnoreCase("partner")) {
                call.setVisibility(View.GONE);
                msg.setVisibility(View.GONE);
                rComplete.setVisibility(View.GONE);
                cancelRide.setVisibility(View.GONE);
                partner.setText("Driver");
                //intimation_partner.setVisibility(View.GONE);
            } else if (!typ.equalsIgnoreCase("") && typ.equalsIgnoreCase("driver")) {
                cancelRide.setVisibility(View.VISIBLE);
                call.setVisibility(View.VISIBLE);
                msg.setVisibility(View.VISIBLE);
                rComplete.setVisibility(View.VISIBLE);
                partner.setText("Partner");
                //intimation_partner.setVisibility(View.VISIBLE);
            }
            pref1.SavePref(mcontext);
            pref1.setCustomerId(Global.dmap.get("user_id").toString());
            pref1.setRideId(Global.dmap.get("id").toString());

            String ispartnerAuth = pref1.getispartnerAuth();

            if (!ispartnerAuth.equalsIgnoreCase("")) {
                Intent i = new Intent(mcontext, AuthenticatePartner.class);
                mcontext.startActivity(i);
            }

            String ss = pref1.getIsNewMsg();

            if (!ss.equalsIgnoreCase("")) {
                isChatMsg.setVisibility(View.VISIBLE);
            } else {
                isChatMsg.setVisibility(View.GONE);
            }

            String isridestart = pref1.getisridestart();
            if (isridestart.equalsIgnoreCase("")) {
                rComplete.setText("START RIDE");
                dnavigation.setVisibility(View.GONE);
                rComplete.setBackgroundColor(getResources().getColor(R.color.black));
                unavigation.setVisibility(View.VISIBLE);
            } else {
                rComplete.setText("COMPLETE RIDE");
                rComplete.setBackgroundColor(getResources().getColor(R.color.green));
                dnavigation.setVisibility(View.VISIBLE);
            }

            Gson gson = new Gson();
            String hashMapString = gson.toJson(Global.dmap);
            pref1.setcustomermap(hashMapString);
            pref1.setisnewride("");

            if (!ss.equalsIgnoreCase("")) {
                isChatMsg.setVisibility(View.VISIBLE);
            } else {
                isChatMsg.setVisibility(View.GONE);
            }

            Log.e("ride1234567,", pref1.getRideId());
            LatLng origin = null, dest = null;
            mMap.clear();

            if (Global.dmap.containsKey("latitude") && !Global.dmap.get("latitude").toString().equalsIgnoreCase("null"))
                origin = new LatLng(Double.valueOf(Global.dmap.get("latitude").toString()), Double.valueOf(Global.dmap.get("longitude").toString()));

            if (Global.dmap.containsKey("pickup_lat") && !Global.dmap.get("pickup_lat").toString().equalsIgnoreCase("null"))
                dest = new LatLng(Double.valueOf(Global.dmap.get("pickup_lat").toString()), Double.valueOf(Global.dmap.get("pickup_long").toString()));

            if (origin != null && dest != null) {
                MarkerOptions options = new MarkerOptions();
                options.position(origin);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car38);
                options.icon(icon);
                mMap.addMarker(options);

                MarkerOptions options1 = new MarkerOptions();
                options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                options1.position(dest);
                mMap.addMarker(options1);

                String url = getUrl(origin, dest);
                if (!url.equalsIgnoreCase("") && !url.equalsIgnoreCase("null")) {
                    FetchUrl FetchUrl = new FetchUrl();
                    FetchUrl.execute(url);
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(dest);

                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10);// offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                mMap.animateCamera(cu);
            }
            driverOnlineStatusRequest();
        } else {
            layout_bottom.setVisibility(View.GONE);

            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            pref1.setridemap("");
            pref1.setisnewride("");




        }
    }

    public void onCompleteRideStatus() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        pref1.setCustomerId("");
        pref1.setRideId("");
        pref1.setcustomermap("");
        pref1.setridemap("");

        layout_bottom.setVisibility(View.GONE);

        LatLng pos = null;

        if (uMarker != null)
            pos = uMarker.getPosition();

        mMap.clear();

        if (pos != null) {
            MarkerOptions options1 = new MarkerOptions();
            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            options1.position(pos);
            mMap.addMarker(options1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14));
        }
        driverOnlineStatusRequest();
    }

    public void updateOnloineStatus(HashMap<String, Object> map) {
        if (map != null & map.size() > 0) {
            if (map.get("online_status").toString().equalsIgnoreCase("1")) {
                imgOnStatus.setImageResource(R.drawable.online);
                dataOnStatus.setText(map.get("msg").toString());
                dataOnStatus.setTextColor(getResources().getColor(R.color.green));
                online.setVisibility(View.GONE);
                offline.setVisibility(View.VISIBLE);
            } else {
                imgOnStatus.setImageResource(R.drawable.offline);
                dataOnStatus.setText(map.get("msg").toString());
                dataOnStatus.setTextColor(getResources().getColor(R.color.red));
                online.setVisibility(View.VISIBLE);
                offline.setVisibility(View.GONE);
            }

            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            if (Global.mapMain.containsKey("rating") && !Global.mapMain.get("rating").toString().equalsIgnoreCase(""))
                pref1.setRating(Global.mapMain.get("rating").toString());
        }

    }

    public void driverTypeRequest() {
        OnlineRequest.driverRoleRequest(mcontext, null);
    }

    public void driverOnlineRequest(String status) {
        new Utils(mcontext);
        map = new HashMap<>();
        map.put("status", status);
        OnlineRequest.driverOnlineRequest(mcontext, map);
    }

    public void driverOnlineStatusRequest() {
        new Utils(mcontext);
        OnlineRequest.driverOnlineStatusRequest(mcontext, null);
    }

    private void getPartnerDetails(HashMap<String, Object> vmap) {
        if (vmap.containsKey("ride_id") && !vmap.get("ride_id").toString().equalsIgnoreCase("")) {
            new Utils(mcontext);
            map = new HashMap<>();
            map.put("ride_id", vmap.get("ride_id").toString());
            OnlineRequest.getPartnerDetailsRequest(mcontext, map);
        } else if (vmap.containsKey("id") && !vmap.get("id").toString().equalsIgnoreCase("")) {
            new Utils(mcontext);
            map = new HashMap<>();
            map.put("ride_id", vmap.get("id").toString());
            OnlineRequest.getPartnerDetailsRequest(mcontext, map);
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            //Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            //Log.d("Exception", logError.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service

                data = downloadUrl(url[0]);
                //Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                //Log.d("Background Task", logError.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                //Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                // Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                // Log.d("ParserTask","Executing routes");
                //Log.d("ParserTask",routes.toString());
            } catch (Exception e) {
                //Log.d("ParserTask",logError.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
                //Log.d("onPostExecute","onPostExecute lineoptions decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                // Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

    public void showMessage(String dlgText, final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog6, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        // final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final Button attend = (Button) dialog.findViewById(R.id.attend);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        textView.setText(dlgText);
        title.setText(R.string.app_name);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                    callPermission(data);
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

    private void callPermission(HashMap<String, Object> map) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                getCall(map);
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, RC_CALL_PERM);
                //ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},RC_SMS_PERM);
            }
        } else {
            getCall(map);
        }
    }

    public void getCall(HashMap<String, Object> map) {
        if (map.containsKey("mobile_no") && !map.get("mobile_no").toString().equalsIgnoreCase("")) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", map.get("mobile_no").toString(), null)));
        }
    }

    public static void getDriverTypeResponce(HashMap<String, Object> dmap) {
        if (dmap.containsKey("reciever_id") && !dmap.get("reciever_id").toString().equalsIgnoreCase("0")) {
            if (dmap.containsKey("role") && dmap.get("role").toString().equalsIgnoreCase("driver")) {
                Intent i = new Intent(mcontext, ActivityDriverChat.class);
                i.putExtra("role", dmap.get("role").toString());
                i.putExtra("rid", dmap.get("reciever_id").toString());
                mcontext.startActivity(i);
            } else if (dmap.containsKey("role") && dmap.get("role").toString().equalsIgnoreCase("partner")) {
                //Utils.startActivity(mcontext, ActivityPartnerChat.class);

                Intent i = new Intent(mcontext, ActivityPartnerChat.class);
                i.putExtra("role", dmap.get("role").toString());
                i.putExtra("rid", dmap.get("reciever_id").toString());
                mcontext.startActivity(i);
            }
        } else {
            Utils.toastTxt("Your Partner has not registered.", mcontext);
        }
    }
}

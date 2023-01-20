package com.lncdriver.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.lncdriver.R;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/16/2018.
 */

public class ViewPartnerRideDetails extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ViewPartnerRideDetails";
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    public static ViewPartnerRideDetails Instance;
    public static Context mcontext;
    HashMap<String, Object> map, rMap;


    TextView dnameUSER;
    TextView dnumberUSER;
    ImageView driverimageUSER;

    TextView dname;
    TextView dnumber;
    ImageView driverimage;
    ImageView call;
    ImageView msg;
    TextView ischatmsg;
    TextView rideAddress;
    Button btnCancelRide;


    ImageView imageViewUser, imageViewDestination;

    @BindView(R.id.card_user)
    CardView user_root;

    private static final int RC_CALL_PERM = 100;


    Button gotouser, btnGoToDestination;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpartner_future_ride);
        ButterKnife.bind(this);

        driverimageUSER = findViewById(R.id.userimage_new);
        dnameUSER = findViewById(R.id.uname_new);
        dnumberUSER = findViewById(R.id.unumber_new);


        imageViewUser = findViewById(R.id.unavigation);
        imageViewDestination = findViewById(R.id.dnavigation);


        gotouser = findViewById(R.id.gotouser);
        btnGoToDestination = findViewById(R.id.btnGoToDestination);


        driverimage = findViewById(R.id.userimage);
        dname = findViewById(R.id.uname);
        dnumber = findViewById(R.id.unumber);

        rideAddress = findViewById(R.id.rideaddress);
        ischatmsg = findViewById(R.id.newchatmsg);
        call = findViewById(R.id.call);
        msg = findViewById(R.id.msg);
        btnCancelRide = findViewById(R.id.btn_cancel);

        mcontext = this;
        Instance = this;

        title.setText("Driver Details");

        if (getIntent() != null) {
            rMap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
            //Log.e("TAGGGG", "rmap " + rMap.toString());
        }

        back.setOnClickListener(this);
        msg.setOnClickListener(this);
        call.setOnClickListener(this);
        btnCancelRide.setOnClickListener(this);



        gotouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String add = "";
                    if (rMap.containsKey("pickup_lat") && !rMap.get("pickup_lat").
                            toString().equalsIgnoreCase("")) {
                        add = "google.navigation:q=" + rMap.get("pickup_lat").toString()
                                + "," + rMap.get("pickup_long").toString() + "&mode=d";

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



        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String add = "";
                    if (rMap.containsKey("pickup_lat") && !rMap.get("pickup_lat").
                            toString().equalsIgnoreCase("")) {
                        add = "google.navigation:q=" + rMap.get("pickup_lat").toString()
                                + "," + rMap.get("pickup_long").toString() + "&mode=d";

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






        btnGoToDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String add = "";
                    if (rMap.containsKey("d_lat") && !rMap.get("d_lat").toString().equalsIgnoreCase("")) {
                        if (rMap.containsKey("pickup_lat") && !rMap.get("pickup_lat").
                                toString().equalsIgnoreCase("")) {

                            Log.e(TAG , "WWWWCC "+rMap.get("pickup_lat").toString()+","+rMap.get("pickup_long").toString()
                                    +"&daddr="+rMap.get("d_lat").toString() + "," + rMap.get("d_long").toString());


                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?saddr="+rMap.get("pickup_lat").toString()+","+rMap.get("pickup_long").toString()+"&daddr="+rMap.get("d_lat").toString()+","+rMap.get("d_long").toString()));
                            startActivity(intent);


//                            String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+rMap.get("pickup_lat").toString()+","+rMap.get("pickup_long").toString()
//                                    +"&daddr="+rMap.get("d_lat").toString() + "," + rMap.get("d_long").toString();
//                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//                            mcontext.startActivity(intent);
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


        imageViewDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String add = "";
                    if (rMap.containsKey("d_lat") && !rMap.get("d_lat").toString().equalsIgnoreCase("")) {
                        if (rMap.containsKey("pickup_lat") && !rMap.get("pickup_lat").
                                toString().equalsIgnoreCase("")) {

                            Log.e(TAG , "WWWWCC "+rMap.get("pickup_lat").toString()+","+rMap.get("pickup_long").toString()
                                    +"&daddr="+rMap.get("d_lat").toString() + "," + rMap.get("d_long").toString());


                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?saddr="+rMap.get("pickup_lat").toString()+","+rMap.get("pickup_long").toString()+"&daddr="+rMap.get("d_lat").toString()+","+rMap.get("d_long").toString()));
                            startActivity(intent);


//                            String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+rMap.get("pickup_lat").toString()+","+rMap.get("pickup_long").toString()
//                                    +"&daddr="+rMap.get("d_lat").toString() + "," + rMap.get("d_long").toString();
//                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//                            mcontext.startActivity(intent);
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


        getRideUserDetailsRequest(ViewPartnerRideDetails.this, rMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.call:
                showMessage("Do You Want To Call ?", map);
                break;
            case R.id.msg:
                ischatmsg.setVisibility(View.GONE);
                HashMap<String, Object> dmap1 = new HashMap<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (!value.toString().equalsIgnoreCase("null")) {
                        dmap1.put(key, value);
                    }
                }

                if (!dmap1.containsKey("id")) {
                    Utils.toastTxt("Driver not existed", mcontext);
                } else {
                    Intent j = new Intent(mcontext, ActivityPartnerChat.class);
                    j.putExtra("map", (Serializable) dmap1);
                    startActivity(j);
                }
                break;

            case R.id.btn_cancel:
                OnlineRequest.cancelFutureRideByPartner(this, rMap);
                break;
        }
    }

    public void showMessage(String dlgText, final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog6, null);
        final AlertDialog dialog = new AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        //final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                getCall(map);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, RC_CALL_PERM);
                }
                //ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},RC_SMS_PERM);
            }
        } else {
            getCall(map);
        }
    }

    public void getCall(HashMap<String, Object> map) {
        if (map.containsKey("mobile") && !map.get("mobile").toString().equalsIgnoreCase("")) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", map.get("mobile").toString(), null)));
        }
    }

    public void getRideUserDetailsRequest(Context mcontext, HashMap<String, Object> rid) {
        map = new HashMap<>();

        if (rMap.containsKey("id"))
            map.put("ride_id", rMap.get("id").toString());

        OnlineRequest.getPartnerRideUserDetailsRequest(mcontext, map);
    }

    public void getFutureRideUserDetails(List<HashMap<String, Object>> list) {
        map = new HashMap<>();

        if (list != null) {
            user_root.setVisibility(View.VISIBLE);
            map = list.get(0);



            if (map.containsKey("user_profile_pic")) {
                if (!map.get("user_profile_pic").toString().equalsIgnoreCase(""))
                    Picasso.with(mcontext).load("http://latenightchauffeurs.com/lnc-administrator/uploads/" + map.get("user_profile_pic").toString()).placeholder(R.drawable.appicon).into(driverimageUSER);
                // userpic.setImageBitmap(getBitmapFromURL(image));
            }

            StringBuilder sb1 = new StringBuilder();
            if (map.containsKey("user_first_name")) {
                if (!map.get("user_first_name").toString().equalsIgnoreCase(""))
                    sb1.append(map.get("user_first_name").toString());
            }

            if (map.containsKey("user_last_name")) {
                if (!map.get("user_last_name").toString().equalsIgnoreCase(""))
                    sb1.append(" ");
                sb1.append(map.get("user_last_name").toString());
                dnameUSER.setText(sb1);
            }

            if (map.containsKey("user_mobile")) {
                if (!map.get("user_mobile").toString().equalsIgnoreCase(""))
                    dnumberUSER.setText(map.get("user_mobile").toString());
            }







            if (map.containsKey("profile_pic")) {
                if (!map.get("profile_pic").toString().equalsIgnoreCase(""))
                    Picasso.with(mcontext).load("http://latenightchauffeurs.com/lnc-administrator/uploads/" + map.get("profile_pic").toString()).placeholder(R.drawable.appicon).into(driverimage);
                // userpic.setImageBitmap(getBitmapFromURL(image));
            }

            StringBuilder sb = new StringBuilder();

            if (map.containsKey("first_name")) {
                if (!map.get("first_name").toString().equalsIgnoreCase(""))
                    sb.append(map.get("first_name").toString());
            }

            if (map.containsKey("last_name")) {
                if (!map.get("last_name").toString().equalsIgnoreCase(""))
                    sb.append(" ");
                sb.append(map.get("last_name").toString());
                dname.setText(sb);
            }

            if (map.containsKey("mobile")) {
                if (!map.get("mobile").toString().equalsIgnoreCase(""))
                    dnumber.setText(map.get("mobile").toString());
            }

            String s = "", e = "";
            if (map.containsKey("pickup_address")) {
                if (!map.get("pickup_address").toString().equalsIgnoreCase(""))
                    s = getColoredSpanned(map.get("pickup_address").toString(), "#F69625");
            }

            if (map.containsKey("drop_address")) {
                if (!map.get("drop_address").toString().equalsIgnoreCase(""))
                    e = getColoredSpanned(map.get("drop_address").toString(), "#F69625");
                rideAddress.setText(Html.fromHtml("Ride from :<br />" + s + "<br />  to  <br />" + e));
            }
        } else {
            user_root.setVisibility(View.VISIBLE);
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }







    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG , "aaaaaisidAAA");

            if (intent != null) {
                Log.e(TAG , "aaaaaisidBBB");
                String isid = intent.getStringExtra("isid");

                if (isid.equalsIgnoreCase("9")) {
                    String status = intent.getStringExtra("status");

                    HashMap<String, Object> data = new HashMap<>();
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("future complete")) {
                        Log.e(TAG , "aaaaaisidCCC");

                        Intent i = new Intent(ViewPartnerRideDetails.this, Navigation.class);
                        //i.putExtra("keyPosition" , 1);
                        startActivity(i);
                        finishAffinity();
                        finish();

                    }

                }
            }
        }
    };





    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
    }



    @Override
    public void onDestroy() {
        unregisterReceiver(mHandleMessageReceiver);
        Utils.logError("101", "destroy");
        super.onDestroy();


    }



}

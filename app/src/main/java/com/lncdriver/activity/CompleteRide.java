package com.lncdriver.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AnaadIT on 3/16/2017.
 */

public class CompleteRide extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.ridecost_basic)
    TextView ridecost_basic;


    @BindView(R.id.tv_new_admin_fare)
    TextView tvAdminFare;

    @BindView(R.id.waiting_charges)
    TextView waiting_charges;

    @BindView(R.id.total_charges)
    TextView total_charges;

    @BindView(R.id.tv_total_waiting_time_charges)
    TextView tvTotalWaitingTimeCharges;

    @BindView(R.id.tv_total_unplanned)
    TextView tvUnplannedStop;



    @BindView(R.id.tv_new_ride_fare)
    TextView tvRideFare;

    @BindView(R.id.tv_earnings)
    TextView tvRideEarnings;

    @BindView(R.id.complete)
    Button complete;

    public static CompleteRide Instance;
    HashMap<String, Object> map, map_prices;
    public int id = 0;

    public static String TAG = CompleteRide.class.getName();

    SavePref pref = new SavePref();
    private String timeSet = "", strUnPlannedSet = "", rideFare = "", earnings = "";
    private long posTime = 0, totalWaitinTime = 0, totalUnplannedStops = 0, tWaitinCharges = 0;
    private double finalTotalRide = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ridecomplete);
        ButterKnife.bind(this);

        Instance = this;
        pref.SavePref(Instance);

        back.setOnClickListener(this);
        complete.setOnClickListener(this);

        back.setVisibility(View.VISIBLE);
        title.setText("Ride Preview");
      //  title.setText(Html.fromHtml("<font color='#ffffff'> Ride&nbsp; </font><font color='#8bbc50'> Preview</font>"));

        if (getIntent().hasExtra("map")) {
            rideFare = getIntent().getExtras().getString(Utils.RIDE_FARE);
            earnings = getIntent().getExtras().getString(Utils.EARNINGS);
            tvRideFare.setText("$"+rideFare + "");
            tvRideEarnings.setText("$"+earnings + "");
            strUnPlannedSet = getIntent().getExtras().getString(Utils.TOTAL_UNPLANNED_TIME);
            posTime = getIntent().getLongExtra(Utils.POSITION_TIME, 0);
            timeSet = getIntent().getExtras().getString(Utils.TOTAL_WAITING_TIME);
            if (!strUnPlannedSet.trim().equals("")) {
                totalUnplannedStops = Long.parseLong(strUnPlannedSet);
                totalUnplannedStops = totalUnplannedStops * 10;
            }

            if (!timeSet.trim().equals("")) {
                //posTime++;
                tWaitinCharges = posTime * 10;
                totalWaitinTime = posTime * 15;
            }

            map_prices = new HashMap<>();
            map_prices = (HashMap<String, Object>) getIntent().getSerializableExtra("map");

            if (map_prices != null) {
                if (map_prices.size() > 0) {
                    loadChrgesData(map_prices);
                }
            }
        }


//        if(!pref.getUserId().equalsIgnoreCase("")){
//            HashMap<String, Object> smap = new HashMap<>();
//            smap.put("driverid", pref.getUserId());
//            OnlineRequest.waitingChargesAmount(Instance, map);
//        }

    }

    public void loadChrgesData(HashMap<String, Object> mMap) {
        if (mMap != null) {
            if (mMap.containsKey("ride_cost")) {
                if (!mMap.get("ride_cost").toString().equalsIgnoreCase("") &&
                        !mMap.get("ride_cost").toString().equalsIgnoreCase("null")) {
                    ridecost_basic.setText("$"+mMap.get("ride_cost").toString() + "");
                }
                if (mMap.containsKey("waiting_charge")) {
                    if (!mMap.get("waiting_charge").toString().equalsIgnoreCase("") &&
                            !mMap.get("waiting_charge").toString().equalsIgnoreCase("null")) {
                        waiting_charges.setText("$"+mMap.get("waiting_charge").toString() + "");
                    }
                }







//                if (tWaitinCharges > 0) {
//                    tvTotalWaitingTimeCharges.setText(tWaitinCharges + " $");
//                } else {
//                    tvTotalWaitingTimeCharges.setText(tWaitinCharges + " $");
//                }

//                if (totalUnplannedStops > 0) {
//                    tvUnplannedStop.setText(totalUnplannedStops + " $");
//                } else {
//                    tvUnplannedStop.setText(totalUnplannedStops + " $");
//                }

                Bundle bundle = getIntent().getExtras();

                String plannedCount = bundle.getString("position_unplanned_count");
                if (plannedCount != null && !plannedCount.equalsIgnoreCase(""))
                    tvUnplannedStop.setText(plannedCount + "");
                else {
                    tvUnplannedStop.setText("0");
                }

                String waitNTime = bundle.getString("total_waiting_time");
                if (waitNTime != null && !waitNTime.equalsIgnoreCase(""))
                    tvTotalWaitingTimeCharges.setText(waitNTime + " Minutes");
                else
                    tvTotalWaitingTimeCharges.setText("0 Minutes");

                if (mMap.containsKey("total_cost")) {
                    if (!mMap.get("total_cost").toString().equalsIgnoreCase("") &&
                            !mMap.get("total_cost").toString().equalsIgnoreCase("null")) {
                        finalTotalRide = Double.parseDouble(mMap.get("total_cost").toString());
                        finalTotalRide = finalTotalRide + totalUnplannedStops + tWaitinCharges;
                        total_charges.setText("$"+finalTotalRide + "");
                    }
                }



                String admin_fee = bundle.getString("admin_fee");

                Log.e(TAG , "WWWWWWWWWW "+admin_fee);



                if (admin_fee != null && !admin_fee.equalsIgnoreCase(""))
                    tvAdminFare.setText("$"+admin_fee + "");
                else {
                    tvAdminFare.setText("$0");
                }



            }
        }
    }

    public void closeActivity() {
        Navigation.nid = 3;
        Utils.startActivity(CompleteRide.this, Navigation.class);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.complete:
                if (map_prices != null) {
                    if (map_prices.size() > 0) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(Utils.FINAL_W_TIME, totalWaitinTime + "");
                        returnIntent.putExtra(Utils.FINAL_UNPLANNED, strUnPlannedSet + "");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
                break;
        }
    }
}



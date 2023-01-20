package com.lncdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.activity.ViewPaymentDetailsInfo;
import com.lncdriver.model.ModelItem;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class PaymentListHistoryAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PaymentListHistoryAdapter";
    private final List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private final int itemLayout;
    public static int adapterMode;
    public Context mcontext;
    String driverId;

    public PaymentListHistoryAdapter(Context context, List<ModelItem> students, int layout, int mode) {
        adapterList = students;
        itemLayout = layout;
        adapterMode = mode;
        mcontext = context;
        //Context context, List<ModelItem> students, RecyclerView recyclerView, int layout, int mode
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
            ModelItem singleTripData = adapterList.get(position);
            HashMap<String, Object> detailMap = singleTripData.getMapMain();
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            driverId = pref1.getUserId();
            try {
                //((InboxViewHolder) holder).rootItemView.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).pInfo.setTag(position);

                if (detailMap.containsKey("otherdate") && detailMap.containsKey("time")) {
                    String otherdate = detailMap.get("otherdate").toString();
                    String time = detailMap.get("time").toString();

                    ((InboxViewHolder) holder).tvRideDate.setText(otherdate+" | "+time);
                }


                if (detailMap.containsKey("booking_id")) {
                    ((InboxViewHolder) holder).tvRide_id.setText(detailMap.get("booking_id").toString());
                }


            if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
                ((InboxViewHolder) holder).textViewText.setText("Follower Pay Summary");
            }else{
                ((InboxViewHolder) holder).textViewText.setText("Driver Pay Summary");
            }



            double base = 0;

            try{
                base = Double.parseDouble(detailMap.get("base_price").toString());
            }catch(Exception e){

            }


            if(base > 0){
                ((InboxViewHolder) holder).tvRideFare.setText("$"+detailMap.get("base_price").toString() + "");
            }else{
                ((InboxViewHolder) holder).tvRideFare.setText("$"+detailMap.get("ride_amt").toString() + "");
             }





                if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
                    if(base > 0){
                        ((InboxViewHolder) holder).tvDriverAmt.setText("$" + detailMap.get("partner_base_share").toString() + "");
                    }else{
                        ((InboxViewHolder) holder).tvDriverAmt.setText("$" + detailMap.get("partners_amount").toString() + "");
                    }
                }else{
                    if(base > 0){
                        ((InboxViewHolder) holder).tvDriverAmt.setText("$" + detailMap.get("driver_base_share").toString() + "");
                    }else{
                        ((InboxViewHolder) holder).tvDriverAmt.setText("$" + detailMap.get("drivers_amount").toString() + "");
                    }
                }


                if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
                    if(base > 0){
                        ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$" + detailMap.get("partner_unplanned_stop_amount").toString() + "");
                    }else{
                      //  ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$" + detailMap.get("unplaned_stop_amt").toString() + "");
                        ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$0");
                    }
                }else{
                    if(base > 0){
                        ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$" + detailMap.get("driver_unplanned_stop_amount").toString() + "");
                    }else{
                       // ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$" + detailMap.get("unplaned_stop_amt").toString() + "");
                        ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$0");
                    }
                }




                if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
                    if(base > 0){
                        ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$" + detailMap.get("partner_planned_stop_amount").toString() + "");
                    }else{
                       // ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$" + detailMap.get("planed_stop_amt").toString() + "");
                        ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$0");
                    }
                }else{
                    if(base > 0){
                        ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$" + detailMap.get("driver_planned_stop_amount").toString() + "");
                    }else{
                       // ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$" + detailMap.get("planed_stop_amt").toString() + "");
                        ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$0");
                    }
                }

                if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
                    if(base > 0){
                        ((InboxViewHolder) holder).tvWaitingCharges.setText("$"+detailMap.get("partner_unplanned_waiting_time").toString() + "");
                    }else{
                       // ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$" + detailMap.get("unplaned_waiting_amt").toString() + "");
                        ((InboxViewHolder) holder).tvWaitingCharges.setText("$0");
                    }
                }else{
                    if(base > 0){
                        ((InboxViewHolder) holder).tvWaitingCharges.setText("$"+detailMap.get("driver_unplanned_waiting_time").toString() + "");
                    }else{
                       // ((InboxViewHolder) holder).tvWaitingCharges.setText("$" + detailMap.get("unplaned_waiting_amt").toString() + "");
                        ((InboxViewHolder) holder).tvWaitingCharges.setText("$0");
                    }
                }





                if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
                    ((InboxViewHolder) holder).tvDriverTipAmt.setText("$"+detailMap.get("drivers_tip").toString() + "");
                }else{
                    ((InboxViewHolder) holder).tvDriverTipAmt.setText("$"+detailMap.get("partners_tip").toString() + "");
                }

//                if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvDriverTipAmt.setText("$"+detailMap.get("drivers_tip").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvDriverTipAmt.setText("$0");
//                }




                if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
                    if(base > 0){
                        double amountDriver = 0;
                        double amountTip = 0;
                        try{
                            amountDriver = Double.parseDouble(detailMap.get("partner_share").toString());
                        }catch (Exception e){

                        }

                        try {
                            amountTip = Double.parseDouble(detailMap.get("partners_tip").toString());
                        } catch (Exception e) {

                        }
                        ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
                    }else{
                        double amountDriver = 0;
                        double amountTip = 0;
                        try{
                            amountDriver = Double.parseDouble(detailMap.get("partners_amount").toString());
                        }catch (Exception e){

                        }

                        try {
                            amountTip = Double.parseDouble(detailMap.get("partners_tip").toString());
                        } catch (Exception e) {

                        }
                        ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
                    }
                }else{
                    if(base > 0){
                        double amountDriver = 0;
                        double amountTip = 0;
                        try{
                            amountDriver = Double.parseDouble(detailMap.get("driver_share").toString());
                        }catch (Exception e){

                        }

                        try {
                                amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
                            } catch (Exception e) {

                            }
                        ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
                    }else{
                        double amountDriver = 0;
                        double amountTip = 0;
                        try{
                            amountDriver = Double.parseDouble(detailMap.get("drivers_amount").toString());
                        }catch (Exception e){

                        }

                        try {
                            amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
                        } catch (Exception e) {

                        }
                        ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
                    }
                }



//                if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
//                    if(detailMap.containsKey("partner_base_share") && !detailMap.get("partner_base_share").toString()
//                            .equalsIgnoreCase("") && !detailMap.get("partner_base_share").toString()
//                            .equalsIgnoreCase("null")) {
//                        double amountDriver = 0;
//                        double amountTip = 0;
//                        try{
//                            amountDriver = Double.parseDouble(detailMap.get("partner_base_share").toString());
//                        }catch (Exception e){
//
//                        }
//                        if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                                .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                                .equalsIgnoreCase("null")) {
//                            try {
//                                amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
//                            } catch (Exception e) {
//
//                            }
//                        }
//
//                        ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
//                    }else{
//                        if (detailMap.containsKey("drivers_amount") && !detailMap.get("drivers_amount").toString()
//                                .equalsIgnoreCase("") && !detailMap.get("drivers_amount").toString()
//                                .equalsIgnoreCase("null")) {
//                            double amountDriver = 0;
//                            double amountTip = 0;
//                            try{
//                                amountDriver = Double.parseDouble(detailMap.get("partners_amount").toString());
//                            }catch (Exception e){
//
//                            }
//                            if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("null")) {
//                                try {
//                                    amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
//                                } catch (Exception e) {
//
//                                }
//                            }
//                            ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
//                        }else{
//                            ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$0");
//                        }
//                    }
//                }else{
//                    if(detailMap.containsKey("driver_base_share") && !detailMap.get("driver_base_share").toString()
//                            .equalsIgnoreCase("") && !detailMap.get("driver_base_share").toString()
//                            .equalsIgnoreCase("null")) {
//                        double amountDriver = 0;
//                            double amountTip = 0;
//                            try{
//                                amountDriver = Double.parseDouble(detailMap.get("driver_base_share").toString());
//                            }catch (Exception e){
//
//                            }
//                            if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("null")) {
//                                try {
//                                    amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
//                                } catch (Exception e) {
//
//                                }
//                            }
//
//                        ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
//                    }else{
//                        if (detailMap.containsKey("drivers_amount") && !detailMap.get("drivers_amount").toString()
//                                .equalsIgnoreCase("") && !detailMap.get("drivers_amount").toString()
//                                .equalsIgnoreCase("null")) {
//                            double amountDriver = 0;
//                            double amountTip = 0;
//                            try{
//                                amountDriver = Double.parseDouble(detailMap.get("drivers_amount").toString());
//                            }catch (Exception e){
//
//                            }
//                            if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("null")) {
//                                try {
//                                    amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
//                                } catch (Exception e) {
//
//                                }
//                            }
//
//                            ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$" + (amountDriver+amountTip) + "");
//                        }else{
//                            ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$0");
//                        }
//                    }
//                }
//





//
//                if (detailMap.containsKey("actual_amount") && !detailMap.get("actual_amount").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("actual_amount").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvRideFare.setText("$"+detailMap.get("actual_amount").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvRideFare.setText("$0");
//                }
//
//
//                if (detailMap.containsKey("admin_fee") && !detailMap.get("admin_fee").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("admin_fee").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvAdminFee.setText("$"+detailMap.get("admin_fee").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvAdminFee.setText("$0");
//                }
//
//
//                if (detailMap.containsKey("unplaned_waiting_time") && !detailMap.get("unplaned_waiting_time").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("unplaned_waiting_time").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvWaitingTime.setText(detailMap.get("unplaned_waiting_time").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvWaitingTime.setText("0");
//                }
//
//                if (detailMap.containsKey("unplaned_waiting_amt") && !detailMap.get("unplaned_waiting_amt").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("unplaned_waiting_amt").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvWaitingCharges.setText("$"+detailMap.get("unplaned_waiting_amt").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvWaitingCharges.setText("$0");
//                }
//
//
//                if (detailMap.containsKey("planed_stop_count") && !detailMap.get("planed_stop_count").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("planed_stop_count").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvPlannedStops.setText(detailMap.get("planed_stop_count").toString() + " ");
//                } else {
//                    ((InboxViewHolder) holder).tvPlannedStops.setText("0");
//                }
//
//
//                if (detailMap.containsKey("planed_stop_amt") && !detailMap.get("planed_stop_amt").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("planed_stop_amt").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$"+detailMap.get("planed_stop_amt").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvPlannedStopsAmt.setText("$0");
//                }
//
//
//                if (detailMap.containsKey("unplaned_stop_count") && !detailMap.get("unplaned_stop_count").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("unplaned_stop_count").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvUnplannedStops.setText(detailMap.get("unplaned_stop_count").toString() + " ");
//                } else {
//                    ((InboxViewHolder) holder).tvUnplannedStops.setText("0");
//                }
//
//
//                if (detailMap.containsKey("unplaned_stop_amt") && !detailMap.get("unplaned_stop_amt").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("unplaned_stop_amt").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$"+detailMap.get("unplaned_stop_amt").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvUnplannedStopsAmount.setText("$0");
//                }
//
//
//                if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvDriverTipAmt.setText("$"+detailMap.get("drivers_tip").toString() + "");
//                } else {
//                    ((InboxViewHolder) holder).tvDriverTipAmt.setText("$0");
//                }
//
//
//                if (detailMap.containsKey("driver_id_for_future_ride") && !detailMap.get("driver_id_for_future_ride").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("driver_id_for_future_ride").toString()
//                        .equalsIgnoreCase("null")) {
//
//                    if (detailMap.get("driver_id_for_future_ride").toString().equalsIgnoreCase(driverId)) {
//                        ((InboxViewHolder) holder).tv_driver_act.setText("Driver Earning :");
//
//                        if (detailMap.containsKey("drivers_amount") && !detailMap.get("drivers_amount").toString()
//                                .equalsIgnoreCase("") && !detailMap.get("drivers_amount").toString()
//                                .equalsIgnoreCase("null")) {
//                            ((InboxViewHolder) holder).tvDriverAmt.setText("$"+detailMap.get("drivers_amount").toString() + "");
//
//                            double amountDriver = 0;
//                            double amountTip = 0;
//                            try{
//                                amountDriver = Double.parseDouble(detailMap.get("drivers_amount").toString());
//                            }catch (Exception e){
//
//                            }
//                            if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("null")) {
//                                try{
//                                    amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
//                                }catch (Exception e){
//
//                                }
//
//                               // Log.e(TAG , amountDriver+" CCCCCC "+amountTip);
//
//                                ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$"+(amountDriver+amountTip)+"");
//
//                            } else {
//                                ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$0");
//                            }
//
//
//                        } else {
//                            ((InboxViewHolder) holder).tvDriverAmt.setText("$0");
//                        }
//
//                    } else if (detailMap.get("future_partner_id").toString().equalsIgnoreCase(driverId)) {
//                        ((InboxViewHolder) holder).tv_driver_act.setText("Partner Earning :");
//
//                        if (detailMap.containsKey("partners_amount") && !detailMap.get("partners_amount").toString()
//                                .equalsIgnoreCase("") && !detailMap.get("partners_amount").toString()
//                                .equalsIgnoreCase("null")) {
//                            ((InboxViewHolder) holder).tvDriverAmt.setText("$"+detailMap.get("partners_amount").toString() + "");
//
//                            double amountPartner = 0;
//                            double amountTip = 0;
//                            try{
//                                amountPartner = Double.parseDouble(detailMap.get("partners_amount").toString());
//                            }catch (Exception e){
//
//                            }
//                            if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("") && !detailMap.get("drivers_tip").toString()
//                                    .equalsIgnoreCase("null")) {
//                                try{
//                                    amountTip = Double.parseDouble(detailMap.get("drivers_tip").toString());
//                                }catch (Exception e){
//
//                                }
//
//                                ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$"+(amountPartner+amountTip)+"");
//
//                            } else {
//                                ((InboxViewHolder) holder).tvPartnerTipAmt.setText("$0");
//                            }
//
//                        } else {
//                            ((InboxViewHolder) holder).tvDriverAmt.setText("$0");
//                        }
//                    }
//                }
//















//
//
                ((InboxViewHolder) holder).tvCustomerName.setText(detailMap.get("first_name").toString() + " "
                        + detailMap.get("last_name").toString());

//                if ((detailMap.containsKey("ride_amt") && !detailMap.get("ride_amt").toString()
//                        .equalsIgnoreCase("")) && !detailMap.get("ride_amt").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvActualRideCost.setText(detailMap.get("ride_amt").toString() + " ");
//                } else {
//                    ((InboxViewHolder) holder).tvActualRideCost.setText("0 ");
//                }
//
//                if (detailMap.containsKey("unplaned_waiting_amt") && !detailMap.get("unplaned_waiting_amt").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("unplaned_waiting_amt").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvUnplannedWaitinCharges.setText
//                            (detailMap.get("unplaned_waiting_amt").toString() + " ");
//                } else {
//                    ((InboxViewHolder) holder).tvUnplannedWaitinCharges.setText("0 ");
//                }
//
//
//                if (detailMap.containsKey("total_tip_amount") && !detailMap.get("total_tip_amount").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("total_tip_amount").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvTipAmt.setText(detailMap.get("total_tip_amount").toString() + " ");
//                } else {
//                    ((InboxViewHolder) holder).tvTipAmt.setText("0 ");
//                }
//
//
//
//
//
//                if (detailMap.containsKey("city_charges") && !detailMap.get("city_charges").toString()
//                        .equalsIgnoreCase("") && !detailMap.get("city_charges").toString()
//                        .equalsIgnoreCase("null")) {
//                    ((InboxViewHolder) holder).tvCityCharges.setText(detailMap.get("city_charges").toString() + " ");
//                } else {
//                    ((InboxViewHolder) holder).tvCityCharges.setText("0 ");
//                }
//
//
//                ((InboxViewHolder) holder).tvTotalAmt.setText(detailMap.get("amount").toString() + " ");







                ((InboxViewHolder) holder).pInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) v.getTag();
                        ModelItem singleTripData = adapterList.get(pos);
                        HashMap<String, Object> detailMap = singleTripData.getMapMain();
                        Global.mapData = detailMap;

                        if (Global.mapData.size() > 0) {
                            try {
                                boolean isMyId = detailMap.get("driver_id_for_future_ride").toString().equalsIgnoreCase(driverId);
                                Utils.logError("TestHistory", detailMap.toString() + " \n " + isMyId);

                                JSONArray jsonArray = null;
                                //if (isMyId) {
                                jsonArray = new JSONArray(detailMap.get("partners_details").toString());
                                /*} else {
                                    jsonArray = new JSONArray(detailMap.get("driver_details").toString());
                                }*/
                                JSONObject jsonObject = null;
                                if (jsonArray != null)
                                    jsonObject = jsonArray.optJSONObject(0);
                                if (Global.mapData.size() > 0) {

                                    Intent i = new Intent(mcontext, ViewPaymentDetailsInfo.class);
                                    i.putExtra(Utils.CUSTOMER_NAME, detailMap.get("first_name").toString() + " "
                                            + detailMap.get("last_name").toString());
                                    i.putExtra(Utils.PICK_UP, detailMap.get("pickup_address").toString());
                                    i.putExtra(Utils.DROP_OFF, detailMap.get("drop_address").toString());
                                    i.putExtra(Utils.TRANSACTION_ID, detailMap.get("transaction_id").toString());
                                    i.putExtra(Utils.IS_MY_ID, isMyId);

                                    if (jsonObject != null) {
                                        i.putExtra(Utils.PARTNER_NAME, jsonObject.opt(Utils.PARTNER_NAME).toString());
                                        i.putExtra(Utils.PARTNER_EMAIL, jsonObject.opt(Utils.PARTNER_EMAIL).toString());
                                        i.putExtra(Utils.PARTNER_PHONE, jsonObject.opt(Utils.PARTNER_PHONE).toString());
                                    }
                                    mcontext.startActivity(i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            //Intent i = new Intent(mcontext, ViewPaymentDetailsInfo.class);
                            // i.putExtra("DetailMap", detailMap);
                            //i.putExtra(Utils.PARTNER_EMAIL, jsonObject.opt(Utils.PARTNER_EMAIL).toString());
                            //i.putExtra(Utils.PARTNER_PHONE, jsonObject.opt(Utils.PARTNER_PHONE).toString());
                            //mcontext.startActivity(i);
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            //((InboxViewHolder) holder).modelItem = singleTripData;
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //Notification
        //public ModelItem modelItem;

        public LinearLayout llDateTime, llCustomerName, llRideCost, llWaitingAmt,
                llUnplannedWaitingAmt, llUnplannedStops, llPlannedStops, llPromoDiscount,
                llCityCharges, llTipAmt, llDriverTipAmt, llPartnerTipAmt, llDriverType;

        public TextView tvRideDate, tvCustomerName, tvRide_id , tvTotalAmt,
                tvActualRideCost, tvWaitingCharges, tvUnplannedStopsAmount,
                tvCityCharges, tvTipAmt, tvDriverTipAmt,
                tvDriverAmt, tvDriverType, tv_driver_act, tvRideFare , tvWaitingTime, tvPlannedStopsAmt, tvPartnerTipAmt, textViewText ;

        public ImageView pInfo;

        public InboxViewHolder(View v) {
            super(v);
            try {
                tvRideDate = v.findViewById(R.id.date);
                tvCustomerName = v.findViewById(R.id.tv_customer_name);
                tvRide_id = v.findViewById(R.id.tv_ride_id);
                tvActualRideCost = v.findViewById(R.id.tv_ride_cost);
                tvUnplannedStopsAmount = v.findViewById(R.id.tv_unplanned_stops_amt);

                tvTipAmt = v.findViewById(R.id.tv_tip_amt);
                tvWaitingCharges = v.findViewById(R.id.tv_waiting_charges);
                tvCityCharges = v.findViewById(R.id.tv_city_charges);//
                tvDriverTipAmt = v.findViewById(R.id.tv_driver_tip_amt);//
                tvDriverAmt = v.findViewById(R.id.tv_driver_amt);
                tvTotalAmt = v.findViewById(R.id.tv_total_amt);
                tv_driver_act = v.findViewById(R.id.tv_driver_act);
                tvRideFare = v.findViewById(R.id.tv_ride_fare);
                pInfo = v.findViewById(R.id.pinfo);

                tvWaitingTime = v.findViewById(R.id.tv_unplanned_waiting_charges);
                tvPlannedStopsAmt = v.findViewById(R.id.tv_planned_stops_amt);
                tvPartnerTipAmt = v.findViewById(R.id.tv_partner_tip_amt);

                textViewText = v.findViewById(R.id.tv_ride_summery);






            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
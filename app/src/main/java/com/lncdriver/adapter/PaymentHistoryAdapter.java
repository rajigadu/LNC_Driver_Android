package com.lncdriver.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.activity.ViewPaymentDetails;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class PaymentHistoryAdapter extends RecyclerView.Adapter {
    private final List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private final int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public PaymentHistoryAdapter(Context context, List<ModelItem> students, RecyclerView recyclerView, int layout, int mode) {
        adapterList = students;
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

            ModelItem singleTripData = adapterList.get(position);
            HashMap<String, Object> detailMap = singleTripData.getMapMain();
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            String did = pref1.getUserId();

            try {
                ((InboxViewHolder) holder).rootItemView.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).pInfo.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("first_name") && !detailMap.get("first_name").toString()
                        .equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).tvUserName.setVisibility(View.VISIBLE);
                    ((InboxViewHolder) holder).tvUserName.setText(detailMap.get("first_name").toString() + " "
                            + detailMap.get("last_name").toString());
                }
                if (detailMap.containsKey("payment_date") && !detailMap.get("payment_date").toString()
                        .equalsIgnoreCase("")) {
                    String[] d_array, dd_array;
                    String d_main = "";

                    d_array = detailMap.get("payment_date").toString().split(" ");
                    dd_array = d_array[0].split("-");
                    d_main = dd_array[1] + "-" + dd_array[2] + "-" + dd_array[0] + " " + d_array[1];
                    ((InboxViewHolder) holder).date.setText(d_main);
                }

                if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address")
                        .toString().equalsIgnoreCase("")) {

                    String s = "", e = "";

                    if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address")
                            .toString().equalsIgnoreCase(""))
                        s = getColoredSpanned(detailMap.get("pickup_address").toString(), "#800000");

                    if (detailMap.containsKey("drop_address") && !detailMap.get("drop_address")
                            .toString().equalsIgnoreCase(""))
                        e = getColoredSpanned(detailMap.get("drop_address").toString(), "#F69625");

                    ((InboxViewHolder) holder).ride.setText(Html.fromHtml(s + "<br />  to  <br />" + e));
                }

                if (detailMap.containsKey("transaction_id")) {
                    if (!detailMap.get("transaction_id").toString().equalsIgnoreCase(""))
                        ((InboxViewHolder) holder).payId.setText(detailMap.get("transaction_id").toString());
                    else {
                        ((InboxViewHolder) holder).payId.setText("PAYMENT FAILED");
                    }
                } else {
                    ((InboxViewHolder) holder).payId.setText("PAYMENT FAILED");
                }

                if (!detailMap.get("driver_id").toString().equalsIgnoreCase("")) {
                    String s = "", e = "";

                    if (detailMap.get("driver_id").toString().equalsIgnoreCase(did)) {
                        s = getColoredSpanned("Driver Amount : ", "#99000000");

                        if (detailMap.containsKey("drivers_amount") && !detailMap.get("drivers_amount")
                                .toString().equalsIgnoreCase(""))
                            e = getColoredSpanned(detailMap.get("drivers_amount")
                                    .toString() + "$", "#1F8B4D");

                        ((InboxViewHolder) holder).amount.setText(Html.fromHtml(s + e));
                    } else {
                        s = getColoredSpanned("Partner Amount : ", "#99000000");

                        if (detailMap.containsKey("partners_amount") && !detailMap.get("partners_amount")
                                .toString().equalsIgnoreCase(""))
                            e = getColoredSpanned(detailMap.get("partners_amount").toString() + "$", "#1F8B4D");

                        ((InboxViewHolder) holder).amount.setText(Html.fromHtml(s + e));
                    }
                }

                if (detailMap.containsKey("driver_id") && !detailMap.get("driver_id").toString()
                        .equalsIgnoreCase("")) {
                    String s = "", e = "";

                    if (detailMap.get("driver_id").toString().equalsIgnoreCase(did)) {
                        s = getColoredSpanned("Driver TipAmount : ", "#99000000");

                        if (detailMap.containsKey("drivers_tip") && !detailMap.get("drivers_tip")
                                .toString().equalsIgnoreCase("")) {
                            e = getColoredSpanned(detailMap.get("drivers_tip").toString() + "$", "#1F8B4D");
                        } else {
                            s = "";
                            ((InboxViewHolder) holder).tip.setVisibility(View.GONE);
                        }

                        ((InboxViewHolder) holder).tip.setText(Html.fromHtml(s + e));
                    } else {
                        s = getColoredSpanned("Partner TipAmount : ", "#99000000");

                        if (detailMap.containsKey("partners_tip") && !detailMap.get("partners_tip")
                                .toString().equalsIgnoreCase(""))
                            e = getColoredSpanned(detailMap.get("partners_tip").toString() + "$",
                                    "#1F8B4D");

                        ((InboxViewHolder) holder).tip.setText(Html.fromHtml(s + e));
                    }
                }

                if (detailMap.containsKey("driver_status") && !detailMap.get("driver_status").
                        toString().equalsIgnoreCase("")) {
                    if (detailMap.get("driver_status").toString().equalsIgnoreCase("self")) {
                        ((InboxViewHolder) holder).pType.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).pInfo.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).pType.setText(detailMap.get("driver_status").toString());
                    } else {
                        ((InboxViewHolder) holder).pType.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).pInfo.setVisibility(View.GONE);
                    }
                }

                ((InboxViewHolder) holder).pInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Global.mapData.size() > 0) {
                            HashMap<String, Object> dmap = new HashMap<>();
                            Global.mapData = (HashMap<String, Object>) v.getTag();

                            if (Global.mapData.size() > 0) {
                                for (Map.Entry<String, Object> entry : Global.mapData.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();

                                    if (!value.toString().equalsIgnoreCase("null")) {
                                        dmap.put(key, value);

                                    }
                                }
                            }
                            try {
                                JSONArray jsonArray = new JSONArray(dmap.get("partners_details").toString());
                                JSONObject jsonObject = jsonArray.optJSONObject(0);
                                if (Global.mapData.size() > 0) {
                                    Intent i = new Intent(mcontext, ViewPaymentDetails.class);
                                    i.putExtra(Utils.PARTNER_NAME, jsonObject.opt(Utils.PARTNER_NAME).toString());
                                    i.putExtra(Utils.PARTNER_EMAIL, jsonObject.opt(Utils.PARTNER_EMAIL).toString());
                                    i.putExtra(Utils.PARTNER_PHONE, jsonObject.opt(Utils.PARTNER_PHONE).toString());
                                    mcontext.startActivity(i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).modelItem = singleTripData;
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //Notification
        public ModelItem modelItem;
        public View gView;

        public TextView date, payId, ride, amount, tip, pType, tvUserName;
        public CardView rootItemView;
        public ImageView pInfo;

        public InboxViewHolder(View v) {
            super(v);
            try {
                date = v.findViewById(R.id.date);
                tvUserName = v.findViewById(R.id.tvUserName);
                payId = v.findViewById(R.id.pid);
                pType = v.findViewById(R.id.ptype);
                tip = v.findViewById(R.id.tip);
                pInfo = v.findViewById(R.id.pinfo);
                ride = v.findViewById(R.id.ride);
                amount = v.findViewById(R.id.amount);
                rootItemView = v.findViewById(R.id.rowitem_root);
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
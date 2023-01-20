package com.lncdriver.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.activity.StopLocationsList;
import com.lncdriver.activity.ViewRideMap;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class RideHistoryAdapter extends RecyclerView.Adapter {
    private final List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private final int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public RideHistoryAdapter(Context context, List<ModelItem> students, RecyclerView recyclerView, int layout, int mode) {
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
            ModelItem singleTripData = (ModelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            Utils.logError("paymentslistadapter 107", "" + detailMap);

            try {





                ((InboxViewHolder) holder).root.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).stops.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("first_name") && !detailMap.get("first_name").toString().equalsIgnoreCase("") ||
                        detailMap.containsKey("last_name") && !detailMap.get("last_name").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).textViewFullName.setText(""+detailMap.get("first_name").toString()+" "+detailMap.get("last_name").toString());
                }


                if (detailMap.containsKey("unplanned_stops") && !detailMap.get("unplanned_stops").toString().equalsIgnoreCase("0")) {
                    ((InboxViewHolder) holder).tvUnplannedStops.setText(detailMap.get("unplanned_stops").toString());
                } else {
                    ((InboxViewHolder) holder).tvUnplannedStops.setVisibility(View.GONE);
                    ((InboxViewHolder) holder).tvStops.setVisibility(View.GONE);
                }

                if (detailMap.containsKey("notes") && !detailMap.get("notes").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).tvRideNotes.setText(detailMap.get("notes").toString());
                } else {
                    ((InboxViewHolder) holder).tvRideNotes.setVisibility(View.GONE);
                    ((InboxViewHolder) holder).tvNotesTitle.setVisibility(View.GONE);
                }

                if (detailMap.containsKey("booking_type") && !detailMap.get("booking_type").toString().equalsIgnoreCase("")) {
                    if (detailMap.get("booking_type").toString().equalsIgnoreCase("1")) {
                        if (detailMap.containsKey("otherdate") && !detailMap.get("otherdate").toString().equalsIgnoreCase("")) {
                            String ss = "", ee = "";

                            ss = "Date :" + detailMap.get("otherdate").toString();
                            ee = "Time :" + detailMap.get("time").toString();

                            String s = ss
                                    + System.getProperty("line.separator")
                                    + ee
                                    + System.getProperty("line.separator");

                            ((InboxViewHolder) holder).date.setText(s);
                        }
                        //((InboxViewHolder) holder).tvRideDate.setText(Html.fromHtml(detailMap.get("created_at").toString()));
                    } else {
                        String ss = "", ee = "";

                        ss = "Date  :" + detailMap.get("otherdate").toString();
                        ee = "Time :" + detailMap.get("time").toString();

                        String s = ss
                                + System.getProperty("line.separator")
                                + ee
                                + System.getProperty("line.separator");

                        ((InboxViewHolder) holder).date.setText(s);
                    }
                }

                if (detailMap.containsKey("distance") && !detailMap.get("distance").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).distance.setText(String.format("%.2f", Double.valueOf(detailMap.get("distance").toString())) + " mi");
                }

                if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                    String s = "", e = "";

                    if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase(""))
                        s = getColoredSpanned(detailMap.get("pickup_address").toString(), "#800000");

                    if (detailMap.containsKey("drop_address") && !detailMap.get("drop_address").toString().equalsIgnoreCase(""))
                        e = getColoredSpanned(detailMap.get("drop_address").toString(), "#F69625");

                    ((InboxViewHolder) holder).ride.setText(Html.fromHtml(s + "<br />  to  <br />" + e));
                }

                ((InboxViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                        if (Global.mapData.size() > 0) {
                            Intent i = new Intent(mcontext, ViewRideMap.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });
                ((InboxViewHolder) holder).stops.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                        if (Global.mapData.size() > 0) {
                            //Utils.toastTxt("Still haven't implemented",mcontext);
                            Intent i = new Intent(mcontext, StopLocationsList.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });
            } catch (Exception e) {
                // Utils.logError(Tag+"180","Exception======================Exception======================Exception");
                e.printStackTrace();
            }

            ((InboxViewHolder) holder).hotel = singleTripData;
        } else {

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

        public TextView date, stops, ride, distance, tvStops, tvUnplannedStops, tvNotesTitle, tvRideNotes, textViewFullName;
        public RelativeLayout root;

        public InboxViewHolder(View v) {
            super(v);
            try {
                date = v.findViewById(R.id.date);
                stops = v.findViewById(R.id.stops);
                ride = v.findViewById(R.id.ride);
                tvStops = v.findViewById(R.id.tv_stops);
                tvUnplannedStops = v.findViewById(R.id.tv_unplanned_stops);
                distance = v.findViewById(R.id.distance);
                root = v.findViewById(R.id.rowitem_root);
                tvNotesTitle = v.findViewById(R.id.tv_notes_title);
                tvRideNotes = v.findViewById(R.id.tv_notes);
                textViewFullName = v.findViewById(R.id.full_name);
            } catch (Exception e) {
                // Utils.logError("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}
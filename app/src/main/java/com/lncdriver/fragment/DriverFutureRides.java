package com.lncdriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lncdriver.R;
import com.lncdriver.activity.ActivityDriverChat;
import com.lncdriver.adapter.DriverFutureRideAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class DriverFutureRides extends Fragment {
    static RecyclerView rv_loc;
    public static DriverFutureRides Instance;
    public static boolean isListEmpty = false;

    public static Context mcontext;
    public static DriverFutureRideAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    private static boolean isNotifClick = false;
    private static HashMap<String, Object> notifMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_currentrides, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        rv_loc = (RecyclerView) v.findViewById(R.id.rv_loc);

        mcontext = getActivity();
        Instance = this;

        requestsListMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);

        MyrequestsListRequest();

        if (isListEmpty) {
            MyrequestsListRequest();
        }

        isListEmpty = false;

        if (getArguments() != null) {
            map = (HashMap<String, Object>) getArguments().getSerializable("map");
            notifMap = (HashMap<String, Object>) getArguments().getSerializable(Utils.IS_NOTIF_MAP);
            isNotifClick = getArguments().getBoolean(Utils.IS_NOTIFICATION_CLICK);
            /*if (isNotifClick) {
                notifMap = (HashMap<String, Object>) getArguments().getSerializable(Utils.IS_NOTIF_MAP);
                Intent i = new Intent(mcontext, ViewCustomerFRideDetails.class);
                i.putExtra(Utils.IS_NOTIF_MAP, (Serializable) notifMap);
                i.putExtra(Utils.IS_NOTIFICATION_CLICK, isNotifClick);
                startActivity(i);
            }*/

        }


       /* for (int i=0;i<5;i++)
        {
            map=new HashMap<>();
            map.put("tvRideDate","tvRideDate "+String.valueOf(i));
            map.put("time","time "+String.valueOf(i));
            map.put("stops",String.valueOf(i));
            map.put("pick","pickup address"+String.valueOf(i));
            map.put("drop","drop off address "+String.valueOf(i));
            map.put("dist",String.valueOf(i*10));
            list.add(map);
        }
        if (list!=null&&list.size()>0)
            loadRequestsList(mcontext,list,"");*/

        super.onViewCreated(v, savedInstanceState);
    }

    public static void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driver_id", id);
        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DRIVERFUTURERIDES);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DriverFutureRides);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        if (viewList != null && viewList.size() > 0) {
            try {
                requestsList = new ArrayList<>();
                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);
                    if (!requestsList.contains(mp)) {
                        requestsList.add(new ModelItem(mp));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!mode.equalsIgnoreCase("update")) {
                    setAdapterFriendsRequestList();
                } else {
                    requestsAdapter.notifyItemInserted(requestsList.size());
                    requestsAdapter.notifyDataSetChanged();
                }
            }
            requestsListMan.setAutoMeasureEnabled(true);
        } else {
            rv_loc.setVisibility(View.GONE);
        }
    }

    public static void setAdapterFriendsRequestList() {
        if (rv_loc != null) {
            rv_loc.setVisibility(View.VISIBLE);
        }
        requestsAdapter = new DriverFutureRideAdapter(mcontext, requestsList, rv_loc, R.layout.f_ride_rowitem,
                ConstVariable.FutureRides);
        rv_loc.setAdapter(requestsAdapter);
        openN_Activity();
    }

    private static void openN_Activity() {
        if (isNotifClick) {
            Intent i = new Intent(mcontext, ActivityDriverChat.class);
            i.putExtra(Utils.IS_NOTIF_MAP, (Serializable) notifMap);
            i.putExtra(Utils.IS_NOTIFICATION_CLICK, isNotifClick);
            ((Activity) mcontext).startActivity(i);
        }
    }
}
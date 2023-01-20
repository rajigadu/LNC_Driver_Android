package com.lncdriver.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lncdriver.R;
import com.lncdriver.adapter.PartnerFutureRideAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class PartnerFutureRides extends Fragment {
    private static final String TAG = "PartnerFutureRides";
    public static int rideCount = -1;
    static RecyclerView rv_loc;
    public static PartnerFutureRides Instance;

    public static Context mcontext;
    public static PartnerFutureRideAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList = new ArrayList<ModelItem>();
    public HashMap<String, Object> map;
//    public List<HashMap<String, Object>> list = new ArrayList<>();

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


        requestsList.clear();


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

    @Override
    public void onResume() {
        super.onResume();
        MyrequestsListRequest();
    }

    public static void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PARTNERFUTURERIDES);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.PartnerFutureRides);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.logError(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            Log.e(TAG,  "requestsAdapterDD ");
            try {
                requestsList = new ArrayList<ModelItem>();

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if (!requestsList.contains(mp)) {
                        requestsList.add(new ModelItem(mp));
                    }
                }
                //Utils.logError(TAG+"118", "browseMembersList"+eventList.size());
            } catch (Exception e) {
                //Utils.logError(TAG+"122","Exception======================Exception======================Exception");
                e.printStackTrace();
            } finally {
                // Utils.logError(TAG+"127", "browseMembersList"+eventList.size());
                //Utils.logError(TAG+"128", "ok");
                if (!mode.equalsIgnoreCase("update")) {
                    setAdapterFriendsRequestList(context);
                } else {
                    requestsAdapter.notifyItemInserted(requestsList.size());
                    requestsAdapter.notifyDataSetChanged();
                }
            }
            requestsListMan.setAutoMeasureEnabled(true);
        } else {
            rv_loc.setVisibility(View.GONE);
            //noData.setVisibility(View.VISIBLE);
            Log.e(TAG,  "requestsAdapterCC ");
           // setAdapterFriendsRequestList(context);
        }

       // setAdapterFriendsRequestList(context);
        Log.e(TAG,  "requestsAdapterBB ");
    }

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rv_loc != null) {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        if (rideCount == 0) {
            rv_loc.setAdapter(null);
           // rideCount = -1;
            // }
        } else {

        }

        requestsAdapter = new PartnerFutureRideAdapter(mcontext, requestsList, rv_loc,
                R.layout.f_ride_rowitem, ConstVariable.FutureRides);
        rv_loc.setAdapter(requestsAdapter);

        Log.e(TAG,  "requestsAdapterAA ");

    }




}
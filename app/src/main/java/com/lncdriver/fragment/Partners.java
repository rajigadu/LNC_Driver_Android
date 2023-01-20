package com.lncdriver.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lncdriver.R;
import com.lncdriver.activity.AddPartner;
import com.lncdriver.adapter.PartnersAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Partners extends Fragment {
    static RecyclerView rv_loc;
    public static Partners Instance;
    public static Context mcontext;
    Button addPartner;
    public static PartnersAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_partnerslist, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        rv_loc = (RecyclerView) v.findViewById(R.id.rv_loc);
        addPartner = (Button) v.findViewById(R.id.addpartner);
        mcontext = getContext();
        Instance = this;

        requestsListMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);

        addPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivity(mcontext, AddPartner.class);
            }
        });

        MyrequestsListRequest();


        loadRequestsList(mcontext, list, "");

        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_MANAGE_PARTNERS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.PartnersList);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        if (viewList != null && viewList.size() > 0) {
            try {
                requestsList = new ArrayList<ModelItem>();
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
        }
    }

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rv_loc != null) {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        //Utils.logError(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new PartnersAdapter(mcontext, requestsList, rv_loc, R.layout.partner_rowitem, ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.logError(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }


    public static void partnerOnOffListRequest(HashMap<String, Object> map, String type) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driverid", id);
        Global.mapMain.put("partner_id", map.get("id").toString());
        Global.mapMain.put("status", type);

        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PARTNERACTIVATE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.ActivatePartner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

}
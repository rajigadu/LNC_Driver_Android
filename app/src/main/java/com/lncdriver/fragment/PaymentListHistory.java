package com.lncdriver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lncdriver.R;
import com.lncdriver.adapter.PaymentHistoryAdapter;
import com.lncdriver.adapter.PaymentListHistoryAdapter;
import com.lncdriver.model.ModelItem;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentListHistory extends Fragment {

    static RecyclerView rv_loc;
    public static PaymentListHistory Instance;

    public static Context mcontext;
    public static PaymentListHistoryAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_paymenthistory, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        rv_loc = v.findViewById(R.id.rv_loc);
        mcontext = getActivity();
        Instance = this;

        requestsListMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);

        MyRequestsListRequest();
        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void MyRequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driverid", id);
        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PAYMENTHISTORY);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.New_Payment_History);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        if (viewList != null && viewList.size() > 0) {
            try {
                requestsList = new ArrayList<>();

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = viewList.get(i);
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

        requestsAdapter = new PaymentListHistoryAdapter(mcontext, requestsList,
                R.layout.payment_history_list_item_1, ConstVariable.Login);
        rv_loc.setAdapter(requestsAdapter);
    }

}
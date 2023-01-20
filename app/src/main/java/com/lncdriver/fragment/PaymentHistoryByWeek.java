package com.lncdriver.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lncdriver.R;
import com.lncdriver.adapter.PaymentHistoryByWeekAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.Week;
import com.lncdriver.model.WeekList;
import com.lncdriver.utils.ServiceApi;
import com.lncdriver.utils.ServiceGenerator;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryByWeek extends Fragment {
    private static final String TAG = "PaymentHistoryByWeek";
    private RecyclerView rvWeekLocList;
    public PaymentHistoryByWeek Instance;

    public static Context mcontext;
    public static RecyclerView.LayoutManager requestsListMan;
    private final List<Week> weekList = new ArrayList<>();
    private PaymentHistoryByWeekAdapter paymentHistoryByWeekAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_paymenthistory, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        rvWeekLocList = v.findViewById(R.id.rv_loc);
        mcontext = getActivity();
        Instance = this;

        requestsListMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rvWeekLocList.setHasFixedSize(true);
        rvWeekLocList.setLayoutManager(requestsListMan);

        paymentHistoryByWeekAdapter = new PaymentHistoryByWeekAdapter(mcontext, Instance, weekList,
                R.layout.payment_week_item);
        rvWeekLocList.setAdapter(paymentHistoryByWeekAdapter);

        getListByWeek();
    }

    private void getListByWeek() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String driverId = pref1.getUserId();
        ServiceApi service = ServiceGenerator.createService(ServiceApi.class);
        Call<WeekList> response = service.fetchWeekList(driverId);
        Utils.initiatePopupWindow(mcontext, "Please wait...");
        response.enqueue(new Callback<WeekList>() {
            @Override
            public void onResponse(Call<WeekList> call, Response<WeekList> response) {

//                Gson gson = new Gson();
//                String dd = gson.toJson(response);
//                Log.e(TAG, "dd11 "+dd);

                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                if (response.body().getWeek() != null && response.body().getWeek().size() > 0) {
                    weekList.addAll(response.body().getWeek());
                    paymentHistoryByWeekAdapter.notifyDataSetChanged();
                } else {
                    Utils.toastTxt(response.body().getMessage(), mcontext);
                }
            }

            @Override
            public void onFailure(Call<WeekList> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
            }
        });
    }
}
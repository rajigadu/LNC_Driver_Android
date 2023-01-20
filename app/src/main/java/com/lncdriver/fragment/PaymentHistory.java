package com.lncdriver.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lncdriver.R;
import com.lncdriver.adapter.PaymentHistoryAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.Week;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Utils;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentHistory extends Fragment {

    static RecyclerView rv_loc;
    public static PaymentHistory Instance;

    public static Context mcontext;
    public static PaymentHistoryAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    private Week week;

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

        if (getArguments() != null) {
            week = (Week) getArguments().getSerializable(Utils.WEEK_DATA);
        }

        MyRequestsListRequest();

       /* for (int i=0;i<5;i++){
            map=new HashMap<>();
            map.put("tvRideDate","tvRideDate "+String.valueOf(i));
            map.put("payid","10"+String.valueOf(i));
            map.put("rideInf","pickup address"+String.valueOf(i) + " to " + " Drop off address"+String.valueOf(i));
            map.put("amount","100"+String.valueOf(i*10));
            list.add(map);
        }
        if (list!=null&&list.size()>0)
            loadRequestsList(mContext,list,"");*/

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
        Global.mapMain.put("driver_id", id);
        Global.mapMain.put("from_week", week.getFromDate());
        Global.mapMain.put("to_week", week.getToDate());
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_WEEK_REPORT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.Payment_History);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.e(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.e(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<>();

                //Utils.e(TAG+"93", "browseMembersList new "+eventList);

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
            //noData.setVisibility(View.GONE);
        }

        //Utils.e(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new PaymentHistoryAdapter(mcontext, requestsList, rv_loc,
                R.layout.payment_rowitem, ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

  /*  public  void DataFromoneTotwo()
    {
       FragmentTransaction transection=getFragmentManager().beginTransaction();
       Test mfragment=new Test();
        //using Bundle to send data
       Bundle bundle=new Bundle();
       bundle.putString("email","qbc");
       mfragment.setArguments(bundle); //data being send to SecondFragment
       transection.replace(R.id.frame, mfragment);
       transection.commit();
   }*/
}
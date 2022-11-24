package com.lncdriver.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lncdriver.R;
import com.lncdriver.activity.Navigation;
import com.lncdriver.adapter.FutureRidePIncomingAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuturePartnerRequests extends Fragment {
    private static final String TAG = "FuturePartnerRequests";



    static RecyclerView rv_loc;
    public static FuturePartnerRequests Instance;

    public static Context mcontext;
    public static FutureRidePIncomingAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();

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

//        MyrequestsListRequest();

        callLogDrive();

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






    private void callLogDrive() {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
        Call<ResponseBody> call = null;
        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        call = apiInterface.logDriver(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();
                        Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC: " + responseCode);

                        JSONObject object = new JSONObject(responseCode);
                        if(object.getString("status").equalsIgnoreCase("1")){

                            MyrequestsListRequest();

//                            String msg = object.getString("msg");
//                            Utils.toastTxt(""+msg, mcontext);
//
//                            Navigation.nid = 4;
//                            com.lncdriver.fragment.FutureRides.id = 1;
//                            Intent intent = new Intent(mcontext, Navigation.class);
//                            mcontext.startActivity(intent);
//                            ((Activity) mcontext).finish();
                        }else{
//                            String msg = object.getString("msg");
//                            Utils.toastTxt(""+msg, mcontext);
                            Navigation.Instance.logoutRequest();
                        }

                    }else{
                    }

                    //  Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Refreshed getMessage: " + e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCERR: " + t.getMessage());
            }
        });


    }




    public static void MyrequestsListRequest() {
        SharedPreferences pref = mcontext.getApplicationContext().getSharedPreferences("lnctoken", 0);
        SharedPreferences.Editor editor = pref.edit();

        String tId = pref.getString("tokenid", null);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driver_id", id);
        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PARTNERINCOMINGFUTURERIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.PartnerIncomingFutureRides);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.logError(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.logError(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<ModelItem>();

                //Utils.logError(TAG+"93", "browseMembersList new "+eventList);

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
        }
    }

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rv_loc != null) {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        //Utils.logError(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new FutureRidePIncomingAdapter(mcontext, requestsList, rv_loc, R.layout.f_request_rowitem, ConstVariable.FutureRides);
        //set the adapter object to the Recyclerview
        //Utils.logError(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    public void acceptFutureRideRequest(String rid) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driver_id", id);
        Utils.global.mapMain.put("ride_id", rid);
        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PARTNERACCEPTFUTURERIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.PartnerAcceptFutureRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }


}
package com.lncdriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lncdriver.R;
import com.lncdriver.activity.Navigation;
import com.lncdriver.activity.RequestPartnerType;
import com.lncdriver.adapter.DriverFutureRideAdapter;
import com.lncdriver.adapter.FutureRideDIncomingAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.ParsingHelper;
import com.lncdriver.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FutureDriverRequests extends Fragment {
    private static final String TAG = "FutureDriverRequests";
    static RecyclerView rv_loc;
    public static FutureDriverRequests Instance;

    public static Context mcontext;
    public static FutureRideDIncomingAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    private String rideId = "";


    static ArrayList<ItemFutureEstimation> arrayList = new ArrayList<ItemFutureEstimation>();

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


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

//        MyrequestsListRequest();



    //    callLogDrive(id);

        callLogDrive();

        Log.e(TAG , "driverLogCall111");

        super.onViewCreated(v, savedInstanceState);
    }

    public static void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driver_id", id);
        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DRIVERINCOMINGFUTURERIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DriverIncomingFutureRides);
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

      //  Utils.initiatePopupWindow(context, "Please wait request is in progress...");

        ArrayList<ItemFutureEstimation> arrayList = new ArrayList<ItemFutureEstimation>();
                requestsAdapter = new FutureRideDIncomingAdapter(mcontext, requestsList, arrayList, rv_loc,
                R.layout.f_request_rowitem, ConstVariable.FutureRides);
        rv_loc.setAdapter(requestsAdapter);



     //   driverApiCall();




    }

//    private static void driverApiCall() {
//
//        Call<ResponseBody> call = null;
//        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
//
//        SavePref pref1 = new SavePref();
//        pref1.SavePref(mcontext);
//        String id = pref1.getUserId();
//
//
////        call = apiInterface.getPartnerAcceptedRide(id);
//        call = apiInterface.getAcceptRide(id);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                Utils.progressDialog.dismiss();
//                String responseCode = "";
//                try {
//                    if(response.body() != null) {
//                        responseCode = response.body().string();
//                        Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC: " + responseCode);
//
//
//                        arrayList = new ParsingHelper().getFutureEstimation(responseCode);
//
//                        Gson gson = new Gson();
//                        String ss = gson.toJson(arrayList);
//
//                        Log.e(TAG, "Refreshed ssSS: " + ss);
//
//                        if (Utils.progressDialog != null) {
//                            Utils.progressDialog.dismiss();
//                            Utils.progressDialog = null;
//                        }
//
//
//                        partnerApiCall();
//
////                        requestsAdapter = new FutureRideDIncomingAdapter(mcontext, requestsList, arrayList, rv_loc,
////                                R.layout.f_request_rowitem, ConstVariable.FutureRides);
////                        rv_loc.setAdapter(requestsAdapter);
//
//                    }else{
//                    }
//
//                    //  Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
////                Utils.progressDialog.dismiss();
//                if (Utils.progressDialog != null) {
//                    Utils.progressDialog.dismiss();
//                    Utils.progressDialog = null;
//                }
//
//                partnerApiCall();
//                Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCERR: " + t.getMessage());
//            }
//        });
//
//
//
//
//    }
//
//
//
//
//    private static void partnerApiCall() {
//
//        Call<ResponseBody> call = null;
//        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
//
//        SavePref pref1 = new SavePref();
//        pref1.SavePref(mcontext);
//        String id = pref1.getUserId();
//
//
//
//
//        call = apiInterface.getPartnerAcceptedRide(id);
////        call = apiInterface.getAcceptRide(id);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                Utils.progressDialog.dismiss();
//                String responseCode = "";
//                try {
//                    if(response.body() != null) {
//                        responseCode = response.body().string();
//                        Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCPart: " + responseCode);
//
//
//                        ArrayList<ItemFutureEstimation> arrayList11 = new ParsingHelper().getFutureEstimation(responseCode);
//
//                       // arrayList.addAll(arrayList11);
//
//                        Gson gson = new Gson();
//                        String ss = gson.toJson(arrayList11);
//
//                        Log.e(TAG, "Refreshed ssSSPart: " + ss);
//
//                        if (Utils.progressDialog != null) {
//                            Utils.progressDialog.dismiss();
//                            Utils.progressDialog = null;
//                        }
//
//                        requestsAdapter = new FutureRideDIncomingAdapter(mcontext, requestsList, arrayList, rv_loc,
//                                R.layout.f_request_rowitem, ConstVariable.FutureRides);
//                        rv_loc.setAdapter(requestsAdapter);
//
//                    }else{
//                    }
//
//                    //  Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
////                Utils.progressDialog.dismiss();
//                if (Utils.progressDialog != null) {
//                    Utils.progressDialog.dismiss();
//                    Utils.progressDialog = null;
//                }
//                Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCERR: " + t.getMessage());
//
//
//                requestsAdapter = new FutureRideDIncomingAdapter(mcontext, requestsList, arrayList, rv_loc,
//                        R.layout.f_request_rowitem, ConstVariable.FutureRides);
//                rv_loc.setAdapter(requestsAdapter);
//            }
//        });
//
//
//
//
//    }






    public void acceptFutureRideRequest(String rid) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driver_id", id);
        Utils.global.mapMain.put("ride_id", rid);
        rideId = rid;
        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DRIVERACCEPTFUTURERIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DriverAcceptFutureRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public void intimation_FutureRide() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String driverId = pref1.getUserId();
        Intent intent = new Intent(getActivity(), RequestPartnerType.class);
        intent.putExtra(Utils.DRIVER_ID, driverId);
        intent.putExtra(Utils.RIDE_ID, rideId);
        getActivity().startActivity(intent);

        /*AddPartnerFragment fragment = new AddPartnerFragment();
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.rl_partner_container, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();*/
    }





//
//    private static void driverLogCall() {
//
//        Log.e(TAG , "driverLogCall222");
//
//        Call<ResponseBody> call = null;
//        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
//
//        SavePref pref1 = new SavePref();
//        pref1.SavePref(mcontext);
//        String id = pref1.getUserId();
//
//        call = apiInterface.logDriver(id);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                Log.e(TAG , "driverLogCall333");
//                String responseCode = "";
//                try {
//                    if(response.body() != null) {
//                        responseCode = response.body().string();
//                        Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC: " + responseCode);
//
//
//
//                    }else{
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e(TAG , "driverLogCall444");
//                if (Utils.progressDialog != null) {
//                    Utils.progressDialog.dismiss();
//                    Utils.progressDialog = null;
//                }
//            }
//        });
//
//
//
//
//    }
//





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


}
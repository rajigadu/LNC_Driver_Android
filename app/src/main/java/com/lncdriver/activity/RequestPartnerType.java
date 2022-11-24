package com.lncdriver.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.pojo.PartnerList;
import com.lncdriver.pojo.PartnersResponce;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.ServiceApiGet;
import com.lncdriver.utils.ServiceGenerator;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lncdriver.fragment.DriverFutureRides.isListEmpty;

/**
 * Created by narayana on 5/2/2018.
 */

public class RequestPartnerType extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RequestPartnerType";
    @BindView(R.id.agroups)
    RadioGroup typegroup;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.pname)
    TextView pname;

    @BindView(R.id.pemail)
    TextView pemail;

    @BindView(R.id.pmobile)
    TextView pmobile;

    @BindView(R.id.rl_partner)
    RelativeLayout rl_partner_details;

    @BindView(R.id.et_partner_list)
    EditText etPartnerList;

    @BindView(R.id.toolbar)
    RelativeLayout toolBar;

    @BindView(R.id.cb_default_driver)
    CheckBox cbDefaultDriver;

    @BindView(R.id.radio2)
    RadioButton rbWithoutPartner;

    @BindView(R.id.radio1)
    RadioButton rbWithPartner;

    int eventselectedId = 0;
    String type = "0";
    HashMap<String, Object> map;
    private RadioButton typebutton;
    private Context mcontext;
    private ArrayList<PartnerList> partnerLists;

    private String rideId = "", driverId = "";
    private String partDummyId = "";

    @BindView(R.id.back)
    ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicetype);
        ButterKnife.bind(this);
        mcontext = this;
        toolBar.setVisibility(View.VISIBLE);
        etPartnerList.setHint("Add Partner");
        partnerLists = new ArrayList<>();

        submit.setOnClickListener(this);
        rl_partner_details.setVisibility(View.VISIBLE);

        eventselectedId = typegroup.getCheckedRadioButtonId();
        typebutton = (RadioButton) findViewById(eventselectedId);
        type = "1";

        if (getIntent().getExtras() != null) {
            rideId = getIntent().getExtras().getString(Utils.RIDE_ID);
            driverId = getIntent().getExtras().getString(Utils.DRIVER_ID);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        typegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) findViewById(eventselectedId);
                        type = "1";
                        rl_partner_details.setVisibility(View.VISIBLE);
                        cbDefaultDriver.setVisibility(View.VISIBLE);
                        cbDefaultDriver.setChecked(true);
                        //Toast.makeText(ServiceType.this,type,Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.radio2:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) findViewById(eventselectedId);
                        type = "2";
                        rl_partner_details.setVisibility(View.GONE);
                        cbDefaultDriver.setVisibility(View.GONE);
                        cbDefaultDriver.setChecked(false);
                        // Toast.makeText(ServiceType.this,type,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        cbDefaultDriver.setChecked(true);
        cbDefaultDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //eventselectedId = typegroup.getCheckedRadioButtonId();
                    rbWithPartner.setChecked(true);
                    rbWithoutPartner.setChecked(false);
                    //typebutton = (RadioButton) findViewById(eventselectedId);
                    type = "1";
                    rl_partner_details.setVisibility(View.VISIBLE);
                } else {
                    //eventselectedId = typegroup.getCheckedRadioButtonId();
                    //typebutton = (RadioButton) findViewById(eventselectedId);
                    type = "2";
                    rbWithPartner.setChecked(false);
                    rbWithoutPartner.setChecked(true);
                    rl_partner_details.setVisibility(View.GONE);
                }
            }
        });

        etPartnerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (partnerLists.size() > 0) {
                                    pickBookingType();
                                } else {
                                    try {
                                        getPartnersRequest();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });

            }
        });

        getActivePartnerRequest();
    }

    public void getActivePartnerRequest() {
        OnlineRequest.getDriverTypePartnerReq(this, null);
    }


    public void submit() {
        new Utils(RequestPartnerType.this);
        if (type.equalsIgnoreCase("1")) {
            if (pname.getText().toString().isEmpty()) {
                Utils.toastTxt("please enter btnPartnerInfo name.", RequestPartnerType.this);
            } else if (pemail.getText().toString().isEmpty()) {
                Utils.toastTxt("please enter btnPartnerInfo email.", RequestPartnerType.this);
            } else if (!Utils.isValidEmail(pemail.getText().toString().trim())) {
                Utils.toastTxt("Please enter valid email address.", RequestPartnerType.this);
            } else if (pmobile.getText().toString().isEmpty()) {
                Utils.toastTxt("please enter btnPartnerInfo mobile number.", RequestPartnerType.this);
            } else {
                map = new HashMap<>();
                map.put("driver_type", type);
                map.put("pname", pname.getText().toString());
                map.put("pemail", pemail.getText().toString());
                map.put("pmobile", pmobile.getText().toString());
                map.put("utype", "1");
                map.put("default", "yes");

                if(partDummyId == null){
                    map.put("id", "");
                }else{
                    map.put("id", partDummyId);
                }


                if (Utils.isNetworkAvailable(mcontext)) {
                    Log.e(TAG, "postDummyPartner11");

                    postDummyPartner();
                } else {
                    Log.e(TAG, "postDummyPartner22");
                    Utils.showInternetErrorMessage(mcontext);
                }
                //OnlineRequest.serviceTypeRequest(RequestPartnerType.this, map);
            }
        } else {
            map = new HashMap<>();
            map.put("driver_type", type);
            map.put("utype", "1");
            map.put("default", "no");
            OnlineRequest.serviceTypePartnerRequest(RequestPartnerType.this, map);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submit();
                break;
        }
    }

    private void getPartnersRequest() {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String driverId = pref1.getUserId();
        ServiceApiGet service = ServiceGenerator.createService(ServiceApiGet.class);
        Call<PartnersResponce> response = service.fetchPartnerList(driverId);
        response.enqueue(new Callback<PartnersResponce>() {
            @Override
            public void onResponse(Call<PartnersResponce> call, Response<PartnersResponce> response) {
                try {
                    if (Utils.progressDialog != null) {
                        Utils.progressDialog.dismiss();
                        Utils.progressDialog = null;
                        PartnersResponce partnersResponce = response.body();
                        if (partnersResponce.getStatus().equals("1")) {
                            PartnerList list = new PartnerList();
                            list.setPartnerName("Add New Partner");
                            list.setDriverId("");
                            list.setPartnerEmail("");
                            list.setPartnerPhone("");
                            partnerLists.add(list);

                            partnerLists.addAll(partnersResponce.getPartnerLists());
                            pickBookingType();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PartnersResponce> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
            }
        });
    }

    private void pickBookingType() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1);

        for (int i = 0; i < partnerLists.size(); i++) {
            adapter.add(partnerLists.get(i).getPartnerName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
       // builder.setTitle("Add Partner");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                etPartnerList.setText(partnerLists.get(item).getPartnerName());

                if(partnerLists.get(item).getPartnerName().equalsIgnoreCase("Add New Partner")){
                    pname.setText("");
                }else{
                    pname.setText(partnerLists.get(item).getPartnerName());
                }


                pemail.setText(partnerLists.get(item).getPartnerEmail());
                pmobile.setText(partnerLists.get(item).getPartnerPhone());
                partDummyId = partnerLists.get(item).getId();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void postDummyPartner() {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");

        new Utils(mcontext);
        Utils.global.mapMain();


        Log.e(TAG , "rideId "+rideId);
        Log.e(TAG , "pname "+map.get("pname").toString());
        Log.e(TAG , "pemail "+map.get("pemail").toString());
        Log.e(TAG , "pmobile "+map.get("pmobile").toString());
        Log.e(TAG , "partDummyId); "+partDummyId);
        Log.e(TAG , "driverId); "+driverId);

        Utils.global.mapMain.put(Utils.RIDE_ID, rideId);
        Utils.global.mapMain.put("pname", map.get("pname").toString());
        Utils.global.mapMain.put("pemail", map.get("pemail").toString());
        Utils.global.mapMain.put("pmobile", map.get("pmobile").toString());
        if(partDummyId == null){
            Utils.global.mapMain.put("dummy_id", "");
        }else{
            Utils.global.mapMain.put("dummy_id", partDummyId);
        }

        Utils.global.mapMain.put(ConstVariable.DRIVER_ID, driverId);


        Log.e(TAG , "dd111111111 ");

        ServiceApiGet service = ServiceGenerator.createService(ServiceApiGet.class);
        Call<ResponseBody> response = service.dummyPartner(
                Utils.global.mapMain);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Gson gson = new Gson();
                String dd = gson.toJson(response);

                Log.e(TAG , "ddQQQQ "+dd);

                Navigation.nid = 4;
                com.lncdriver.fragment.FutureRides.id = 0;
                isListEmpty = true;
                Intent intent = new Intent(mcontext, Navigation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG , "dd222222 ");
            }
        });
    }
}

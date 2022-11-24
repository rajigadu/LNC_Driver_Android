package com.lncdriver.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.pojo.PartnerList;
import com.lncdriver.pojo.PartnersResponce;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.ServiceApiGet;
import com.lncdriver.utils.ServiceGenerator;
import com.lncdriver.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by narayana on 5/2/2018.
 */

public class AuthenticatePartner extends AppCompatActivity implements View.OnClickListener {

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

    private ArrayList<PartnerList> partnerLists;

    int eventselectedId = 0;
    String type = "0";
    HashMap<String, Object> map;
    private RadioButton typebutton;
    public static AuthenticatePartner Instance;
    public String pId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticatepartner);
        ButterKnife.bind(this);
        partnerLists = new ArrayList<>();
        submit.setOnClickListener(this);

        Instance = this;
        rl_partner_details.setVisibility(View.VISIBLE);

        eventselectedId = typegroup.getCheckedRadioButtonId();
        typebutton = (RadioButton) findViewById(eventselectedId);
        type = "1";
        etPartnerList.setHint("Add Partner");

        SavePref pref1 = new SavePref();
        pref1.SavePref(AuthenticatePartner.this);
        pref1.setispartnerAuth("");

        typegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) findViewById(eventselectedId);
                        type = "1";
                        rl_partner_details.setVisibility(View.VISIBLE);
                        //Toast.makeText(ServiceType.this,type,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio2:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) findViewById(eventselectedId);
                        type = "2";
                        rl_partner_details.setVisibility(View.GONE);
                        // Toast.makeText(ServiceType.this,type,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        getActivePartnerRequest();
        getPartnersRequest();

        etPartnerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBookingType();

            }
        });
    }

    private void pickBookingType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AuthenticatePartner.this,
                android.R.layout.simple_dropdown_item_1line);

        for (int i = 0; i < partnerLists.size(); i++) {
            adapter.add(partnerLists.get(i).getPartnerName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Partner");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                etPartnerList.setText(partnerLists.get(item).getPartnerName());
                pname.setText(partnerLists.get(item).getPartnerName());
                pemail.setText(partnerLists.get(item).getPartnerEmail());
                pmobile.setText(partnerLists.get(item).getPartnerPhone());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getActivePartnerRequest() {
        OnlineRequest.getActivePartnerRequest(AuthenticatePartner.this, null);
    }

    public void submit() {
        new Utils(AuthenticatePartner.this);

        if (type.equalsIgnoreCase("1")) {
            if (pname.toString().isEmpty()) {
                Utils.toastTxt("please enter btnPartnerInfo name.", AuthenticatePartner.this);
            } else if (pemail.toString().isEmpty()) {
                Utils.toastTxt("please enter btnPartnerInfo email.", AuthenticatePartner.this);
            } else if (!Utils.isValidEmail(pemail.getText().toString().trim())) {
                Utils.toastTxt("Please enter valid email address.", AuthenticatePartner.this);
            } else if (pmobile.toString().isEmpty()) {
                Utils.toastTxt("please enter btnPartnerInfo mobile number.", AuthenticatePartner.this);
            } else {
                map = new HashMap<>();
                map.put("driver_type", type);
                map.put("pname", pname.getText().toString());
                map.put("pemail", pemail.getText().toString());
                map.put("pmobile", pmobile.getText().toString());
                OnlineRequest.authenticatePartnerRequest(AuthenticatePartner.this, map);
            }
        } else {
            map = new HashMap<>();
            map.put("driver_type", type);
            OnlineRequest.authenticatePartnerRequest(AuthenticatePartner.this, map);
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

    public void populatePartnerDetails(JSONObject jsonObject) {
        try {
            JSONObject smap = jsonObject.optJSONObject("data");
            if (smap != null) {
                if (smap.has("partner_name")) {
                    pname.setText(smap.optString("partner_name").toString());
                }

                if (smap.has("partner_email")) {
                    pemail.setText(smap.optString("partner_email").toString());
                }

                if (smap.has("partner_phone")) {
                    pmobile.setText(smap.optString("partner_phone").toString());
                }

                if (smap.has("id")) {
                    pId = smap.optString("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPartnersRequest() {
        Utils.initiatePopupWindow(this, "Please wait request is in progress...");
        SavePref pref1 = new SavePref();
        pref1.SavePref(this);
        String driverId = pref1.getUserId();
        ServiceApiGet service = ServiceGenerator.createService(ServiceApiGet.class);
        Call<PartnersResponce> response = service.fetchPartnerList(driverId);
        response.enqueue(new Callback<PartnersResponce>() {
            @Override
            public void onResponse(Call<PartnersResponce> call, Response<PartnersResponce> response) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                    PartnersResponce partnersResponce = response.body();

                    Utils.toastTxt(response.message(), AuthenticatePartner.this);

                    if (partnersResponce.getStatus().equals("1")) {

                    }
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
}

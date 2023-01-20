package com.lncdriver.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.pojo.PartnerList;
import com.lncdriver.pojo.PartnersResponce;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.ServiceApiGet;
import com.lncdriver.utils.ServiceGenerator;
import com.lncdriver.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverType extends Fragment implements View.OnClickListener {
    private static final String TAG = "DriverType";
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

    @BindView(R.id.cb_default_driver)
    CheckBox cbDefaultDriver;

    @BindView(R.id.radio2)
    RadioButton rbWithoutPartner;

    @BindView(R.id.radio1)
    RadioButton rbWithPartner;

    int eventselectedId = 0;
    String type = "0";
    HashMap<String, Object> map;
    private Unbinder unbinder;
    public static Context mcontext;
    private RadioButton typeButton;
    public View view;

    @BindView(R.id.et_partner_list)
    EditText etPartnerList;
    private ArrayList<PartnerList> partnerLists;
    private String partDummyId = "";

    public static DriverType Instance = null;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static OnFragmentInteractionListenerToHome mListener;

    public interface OnFragmentInteractionListenerToHome {
        void onFragmentInteraction(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerToHome) {
            mListener = (OnFragmentInteractionListenerToHome) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public static void updateDriverType() {
        mListener.onFragmentInteraction(ConstVariable.Home);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drivertype, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        Instance = this;
        mcontext = getActivity();
        unbinder = ButterKnife.bind(this, v);
        partnerLists = new ArrayList<>();
        view = v;

        submit.setOnClickListener(this);
        rl_partner_details.setVisibility(View.VISIBLE);
        etPartnerList.setHint("Add Partner");

        eventselectedId = typegroup.getCheckedRadioButtonId();
        typeButton = (RadioButton) v.findViewById(eventselectedId);
        type = "1";
        cbDefaultDriver.setChecked(true);
        typegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typeButton = (RadioButton) view.findViewById(eventselectedId);
                        type = "1";
                        rl_partner_details.setVisibility(View.VISIBLE);
                        cbDefaultDriver.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio2:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typeButton = (RadioButton) view.findViewById(eventselectedId);
                        type = "2";
                        rl_partner_details.setVisibility(View.GONE);
                        cbDefaultDriver.setChecked(false);
                        cbDefaultDriver.setVisibility(View.GONE);
                        break;
                }
            }
        });

        cbDefaultDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbWithPartner.setChecked(true);
                    rbWithoutPartner.setChecked(false);
                    type = "1";
                    rl_partner_details.setVisibility(View.VISIBLE);
                } else {
                    type = "2";
                    rbWithPartner.setChecked(false);
                    rbWithoutPartner.setChecked(true);
                    rl_partner_details.setVisibility(View.GONE);
                }
            }
        });

        getActivePartnerRequest();
        // getPartnersRequest();
        etPartnerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void getActivePartnerRequest() {
        OnlineRequest.getDriverTypePartnerReq(getActivity(), null);
    }

    public void populatePartnerDetails(JSONObject jsonObject) {
        try {
            JSONObject smap = jsonObject.optJSONObject("data");
            if (smap != null) {
                if (smap.has("partner_name")) {
                    pname.setText(smap.optString("partner_name"));
                }

                if (smap.has("partner_email")) {
                    pemail.setText(smap.optString("partner_email"));
                }

                if (smap.has("partner_phone")) {
                    pmobile.setText(smap.optString("partner_phone"));
                }

                if (smap.has("id")) {
                    partDummyId = smap.optString("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickBookingType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1);

        for (int i = 0; i < partnerLists.size(); i++) {
            adapter.add(partnerLists.get(i).getPartnerName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        //builder.setTitle("Add Partner");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submit();
                break;
        }
    }

    public void submit() {
        new Utils(mcontext);

        if (type.equalsIgnoreCase("1")) {



            if (pname.getText().toString().isEmpty()) {
                Utils.toastTxt("please enter partner name.", mcontext);
            } else if (pemail.getText().toString().isEmpty()) {
                Utils.toastTxt("please enter partner email.", mcontext);
            } else if (!Utils.isValidEmail(pemail.getText().toString().trim())) {
                Utils.toastTxt("Please enter valid email address.", mcontext);
            } else if (pmobile.getText().toString().isEmpty()) {
                Utils.toastTxt("please enter partner mobile number.", mcontext);
            } else {
                map = new HashMap<>();
                map.put("driver_type", type);
                map.put("pname", pname.getText().toString());
                map.put("pemail", pemail.getText().toString());
                map.put("pmobile", pmobile.getText().toString());
                map.put("utype", "2");
                map.put("default", "yes");

                if(partDummyId == null){
                    map.put("id", "");
                }else{
                    map.put("id", partDummyId);
                }


                OnlineRequest.serviceTypeRequest(mcontext, map);

                Log.e(TAG, "CCCCCCCtype "+type);
                Log.e(TAG, "CCCCCCCpname "+pname.getText().toString());
                Log.e(TAG, "CCCCCCCpemail "+pemail.getText().toString());
                Log.e(TAG, "CCCCCCCpmobile "+pmobile.getText().toString());
                Log.e(TAG, "CCCCCCCpartDummyId "+partDummyId);

            }
        } else {
            map = new HashMap<>();
            map.put("driver_type", type);
            map.put("utype", "2");
            map.put("default", "no");
            OnlineRequest.serviceTypeRequest(mcontext, map);
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
                    } else {
                        Utils.toastTxt(partnersResponce.getMessage(), mcontext);
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
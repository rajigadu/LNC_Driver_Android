package com.lncdriver.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPartnerFragment extends Fragment implements View.OnClickListener {

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

    @BindView(R.id.toolbar)
    RelativeLayout rlToolBar;

    int eventselectedId = 0;
    String type = "0";
    HashMap<String, Object> map;
    private Unbinder unbinder;
    public static Context mcontext;
    private RadioButton typebutton;
    public View view;

    @BindView(R.id.et_partner_list)
    EditText etPartnerList;
    private ArrayList<PartnerList> partnerLists;
    static HashMap<String, Object> rMap;

    private String partDummyId = "";

    @BindView(R.id.cb_default_driver)
    CheckBox cbDefaultDriver;

    @BindView(R.id.radio2)
    RadioButton rbWithoutPartner;

    @BindView(R.id.radio1)
    RadioButton rbWithPartner;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static OnFragmentInteractionListenerToHome mListener;

    public interface OnFragmentInteractionListenerToHome {
        // TODO: Update argument type and name
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
        unbinder = ButterKnife.bind(this, v);
        partnerLists = new ArrayList<>();
        view = v;
        mcontext = getContext();
        submit.setOnClickListener(this);
        rl_partner_details.setVisibility(View.VISIBLE);
        etPartnerList.setHint("Add Partner");

        if (getArguments() != null) {
            rMap = (HashMap<String, Object>) getArguments().getSerializable("map");
        }

        rlToolBar.setVisibility(View.VISIBLE);
        eventselectedId = typegroup.getCheckedRadioButtonId();
        typebutton = (RadioButton) v.findViewById(eventselectedId);
        type = "1";
        cbDefaultDriver.setChecked(true);
        typegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) view.findViewById(eventselectedId);
                        type = "1";
                        rl_partner_details.setVisibility(View.VISIBLE);
                        cbDefaultDriver.setVisibility(View.VISIBLE);
                        cbDefaultDriver.setChecked(true);
                        //Toast.makeText(ServiceType.this,type,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio2:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) view.findViewById(eventselectedId);
                        type = "2";
                        rl_partner_details.setVisibility(View.GONE);
                        cbDefaultDriver.setVisibility(View.GONE);
                        cbDefaultDriver.setChecked(false);
                        partDummyId = "";
                        // Toast.makeText(ServiceType.this,type,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

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

        getPartnersRequest();
        etPartnerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partDummyId = "";
                pickBookingType();

            }
        });
    }

    private void pickBookingType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line);

        for (int i = 0; i < partnerLists.size(); i++) {
            adapter.add(partnerLists.get(i).getPartnerName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Add Partner");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                etPartnerList.setText(partnerLists.get(item).getPartnerName());
                pname.setText(partnerLists.get(item).getPartnerName());
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
            if (pname.toString().isEmpty()) {
                Utils.toastTxt("please enter partner name.", mcontext);
            } else if (pemail.toString().isEmpty()) {
                Utils.toastTxt("please enter partner email.", mcontext);
            } else if (!Utils.isValidEmail(pemail.getText().toString().trim())) {
                Utils.toastTxt("Please enter valid email address.", mcontext);
            } else if (pmobile.toString().isEmpty()) {
                Utils.toastTxt("please enter partner mobile number.", mcontext);
            } else {
                map = new HashMap<>();
                map.put("driver_type", type);
                map.put("pname", pname.getText().toString());
                map.put("pemail", pemail.getText().toString());
                map.put("pmobile", pmobile.getText().toString());
                map.put("dummy_id", partDummyId);
                map.put("utype", "2");
                map.put("default", "yes");
                if (Utils.isNetworkAvailable(mcontext)) {
                    postDummyPartner();
                } else {
                    Utils.showInternetErrorMessage(mcontext);
                }
                //OnlineRequest.serviceTypeRequest(mcontext, map);
            }
        } else {
            map = new HashMap<>();
            map.put("driver_type", type);
            map.put("default", "no");
            map.put("utype", "2");
            OnlineRequest.serviceTypeRequest(mcontext, map);
        }
    }

    private void postDummyPartner() {
        Utils.initiatePopupWindow(mcontext, "Please wait request is in progress...");
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String driverId = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();


        Utils.global.mapMain.put("pname", map.get("pname").toString());
        Utils.global.mapMain.put("pemail", map.get("pemail").toString());
        Utils.global.mapMain.put("pmobile", map.get("pmobile").toString());
        Utils.global.mapMain.put("dummy_id", partDummyId);
        Utils.global.mapMain.put(ConstVariable.DRIVER_ID, driverId);

        ServiceApiGet service = ServiceGenerator.createService(ServiceApiGet.class);
        Call<ResponseBody> response = service.login(Settings.URL_DUMMY_PARTNER_FUTURE_ACCEPT, Utils.global.mapMain);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
                        partnerLists.addAll(partnersResponce.getPartnerLists());
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
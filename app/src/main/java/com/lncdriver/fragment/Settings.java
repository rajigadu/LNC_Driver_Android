package com.lncdriver.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.activity.AddBankAccount;
import com.lncdriver.activity.ChangePassword;
import com.lncdriver.activity.ContactUs;
import com.lncdriver.activity.EditPersonalInfo;
import com.lncdriver.activity.EditVehicleInfo;
import com.lncdriver.activity.WebViewAll;
import com.lncdriver.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Settings extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;
    public static String TAG = Settings.class.getName();
    public static Context mcontext;
    public static Settings Instance;

    @BindView(R.id.editpinfo)
    TextView edit_personal;

    @BindView(R.id.editvinfo)
    TextView edit_vehicle;

    @BindView(R.id.addbank)
    TextView add_bankAccount;

    @BindView(R.id.changepassword)
    TextView changep;

    @BindView(R.id.privacypolicy)
    TextView privacy;

    @BindView(R.id.terms)
    TextView terms;

    @BindView(R.id.contactus)
    TextView contactus;

    TextView version;


    HashMap<String, Object> map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_settings, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        unbinder = ButterKnife.bind(this, v);
        mcontext = getContext();
        Instance = this;

        version = (TextView) v.findViewById(R.id.version);

        edit_personal.setOnClickListener(this);
        edit_vehicle.setOnClickListener(this);
        add_bankAccount.setOnClickListener(this);
        changep.setOnClickListener(this);
        privacy.setOnClickListener(this);
        terms.setOnClickListener(this);
        contactus.setOnClickListener(this);

        String vername = "";
        try {
            PackageInfo pInfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0);
            vername = pInfo.versionName;
            // verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!vername.equalsIgnoreCase("")) {
            version.setText(vername);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editpinfo:
                new Utils(getActivity());
                Utils.startActivity(getActivity(), EditPersonalInfo.class);
                break;

            case R.id.editvinfo:
                new Utils(getActivity());
                Utils.startActivity(getActivity(), EditVehicleInfo.class);
                break;

            case R.id.changepassword:
                Utils.startActivity(getActivity(), ChangePassword.class);
                break;

            case R.id.addbank:
                Utils.startActivity(getActivity(), AddBankAccount.class);
                break;

            case R.id.privacypolicy:
                Intent j = new Intent(getActivity(), WebViewAll.class);
                j.putExtra("webpage", com.lncdriver.utils.Settings.URL_PRIVACY_POLICY);
                j.putExtra("pageheading", "Privacy Policy");
                startActivity(j);
                break;

            case R.id.terms:
                Intent i = new Intent(getActivity(), WebViewAll.class);
                i.putExtra("webpage", com.lncdriver.utils.Settings.URL_TERMS_CONDITIONS);
                i.putExtra("pageheading", "Terms and  Conditions");
                startActivity(i);
                break;

            case R.id.contactus:
                new Utils(getActivity());
                Utils.startActivity(getActivity(), ContactUs.class);
                break;
        }
    }

   /* public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent != null)
            {
                String isid = intent.getStringExtra("isid");

                if (isid.equalsIgnoreCase("1"))
                {
                    String status = intent.getStringExtra("status");
                    String data = intent.getStringExtra("data");

                    if (status.equalsIgnoreCase("1"))
                    {
                        imgOnStatus.setImageResource(R.drawable.online);
                        dataOnStatus.setText(data);
                        dataOnStatus.setTextColor(getResources().getColor(R.color.green));
                        online.setVisibility(View.GONE);
                        offline.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        imgOnStatus.setImageResource(R.drawable.offline);
                        dataOnStatus.setText(data);
                        dataOnStatus.setTextColor(getResources().getColor(R.color.red));
                        online.setVisibility(View.VISIBLE);
                        offline.setVisibility(View.GONE);
                    }
                }
            }
        }
    };*/

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().registerReceiver(mHandleMessageReceiver , new IntentFilter("OPEN_NEW_ACTIVITY"));
    }

    @Override
    public void onDestroy() {
        // getActivity().unregisterReceiver(mHandleMessageReceiver);

       /* if (mtimer!=null)
            mtimer.cancel();*/

        super.onDestroy();
    }
}
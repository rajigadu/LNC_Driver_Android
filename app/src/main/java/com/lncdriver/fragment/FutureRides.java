package com.lncdriver.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.lncdriver.R;
import com.lncdriver.utils.Utils;

import java.util.HashMap;

/**
 * Created by narayana on 3/6/2018.
 */

public class FutureRides extends Fragment {
    public static FutureRides Instance;
    public static String TAG = FutureRides.class.getName();
    public static Context mcontext;
    public RelativeLayout container;
    public static int id = 0;
    private boolean isNotifClick = false;
    public HashMap<String, Object> map;

    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_rides, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        container = (RelativeLayout) v.findViewById(R.id.container);
        // viewPager = (ViewPager) findViewById(R.id.viewPager);

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Driver future rides"));
        tabLayout.addTab(tabLayout.newTab().setText("Partner future rides"));

        Instance = this;
        mcontext = getActivity();

        //  viewPagerAdapter = new ViewPagerAdapter(mcontext,getSupportFragmentManager());
        //  viewPager.setAdapter(viewPagerAdapter);
        //  viewPager.setCurrentItem(1);
        //  tabLayout.setupWithViewPager(viewPager);

        if (getArguments() != null) {
            isNotifClick = getArguments().getBoolean(Utils.IS_NOTIFICATION_CLICK);
            if (isNotifClick) {
                map = (HashMap<String, Object>) getArguments().getSerializable("map");
                id = 0;
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                new Utils(mcontext);
                //  Utils.toastTxt(String.valueOf(tab.getPosition()),Utils.context);

                if (tab.getPosition() == 0) {
                    tab = tabLayout.getTabAt(0);
                    tab.select();
                    replaceFragment(new DriverFutureRides());
                } else if (tab.getPosition() == 1) {
                    tab = tabLayout.getTabAt(1);
                    tab.select();
                    replaceFragment(new PartnerFutureRides());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (id == 0) {
            Log.e("tabs====123", "456");
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            replaceFragment(new DriverFutureRides());
        } else if (id == 1) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            replaceFragment(new PartnerFutureRides());
            Log.e("tabs====123", "4567");
        }
        super.onViewCreated(v, savedInstanceState);
    }

    public void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = getChildFragmentManager();
        Bundle bundle = new Bundle();
        if (isNotifClick) {
            bundle.putSerializable(Utils.IS_NOTIF_MAP, map);
            bundle.putBoolean(Utils.IS_NOTIFICATION_CLICK, isNotifClick);
        }
        fragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionhaystack(int page);
    }
}

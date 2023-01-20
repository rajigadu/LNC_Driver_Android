package com.lncdriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.lncdriver.R;
import com.lncdriver.utils.Utils;

/**
 * Created by narayana on 3/6/2018.
 */

public class Rides extends Fragment {
    private FragmentActivity myContext;
    public static Rides Instance;
    public static String TAG = Rides.class.getName();
    public static Context mcontext;
    public RelativeLayout container;
    public static int id = 0;

    TabLayout tabLayout;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_rides, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        container = (RelativeLayout) v.findViewById(R.id.container);
        // viewPager = (ViewPager) findViewById(R.id.viewPager);

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        Instance = this;
        mcontext = getActivity();

        //  viewPagerAdapter = new ViewPagerAdapter(mcontext,getSupportFragmentManager());
        //viewPager.setAdapter(viewPagerAdapter);
        //  viewPager.setCurrentItem(1);
        //tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                new Utils(mcontext);
                //  Utils.toastTxt(String.valueOf(tab.getPosition()),Utils.context);

                if (tab.getPosition() == 0) {
                    tab = tabLayout.getTabAt(0);
                    tab.select();
                    replaceFragment(new CurrentRides());
                } else if (tab.getPosition() == 1) {
                    tab = tabLayout.getTabAt(1);
                    tab.select();
                    replaceFragment(new RideHistory());
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
            replaceFragment(new CurrentRides());
        } else if (id == 1) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            replaceFragment(new RideHistory());
            Log.e("tabs====123", "4567");
        }
        super.onViewCreated(v, savedInstanceState);
    }

    public void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = getChildFragmentManager();
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

package com.lncdriver.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.fragment.CurrentRides;
import com.lncdriver.fragment.FutureDriverRequests;
import com.lncdriver.fragment.FuturePartnerRequests;
import com.lncdriver.fragment.RideHistory;
import com.lncdriver.fragment.Rides;
import com.lncdriver.utils.Utils;

public class FutureRides extends AppCompatActivity {
    public static FutureRides Instance;
    public static String TAG = FutureRides.class.getName();
    public static Context mcontext;
    public RelativeLayout container;
    public static int id = 0;
    TabLayout tabLayout;

    ImageView back;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_rides);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        container = (RelativeLayout) findViewById(R.id.container);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        // viewPager = (ViewPager) findViewById(R.id.viewPager);

        title.setText("Future Requests");
        title.setText(Html.fromHtml("<font color='#ffffff'> future&nbsp; </font><font color='#8bbc50'> Ride</font>"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("DRIVER REQUESTS"));
        tabLayout.addTab(tabLayout.newTab().setText("PARTNER REQUESTS"));

        Instance = this;
        mcontext = this;

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
                    replaceFragment(new FutureDriverRequests());
                } else if (tab.getPosition() == 1) {
                    tab = tabLayout.getTabAt(1);
                    tab.select();
                    replaceFragment(new FuturePartnerRequests());
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
            replaceFragment(new FutureDriverRequests());
        } else if (id == 1) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            replaceFragment(new FuturePartnerRequests());
            Log.e("tabs====123", "4567");
        }
    }

    public void replaceFragment(android.support.v4.app.Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionhaystack(int page);
    }


}

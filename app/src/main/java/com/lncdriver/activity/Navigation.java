package com.lncdriver.activity;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.bumptech.glide.Glide;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lncdriver.R;
import com.lncdriver.dbh.DbhAssignRides;
import com.lncdriver.dbh.base.BaseActivity;
import com.lncdriver.fcm.LocationBackgroundService;
import com.lncdriver.fcm.MyFirebaseMessagingService;
import com.lncdriver.fragment.CurrentRides;
import com.lncdriver.fragment.DriverType;
import com.lncdriver.fragment.FutureHistoryRides;
import com.lncdriver.fragment.FutureRideRequests;
import com.lncdriver.fragment.FutureRides;
import com.lncdriver.fragment.Home;
import com.lncdriver.fragment.Partners;
import com.lncdriver.fragment.PaymentHistoryByWeek;
import com.lncdriver.fragment.PaymentListHistory;
import com.lncdriver.fragment.PendingCurrentRides;
import com.lncdriver.fragment.RideHistory;
import com.lncdriver.fragment.Settings;
import com.lncdriver.fragment.WebFragment;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.SoundService;
import com.lncdriver.other.AlarmJobService;
import com.lncdriver.reward.BounsProgramFragment;
import com.lncdriver.reward.MyRewardProgramFragment;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Navigation extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        Home.OnFragmentInteractionListenerHome, Home.SendData, DriverType.OnFragmentInteractionListenerToHome,
        PendingCurrentRides.OnFragmentInteractionListenerPendingCurrebtRides {

    private static final int REFRESH_REQUEST = 122;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
    public Call<ResponseBody> call = null;

    SavePref pref1 = new SavePref();


    private final BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private final BadgeStyle bigStyle = ActionItemBadge.BadgeStyles.DARK_GREY_LARGE.getStyle();


    Menu menuChoose = null;


    private static final String TAG = "Navigation";
    String title = "";
    public static int nid = 0;
    public static Navigation Instance;
    boolean back = false;
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    TextView txtTimner;
    public static CountDownTimer mtimer;
    androidx.appcompat.app.AlertDialog dialog = null;
    public HashMap<String, Object> map;
    static HashMap<String, Object> smap;
    static TextView fRide_Time;

    private boolean isNotifClick = false;
    public HashMap<String, Object> notifMap;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        pref1.SavePref(Navigation.this);

        // ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        fRide_Time = findViewById(R.id.status_fride);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(Navigation.this, DividerItemDecoration.VERTICAL));

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_full));
        Instance = this;

        View header = navigationView.getHeaderView(0);
        ImageView pic = header.findViewById(R.id.imageView);
        TextView username = header.findViewById(R.id.username);
        TextView rating = header.findViewById(R.id.rating);

        SavePref pref1 = new SavePref();
        pref1.SavePref(Navigation.this);
        String urat = pref1.getRating();
        String fname = pref1.getUserFName();

        String llname = pref1.getUserLName();
        String imag = pref1.getImage();





        if (!imag.equalsIgnoreCase("")) {
            Picasso.with(Navigation.this).load(imag).into(pic);
            Picasso.with(Navigation.this).load(com.lncdriver.utils.Settings.URLIMAGEBASE +
                    imag).placeholder(R.drawable.appicon).into(pic);
        }

        StringBuilder sb = new StringBuilder();

        if (!fname.equalsIgnoreCase("")) {
            sb.append(fname);
        }
        if (!urat.equalsIgnoreCase("")) {
            rating.setText(urat);
        }
        if (!llname.equalsIgnoreCase("")) {
            sb.append(" ");
            sb.append(llname);
            username.setText(sb);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        printHashKey();
        navigationView.setNavigationItemSelectedListener(this);

        title = getString(R.string.home);

        if (getIntent().getExtras() != null) {
            isNotifClick = getIntent().getExtras().getBoolean(Utils.IS_NOTIFICATION_CLICK);
            notifMap = (HashMap<String, Object>) getIntent().getExtras().getSerializable("map");
        }

        fragmentList.clear();
        fragmentList.add(Fragment.instantiate(this, Home.class.getName()));
        fragmentList.add(Fragment.instantiate(this, CurrentRides.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Partners.class.getName()));
        fragmentList.add(Fragment.instantiate(this, RideHistory.class.getName()));
        fragmentList.add(Fragment.instantiate(this, FutureRideRequests.class.getName()));
        fragmentList.add(Fragment.instantiate(this, FutureRides.class.getName()));
        fragmentList.add(Fragment.instantiate(this, FutureHistoryRides.class.getName()));
        fragmentList.add(Fragment.instantiate(this, PaymentHistoryByWeek.class.getName()));
        fragmentList.add(Fragment.instantiate(this, MyRewardProgramFragment.class.getName()));
        fragmentList.add(Fragment.instantiate(this, BounsProgramFragment.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Settings.class.getName()));
        // fragmentList.add(Fragment.instantiate(this, Test.class.getName()));

        if (nid == 1) {
            getFragment(1);
        } else if (nid == 2) {
            getFragment(2);
        } else if (nid == 3) {
            getFragment(3);
        } else if (nid == 4) {
            getFragment(4);
        } else if (nid == 5) {
            getFragment(5);
        } else if (nid == 6) {
            getFragment(6);
        }  else if (nid == 9) {
            getFragment(9);
        } else {
            getFragment(0);
        }


        //ActionItemBadge.update(Navigation.this, menuChoose.findItem(R.id.item_bolo_alert_badge), FontAwesome.Icon.faw_user_plus, style, 2);


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }*/
        //scheduleJob();

        // Utils.startPowerSaverIntent(this);
        // Utils.ifHuaweiAlert(this);

    }

    private void scheduleJob() {
        JobScheduler jobScheduler;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            final ComponentName name = new ComponentName(this, AlarmJobService.class);
            final int result = jobScheduler.schedule(getJobInfo(123, name));

            //jobScheduler.btnCancelRide(123);

            if (result == JobScheduler.RESULT_SUCCESS) {
                Log.d("", "Scheduled Job Successfully!");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private JobInfo getJobInfo(int id, ComponentName name) {
        JobInfo jobInfo;
        long interval = 5000;
        boolean isPersistent = true;
        int networkType = JobInfo.NETWORK_TYPE_ANY;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }
        return jobInfo;
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("keyhash1234545666", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("", "printHashKey()", e);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fRide_Time.setVisibility(View.GONE);
        isNotifClick = false;
        if (id == R.id.nav_reservation) {
            title = getString(R.string.home);
            if (!fRide_Time.getText().toString().trim().equals(""))
                fRide_Time.setVisibility(View.VISIBLE);
            getFragment(0);
        }
        if (id == R.id.nav_drivertype) {
            title = getString(R.string.drivertype);
            getFragment(1);
        } else if (id == R.id.nav_partners) {
            title = getString(R.string.partners);
            getFragment(2);
        } else if (id == R.id.nav_friderequests) {
            title = getString(R.string.frequests);
            getFragment(3);
        } else if (id == R.id.nav_futurerides) {
            title = getString(R.string.futurerides);
            getFragment(4);
        } else if (id == R.id.nav_fhistory) {
             title = getString(R.string.fhistory);
             getFragment(5);
        } else if (id == R.id.nav_p_history) {
             title = getString(R.string.payment_history);
            getFragment(6);
        } else if (id == R.id.nav_address) {
            title = getString(R.string.phistory);
            getFragment(7);
        } else if (id == R.id.nav_my_reward_program) {
            title = getString(R.string.nav_my_reward_program);
            getFragment(8);
        } else if (id == R.id.nav_settings) {
            title = getString(R.string.settings);
            getFragment(9);
        } else if (id == R.id.nav_handbook) {
            title = getString(R.string.handbook);
            getFragment(10);
        } else if (id == R.id.nav_logout) {
            getFragment(11);
        } else if (id == R.id.nav_dbh_rides) {
            title = getString(R.string.dbh_rides);
            getFragment(12);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Fragment getFragment(Integer navItemIndex) {
        Fragment fragment = null;
        title = getString(R.string.app_name);
        Utils.logError(MyFirebaseMessagingService.TAG, navItemIndex + " k1 ");
    Log.e(TAG, "AAAAAAAAAAA");

        setUpdateStatusApi();
      //  callLogDrive();


        switch (navItemIndex) {
            case 0:
                // home
                title = getString(R.string.home);
                fragment = new Home();
                fragmenttransactions(fragment);
                break;
            case 1:
                // home
                title = getString(R.string.drivertype);
//                title = "<font color='#ffffff'>Driver&nbsp; </font><font color='#8bbc50'> Type</font>";
                fragment = new DriverType();
                fragmenttransactions(fragment);
                break;

            case 2:
                // photos
                title = getString(R.string.partners);
//                title = "<font color='#ffffff'>Manage&nbsp; </font><font color='#8bbc50'> Partners</font>";
                fragment = new Partners();
                fragmenttransactions(fragment);
                break;
//            case 4:
//                // movies fragment
//                title = getString(R.string.criderequests);
////                title = "<font color='#ffffff'>ASAP &nbsp; </font><font color='#8bbc50'> rides</font>";
//                fragment = new PendingCurrentRides();
//                fragmenttransactions(fragment);
//                break;

            case 3:
                // movies fragment
              //  Utils.logError(MyFirebaseMessagingService.TAG, navItemIndex + " ");
                title = getString(R.string.frequests);
//                title = "<font color='#ffffff'>Ride&nbsp; </font><font color='#8bbc50'> Reservations</font>";
                fragment = new FutureRideRequests();
                fragmenttransactions(fragment);
                break;

            case 4:
                // movies fragment
                title = getString(R.string.futurerides);
//                title = "<font color='#ffffff'> Accepted </font><font color='#ffffff'> Future</font><font color='#8bbc50'> Rides</font>";
                fragment = new FutureRides();
                fragmentTransactionsChat(fragment);
                break;



            case 5:
                // movies fragment//
                 title = getString(R.string.fhistory);
//                title = "<font color='#ffffff'>Ride&nbsp; </font><font color='#8bbc50'> History</font>";
                fragment = new RideHistory();
                fragmenttransactions(fragment);
                break;


            case 6:
                // movies fragment
                title = getString(R.string.payment_history);
//                title = "<font color='#ffffff'>Payment&nbsp; </font><font color='#8bbc50'> History</font>";
                fragment = new PaymentListHistory();
                fragmenttransactions(fragment);
                break;


            case 7:
                // movies fragment
                title = getString(R.string.phistory);
//                title = "<font color='#ffffff'>Payment&nbsp; </font><font color='#8bbc50'> History</font>";
                fragment = new PaymentHistoryByWeek();
                fragmenttransactions(fragment);
                break;


            case 8:
                // movies fragment
                title = getString(R.string.nav_my_reward_program);
//                title = "<font color='#ffffff'>My&nbsp; </font><font color='#8bbc50'> Reward</font>";
                fragment = new MyRewardProgramFragment();
                fragmenttransactions(fragment);
                break;

            case 9:
                // movies fragment
                title = getString(R.string.settings);
                fragment = new Settings();
                fragmenttransactions(fragment);
                break;




            case 10:
                // movies fragment//
                title = getString(R.string.handbook);
//                title = "<font color='#ffffff'>Ride&nbsp; </font><font color='#8bbc50'> History</font>";
                fragment = new WebFragment();
                fragmenttransactions(fragment);
//                String url = "https://lnc.latenightchauffeurs.com/lnc-administrator/admin/handbook_ios.php";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
                break;



            case 11:
                logoutRequest();
                break;

            case 12:
                title = getString(R.string.dbh_rides);
                fragment = new DbhAssignRides();
                fragmenttransactions(fragment);
                break;

            default:
                break;
        }

        return fragment;
    }





    private void setUpdateStatusApi() {

        call = apiInterface.activeOnlineStatus(pref1.getUserId(), "1", "yes");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();
                        Log.e(TAG, "responseCode " + responseCode);

                        JSONObject jsonObject = new JSONObject(responseCode);

                        String status = jsonObject.getString("status");

                        if(status.equals("4")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(jsonArray.length() > 0){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                if(jsonObject1.has("msg")){
                                    String msg = jsonObject1  .getString("msg");
                                    LayoutInflater inflater = LayoutInflater.from(Utils.context);
                                    final View dialogLayout = inflater.inflate(R.layout.alert_dialog66, null);
                                    final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(Utils.context).create();
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    dialog.setView(dialogLayout);
                                    dialog.show();

                                    //  final RelativeLayout rootLo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
                                    final TextView title = dialog.findViewById(R.id.title);
                                    final TextView textView = dialog.findViewById(R.id.desc);
                                    final Button buttonOk = dialog.findViewById(R.id.attend);

                                    textView.setText(msg);
                                    title.setText(R.string.app_name);
                                    // Utils.setFontStyle(context, buttonok);
                                    buttonOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                            final String appPackageName = getPackageName();
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            }
                                        }
                                    });
                                    dialog.show();
                                }

                            }

                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

               // Log.e(TAG , "itemCardsAAAA "+responseCode);

                //Toast.makeText(Navigation.this , responseCode, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
            }

        });
    }





    private void callLogDrive() {
       // Utils.initiatePopupWindow(Navigation.this, "Please wait request is in progress...");
        Call<ResponseBody> call = null;
        APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

        SavePref pref1 = new SavePref();
        pref1.SavePref(Navigation.this);
        String id = pref1.getUserId();

        call = apiInterface.logDriver(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (Utils.progressDialog != null) {
//                    Utils.progressDialog.dismiss();
//                    Utils.progressDialog = null;
//                }
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();
                        Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC: " + responseCode);

                        JSONObject object = new JSONObject(responseCode);
                        if(object.getString("status").equalsIgnoreCase("1")){



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
//                if (Utils.progressDialog != null) {
//                    Utils.progressDialog.dismiss();
//                    Utils.progressDialog = null;
//                }
                Log.e(TAG, "Refreshed getactiveRewardProgramsizeCCERR: " + t.getMessage());
            }
        });


    }



    public void logoutRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(Navigation.this);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Global.mapMain.put("type", "logout");
        Global.mapMain.put("status", "2");
        Global.mapMain.put("version", "yes");
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_DRIVER_ONLINE_REQUEST);

        if (Utils.isNetworkAvailable(Navigation.this)) {
            JsonPost.getNetworkResponse(Navigation.this, null, Global.mapMain, ConstVariable.Log_Out);
        } else {
            Utils.showInternetErrorMessage(Navigation.this);
        }
    }

    public void afterLogoutStatus() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(Navigation.this);
        pref1.clear();

        SharedPreferences preferences = getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        com.lncdriver.utils.Utils.toastTxt("You Log Out Successfully", Navigation.this);
        com.lncdriver.utils.Settings.NETWORK_STATUS = "";
        com.lncdriver.utils.Settings.USERNAME = "";
        com.lncdriver.utils.Utils.startActivity(Navigation.this, Login.class);
        finish();
    }

    public void fragmenttransactions(final Fragment fragment) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String backStateName = fragment.getClass().getName();
                    FragmentManager manager = getSupportFragmentManager();
                    getSupportActionBar().setTitle(title);

                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.frame, fragment, backStateName);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(backStateName);
                    //ft.detach(fragment);
                    //ft.attach(fragment);
                    getSupportActionBar().setTitle(Html.fromHtml(title));
                    ft.commit();
                    //android.app.Fragment fragment = getFragmentManager().findFragmentByTag(backStateName);
                    //Log.logError("frag___123",fragment.getTag());
                } catch (Exception e) {
                    Log.e("error123", e.getMessage());
                }
            }
        }, 250);
    }

    private void fragmentTransactionsChat(final Fragment fragment) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String backStateName = fragment.getClass().getName();
                    FragmentManager manager = getSupportFragmentManager();
                    getSupportActionBar().setTitle(title);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map", notifMap);
                    bundle.putBoolean(Utils.IS_NOTIFICATION_CLICK, isNotifClick);
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.frame, fragment, backStateName);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(backStateName);
                    ft.commit();

                } catch (Exception e) {
                    Log.e("error123", e.getMessage());
                }
            }
        }, 250);
    }

    @Override
    public void onFragmentInteraction(int page) {
        if (page == ConstVariable.Home) {
            title = getString(R.string.home);
            fragmenttransactions(fragmentList.get(0));
        }
        if (page == ConstVariable.DriverFutureRides) {
            title = getString(R.string.futurerides);
            fragmenttransactions(fragmentList.get(4));
        }
    }

    @Override
    public void sendData(HashMap<String, Object> smap) {
        try {
            String tag = Home.class.getName();
            Home f = (Home) getSupportFragmentManager().findFragmentById(R.id.frame);
            android.app.Fragment fragment = getFragmentManager().findFragmentByTag(tag);
            if (f != null)
                f.displayReceivedData(smap);
        } catch (Exception e) {
            // Log.logError("error123",logError.getMessage());
        }
    }

//    public static void assignServiceTime(JSONObject data) throws Exception {
//        if (data.has("time_left")) {
//            if (!data.getString("time_left").trim().toString().equalsIgnoreCase("")) {
//                fRide_Time.setVisibility(View.VISIBLE);
//                fRide_Time.setText(Html.fromHtml("Your next reservation is in :<br /> " + data.get("time_left").toString()));
//                fRide_Time.setTextColor(Color.WHITE);
//            } else {
//                fRide_Time.setVisibility(View.GONE);
//            }
//        }
//    }


    public static void assignNextServiceTime(JSONObject data) throws Exception {
        if (data.has("time_left")) {
            if (!data.getString("time_left").trim().equalsIgnoreCase("")) {
                fRide_Time.setVisibility(View.VISIBLE);
                fRide_Time.setText(Html.fromHtml("Your next reservation is in :<br /> " + data.get("time_left").toString()));
                fRide_Time.setTextColor(Color.WHITE);
            } else {
                fRide_Time.setVisibility(View.GONE);
            }
        }
    }



    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String isid = intent.getStringExtra("isid");



                if (isid.equalsIgnoreCase("9")) {
                    String status = intent.getStringExtra("status");

                    map = new HashMap<>();
                    map = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newride")) {
                        if (map.containsKey("message") && !map.get("message").toString().equalsIgnoreCase(""))
                            showMessage(Navigation.this, map);

                        startService(new Intent(Navigation.this, SoundService.class));
                    }
                    if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("btnCancelRide")) {
                        getFragment(0);
                        if (map != null && map.size() > 0) {
                            if (map.containsKey("reason")) {
                                if (map.get("reason").toString().equalsIgnoreCase("")) {
                                    Utils.showCancelRideStatusDialog(map.get("message").toString(),
                                            "", Navigation.this);
                                } else {
                                    Utils.showCancelRideStatusDialog(map.get("message").toString(),
                                            map.get("reason").toString(), Navigation.this);
                                }
                            }
                        }
                    }
                    if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("Cancel future ride")) {
                        getFragment(0);
                        if (map != null && map.size() > 0) {
                            if (map.containsKey("reason")) {
                                if (map.get("reason").toString().equalsIgnoreCase("")) {
                                    Utils.showCancelRideStatusDialog(map.get("message").toString(),
                                            "", Navigation.this);
                                } else {
                                    Utils.showCancelRideStatusDialog(map.get("message").toString(),
                                            map.get("reason").toString(), Navigation.this);
                                }
                            }
                        }
                    }
                    if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("complete")) {
                        getFragment(0);
                        if (map != null && map.size() > 0) {
                            if (map.containsKey("message") && !map.get("message").toString().equalsIgnoreCase("")) {
                                Utils.toastTxt(map.get("message").toString(), Navigation.this);
                            }
                        }
                    }



                    if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("richnotification")) {

                        Log.e(TAG , "richnotification DDDD ");

                        updateBadge();
                    }

                }


            }
        }
    };

    public void showMessage(final Context mcontext, final HashMap<String, Object> map) {
        try {
            mtimer = new CountDownTimer(15000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval

                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                    if (txtTimner != null) {
                        txtTimner.setText(String.valueOf(seconds));
                    }
                }

                public void onFinish() {
                    HashMap<String, Object> dmap = null;

                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mcontext);
                    String cmap = pref1.getridemap();

                    if (!cmap.equalsIgnoreCase("")) {
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                        }.getType();
                        dmap = gson.fromJson(cmap, type);
                    }
                    if (mtimer != null) {
                        stopService(new Intent(Navigation.this, SoundService.class));
                        mtimer.cancel();
                    }

                    if (dmap.containsKey("type") && dmap.get("type").toString().equalsIgnoreCase("driver")) {
                        DriverAcceptRequest(mcontext, dmap, "2");
                    } else if (dmap.containsKey("type") && dmap.get("type").toString().equalsIgnoreCase("btnPartnerInfo")) {
                        PartnerAcceptRequest(mcontext, dmap, "2");
                    }

                    if (dialog != null)
                        dialog.cancel();
                }
            };

            String data = "";

            if (map != null) {
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                View dialogLayout = inflater.inflate(R.layout.alert_dialog4, null);
                dialog = new androidx.appcompat.app.AlertDialog.Builder(mcontext).create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setView(dialogLayout);
                dialog.show();

                // RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
                TextView title = dialog.findViewById(R.id.title);
                TextView rideTitle = dialog.findViewById(R.id.rtitle);
                TextView textView = dialog.findViewById(R.id.desc);
                TextView car_transmission = dialog.findViewById(R.id.car_transmission);
                txtTimner = dialog.findViewById(R.id.timertxt);
                ImageView gif = dialog.findViewById(R.id.imggif);
                Button attend = dialog.findViewById(R.id.attend);
                Button reject = dialog.findViewById(R.id.cancel);

                Glide.with(this)
                        .load(R.raw.loading)
                        .into(gif);

                if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newride")) {
                    String s = "", e = "";

                    if (map.containsKey("start_location") && !map.get("start_location").toString().equalsIgnoreCase(""))
                        s = getColoredSpanned(map.get("start_location").toString(), "#800000");

                    if (map.containsKey("end_location") && !map.get("end_location").toString().equalsIgnoreCase(""))
                        e = getColoredSpanned(map.get("end_location").toString(), "#F69625");
                    data = map.get("message").toString() + "<br />" + " From : " + s + "<br />  to  <br />" + e;
                }

                if (map.containsKey("type") && !map.get("type").toString().equalsIgnoreCase("")) {
                    rideTitle.setText(map.get("type_data").toString());
                }

                if (map.containsKey("type") && map.get("type").toString().equalsIgnoreCase("driver")) {
                    car_transmission.setVisibility(View.VISIBLE);

                    if (map.containsKey("transmission") && map.get("transmission").toString().equalsIgnoreCase("manual"))
                        car_transmission.setText("Car Is Manual Transmission");
                    else
                        car_transmission.setVisibility(View.GONE);

                } else if (map.containsKey("type") && map.get("type").toString().equalsIgnoreCase("btnPartnerInfo")) {
                    car_transmission.setVisibility(View.VISIBLE);
                }

                textView.setText(Html.fromHtml(data));
                title.setText(R.string.app_name);

                attend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (mtimer != null) {
                                mtimer.cancel();
                                stopService(new Intent(Navigation.this, SoundService.class));
                            }
                            if (map.containsKey("type") && map.get("type").toString().equalsIgnoreCase("driver")) {
                                DriverAcceptRequest(mcontext, map, "1");
                            } else if (map.containsKey("type") && map.get("type").toString().equalsIgnoreCase("btnPartnerInfo")) {
                                PartnerAcceptRequest(mcontext, map, "1");
                            }
                            dialog.dismiss();
                        } catch (Exception e) {

                        }
                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> dmap;

                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        String id = pref1.getUserId();

                        String cmap = pref1.getridemap();

                        if (!cmap.equalsIgnoreCase("")) {
                            Gson gson = new Gson();
                            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                            }.getType();
                            dmap = gson.fromJson(cmap, type);
                        }

                        if (mtimer != null) {
                            stopService(new Intent(Navigation.this, SoundService.class));
                            mtimer.cancel();
                        }

                        if (map.containsKey("type") && map.get("type").toString().equalsIgnoreCase("driver")) {
                            DriverAcceptRequest(mcontext, map, "2");
                        } else if (map.containsKey("type") && map.get("type").toString().equalsIgnoreCase("btnPartnerInfo")) {
                            PartnerAcceptRequest(mcontext, map, "2");
                        }
                        dialog.cancel();
                    }
                });
                // stopService(new Intent(getBaseContext(), MyService.class));
                startService(new Intent(Navigation.this, SoundService.class));
                dialog.setCancelable(false);
                dialog.show();
                mtimer.start();
            }
        } catch (Exception e) {
            Log.e("error123", e.getMessage());
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static void DriverAcceptRequest(Context mcontext, HashMap<String, Object> vmap, String status) {
        if (status.equalsIgnoreCase("2")) {
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            pref1.setridemap("");
            pref1.setisnewride("");
        }

        smap = new HashMap<>();
        smap.put("status", status);
        smap.put("ride_id", vmap.get("ride_id").toString());
        OnlineRequest.DriverAcceptRequest(mcontext, smap);
    }

    public static void PartnerAcceptRequest(Context mcontext, HashMap<String, Object> vmap, String status) {
        if (status.equalsIgnoreCase("2")) {
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            pref1.setridemap("");
            pref1.setisnewride("");
        }

        smap = new HashMap<>();
        smap.put("status", status);
        smap.put("ride_id", vmap.get("ride_id").toString());
        OnlineRequest.partnerAcceptRequest(mcontext, smap);
    }

    public void driverOnlineRequest(String status) {
        new Utils(Navigation.this);
        map = new HashMap<>();
        map.put("status", status);
        OnlineRequest.driverOnlineRequest(Navigation.this, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(Navigation.this , LocationBackgroundService.class));
        unregisterReceiver(mHandleMessageReceiver);
        Utils.logError("101", "destroy");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mtimer != null) {
            mtimer.cancel();
        }

        if (dialog != null) {
            dialog.cancel();
        }
        stopService(new Intent(Navigation.this, SoundService.class));
        driverOnlineRequest("2");

        Utils.logError("100", "stop");

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
        }

        // int i=getSupportFragmentManager().getBackStackEntryCount();

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            Utils.logError("108", "onBackPressed finished");

            if (back) {
                finish();
            } else {
                Utils.toastTxt("Click  again to exit.", Navigation.this);
            }
            back = true;
        } else {
            Utils.logError("112", "onBackPressed");
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteractionpendingCurrentRides(int page, HashMap<String, Object> map) {
        Fragment fragment = null;
        Bundle bundle = null;

        if (page == ConstVariable.Home) {
            fragment = Home.newInstance();

            if (map != null) {
                bundle = new Bundle();
                bundle.putSerializable("map", map);
                fragment.setArguments(bundle);
            }
            fragmenttransactions(fragment);
        }
    }





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ic_menu_notifications, menu);
        menuChoose = menu;

        updateBadge();


        return true;
    }



    private void updateBadge() {
        SavePref pref = new SavePref();
        pref.SavePref(Navigation.this);

        if(pref.getBadgeCount() > 0){
            Drawable myDrawable = getResources().getDrawable(R.mipmap.msg_i);
            ActionItemBadge.update(Navigation.this, menuChoose.findItem(R.id.item_bolo_alert_badge), myDrawable, style, pref.getBadgeCount());
        }else{
            Drawable myDrawable = getResources().getDrawable(R.mipmap.msg_i);
            ActionItemBadge.update(Navigation.this, menuChoose.findItem(R.id.item_bolo_alert_badge), myDrawable, style, null);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_bolo_alert_badge:
                startActivityForResult(new Intent(Navigation.this, Notifications.class), REFRESH_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REFRESH_REQUEST){
            updateBadge();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        SavePref pref1 = new SavePref();
//        pref1.SavePref(Navigation.this);
            startService(new Intent(Navigation.this , LocationBackgroundService.class));
    }



}
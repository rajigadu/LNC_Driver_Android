package com.lncdriver.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lncdriver.R;
import com.lncdriver.adapter.ChatAdapter;
import com.lncdriver.model.ModelItem;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manjeet on 8/23/2017.
 */

public class ActivityDriverChat extends AppCompatActivity {
    public static String TAG = ActivityDriverChat.class.getName();

    public static Context mcontext;
    public static RecyclerView rv_chat;
    public static TextView noData;
    public static boolean isDataLoad = false;
    public static ChatAdapter chatAdapter;
    public static RecyclerView.LayoutManager chatMan;
    public static List<ModelItem> chatList;
    public HashMap<String, Object> map;
    public ImageView image;
    public TextView name;
    TextView title;
    ImageView back;
    EditText message;
    static String role = "";
    Button send;
    // public Handler handler;
    // public Runnable runnable;
    public static String receiverid = "";
    public static HashMap<String, Object> dmap;

    private boolean isNotifClick = false;

    String backPress = "";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandleMessageReceiver != null)
            unregisterReceiver(mHandleMessageReceiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverchat);
        rv_chat = findViewById(R.id.rv_chat);
        noData = findViewById(R.id.nodata);
        name = findViewById(R.id.name);
        back = findViewById(R.id.back);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        title.setVisibility(View.VISIBLE);
        title.setText("Chat");


//        dmap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");


//        Log.e(TAG , "ROLE1 "+getIntent().hasExtra("role"));
//        Log.e(TAG , "rid1 "+getIntent().hasExtra("rid"));
//
//
//        Log.e(TAG , "dmap1 "+dmap.toString());


//        if (getIntent().hasExtra(Utils.IS_NOTIFICATION_CLICK)) {
//            isNotifClick = getIntent().getBooleanExtra(Utils.IS_NOTIFICATION_CLICK, isNotifClick);
//        }
//        if (isNotifClick) {
//            dmap = (HashMap<String, Object>) getIntent().getSerializableExtra(Utils.IS_NOTIF_MAP);
//            role = dmap.get("type").toString();
//            receiverid = dmap.get("partner_id").toString();
//            //{driver_id=19, partner_id=3, message=aa, type=driver, ride=newpartnermessage}
//        } else {
            if (getIntent().hasExtra("role")) {
                role = getIntent().getStringExtra("role");
                receiverid = getIntent().getStringExtra("rid");
                Log.e(TAG , "AAAAAAAA "+receiverid);
            } else if (getIntent().hasExtra("map")) {
                dmap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
                role = "driver";

                if (dmap.containsKey("partnerid")){
                    receiverid = dmap.get("partnerid").toString();
                    Log.e(TAG , "BBBBBBBB "+receiverid);
                }

            }



        if (getIntent().hasExtra("back")) {
            backPress = getIntent().getStringExtra("back");
        }


//        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        mcontext = ActivityDriverChat.this;

        chatMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_chat.setHasFixedSize(true);
        rv_chat.setLayoutManager(chatMan);

        /*  handler = new Handler();
         runnable = new Runnable()
         {
            @Override
            public void run()
            {
                chatListRequest();
                handler.postDelayed(this,5000);
            }
        };
        handler.postDelayed(runnable,5000);*/
        chatListRequest();

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        pref1.setisnewmsg("");

        Log.e(TAG , "XXXXXXXXXXXXXX");


        registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
    }

    public static void chatListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("senderid", id);
        Utils.global.mapMain.put("recieverid", receiverid);
        Utils.global.mapMain.put("msg", "");
        Utils.global.mapMain.put("type", role);
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_DRIVERCHAT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DriverChatList);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public void submit() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(ActivityDriverChat.this);

        if (message.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter message .", ActivityDriverChat.this);
        } else if (message.getText().toString().length() > 250) {
            Utils.toastTxt(" Maximum message length 250 Charecters.", ActivityDriverChat.this);
        } else {
            new Utils(ActivityDriverChat.this);
            Utils.global.mapMain();
            Utils.global.mapMain.put("senderid", id);
            Utils.global.mapMain.put("recieverid", receiverid);
            Utils.global.mapMain.put("msg", message.getText().toString().trim());
            Utils.global.mapMain.put("type", "driver");

            Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_DRIVERCHAT);

            if (Utils.isNetworkAvailable(ActivityDriverChat.this)) {
                message.setText("");
                JsonPost.getNetworkResponse(ActivityDriverChat.this, null, Utils.global.mapMain, ConstVariable.DriverChat);
            } else {
                Utils.showInternetErrorMessage(ActivityDriverChat.this);
            }
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.logError(TAG+"81", "load browseMembersList Data "+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.logError(TAG+"88", "browseMembersList new");
                chatList = new ArrayList<ModelItem>();

                //Utils.logError(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if (!chatList.contains(mp)) {
                        chatList.add(new ModelItem(mp));
                    }
                }
                //Utils.logError(TAG+"118", "browseMembersList"+eventList.size());
            } catch (Exception e) {
                //Utils.logError(TAG+"122", "Exception======================Exception======================Exception");
                e.printStackTrace();
            } finally {
                // Utils.logError(TAG+"127", "browseMembersList "+eventList.size());
                //Utils.logError(TAG+"128", "ok");
                if (!mode.equalsIgnoreCase("update")) {
                    setAdapterFriendsRequestList(context);
                } else {
                    chatAdapter.notifyItemInserted(chatList.size());
                    chatAdapter.notifyDataSetChanged();
                }
            }
            chatMan.setAutoMeasureEnabled(true);
        } else {
            rv_chat.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rv_chat != null) {
            rv_chat.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }

        //Utils.logError(TAG+"156", "setAdapter ok "+eventList);
        chatAdapter = new ChatAdapter(mcontext, chatList, rv_chat, R.layout.chat_rowitem, ConstVariable.UserChat);
        //set the adapter object to the Recyclerview
        //Utils.logError(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_chat.setAdapter(chatAdapter);
        rv_chat.scrollToPosition(chatList.size() - 1);
    }

    @Override
    public void onBackPressed() {


        if(backPress.equalsIgnoreCase("back")){
            Intent i = new Intent(ActivityDriverChat.this, Navigation.class);
            //i.putExtra("keyPosition" , 1);
            startActivity(i);
            finishAffinity();
            finish();
        }else{
            super.onBackPressed();
            Utils.hideSoftKeyboard(this);
        }


    }

    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG , "aaaaaisidAAA");

            if (intent != null) {
                Log.e(TAG , "aaaaaisidBBB");
                String isid = intent.getStringExtra("isid");

                if (isid.equalsIgnoreCase("9")) {
                    String status = intent.getStringExtra("status");

                    HashMap<String, Object> data = new HashMap<>();
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newusermessage")) {
                        Log.e(TAG , "aaaaaisidCCC");

                        chatListRequest();
                    }
                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newpartnermessage")) {
                        chatListRequest();
                    }
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideSoftKeyboard(this);
    }
}

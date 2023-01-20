package com.lncdriver.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.adapter.ChatAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manjeet on 8/23/2017.
 */

public class ActivityPartnerChat extends AppCompatActivity {
    public static String TAG = ActivityPartnerChat.class.getName();

    public static Context mcontext;
    public static RecyclerView rv_chat;
    public static TextView noData;
    public static ChatAdapter chatAdapter;
    public static RecyclerView.LayoutManager chatMan;
    public static List<ModelItem> chatList;
    public HashMap<String, Object> map;
    public ImageView image;
    public TextView name;
    ImageView back;
    TextView title;
    EditText message;
    static String role = "";
    Button send;
    public static String receiverid = "";
    public static HashMap<String, Object> dmap;

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
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        noData = (TextView) findViewById(R.id.nodata);
        name = (TextView) findViewById(R.id.name);
        back = (ImageView) findViewById(R.id.back);
        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        title.setVisibility(View.VISIBLE);
        title.setText("Chat");



        Log.e(TAG , "ROLE1 "+getIntent().hasExtra("role"));
        Log.e(TAG , "rid1 "+getIntent().hasExtra("rid"));

        //------------------------------------------------------------
        if (getIntent().hasExtra("role")) {
            role = getIntent().getStringExtra("role");
            receiverid = getIntent().getStringExtra("rid");
        } else if (getIntent().hasExtra("map")) {
            dmap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
            role = "btnPartnerInfo";
            if (dmap.containsKey("id"))
                receiverid = dmap.get("id").toString();
        }

        Log.e(TAG , "dmap1 "+dmap.toString());


        //------------------------------------------------------------


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent= new Intent(ActivityLogin.this,ActivityNavigation.class);
                //startActivity(intent);
                // handler.removeCallbacks(runnable);
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        mcontext = ActivityPartnerChat.this;
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
        Global.mapMain.put("senderid", id);
        Global.mapMain.put("recieverid", receiverid);
        Global.mapMain.put("msg", "");
        Global.mapMain.put("type", role);
        Global.mapMain.put(ConstVariable.URL, Settings.URL_DRIVERCHAT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.PartnerChatList);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public void submit() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(ActivityPartnerChat.this);




        //------------------------------------------------------------

        if (message.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter message .", ActivityPartnerChat.this);
        } else if (message.getText().toString().length() > 250) {
            Utils.toastTxt(" Maximum message length 250 Charecters.", ActivityPartnerChat.this);
        } else {
            new Utils(ActivityPartnerChat.this);
            Utils.global.mapMain();
            Global.mapMain.put("senderid", id);
            Global.mapMain.put("recieverid", receiverid);
            Global.mapMain.put("msg", message.getText().toString().trim());
            Global.mapMain.put("type", "driver");

            Global.mapMain.put(ConstVariable.URL, Settings.URL_DRIVERCHAT);

            if (Utils.isNetworkAvailable(ActivityPartnerChat.this)) {
                message.setText("");
                JsonPost.getNetworkResponse(ActivityPartnerChat.this, null, Global.mapMain, ConstVariable.PartnerChat);
            } else {
                Utils.showInternetErrorMessage(ActivityPartnerChat.this);
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
        super.onBackPressed();
        Utils.hideSoftKeyboard(this);
    }

    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e(TAG , "aaaaaisidAAA");

            if (intent != null) {


                Log.e(TAG , "aaaaaisidBBB");


                String isid = intent.getStringExtra("isid");

                Log.e(TAG , "aaaaaisidXXX");


                if (isid.equalsIgnoreCase("9")) {
                    Log.e(TAG , "aaaaaisidWWWW");
                    String status = intent.getStringExtra("status");
                    Log.e(TAG , "aaaaaisidEEEE");
                    HashMap<String, Object> data = new HashMap<>();
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    Log.e(TAG , "aaaaaisidYYYYY "+data);

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newusermessage")) {
                        Log.e(TAG , "aaaaaisidCCC");
                        chatListRequest();
                    }
                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newpartnermessage")) {
                        Log.e(TAG , "aaaaaisidDDD");
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

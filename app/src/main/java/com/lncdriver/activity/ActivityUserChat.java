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

import android.text.Html;
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

public class ActivityUserChat extends AppCompatActivity {
    public static String TAG = ActivityUserChat.class.getName();

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
    ImageView back;
    TextView title;
    EditText message;
    Button send;
    // public Handler handler;
    // public Runnable runnable;
    public static HashMap<String, Object> dmap;


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
        setContentView(R.layout.activity_userchat);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        noData = (TextView) findViewById(R.id.nodata);
        name = (TextView) findViewById(R.id.name);
        back = (ImageView) findViewById(R.id.back);
        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        title.setVisibility(View.VISIBLE);
        title.setText("UserChat");
        title.setText(Html.fromHtml("<font color='#ffffff'> User&nbsp; </font><font color='#8bbc50'>Chat</font>"));

        if (getIntent() != null) {
            dmap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
        }


        if (getIntent().hasExtra("back")) {
            backPress = getIntent().getStringExtra("back");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent= new Intent(ActivityLogin.this,ActivityNavigation.class);
                //startActivity(intent);
                // handler.removeCallbacks(runnable);
//                if(backPress.equalsIgnoreCase("back")){
//                    Intent i = new Intent(ActivityUserChat.this, Navigation.class);
//                    //i.putExtra("keyPosition" , 1);
//                    startActivity(i);
//                    finishAffinity();
//                    finish();
//                }else{
//                    finish();
//                }
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        mcontext = ActivityUserChat.this;

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

        registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideSoftKeyboard(this);
    }

    public static void chatListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        String did = "";



//        if (dmap != null)
//            did = dmap.get("userid").toString();
//        else
//            did = pref1.getCustomerId();



//        if (dmap != null){
//            if(dmap.containsKey("userid")){
//                id = dmap.get("userid").toString();
//            }
//        }
//
//
//        if(dmap.containsKey("driverid")){
//            did = dmap.get("driverid").toString();
//        }else{
//            did = pref1.getCustomerId();
//        }



        if (dmap != null){
            if(dmap.containsKey("user_id")){
                did = dmap.get("user_id").toString();
                Log.e(TAG, "AAAAA: "+did);
            }

            if(dmap.containsKey("userid")){
                did = dmap.get("userid").toString();
                Log.e(TAG, "BBBBB: "+did);
            }
        }
        else{
            did = pref1.getDriverId();
            Log.e(TAG, "CCCCC: "+did);
        }


        Log.e(TAG, "id1: "+id);
        Log.e(TAG, "did1: "+did);



        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("senderid", id);
        Global.mapMain.put("recieverid", did);
        Global.mapMain.put("msg", "");
        Global.mapMain.put("keyvalue", "driver");
        Global.mapMain.put(ConstVariable.URL, Settings.URL_USERCHAT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Global.mapMain, ConstVariable.UserChatList);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public void submit() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        String did = "";

//        if (dmap != null)
//            did = dmap.get("userid").toString();
//        else
//            did = pref1.getCustomerId();

        //
        if (dmap != null){
            if(dmap.containsKey("user_id")){
                did = dmap.get("user_id").toString();
                Log.e(TAG, "AAAAA: "+did);
            }

            if(dmap.containsKey("userid")){
                did = dmap.get("userid").toString();
                Log.e(TAG, "BBBBB: "+did);
            }
        }
        else{
            did = pref1.getDriverId();
            Log.e(TAG, "CCCCC: "+did);
        }




        new Utils(ActivityUserChat.this);

        if (message.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter message .", ActivityUserChat.this);
        } else if (message.getText().toString().length() > 250) {
            Utils.toastTxt(" Maximum message length 250 Charecters.", ActivityUserChat.this);
        } else {
            new Utils(ActivityUserChat.this);
            Utils.global.mapMain();
            Global.mapMain.put("senderid", id);
            Global.mapMain.put("recieverid", did);
            Global.mapMain.put("msg", message.getText().toString().trim());
            Global.mapMain.put("keyvalue", "driver");

            Global.mapMain.put(ConstVariable.URL, Settings.URL_USERCHAT);

            if (Utils.isNetworkAvailable(ActivityUserChat.this)) {
                message.setText("");
                JsonPost.getNetworkResponse(ActivityUserChat.this, null, Global.mapMain, ConstVariable.UserChat);
            } else {
                Utils.showInternetErrorMessage(ActivityUserChat.this);
            }
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.e(TAG+"81", "load browseMembersList Data "+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.e(TAG+"88", "browseMembersList new");
                chatList = new ArrayList<ModelItem>();

                //Utils.e(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if (!chatList.contains(mp)) {
                        chatList.add(new ModelItem(mp));
                    }
                }
                //Utils.e(TAG+"118", "browseMembersList"+eventList.size());
            } catch (Exception e) {
                //Utils.e(TAG+"122", "Exception======================Exception======================Exception");
                e.printStackTrace();
            } finally {
                // Utils.e(TAG+"127", "browseMembersList "+eventList.size());
                //Utils.e(TAG+"128", "ok");
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

        //Utils.e(TAG+"156", "setAdapter ok "+eventList);
        chatAdapter = new ChatAdapter(mcontext, chatList, rv_chat, R.layout.chat_rowitem, ConstVariable.UserChat);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_chat.setAdapter(chatAdapter);
        rv_chat.scrollToPosition(chatList.size() - 1);
    }


    @Override
    public void onBackPressed() {
        // handler.removeCallbacks(runnable);
        if(backPress.equalsIgnoreCase("back")){
            Intent i = new Intent(ActivityUserChat.this, Navigation.class);
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
            if (intent != null) {
                String isid = intent.getStringExtra("isid");

                if (isid.equalsIgnoreCase("9")) {
                    String status = intent.getStringExtra("status");

                    HashMap<String, Object> data = new HashMap<>();
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newusermessage")) {
                        chatListRequest();
                    }
                }
            }
        }
    };
}

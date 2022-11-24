package com.lncdriver.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.lncdriver.R;
import com.lncdriver.activity.ActivityDriverChat;
import com.lncdriver.activity.ActivityPartnerChat;
import com.lncdriver.activity.ActivityUserChat;
import com.lncdriver.activity.Navigation;
import com.lncdriver.activity.Notifications;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnaadIT on 3/30/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0)

            Log.e(TAG, "title Message========remoteMessage: " + remoteMessage);

            if (remoteMessage.getData().get("data") != null && !remoteMessage.getData().get("data").toString().equalsIgnoreCase("")) {
                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().get("data").toString());
                    String title = json.getString("title").toString();
                    JSONArray jarray = json.optJSONArray("body");

                    if (jarray != null) {
                        Utils.global.mapMain = Utils.GetJsonDataIntoMap(getApplicationContext(), jarray, "");
                    }

                    Log.e(TAG, "title Message========123456: " + title);
                    Log.e(TAG, "body map Message========1234567: " + Utils.global.mapMain);

                    SavePref pref = new SavePref();
                    pref.SavePref(getApplicationContext());

                    String userid = pref.getUserId();

                    if(!userid.equals("")){
                        sendNotification(title, Utils.global.mapMain);
                    }


                } catch (JSONException e) {
                    Log.e("error1233", e.getMessage());
                    e.printStackTrace();
                }
            }


    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title, HashMap<String, Object> map) {
        // Log.logError(TAG, " Message Title123456: " +title);
        // Log.logError(TAG, " Message Body123456: " +body);

        // String msg="";
        Intent intent = null;
        boolean isNotif = false;

        int maxValue = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);




        Log.e(TAG , "swap_rol_partner1111 "+map.get("ride").toString());


        if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newride")) {
            Log.e(TAG, "AAAAAAAAAAAA000000000 "+map.get("ride").toString());
//            intent = new Intent(this, Navigation.class);
//            Navigation.nid = 4;
        } else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("currentride_pending")) {
            Log.e(TAG, "AAAAAAAAAAAA111111111 "+map.get("ride").toString());
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 4;
            Utils.logError(TAG, "PlayStore Notif currentride_pending");
        } else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("future_notification")) {
            Log.e(TAG, "AAAAAAAAAAAA222222222 "+map.get("ride").toString());
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 3;
            Utils.logError(TAG, "PlayStore Notif Future Reservation");
        } else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("assign_future_reservation_driver")) {
            Log.e(TAG, "AAAAAAAAAAAA222222222 "+map.get("ride").toString());
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 4;
            com.lncdriver.fragment.FutureRides.id = 0;
            Utils.logError(TAG, "PlayStore Notif Future Reservation");
        }else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("assign_future_reservation_partner")) {
            Log.e(TAG, "AAAAAAAAAAAA222222222 "+map.get("ride").toString());
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 4;
            com.lncdriver.fragment.FutureRides.id = 1;
            Utils.logError(TAG, "PlayStore Notif Future Reservation");
        }else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newdrivermessage")) {
            intent = new Intent(this, ActivityDriverChat.class);
            intent.putExtra("back", "back");
        } else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newpartnermessage")) {
            intent = new Intent(this, ActivityDriverChat.class);
            intent.putExtra("back", "back");
        }else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newusermessage")) {
            intent = new Intent(this, ActivityUserChat.class);
            intent.putExtra("back", "back");
        }





        else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("swap_rol_partner")) {
            Log.e(TAG , "swap_rol_partner");
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 4;
            com.lncdriver.fragment.FutureRides.id = 1;
        }
        else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("swap_rol_driver")) {
            Log.e(TAG , "swap_rol_driver");
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 4;
            com.lncdriver.fragment.FutureRides.id = 0;
        }

        else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("richnotification")) {
            intent = new Intent(this, Notifications.class);
            intent.putExtra("back", "back");

            SavePref pref = new SavePref();
            pref.SavePref(getApplicationContext());

            int ii = pref.getBadgeCount();

            Log.e(TAG, "QQQQiii "+ii);

            int xx = ii + 1;

            Log.e(TAG, "QQQQxx "+xx);


            pref.setBadgeCount(xx);

        }else {
            Log.e(TAG, "PlayStore Notif else");
            Navigation.nid = 0;

            intent = new Intent(this, Navigation.class);
        }







      /* if(map.containsKey("ride")&&map.get("ride").toString().equalsIgnoreCase("newusermessage"))
        {
            intent = new Intent(this,ActivityDriverChat.class);
        }
        else if(map.containsKey("ride")&&map.get("ride").toString().equalsIgnoreCase("newdrivermessage"))
        {
            intent = new Intent(this,ActivityDriverChat.class);
        }
        else if(map.containsKey("ride")&&map.get("ride").toString().equalsIgnoreCase("newpartnermessage"))
        {
            intent = new Intent(this,ActivityPartnerChat.class);
        }
        else
        {
            intent = new Intent(this,Navigation.class);
        }*/

        intent.putExtra("map", map);
        intent.putExtra(Utils.IS_NOTIFICATION_CLICK, isNotif);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        String body = "";

        if (map.containsKey("message") && !map.get("message").toString().equalsIgnoreCase(""))
            body = map.get("message").toString();

        bigText.bigText(body);

        SavePref pref1 = new SavePref();
        pref1.SavePref(this);

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "test";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.appicon);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.appicon)
                .setStyle(bigText)
                .setLargeIcon(icon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setContentText(body);

        Notification notification = notificationBuilder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(mChannel);

        notificationManager.notify(maxValue, notification);

        if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newride")) {
            pref1.SavePref(getApplicationContext());

            Gson gson = new Gson();
            String hashMapString = gson.toJson(map);
            pref1.setridemap(hashMapString);
            pref1.setisnewride("newride");
            //{driver_id=3, message=hlo, rideInf=newpartnermessage}
        }

        pref1.SavePref(getApplicationContext());

        Gson gson = new Gson();
        String hashMapString = gson.toJson(map);
        pref1.setridemap(hashMapString);

        HashMap<String, Object> dmap = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (!value.toString().equalsIgnoreCase("null")) {
                dmap.put(key, value);
            }
        }


        Log.e(TAG , "qqqqqqqqqqqq");

        Intent broadcast = new Intent();
        broadcast.setAction("OPEN_NEW_ACTIVITY");
        broadcast.putExtra("isid", "9");
        broadcast.putExtra("status", "9");
        broadcast.putExtra("data", (Serializable) dmap);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
        sendBroadcast(broadcast);
    }
}
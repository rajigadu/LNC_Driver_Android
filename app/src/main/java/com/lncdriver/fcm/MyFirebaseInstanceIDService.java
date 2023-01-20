package com.lncdriver.fcm;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by AnaadIT on 3/30/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService
{
    public  int i=0;
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + token);

        if ((token != null || token != "") && i == 0)
            sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token)
    {
        /*SavePref pref1 = new SavePref();
        pref1.SavePref(getApplicationContext());
        pref1.setdToken(token);*/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("lncdrivertoken", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("tokenid", token);
        editor.apply();

        // Log.e(TAG, "db token token123456: " +pref1.getdToken());
        i=1;
    }
}

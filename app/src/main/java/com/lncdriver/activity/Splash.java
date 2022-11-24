package com.lncdriver.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by narayana on 10/25/2017.
 */

public class Splash extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        handler = new Handler();

        try {
            new GetLatestVersion().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    private class GetLatestVersion extends AsyncTask<String, String, String> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String newVersion = null;

            try {
               /* String urlOfAppFromPlayStore ="https://play.google.com/store/apps/details?id=com.qwikride";
                Document  doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();
*/
                Document document = Jsoup.connect("http://play.google.com/store/apps/details?id=" + Splash.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                                //Log.e("latestversion___123",newVersion);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("error1231234567", e.getMessage());
                e.printStackTrace();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            update(s);
        }
    }

    private String getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        return currentVersion;
    }

    public void update(String latestVersion) {
        String currentVersion = getCurrentVersion();
        // Log.e("haystack", "Current version = " + currentVersion);

        //If the versions are not the same
        if (latestVersion != null && !currentVersion.equals(latestVersion)) {
            // Log.e("version1234567",latestVersion);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("An Update is Available");
            builder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Click button action
                    startActivityIfNeeded(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Splash.this.getPackageName())),2);
                    dialog.dismiss();
                }
            });


            builder.setCancelable(false);
            builder.show();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SavePref pref1 = new SavePref();
                    pref1.SavePref(Splash.this);

                    String email = pref1.getEmail();
                    // String id = pref1.getUserId();

                    if (!email.equalsIgnoreCase("")) {
                        Utils.startActivity(Splash.this, Navigation.class);
                    } else {
                        Utils.startActivity(Splash.this, Login.class);
                    }
                    finish();
                }
            }, 3000);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            Bundle extras = intent.getExtras();
            switch(requestCode)
            {
                case 1:
                    break;
                case 2:
                    break;
            }
        }
        else if (resultCode==RESULT_CANCELED)
        {
//            try
//            {
//                new GetLatestVersion().execute().get();
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//            catch (ExecutionException e)
//            {
//                e.printStackTrace();
//            }
        }

    }



}


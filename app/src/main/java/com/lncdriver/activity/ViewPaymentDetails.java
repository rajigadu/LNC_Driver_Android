package com.lncdriver.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/21/2018.
 */

public class ViewPaymentDetails extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.pname)
    TextView name;

    @BindView(R.id.pemail)
    TextView email;

    @BindView(R.id.pmobile)
    TextView mobile;

    @BindView(R.id.damount)
    TextView dAmount;

    @BindView(R.id.dtip)
    TextView dTip;

    @BindView(R.id.pamount)
    TextView pAmount;

    @BindView(R.id.ptip)
    TextView pTip;

    @BindView(R.id.title)
    TextView title;

    //HashMap<String, Object> map;

    String partnerName = "", partnerEmail = "", partnerPhone = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpaymentdetails);
        ButterKnife.bind(this);

        back.setOnClickListener(this);

        title.setText("Self Partner Details");

        //map = new HashMap<>();

        if (getIntent() != null) {
            partnerName = getIntent().getExtras().getString(Utils.PARTNER_NAME);
            partnerEmail = getIntent().getExtras().getString(Utils.PARTNER_EMAIL);
            partnerPhone = getIntent().getExtras().getString(Utils.PARTNER_PHONE);

            //map = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
            populatePaymentData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    public void populatePaymentData() {
        if (partnerName != null && !partnerName.trim().equalsIgnoreCase("")) {
            String s = "", e = "";

            s = getColoredSpanned("Parter Name : ", "#99000000");
            e = getColoredSpanned(partnerName.toString(), "#FFFFFF");

            name.setText(Html.fromHtml(s + e));
        } else {
            name.setVisibility(View.GONE);
        }

        if (partnerEmail != null && !partnerEmail.toString().equalsIgnoreCase("")) {
            String s = "", e = "";

            s = getColoredSpanned("Partner Email : ", "#99000000");

            e = getColoredSpanned(partnerEmail.toString(), "#FFFFFF");

            email.setText(Html.fromHtml(s + e));
        } else {
            email.setVisibility(View.GONE);
        }

        if (partnerPhone != null && !partnerPhone.toString().equalsIgnoreCase("")) {
            String s = "", e = "";

            s = getColoredSpanned("Partner Mobile : ", "#99000000");

            e = getColoredSpanned(partnerPhone.toString(), "#FFFFFF");

            mobile.setText(Html.fromHtml(s + e));
        } else {
            mobile.setVisibility(View.GONE);
        }

        /*
        if (map.containsKey("drivers_amount") && !map.get("drivers_amount").toString().equalsIgnoreCase("")) {
            String s = "", e = "";

            s = getColoredSpanned("Driver Amount : ", "#99000000");

            if (map.containsKey("drivers_amount") && !map.get("drivers_amount").toString().equalsIgnoreCase(""))
                e = getColoredSpanned(map.get("drivers_amount").toString() + "$", "#FFFFFF");

            dAmount.setText(Html.fromHtml(s + e));
        } else {
            dAmount.setVisibility(View.GONE);
        }

        if (map.containsKey("drivers_tip") && !map.get("drivers_tip").toString().equalsIgnoreCase("")) {
            String s = "", e = "";

            s = getColoredSpanned("Parter Tip : ", "#99000000");

            if (map.containsKey("drivers_tip") && !map.get("drivers_tip").toString().equalsIgnoreCase(""))
                e = getColoredSpanned(map.get("drivers_tip").toString() + "$", "#FFFFFF");

            dTip.setText(Html.fromHtml(s + e));
        } else {
            dTip.setVisibility(View.GONE);

        }

        if (map.containsKey("partners_amount") && !map.get("partners_amount").toString().equalsIgnoreCase("")) {
            String s = "", e = "";

            s = getColoredSpanned("Parter Amount : ", "#99000000");

            if (map.containsKey("partners_amount") && !map.get("partners_amount").toString().equalsIgnoreCase(""))
                e = getColoredSpanned(map.get("partners_amount").toString() + "$", "#FFFFFF");

            pAmount.setText(Html.fromHtml(s + e));
        } else {
            pAmount.setVisibility(View.GONE);
        }

        if (map.containsKey("partners_tip") && !map.get("partners_tip").toString().equalsIgnoreCase("")) {
            String s = "", e = "";

            s = getColoredSpanned("Parter Tip : ", "#99000000");

            if (map.containsKey("partners_tip") && !map.get("partners_tip").toString().equalsIgnoreCase(""))
                e = getColoredSpanned(map.get("partners_tip").toString() + "$", "#FFFFFF");

            pTip.setText(Html.fromHtml(s + e));
        } else {
            pTip.setVisibility(View.GONE);
        }*/
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}

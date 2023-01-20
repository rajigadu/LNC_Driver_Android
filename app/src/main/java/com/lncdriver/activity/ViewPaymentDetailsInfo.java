package com.lncdriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/21/2018.
 */

public class ViewPaymentDetailsInfo extends AppCompatActivity implements View.OnClickListener {
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

    @BindView(R.id.tv_Customer_Name)
    TextView tvCustomerName;

    @BindView(R.id.tv_pick_up)
    TextView tvPickUp;

    @BindView(R.id.tv_drop_off)
    TextView tvDropOff;

    @BindView(R.id.tv_transaction_id)
    TextView tvTransactionId;

    @BindView(R.id.tvPartnerInfo)
    TextView tvPartnerInfo;

    //HashMap<String, Object> map;

    String partnerName = "", partnerEmail = "", partnerPhone = "", pickUpAddress = "", dropAddress,
            transactionId, customerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpaymentdetails_info);
        ButterKnife.bind(this);

        back.setOnClickListener(this);

        title.setText("Ride Info");

        //map = new HashMap<>();

        if (getIntent() != null) {
            customerName = getIntent().getExtras().getString(Utils.CUSTOMER_NAME);
            pickUpAddress = getIntent().getExtras().getString(Utils.PICK_UP);
            dropAddress = getIntent().getExtras().getString(Utils.DROP_OFF);
            transactionId = getIntent().getExtras().getString(Utils.TRANSACTION_ID);
            tvPartnerInfo.setVisibility(View.GONE);
            if (getIntent().getExtras().getString(Utils.PARTNER_NAME) != null) {
                partnerName = getIntent().getExtras().getString(Utils.PARTNER_NAME);
                partnerEmail = getIntent().getExtras().getString(Utils.PARTNER_EMAIL);
                partnerPhone = getIntent().getExtras().getString(Utils.PARTNER_PHONE);
                tvPartnerInfo.setVisibility(View.VISIBLE);
            }

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

        if (customerName != null && !customerName.equalsIgnoreCase("")) {
            String s = "", e = "";
            s = getColoredSpanned("Customer Name : ", "#8000000");
            e = getColoredSpanned(customerName, "#99000000");
            tvCustomerName.setText(Html.fromHtml(s + e));
        } else {
            tvCustomerName.setVisibility(View.GONE);
        }

        if (pickUpAddress != null && !pickUpAddress.equalsIgnoreCase("")) {
            String s = "", e = "";
            s = getColoredSpanned("Pick Up : ", "#8000000");
            e = getColoredSpanned(pickUpAddress, "#99000000");
            tvPickUp.setText(Html.fromHtml(s + e));
        } else {
            tvPickUp.setVisibility(View.GONE);
        }

        if (transactionId != null && !transactionId.equalsIgnoreCase("")) {
            String s = "", e = "";
            s = getColoredSpanned("Transaction Id : ", "#8000000");
            e = getColoredSpanned(transactionId, "#99000000");
            tvTransactionId.setText(Html.fromHtml(s + e));
        } else {
            tvTransactionId.setVisibility(View.GONE);
        }

        if (dropAddress != null && !dropAddress.equalsIgnoreCase("")) {
            String s = "", e = "";
            s = getColoredSpanned("Drop Off : ", "#8000000");
            e = getColoredSpanned(dropAddress, "#99000000");
            tvDropOff.setText(Html.fromHtml(s + e));
        } else {
            tvDropOff.setVisibility(View.GONE);
        }

        if (partnerName != null && !partnerName.trim().equalsIgnoreCase("")) {
            String s = "", e = "";
            s = getColoredSpanned("Parter Name : ", "#8000000");
            e = getColoredSpanned(partnerName, "#99000000");
            name.setText(Html.fromHtml(s + e));
        } else {
            name.setVisibility(View.GONE);
        }

        if (partnerEmail != null && !partnerEmail.equalsIgnoreCase("")) {
            String s = "", e = "";
            s = getColoredSpanned("Partner Email : ", "#8000000");
            e = getColoredSpanned(partnerEmail, "#99000000");
            email.setText(Html.fromHtml(s + e));
        } else {
            email.setVisibility(View.GONE);
        }

        if (partnerPhone != null && !partnerPhone.equalsIgnoreCase("")) {
            String s = "", e = "";
            s = getColoredSpanned("Partner Mobile : ", "#8000000");
            e = getColoredSpanned(partnerPhone, "#99000000");
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

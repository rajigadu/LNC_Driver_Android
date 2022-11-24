package com.lncdriver.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/2/2018.
 */

public class AddBankAccount extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.fname)
    EditText fname;

    @BindView(R.id.lname)
    EditText lname;

    @BindView(R.id.accnno)
    EditText accnNumber;
    @BindView(R.id.accnno_one)
    EditText accnNumber_Conf;

    @BindView(R.id.routingnum)
    EditText routingNum;

   /* @BindView(R.id.npc)
    EditText npc;*/

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.et_bank_number)
    EditText etBankName;

   /* @BindView(R.id.type)
    EditText type;*/

    @BindView(R.id.submit)
    Button submit;

    public static AddBankAccount Instance;

    HashMap<String, Object> map;
    JSONObject json;

    public List<String> mAreas;
    public String mType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbaccount);
        ButterKnife.bind(this);
        Instance = this;
        title.setText("Bank Account");
        title.setText(Html.fromHtml("<font color='#ffffff'> Bank&nbsp; </font><font color='#8bbc50'> Account</font>"));
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        //type.setOnClickListener(this);
        mAreas = new ArrayList<>();
        mAreas.add("Entry");
        mAreas.add("Check In");
        getBankDetailsRequest();
    }

    public void submit() {
        new Utils(AddBankAccount.this);

        if (fname.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please Enter First name.", AddBankAccount.this);
        } else if (lname.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please Enter Last name.", AddBankAccount.this);
        } else if (etBankName.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please Enter Bank Name", AddBankAccount.this);
        } else if (mobile.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please Enter Mobile no.", AddBankAccount.this);
        } else if (accnNumber.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please Enter A/c Number..", AddBankAccount.this);
        } else if (accnNumber_Conf.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please Enter confirm A/C Number.", AddBankAccount.this);
        } else if (!accnNumber.getText().toString().trim().equalsIgnoreCase(accnNumber_Conf.getText().toString().trim())) {
            Utils.toastTxt("Account Numbers are not matched.", AddBankAccount.this);
        } else if (routingNum.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please Enter Routing no.", AddBankAccount.this);
        }

      /*  else if(npc.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("NPC Account.",AddBankAccount.this);
        }
        else if(type.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Select account type.",AddBankAccount.this);
        }*/
        else {
            SavePref pref1 = new SavePref();
            pref1.SavePref(AddBankAccount.this);
            String id = pref1.getUserId();

            json = new JSONObject();

            try {
                json.put(ConstVariable.DRIVER_ID, id);
                json.put("fname", fname.getText().toString());
                json.put("lname", lname.getText().toString());
                json.put("bankName", etBankName.getText().toString());
                json.put("accnumber", accnNumber.getText().toString());
                json.put("mobile", mobile.getText().toString());
                json.put("routingnumber", routingNum.getText().toString());
                // json.put("npcAccount",npc.getText().toString());
                //json.put("type",type.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            map = new HashMap<>();
            map.put("json", json);
            OnlineRequest.addBankAccountRequest(AddBankAccount.this, map);
        }
    }

    public void pickAccountType() {
        new Utils(AddBankAccount.this);
        final CharSequence[] items;
        items = mAreas.toArray(new CharSequence[mAreas.size()]);
        if (items.length > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddBankAccount.this);
            builder.setTitle("Account Type");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    mType = items[item].toString();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void getBankDetailsRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(this);
        String id = pref1.getUserId();

        new Utils(this);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driverid", id);
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_EDIT_BANK_ACCOUNT);

        if (Utils.isNetworkAvailable(AddBankAccount.this)) {
            JsonPost.getNetworkResponse(AddBankAccount.this, null, Utils.global.mapMain,
                    ConstVariable.EdtBankAccount);
        } else {
            Utils.showInternetErrorMessage(AddBankAccount.this);
        }
    }

    public void populateBankDetails(HashMap<String, Object> map) {
        if (map != null) {
            if (map.containsKey("fname") && !map.get("fname").toString().equalsIgnoreCase("")
                    && !map.get("fname").toString().equalsIgnoreCase("null")) {
                fname.setText(map.get("fname").toString());
            }

            if (map.containsKey("lname") && !map.get("lname").toString().equalsIgnoreCase("")
                    && !map.get("lname").toString().equalsIgnoreCase("null")) {
                lname.setText(map.get("lname").toString());
            }

            if (map.containsKey("mobile") && !map.get("mobile").toString().equalsIgnoreCase("")
                    && !map.get("mobile").toString().equalsIgnoreCase("null")) {
                mobile.setText(map.get("mobile").toString());
            }

            if (map.containsKey("accnumber") && !map.get("accnumber").toString().equalsIgnoreCase("")
                    && !map.get("accnumber").toString().equalsIgnoreCase("null")) {
                accnNumber.setText(map.get("accnumber").toString());
                accnNumber_Conf.setText(map.get("accnumber").toString());
            }

            if (map.containsKey("routingnumber") && !map.get("routingnumber").toString().equalsIgnoreCase("")
                    && !map.get("routingnumber").toString().equalsIgnoreCase("null")) {
                routingNum.setText(map.get("routingnumber").toString());
            }

            if (map.containsKey("bank_name") && !map.get("bank_name").toString().equalsIgnoreCase("")
                    && !map.get("bank_name").toString().equalsIgnoreCase("null")) {
                etBankName.setText(map.get("bank_name").toString());
            }

            /*if(map.containsKey("npcAccount")&& !map.get("npcAccount").toString().
               equalsIgnoreCase("")&& !map.get("npcAccount").toString().equalsIgnoreCase("null")) {
                npc.setText(map.get("npcAccount").toString());
            }

            if(map.containsKey("type")&& !map.get("type").toString().
             equalsIgnoreCase("")&& !map.get("type").toString().equalsIgnoreCase("null")) {
              type.setText(map.get("type").toString());
            }*/

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type:
                pickAccountType();
                break;

            case R.id.back:
                finish();
                break;

            case R.id.submit:
                submit();
                break;
        }

    }

    public void closeactivity() {
        finish();
    }
}

package com.lncdriver.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Login.class.getSimpleName();

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.signin)
    Button signin;

    @BindView(R.id.signup)
    TextView signup;

    @BindView(R.id.forgot)
    TextView forgot;

    @BindView(R.id.noaccount)
    TextView tvNoAccount;

    HashMap<String, Object> map;
    public static Login Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Instance = this;
        signin.setOnClickListener(Login.this);
        signup.setOnClickListener(Login.this);
        forgot.setOnClickListener(Login.this);
        tvNoAccount.setOnClickListener(Login.this);
        tvNoAccount.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                Utils.startActivity(Login.this, SignUp.class);
                break;
            case R.id.forgot:
                Utils.startActivity(Login.this, Forgot.class);
                break;
            case R.id.signin:
                submit();
                break;
            case R.id.noaccount:
                signup.performClick();
                break;
        }
    }

    public void submit() {
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        new Utils(Login.this);

        if (email.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter email address.", Login.this);
        } else if (!Utils.isValidEmail(email.getText().toString().trim())) {
            Utils.toastTxt("Please enter valid email address.", Login.this);
        } else if (password.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter password.", Login.this);
        } else {
            map = new HashMap<>();
            map.put("emailid", email.getText().toString());
            map.put("password", password.getText().toString());
            OnlineRequest.loginRequest(Login.this, map);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void closeActivity() {
        finish();
    }

    public void registrationIDtoServer() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("lncdrivertoken", 0);
        SharedPreferences.Editor editor = pref.edit();

        String tId = pref.getString("tokenid", null);

        SavePref pref1 = new SavePref();
        pref1.SavePref(Login.this);
        String id = pref1.getUserId();

        if (tId != "") {
            map = new HashMap<>();
            map.put(ConstVariable.USERID, id);
            map.put("devicetoken", tId);
            OnlineRequest.deviceTokenRequest(Login.this, map);
        }
    }
}

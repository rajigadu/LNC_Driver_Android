package com.lncdriver.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AnaadIT on 3/16/2017.
 */

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.newpassword)
    EditText newpassword;

    @BindView(R.id.oldpassword)
    EditText oldpassword;

    @BindView(R.id.cpassword)
    EditText cpassword;

    @BindView(R.id.updatepassword)
    Button updatepassword;

    public static ChangePassword Instance;
    HashMap<String, Object> map;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ButterKnife.bind(this);
        Instance = this;

        back.setOnClickListener(this);
        updatepassword.setOnClickListener(this);
        title.setText("Change Password");
        title.setText(Html.fromHtml("<font color='#ffffff'> Change&nbsp; </font><font color='#8bbc50'> Password</font>"));

    }

    public void successPassword() {
        finish();
    }

    public void submit() {
        new Utils(ChangePassword.this);

        if (oldpassword.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter old password.", ChangePassword.this);
        } else if (newpassword.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter new password.", ChangePassword.this);
        } else if (cpassword.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter confirm password again.", ChangePassword.this);
        } else if (!cpassword.getText().toString().trim().equalsIgnoreCase(newpassword.getText().toString().trim())) {
            Utils.toastTxt("Password and confirm password must be same.", ChangePassword.this);
        } else {
            map = new HashMap<>();
            map.put("oldpassword", oldpassword.getText().toString());
            map.put("newpassword", cpassword.getText().toString());
            OnlineRequest.changePawwordRequest(ChangePassword.this, map);
            oldpassword.setText("");
            newpassword.setText("");
            cpassword.setText("");
        }
    }

    public void closeactivity() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.updatepassword:
                try {
                    submit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}

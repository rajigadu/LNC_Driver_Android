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
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AnaadIT on 3/16/2017.
 */

public class ContactUs extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.message)
    EditText message;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.updateprofile)
    Button updateProfile;

    public static ContactUs Instance;
    HashMap<String, Object> map;
    public int id = 0;

    public static String TAG = ContactUs.class.getName();
    public ImageView imageupload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        ButterKnife.bind(this);

        Instance = this;

       /* back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        message = (EditText) findViewById(R.id.message);
        updateProfile = (Button) findViewById(R.id.updateprofile);*/

        back.setOnClickListener(this);
        updateProfile.setOnClickListener(this);

        back.setVisibility(View.VISIBLE);
        title.setText("Contact Us");
        title.setText(Html.fromHtml("<font color='#ffffff'> Contact&nbsp; </font><font color='#8bbc50'> Us</font>"));
    }

    public void submit() {
        new Utils(ContactUs.this);
        if (name.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter name.", ContactUs.this);
        } else if (email.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter email address.", ContactUs.this);
        } else if (!Utils.isValidEmail(email.getText().toString().trim())) {
            Utils.toastTxt("Please enter valid email address.", ContactUs.this);
        } else if (message.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter your query message.", ContactUs.this);
        } else {
            map = new HashMap<>();
            map.put("fullname", name.getText().toString());
            map.put(ConstVariable.EMAIL, email.getText().toString());
            map.put("message", message.getText().toString());
            OnlineRequest.contactusRequest(ContactUs.this, map);
        }
    }

    public void closeactivity() {
       // Navigation.nid = 3;
        Utils.startActivity(ContactUs.this, Navigation.class);
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
            case R.id.updateprofile:
                submit();
                break;
        }
    }
}



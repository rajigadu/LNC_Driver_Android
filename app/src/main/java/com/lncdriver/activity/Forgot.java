package com.lncdriver.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Forgot extends AppCompatActivity {
    public static Forgot Instance;

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.resetnow)
    Button restnow;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    @OnClick(R.id.resetnow)
    void funresetnow() {
        submit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Instance = this;

        ButterKnife.bind(this);

        // back=(ImageView) findViewById(R.id.back);
        //title=(TextView) findViewById(R.id.title);
        title.setText("Forgot Password");
        title.setText(Html.fromHtml("<font color='#ffffff'> Forgot&nbsp; </font><font color='#8bbc50'> Password</font>"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void submit() {
        new Utils(Forgot.this);

        if (email.getText().toString().trim().isEmpty()) {
            Utils.showOkDialog("Please enter email address.", Forgot.this);
        } else if (!Utils.isValidEmail(email.getText().toString().trim())) {
            Utils.showOkDialog("Please enter valid email address.", Forgot.this);
        } else {
            Utils.global.mapMain();
            Global.mapMain.put("email", email.getText().toString().trim());
            Global.mapMain.put(ConstVariable.URL, Settings.URL_FORGETPASSWORD);

            if (Utils.isNetworkAvailable(Forgot.this)) {
                JsonPost.getNetworkResponse(Forgot.this, null, Global.mapMain, ConstVariable.ForgetPassword);
            } else {
                Utils.showInternetErrorMessage(Forgot.this);
            }
        }
    }

    public void closeActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {
        //finishAffinity();
        super.onBackPressed();
    }
}

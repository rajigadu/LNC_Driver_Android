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
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lncdriver.R;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPartner extends AppCompatActivity implements View.OnClickListener {
    public String type = "";

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.submit)
    Button submit;

    HashMap<String, Object> map;
    public static Context mcontext;
    public static AddPartner Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpartner);
        ButterKnife.bind(this);

        mcontext = this;
        Instance = this;

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submit();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void submit() {
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        new Utils(AddPartner.this);

        if (name.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter first name.", AddPartner.this);
        } else if (mobile.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter mobile number.", AddPartner.this);
        } else if (email.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter email address.", AddPartner.this);
        } else if (!Utils.isValidEmail(email.getText().toString().trim())) {
            Utils.toastTxt("Please enter valid email address.", AddPartner.this);
        } else {
            map = new HashMap<>();
            map.put("name", name.getText().toString());
            map.put("mobile", mobile.getText().toString());
            map.put("email", email.getText().toString());

            OnlineRequest.addPartnerRequest(AddPartner.this, map);
        }
    }

    public void closeActivity() {
        finish();

    }

}

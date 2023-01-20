package com.lncdriver.activity;

import android.content.Context;
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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/16/2018.
 */

public class CancelRide extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.message)
    EditText message;

    String rId = "";
    public static CancelRide Instance;
    HashMap<String, Object> map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelride);
        ButterKnife.bind(this);

        Instance = this;

        title.setText("Ride Cancel");
        title.setText(Html.fromHtml("<font color='#ffffff'> Ride&nbsp; </font><font color='#8bbc50'> Cancel</font>"));

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);

        if (getIntent() != null) {
            rId = getIntent().getStringExtra("rideid");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                cancelCurrentRideRequest(CancelRide.this, rId);
                break;
        }
    }

    public void cancelCurrentRideRequest(Context mcontext, String rid) {
        map = new HashMap<>();
        map.put("ride_id", rid);
        map.put("reason", message.getText().toString());
        OnlineRequest.cancelUserRidRequest(CancelRide.this, map);

    }

    public void closeActivity() {
        finish();
    }
}

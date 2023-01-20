 package com.lncdriver.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.msg)
    EditText msg;

    @BindView(R.id.dname)
    TextView driverName;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.user_image)
    ImageView pic;

    @BindView(R.id.rating)
    RatingBar rating;

    HashMap<String, Object> map;
    public static RatingActivity Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackalert);
        ButterKnife.bind(this);
        Instance = this;
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        title.setText("Feedback");
        if (getIntent().hasExtra("map")) {
            map = new HashMap<>();
            map = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
            Utils.logError("Msggggg", map.toString());
            if (map.containsKey("photo")) {
                if (!map.get("photo").toString().equalsIgnoreCase(""))
                    Picasso.with(RatingActivity.this).load(Settings.URLIMAGEBASE +
                            map.get("photo").toString()).placeholder(R.drawable.appicon).into(pic);
            }
            StringBuilder sb = new StringBuilder();
            if (map.containsKey("first_name")) {
                if (!map.get("first_name").toString().equalsIgnoreCase(""))
                    sb.append(map.get("first_name").toString());
            }
            if (map.containsKey("last_name")) {
                sb.append(" ");

                if (!map.get("last_name").toString().equalsIgnoreCase(""))
                    sb.append(map.get("last_name").toString());
                driverName.setText(sb);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submit:
                submitFeedback();
                break;

            case R.id.back:
                finish();
                break;

        }
    }

    public void submitFeedback() {
        float x = 0.0f;
        if (rating.getRating() == x) {
            Utils.toastTxt("Please provide a rating.", Instance);
        } else {
            getFeedbackRequest(msg.getText().toString(), String.valueOf(rating.getRating()));
        }
    }

    public void getFeedbackRequest(String msg, String rating) {
        double r = Double.parseDouble(rating);
        int rateToUser = (int) r;
        new Utils(Instance);
        Utils.global.mapMain();
        if (map != null) {
            Global.mapMain.put("driverid", map.get("driver_id").toString());
            Global.mapMain.put("userid", map.get("user_id").toString());
            Global.mapMain.put("msg", msg);
            Global.mapMain.put("rating", rateToUser);
            Global.mapMain.put(ConstVariable.URL, Settings.URL_FEED_BACK);
            if (Utils.isNetworkAvailable(Instance)) {
                JsonPost.getNetworkResponse(Instance, null, Global.mapMain,
                        ConstVariable.User_Feed_Back);
            } else {
                Utils.showInternetErrorMessage(this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}

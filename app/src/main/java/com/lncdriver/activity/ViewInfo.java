package com.lncdriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.lncdriver.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/15/2018.
 */

public class ViewInfo extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener
{
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewinfo);
        ButterKnife.bind(this);

        back.setOnClickListener(this);

        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {


    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case  R.id.back:
                finish();
                break;
        }
    }
}

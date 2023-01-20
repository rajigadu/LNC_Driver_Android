package com.lncdriver;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;

public class Demo2 extends AppCompatActivity {

    private static final String TAG = "Demo2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_swipe);

        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipe);

        //swipeLayout.setSwipeEnabled(false);
//        swipeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              //  swipeLayout.setSw
//            }
//        });


        swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLayout.open(true);
            }
        });

    }

}
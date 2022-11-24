package com.lncdriver.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AditionalStops extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();


    RecyclerView recyclerViewHistory;
    VOHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aditional_stops);

        ButterKnife.bind(this);

        back.setOnClickListener(this);

        back.setVisibility(View.VISIBLE);
        title.setText("Aditional Stops");
        title.setText(Html.fromHtml("<font color='#ffffff'> Additional&nbsp; </font><font color='#8bbc50'> Stops</font>"));


        recyclerViewHistory = (RecyclerView) findViewById(R.id.actionable_items);

        recyclerViewHistory.setHasFixedSize(true);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(AditionalStops.this , LinearLayoutManager.VERTICAL,false));
        //voHistoryList.clear();
        historyAdapter = new VOHistoryAdapter(AditionalStops.this, arrayList);
        recyclerViewHistory.setAdapter(historyAdapter);





        Bundle bundle = getIntent().getExtras();

        String sss = bundle.getString("key");

        try{
            JSONArray jsonArray = new JSONArray(sss);

            if(jsonArray.length() > 0){
                for(int i = 0 ; i < jsonArray.length() ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String id = jsonObject.getString("id");
                    String ride_id = jsonObject.getString("ride_id");
                    String location = jsonObject.getString("location");

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id" , id);
                    map.put("ride_id" , ride_id);
                    map.put("location" , location);

                    arrayList.add(map);

                }
            }


        }catch (Exception e){

        }





        historyAdapter.updateData(arrayList);



    }












    public class VOHistoryAdapter extends RecyclerView.Adapter<VOHistoryAdapter.ViewHolder> {

        private static final String TAG = "VOHistoryAdapter";

        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        Activity context;


        public VOHistoryAdapter(Activity context11, ArrayList<HashMap<String, Object>> arrayList1) {
            super();
            this.context = context11;
            this.arrayList = arrayList1;

        }

        @Override
        public VOHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.aditional_stops_item, viewGroup, false);
            return new VOHistoryAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final VOHistoryAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.bindItems(arrayList.get(i), i, context);
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view11 = null;

            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
            }


            public void bindItems(final HashMap<String, Object> itemSettings, int i, final Activity context) {


                TextView textViewLocation = (TextView) itemView.findViewById(R.id.itemaa);

               textViewLocation.setText(""+itemSettings.get("location").toString());

            }

        }



        public void updateData(ArrayList<HashMap<String, Object>> arrayList2) {
            // TODO Auto-generated method stub
            //arrayList.clear();
//            alName.addAll(arrayList2);
            arrayList = arrayList2;
            notifyDataSetChanged();
        }



    }







    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}

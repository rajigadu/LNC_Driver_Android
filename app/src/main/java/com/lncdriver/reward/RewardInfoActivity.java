package com.lncdriver.reward;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.ParsingHelper;
import com.lncdriver.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardInfoActivity extends AppCompatActivity {

    TextView textViewTitle;


    private static final String TAG = "MyRewardProgramFragment";
    SavePref pref1 = new SavePref();


    public Call<ResponseBody> call = null;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

    Activity activity;

    RecyclerView recyclerViewMyRewardProgram;
    MyRewardProgramAdapter historyAdapter;

    TextView textViewMsg, textViewTotalPoints;
    ProgressBar progressBar;

    ImageView imageViewBack;

    ArrayList<ItemHistoryRewardProgram> arrayList = new ArrayList<ItemHistoryRewardProgram>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reward_info_activity);

        textViewTitle = (TextView) findViewById(R.id.title);

        textViewTitle.setText("History");


        textViewTotalPoints = (TextView) findViewById(R.id.textView_total_points);
        textViewMsg = (TextView) findViewById(R.id.textView123124);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1444);

        imageViewBack = (ImageView) findViewById(R.id.back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewMyRewardProgram = (RecyclerView) findViewById(R.id.actionable_items);
        recyclerViewMyRewardProgram.setHasFixedSize(true);
        recyclerViewMyRewardProgram.setLayoutManager(new LinearLayoutManager(activity , LinearLayoutManager.VERTICAL,false));
        historyAdapter = new MyRewardProgramAdapter(RewardInfoActivity.this, arrayList);
        recyclerViewMyRewardProgram.setAdapter(historyAdapter);


        pref1.SavePref(RewardInfoActivity.this);
        // String id = pref1.getUserId();

        Bundle bundle = getIntent().getExtras();

        advertise(bundle.getString("getId"), bundle.getString("getDriver_time"));
    }



    private void advertise(String program_id1, String driver_time1) {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<MultipartBody.Part> arrayListMash = new ArrayList<MultipartBody.Part>();
//        MultipartBody.Part key1 = MultipartBody.Part.createFormData("", pref1.getUserId());
//        arrayListMash.add(key1);

        MultipartBody.Part userid = MultipartBody.Part.createFormData("driver_id", pref1.getUserId());
        arrayListMash.add(userid);

        MultipartBody.Part program_id = MultipartBody.Part.createFormData("program_id", program_id1);
        arrayListMash.add(program_id);

        MultipartBody.Part driver_time = MultipartBody.Part.createFormData("driver_time", driver_time1);
        arrayListMash.add(driver_time);

      //  Log.e(TAG , "keyQQQ "+key);

        call = apiInterface.historyRewardProgramList(arrayListMash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();

                        Log.e(TAG, "Refreshed historyRewardProgramList: " + responseCode);


                        JSONObject jsonObject = new JSONObject(responseCode);
                        String status = jsonObject.getString("status");

                        arrayList = new ParsingHelper().getHistoryRewardProgramList(responseCode);
                        Log.e(TAG, "Refreshed getHistoryRewardProgramList: " + arrayList.size());

                        if(status.equals("1")){
                            textViewMsg.setVisibility(View.GONE);
                            String total = jsonObject.getString("total");
                            textViewTotalPoints.setText("Total Points: "+total);
                        }else{
                            textViewMsg.setVisibility(View.VISIBLE);
                            String message = jsonObject.getString("msg");
                            textViewMsg.setText(""+message);
                        }

                        historyAdapter.updateData(arrayList);



                    }else{

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);

            }
        });




    }







    public class MyRewardProgramAdapter extends RecyclerView.Adapter<MyRewardProgramAdapter.ViewHolder> {

        private static final String TAG = "MyRewardProgramAdapter";

        ArrayList<ItemHistoryRewardProgram> arrayList = new ArrayList<ItemHistoryRewardProgram>();

        Activity context;

        ImageLoader imageLoader;
        DisplayImageOptions options;


        public MyRewardProgramAdapter(Activity context11, ArrayList<ItemHistoryRewardProgram> arrayList1) {
            super();
            this.context = context11;
            this.arrayList = arrayList1;

            imageLoader = (ImageLoader) Utils.getImageLoaderDefaultCar(context11).get("imageLoader");
            options = (DisplayImageOptions) Utils.getImageLoaderDefaultCar(context11).get("options");
        }

        @Override
        public MyRewardProgramAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.history_reward_program_item, viewGroup, false);
            return new MyRewardProgramAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MyRewardProgramAdapter.ViewHolder viewHolder, final int i) {
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


            public void bindItems(final ItemHistoryRewardProgram itemMyRewardProgram, int i, final Activity context) {

                TextView textViewDate = (TextView) itemView.findViewById(R.id.text_date);
                TextView textViewPickup = (TextView) itemView.findViewById(R.id.text_pickup);
                TextView textViewDropoff = (TextView) itemView.findViewById(R.id.text_dropoff);
                TextView textViewMiles = (TextView) itemView.findViewById(R.id.text_miles);
                TextView textViewRewardPoint = (TextView) itemView.findViewById(R.id.text_reward_point);

//                Button buttonActivate = (Button) itemView.findViewById(R.id.button_active);
//
//
                textViewDate.setText("Date & Time: "+itemMyRewardProgram.getOtherdate()+" "+itemMyRewardProgram.getTime());
                textViewPickup.setText("PickUp: "+itemMyRewardProgram.getPickup_address());
                textViewDropoff.setText("Drop Off: "+itemMyRewardProgram.getDrop_address());
                textViewMiles.setText("Miles: "+itemMyRewardProgram.getDistance());
                textViewRewardPoint.setText("Reward Points: "+itemMyRewardProgram.getPoint());




            }

        }



        public void updateData(ArrayList<ItemHistoryRewardProgram> arrayList2) {
            // TODO Auto-generated method stub
            //arrayList.clear();
//            alName.addAll(arrayList2);
            arrayList = arrayList2;
            notifyDataSetChanged();
        }



    }





}

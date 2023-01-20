package com.lncdriver.reward;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


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
public class MyRewardProgramFragment extends Fragment {

    private static final String TAG = "MyRewardProgramFragment";
    SavePref pref1 = new SavePref();


    public Call<ResponseBody> call = null;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

    Activity activity;

    RecyclerView recyclerViewMyRewardProgram;
    MyRewardProgramAdapter historyAdapter;

    TextView textViewMsg;
    ProgressBar progressBar;


    ArrayList<ItemMyRewardProgram> arrayList = new ArrayList<ItemMyRewardProgram>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_reward_program_fragment, container, false);
        return view;
    }



    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        activity = getActivity();

        textViewMsg = (TextView) v.findViewById(R.id.textView123124);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar1444);


        recyclerViewMyRewardProgram = (RecyclerView) v.findViewById(R.id.actionable_items);
        recyclerViewMyRewardProgram.setHasFixedSize(true);
        recyclerViewMyRewardProgram.setLayoutManager(new LinearLayoutManager(activity , LinearLayoutManager.VERTICAL,false));
        historyAdapter = new MyRewardProgramAdapter(getActivity(), arrayList);
        recyclerViewMyRewardProgram.setAdapter(historyAdapter);

        pref1.SavePref(getActivity());
       // String id = pref1.getUserId();

        advertise();


    }







    private void advertise() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<MultipartBody.Part> arrayListMash = new ArrayList<MultipartBody.Part>();
//        MultipartBody.Part key1 = MultipartBody.Part.createFormData("", pref1.getUserId());
//        arrayListMash.add(key1);

        MultipartBody.Part userid = MultipartBody.Part.createFormData("driver_id", pref1.getUserId());
        arrayListMash.add(userid);


        call = apiInterface.myRewardProgramList(arrayListMash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();

                        Log.e(TAG, "Refreshed getMyRewardresponseCode: " + responseCode);

                        JSONObject jsonObject = new JSONObject(responseCode);
                        String status = jsonObject.getString("status");

                        arrayList = new ParsingHelper().getMyRewardProgramList(responseCode);
                        Log.e(TAG, "Refreshed getMyRewardProgramList: " + arrayList.size());

                        if(status.equals("1")){
                            textViewMsg.setVisibility(View.GONE);
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

        ArrayList<ItemMyRewardProgram> arrayList = new ArrayList<ItemMyRewardProgram>();

        Activity context;

        ImageLoader imageLoader;
        DisplayImageOptions options;


        public MyRewardProgramAdapter(Activity context11, ArrayList<ItemMyRewardProgram> arrayList1) {
            super();
            this.context = context11;
            this.arrayList = arrayList1;

            imageLoader = (ImageLoader) Utils.getImageLoaderDefaultCar(context11).get("imageLoader");
            options = (DisplayImageOptions) Utils.getImageLoaderDefaultCar(context11).get("options");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.my_reward_program_item, viewGroup, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
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


            public void bindItems(final ItemMyRewardProgram itemMyRewardProgram, int i, final Activity context) {

                TextView textViewDate = (TextView) itemView.findViewById(R.id.text_date);
                TextView textViewTitle = (TextView) itemView.findViewById(R.id.text_title);
                TextView textViewTarget = (TextView) itemView.findViewById(R.id.text_target);
                TextView textViewBouns = (TextView) itemView.findViewById(R.id.text_bonus);
                TextView textViewEarning= (TextView) itemView.findViewById(R.id.text_earn);

                Button buttonActivate = (Button) itemView.findViewById(R.id.button_active);


                textViewDate.setText(""+itemMyRewardProgram.getCraeted_date());
                textViewTitle.setText("Program Name: "+itemMyRewardProgram.getPname());
                textViewTarget.setText("Target Points: "+itemMyRewardProgram.getTarget());
                textViewBouns.setText("Bonus Amount: "+itemMyRewardProgram.getBonus());
                textViewEarning.setText("Your Total Earnings: "+itemMyRewardProgram.getTotal());


                buttonActivate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity() , RewardInfoActivity.class);
                        intent.putExtra("getId" , itemMyRewardProgram.getId());
                        intent.putExtra("getDriver_time" , itemMyRewardProgram.getDriver_time());
                        startActivity(intent);
                    }
                });

            }

        }



        public void updateData(ArrayList<ItemMyRewardProgram> arrayList2) {
            // TODO Auto-generated method stub
            //arrayList.clear();
//            alName.addAll(arrayList2);
            arrayList = arrayList2;
            notifyDataSetChanged();
        }



    }


}

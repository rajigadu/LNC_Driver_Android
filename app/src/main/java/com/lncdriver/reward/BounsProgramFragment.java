package com.lncdriver.reward;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lncdriver.R;
import com.lncdriver.activity.Navigation;
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

public class BounsProgramFragment extends Fragment {
    private static final String TAG = "BounsProgramFragment";

    SavePref pref1 = new SavePref();


    public Call<ResponseBody> call = null;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

    Activity activity;

    RecyclerView recyclerViewBounsProgram;
    BounsProgramAdapter historyAdapter;

    TextView textViewMsg;
    ProgressBar progressBar;


    ArrayList<ItemBonusProgram> arrayList = new ArrayList<ItemBonusProgram>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bouns_program_fragment, container, false);
        return view;
    }



    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        activity = getActivity();

        textViewMsg = (TextView) v.findViewById(R.id.textView123124);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar1444);


        recyclerViewBounsProgram = (RecyclerView) v.findViewById(R.id.actionable_items);
        recyclerViewBounsProgram.setHasFixedSize(true);
        recyclerViewBounsProgram.setLayoutManager(new LinearLayoutManager(activity , LinearLayoutManager.VERTICAL,false));
        historyAdapter = new BounsProgramAdapter(getActivity(), arrayList);
        recyclerViewBounsProgram.setAdapter(historyAdapter);

        pref1.SavePref(getActivity());

        advertise();


    }







    private void advertise() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<MultipartBody.Part> arrayListMash = new ArrayList<MultipartBody.Part>();

        MultipartBody.Part userid = MultipartBody.Part.createFormData("driver_id", pref1.getUserId());
        arrayListMash.add(userid);


        call = apiInterface.rewardProgramList(arrayListMash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();

//                        {"status":"0","message":"No Bids Yet!.","data":[]}
                        Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC: " + responseCode);

                        JSONObject jsonObject = new JSONObject(responseCode);
                        String status = jsonObject.getString("status");

                        arrayList = new ParsingHelper().getBonusRewardProgramList(responseCode);
                        Log.e(TAG, "Refreshed getgetMyRewardProgramListsize: " + arrayList.size());

                        if(status.equals("1")){
                            textViewMsg.setVisibility(View.GONE);
                        }else{
                            textViewMsg.setVisibility(View.VISIBLE);
                            String message = jsonObject.getString("msg");
                            textViewMsg.setText(""+message);
                        }

                        historyAdapter.updateData(arrayList);
//
//
                        // voActiveBidsVOAdsList = new ParsingHelper().getVOActiveBidsList2Ads(responseCode);
//
//
//                        voActiveBidsVOADMechanicAdapter.updateData(voActiveBidsVOAdsList);


                    }else{

                    }

                    Log.e(TAG, "Refreshed getactiveRewardProgramsizeCC111: " + responseCode);


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










    public class BounsProgramAdapter extends RecyclerView.Adapter<BounsProgramAdapter.ViewHolder> {

        private static final String TAG = "MyRewardProgramAdapter";

        ArrayList<ItemBonusProgram> arrayList = new ArrayList<ItemBonusProgram>();

        Activity context;

        ImageLoader imageLoader;
        DisplayImageOptions options;


        public BounsProgramAdapter(Activity context11, ArrayList<ItemBonusProgram> arrayList1) {
            super();
            this.context = context11;
            this.arrayList = arrayList1;

            imageLoader = (ImageLoader) Utils.getImageLoaderDefaultCar(context11).get("imageLoader");
            options = (DisplayImageOptions) Utils.getImageLoaderDefaultCar(context11).get("options");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.bonus_program_item, viewGroup, false);
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


            public void bindItems(final ItemBonusProgram bonusProgram, int i, final Activity context) {


                TextView textViewTitle = (TextView) itemView.findViewById(R.id.text_title);
                TextView textViewDescription = (TextView) itemView.findViewById(R.id.text_description);
                Button buttonActivate = (Button) itemView.findViewById(R.id.button_active);


                textViewTitle.setText(""+bonusProgram.getPname());
                textViewDescription.setText(""+bonusProgram.getPdescription());


                buttonActivate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activate(bonusProgram);
                    }
                });



            }

        }



        public void updateData(ArrayList<ItemBonusProgram> arrayList2) {
            // TODO Auto-generated method stub
            //arrayList.clear();
//            alName.addAll(arrayList2);
            arrayList = arrayList2;
            notifyDataSetChanged();
        }



    }







    private void activate(ItemBonusProgram bonusProgram) {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<MultipartBody.Part> arrayListMash = new ArrayList<MultipartBody.Part>();

        MultipartBody.Part userid = MultipartBody.Part.createFormData("driver_id", pref1.getUserId());
        arrayListMash.add(userid);

        MultipartBody.Part pid = MultipartBody.Part.createFormData("program_id", bonusProgram.getId());
        arrayListMash.add(pid);

        call = apiInterface.activeRewardProgram(arrayListMash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();

//                        {"status":"0","message":"No Bids Yet!.","data":[]}
                        Log.e(TAG, "Refreshed getactiveCC: " + responseCode);

                        JSONObject jsonObject = new JSONObject(responseCode);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("1")){

                        }else{
                            String msg = jsonObject.getString("msg");
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                            builder.setTitle(getActivity().getString(R.string.app_name_1));
                            builder.setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();

                                            Navigation navigation = (Navigation) getActivity();
                                            navigation.getFragment(10);
                                        }
                                    });
                            androidx.appcompat.app.AlertDialog alert = builder.create();
                            alert.setCancelable(false);
                            alert.show();
                        }

                    }else{

                    }

                    Log.e(TAG, "Refreshed getactiveCCCC111: " + responseCode);

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






}

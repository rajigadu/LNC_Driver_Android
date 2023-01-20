package com.lncdriver.reward;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lncdriver.R;
import com.lncdriver.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;

public class EmployeRewardProgramFragment extends Fragment {

    Activity activity;

    RecyclerView recyclerViewBounsProgram;
    EmployeRewardProgramAdapter historyAdapter;

    TextView textViewMsg;
    ProgressBar progressBar;


    ArrayList<ItemMyRewardProgram> arrayList = new ArrayList<ItemMyRewardProgram>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employe_reward_program_fragment, container, false);
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
        historyAdapter = new EmployeRewardProgramAdapter(getActivity(), arrayList);
        recyclerViewBounsProgram.setAdapter(historyAdapter);






    }











    public class EmployeRewardProgramAdapter extends RecyclerView.Adapter<EmployeRewardProgramAdapter.ViewHolder> {

        private static final String TAG = "EmployeRewardProgramAdapter";

        ArrayList<ItemMyRewardProgram> arrayList = new ArrayList<ItemMyRewardProgram>();

        Activity context;

        ImageLoader imageLoader;
        DisplayImageOptions options;


        public EmployeRewardProgramAdapter(Activity context11, ArrayList<ItemMyRewardProgram> arrayList1) {
            super();
            this.context = context11;
            this.arrayList = arrayList1;

            imageLoader = (ImageLoader) Utils.getImageLoaderDefaultCar(context11).get("imageLoader");
            options = (DisplayImageOptions) Utils.getImageLoaderDefaultCar(context11).get("options");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.employe_reward_program_item, viewGroup, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            //viewHolder.bindItems(arrayList.get(i), i, context);
        }


        @Override
        public int getItemCount() {
            return 10;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view11 = null;

            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
            }


            public void bindItems(final ItemMyRewardProgram rewardProgram, int i, final Activity context) {


//                TextView textViewCategory = (TextView) itemView.findViewById(R.id.category);
//
//                TextView textViewVName = (TextView) itemView.findViewById(R.id.service);
//                TextView textViewVMake = (TextView) itemView.findViewById(R.id.feature_title);
//                TextView textViewVModel = (TextView) itemView.findViewById(R.id.model_title);
//
//
//                TextView textViewVNotes = (TextView) itemView.findViewById(R.id.notes_title);
//                // TextView textViewRate= (TextView) itemView.findViewById(R.id.rate_mc);
//                //textViewRate.setVisibility(View.VISIBLE);
//                RatingBar ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
//                ratingBar.setVisibility(View.VISIBLE);
//
//
//                ImageView icon=(ImageView) itemView.findViewById(R.id.icon);
//                RelativeLayout root=(RelativeLayout) itemView.findViewById(R.id.rowitem_root);
//
//
//                TextView textViewDate = (TextView) itemView.findViewById(R.id.date11);
//                TextView textViewAomunt = (TextView) itemView.findViewById(R.id.bid_amount);
//
//
//                textViewVName.setText(""+itemSettings.getMapMain().get("v_make").toString());
//                textViewVMake.setText(""+itemSettings.getMapMain().get("v_model").toString());
//                textViewVModel.setText(""+itemSettings.getMapMain().get("v_year").toString());
//                textViewVNotes.setText(""+itemSettings.getMapMain().get("v_notes").toString());
//
//                textViewDate.setText(""+itemSettings.getMapMain().get("posted_on").toString());
//                textViewAomunt.setText(""+itemSettings.getMapMain().get("bid_price").toString()+" $");
//
//                textViewCategory.setText(""+itemSettings.getMapMain().get("bid_category").toString());
//
//                //textViewRate.setText(""+itemSettings.getMapMain().get("mc_rating").toString());
//
//                double ratt = 0;
//                try{
//                    ratt = Double.parseDouble(itemSettings.getMapMain().get("mc_rating").toString());
//                }catch (Exception e){
//
//                }
//
//                ratingBar.setRating((float) ratt);
//
//
//                imageLoader.displayImage(itemSettings.getMapMain().get("car_img_url").toString()+itemSettings.getMapMain().get("car_dir").toString()+itemSettings.getMapMain().get("vehicle_image").toString(),
//                        icon,
//                        options);
//
//
//                root.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onFragmentInteractionVO(Utility.VO_HISTORY_DETAIL, itemSettings.getMapMain());
//                    }
//                });


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

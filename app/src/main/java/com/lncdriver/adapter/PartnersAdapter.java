package com.lncdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lncdriver.R;
import com.lncdriver.fragment.Partners;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.Global;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class PartnersAdapter extends RecyclerView.Adapter {
    private final List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private final int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public PartnersAdapter(Context context, List<ModelItem> students, RecyclerView recyclerView, int layout, int mode) {
        adapterList = students;
        itemLayout = layout;
        adapterMode = mode;
        mcontext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        vh = new InboxViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InboxViewHolder) {
            ModelItem singleTripData = (ModelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            // Utils.e("paymentslistadapter 107", "" + detailMap);

            try {
                ((InboxViewHolder) holder).onOff.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("partner_name") && !detailMap.get("partner_name").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).name.setText(detailMap.get("partner_name").toString());
                }

                if (detailMap.containsKey("partner_phone") && !detailMap.get("partner_phone").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).number.setText(detailMap.get("partner_phone").toString());
                }

                if (detailMap.containsKey("partner_email") && !detailMap.get("partner_email").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).email.setText(detailMap.get("partner_email").toString());
                }

                if (detailMap.containsKey("default_status") && !detailMap.get("default_status").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).onOff.setChecked(detailMap.get("default_status").toString().equalsIgnoreCase("yes"));
                }

                ((InboxViewHolder) holder).onOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Global.mapData = (HashMap<String, Object>) v.getTag();
                        if (((Switch) v).isChecked() == true) {
                             Partners.partnerOnOffListRequest(Global.mapData, "YES");
                        } else {
                            Partners.partnerOnOffListRequest(Global.mapData, "NO");
                        }


//                        if (((Switch) v).isChecked()) {
//                           // Partners.partnerOnOffListRequest(Utils.global.mapData);
////                        } else {
////                            ((Switch) v).setChecked(true);
////                            Utils.toastTxt("Add new Partner or Activate required partner for Ride SHARING!", mcontext);
////                        }
                    }
                });

//                ((InboxViewHolder) holder).onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    }
//                });
            } catch (Exception e) {
                //Utils.logError(Tag+"180","Exception=========Exception==Exception "+logError.getMessage());
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).hotel = singleTripData;
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //Notification
        public ModelItem hotel;

        public TextView name, number, email;
        public Switch onOff;

        public InboxViewHolder(View v) {
            super(v);
            try {
                name = (TextView) v.findViewById(R.id.name);
                number = (TextView) v.findViewById(R.id.number);
                email = (TextView) v.findViewById(R.id.email);
                onOff = (Switch) v.findViewById(R.id.active);
            } catch (Exception e) {
                //Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }
}
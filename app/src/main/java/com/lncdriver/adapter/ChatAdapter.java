package com.lncdriver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public ChatAdapter(Context context, List<ModelItem> students, RecyclerView recyclerView, int layout, int mode) {
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
            Utils.logError("paymentslistadapter 107", "" + detailMap);

            try {
                ((InboxViewHolder) holder).left.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).right.setTag(singleTripData.getMapMain());

                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                String id = pref1.getUserId();

                if (detailMap.containsKey("sender") && !detailMap.get("sender").toString().equalsIgnoreCase("")) {
                    if (detailMap.containsKey("sender") && !detailMap.get("sender").toString().equalsIgnoreCase(id)) {
                        ((InboxViewHolder) holder).left.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).right.setVisibility(View.GONE);

                        /*if (detailMap.containsKey("profileImageReciever") && detailMap.get("profileImageSender") != null) {
                            Utils.setImagePiccaso(mcontext, Settings.URLIMAGEBASE + detailMap.get("profileImageSender").toString(), ((InboxViewHolder) holder).left_img);
                        }*/

                        if (detailMap.containsKey("mesage") && !detailMap.get("mesage").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).left_msg.setText(detailMap.get("mesage").toString());
                        }

                        if (detailMap.containsKey("tvRideDate") && !detailMap.get("tvRideDate").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).left_dt.setText(detailMap.get("tvRideDate").toString());
                        }
                    } else if (detailMap.containsKey("sender") && detailMap.get("sender").toString().equalsIgnoreCase(id)) {
                        ((InboxViewHolder) holder).left.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).right.setVisibility(View.VISIBLE);

                        /*if (detailMap.containsKey("profileImageSender") && detailMap.get("profileImageSender") != null) {
                            Utils.setImagePiccaso(mcontext, Settings.URLIMAGEBASE + detailMap.get("profileImageSender").toString(), ((InboxViewHolder) holder).right_img);
                        }*/
                        if (detailMap.containsKey("mesage") && !detailMap.get("mesage").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).right_msg.setText(detailMap.get("mesage").toString());
                        }

                        if (detailMap.containsKey("tvRideDate") && !detailMap.get("tvRideDate").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).right_dt.setText(detailMap.get("tvRideDate").toString());
                        }
                    }
                }
            } catch (Exception e) {
                Utils.logError(Tag + "180", "Exception======================Exception======================Exception");
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
        public View gView;
        //***

        public TextView left_dt, right_dt, left_msg, right_msg;
        public LinearLayout root;
        public ImageView left_img, right_img;
        public RelativeLayout left, right;

        public InboxViewHolder(View v) {
            super(v);
            try {
                left_img = (ImageView) v.findViewById(R.id.img_left);
                right_img = (ImageView) v.findViewById(R.id.img_right);
                left_msg = (TextView) v.findViewById(R.id.msg_left);
                right_msg = (TextView) v.findViewById(R.id.msg_right);
                right_dt = (TextView) v.findViewById(R.id.right_dt);
                left_dt = (TextView) v.findViewById(R.id.left_dt);
                left = (RelativeLayout) v.findViewById(R.id.rl_left);
                right = (RelativeLayout) v.findViewById(R.id.rl_right);
            } catch (Exception e) {
                Utils.logError("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }
}
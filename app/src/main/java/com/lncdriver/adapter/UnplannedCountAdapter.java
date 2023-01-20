package com.lncdriver.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lncdriver.R;
import com.lncdriver.activity.ViewCustomerFRideDetails;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;

public class UnplannedCountAdapter extends RecyclerView.Adapter<TimeViewHolder> {

    private final ArrayList<String> timeList;
    private final AppCompatActivity ctx;
    private int lastPos = -1;

    public UnplannedCountAdapter(AppCompatActivity context, ArrayList<String> timeList) {
        this.timeList = timeList;
        ctx = context;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.adapter_time_item, parent, false);
        TimeViewHolder timeViewHolder = new TimeViewHolder(itemView);
        return timeViewHolder;
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pos = position;
        holder.tvTime.setTag(position);
        holder.tvTime.setText(timeList.get(position));
        if (lastPos == position) {
            holder.tvTime.setBackgroundColor(ctx.getResources().getColor(R.color.green_light));
        } else {
            holder.tvTime.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                lastPos = pos;
                Utils.logError("UnplannedAdapter", timeList.get(pos));
                if (ctx instanceof ViewCustomerFRideDetails) {
                    ViewCustomerFRideDetails viewCustomerFRideDetails = (ViewCustomerFRideDetails) ctx;
                    viewCustomerFRideDetails.getUnplannedCount(timeList.get(pos));
                }
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }
}

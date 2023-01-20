package com.lncdriver.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.lncdriver.R;


/**
 */

public class TimeViewHolder extends RecyclerView.ViewHolder {

    public TextView  tvTime;
    public int pos;

    public TimeViewHolder(View itemView) {
        super(itemView);
        tvTime =  itemView.findViewById(R.id.tv_item_time);

    }
}

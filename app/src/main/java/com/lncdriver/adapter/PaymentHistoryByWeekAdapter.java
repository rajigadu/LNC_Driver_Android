package com.lncdriver.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.fragment.PaymentHistory;
import com.lncdriver.fragment.PaymentHistoryByWeek;
import com.lncdriver.model.Week;
import com.lncdriver.utils.Utils;

import java.util.List;

public class PaymentHistoryByWeekAdapter extends RecyclerView.Adapter {
    private final List<Week> weekList;
    String Tag = "GroupAdapter";
    private final int itemLayout;
    public Context mcontext;
    PaymentHistoryByWeek Instance;

    public PaymentHistoryByWeekAdapter(Context context, PaymentHistoryByWeek Instance, List<Week> weekList, int layout) {
        this.weekList = weekList;
        itemLayout = layout;
        mcontext = context;
        this.Instance = Instance;
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
            Week week = weekList.get(position);


            if(week.getFromDate().equalsIgnoreCase("") && week.getToDate().equalsIgnoreCase("")){
                ((InboxViewHolder) holder).tvFromDate.setText(" " + week.getEffectiveCreditDate()+" 04:00 AM");
                ((InboxViewHolder) holder).tvToDate.setText(" " + week.getEffectiveDebitDate()+" 03:59 AM");
            }else{
                ((InboxViewHolder) holder).tvFromDate.setText(" " + week.getFromDate());
                ((InboxViewHolder) holder).tvToDate.setText(" " + week.getToDate());
            }


            ((InboxViewHolder) holder).tvWeeklyAmount.setText("$"+week.getAmount() + "");
            //((InboxViewHolder) holder).tvWeeklyTip.setText(" " + week.get + "$");
            ((InboxViewHolder) holder).cardViewRoot.setTag(position);
            ((InboxViewHolder) holder).cardViewRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    Week week = weekList.get(pos);

                    Fragment fragment = new PaymentHistory();
                    String backStateName = Instance.getClass().getName();
                    FragmentManager manager = Instance.getFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Utils.WEEK_DATA, week);
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = manager.beginTransaction();
                    ft.add(R.id.frame, fragment, backStateName);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(backStateName);
                    ft.commit();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return weekList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFromDate, tvToDate, tvWeeklyAmount, tvWeeklyTip;
        public CardView cardViewRoot;

        public InboxViewHolder(View v) {
            super(v);
            try {
                tvFromDate = v.findViewById(R.id.tv_from_date);
                tvToDate = v.findViewById(R.id.tv_to_date);
                cardViewRoot = v.findViewById(R.id.card_row_item);
                tvWeeklyAmount = v.findViewById(R.id.tv_weekly_amount);
                tvWeeklyTip = v.findViewById(R.id.tv_weekly_tip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
package com.lncdriver.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class StopsAddressAdapter extends RecyclerView.Adapter
{
    private List<ModelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public StopsAddressAdapter(Context context, List<ModelItem> students, RecyclerView recyclerView, int layout, int mode)
    {
        adapterList = 	students;
        itemLayout 	= 	layout;
        adapterMode	=	mode;
        mcontext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder vh;

            View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent,false);
            vh = new InboxViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if (holder instanceof InboxViewHolder)
        {
            ModelItem singleTripData = (ModelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            Utils.logError("paymentslistadapter 107", "" + detailMap);

            try
            {
                if(detailMap.containsKey("location") && !detailMap.get("location").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).location.setText(detailMap.get("location").toString());
                }
           }
            catch (Exception e)
            {
                Utils.logError(Tag+"180","Exception======================Exception======================Exception");
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).hotel= singleTripData;
        }
        else
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder
    {
        //Notification
        public ModelItem hotel;
        public View gView;
        //***

        public TextView location;
        public CardView root;
        public InboxViewHolder(View v)
        {
            super(v);
            try
            {
                location=(TextView) v.findViewById(R.id.location);
                root=(CardView) v.findViewById(R.id.rowitem_root);
            }
            catch(Exception e)
            {
                Utils.logError("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }
}
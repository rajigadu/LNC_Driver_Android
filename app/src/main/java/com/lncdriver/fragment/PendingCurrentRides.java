package com.lncdriver.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lncdriver.R;
import com.lncdriver.adapter.PendingCurrentRidesAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.Global;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class PendingCurrentRides extends Fragment
{
    static RecyclerView rv_loc;

    public static Context mcontext;
    public static PendingCurrentRidesAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String,Object>> list=new ArrayList<>();

    public static OnFragmentInteractionListenerPendingCurrebtRides mListener;

    public interface OnFragmentInteractionListenerPendingCurrebtRides
    {
        // TODO: Update argument type and name
        void onFragmentInteractionpendingCurrentRides(int position,HashMap<String,Object> map);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerPendingCurrebtRides)
        {
            mListener = (OnFragmentInteractionListenerPendingCurrebtRides) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_currentrides,container, false);



        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState)
    {
        ButterKnife.bind(getActivity());
        rv_loc=(RecyclerView) v.findViewById(R.id.rv_loc);

        mcontext=getActivity();

        requestsListMan = new LinearLayoutManager(mcontext,LinearLayoutManager.VERTICAL,false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);

        MyrequestsListRequest();

       /* for (int i=0;i<5;i++)
        {
            map=new HashMap<>();
            map.put("tvRideDate","tvRideDate "+String.valueOf(i));
            map.put("numstops",String.valueOf(i));
            map.put("slocation","pickup address "+String.valueOf(i));
            map.put("mlocation","drop off address "+String.valueOf(i));
            map.put("distance",String.valueOf(i*10));
            list.add(map);
        }
        if (list!=null&&list.size()>0)
            loadRequestsList(mcontext,list,"");*/

        super.onViewCreated(v, savedInstanceState);
    }

    public static void DriverAcceptRequest(Context mcontext,HashMap<String,Object> vmap,String status)
    {
        if(status.equalsIgnoreCase("2"))
        {
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            pref1.setridemap("");
            pref1.setisnewride("");
        }
        OnlineRequest.DriverAcceptRequest(mcontext,vmap);
    }
    public static  void MyrequestsListRequest()
    {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Global.mapMain.put("driver_id",id);
        Global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_PENDINGCURRENTRIDES);

        if(Utils.isNetworkAvailable(mcontext))
        {
            JsonPost.getNetworkResponse(mcontext,null, Global.mapMain,ConstVariable.Pending_Current_Rides);
        }
        else
        {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static  void loadRequestsList(Context context, List<HashMap<String,Object>> viewList, String mode)
    {
        // Utils.logError(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0)
        {
            try
            {
                //Utils.logError(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<ModelItem>();

                //Utils.logError(TAG+"93", "browseMembersList new "+eventList);

                for(int i = 0; i < viewList.size(); i++)
                {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if(!requestsList.contains(mp))
                    {
                        requestsList.add(new ModelItem(mp));
                    }
                }
                //Utils.logError(TAG+"118", "browseMembersList"+eventList.size());
            }
            catch (Exception e)
            {
                //Utils.logError(TAG+"122","Exception======================Exception======================Exception");
                e.printStackTrace();
            }
            finally
            {
                // Utils.logError(TAG+"127", "browseMembersList"+eventList.size());
                //Utils.logError(TAG+"128", "ok");
                if(!mode.equalsIgnoreCase("update"))
                {
                    setAdapterFriendsRequestList(context);
                }
                else
                {
                    requestsAdapter.notifyItemInserted(requestsList.size());
                    requestsAdapter.notifyDataSetChanged();
                }
            }
            requestsListMan.setAutoMeasureEnabled(true);
        }
        else
        {
            rv_loc.setVisibility(View.GONE);
            //noData.setVisibility(View.VISIBLE);
        }
    }

    public static void setAdapterFriendsRequestList(final Context context)
    {
        if(rv_loc != null)
        {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        //Utils.logError(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new PendingCurrentRidesAdapter(mcontext,requestsList,rv_loc,R.layout.pending_cride_rowitem,ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.logError(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }
}
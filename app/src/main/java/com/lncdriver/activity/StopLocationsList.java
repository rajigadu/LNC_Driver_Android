package com.lncdriver.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.adapter.StopsAddressAdapter;
import com.lncdriver.model.SavePref;
import com.lncdriver.model.ModelItem;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by narayana on 10/25/2017.
 */

public class StopLocationsList extends AppCompatActivity
{
    public static RecyclerView rv_loc;
    ImageView back;
    TextView title;
    public static StopLocationsList Instance;
    public static Context mcontext;
    public static StopsAddressAdapter requestsAdapter;
    public static List<ModelItem> requestsList;
    public HashMap<String, Object> map,smap;
    public List<HashMap<String,Object>> list=new ArrayList<>();

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_stopslist);
        super.onCreate(savedInstanceState);

        rv_loc=(RecyclerView) findViewById(R.id.rv_loc);
        back=(ImageView) findViewById(R.id.back);
        title=(TextView) findViewById(R.id.title);

        title.setText("Stop Locations List");

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        Instance=this;
        mcontext=this;

        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL,false));

        smap=new HashMap<>();

        if(getIntent().hasExtra("map"))
        {
            smap=(HashMap<String, Object>) getIntent().getSerializableExtra("map");
        }

        dropAddressRequest();
      /*  for (int i=0;i<5;i++)
        {
            map=new HashMap<>();
            map.put("fname","c"+String.valueOf(i));
            list.add(map);
        }*/

       /* if (Utils.global.dropAddresslist!=null&&Utils.global.dropAddresslist.size()>0)
        loadRequestsList(mcontext,Utils.global.dropAddresslist,"");*/
    }

    public void dropAddressRequest()
    {
        MyrequestsListRequest();
    }

    public  void MyrequestsListRequest()
    {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        map=new HashMap<>();

        map.put("userid",id);

        if (smap.containsKey("id"))
            map.put("rideid",smap.get("id").toString());

        OnlineRequest.getStopLlocationsRequest(mcontext,map);
    }

    public static  void loadRequestsList(Context context,List<HashMap<String, Object>> viewList, String mode)
    {
        // Utils.logError(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0)
        {
            try
            {
                //Utils.logError(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<ModelItem>();

                //Utils.logError(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++)
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
        }
        else
        {
            rv_loc.setVisibility(View.GONE);
            //noData.setVisibility(View.VISIBLE);
        }
    }

    public static  void setAdapterFriendsRequestList(final Context context)
    {
        if(rv_loc != null)
        {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        //Utils.logError(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new StopsAddressAdapter(mcontext,requestsList,rv_loc,R.layout.stop_rowitem, ConstVariable.StopLocations);
        //set the adapter object to the Recyclerview
        //Utils.logError(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }
}

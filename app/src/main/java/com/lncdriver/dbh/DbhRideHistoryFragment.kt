package com.lncdriver.dbh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lncdriver.databinding.FragmentDbhAssignRidesLayoutBinding
import com.lncdriver.dbh.adapter.DbhRideHistoryAdapter
import com.lncdriver.dbh.model.DbhRideHistoryData
import com.lncdriver.dbh.utils.FragmentCallback
import com.lncdriver.dbh.utils.ProgressCaller
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.dbh.viewmodel.DbhViewModel
import com.lncdriver.model.SavePref

/**
 * Create by Siru Malayil on 27-04-2023.
 */
class DbhRideHistoryFragment : Fragment() {

    private var binding: FragmentDbhAssignRidesLayoutBinding? = null
    private var dbhViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhAssignRidesLayoutBinding.inflate(layoutInflater, container,false)
        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SavePref()
        preferences?.SavePref(activity)

        binding?.refreshDbhRides?.setOnRefreshListener {
            binding?.refreshDbhRides?.isRefreshing = true
            getDbhRideHistory()
        }

        getDbhRideHistory()
    }

    private fun getDbhRideHistory() {
        dbhViewModel?.dbhRideHistory(preferences?.userId ?: "")?.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    val dbhHistoryData = result.data?.data
                    if (result.data?.status == "1") {
                        initializeHistoryListAdapter(dbhHistoryData)
                    }
                    binding?.refreshDbhRides?.isRefreshing = false
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    binding?.refreshDbhRides?.isRefreshing = false
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

    private fun initializeHistoryListAdapter(dbhHistoryData: List<DbhRideHistoryData>?) {
        val dbhHistoryAdapter = DbhRideHistoryAdapter(callback = object: FragmentCallback {
            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {

            }
        })
        binding?.recyclerDbRides?.apply {
            adapter = dbhHistoryAdapter
        }
        dbhHistoryAdapter.submitList(dbhHistoryData)
    }
}
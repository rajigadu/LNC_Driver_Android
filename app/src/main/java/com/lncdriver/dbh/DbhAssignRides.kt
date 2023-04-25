package com.lncdriver.dbh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lncdriver.databinding.FragmentDbhAssignRidesLayoutBinding
import com.lncdriver.dbh.utils.ProgressCaller
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.dbh.viewmodel.DbhViewModel
import com.lncdriver.model.SavePref

/**
 * Create by Siru Malayil on 25-04-2023.
 */
class DbhAssignRides : Fragment() {

    private var binding: FragmentDbhAssignRidesLayoutBinding? = null
    private var dbhViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhAssignRidesLayoutBinding.inflate(layoutInflater, container, false)
        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SavePref()
        preferences?.SavePref(activity)

        binding?.refreshDbhRides?.setOnRefreshListener {
            binding?.refreshDbhRides?.isRefreshing = true
            getDbhAssignedRides()
        }

        getDbhAssignedRides()
    }

    private fun getDbhAssignedRides() {
        val driverId = preferences?.userId ?: ""
        dbhViewModel?.dbhAssignedRideList(driverId)?.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    ProgressCaller.hideProgressDialog()
//                    if (result.data.status == "1") {
//                        initializeRideListAdapter()
//                    } else {
//                        showAlertMessage()
//                    }
                }
                Resource.Status.ERROR -> {
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }
}
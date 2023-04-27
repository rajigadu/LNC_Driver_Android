package com.lncdriver.dbh

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lncdriver.R
import com.lncdriver.databinding.FragmentDbhAssignedRideDetailsBinding
import com.lncdriver.dbh.base.AlertMessageDialogFragment.Companion.ACTION_OK
import com.lncdriver.dbh.base.BaseActivity
import com.lncdriver.dbh.model.DbhAssignedRideData
import com.lncdriver.dbh.model.DefaultResponse
import com.lncdriver.dbh.utils.FragmentCallback
import com.lncdriver.dbh.utils.ProgressCaller
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.dbh.viewmodel.DbhViewModel
import com.lncdriver.model.SavePref

/**
 * Create by Siru Malayil on 26-04-2023.
 */
class DbhAssignedRidesDetails : Fragment() {

    private var binding: FragmentDbhAssignedRideDetailsBinding? = null
    private var rideData: DbhAssignedRideData? = null
    private var dbhViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null

    companion object {
        fun newInstance(rideData: DbhAssignedRideData? = null) = DbhAssignedRidesDetails().apply {
            this.rideData = rideData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhAssignedRideDetailsBinding.inflate(layoutInflater, container, false)
        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SavePref()
        preferences?.SavePref(activity)

        setViewData()
        onClickListener()
    }

    private fun onClickListener() {
        binding?.btnChat?.setOnClickListener {

        }
        binding?.btnCompleteRide?.setOnClickListener {
            dbhViewModel?.dbhCompleteRide(
                userId = preferences?.userId ?: "",
                bookingId = rideData?.booking_type ?: "", //TODO confirm this as booking_id
                payDateTime = rideData?.otherdate ?: "", //TODO confirm this as payDateTime
                endTime = rideData?.end_time ?: "" //TODO confirm this as endTime
            )?.observe(viewLifecycleOwner) { result ->
                when(result.status) {
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.SUCCESS -> {

                    }
                    Resource.Status.ERROR -> {

                    }
                }
            }
        }
        binding?.btnCall?.setOnClickListener {
            (activity as BaseActivity).showAlertMessageDialog(
                message = "Are you sure want to call?",
                negativeButton = true,
                fragmentCallback = object : FragmentCallback {
                    override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                        when(param1) {
                            ACTION_OK -> {
                                callPermission(rideData?.mobile.toString().trim())
                            }
                        }
                    }
                }
            )
        }
        binding?.btnGoToUser?.setOnClickListener {
            launchPickupLocationInMap()
        }

        binding?.btnStartRide?.setOnClickListener {
            startDbhRide()
        }
    }

    /**
     * Here will invoke DBH Stat Ride API
     */
    private fun startDbhRide() {
        dbhViewModel?.dbhStartRide(
            driverId = preferences?.userId ?: "",
            rideId = rideData?.id ?: "",
            time = rideData?.time ?: ""
        )?.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    val response = result.data
                    if (response?.status == "1") {
                        (activity as BaseActivity).showAlertMessageDialog(
                            message = response.data.firstOrNull()?.msg ?: getString(R.string.something_went_wrong)
                        )
                    }
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    (activity as BaseActivity).showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg ?: getString(R.string.something_went_wrong)
                    )
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

    /**
     * Launch Google Map if existing in device and show the location route
     * else will show a message to install
     */
    private fun launchPickupLocationInMap() {
        try {
            val add = ("google.navigation:q=" + rideData?.pickup_lat + ","
                    + rideData?.pickup_long + "&mode=d")
            val gmmIntentUri = Uri.parse(add)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        } catch (anf: ActivityNotFoundException) {
            (activity as BaseActivity).showAlertMessageDialog(
                message = "Please Install Google Maps "
            )
        } catch (ex: Exception) {
            ex.message
        }
    }

    /**
     * Phone Call permission will request here, once request accepted
     * will invoke dialer dashboard
     */
    private fun callPermission(number: String) {
        activity?.let { activity ->
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getCall(number)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CALL_PHONE),
                        100
                    )
                }
            }
        }
    }

    private fun getCall(number: String) {
        if (number.trim { it <= ' ' }.isNotEmpty()) startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", number, null)
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setViewData() {
        binding?.customerName?.text = rideData?.first_name
        binding?.customerNumber?.text = rideData?.mobile
        binding?.pickupDateAndTime?.text = "Pickup Date/Time: ${rideData?.otherdate} ${rideData?.time}"
        binding?.pickup?.text = "Pickup: ${rideData?.pickup_address}"
    }
}
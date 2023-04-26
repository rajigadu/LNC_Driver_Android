package com.lncdriver.dbh

import android.Manifest
import android.annotation.SuppressLint
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
import com.lncdriver.activity.ViewCustomerFRideDetails
import com.lncdriver.databinding.FragmentDbhAssignedRideDetailsBinding
import com.lncdriver.dbh.base.AlertMessageDialogFragment.Companion.ACTION_OK
import com.lncdriver.dbh.base.BaseActivity
import com.lncdriver.dbh.model.DbhAssignedRideData
import com.lncdriver.dbh.utils.FragmentCallback

/**
 * Create by Siru Malayil on 26-04-2023.
 */
class DbhAssignedRidesDetails : Fragment() {

    private var binding: FragmentDbhAssignedRideDetailsBinding? = null
    private var rideData: DbhAssignedRideData? = null

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewData()
        onClickListener()
    }

    private fun onClickListener() {
        binding?.btnChat?.setOnClickListener {

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
    }

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
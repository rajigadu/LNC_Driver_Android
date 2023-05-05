package com.lncdriver.dbh.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.lncdriver.R
import com.lncdriver.activity.ActivityUserChat
import com.lncdriver.databinding.FragmentDbhAssignedRideDetailsBinding
import com.lncdriver.dbh.base.AlertMessageDialogFragment.Companion.ACTION_OK
import com.lncdriver.dbh.base.BaseActivity
import com.lncdriver.dbh.model.DbhAssignedRideData
import com.lncdriver.dbh.utils.DbhUtils
import com.lncdriver.dbh.utils.FragmentCallback
import com.lncdriver.dbh.utils.ProgressCaller
import com.lncdriver.dbh.utils.Resource
import com.lncdriver.dbh.viewmodel.DbhViewModel
import com.lncdriver.fragment.Home
import com.lncdriver.model.SavePref
import com.lncdriver.utils.PermissionUtils
import org.json.JSONObject
import java.io.Serializable
import java.util.*

/**
 * Create by Siru Malayil on 26-04-2023.
 */

const val TAG = "DBHRide"
val permissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
)
class DbhAssignedRidesDetails : BaseActivity() {

    private var binding: FragmentDbhAssignedRideDetailsBinding? = null
    private var rideData: DbhAssignedRideData? = null
    private var dbhViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDbhAssignedRideDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]

        rideData = intent?.extras?.getParcelable(DbhUtils.DBH_RIDE_DATA) as? DbhAssignedRideData

        checkLocationPermission()

        preferences = SavePref()
        preferences?.SavePref(this)

        setViewData()
        onClickListener()

    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.hasPermissions(Home.mcontext, *permissions)) {
                requestPermissions(
                    permissions, Home.MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                getCurrentLocation()
            }
        } else {
            getCurrentLocation()
        }
    }


    private fun onClickListener() {
        binding?.btnChat?.setOnClickListener {
            val dmap = HashMap<String, Any>()
            dmap["userid"] = rideData?.user_id ?: ""
            startActivity(
                Intent(this,ActivityUserChat::class.java).apply {
                    putExtra("map", dmap as Serializable)
                }
            )
        }
        binding?.btnCompleteRide?.setOnClickListener {
            completeRide()
        }
        binding?.btnCall?.setOnClickListener {
            showAlertMessageDialog(
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
            showAlertMessageDialog(
                message = "Are you sure want to start this ride!",
                negativeButton = true,
                fragmentCallback = object : FragmentCallback {
                    override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                        when(param1) {
                            ACTION_OK -> {
                                startDbhRide()
                            }
                        }
                    }
                }
            )
        }
        binding?.btnCancelRide?.setOnClickListener {
            showAlertMessageDialog(
                message = "Are you sure want to cancel this ride ?",
                negativeButton = true,
                fragmentCallback = object : FragmentCallback {
                    override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                        when(param1) {
                            ACTION_OK -> {
                                cancelDbhRide()
                            }
                        }
                    }
                }
            )
        }
    }

    private fun cancelDbhRide() {
        dbhViewModel?.cancelDbhRide(
            driverId = preferences?.userId ?: "",
            rideId = rideData?.id ?: ""
        )?.observe(this) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { ProgressCaller.showProgressDialog(this)}
                Resource.Status.SUCCESS -> {
                    showAlertMessageDialog(
                        message = result?.message,
                        fragmentCallback = object : FragmentCallback {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                finish()
                            }
                        }
                    )
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    showAlertMessageDialog(
                        message = result?.message ?: getString(R.string.something_went_wrong))
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

    private fun getCurrentLocation() {
        // Create an instance of the FusedLocationProviderClient
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            // Request the last known location from the FusedLocationProviderClient
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // If the last known location is not null, use its latitude and longitude
                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val geocoder = Geocoder(this@DbhAssignedRidesDetails, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                    val address = addresses?.get(0)?.getAddressLine(0)
                    val city = addresses?.get(0)?.locality
                    val state = addresses?.get(0)?.adminArea
                    val country = addresses?.get(0)?.countryName
                    val postalCode = addresses?.get(0)?.postalCode
                    val knownName = addresses?.get(0)?.featureName

                    dbhViewModel?.destinationAddress = addresses?.firstOrNull()
                    dbhViewModel?.destinationLatLng = LatLng(
                        location.latitude,
                        location.longitude
                    )

                    Log.d(
                        TAG,
                        "Latitude: $latitude, Longitude: $longitude, Address: $address, City: $city, State: $state, Country: $country, Postal Code: $postalCode, Known Name: $knownName"
                    )
                }
            }
        }
    }

    /**
     * Started Ride will complete here, API calling
     */
    private fun completeRide() {
        val dbhCompleteRide = JSONObject()
            .put("userid", rideData?.user_id)
            .put("booking_id", rideData?.id)
            .put("pay_datetime", DbhUtils.getCurrentDateAndTime())
            .put("end_time", DbhUtils.getCurrentDateAndTime())
            .put("d_address", dbhViewModel?.destinationAddress?.getAddressLine(0))
            .put("d_lat", dbhViewModel?.destinationLatLng?.latitude)
            .put("d_long", dbhViewModel?.destinationLatLng?.longitude)
        dbhViewModel?.dbhCompleteRide(dbhCompleteRide)?.observe(this) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { ProgressCaller.showProgressDialog(this) }
                Resource.Status.SUCCESS -> {
                    showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg,
                        fragmentCallback = object : FragmentCallback{
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                finish()
                            }
                        }
                    )
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg)
                    ProgressCaller.hideProgressDialog()
                }
            }
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
        )?.observe(this) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { ProgressCaller.showProgressDialog(this) }
                Resource.Status.SUCCESS -> {
                    val response = result.data
                    if (response?.status == "1") {
                        showAlertMessageDialog(
                            message = response.msg,
                            fragmentCallback = object : FragmentCallback {
                                override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                    finish()
                                }
                            }
                        )
                    }
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    showAlertMessageDialog(
                        message = result.data?.msg ?: getString(R.string.something_went_wrong)
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
            showAlertMessageDialog(
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
        if (ContextCompat.checkSelfPermission(
                this,
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

        if (rideData?.status == "1"){
            binding?.btnCancelRide?.visibility = View.GONE
            binding?.btnStartRide?.visibility = View.INVISIBLE
            binding?.btnCompleteRide?.visibility = View.VISIBLE
            binding?.rideStartTime?.visibility = View.VISIBLE
            binding?.rideStartTime?.text = "Ride Start Time: ${rideData?.ride_start_time}"
        } else {
            binding?.btnCancelRide?.visibility = View.VISIBLE
            binding?.btnStartRide?.visibility = View.VISIBLE
            binding?.btnCompleteRide?.visibility = View.INVISIBLE
            binding?.rideStartTime?.visibility = View.INVISIBLE
        }
    }
}
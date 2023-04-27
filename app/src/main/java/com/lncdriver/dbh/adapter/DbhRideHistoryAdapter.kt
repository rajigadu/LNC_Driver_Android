package com.lncdriver.dbh.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lncdriver.databinding.DbhRideHistoryLayoutBinding
import com.lncdriver.dbh.model.DbhRideHistoryData
import com.lncdriver.dbh.utils.FragmentCallback

/**
 * Create by Siru Malayil on 27-04-2023.
 */
class DbhRideHistoryAdapter(private val callback: FragmentCallback? = null)
    : ListAdapter<DbhRideHistoryData, DbhRideHistoryAdapter.ViewHolder>(DiffCallBack()) {


    class DiffCallBack: DiffUtil.ItemCallback<DbhRideHistoryData>() {
        override fun areItemsTheSame(
            oldItem: DbhRideHistoryData,
            newItem: DbhRideHistoryData
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DbhRideHistoryData,
            newItem: DbhRideHistoryData
        ): Boolean = oldItem == newItem

    }

    inner class  ViewHolder(private val binding: DbhRideHistoryLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(rideHistory: DbhRideHistoryData?) {
            binding.customerName.text = "${rideHistory?.first_name} ${rideHistory?.last_name}"
            binding.date.text = "${rideHistory?.date}\n ${rideHistory?.time}"
            binding.pickupAddress.text = rideHistory?.pickup_address
            binding.tvNotes.text = rideHistory?.notes ?: ""
            //binding.distance.text = rideHistory?. //TODO map distance data
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            DbhRideHistoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}
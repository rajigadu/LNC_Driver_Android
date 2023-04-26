package com.lncdriver.dbh.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lncdriver.databinding.LayoutDbhAssignRidesBinding
import com.lncdriver.dbh.model.DbhAssignedRideData
import com.lncdriver.dbh.utils.FragmentCallback

/**
 * Create by Siru Malayil on 25-04-2023.
 */
class DbhAssignedRidesAdapter(private val callback: FragmentCallback? = null): ListAdapter<DbhAssignedRideData, DbhAssignedRidesAdapter.ViewHolder>(DiffCallBack()) {



    class DiffCallBack: DiffUtil.ItemCallback<DbhAssignedRideData>() {
        override fun areItemsTheSame(
            oldItem: DbhAssignedRideData,
            newItem: DbhAssignedRideData
        ): Boolean =  oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DbhAssignedRideData,
            newItem: DbhAssignedRideData
        ): Boolean = oldItem == newItem
    }

    inner class ViewHolder(val binding: LayoutDbhAssignRidesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(rideData: DbhAssignedRideData?) {
            binding.valueDateAndTime.text = rideData?.otherdate
            binding.valueNotes.text = rideData?.notes
            binding.valuePickup.text = rideData?.pickup_address
            binding.root.setOnClickListener {
                callback?.onResult(rideData)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutDbhAssignRidesBinding.inflate(
                LayoutInflater.from(parent.context),parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}
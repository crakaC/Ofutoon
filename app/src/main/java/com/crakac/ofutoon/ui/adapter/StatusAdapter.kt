package com.crakac.ofutoon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.databinding.ListItemStatusBinding
import com.crakac.ofutoon.ui.vh.StatusViewHolder

class StatusAdapter : RefreshableAdapter<Status>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StatusViewHolder).setup(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(ListItemStatusBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].reblog != null) {
            HolderType.Reblog.rawValue
        } else {
            HolderType.Status.rawValue
        }
    }

    private enum class HolderType(val rawValue: Int) {
        Status(3),
        Reblog(4)
    }
}
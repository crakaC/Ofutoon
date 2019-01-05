package com.crakac.ofutoon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crakac.ofutoon.api.entity.Notification
import com.crakac.ofutoon.databinding.ListItemFollowedNotificationBinding
import com.crakac.ofutoon.databinding.ListItemStatusBinding
import com.crakac.ofutoon.ui.vh.FollowedNotificationViewHolder
import com.crakac.ofutoon.ui.vh.StatusViewHolder

class NotificationAdapter : RefreshableAdapter<Notification>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.Follow.value) {
            FollowedNotificationViewHolder(ListItemFollowedNotificationBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            StatusViewHolder(ListItemStatusBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = getItem(position)
        if (holder is StatusViewHolder) {
            holder.setup(notification)
        }
        if (holder is FollowedNotificationViewHolder) {
            holder.setNotification(notification)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).getType()) {
            Notification.Type.Follow -> {
                ViewType.Follow.value
            }
            else -> {
                ViewType.Status.value
            }
        }
    }

    private enum class ViewType(val value: Int) {
        Follow(0),
        Status(1)
    }
}
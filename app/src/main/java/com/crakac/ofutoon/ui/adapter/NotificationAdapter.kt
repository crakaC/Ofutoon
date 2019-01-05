package com.crakac.ofutoon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crakac.ofutoon.BR
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.entity.Notification
import com.crakac.ofutoon.databinding.ListItemFollowedNotificationBinding
import com.crakac.ofutoon.databinding.ListItemStatusBinding
import com.crakac.ofutoon.util.TextUtil

class NotificationAdapter : RefreshableAdapter<Notification>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.Follow.value) {
            FollowedNotificationViewHolder(ListItemFollowedNotificationBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            StatusAdapter.StatusViewHolder(ListItemStatusBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = getItem(position)
        if (holder is StatusAdapter.StatusViewHolder) {
            holder.setup(notification.status!!)
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

    class FollowedNotificationViewHolder(val binding: ListItemFollowedNotificationBinding) :
        RecyclerView.ViewHolder(binding.root), Refreshable {
        fun setNotification(notification: Notification) {
            val account = notification.account
            val nameWithEmoij = TextUtil.emojify(
                binding.followedBy,
                itemView.context.getString(R.string.followed_by).format(account.displayName),
                account.emojis
            )
            binding.followedBy.text = nameWithEmoij
            binding.displayName.text = nameWithEmoij
            binding.setVariable(BR.account, account)
        }
    }
}
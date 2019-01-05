package com.crakac.ofutoon.ui.vh

import androidx.recyclerview.widget.RecyclerView
import com.crakac.ofutoon.BR
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.entity.Notification
import com.crakac.ofutoon.databinding.ListItemFollowedNotificationBinding
import com.crakac.ofutoon.ui.adapter.RefreshableAdapter
import com.crakac.ofutoon.util.TextUtil

class FollowedNotificationViewHolder(private val binding: ListItemFollowedNotificationBinding) :
    RecyclerView.ViewHolder(binding.root), RefreshableAdapter.Refreshable {
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

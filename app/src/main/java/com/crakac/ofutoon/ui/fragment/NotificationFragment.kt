package com.crakac.ofutoon.ui.fragment

import com.crakac.ofutoon.HasTitle
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.api.entity.Notification
import com.crakac.ofutoon.ui.adapter.NotificationAdapter
import com.crakac.ofutoon.ui.adapter.RefreshableAdapter
import retrofit2.Call

class NotificationFragment : MastodonApiFragment<Notification, List<Notification>>(), HasTitle {
    override fun createAdapter(): RefreshableAdapter<Notification> = NotificationAdapter()

    override fun refreshCall(): Call<List<Notification>> = Mastodon.api.getNotifications(prev)

    override fun onRefreshSuccess(response: List<Notification>) {
        insertQuietly(response)
    }

    override fun loadMoreCall(): Call<List<Notification>> = Mastodon.api.getNotifications(prev)

    override fun onLoadMoreSuccess(response: List<Notification>) {
        adapter.addBottom(response)
    }

    override fun getTitle(): CharSequence = "通知"
}
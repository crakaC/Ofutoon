package com.crakac.ofutoon.ui.fragment

import com.crakac.ofutoon.HasTitle
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.api.entity.Status
import retrofit2.Call

class HomeTimelineFragment : StatusFragment(), HasTitle {
    override fun refreshCall(): Call<List<Status>> =
        Mastodon.api.getHomeTimeline(prev)

    override fun loadMoreCall(): Call<List<Status>> =
        Mastodon.api.getHomeTimeline(next)

    override fun getTitle(): CharSequence =
        "ホーム"
}

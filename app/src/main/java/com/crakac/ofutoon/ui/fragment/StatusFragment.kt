package com.crakac.ofutoon.ui.fragment

import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.ui.adapter.RefreshableAdapter
import com.crakac.ofutoon.ui.adapter.StatusAdapter

abstract class StatusFragment : MastodonApiFragment<Status, List<Status>>() {
    override fun createAdapter(): RefreshableAdapter<Status> {
        return StatusAdapter()
    }

    override fun onRefreshSuccess(response: List<Status>) {
        insertQuietly(response)
    }

    override fun onLoadMoreSuccess(response: List<Status>) {
        adapter.addBottom(response)
    }
}

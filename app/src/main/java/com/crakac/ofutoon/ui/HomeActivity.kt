package com.crakac.ofutoon.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.api.entity.MastodonCallback
import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.ui.adapter.StatusAdapter
import com.crakac.ofutoon.ui.widget.FastScrollLinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    companion object {
        const val ACTION_RELOAD = "action_reload"
    }

    private val statusAdapter = StatusAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh.recyclerView.adapter = statusAdapter

        val layoutManager = FastScrollLinearLayoutManager(this)
        swipeRefresh.recyclerView.layoutManager = layoutManager

        val divider = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation).apply {
            setDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.divider)!!)
        }
        swipeRefresh.recyclerView.addItemDecoration(divider)

        Mastodon.api.getHomeTimeline().enqueue(
            object : MastodonCallback<List<Status>> {
                override fun onSuccess(result: List<Status>) {
                    statusAdapter.add(result)
                    result.forEach {
                        Log.i("media_attachments", it.content + "\n" + Gson().toJson(it.mediaAttachments))
                    }
                }
            }
        )
    }
}

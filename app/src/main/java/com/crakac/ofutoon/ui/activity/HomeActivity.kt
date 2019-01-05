package com.crakac.ofutoon.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.ui.adapter.MyFragmentPagerAdapter
import com.crakac.ofutoon.ui.fragment.HomeTimelineFragment
import com.crakac.ofutoon.ui.fragment.LocalTimelineFragment
import com.crakac.ofutoon.ui.fragment.MastodonApiFragment
import com.crakac.ofutoon.ui.fragment.NotificationFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    companion object {
        const val ACTION_RELOAD = "action_reload"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = Mastodon.api.user?.domain
        setSupportActionBar(toolbar)
        val adapter = MyFragmentPagerAdapter(supportFragmentManager).apply {
            add(NotificationFragment())
            add(HomeTimelineFragment())
            add(LocalTimelineFragment())
        }
        pager.adapter = adapter
        pager.currentItem = 1
        tab.setupWithViewPager(pager)
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                val fragment = adapter.instantiateItem(pager, tab.position) as MastodonApiFragment<*, *>
                fragment.scrollToTop()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {}
        })
    }
}
package com.crakac.ofutoon.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.library.baseAdapters.BR
import com.crakac.ofutoon.C
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.MastodonCallback
import com.crakac.ofutoon.databinding.ListItemUserHeaderBinding
import com.crakac.ofutoon.db.AppDatabase
import com.crakac.ofutoon.db.User
import com.crakac.ofutoon.ui.adapter.MyFragmentPagerAdapter
import com.crakac.ofutoon.ui.adapter.UserAdapter
import com.crakac.ofutoon.ui.fragment.HomeTimelineFragment
import com.crakac.ofutoon.ui.fragment.LocalTimelineFragment
import com.crakac.ofutoon.ui.fragment.MastodonApiFragment
import com.crakac.ofutoon.ui.fragment.NotificationFragment
import com.crakac.ofutoon.util.PrefsUtil
import com.crakac.ofutoon.util.TextUtil
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : AppCompatActivity() {
    companion object {
        const val ACTION_RELOAD = "action_reload"
    }

    lateinit var drawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar.title = Mastodon.api.user?.domain
        setSupportActionBar(toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(drawerToggle)

        navigationView.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        val userAdapter = UserAdapter(this)
        userAdapter.onClickUserListener = { user ->
            switchUser(user)
        }
        drawerList.adapter = userAdapter
        drawerList.addFooterView(View.inflate(this, R.layout.list_item_add_user, null).apply {
            setOnClickListener {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                intent.action = LoginActivity.ACTION_ADD_ACCOUNT
                startActivity(intent)
            }
        })

        val headerBinding = ListItemUserHeaderBinding.bind(drawerHeader)
        headerBinding.setVariable(BR.user, Mastodon.api.user)
        val account = Mastodon.api.user!!.account
        headerBinding.displayName.text = TextUtil.emojify(headerBinding.displayName, account.displayName, account.emojis)
        Mastodon.api.getCurrentAccount().enqueue(object : MastodonCallback<Account> {
            override fun onSuccess(result: Account) {
                Mastodon.api.user?.let { user ->
                    AppDatabase.execute {
                        user.account = result
                        user.accountJson = Gson().toJson(result)
                        AppDatabase.instance.userDao().update(user)
                    }
                }
                headerBinding.setVariable(BR.user, Mastodon.api.user)
                headerBinding.displayName.text = TextUtil.emojify(headerBinding.displayName, result.displayName, result.emojis)
            }
        })

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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onNewIntent(intent: Intent?) {
        if(intent?.action == ACTION_RELOAD){
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun switchUser(user: User) {
        PrefsUtil.putInt(C.CURRENT_USER_ID, user.id)
        Mastodon.initialize(user)
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
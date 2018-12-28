package com.crakac.ofutoon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crakac.ofutoon.R

class HomeActivity : AppCompatActivity() {
    companion object {
        const val ACTION_RELOAD = "action_reload"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

package com.crakac.ofutoon.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crakac.ofutoon.api.Mastodon

class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mastodon.currentAccount { user ->
            if (user == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Mastodon.initialize(user)
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()
        }
    }
}

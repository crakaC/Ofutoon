package com.crakac.ofutoon

import android.app.Application
import com.crakac.ofutoon.db.AppDatabase
import com.crakac.ofutoon.util.PrefsUtil

class OfutoonApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PrefsUtil.init(this)
        AppDatabase.init(this)
    }
}
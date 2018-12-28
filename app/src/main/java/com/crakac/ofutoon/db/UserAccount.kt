package com.crakac.ofutoon.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class UserAccount(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: Long = 0,
    var avatar: String = "",
    var name: String = "",
    var displayName: String = "",
    var domain: String = "",
    var token: String = ""
)
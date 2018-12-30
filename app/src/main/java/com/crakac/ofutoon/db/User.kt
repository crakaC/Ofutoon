package com.crakac.ofutoon.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: Long = 0,
    var avatar: String = "",
    var name: String = "",
    var displayName: String = "",
    var domain: String = "",
    var token: String = ""
)
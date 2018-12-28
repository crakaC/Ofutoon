package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Conversation {
    @SerializedName("id")
    val id = 0L
    @SerializedName("accounts")
    val accounts: List<Account> = emptyList()
    @SerializedName("last_status")
    val lastStatus: Status? = null
    @SerializedName("unread")
    val isUnread = false
}
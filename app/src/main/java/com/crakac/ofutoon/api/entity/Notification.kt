package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Notification : Identifiable() {
    enum class Type(val value: String) {
        Mention("mention"),
        ReBlog("reblog"),
        Favourite("favourite"),
        Follow("follow")
    }

    @SerializedName("type")
    val type: String = Type.Mention.value
    @SerializedName("created_at")
    val createdAt: String = ""
    @SerializedName("account")
    val account = Account()
    @SerializedName("status")
    val status: Status? = null

    fun getType() = Type.values().first { e -> e.value == type }
}
package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Relationship {
    @SerializedName("id")
    val id = 0L
    @SerializedName("following")
    val following = false
    @SerializedName("followed_by")
    val followedBy = false
    @SerializedName("blocking")
    val blocking = false
    @SerializedName("muting")
    val muting = false
    @SerializedName("muting_notification")
    val mutingNotification = false
    @SerializedName("requested")
    val requested = false
    @SerializedName("domain_blocking")
    val domainBlocking = false
    @SerializedName("showing_reblogs")
    val showingReblogs = true
    @SerializedName("endorsed")
    val endorsed = false
}
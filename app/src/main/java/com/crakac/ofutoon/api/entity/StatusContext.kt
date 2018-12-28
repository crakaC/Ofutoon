package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class StatusContext {
    val TAG: String = "StatusContext"
    @SerializedName("ancestors")
    val ancestors: List<Status> = emptyList()
    @SerializedName("descendants")
    val descendants: List<Status> = emptyList()
}
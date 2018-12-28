package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class AccessToken {
    @SerializedName("access_token")
    val accessToken: String = ""
    @SerializedName("token_type")
    val tokenType: String? = null
    @SerializedName("scope")
    val scope: String? = null
    @SerializedName("createdAt")
    val createdAt: Long? = null
}
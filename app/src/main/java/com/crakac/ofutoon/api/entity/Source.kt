package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Source {
    @SerializedName("privacy")
    val privacy: String? = null
    @SerializedName("sensitive")
    val isSensitive: Boolean? = null
    @SerializedName("language")
    val language: String? = null
    @SerializedName("note")
    val note = ""
    @SerializedName("fields")
    val fields: List<Field> = emptyList()
}
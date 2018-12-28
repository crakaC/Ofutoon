package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Attachment {
    enum class Type(val value: String) {
        Unknown("unknown"),
        Image("image"),
        Video("video"),
        Gifv("gifv")
    }

    @SerializedName("id")
    val id: Long = 0L
    @SerializedName("type")
    val type: String = Type.Image.value
    @SerializedName("url")
    val url: String = ""
    @SerializedName("remote_url")
    val remoteUrl: String? = null
    @SerializedName("preview_url")
    val previewUrl: String = ""
    @SerializedName("text_url")
    val textUrl: String? = null
    //    @SerializedName("meta")
//    val meta
    @SerializedName("description")
    val description: String? = null

    fun getType(): Type = Type.values().first { e -> e.value == type }
}
package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Card {
    enum class Type(val value: String) {
        Link("link"),
        Photo("photo"),
        Video("video"),
        Rich("rich")
    }

    @SerializedName("url")
    val url: String = ""
    @SerializedName("title")
    val title: String = ""
    @SerializedName("description")
    val description: String = ""
    @SerializedName("image")
    val image: String? = null
    @SerializedName("type")
    val type: String = Type.Link.value
    @SerializedName("author_name")
    val authorName: String? = null
    @SerializedName("author_url")
    val authorUrl: String? = null
    @SerializedName("provider_name")
    val providerName: String? = null
    @SerializedName("provider_url")
    val providerUrl: String? = null
    @SerializedName("html")
    val html: String? = null
    @SerializedName("width")
    val width = 0
    @SerializedName("height")
    val height = 0
}
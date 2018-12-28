package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Instance {
    @SerializedName("uri")
    val uri: String = ""
    @SerializedName("title")
    val title: String = ""
    @SerializedName("description")
    val description: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("version")
    val version: String = ""
    @SerializedName("thumbnail")
    val thumbnailUrl: String? = null
    @SerializedName("urls")
    val streamingApi = StreamingApi()
    @SerializedName("stats")
    val stats = Stats()
    @SerializedName("languages")
    val languages: List<String> = emptyList()
    @SerializedName("contact_account")
    val contactAccount: Account? = null

    class StreamingApi {
        @SerializedName("streaming_api")
        val endpoint: String = ""
    }

    class Stats{
        @SerializedName("user_count")
        val userCount = 0L
        @SerializedName("status_count")
        val statusCount = 0L
        @SerializedName("domain_count")
        val domainCount = 0L
    }
}

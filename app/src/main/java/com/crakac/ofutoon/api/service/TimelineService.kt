package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Conversation
import com.crakac.ofutoon.api.entity.Status
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TimelineService {
    @GET("/api/v1/timelines/home")
    fun getHomeTimeline(
        @QueryMap
        paging: Map<String, String>? = emptyMap(),
        @Query("limit")
        limit: Int = 20
    ): Call<List<Status>>

    @GET("/api/v1/conversations")
    fun getConversations(
        @QueryMap
        paging: Map<String, String>? = emptyMap(),
        @Query("limit")
        limit: Int = 20
    ): Call<List<Conversation>>

    @GET("/api/v1/timelines/public")
    fun getPublicTimeline(
        @Query("local")
        isLocal: Boolean? = null,
        @Query("only_media")
        onlyMedia: Boolean? = null,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 20
    ): Call<List<Status>>

    @GET("/api/v1/timelines/tag/{hashtag}")
    fun getHashtagTimeline(
        @Path("hashtag")
        tag: String,
        @Query("local")
        localOnly: Boolean? = null,
        @Query("only_media")
        onlyMedia: Boolean? = null,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 20
    ): Call<List<Status>>

    @GET("/api/v1/timelines/list/{id}")
    fun getListTimeline(
        @Path("id")
        listId: Long,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 20
    ): Call<List<Status>>
}
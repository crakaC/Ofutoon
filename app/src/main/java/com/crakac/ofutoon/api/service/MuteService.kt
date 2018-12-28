package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.Relationship
import com.crakac.ofutoon.api.entity.Status
import retrofit2.Call
import retrofit2.http.*

interface MuteService {
    @GET("/api/v1/mutes")
    fun getMutingAccounts(
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    @FormUrlEncoded
    @POST("/api/v1/accounts/{id}/mute")
    fun muteAccount(
        @Path("id")
        accountId: Long,
        @Field("notifications")
        muteNotification: Boolean? = null
    ): Call<Relationship>

    @POST("/api/v1/accounts/{id}/unmute")
    fun unmuteAccount(
        @Path("id")
        accountId: Long
    ): Call<Relationship>

    @POST("/api/v1/statuses/{id}/mute")
    fun muteConversation(
        @Path("id")
        statusId: Long
    ): Call<Status>

    @POST("/api/v1/statuses/{id}/unmute")
    fun unmuteConversation(
        @Path("id")
        statusId: Long
    ): Call<Status>
}
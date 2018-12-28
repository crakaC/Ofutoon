package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import retrofit2.Call
import retrofit2.http.*

interface FollowRequestService {
    @GET("/api/v1/follow_requests")
    fun getFollowRequests(
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    @POST("/api/v1/follow_requests/{id}/authorize")
    fun authorizeFollowRequest(
        @Path("id")
        id: Long
    ): Call<Unit>

    @POST("/api/v1/follow_requests/{id}/reject")
    fun rejectFollowRequest(
        @Path("id")
        id: Long
    ): Call<Unit>
}
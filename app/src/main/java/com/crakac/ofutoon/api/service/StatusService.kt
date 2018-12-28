package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.Card
import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.api.entity.StatusContext
import com.crakac.ofutoon.api.parameter.StatusParam
import retrofit2.Call
import retrofit2.http.*

interface StatusService {

    @GET("/api/v1/statuses/{id}")
    fun getStatus(
        @Path("id")
        id: Long
    ): Call<Status>

    @GET("/api/v1/statuses/{id}/context")
    fun getStatusContext(
        @Path("id")
        id: Long
    ): Call<StatusContext>

    @GET("/api/v1/statuses/{id}/card")
    fun getCard(
        @Path("id")
        id: Long
    ): Call<Card>

    @GET("/api/v1/statuses/{id}/reblogged_by")
    fun getRebloggedByAccounts(
        @Path("id")
        id: Long,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    @GET("/api/v1/statuses/{id}/favourited_by")
    fun getFavouritedByAccounts(
        @Path("id")
        id: Long,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    /*
        Idempotency
        In order to prevent duplicate statuses, this endpoint accepts an Idempotency-Key header,
        which should be set to a unique string for each new status.
        In the event of a network error, a request can be retried with the same Idempotency-Key.
        Only one status will be created regardless of how many requests with the same Idempotency-Key did go through.

        See https://stripe.com/blog/idempotency for more on idempotency and idempotency keys.
     */
    @POST("/api/v1/statuses")
    fun postStatus(
        @Body
        param: StatusParam
    ): Call<Status>


    @DELETE("/api/v1/statuses/{id}")
    fun deleteStatus(
        @Path("id")
        id: Long
    ): Call<Unit>

    @POST("/api/v1/statuses/{id}/reblog")
    fun reblogStatus(
        @Path("id")
        id: Long
    ): Call<Status>

    @POST("/api/v1/statuses/{id}/unreblog")
    fun unreblogStatus(
        @Path("id")
        id: Long
    ): Call<Status>

    @POST("/api/v1/statuses/{id}/pin")
    fun pinStatus(
        @Path("id")
        id: Long
    ): Call<Status>

    @POST("/api/v1/statuses/{id}/unpin")
    fun unpinStatus(
        @Path("id")
        id: Long
    ): Call<Status>

}
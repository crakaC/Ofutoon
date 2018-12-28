package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.Relationship
import retrofit2.Call
import retrofit2.http.*

interface BlockService {

    @GET("/api/v1/blocks")
    fun getBlockingAccounts(
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>


    @POST("/api/v1/accounts/{id}/block")
    fun block(
        @Path("id")
        accountId: Long
    ): Call<Relationship>

    @POST("/api/v1/accounts/{id}/unblock")
    fun unblock(
        @Path("id")
        accountId: Long
    ): Call<Relationship>

}
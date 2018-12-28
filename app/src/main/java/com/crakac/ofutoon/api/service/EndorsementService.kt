package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.Relationship
import retrofit2.Call
import retrofit2.http.*

interface EndorsementService {
    @GET("/api/v1/endorsements")
    fun getEndorsements(
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    @POST("/api/v1/accounts/{id}/pin")
    fun endorseAccount(
        @Path("id")
        accountId: Long
    ): Call<Relationship>

    @POST("/api/v1/accounts/{id}/unpin")
    fun unendorseAccount(
        @Path("id")
        accountId: Long
    ): Call<Relationship>
}
package com.crakac.ofutoon.api.service

import retrofit2.Call
import retrofit2.http.*

interface DomainBlockService {
    @GET("/api/v1/domain_blocks")
    fun getDomainBlocks(
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<String>>


    @FormUrlEncoded
    @POST("/api/v1/domain_blocks")
    fun block(
        @Field("domain")
        domain: String
    ): Call<Unit>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/api/v1/domain_blocks", hasBody = true)
    fun unblock(
        @Field("domain")
        domain: String
    ): Call<Unit>
}
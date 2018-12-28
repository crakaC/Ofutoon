package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Results
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("/api/v1/search")
    fun search(
        @Query("q")
        query: String,
        @Query("resolve")
        resolveNonLocalAccount: Boolean? = null
    ): Call<Results>

    @GET("/api/v2/search")
    fun searchV2(
        @Query("q")
        query: String,
        @Query("resolve")
        resolveNonLocalAccount: Boolean? = null
    ): Call<Results>
}
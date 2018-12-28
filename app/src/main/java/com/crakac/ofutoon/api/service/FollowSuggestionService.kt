package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface FollowSuggestionService {
    @GET("/api/v1/suggestions")
    fun getFollowSuggestions(): Call<List<Account>>

    @DELETE("/api/v1/suggestions/{id}")
    fun deleteFollowSuggestion(
        @Path("id")
        accountId: Long
    ): Call<Unit>

}
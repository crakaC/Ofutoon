package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Status
import retrofit2.Call
import retrofit2.http.*

interface FavouriteService {

    @GET("/api/v1/favourites")
    fun getFavourites(
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Status>>

    @POST("/api/v1/statuses/{id}/favourite")
    fun favouriteStatus(
        @Path("id")
        id: Long
    ): Call<Status>

    @POST("/api/v1/statuses/{id}/unfavourite")
    fun unfavouriteStatus(
        @Path("id")
        id: Long
    ): Call<Status>

}
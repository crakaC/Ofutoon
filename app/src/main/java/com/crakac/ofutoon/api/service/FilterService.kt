package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Filter
import com.crakac.ofutoon.api.parameter.FilterParam
import retrofit2.Call
import retrofit2.http.*

interface FilterService {
    @GET("/api/v1/filters")
    fun getFilters(): Call<List<Filter>>

    @POST("/api/v1/filters")
    fun createFilter(
        @Body
        filter: FilterParam
    ): Call<Filter>

    @GET("/api/v1/filters/{id}")
    fun getFilter(
        @Path("id")
        filterId: Long
    ): Call<Filter>

    @PUT("/api/v1/filters/{id}")
    fun updateFilter(
        @Path("id")
        filterId: Long,
        @Body
        filter: FilterParam
    ): Call<Filter>

    @DELETE("/api/v1/filters/{id}")
    fun deleteFilter(
        @Path("id")
        filterId: Long
    ): Call<Unit>
}
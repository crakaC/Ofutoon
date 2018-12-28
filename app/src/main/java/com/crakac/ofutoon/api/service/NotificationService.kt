package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Notification
import retrofit2.Call
import retrofit2.http.*

interface NotificationService {

    @GET("/api/v1/notifications")
    fun getNotifications(
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 20,
        @Query("exclude_types")
        excludeTypes: List<String>? = null
    ): Call<List<Notification>>

    @GET("/api/v1/notifications/{id}")
    fun getNotification(
        @Path("id")
        id: Long
    ): Call<Notification>

    @POST("/api/v1/notifications/clear")
    fun clearNotification(): Call<Unit>

    @FormUrlEncoded
    @POST("/api/v1/notifications/dismiss")
    fun dismissNotification(
        @Field("id")
        notificationId: Long
    ): Call<Unit>

    // @POST ("/api/v1/push/subscription")
    // @GET ("/api/v1/push/subscription")
    // @PUT ("/api/v1/push/subscription")
    // @DELETE ("/api/v1/push/subscription")
}
package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.AppCredentials
import com.crakac.ofutoon.api.entity.Application
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AppService {

    @FormUrlEncoded
    @POST("/api/v1/apps")
    fun registerApplication(
        @Field("client_name")
        clientName: String,
        @Field("redirect_uris")
        redirectUris: String,
        @Field("scopes")
        scopes: String,
        @Field("website")
        website: String
    ): Call<AppCredentials>

    @GET("/api/v1/apps/verify_credentials")
    fun verifyCredentials(): Call<Application>
}
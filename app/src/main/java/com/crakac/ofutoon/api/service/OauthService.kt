package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.AccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Kosuke on 2017/04/27.
 */
interface OauthService {

    @FormUrlEncoded
    @POST("oauth/token")
    fun fetchAccessToken(
        @Field("client_id")
        clientId: String,
        @Field("client_secret")
        clientSecret: String,
        @Field("redirect_uri")
        redirectUri: String,
        @Field("code")
        code: String,
        @Field("grant_type")
        grantType: String
    ): Call<AccessToken>

    @FormUrlEncoded
    @POST("oauth/token")
    fun fetchAccessTokenByPassword(
        @Field("client_id")
        clientId: String,
        @Field("client_secret")
        clientSecret: String,
        @Field("grant_type")
        grantType: String,
        @Field("username")
        userName: String,
        @Field("password")
        password: String,
        @Field("scope")
        scope: String
    ): Call<AccessToken>
}
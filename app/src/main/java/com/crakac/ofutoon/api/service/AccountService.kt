package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.Relationship
import com.crakac.ofutoon.api.entity.Status
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface AccountService {
    @GET("/api/v1/accounts/{id}")
    fun getAccount(
        @Path("id")
        id: Long
    ): Call<Account>

    @GET("/api/v1/accounts/verify_credentials")
    fun getCurrentAccount(): Call<Account>

    @FormUrlEncoded
    @PATCH("/api/v1/accounts/update_credentials")
    fun updateAccountCredentials(
        @Field("display_name")
        displayName: String? = null,
        @Field("note")
        note: String? = null,
        @Field("locked")
        isLocked: Boolean? = null,
        @Field("source[privacy]")
        defaultPrivacy: String? = null,
        @Field("source[sensitive]")
        defaultSensitive: Boolean? = null,
        @Field("source[language]")
        defaultLanguage: String? = null,
        @FieldMap
        fieldsAttributes: Map<String, String> = emptyMap()
    ): Call<Account>

    @Multipart
    @PATCH("/api/v1/accounts/update_credentials")
    fun updateAvatar(
        @Part
        avatar: MultipartBody.Part
    ): Call<Account>

    @Multipart
    @PATCH("/api/v1/accounts/update_credentials")
    fun updateHeader(
        @Part
        header: MultipartBody.Part
    ): Call<Account>

    @GET("/api/v1/accounts/{id}/followers")
    fun getFollowers(
        @Path("id")
        accountId: Long,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    @GET("/api/v1/accounts/{id}/following")
    fun getFollowings(
        @Path("id")
        accountId: Long,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    @GET("/api/v1/accounts/{id}/statuses")
    fun getStatuses(
        @Path("id")
        accountId: Long,
        @Query("only_media")
        onlyMedia: Boolean? = null,
        @Query("pinned")
        onlyPinned: Boolean? = null,
        @Query("exclude_replies")
        excludeReplies: Boolean? = null,
        @QueryMap
        paging: Map<String, String> = emptyMap(),
        @Query("limit")
        limit: Int = 20
    ): Call<List<Status>>

    @FormUrlEncoded
    @POST("/api/v1/accounts/{id}/follow")
    fun follow(
        @Path("id")
        accountId: Long,
        @Field("reblogs")
        showReblogsInHomeTimeline: Boolean? = null
    ): Call<Relationship>

    @POST("/api/v1/accounts/{id}/unfollow")
    fun unfollow(
        @Path("id")
        accountId: Long
    ): Call<Relationship>

    @GET("/api/v1/accounts/relationships")
    fun getRelationships(
        @Query("id[]", encoded = true)
        accountIds: List<Long>
    ): Call<List<Relationship>>

    @GET("/api/v1/accounts/search")
    fun searchAccounts(
        @Query("q")
        query: String,
        @Query("limit")
        limit: Int = 40,
        @Query("following")
        onlyFollowing: Boolean? = null
    ): Call<List<Account>>
}
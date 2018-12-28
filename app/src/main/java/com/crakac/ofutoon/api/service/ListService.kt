package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Account
import com.crakac.ofutoon.api.entity.MastodonList
import retrofit2.Call
import retrofit2.http.*

interface ListService {
    @GET("/api/v1/lists")
    fun getLists(): Call<List<MastodonList>>

    @GET("/api/v1/accounts/{id}/lists")
    fun getListsOfAccount(
        @Path("id")
        accountId: Long
    ): Call<List<MastodonList>>

    /**
     * If you specify a limit of 0 in the query, all accounts will be returned without pagination.
     * Otherwise, standard account pagination rules apply.
     */
    @GET("/api/v1/lists/{id}/accounts")
    fun getAccountsInList(
        @Path("id")
        listId: Long,
        @Query("limit")
        limit: Int = 40
    ): Call<List<Account>>

    @GET("/api/v1/lists/{id}")
    fun getList(
        @Path("id")
        listId: Long
    ): Call<MastodonList>

    @FormUrlEncoded
    @POST("/api/v1/lists")
    fun createList(
        @Field("title")
        title: String
    ): Call<MastodonList>

    @FormUrlEncoded
    @PUT("/api/v1/lists/{id}")
    fun updateListName(
        @Path("id")
        listId: Long,
        @Field("title")
        title: String
    ): Call<MastodonList>

    @DELETE("/api/v1/lists/{id}")
    fun deleteList(
        @Path("id")
        listId: Long
    ): Call<Unit>

    /**
     * Only accounts already followed by the user can be added to a list.
     */
    @FormUrlEncoded
    @POST("/api/v1/lists/{id}/accounts")
    fun addAccountsToList(
        @Path("id")
        listId: Long,
        @Field("account_ids[]")
        accountIds: List<Long>
    ): Call<Unit>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/api/v1/lists/{id}/accounts", hasBody = true)
    fun removeAccountsFromList(
        @Path("id")
        listId: Long,
        @Field("account_ids[]")
        accountIds: List<Long>
    ): Call<Unit>
}
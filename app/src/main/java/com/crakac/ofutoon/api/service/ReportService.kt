package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Report
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ReportService {

    /**
     * comment is up to 1,000 characters
     */
    @FormUrlEncoded
    @POST("/api/v1/reports")
    fun report(
        @Field("account_id")
        accountId: Long,
        @Field("status_ids[]")
        statusIds: List<Long>? = null,
        @Field("comment")
        comment: String?
    ): Call<Report>
}
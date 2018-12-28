package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import org.junit.Assert
import org.junit.Test

class ReportServiceTest : ApiTestBase() {
    val api = baseApi as ReportService

    @Test
    fun report() {
        val statuses = baseApi.getStatuses(BuildConfig.DEBUG_ACCOUNT_ID, limit = 5).execute().body()!!
        Assert.assertTrue(
            api.report(
                BuildConfig.DEBUG_ACCOUNT_ID,
                statuses.map { s -> s.id },
                "something awful"
            ).execute().isSuccessful
        )
    }
}
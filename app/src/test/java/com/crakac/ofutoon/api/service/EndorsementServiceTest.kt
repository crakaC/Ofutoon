package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import org.junit.Assert
import org.junit.Test

class EndorsementServiceTest : ApiTestBase() {
    val api = baseApi as EndorsementService

    @Test
    fun endorse() {
        val targetId = BuildConfig.DEBUG_ACCOUNT_ID
        api.unendorseAccount(targetId).execute()

        run {
            val response = api.endorseAccount(targetId).execute()
            Assert.assertTrue(response.isSuccessful)
            Assert.assertTrue(response.body()!!.endorsed)
        }
        run {
            val accounts = api.getEndorsements().execute().body()!!
            Assert.assertTrue(accounts.map { a -> a.id }.contains(targetId))
        }
        run {
            api.unendorseAccount(targetId).execute()
            val accounts = api.getEndorsements().execute().body()!!
            Assert.assertFalse(accounts.map { a -> a.id }.contains(targetId))
        }
    }
}
package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import org.junit.Assert
import org.junit.Test

class BlockServiceTest: ApiTestBase() {
    val api = baseApi as BlockService
    @Test
    fun block() {
        run {
            val r = api.block(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
        run {
            val r = api.unblock(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }

    @Test
    fun getBlockingAccounts() {
        val r = api.getBlockingAccounts(pagingInt.q).execute()
        Assert.assertTrue(r.isSuccessful)
    }
}
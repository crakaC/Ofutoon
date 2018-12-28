package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import org.junit.Assert
import org.junit.Test

class MuteServiceTest: ApiTestBase() {
    val api = baseApi as MuteService

    @Test
    fun muteAccount() {
        run {
            val r = api.muteAccount(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)

            val accounts = api.getMutingAccounts().execute().body()!!
            Assert.assertTrue(accounts.map { account -> account.id }.contains(BuildConfig.DEBUG_ACCOUNT_ID))
        }
        run {
            val r = api.unmuteAccount(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)

            val accounts = api.getMutingAccounts().execute().body()!!
            Assert.assertFalse(accounts.map { account -> account.id }.contains(BuildConfig.DEBUG_ACCOUNT_ID))
        }
    }

    @Test
    fun muteConversation() {
        run {
            val r = api.muteConversation(BuildConfig.DEBUG_STATUS_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
        run {
            val r = api.unmuteConversation(BuildConfig.DEBUG_STATUS_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }
}
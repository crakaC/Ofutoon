package com.crakac.ofutoon.api.service

import org.junit.Assert
import org.junit.Test

class DomainBlockServiceTest : ApiTestBase() {
    val api = baseApi as DomainBlockService

    @Test
    fun block() {
        val domain = "example.com"
        run {
            val response = api.block(domain).execute()
            Assert.assertTrue(response.isSuccessful)
        }
        run {
            val response = api.getDomainBlocks().execute()
            Assert.assertTrue(response.isSuccessful)
            Assert.assertTrue(response.body()!!.contains(domain))
        }
        run {
            val response = api.unblock(domain).execute()
            Assert.assertTrue(response.isSuccessful)
            val list = api.getDomainBlocks().execute().body()!!
            Assert.assertFalse(list.contains(domain))
        }

    }
}
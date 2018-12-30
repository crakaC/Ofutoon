package com.crakac.ofutoon.api.service

import org.junit.Assert
import org.junit.Test

class CustomEmojiServiceTest : ApiTestBase() {
    val api = baseApi as CustomEmojiService

    @Test
    fun getEmojis() {
        val response = api.getCustomEmojis().execute()
        Assert.assertTrue(response.isSuccessful)
        Assert.assertTrue(response.body()!!.isNotEmpty())
    }
}
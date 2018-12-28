package com.crakac.ofutoon.api.service

import org.junit.Assert
import org.junit.Test

class FollowSuggestionServiceTest: ApiTestBase() {
    val api = baseApi as FollowSuggestionService

    @Test
    fun suggestion(){
        val response = api.getFollowSuggestions().execute()
        Assert.assertTrue(response.isSuccessful)
    }

    @Test
    fun deleteSugesstion(){
        Assert.fail("suitable test is not existed")
        //Assert.assertTrue(api.deleteFollowSuggestion())
    }
}
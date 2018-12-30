package com.crakac.ofutoon.api.service

import org.junit.Assert
import org.junit.Test

class FollowRequestServiceTest : ApiTestBase() {
    val api = baseApi as FollowRequestService

    @Test
    fun getFollowRequests() {
        val r = api.getFollowRequests(pagingInt.q).execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun authorizeRequest() {
        Assert.fail("not yet")
    }

    @Test
    fun rejectRequest() {
        Assert.fail("not yet")
    }
}
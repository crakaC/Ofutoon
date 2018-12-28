package com.crakac.ofutoon.api.service

import org.junit.Assert
import org.junit.Test

class InstanceServiceTest : ApiTestBase() {
    val api = baseApi as InstanceService
    @Test
    fun instance() {
        val response = api.getInstanceInformation().execute()
        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(response.body())
        val instance = response.body()!!
        Assert.assertTrue(instance.streamingApi.endpoint.startsWith("ws"))
    }
}
package com.crakac.ofutoon.api.service

import org.junit.Assert
import org.junit.Test

class TimelineServiceTest : ApiTestBase() {
    val api = baseApi as TimelineService

    @Test
    fun hometimeline() {
        Assert.assertTrue(api.getHomeTimeline(pagingLong.q).execute().isSuccessful)
    }

    @Test
    fun publicTimeline() {
        Assert.assertTrue(api.getPublicTimeline(paging = pagingLong.q).execute().isSuccessful)
        Assert.assertTrue(api.getPublicTimeline(isLocal = true, paging = pagingLong.q).execute().isSuccessful)

        val statuses = api.getPublicTimeline(onlyMedia = true, paging = pagingLong.q).execute().body()!!
        Assert.assertTrue(statuses.none { it.mediaAttachments.isEmpty() })
    }

    @Test
    fun tagTimeline() {
        val statuses = api.getHashtagTimeline("test").execute().body()!!
        Assert.assertTrue(statuses.none { it.tags.isEmpty() })
    }

    @Test
    fun conversations(){
        Assert.assertTrue(api.getConversations().execute().isSuccessful)
    }

    @Test
    fun listTimeline(){
        val listId = baseApi.getLists().execute().body()!!.first().id
        Assert.assertTrue(api.getListTimeline(listId).execute().isSuccessful)
    }
}
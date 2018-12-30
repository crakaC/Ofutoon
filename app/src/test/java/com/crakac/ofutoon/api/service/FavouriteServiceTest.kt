package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import org.junit.Assert
import org.junit.Test

class FavouriteServiceTest : ApiTestBase() {
    val api = baseApi as FavouriteService

    @Test
    fun getFavorites() {
        Assert.assertTrue(
            api.getFavourites().execute().isSuccessful
        )
        Assert.assertTrue(
            api.getFavourites(pagingLong.q).execute().isSuccessful
        )
    }

    @Test
    fun favouriteStatus() {
        run {
            val r = api.favouriteStatus(BuildConfig.DEBUG_STATUS_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
        run {
            val r = api.unfavouriteStatus(BuildConfig.DEBUG_STATUS_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }

}
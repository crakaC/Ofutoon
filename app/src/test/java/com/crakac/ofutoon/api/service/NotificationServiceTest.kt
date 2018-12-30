package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import com.crakac.ofutoon.api.Mastodon
import org.junit.Assert
import org.junit.Test

class NotificationServiceTest : ApiTestBase() {
    val api = baseApi as NotificationService

    @Test
    fun getNotifications() {
        generateNotification()
        val notifications = api.getNotifications().execute().body()!!
        val notification = api.getNotification(notifications.first().id).execute().body()!!
        Assert.assertTrue(notifications.map { n -> n.id }.contains(notification.id))

        // Dismiss single notification
        Assert.assertTrue(api.dismissNotification(notification.id).execute().isSuccessful)
        Assert.assertFalse(api.getNotifications().execute().body()!!.map { n -> n.id }.contains(notification.id))

        // Clear
        Assert.assertTrue(api.clearNotification().execute().isSuccessful)
        Assert.assertTrue(api.getNotifications().execute().body()!!.isEmpty())
    }

    private fun generateNotification() {
        val genApi = Mastodon.create("localhost", BuildConfig.LOCAL_TOKEN_2)
        val statuses =
            baseApi.getStatuses(baseApi.getCurrentAccount().execute().body()!!.id, limit = 10).execute().body()!!
        statuses.forEach { s ->
            genApi.unreblogStatus(s.id).execute()
            genApi.unfavouriteStatus(s.id).execute()
            genApi.reblogStatus(s.id).execute()
            genApi.favouriteStatus(s.id).execute()
        }
    }
}
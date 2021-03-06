package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.api.entity.AppCredentials
import org.junit.Assert
import org.junit.Test

class AppServiceTest {
    val noTokenApi = Mastodon.RegisterService.create("localhost")

    @Test
    fun register() {
        val appName = "OfutodonTest"
        var credential: AppCredentials?
        var api: AppService? = null
        run {
            val response =
                noTokenApi.registerApplication(appName, "urn:ietf:wg:oauth:2.0:oob", "read", "https://example.com")
                    .execute()
            Assert.assertTrue(response.isSuccessful)
            credential = response.body()
        }

        credential?.let {
            val response = noTokenApi.fetchAccessTokenByPassword(
                it.clientId,
                it.clientSecret,
                "password",
                BuildConfig.TEST_USER,
                BuildConfig.TEST_PASSWORD,
                "read"
            ).execute()
            Assert.assertTrue(response.isSuccessful)
            val token = response.body()!!
            System.out.println(token.scope)
            api = Mastodon.create("localhost", token.accessToken)
        }

        api?.let {
            val response = it.verifyCredentials().execute()
            Assert.assertTrue(response.isSuccessful)
            Assert.assertEquals(appName, response.body()!!.name)
        }

    }
}
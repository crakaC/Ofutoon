package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import com.crakac.ofutoon.api.Mastodon
import com.crakac.ofutoon.api.Paging

open class ApiTestBase {
    val pagingLong = Paging(Long.MAX_VALUE, 0)
    val pagingInt = Paging(Int.MAX_VALUE.toLong(), 0)
    val baseApi = Mastodon.create("localhost", BuildConfig.LOCAL_TOKEN)

    fun randomString(length: Int = 10): String {
        return ('A'..'z').shuffled().subList(0, length).joinToString("")
    }
}
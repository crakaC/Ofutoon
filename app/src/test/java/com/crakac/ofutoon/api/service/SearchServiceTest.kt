package com.crakac.ofutoon.api.service

import com.google.gson.Gson
import org.junit.Test

class SearchServiceTest: ApiTestBase() {
    val api = baseApi as SearchService

    @Test
    fun search(){
        val result = api.search("test").execute().body()!!
        System.out.println(Gson().toJson(result))

        val resultV2 = api.searchV2("test").execute().body()!!
        System.out.println(Gson().toJson(resultV2))
    }
}
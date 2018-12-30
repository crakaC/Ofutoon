package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Filter.Context
import com.crakac.ofutoon.api.parameter.FilterParam
import org.junit.Assert
import org.junit.Test

class FilterServiceTest : ApiTestBase() {
    val api = baseApi as FilterService

    @Test
    fun create() {
        // Create new Filter
        val phrase = randomString()
        val contextList = listOf(Context.Home.value, Context.Public.value)
        val filterParam = FilterParam(phrase, contextList)
        val response = api.createFilter(filterParam).execute()
        Assert.assertTrue(response.isSuccessful)
        val filter = response.body()!!
        Assert.assertEquals(phrase, filter.phrase)
        Assert.assertTrue(filter.context.containsAll(contextList))

        // Get filter
        Assert.assertTrue(api.getFilter(filter.id).execute().isSuccessful)

        // Update Filter
        val newPhrase = randomString(20)
        val newContext = listOf(Context.Notifications.value)
        val newParam = FilterParam(newPhrase, newContext)
        val newFilter = api.updateFilter(filter.id, newParam).execute().body()!!
        Assert.assertEquals(filter.id, newFilter.id)
        Assert.assertEquals(newPhrase, newFilter.phrase)
        Assert.assertTrue(newFilter.context.containsAll(newContext))
        Assert.assertFalse(newFilter.context.contains(Context.Home.value) || newFilter.context.contains(Context.Public.value))

        // Delete Filter
        Assert.assertTrue(api.deleteFilter(newFilter.id).execute().isSuccessful)

        // Get All Filter
        run {
            val r = api.getFilters().execute()
            Assert.assertTrue(r.isSuccessful)
            val filters = r.body()!!
            Assert.assertFalse(filters.contains(newFilter))
        }
    }
}
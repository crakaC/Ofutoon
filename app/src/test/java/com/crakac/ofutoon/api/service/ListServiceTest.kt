package com.crakac.ofutoon.api.service

import org.junit.Assert
import org.junit.Test

class ListServiceTest: ApiTestBase() {
    val api = baseApi as ListService

    @Test
    fun create(){
        // Create new List
        val listName = randomString()
        val response = api.createList(listName).execute()
        Assert.assertTrue(response.isSuccessful)
        val list = response.body()!!
        Assert.assertEquals(listName, list.title)

        // Update ListName
        val newListName = randomString()
        Assert.assertEquals(
            newListName,
            api.updateListName(list.id, newListName).execute().body()!!.title
        )

        // Add account to list
        val userId = baseApi.getCurrentAccount().execute().body()!!.id
        val followingIds = baseApi.getFollowings(userId, limit = 10).execute().body()!!.map { a -> a.id }
        Assert.assertTrue(
            api.addAccountsToList(list.id, followingIds).execute().isSuccessful
        )

        // Check account in list
        Assert.assertTrue(
            api.getAccountsInList(list.id).execute().body()!!.map { account -> account.id }.containsAll(followingIds)
        )

        // Remove accounts from list
        Assert.assertTrue(
            api.removeAccountsFromList(list.id, followingIds).execute().isSuccessful
        )
        Assert.assertTrue(
            api.getAccountsInList(list.id).execute().body()!!.isEmpty()
        )

        // Delete List
        Assert.assertTrue(
            api.deleteList(list.id).execute().isSuccessful
        )
        Assert.assertFalse(
            api.getList(list.id).execute().isSuccessful
        )

        // Check
        val lists = api.getLists().execute().body()!!
        Assert.assertFalse(
            lists.map { l -> l.id }.contains(list.id)
        )
        followingIds.forEach { id ->
            val listsOfAccount = api.getListsOfAccount(id).execute().body()!!
            Assert.assertFalse(listsOfAccount.map { l -> l.id }.contains(list.id))
        }
    }
}
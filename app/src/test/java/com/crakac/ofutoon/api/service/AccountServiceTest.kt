package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import com.crakac.ofutoon.api.Link
import com.crakac.ofutoon.api.Pageable
import com.crakac.ofutoon.api.parameter.FieldParam
import com.crakac.ofutoon.api.parameter.FieldsAttributes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Test
import java.io.File

class AccountServiceTest : ApiTestBase() {
    val api = baseApi as AccountService
    @Test
    fun getAccount() {
        val r = api.getAccount(BuildConfig.DEBUG_ACCOUNT_ID).execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun getCurrentAccount() {
        val r = api.getCurrentAccount().execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun updateCredentials() {
        run {
            val displayName = randomString()
            val note = randomString(40)
            val r = api.updateAccountCredentials(displayName, note).execute()
            Assert.assertTrue(r.isSuccessful)
            val updated = r.body()!!
            Assert.assertEquals(displayName, updated.displayName)
            Assert.assertTrue(updated.note.contains(note))
        }
    }

    @Test
    fun updateFieldsAttributes(){
        run {
            val field = FieldParam(randomString(), randomString())
            val map = FieldsAttributes(listOf(field)).toFieldMap()
            val r = api.updateAccountCredentials(fieldsAttributes = map).execute()
            Assert.assertTrue(r.isSuccessful)
            val updated = r.body()!!
            Assert.assertEquals(field.name, updated.fields!![0].name)
            Assert.assertEquals(field.value, updated.fields!![0].value)
        }
    }

    @Test
    fun updateAvatar(){
        val avatarFile = imageFile("/icon.png")
        val reqFile = RequestBody.create(MediaType.parse("image/*"), avatarFile)
        val body = MultipartBody.Part.createFormData("avatar", avatarFile.name, reqFile)
        val response = api.updateAvatar(body).execute()
        Assert.assertTrue(response.isSuccessful)
    }

    @Test
    fun updateHeader(){
        val headerFile = imageFile("/lgtm.gif")
        val reqFile = RequestBody.create(MediaType.parse("image/*"), headerFile)
        val body = MultipartBody.Part.createFormData("header", headerFile.name, reqFile)
        val response = api.updateHeader(body).execute()
        Assert.assertTrue(response.isSuccessful)
    }

    @Test
    fun getFollowers() {
        //no paging
        run {
            val r = api.getFollowers(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }

        run {
            val r = api.getFollowers(BuildConfig.DEBUG_ACCOUNT_ID, pagingInt.q).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }

    @Test
    fun getFollowings() {
        run {
            val r = api.getFollowings(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
        run {
            val r = api.getFollowings(BuildConfig.DEBUG_ACCOUNT_ID, pagingInt.q).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }

    @Test
    fun getStatuses() {
        run {
            val r = api.getStatuses(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)
            val pageable = Pageable(r.body()!!, Link.parse(r.headers().get("link")))
            run {
                val r = api.getStatuses(BuildConfig.DEBUG_ACCOUNT_ID, paging = pageable.nextPaging().q).execute()
                Assert.assertTrue(r.isSuccessful)
            }
            run {
                val r = api.getStatuses(BuildConfig.DEBUG_ACCOUNT_ID, paging = pageable.prevPaging().q).execute()
                Assert.assertTrue(r.isSuccessful)
            }
        }
    }

    @Test
    fun getMediaOnlyStatus(){
        val r = api.getStatuses(1, true, false, false).execute()
        Assert.assertTrue(r.isSuccessful)
        val statuses = r.body()!!
        statuses.forEach {
            Assert.assertTrue(it.mediaAttachments.isNotEmpty())
        }
    }

    @Test
    fun followAndUnfollow() {
        run {
            val r = api.follow(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
        run {
            val r = api.unfollow(BuildConfig.DEBUG_ACCOUNT_ID).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }


    @Test
    fun getRelationships() {
        run {
            val r = api.getRelationships(listOf(1, BuildConfig.DEBUG_ACCOUNT_ID)).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }

    @Test
    fun searchAccounts() {
        val r = api.searchAccounts("test", 10).execute()
        Assert.assertTrue(r.isSuccessful)
        val accounts = r.body()!!
        Assert.assertTrue(accounts.size <= 10)
    }

    fun imageFile(fileName: String): File {
        return File(javaClass.getResource(fileName).path)
    }

}
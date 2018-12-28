package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.BuildConfig
import com.crakac.ofutoon.api.entity.Status
import com.crakac.ofutoon.api.parameter.StatusParam
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Test
import java.io.File

class StatusServiceTest : ApiTestBase() {
    val api = baseApi as StatusService

    private val statusId = BuildConfig.DEBUG_STATUS_ID

    @Test
    fun getStatus() {
        val r = api.getStatus(statusId).execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun getContext() {
        val r = api.getStatusContext(statusId).execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun getCard() {
        val r = api.getCard(statusId).execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun favouritedBy() {
        val r = api.getFavouritedByAccounts(statusId, pagingLong.q).execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun rebloggedBy() {
        val r = api.getRebloggedByAccounts(statusId, pagingLong.q).execute()
        Assert.assertTrue(r.isSuccessful)
    }

    @Test
    fun post() {
        var postedStatus: Status? = null

        //post
        run {
            val r = api.postStatus(StatusParam(text = "ハローハロー")).execute()
            Assert.assertTrue(r.isSuccessful)
            postedStatus = r.body()
        }

        //pin, unpinn

        run{
            Assert.assertTrue(api.pinStatus(postedStatus!!.id).execute().isSuccessful)
            Assert.assertTrue(api.unpinStatus(postedStatus!!.id).execute().isSuccessful)
        }

        //reblog, unreblog
        run {
            val r = api.reblogStatus(postedStatus!!.id).execute()
            Assert.assertTrue(r.isSuccessful)

            val u = api.unreblogStatus(postedStatus!!.id).execute()
            Assert.assertTrue(u.isSuccessful)
        }

        // delete
        run {
            val r = api.deleteStatus(postedStatus!!.id).execute()
            Assert.assertTrue(r.isSuccessful)
        }
    }

    @Test
    fun postWithMedia() {
        val f = File(javaClass.getResource("/lgtm.mp4").path)
        val reqFile = RequestBody.create(MediaType.parse("video/*"), f)
        val body = MultipartBody.Part.createFormData("file", f.name, reqFile)
        val attachment = baseApi.uploadMediaAttachment(body).execute().body()
        val r = api.postStatus(StatusParam(text = "( ˘ω˘)ｽﾔｧ", mediaIds = listOf(attachment!!.id))).execute()
        Assert.assertTrue(r.isSuccessful)
    }
}
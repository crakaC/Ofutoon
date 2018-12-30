package com.crakac.ofutoon.api.service

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Test
import java.io.File

class MediaServiceTest : ApiTestBase() {
    val api = baseApi as MediaAttachmentService

    @Test
    fun uploadAndUpdate() {
        val f = imageFile()
        Assert.assertNotNull(f)

        val reqFile = RequestBody.create(MediaType.parse("image/*"), f)
        val body = MultipartBody.Part.createFormData("file", f.name, reqFile)
        val r = api.uploadMediaAttachment(body).execute()
        Assert.assertTrue(r.isSuccessful)

        val attachment = r.body()!!
        run {
            val description = "TEST DESCRIPTION"
            val r = api.updateMediaAttachment(attachment.id, description, "0.1f,0.2f").execute()
            Assert.assertTrue(r.isSuccessful)
            Assert.assertEquals(description, r.body()!!.description)
        }
    }

    fun imageFile(): File {
        return File(javaClass.getResource("/icon.png").path)
    }

}
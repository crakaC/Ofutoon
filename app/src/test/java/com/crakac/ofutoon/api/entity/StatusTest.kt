package com.crakac.ofutoon.api.entity

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

class StatusTest {
    @Test
    fun attachments(){
        val jsonString = javaClass.getResourceAsStream("/status_with_attachment.json").bufferedReader().readText()
        val status = Gson().fromJson<Status>(jsonString, Status::class.java)
        Assert.assertTrue(status.mediaAttachments.isNotEmpty())
        System.out.println(Gson().toJson(status))
    }
}
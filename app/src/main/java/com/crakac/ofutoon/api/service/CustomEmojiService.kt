package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Emoji
import retrofit2.Call
import retrofit2.http.GET

interface CustomEmojiService {
    @GET("/api/v1/custom_emojis")
    fun getCustomEmojis(): Call<List<Emoji>>
}
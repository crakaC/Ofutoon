package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Attachment
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface MediaAttachmentService {
    @Multipart
    @POST("/api/v1/media")
    fun uploadMediaAttachment(
        @Part
        file: MultipartBody.Part,
        @Part("description")
        description: String? = null,
        @Part("focus")
        focus: String? = null
    ): Call<Attachment>

    @FormUrlEncoded
    @PUT("/api/v1/media/{id}")
    fun updateMediaAttachment(
        @Path("id")
        attachmentId: Long,
        @Field("description")
        description: String? = null,
        @Field("focus")
        focus: String? = null
    ): Call<Attachment>
}
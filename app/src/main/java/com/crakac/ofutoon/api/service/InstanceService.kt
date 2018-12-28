package com.crakac.ofutoon.api.service

import com.crakac.ofutoon.api.entity.Instance
import retrofit2.Call
import retrofit2.http.GET

interface InstanceService {
    @GET("/api/v1/instance")
    fun getInstanceInformation(): Call<Instance>
}
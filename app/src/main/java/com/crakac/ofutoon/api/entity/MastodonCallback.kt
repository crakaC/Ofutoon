package com.crakac.ofutoon.api.entity

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MastodonCallback<T> : Callback<T> {
    companion object {
        const val TAG ="MastodonCallback"
    }

    fun onNetworkAccessError(call: Call<T>, t: Throwable) {
        Log.w(TAG, t.toString())
    }

    fun onSuccess(result: T)

    fun onErrorResponse(call: Call<T>, response: Response<T>) {
        Log.w(TAG, "$call is failed")
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onNetworkAccessError(call, t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(response.body()!!)
        } else {
            onErrorResponse(call, response)
        }
    }
}
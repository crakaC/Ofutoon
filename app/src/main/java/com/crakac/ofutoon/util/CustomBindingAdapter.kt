package com.crakac.ofutoon.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class CustomBindingAdapter private constructor(){
    companion object {
        @BindingAdapter("imageUrl", "radius", requireAll = false)
        @JvmStatic
        fun loadImage(imageView: ImageView, url: String?, radius: Int?) {
            if (url == null) {
                GlideApp.with(imageView.context).clear(imageView)
                return
            }
            if (radius != null) {
                GlideApp.with(imageView.context).load(url).apply(RequestOptions().transform(RoundedCorners(radius)))
                    .into(imageView)
            } else {
                GlideApp.with(imageView.context).load(url).into(imageView)
            }
        }
    }

}
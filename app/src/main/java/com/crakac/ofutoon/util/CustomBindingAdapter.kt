package com.crakac.ofutoon.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class CustomBindingAdapter private constructor(){
    companion object {
        @BindingAdapter("imageUrl", "radius", "centerCrop", requireAll = false)
        @JvmStatic
        fun loadImage(imageView: ImageView, url: String?, radius: Int?, centerCrop: Boolean = false) {
            if (url == null) {
                GlideApp.with(imageView.context).clear(imageView)
                return
            }
            var options = RequestOptions()
            if(radius != null){
                options = options.transform(RoundedCorners(radius))
            }
            if(centerCrop){
                options = options.centerCrop()
            }
            GlideApp.with(imageView.context).load(url).apply(options).into(imageView)
        }
    }

}
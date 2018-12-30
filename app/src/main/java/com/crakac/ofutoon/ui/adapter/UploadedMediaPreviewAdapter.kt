package com.crakac.ofutoon.ui.adapter

import android.net.Uri
import android.widget.ProgressBar
import com.crakac.ofutoon.util.GlideApp
import com.github.chrisbanes.photoview.PhotoView

class UploadedMediaPreviewAdapter(val attachments: ArrayList<Uri>): PreviewAdapter(){
    override fun getCount(): Int {
        return attachments.count()
    }

    override fun setupPreview(photoView: PhotoView, progress: ProgressBar, position: Int) {
        val uri = attachments[position]
        GlideApp.with(photoView.context).load(uri).fitCenter().into(photoView)
    }
}
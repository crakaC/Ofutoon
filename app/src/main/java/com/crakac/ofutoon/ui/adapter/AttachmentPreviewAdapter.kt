package com.crakac.ofutoon.ui.adapter

import android.widget.ProgressBar
import com.crakac.ofutoon.api.entity.Attachment
import com.crakac.ofutoon.util.GlideApp
import com.github.chrisbanes.photoview.PhotoView

class AttachmentPreviewAdapter(val attachments: List<Attachment>) : PreviewAdapter() {
    override fun getCount(): Int {
        return attachments.count()
    }

    override fun setupPreview(photoView: PhotoView, progress: ProgressBar, position: Int) {
        val attachment = attachments[position]
        val url = if (attachment.url.isNotEmpty()) attachment.url else attachment.remoteUrl
        GlideApp.with(photoView.context).load(url).listener(PreviewLoadListener(photoView, progress)).into(photoView)
    }
}
package com.crakac.ofutoon.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.graphics.Palette
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.entity.Attachment
import com.crakac.ofutoon.util.GlideApp
import com.crakac.ofutoon.util.ViewUtil
import kotlinx.android.synthetic.main.inline_preview.view.*

class InlineImagePreview(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val TAG: String = "InlineImagePreview"

    companion object {
        private const val PREVIEW_MAX_NUM = 4
    }

    var medias: List<Attachment>? = null

    private val images: List<@JvmSuppressWildcards ImageView>

    private val separators: List<@JvmSuppressWildcards View>

    var listener: OnClickPreviewListener? = null

    init {
        View.inflate(context, R.layout.inline_preview, this)
        images = listOf(image1, image2, image3, image4)
        images.forEach { preview ->
            preview.setOnClickListener { v ->
                onClickPreview(v)
            }
        }
        separators = listOf(separatorVertical, separatorRight, separatorLeft)
        hideMediaButton.setOnClickListener { mediaMask.visibility = View.VISIBLE }
        mediaMask.setOnClickListener { mediaMask.visibility = View.GONE }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val margin = resources.getDimensionPixelSize(R.dimen.spacing_micro)
        val marginParam = layoutParams as MarginLayoutParams
        marginParam.topMargin = margin
        marginParam.bottomMargin = margin
        layoutParams = marginParam
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.makeMeasureSpec((MeasureSpec.getSize(widthMeasureSpec) * 9f / 16f + 0.5f).toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, height)
    }

    fun setMedia(attachments: List<Attachment>, isSensitive: Boolean) {
        if (isSensitive) {
            mediaMask.visibility = View.VISIBLE
            spoilerText.setText(R.string.sensitive_media)
        } else {
            mediaMask.visibility = View.GONE
            spoilerText.setText(R.string.hidden_insensitive_media)
        }

        medias = attachments
        images.forEach { e -> e.visibility = View.GONE }
        attachments.forEachIndexed { index, attachment ->
            val v = getImageView(index, attachments.count())
            GlideApp.with(context.applicationContext).asBitmap().load(attachment.previewUrl).centerCrop().listener(object : RequestListener<Bitmap> {
                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val bitmap = resource ?: return false
                        Palette.from(bitmap).generate { palette ->
                            v.foreground = ViewUtil.createRipple(palette, 0.25f, 0.5f, context.getColor(R.color.mid_grey), true)
                        }
                    }
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean = false
            }).into(v)

            v.visibility = View.VISIBLE
        }
        rightContainer.visibility = if (attachments.size >= 2) View.VISIBLE else View.GONE
        separators.forEach { e -> e.visibility = View.GONE }
        for (i in 0..Math.min(attachments.size - 2, PREVIEW_MAX_NUM)) {
            separators[i].visibility = View.VISIBLE
        }
    }

    private fun onClickPreview(v: View) {
        listener?.let {
            val index = images.indexOf(v)
            it.onClick(index)
        }
    }

    fun setOnPreviewClickListener(listener: OnClickPreviewListener?) {
        this.listener = listener
    }

    interface OnClickPreviewListener {
        fun onClick(attachmentIndex: Int)
    }

    private fun getImageView(index: Int, numOfAttachment: Int): ImageView {
        return if (numOfAttachment == 3 && index == 2) {
            images[3]
        } else {
            images[index]
        }
    }
}
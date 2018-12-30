package com.crakac.ofutodon.ui.widget

import android.text.TextPaint
import android.text.style.URLSpan
import androidx.core.graphics.ColorUtils
import com.crakac.ofutoon.ui.widget.OnHighlightListener

open class LinkClickableSpan(val text: String, val url: String) : URLSpan(url), OnHighlightListener {
    private val TAG: String = "LinkClickableSpan"

    private val backgroundAlpha = (0.5 * 255).toInt()
    private var isHighlight = false

    override fun onHighlight(isHighlight: Boolean) {
        this.isHighlight = isHighlight
    }

    override fun updateDrawState(ds: TextPaint) {
        if (isHighlight) {
            ds.bgColor = ColorUtils.setAlphaComponent(ds.linkColor, backgroundAlpha)
        }
        ds.color = ds.linkColor
        ds.isUnderlineText = false
    }
}
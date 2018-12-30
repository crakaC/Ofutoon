package com.crakac.ofutodon.ui.widget

import android.view.View

class TagClickableSpan(text: String, url: String, val hashtag: String) : LinkClickableSpan(text, url) {
    val TAG: String = "MensionClickableSpan"

    override fun onClick(widget: View) {
//        val intent = Intent(widget.context, HashtagTimelineActivity::class.java)
//        HashtagTimelineActivity.setHashtag(intent, hashtag)
//        widget.context.startActivity(intent)
    }
}
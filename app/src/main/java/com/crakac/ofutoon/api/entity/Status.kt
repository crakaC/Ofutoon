package com.crakac.ofutoon.api.entity

import android.text.Spanned
import com.crakac.ofutoon.util.TextUtil
import com.google.gson.annotations.SerializedName

class Status {
    enum class Visibility(val value: String) {
        Public("public"),
        UnListed("unlisted"),
        Private("private"),
        Direct("direct")
    }

    @SerializedName("id")
    val id = 0L
    @SerializedName("uri")
    val uri = ""
    @SerializedName("url")
    val url: String? = null
    @SerializedName("account")
    val account = Account()
    @SerializedName("in_reply_to_id")
    val inReplyToId = 0L
    @SerializedName("in_reply_to_account_id")
    val inReplyToAccountId = 0L
    @SerializedName("reblog")
    var reblog: Status? = null
    @SerializedName("content")
    val content: String = ""
    @SerializedName("created_at")
    val createdAt: String = ""
    @SerializedName("emojis")
    val emojis: List<Emoji> = emptyList()
    @SerializedName("reblogs_count")
    val reblogsCount = 0L
    @SerializedName("favourites_count")
    val favouritesCount = 0L
    @SerializedName("reblogged")
    val isReblogged = false
    @SerializedName("favourited")
    val isFavourited = false
    @SerializedName("muted")
    val isMuted = false
    @SerializedName("sensitive")
    val isSensitive = false
    @SerializedName("spoiler_text")
    val spoilerText = ""
    @SerializedName("visibility")
    val visibility: String = Visibility.Public.value
    @SerializedName("media_attachments")
    val mediaAttachments: List<Attachment> = emptyList()
    @SerializedName("mentions")
    val mentions: List<Mention> = emptyList()
    @SerializedName("tags")
    val tags: List<Tag> = emptyList()
    @SerializedName("Card")
    val card: Card? = null
    @SerializedName("application")
    val application = Application()
    @SerializedName("language")
    val language: String? = null
    @SerializedName("pinned")
    val isPinned = false

    @Transient
    var spannedContent: Spanned? = null

    @Transient
    var createdAtMillis = 0L

    fun getRelativeTime(): CharSequence {
        if (createdAtMillis == 0L) {
            createdAtMillis = TextUtil.parseCreatedAt(createdAt)
        }
        return TextUtil.getRelativeTimeSpanString(createdAtMillis)
    }

    fun isBoostable(): Boolean {
        return visibility == Visibility.Public.value || visibility == Visibility.UnListed.value
    }

    fun isUnlisted() = visibility == Visibility.UnListed.value
    fun isPrivate() = visibility == Visibility.Private.value
    fun isDirect() = visibility == Visibility.Direct.value

    val originalId: Long
        get() {
            return reblog?.id ?: id
        }

    val isFaved: Boolean
        get() {
            return reblog?.isFavourited ?: isFavourited
        }

    val isBoosted: Boolean
        get() {
            return reblog?.isReblogged ?: isReblogged
        }

    fun hasSpoileredText() = spoilerText.isNotEmpty()
    var hasExpanded = false
}
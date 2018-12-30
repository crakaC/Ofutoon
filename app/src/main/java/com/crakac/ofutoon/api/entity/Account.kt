package com.crakac.ofutoon.api.entity

import android.text.Spanned
import com.google.gson.annotations.SerializedName
import java.net.IDN

class Account {
    @SerializedName("id")
    val id: Long = 0L
    @SerializedName("username")
    val username: String = ""
    @SerializedName("acct")
    val acct: String = ""
    @SerializedName("display_name")
    val displayName: String = ""
    @SerializedName("locked")
    val locked: Boolean = false
    @SerializedName("created_at")
    val createdAt: String = ""
    @SerializedName("followers_count")
    val followersCount: Long = 0L
    @SerializedName("following_count")
    val followingCount: Long = 0L
    @SerializedName("statuses_count")
    val statusesCount: Long = 0L
    @SerializedName("note")
    val note: String = ""
    @SerializedName("url")
    val url: String = ""
    @SerializedName("avatar")
    val avatar: String = ""
    @SerializedName("avatar_static")
    val avatarStatic: String = ""
    @SerializedName("header")
    val header: String = ""
    @SerializedName("header_static")
    val headerStatic: String = ""
    @SerializedName("emojis")
    val emojis: List<Emoji> = emptyList()
    @SerializedName("moved")
    val moved: Account? = null
    @SerializedName("fields")
    val fields: List<Field>? = null
    @SerializedName("bot")
    val isBot: Boolean = false
    @SerializedName("source")
    val source: Source? = null

    @Transient
    var displayNameWithEmoji: Spanned? = null

    @Transient
    var spannedDisplayNameWithAcct: Spanned? = null

    val unicodeAcct: String
        get() {
            val splitted = acct.split("@")
            return if (splitted.size == 1) {
                acct
            } else {
                splitted[0] + "@" + IDN.toUnicode(splitted[1])
            }
        }
}
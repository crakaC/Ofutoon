package com.crakac.ofutoon.api.entity

import com.google.gson.annotations.SerializedName

class Filter {
    enum class Context(val value: String){
        Home("home"),
        Notifications("notifications"),
        Public("public"),
        Thread("thread")
    }
    @SerializedName("id")
    val id: Long = 0L
    @SerializedName("phrase")
    val phrase = ""
    @SerializedName("context")
    val context: List<String> = emptyList()
    @SerializedName("expires_at")
    val expiresAt: String? = null
    @SerializedName("irreversible")
    val isIrreversible = false
    @SerializedName("whole_word")
    val isWholeWord = false

    /*
    Implementation notes
    If whole_word is true , client app should do:

    Define ‘word constituent character’ for your app. In the official implementation, it’s [A-Za-z0-9_] in JavaScript, and [[:word:]] in Ruby. In Ruby case it’s the POSIX character class (Letter | Mark | Decimal_Number | Connector_Punctuation).
    If the phrase starts with a word character, and if the previous character before matched range is a word character, its matched range should be treated to not match.
    If the phrase ends with a word character, and if the next character after matched range is a word character, its matched range should be treated to not match.
    Please check app/javascript/mastodon/selectors/index.js and app/lib/feed_manager.rb in the Mastodon source code for more details.
     */
}
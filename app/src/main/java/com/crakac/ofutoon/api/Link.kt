package com.crakac.ofutoon.api

class Link(
    val linkHeader: String,
    val nextPath: String,
    val prevPath: String,
    val maxId: Long,
    val sinceId: Long,
    val minId: Long
) {
    companion object {
        private val NEXT_REL = ".*max_id=([0-9]+).*rel=\"next\"".toRegex()
        private val PREV_REL = ".*since_id=([0-9]+).*rel=\"prev\"".toRegex()
        private val PREV2_REL = ".*min_id=([0-9]+).*rel=\"prev\"".toRegex()

        fun parse(linkHeader: String?): Link? {
            return linkHeader?.let {
                val links = it.split(",")
                var nextPath = ""
                var maxId = 0L
                var prevPath = ""
                var sinceId = 0L
                var minId = 0L

                links.forEach {
                    val link = it.trim()
                    NEXT_REL.matchEntire(link)?.let {
                        nextPath = it.value.replace("; rel=\"next\'", "")
                        maxId = it.groupValues[1].toLong()
                    }

                    val prevMatch = PREV_REL.matchEntire(link)
                    if (prevMatch != null) {
                        prevPath = prevMatch.value.replace("; rel=\"prev\"", "")
                        sinceId = prevMatch.groupValues[1].toLong()
                    } else {
                        PREV2_REL.matchEntire(link)?.let {
                            prevPath = it.value.replace("; rel=\"prev\"", "")
                            minId = it.groupValues[1].toLong()
                        }
                    }

                }
                Link(it, nextPath, prevPath, maxId, sinceId, minId)
            }
        }
    }

    fun nextPaging(): Paging = Paging(maxId = maxId)
    fun prevPaging(): Paging {
        return if (minId > sinceId) {
            Paging(minId = minId)
        } else {
            Paging(sinceId = sinceId)
        }
    }
}
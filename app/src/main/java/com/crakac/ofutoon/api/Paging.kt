package com.crakac.ofutoon.api

import java.util.*

class Paging(
    var maxId: Long? = null,
    var sinceId: Long? = null,
    var minId: Long? = null
) {

    var q = emptyMap<String, String>()
        get() {
            return toQueryMap()
        }

    private fun toQueryMap(): Map<String, String> {
        val map = HashMap<String, String>()

        maxId?.let {
            map.put("max_id", it.toString())
        }

        sinceId?.let {
            map.put("since_id", it.toString())
        }

        minId?.let {
            map.put("min_id", it.toString())
        }

        return map
    }
}
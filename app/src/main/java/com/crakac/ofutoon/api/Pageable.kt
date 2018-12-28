package com.crakac.ofutoon.api

class Pageable<T>(val part: List<T>, val link: Link?) {
    val TAG: String = "Pageable"
    fun nextPaging(): Paging = Paging(maxId = link?.maxId)
    fun prevPaging(): Paging = Paging(sinceId = link?.sinceId)
    fun toPaging(): Paging = Paging(link?.maxId, link?.sinceId)
}
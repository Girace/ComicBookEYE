package com.example.comicbookeye.helpers

import com.example.comicbookeye.infrastructure.domain.ComicBook

fun MutableMap<String, Any>.toComicBook() : ComicBook {
    return ComicBook(
        id = (this["id"] as Long).toInt(),
        title = this["title"] as String,
        image = this["image"] as String,
        series = this["series"] as String,
        description = this["description"] as String,
        status = (this["status"] as Long).toInt(),
        volumeNumber = (this["volumeNumber"] as Long).toInt()
    )
}

package com.example.comicbookeye.helpers

enum class ComicBookStatusEnum {
    PRESENT, ORDERED, ABSENT, NOT_DEFINED
}

fun Int.comicBookStatus(): ComicBookStatusEnum {
    return when (this) {
        1 -> ComicBookStatusEnum.PRESENT
        2 -> ComicBookStatusEnum.ORDERED
        3 -> ComicBookStatusEnum.ABSENT
        else -> ComicBookStatusEnum.NOT_DEFINED
    }
}

fun ComicBookStatusEnum.customToString(): String {
    return when (this) {
        ComicBookStatusEnum.PRESENT -> "Presente"
        ComicBookStatusEnum.ORDERED -> "In prenotazione"
        ComicBookStatusEnum.ABSENT -> "Mancante"
        else -> "Non conosciuto"
    }
}

fun ComicBookStatusEnum.index(): Int {
    return when (this) {
        ComicBookStatusEnum.PRESENT -> 1
        ComicBookStatusEnum.ORDERED -> 2
        ComicBookStatusEnum.ABSENT -> 3
        ComicBookStatusEnum.NOT_DEFINED -> -1
    }
}
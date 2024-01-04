package com.example.comicbookeye.ui.adapters

import com.example.comicbookeye.infrastructure.domain.ComicBook

/**
 * Oggetto utilizzato per popolare la recyclerview. Il campo "title" e "comicBook" sono mutuamente
 * esclusivi.
 */
class ComicBookAdapterModel(var type: Int, var title: String?, var comicBook: ComicBook?) {
    companion object {
        const val TITLE = 0
        const val CONTENT = 1
    }
}
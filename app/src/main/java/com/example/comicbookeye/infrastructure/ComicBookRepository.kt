package com.example.comicbookeye.infrastructure

import androidx.lifecycle.LiveData
import com.example.comicbookeye.infrastructure.domain.ComicBook
import com.example.comicbookeye.infrastructure.local.ComicBookDao

class ComicBookRepository(private val comicBookDao: ComicBookDao) {

    val allComicBooks: LiveData<List<ComicBook>> = comicBookDao.getAllComicBooks()

    suspend fun insert(comicBooks: List<ComicBook>) {
        comicBookDao.insertOrUpdateItems(comicBooks)
    }

    suspend fun updateStatus(newStatus: Int, comicId: Int) {
        comicBookDao.updateStatus(newStatus, comicId)
    }



}
package com.example.comicbookeye.infrastructure

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.comicbookeye.helpers.readJSONFromAsset
import com.example.comicbookeye.infrastructure.domain.ComicBook
import com.example.comicbookeye.infrastructure.local.ComicBookDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ComicBookRepository(private val comicBookDao: ComicBookDao) {

    val allComicBooks: LiveData<List<ComicBook>> = comicBookDao.getAllComicBooks()

    suspend fun insert(comicBooks: List<ComicBook>) {
        comicBookDao.insertOrUpdateItems(comicBooks)
    }

    suspend fun updateStatus(newStatus: Int, comicId: Int) {
        comicBookDao.updateStatus(newStatus, comicId)
    }

    suspend fun loadDataset(application: Application) {
        val json = readJSONFromAsset(application, "remote_data.json")
        val gson = Gson()
        val listComicBookType = object : TypeToken<ArrayList<ComicBook>>() {}.type
        val comicBooks: ArrayList<ComicBook> = gson.fromJson(json, listComicBookType)
        insert(comicBooks)
    }


}
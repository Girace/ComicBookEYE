package com.example.comicbookeye.infrastructure

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.comicbookeye.helpers.readJSONFromAsset
import com.example.comicbookeye.helpers.toComicBook
import com.example.comicbookeye.infrastructure.domain.ComicBook
import com.example.comicbookeye.infrastructure.local.ComicBookDao
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ComicBookRepository(private val comicBookDao: ComicBookDao) {

    val allComicBooks: LiveData<List<ComicBook>> = comicBookDao.getAllComicBooks()
    private val remoteDb = Firebase.firestore

    suspend fun addDataToRemoteDB(comicBooks: List<ComicBook>) {
        comicBooks.forEach {book ->
            remoteDb.collection("books").document(book.id.toString()).set(book).await()
        }
    }

    fun restoreDataFromRemoteDB() {
        remoteDb.collection("books").get().addOnSuccessListener { result ->
            CoroutineScope(Dispatchers.IO).launch {
                comicBookDao.insertOrUpdateItems(
                    result.map { it.data.toComicBook() }.toList(),
                    forceUpdate = true
                )
            }
        }
    }

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
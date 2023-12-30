package com.example.comicbookeye.infrastructure.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.comicbookeye.infrastructure.ComicBookRepository
import com.example.comicbookeye.infrastructure.domain.ComicBook
import com.example.comicbookeye.infrastructure.local.ComicBookDatabase
import kotlinx.coroutines.launch

class ComicBookViewModel(application: Application) : AndroidViewModel(application) {

    val allComicBooks: LiveData<List<ComicBook>>
    private val repository: ComicBookRepository

    init {
        /**
        Recupero il dao dal database e procedo con l'inizializzazione del repository e del livedata
        usato dalla ui per la visualizzazione della lista.
         */
        val dao = ComicBookDatabase.getDatabase(application.applicationContext).getComicBookDao()
        repository = ComicBookRepository(dao)
        allComicBooks = repository.allComicBooks
        // Recupero i dati dal json
        viewModelScope.launch { repository.loadDataset(application) }
    }

    fun updateStatus(newStatus: Int, comicId: Int) {
        viewModelScope.launch { repository.updateStatus(newStatus, comicId) }
    }


}
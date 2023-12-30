package com.example.comicbookeye.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import com.example.comicbookeye.R
import com.example.comicbookeye.infrastructure.view_model.ComicBookViewModel

class ComicBookEYEActivity : AppCompatActivity() {

    lateinit var viewModel: ComicBookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_book_eye)
        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ComicBookViewModel::class.java]

        viewModel.allComicBooks.observe(this) {
            list -> //TODO fai cose
        }

        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
            }

        })

        val checkBoxMissingBook = findViewById<CheckBox>(R.id.checkbox_missing_book)
        checkBoxMissingBook.setOnCheckedChangeListener { _, isChecked ->
            //TODO fai cose
        }

    }
}
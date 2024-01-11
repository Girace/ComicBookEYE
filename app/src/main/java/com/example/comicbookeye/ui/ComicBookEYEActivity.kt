package com.example.comicbookeye.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comicbookeye.R
import com.example.comicbookeye.helpers.ComicBookStatusEnum
import com.example.comicbookeye.helpers.comicBookStatus
import com.example.comicbookeye.infrastructure.domain.ComicBook
import com.example.comicbookeye.infrastructure.view_model.ComicBookViewModel
import com.example.comicbookeye.ui.adapters.ComicBookAdapterModel
import com.example.comicbookeye.ui.adapters.ComicBooksAdapter

class ComicBookEYEActivity : AppCompatActivity() {

    lateinit var viewModel: ComicBookViewModel
    var absentListFilterMode: Boolean? = null
    var searchTextFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_book_eye)
        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ComicBookViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_id)
        val comicBookAdapter = ComicBooksAdapter(this, viewModel)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = comicBookAdapter
        recyclerView.layoutManager = layoutManager

        viewModel.allComicBooks.observe(this) { list ->
            val adapterModels = filterComicList(list)
            comicBookAdapter.updateList(adapterModels)
        }

        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchTextFilter = newText
                viewModel.allComicBooks.value.let {
                    if(it != null){
                        comicBookAdapter.updateList(filterComicList(it))
                    }
                }
                return true
            }

        })

        val checkBoxMissingBook = findViewById<CheckBox>(R.id.checkbox_missing_book)
        checkBoxMissingBook.setOnCheckedChangeListener { _, isChecked ->
            absentListFilterMode = isChecked
            viewModel.allComicBooks.value.let {
                if(it != null) {
                    comicBookAdapter.updateList(filterComicList(it))
                }
            }
        }

    }

    private fun filterComicList(list: List<ComicBook>): ArrayList<ComicBookAdapterModel> {

        val filteredList = arrayListOf<ComicBook>()

        for (item in list) {
            if (absentListFilterMode == true) {
                if (item.status.comicBookStatus() != ComicBookStatusEnum.ABSENT) {
                    continue
                }
            }
            if (searchTextFilter != null && searchTextFilter!!.isNotEmpty()) {
                if (!item.title.lowercase().contains(searchTextFilter!!.lowercase())) {
                    continue
                }
            }

            filteredList.add(item)
        }

        val adapterModels = ArrayList<ComicBookAdapterModel>()
        val groupDataMap: Map<String, List<ComicBook>> =
            filteredList.groupBy { item -> item.series }
        groupDataMap.forEach { (key, values) ->
            adapterModels.add(ComicBookAdapterModel(ComicBookAdapterModel.TITLE, key, null))
            adapterModels.addAll(values.sortedBy { it.volumeNumber }.map { value ->
                ComicBookAdapterModel(ComicBookAdapterModel.CONTENT, null, value)
            })
        }
        return adapterModels
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_backup -> {
                val data = viewModel.allComicBooks.value
                if (!data.isNullOrEmpty()){
                    viewModel.addDataToRemoteDB(data)
                }
                true
            }
            R.id.action_restore -> {
                viewModel.restoreDataFromRemoteDB()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
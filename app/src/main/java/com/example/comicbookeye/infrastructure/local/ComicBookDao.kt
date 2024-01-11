package com.example.comicbookeye.infrastructure.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.comicbookeye.infrastructure.domain.ComicBook

@Dao
interface ComicBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(comicBooks: List<ComicBook>)

    @Query("SELECT * from comics")
    fun getAllComicBooks(): LiveData<List<ComicBook>>

    @Query("UPDATE comics SET status = :newStatus WHERE id = :comicId")
    suspend fun updateStatus(newStatus: Int, comicId: Int)

    @Query("UPDATE comics SET title = :title, series = :series, image = :image, description = :description, volumeNumber = :volumeNumber WHERE id = :comicId")
    fun updateComicExcludingStatus(
        comicId: Int,
        title: String,
        series: String,
        image: String,
        description: String,
        volumeNumber: Int
    )

    /**
     * Utilizziamo la transaction per garantire che l'intera operazione
     * avvenga all'interno di una singola transazione, garantendo la consistenza
     * dei dati.
     */
    @Transaction
    suspend fun insertOrUpdateItems(items: List<ComicBook>, forceUpdate: Boolean = false) {
        val itemsToInsert = mutableListOf<ComicBook>()
        val itemsToUpdate = mutableListOf<ComicBook>()

        if(forceUpdate) {
            insertAll(items)
        } else {
            for (item in items) {
                if (getItemById(item.id) == null) {
                    itemsToInsert.add(item)
                } else {
                    itemsToUpdate.add(item)
                }
            }

            if (itemsToInsert.isNotEmpty()) {
                insertAll(itemsToInsert)
            }

            for (item in itemsToUpdate) {
                updateComicExcludingStatus(
                    item.id,
                    item.title,
                    item.series,
                    item.image,
                    item.description,
                    item.volumeNumber
                )
            }
        }
    }
    @Query("SELECT * FROM comics WHERE id = :comicId LIMIT 1")
    fun getItemById(comicId: Int): ComicBook?

}
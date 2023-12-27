package com.example.comicbookeye.infrastructure.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.comicbookeye.infrastructure.domain.ComicBook

@Database(entities = [ComicBook::class], version = 1)
abstract class ComicBookDatabase : RoomDatabase() {

    abstract fun getComicBookDao(): ComicBookDao

    companion object {
        @Volatile
        private var INSTANCE: ComicBookDatabase? = null

        /**
         * Implementazione del singleton per prevenire l'apertura
         * di molteplici istanze allo stesso tempo del database.
         */
        @Synchronized
        fun getDatabase(context: Context): ComicBookDatabase {

            //Se l'istanza è null, la ritorno, altrimenti la creo.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ComicBookDatabase::class.java,
                    "comic_book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
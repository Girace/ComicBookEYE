package com.example.comicbookeye.infrastructure.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics")
data class ComicBook(
    @PrimaryKey val id: Int,
    val title: String,
    val series: String,
    val image: String,
    val description: String,
    val status: Int,
    val volumeNumber: Int
)
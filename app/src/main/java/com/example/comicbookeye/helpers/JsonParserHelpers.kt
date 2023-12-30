package com.example.comicbookeye.helpers
import android.app.Application
import java.io.InputStream

// Funzione per la lettura del json locale
fun readJSONFromAsset(application: Application, jsonName: String): String? {
    val json: String?
    try {
        val inputStream: InputStream = application.assets.open(jsonName)
        json = inputStream.bufferedReader().use { it.readText() }
    } catch (ex: Exception) {
        ex.printStackTrace()
        return null
    }
    return json
}
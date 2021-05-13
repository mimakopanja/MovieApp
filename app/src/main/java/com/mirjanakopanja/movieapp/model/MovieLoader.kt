package com.mirjanakopanja.movieapp.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object MovieLoader {
    fun loadMovie(id: Int): MoviesDataTransfer? {
        try {
            val uri = URL("https://api.themoviedb.org/3/movie/${id}?api_key=c494e29ff29ab2436abfd4cca89c8243")

            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.addRequestProperty(
                    "api-key", "c494e29ff29ab2436abfd4cca89c8243"
                )
                urlConnection.readTimeout = 10000
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                return Gson().fromJson(getLines(bufferedReader), MoviesDataTransfer::class.java)
            } catch (e: Exception) {
                e.printStackTrace()

            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}
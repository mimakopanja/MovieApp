package com.mirjanakopanja.movieapp.broadcast

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.mirjanakopanja.movieapp.*
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.extensions.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class DescriptionService: JobIntentService( ) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleWork(intent: Intent) {
        val id = intent.getLongExtra(ID_EXTRA, 0)
        if (id.equals(0) && id == null) {
            onEmptyData()
        } else {
            loadMovieFromService(id)
        }
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun loadMovieFromService(id: Long) {
        try {
            val uri = URL("https://api.themoviedb.org/3/movie/${id}?api_key=${BuildConfig.api_key}")
            lateinit var conn: HttpsURLConnection
            try {
                conn = uri.openConnection() as HttpsURLConnection
                conn.apply {
                    requestMethod = "GET"
                    readTimeout = 1000
                    addRequestProperty("api_key", BuildConfig.api_key)
                }
                val movieDTO: MoviesDataTransfer =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(conn.inputStream))),
                        MoviesDataTransfer::class.java
                    )
                onResponse(movieDTO)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                conn.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    private fun onErrorRequest(err: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, err)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onResponse(movieDTO: MoviesDataTransfer) {
        movieDTO?.let {
            onSuccessResponse(
                movieDTO.title,
                movieDTO.overview,
                movieDTO.vote_average?.toDouble(),
                movieDTO.release_date,
                movieDTO.tagline
            )
        }
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onSuccessResponse(
        title: String?,
        overview: String?,
        voteAverage: Double?,
        releaseDate: String?,
        tagline: String?
    ) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(TITLE_EXTRA, title)
        broadcastIntent.putExtra(OVERVIEW_EXTRA, overview)
        broadcastIntent.putExtra(RELEASE_DATE_EXTRA, releaseDate)
        broadcastIntent.putExtra(RATING_EXTRA, voteAverage)
        broadcastIntent.putExtra(TAGLINE_EXTRA, tagline)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    companion object {
        fun start(context: Context, intent: Intent) {
            enqueueWork(context, DescriptionService::class.java, 1111, intent)
        }
    }

}
package com.vlu.newsreader.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Minimal HTTP helper built on HttpURLConnection.
 * All public suspend functions switch to Dispatchers.IO, so callers on the
 * Main thread (e.g. lifecycleScope.launch) never block the UI.
 */
object HttpClient {

    const val POSTS_URL = "https://jsonplaceholder.typicode.com/posts"
    private const val TIMEOUT_MS = 10_000

    /** Safe version: runs the request on the I/O thread pool. */
    suspend fun get(url: String): String = withContext(Dispatchers.IO) {
        executeGet(url)
    }

    /**
     * Blocking version WITHOUT a thread switch.
     * Used ONLY by the debugging challenge to reproduce NetworkOnMainThreadException.
     */
    fun getBlocking(url: String): String = executeGet(url)

    private fun executeGet(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = TIMEOUT_MS
            connection.readTimeout = TIMEOUT_MS
            connection.setRequestProperty("Accept", "application/json")

            val code = connection.responseCode
            if (code !in 200..299) {
                throw IOException("HTTP error $code: ${connection.responseMessage}")
            }
            return connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }
}

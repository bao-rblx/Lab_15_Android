package com.vlu.newsreader

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParseException
import com.vlu.newsreader.databinding.ActivityMainBinding
import com.vlu.newsreader.debug.DebugChallenge
import com.vlu.newsreader.model.NewsItem
import com.vlu.newsreader.network.HttpClient
import com.vlu.newsreader.parser.NewsJsonParser
import com.vlu.newsreader.ui.NewsAdapter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = NewsAdapter()
    private var useGson = true
    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = adapter

        binding.btnRetry.setOnClickListener { loadNews() }
        binding.btnToggleParser.setOnClickListener {
            useGson = !useGson
            updateParserLabel()
            loadNews()
        }
        updateParserLabel()

        if (DebugChallenge.TRIGGER_NETWORK_ON_MAIN_THREAD) {
            DebugChallenge.runNetworkOnMainThread()
        }

        loadNews()
    }

    private fun updateParserLabel() {
        binding.btnToggleParser.text =
            getString(if (useGson) R.string.parser_gson else R.string.parser_manual)
    }

    private fun loadNews() {
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {           // starts on Main thread
            showLoading()
            try {
                val json = HttpClient.get(HttpClient.POSTS_URL)   // switches to Dispatchers.IO
                val items = withContext(Dispatchers.Default) {     // parsing off the UI thread
                    if (useGson) NewsJsonParser.parseWithGson(json)
                    else NewsJsonParser.parseManual(json)
                }
                Log.d(TAG, "Loaded ${items.size} items using ${if (useGson) "Gson" else "Manual"} parser")
                showData(items)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load news", e)
                showError(mapError(e))
            }
        }
    }

    private fun mapError(e: Exception): String = when (e) {
        is SecurityException -> getString(R.string.err_permission)
        is UnknownHostException -> getString(R.string.err_no_internet)
        is SocketTimeoutException -> getString(R.string.err_timeout)
        is SocketException ->
            if (e.message?.contains("EPERM") == true) getString(R.string.err_permission)
            else getString(R.string.err_no_internet)
        is JSONException, is JsonParseException -> getString(R.string.err_parse)
        is IOException -> getString(R.string.err_server, e.message ?: "")
        else -> getString(R.string.err_unknown, e.javaClass.simpleName)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.errorGroup.visibility = View.GONE
        binding.tvStatus.text = getString(R.string.loading)
    }

    private fun showData(items: List<NewsItem>) {
        binding.progressBar.visibility = View.GONE
        binding.errorGroup.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        adapter.submitList(items)
        binding.tvStatus.text = getString(
            R.string.loaded_count, items.size,
            if (useGson) "Gson" else "JSONArray"
        )
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.errorGroup.visibility = View.VISIBLE
        binding.tvError.text = message
        binding.tvStatus.text = getString(R.string.load_failed)
    }

    companion object {
        private const val TAG = "NewsReader"
    }
}

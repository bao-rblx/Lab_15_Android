package com.vlu.newsreader.parser

import com.google.gson.Gson
import com.vlu.newsreader.model.NewsItem
import org.json.JSONArray

object NewsJsonParser {

    private val gson = Gson()

    /** Strategy 1: manual parsing with org.json JSONArray / JSONObject. */
    fun parseManual(jsonString: String): List<NewsItem> {
        val array = JSONArray(jsonString)
        val result = ArrayList<NewsItem>(array.length())
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(
                NewsItem(
                    id = obj.getInt("id"),
                    userId = obj.getInt("userId"),
                    title = obj.getString("title"),
                    body = obj.getString("body")
                )
            )
        }
        return result
    }

    /** Strategy 2: automated parsing with Gson + @SerializedName. */
    fun parseWithGson(jsonString: String): List<NewsItem> =
        gson.fromJson(jsonString, Array<NewsItem>::class.java).toList()
}

package com.vlu.newsreader.parser

import org.json.JSONException
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsJsonParserTest {

    private val json: String =
        javaClass.classLoader!!.getResource("posts.json")!!.readText()

    @Test
    fun manualParser_returns100Posts() {
        val items = NewsJsonParser.parseManual(json)
        assertEquals(100, items.size)
        assertEquals(1, items[0].id)
        assertEquals(1, items[0].userId)
        assertEquals(100, items.last().id)
    }

    @Test
    fun gsonParser_returns100Posts() {
        val items = NewsJsonParser.parseWithGson(json)
        assertEquals(100, items.size)
        assertEquals(1, items[0].id)
    }

    @Test
    fun bothParsers_produceIdenticalResults() {
        assertEquals(NewsJsonParser.parseManual(json), NewsJsonParser.parseWithGson(json))
    }

    @Test(expected = JSONException::class)
    fun manualParser_throwsOnMalformedJson() {
        NewsJsonParser.parseManual("{ not an array ")
    }
}

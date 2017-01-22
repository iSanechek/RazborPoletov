package com.isanechek.razborpoletov.utils

import android.util.Log
import com.isanechek.razborpoletov.R
import com.isanechek.razborpoletov.data.EpisodeModel
import com.isanechek.razborpoletov.data.rss.RssItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object ContentUtil {
    private val MAX_SHORT_SUMMARY_ITEM_COUNT: Int = 4
    private val excluded: MutableList<String> = Arrays.asList("Виктор", "Антон", "Барух", "1","Разделяй","книжка","ты кто такой","ложить","этого","Полезняшки:")!!

    private fun extractImageLink(document: Document): String {
        val elements: Elements = document.select("a[href]")
        elements.map { it.attributes().get("href") }
                .filter { it.contains("http") && (it.contains(".png") || it.contains(".jpg")) }
                .forEach { return it }

        val elements2: Elements = document.select("img")
        elements2.map { it.attributes().get("src") }
                .filter { it.contains("http") && (it.contains(".png") || it.contains(".jpg")) && !it.contains("mp3") }
                .forEach { return it }

        return "drawable://" + R.mipmap.ic_launcher
    }

    private fun extractContent(document: Document): String {
        val elements: Elements = document.select("ul")
        val element: Element = elements.first()
        val elements1: Elements = element.select("li")
        var count: Int = 0
        val stringBuilder = StringBuilder()
        elements1.map { it.children().first() }.forEach {
                    if (it != null) {
                        val lastChild = getLastChild(it.childNode(0))
                        val str: String = lastChild.toString()
                        count = addToStringBuilder(count, stringBuilder, str)
                    } else {
                        val str: String = element.ownText()
                        count = addToStringBuilder(count, stringBuilder, str)
                    }
                }
        return stringBuilder.toString()
    }

    private fun extractDownloadLink(document: Document): String {
        val links: Elements = document.select("a[href]")
        return links.map { it.attributes().get("href") }.first { it.contains("mp3") }
    }

    private fun addToStringBuilder(count: Int, stringBuilder: StringBuilder, string: String): Int {
        if (!excluded.contains(string) && !string.isEmpty() && string.length < 30 && string.length > 3 && count < MAX_SHORT_SUMMARY_ITEM_COUNT) {
            stringBuilder.append(string)
            stringBuilder.append("\n")
            count.unaryPlus()
        }
        return count
    }

    private fun getLastChild(lis: Node): Node {
        if (lis.childNodeSize() == 0) {
            return lis
        } else {
            return getLastChild(lis.childNodes()[0])
        }
    }

    private fun mappingObject(rssItem: RssItem): EpisodeModel {
        val doc: Document = Jsoup.parse(rssItem.description)
        val date: String = SimpleDateFormat("d MM yyyy").format(rssItem.date)
        val model = EpisodeModel()
        model.link = rssItem.link
        model.title = rssItem.title.replace("(\\d+)", "")
        model.description = extractContent(doc)
        model.epNumber = rssItem.title.replace("[\\^-?0-9]+", "")
        model.epDate = date
        model.imgUrl = extractImageLink(doc)
        model.mp3Url = extractDownloadLink(doc)
        return model
    }

    fun getMappingList(rssItems: ArrayList<RssItem>): ArrayList<EpisodeModel> {
        val cache = ArrayList<EpisodeModel>()
        rssItems.filter { it.title.contains("Episode") }.mapTo(cache) { mappingObject(it) }
        return cache
    }
}
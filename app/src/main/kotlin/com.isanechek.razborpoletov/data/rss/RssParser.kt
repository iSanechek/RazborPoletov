package com.isanechek.razborpoletov.data.rss

import org.w3c.dom.Element
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by isanechek on 21.01.17.
 */
object RssParser {

    private val REFRESH_TIMEOUT_MILLIS = 50000

    fun getRssItems(feedUrl: String): ArrayList<RssItem> {
        val cache = ArrayList<RssItem>()
        try {
            //open an URL connection make GET to the server and
            //take xml RSS data
            val url = URL(feedUrl)
            val conn = url.openConnection() as HttpURLConnection

            conn.connectTimeout = REFRESH_TIMEOUT_MILLIS
            conn.readTimeout = REFRESH_TIMEOUT_MILLIS

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val `is` = conn.inputStream

                //DocumentBuilderFactory, DocumentBuilder are used for
                //xml parsing
                val dbf = DocumentBuilderFactory
                        .newInstance()
                val db = dbf.newDocumentBuilder()

                //using db (Document Builder) parse xml data and assign
                //it to Element
                val document = db.parse(`is`)
                val element = document.documentElement

                //take rss nodes to NodeList
                val nodeList = element.getElementsByTagName("item")

                if (nodeList.length > 0) {
                    for (i in 0..nodeList.length - 1) {

                        //take each entry (corresponds to <item></item> tags in
                        //xml data

                        val entry = nodeList.item(i) as Element

                        val _titleE = entry.getElementsByTagName(
                                "title").item(0) as Element
                        val _descriptionE = entry
                                .getElementsByTagName("description").item(0) as Element
                        val _pubDateE = entry
                                .getElementsByTagName("pubDate").item(0) as Element
                        val _linkE = entry.getElementsByTagName(
                                "link").item(0) as Element

                        val _title = _titleE.firstChild.nodeValue
                        val _description = _descriptionE.firstChild.nodeValue
                        val _pubDate = Date(_pubDateE.firstChild.nodeValue)
                        val _link = _linkE.firstChild.nodeValue

                        //create RssItemObject and add it to the ArrayList
                        val rssItemParser = RssItem(_title, _description,
                                _pubDate, _link)
                        cache.add(rssItemParser)
                    }
                }

            } else {
                // time refresh out
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cache
    }
}
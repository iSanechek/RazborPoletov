package com.isanechek.razborpoletov.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.isanechek.razborpoletov.R
import com.isanechek.razborpoletov.data.EpisodeModel
import com.isanechek.razborpoletov.data.rss.RssItem
import com.isanechek.razborpoletov.data.rss.RssParser
import com.isanechek.razborpoletov.utils.ContentUtil
import io.realm.Realm
import io.realm.RealmResults
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * Created by isanechek on 21.01.17.
 */
class MainActivity : AppCompatActivity() {

    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
        msg("Hello From Android App For Razbor Poletov")

        realm = Realm.getDefaultInstance()
        getData()
    }

    override fun onResume() {
        super.onResume()
        msg("onResume")
    }

    override fun onPause() {
        super.onPause()
        msg("onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        msg("Good Bay")
        realm.close()
    }

    private fun getData() {
        msg("getData")
        doAsync {
            msg("other thread")
            val items: ArrayList<RssItem> = RssParser.getRssItems("http://feeds.feedburner.com/razbor-podcast")
            msg("items -> " + items.size)
            val result: ArrayList<EpisodeModel> = ContentUtil.getMappingList(items)
            msg("result " + result.size)
            uiThread {
                msg("main thread")
                realm.executeTransaction {
                    realm.insertOrUpdate(result)
                }
                val fromBd: RealmResults<EpisodeModel> = realm.where(EpisodeModel::class.java).findAll()
                msg("from db " + fromBd.size)
                for (i in fromBd) {
                    msg("Episode ${i.title}")
                }
            }
        }
    }

    private fun msg(msg: String) {
        Log.e("MainActivity", msg)
    }
}
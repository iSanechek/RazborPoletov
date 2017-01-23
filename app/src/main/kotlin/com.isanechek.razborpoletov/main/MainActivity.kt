package com.isanechek.razborpoletov.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log.e
import com.isanechek.razborpoletov.data.EpisodeModel
import com.isanechek.razborpoletov.data.rss.RssItem
import com.isanechek.razborpoletov.data.rss.RssParser
import com.isanechek.razborpoletov.utils.ContentUtil
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

/**
 * Created by isanechek on 21.01.17.
 */
class MainActivity : AppCompatActivity() {

    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msg("Hello From Android App For Razbor Poletov")
        getData()
        realm = Realm.getDefaultInstance()
        val fromDb: RealmResults<EpisodeModel> = realm.where(EpisodeModel::class.java).findAll()
        val result: List<EpisodeModel> = fromDb.sort("title", Sort.ASCENDING)
        MainActivityView(ListAdapter(result)).setContentView(this)
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
        realm.close()
        msg("Good Bay")
    }

    private fun getData() {
        doAsync {
            val items: ArrayList<RssItem> = RssParser.getRssItems("http://feeds.feedburner.com/razbor-podcast")
            val result: ArrayList<EpisodeModel> = ContentUtil.getMappingList(items)
            realm = Realm.getDefaultInstance()
            realm.executeTransaction { realm.insertOrUpdate(result) }
            realm.close()
        }
    }

    private fun msg(msg: String) {
        e("MainActivity", msg)
    }
}
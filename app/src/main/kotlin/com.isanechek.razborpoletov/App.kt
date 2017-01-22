package com.isanechek.razborpoletov

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by isanechek on 21.01.17.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}
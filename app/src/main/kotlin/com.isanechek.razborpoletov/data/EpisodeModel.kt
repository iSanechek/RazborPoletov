package com.isanechek.razborpoletov.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by isanechek on 21.01.17.
 */
open class EpisodeModel(
        @PrimaryKey var link: String = "",
        var title: String = "",
        var description: String = "",
        var epNumber: String = "",
        var epDate: String = "",
        var imgUrl: String = "",
        var mp3Url: String= "",
        var mp3LocalUrl: String = "",
        var detailsPageSource: String = "",
        var epDownload: Boolean = false,
        var listingEpisode: Boolean = false,
        var epNew: Boolean = false,
        var totalTime: Int = 0,
        var elapsedTime: Int = 0
): RealmObject()
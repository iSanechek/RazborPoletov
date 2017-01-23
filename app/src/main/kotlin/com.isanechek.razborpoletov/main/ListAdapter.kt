package com.isanechek.razborpoletov.main

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.isanechek.razborpoletov.data.EpisodeModel
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalPadding
import org.jetbrains.anko.wrapContent

/**
 * Created by isanechek on 23.01.17.
 */
class Holder(val textView: TextView): RecyclerView.ViewHolder(textView)

class ListAdapter(val list: List<EpisodeModel> = ArrayList<EpisodeModel>()): RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        return Holder(TextView(parent!!.context).apply {
            textSize = 16f
            verticalPadding = context.dip(4)
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        })
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val model: EpisodeModel = list[position]
        holder!!.textView.text = model.title
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
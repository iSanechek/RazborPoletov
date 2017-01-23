package com.isanechek.razborpoletov.main

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Created by isanechek on 23.01.17.
 */
class MainActivityView(val listAdapter: ListAdapter): AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        return relativeLayout {
            val emptyView = textView("Loading...") {
                textSize = 16f
                typeface = Typeface.MONOSPACE
            }

            fun updateEmptyViewVisibility(recyclerView: RecyclerView) {
                if (doesListHaveItem(recyclerView)) {
                    emptyView.visibility = View.GONE
                } else {
                    emptyView.visibility = View.VISIBLE
                }
            }

            recyclerView {
                val orientation = LinearLayoutManager.VERTICAL
                layoutManager = LinearLayoutManager(context, orientation, true)
                overScrollMode = View.OVER_SCROLL_NEVER
                adapter = listAdapter
                updateEmptyViewVisibility(this)
            }.lparams(width = matchParent, height = wrapContent)
        }
    }

    private fun doesListHaveItem(list: RecyclerView?) = getListItemCount(list) > 0
    private fun getListItemCount(list: RecyclerView?) = list?.adapter?.itemCount ?: 0
}


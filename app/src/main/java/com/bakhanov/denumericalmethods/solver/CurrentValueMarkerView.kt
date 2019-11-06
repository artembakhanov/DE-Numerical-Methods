package com.bakhanov.denumericalmethods.solver

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView

import com.bakhanov.denumericalmethods.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CurrentValueMarkerView(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    private val tvContent: TextView

    private var mOffset: MPPointF? = null

    init {

        val view = LayoutInflater.from(getContext()).inflate(layoutResource, this)
        tvContent = view.findViewById(R.id.tvContent)
    }

    override fun refreshContent(e: Entry, highlight: Highlight?) {

        tvContent.text = e.y.toString()

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }

        return mOffset as MPPointF
    }
}

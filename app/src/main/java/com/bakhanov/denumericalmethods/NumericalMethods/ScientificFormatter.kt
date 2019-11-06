package com.bakhanov.denumericalmethods.NumericalMethods

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter

class ScientificFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return "%g".format(value)
    }
}
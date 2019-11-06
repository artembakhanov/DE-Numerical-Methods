package com.bakhanov.denumericalmethods.numericalMethods

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class ScientificFormatter : ValueFormatter() {
    private val format = "0.0000"

    override fun getFormattedValue(value: Float): String {
        return if (value > 1e6)
            "%.4e".format(value)
        else
            DecimalFormat(format).format(value.toDouble())
    }
}
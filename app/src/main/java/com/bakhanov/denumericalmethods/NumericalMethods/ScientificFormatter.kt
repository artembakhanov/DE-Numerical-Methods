package com.bakhanov.denumericalmethods.NumericalMethods

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class ScientificFormatter : ValueFormatter() {
    private val format = ".##########"

    override fun getFormattedValue(value: Float): String {
        return if (value > 1e6)
            "%.4e".format(value)
        else
            DecimalFormat(format).format(value.toDouble())
    }
}
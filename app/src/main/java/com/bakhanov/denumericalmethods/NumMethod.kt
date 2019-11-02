package com.bakhanov.denumericalmethods

import com.github.mikephil.charting.data.Entry

interface NumMethod {
    var equation: Equation
    fun compute(x0: Double, y0: Double, x: Double, n: Int): Solution
}
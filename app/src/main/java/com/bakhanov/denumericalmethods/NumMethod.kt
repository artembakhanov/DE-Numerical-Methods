package com.bakhanov.denumericalmethods

interface NumMethod {
    val equation: Equation
    fun compute(x0: Double, y0: Double, x: Double, n: Int): Solution
}
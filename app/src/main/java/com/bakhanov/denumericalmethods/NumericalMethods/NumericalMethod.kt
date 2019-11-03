package com.bakhanov.denumericalmethods.NumericalMethods

interface NumericalMethod {
    val equation: Equation
    fun compute(x0: Double, y0: Double, x: Double, n: Int): Solution
}
package com.bakhanov.denumericalmethods.numericalMethods.methods

import com.bakhanov.denumericalmethods.numericalMethods.Equation
import com.bakhanov.denumericalmethods.numericalMethods.Solution

interface NumericalMethod {
    val equation: Equation
    fun compute(
        x0: Double,
        y0: Double,
        x: Double,
        n: Int,
        exactSolution: ArrayList<Double>? = null
    ): Solution
}
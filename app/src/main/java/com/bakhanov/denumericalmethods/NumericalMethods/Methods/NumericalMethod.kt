package com.bakhanov.denumericalmethods.NumericalMethods.Methods

import com.bakhanov.denumericalmethods.NumericalMethods.Equation
import com.bakhanov.denumericalmethods.NumericalMethods.Solution

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
package com.bakhanov.denumericalmethods.NumericalMethods.Methods

import com.bakhanov.denumericalmethods.NumericalMethods.Equation

open class EulerMethod(override val equation: Equation) : IterativeMethod(equation) {
    override fun next(xi: Double, yi: Double): Double {
        return yi + step * equation.function(xi, yi)
    }
}

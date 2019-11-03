package com.bakhanov.denumericalmethods.NumericalMethods

open class EulerMethod(override val equation: Equation) : IterativeMethod(equation) {
    override fun next(xi: Double, yi: Double): Double {
        return yi + step * equation.function(xi, yi)
    }
}

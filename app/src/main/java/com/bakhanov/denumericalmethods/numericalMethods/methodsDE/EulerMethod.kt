package com.bakhanov.denumericalmethods.numericalMethods.methodsDE

import com.bakhanov.denumericalmethods.numericalMethods.Equation

class EulerMethod(override val equation: Equation) : IterativeMethod(equation) {
    override fun next(xi: Double, yi: Double): Double {
        return yi + step * equation.function(xi, yi)
    }
}

package com.bakhanov.denumericalmethods

class ImprovedEulerMethod(override val equation: Equation) : IterativeMethod(equation){
    override fun next(xi: Double, yi: Double): Double {
        val f = equation.function
        val k1i = f(xi, yi)
        val k2i = f(xi + step, yi + step * k1i)
        return yi + step / 2 * (k1i + k2i)
    }
}
package com.bakhanov.denumericalmethods.NumericalMethods

class RungeKuttaMethod(override val equation: Equation) : IterativeMethod(equation) {
    override fun next(xi: Double, yi: Double): Double {
        val f = equation.function
        val k1i = f(xi, yi)
        val k2i = f(xi + step / 2, yi + step / 2 * k1i)
        val k3i = f(xi + step / 2, yi + step / 2 * k2i)
        val k4i = f(xi + step, yi + step * k3i)

        return yi + step / 6 * (k1i + 2 * k2i + 2 * k3i + k4i)
    }
}

package com.bakhanov.denumericalmethods.NumericalMethods

class Equation(val function: (x: Double, y: Double) -> Double,
               val const: (x: Double, y: Double) -> Double,
               val solution: (x: Double, c: Double) -> Double,
               val includedPointsX: (x: Double) -> Boolean,
               val includedPointsY: (y: Double) -> Boolean,
               val includedPointsDescription: String = "x and y should be in the domain") {

    fun compose(x0: Double, y0: Double, x: Double, n: Int) {
        if (x0 >= x) throw NMException(
            "The initial x value should be less than the final one."
        )
        if (n <= 0) throw NMException(
            "The number of steps should be greater than 0"
        )
        val step = (x - x0) / n

        val xs = (0..n).toList().map { x0 + step * it }

        if (!(xs.all(includedPointsX) && includedPointsY(y0))) {
            throw NMException(includedPointsDescription)
        }
    }
}
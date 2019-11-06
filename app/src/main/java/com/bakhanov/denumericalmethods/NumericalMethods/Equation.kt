package com.bakhanov.denumericalmethods.NumericalMethods

import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMException

class Equation(val function: (x: Double, y: Double) -> Double,
               val const: (x: Double, y: Double) -> Double,
               val solution: (x: Double, c: Double) -> Double,
               val includedPointsX: (x: Double, c: Double) -> Boolean,
               val includedPointsY: (y: Double) -> Boolean,
               val includedPointsDescription: String = "x and y should be in the domain") {
}
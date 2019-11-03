package com.bakhanov.denumericalmethods.NumericalMethods

class Equation(val function: (x: Double, y: Double) -> Double,
               val const: (x: Double, y: Double) -> Double,
               val solution: (x: Double, c: Double) -> Double,
               val includedPoints: (x: Double) -> Boolean)
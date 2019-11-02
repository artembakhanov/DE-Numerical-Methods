package com.bakhanov.denumericalmethods

class Equation(val function: (x: Double, y: Double) -> Double,
               val const: (x: Double, y: Double) -> Double,
               val x0: Double, val y0: Double,
               val solution: (x: Double, c: Double) -> Double,
               val excludedPoints: ArrayList<Double>)
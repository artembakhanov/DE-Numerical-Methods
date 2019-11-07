package com.bakhanov.denumericalmethods.numericalMethods

/**
 * This class represents the equation y' = f(x, y) without initial values.
 *
 * @property function y' = f(x, y)
 * @property solution the solution of the differential equation. c is not calculated (param)
 * @property const function that calculates constant for the solution, depending on initial values
 * @property includedPointsX predicate showing whether the points is in the x-domain
 * @property includedPointsY predicate showing whether the points is in the y-domain
 * @property includedPointsDescription text information about the domain of the DE
 */
class Equation(val function: (x: Double, y: Double) -> Double,
               val solution: (x: Double, c: Double) -> Double,
               val const: (x: Double, y: Double) -> Double,
               val includedPointsX: (x: Double, c: Double) -> Boolean,
               val includedPointsY: (y: Double) -> Boolean,
               val includedPointsDescription: String = "x and y should be in the domain") {
}
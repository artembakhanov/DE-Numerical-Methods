package com.bakhanov.denumericalmethods

import com.bakhanov.denumericalmethods.NumericalMethods.*

/**
 * This class unifies all the iterative methods.
 */
class Solver(val equation: Equation) {
    private val methodsNames = arrayListOf("Euler Method", "Improved Euler Method", "Runge-Kutta method")
    private var unstableMethods = arrayListOf<Int>()
    private val numMethods = arrayListOf<NumericalMethod>(
        EulerMethod(equation),
        ImprovedEulerMethod(equation),
        RungeKuttaMethod(equation)
    )
    private val colors = methodsNames.zip(arrayListOf(0x1e88e5, 0x43a047, 0xf4511e)).toMap()


    private lateinit var exactSolution: ArrayList<Double>
    private lateinit var solutions: HashMap<Int, Solution>
    private var step: Double = 42.0

    /**
     * Solves the given equation with the given parameters and transform it to plot data.
     *
     * @param method the integer value of method
     * @return plot data of the solution
     */
    fun generateSolutionPlotData(method: Int, x0: Double, y0: Double, x: Double, n: Int): PlotData {
        if (x0 >= x) throw NMException(
            "The initial x value should be less than the final one."
        )
        if (n <= 0) throw NMException(
            "The number of steps should be greater than 0"
        )
        step = (x - x0) / n
        unstableMethods = ArrayList()
        solutions = HashMap()

        computeExactSolution(x0, y0, n)

        when {
            method == 0 -> {
                for (i in 1..3) {
                    addSolution(method, x0, y0, x, n)
                }
            }
            method <= 3 -> {
                addSolution(method, x0, y0, x, n)
            }
            else -> throw NMException("The method does not exist")
        }

        return PlotData()
    }

    private fun computeExactSolution(x0: Double, y0: Double, n: Int) {
        val const = equation.const(x0, y0)
        exactSolution = ArrayList()
        for (i in 1..n) {
            exactSolution.add(equation.solution(x0 + i * step, const))
        }
    }

    private fun addSolution(
        method: Int,
        x0: Double,
        y0: Double,
        x: Double,
        n: Int
    ) {
        try {
            solutions[method] = numMethods[method].compute(x0, y0, x, n)
        } catch (e: NMStabilityException) {
            unstableMethods.add(method)
        }
    }
}
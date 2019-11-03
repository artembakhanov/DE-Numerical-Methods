package com.bakhanov.denumericalmethods.NumericalMethods

import java.util.ArrayList

abstract class IterativeMethod(override val equation: Equation) :
    NumericalMethod {
    private var y: ArrayList<Double> = ArrayList(42)
    protected var step: Double = 42.0
    private var solution: Solution =
        Solution(0.0, 0.0, 0)

    protected abstract fun next(xi: Double, yi: Double): Double

    /**
     * Computes the solution numerically.
     *
     * @param x0 initial x value
     * @param y0 initial y value
     * @param x x value where the method finishes
     * @param n the number of steps
     *
     */
    override fun compute(x0: Double, y0: Double, x: Double, n: Int): Solution {
        if (x0 >= x) throw NumericalMethodException(
            "The initial x value should be less than the final one."
        )
        if (n <= 0) throw NumericalMethodException(
            "The number of steps should be greater than 0"
        )

        setVals(x0, x, n)
        computeExactSolution(x0, y0, n)
        computeNumericalSolution(x0, y0, n)

        return solution
    }

    /**
     * Compute all points of exact solution.
     */
    private fun computeExactSolution(x0: Double, y0: Double, n: Int) {
        val const = equation.const(x0, y0)
        solution.exactSolution.add(y0)
        for (i in 1..n) {
            solution.exactSolution.add(equation.solution(x0 + i * step, const))
        }
    }

    protected open fun computeNumericalSolution(x0: Double, y0: Double, n: Int) {
        val y = solution.numericalSolution
        val x = solution.x
        y.add(y0)
        x.add(x0)

        for (i in 1..n) {
            y.add(next(x[i - 1], y[i - 1]))
            computeErrors(i)
        }
    }

    /**
     * Computes local and global errors
     *
     * @param i iteration number
     */
    private fun computeErrors(i: Int) {
        computeGlobalError(i)
        computeLocalError(i)
    }

    private fun computeGlobalError(i: Int) {
        solution.globalErrors.add(solution.exactSolution[i] - solution.numericalSolution[i])
    }

    protected open fun computeLocalError(i: Int) {
        val y = solution.exactSolution
        val x = solution.x
        val localSol = next(x[i - 1], y[i - 1])
        solution.localErrors.add(y[i] - localSol)
    }

    /**
     * Sets default values.
     */
    private fun setVals(x0: Double, x: Double, n: Int) {
        y = ArrayList(n + 1)
        solution = Solution(x0, x, n)
        step = (x - x0) / n
    }
}
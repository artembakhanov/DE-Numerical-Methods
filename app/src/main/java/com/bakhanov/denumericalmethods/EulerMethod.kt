package com.bakhanov.denumericalmethods

import java.util.ArrayList

class EulerMethod(override var equation: Equation) : NumMethod {

    private var currentX: Double = 42.0
    private var y: ArrayList<Double> = ArrayList(42)
    private var step: Double = 42.0
    private var solution: Solution = Solution(42)

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
        if (x0 >= x) throw NumericalMethodException("The initial x value should be less than the final one.")
        if (n <= 0) throw NumericalMethodException("The number of steps should be greater than 0")

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
        solution.exactSolution.add(Point(x0, y0))
        for (i in 1..n) {
            solution.exactSolution.add(Point(x0 + i * step, equation.solution(x0 + i * step, const)))
        }
    }

    private fun computeNumericalSolution(x0: Double, y0: Double, n: Int) {
        val sol = solution.numericalSolution
        sol.add(Point(x0, y0))

        for (i in 1..n) {
            sol.add(Point(currentX + step,
                sol[i - 1].y + step * equation.function(currentX, sol[i - 1].y)))
            currentX += step
        }
    }

    private fun setVals(x0: Double, x: Double, n: Int) {
        currentX = x0
        y = ArrayList(n + 1)
        solution = Solution(n)
        step = (x - x0) / n
    }
}

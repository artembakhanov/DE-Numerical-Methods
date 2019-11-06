package com.bakhanov.denumericalmethods.NumericalMethods.Methods

import com.bakhanov.denumericalmethods.NumericalMethods.Equation
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMArgumentException
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMDomainException
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMStabilityException
import com.bakhanov.denumericalmethods.NumericalMethods.Solution
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

abstract class IterativeMethod(override val equation: Equation) :
    NumericalMethod {
    private var y: ArrayList<Double> = ArrayList(42)
    protected var step: Double = 42.0
    private lateinit var solution: Solution

    private var x0 = Double.NaN
    private var y0 = Double.NaN
    private var x = Double.NaN
    private var n = Int.MAX_VALUE
    private var totalError = 0.0

    /**
     * Computes the next value depending on previous point.
     *
     * This function must be implemented separately for each method.
     *
     * @param xi previous x value
     * @param yi previous y value
     */
    protected abstract fun next(xi: Double, yi: Double): Double

    /**
     * Computes the solution numerically.
     *
     * @param x0 initial x value
     * @param y0 initial y value
     * @param x x value where the method finishes
     * @param n the number of steps
     * @param exactSolution predefined exact solution. Its size should be equal to n + 1
     *
     * @throws NMArgumentException if x0 >= x0 or n <= 0 or the size of exactSolution != n + 1
     */
    override fun compute(
        x0: Double,
        y0: Double,
        x: Double,
        n: Int,
        exactSolution: ArrayList<Double>?
    ): Solution {
        if (x0 >= x) throw NMArgumentException(
            "The initial x value should be less than the final one."
        )
        if (n <= 0) throw NMArgumentException(
            "The number of steps should be greater than 0"
        )
        setDefaultValues(x0, y0, x, n)

        if (exactSolution == null)
            computeExactSolution(x0, y0, n)
        else
            solution.exactSolution = exactSolution

        if (solution.exactSolution.size != n + 1)
            throw NMArgumentException("Exact solution size does not correspond to the number of points")

        computeNumericalSolution(x0, y0, n)

        solution.totalError = totalError

        return solution
    }

    /**
     * Compute all points of exact solution.
     *
     * @throws NMDomainException when some points from are not in the domain of the function.
     * Please, check the text description of the equation.
     */
    private fun computeExactSolution(x0: Double, y0: Double, n: Int) {
        val const = equation.const(x0, y0)
        if (const.isNaN()) throw NMDomainException(equation.includedPointsDescription)

        solution.exactSolution.add(y0)
        for (i in 1..n) {
            val next = equation.solution(x0 + i * step, const)
            if (next.isNaN()) throw NMDomainException(equation.includedPointsDescription)
            solution.exactSolution.add(next)
        }
    }

    /**
     * Computes numerical solution for the given initial parameters.
     *
     * @param x0 initial x value
     * @param y0 initial y value
     * @param n the number of steps
     *
     * @throws NMStabilityException when the method is unstable for the step
     */
    protected open fun computeNumericalSolution(x0: Double, y0: Double, n: Int) {
        val y = solution.numericalSolution
        val x = solution.x
        y.add(y0)
        x.add(x0)

        for (i in 1..n) {
            val next = next(x[i - 1], y[i - 1])
            if (next.isNaN())
                throw NMStabilityException(
                    "This method seems to be unstable for the given parameters"
                )

            y.add(next)
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

    /**
     * Computes global errors.
     */
    private fun computeGlobalError(i: Int) {
        solution.globalErrors.add(solution.exactSolution[i] - solution.numericalSolution[i])
        if (solution.globalErrors[i].isNaN() || solution.globalErrors[i].isInfinite())
            throw NMStabilityException("This method seems to be unstable")
    }

    /**
     * Computes local errors (global error without accumulated global errors on previous steps).
     */
    protected open fun computeLocalError(i: Int) {
        val y = solution.exactSolution
        val x = solution.x
        val localSol = next(x[i - 1], y[i - 1])
        val localErr = y[i] - localSol
        if (localErr.absoluteValue > totalError) totalError = localErr.absoluteValue
        solution.localErrors.add(localErr)
    }

    /**
     * Sets default values.
     */
    private fun setDefaultValues(
        x0: Double,
        y0: Double,
        x: Double,
        n: Int
    ) {
        this.x0 = x0
        this.y0 = y0
        this.x = x
        this.n = n

        totalError = 0.0
        y = ArrayList(this.n + 1)
        solution =
            Solution(this.x0, this.x, this.n)
        step = (this.x - this.x0) / this.n
    }
}
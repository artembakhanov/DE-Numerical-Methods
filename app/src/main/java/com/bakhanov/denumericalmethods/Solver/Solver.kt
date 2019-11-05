package com.bakhanov.denumericalmethods.Solver

import com.bakhanov.denumericalmethods.NumericalMethods.*
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMArgumentException
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMStabilityException
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

/**
 * This class unifies all the iterative methods.
 */
class Solver(private val equation: Equation) {
    private var unstableMethods = arrayListOf<Method>()
    private val numMethods = arrayListOf<NumericalMethod>(
        EulerMethod(equation),
        ImprovedEulerMethod(equation),
        RungeKuttaMethod(equation)
    )


    private lateinit var exactSolution: ArrayList<Double>
    private lateinit var solutions: HashMap<Int, Solution>
    private lateinit var solutionPlotData: ArrayList<ILineDataSet>
    private lateinit var errorsPlotData: ArrayList<ILineDataSet>
    private lateinit var totalErrorsPlotData: ArrayList<ILineDataSet>
    private var step: Double = 42.0

    /**
     * Solves the given equation with the given parameters and transform it to plot data.
     *
     * @param method the integer value of method
     * @return plot data of the solution
     */
    fun generateSolutionPlotData(method: Method, x0: Double, y0: Double, x: Double, n: Int): PlotData {
        if (x0 >= x) throw NMArgumentException(
            "The initial x value should be less than the final one."
        )
        if (n <= 0) throw NMArgumentException(
            "The number of steps should be greater than 0"
        )
        step = (x - x0) / n
        unstableMethods = ArrayList()
        solutions = HashMap()
        solutionPlotData = ArrayList()
        errorsPlotData = ArrayList()
        totalErrorsPlotData = ArrayList()

        computeExactSolution(x0, y0, n)

        when (method) {
            Method.ALL -> {
                for (i in 0..2) {
                    addSolution(Method.from(i), x0, y0, x, n)
                    addTotalErrors(Method.from(i), x0, y0, x, n)
                }
            }
            else -> {
                addSolution(method, x0, y0, x, n)
                addTotalErrors(method, x0, y0, x, n)
            }
        }

        return PlotData(
            solutionPlotData,
            errorsPlotData,
            totalErrorsPlotData,
            unstableMethods
        )
    }

    /**
     * Computes exact solution and adds it to the solutionPlotData.
     *
     * This function is used for optimization, since all the methods calculates exact solutions
     * themselves.
     */
    private fun computeExactSolution(x0: Double, y0: Double, n: Int) {
        val const = equation.const(x0, y0)
        val exactSolutionDataSet = ArrayList<Entry>()
        exactSolution = ArrayList()
        exactSolution.add(y0)
        for (i in 1..n) {
            val x = x0 + i * step
            val y = equation.solution(x, const)
            exactSolution.add(y)
            exactSolutionDataSet.add(Entry(x.toFloat(), y.toFloat()))
        }

        val solutionDataSet = LineDataSet(exactSolutionDataSet,"Exact")
        solutionDataSet.setDrawCircles(false)
        solutionDataSet.setColor(Method.ALL.color(), 255)

        solutionPlotData.add(solutionDataSet)
    }

    private fun addSolution(
        method: Method,
        x0: Double,
        y0: Double,
        x: Double,
        n: Int
    ) {
        try {
            val solution = numMethods[method.methodNumber].compute(x0, y0, x, n, exactSolution)
            val solutionDataSet = LineDataSet(solution.getEntries(EntryType.NUMERICAL), method.mname())
            val errorDataSet = LineDataSet(solution.getEntries(EntryType.L_ERROR), method.mname())
            solutionDataSet.setDrawCircles(false)
            errorDataSet.setDrawCircles(false)
            solutionDataSet.setColor(method.color(), 255)
            errorDataSet.setColor(method.color(), 255)

            solutionPlotData.add(solutionDataSet)
            errorsPlotData.add(errorDataSet)

        } catch (e: NMStabilityException) {
            unstableMethods.add(method)
        }
    }

    private fun addTotalErrors(
        method: Method,
        x0: Double,
        y0: Double,
        x: Double,
        n: Int
    ) {
        val totalErrors = ArrayList<Entry>()
        for (i in 1..n) {
            try {
                totalErrors.add(Entry(
                    i.toFloat(),
                    numMethods[method.methodNumber].compute(x0, y0, x, i).totalError.toFloat()
                ))
            } catch (e: NMStabilityException) {}
        }

        val lineDataSet = LineDataSet(totalErrors, method.mname())
        lineDataSet.setDrawCircles(false)
        lineDataSet.setColor(method.color(), 255)

        totalErrorsPlotData.add(lineDataSet)
    }

}
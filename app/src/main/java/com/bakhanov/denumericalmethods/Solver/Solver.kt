package com.bakhanov.denumericalmethods.Solver

import com.bakhanov.denumericalmethods.NumericalMethods.*
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMArgumentException
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMDomainException
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMStabilityException
import com.bakhanov.denumericalmethods.NumericalMethods.Methods.EulerMethod
import com.bakhanov.denumericalmethods.NumericalMethods.Methods.ImprovedEulerMethod
import com.bakhanov.denumericalmethods.NumericalMethods.Methods.NumericalMethod
import com.bakhanov.denumericalmethods.NumericalMethods.Methods.RungeKuttaMethod
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

/**
 * This class unifies all the iterative methods.
 */
class Solver(private val equation: Equation) {
    private var x0: Double = 0.0
    private var y0: Double = 0.0
    private var x: Double = 0.0
    private var n: Int = 0
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
        this.x0 = x0
        this.y0 = y0
        this.n = n
        this.x = x

        computeExactSolution()

        when (method) {
            Method.ALL -> {
                for (i in 0..2) {
                    addSolution(Method.from(i))
                    addTotalErrors(Method.from(i))
                }
            }
            else -> {
                addSolution(method)
                addTotalErrors(method)
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
    private fun computeExactSolution() {
        val const = equation.const(x0, y0)
        if (const.isNaN()) throw NMDomainException(equation.includedPointsDescription)

        val exactSolutionDataSet = ArrayList<Entry>()
        exactSolution = ArrayList()
        exactSolution.add(y0)
        for (i in 1..n) {
            val x = x0 + i * step
            val y = equation.solution(x, const)
            if (y.isNaN()) throw NMDomainException(equation.includedPointsDescription)
            exactSolution.add(y)
            exactSolutionDataSet.add(Entry(x.toFloat(), y.toFloat()))
        }

        val solutionDataSet = composeLineDataSet(exactSolutionDataSet, "Exact", Method.ALL.color())

        solutionPlotData.add(solutionDataSet)
    }

    private fun addSolution(method: Method) {
        try {
            val solution = numMethods[method.methodNumber].compute(x0, y0, x, n, exactSolution)
            val solutionDataSet = composeLineDataSet(
                solution.getEntries(EntryType.NUMERICAL),
                method.mname(),
                method.color())
            val errorDataSet = composeLineDataSet(
                solution.getEntries(EntryType.L_ERROR),
                method.mname(),
                method.color())

            solutionPlotData.add(solutionDataSet)
            errorsPlotData.add(errorDataSet)
        } catch (e: NMStabilityException) {
            unstableMethods.add(method)
        }
    }

    private fun addTotalErrors(method: Method) {
        if (method in unstableMethods) return
        val totalErrors = ArrayList<Entry>()
        for (i in 1..n) {
            try {
                val te = numMethods[method.methodNumber].compute(x0, y0, x, i).totalError.toFloat()
                if (!te.isInfinite())
                    totalErrors.add(Entry(i.toFloat(), te))
            } catch (e: NMStabilityException) {}
        }

        totalErrorsPlotData.add(composeLineDataSet(totalErrors, method.mname(), method.color()))
    }

    private fun composeLineDataSet(
        entries: ArrayList<Entry>,
        name: String,
        color: Int
    ): LineDataSet {
        val lineDataSet = LineDataSet(entries, name)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setColor(color, 255)
        lineDataSet.setDrawValues(false)

        return lineDataSet
    }

}
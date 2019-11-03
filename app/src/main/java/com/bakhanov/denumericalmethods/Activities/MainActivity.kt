package com.bakhanov.denumericalmethods.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bakhanov.denumericalmethods.NumericalMethods.Equation
import com.bakhanov.denumericalmethods.NumericalMethods.*
import com.bakhanov.denumericalmethods.R
import com.bakhanov.denumericalmethods.NumericalMethods.Solution
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_content.*
import java.lang.Math.pow
import kotlin.NumberFormatException
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val eq = Equation(
        { x, _ -> 2 * x },
        { x, y -> y - pow(x, 2.0) },
        { x, c -> pow(x, 2.0) + c },
        { true })

    private val methodsNames = arrayListOf("Euler Method", "Improved Euler Method", "Runge-Kutta method")
    private val numMethods = arrayListOf<NumericalMethod>(
        EulerMethod(eq),
        ImprovedEulerMethod(eq),
        RungeKuttaMethod(eq)
    )
    private val colors = methodsNames.zip(arrayListOf(0x1e88e5, 0x43a047, 0xf4511e)).toMap()

    private var solution: HashMap<String, Solution>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calculate_button.setOnClickListener {
            try {
                val x0 = x0_edit.text.toString().toDouble()
                val x = x_edit.text.toString().toDouble()
                val y0 = y0_edit.text.toString().toDouble()
                val n = n_edit.text.toString().toInt()
                val sol = spinner.selectedItemPosition
                computeSolutions(x0, x, y0, n, sol)
                drawFun(x0, y0, x, n)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please, enter valid numbers", Toast.LENGTH_LONG).show()
            }
        }
        chart_sol.setOnLongClickListener {
            chart_sol.zoom(0f, 0f, 0f, 0f)
            true
        }

        chart_err.setOnLongClickListener {
            chart_err.zoom(0f, 0f, 0f, 0f)
            true
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(include_bottom_sheet)
        bottom_sheet_header.setOnClickListener {
            bottomSheetBehavior.state = when(bottomSheetBehavior.state) {
                STATE_EXPANDED -> STATE_COLLAPSED
                STATE_COLLAPSED -> STATE_EXPANDED
                else -> STATE_EXPANDED
            }
        }

        chart_err.description.text = "Local errors"
        chart_sol.description.text = "Numerical and exact solutions"

        if (savedInstanceState != null) {
            solution = savedInstanceState.get("solution") as HashMap<String, Solution>?

            if (solution != null) drawFun(0.0, 0.0, 0.0, 0, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("solution", solution)
    }

    fun computeSolutions(
        x0: Double,
        x: Double,
        y0: Double,
        n: Int,
        sol: Int
    ) {
        solution = HashMap()
        if (sol == 0) {
            for (i in 1..3) {
                solution?.put(methodsNames[i - 1], numMethods[i - 1].compute(x0, y0, x, n))
            }
        } else {
            solution?.put(methodsNames[sol - 1], numMethods[sol - 1].compute(x0, y0, x, n))
        }
    }

    private fun drawFun(x0: Double, y0: Double, x: Double, n: Int, recompute: Boolean = true) {
        //val solution = em.compute(0.0, 2.0, 10.0, 10000)

        val lineDataMethods = ArrayList<ILineDataSet>()
        val lineDataErrors = ArrayList<ILineDataSet>()

        for ((key, value) in solution!!) {
            val dataSetMethods = LineDataSet(value.getEntries(EntryType.NUMERICAL), key)
            dataSetMethods.setDrawCircles(false)
            dataSetMethods.setColor(colors[key] ?: error(0x8e24aa), 255)
            lineDataMethods.add(dataSetMethods)

            val dataSetErrors = LineDataSet(value.getEntries(EntryType.L_ERROR), "$key Error")
            dataSetErrors.setDrawCircles(false)
            dataSetErrors.setColor(colors[key] ?: error(0x8e24aa), 255)
            lineDataErrors.add(dataSetErrors)
        }

        chart_sol.data = LineData(lineDataMethods)
        chart_err.data = LineData(lineDataErrors)

        chart_sol.animateX(300)
        chart_err.animateX(300)
    }
}

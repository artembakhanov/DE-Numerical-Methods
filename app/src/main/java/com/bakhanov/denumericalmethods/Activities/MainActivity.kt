package com.bakhanov.denumericalmethods.Activities

import  android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bakhanov.denumericalmethods.NumericalMethods.Equation
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMArgumentException
import com.bakhanov.denumericalmethods.NumericalMethods.Exception.NMDomainException
import com.bakhanov.denumericalmethods.NumericalMethods.ScientificFormatter
import com.bakhanov.denumericalmethods.R
import com.bakhanov.denumericalmethods.Solver.Method
import com.bakhanov.denumericalmethods.Solver.PlotData
import com.bakhanov.denumericalmethods.Solver.Solver
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_content.*
import java.lang.Math.*
import kotlin.NumberFormatException

class MainActivity : AppCompatActivity() {
    private val eq = Equation(
        { x, y -> x * (y - pow(y, 3.0)) },
        { x, y -> (1 / pow(y, 2.0) - 1) * exp(pow(x, 2.0))},
        { x, c -> 1 / sqrt(exp(-pow(x, 2.0)) * c + 1) },
        { true },
        { y -> y > 0 },
        "y > 0 should be held")
    private var plotData: PlotData? = null
    private var solver: Solver = Solver(eq)

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
                drawFun(spinner.selectedItemPosition, x0, y0, x, n)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please, enter valid numbers", Toast.LENGTH_LONG).show()
            } catch (e: NMArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            } catch (e: NMDomainException) {

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

        setupPlots()

        if (savedInstanceState != null) {
            plotData = savedInstanceState.get("plotdata") as PlotData?

            if (plotData != null) drawFun(recompute = false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("plotdata", plotData)
    }

    fun setupPlots() {
        chart_err.description.text = "Local errors"
        chart_sol.description.text = "Numerical and exact solutions"
        chart_total_err.description.text = "Total Errors"

        chart_sol.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart_sol.xAxis.valueFormatter = ScientificFormatter()
        chart_sol.axisRight.isEnabled = false
        chart_sol.axisLeft.valueFormatter = ScientificFormatter()

        chart_err.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart_err.xAxis.valueFormatter = ScientificFormatter()
        chart_err.axisRight.isEnabled = false
        chart_err.axisLeft.valueFormatter = ScientificFormatter()

        chart_total_err.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart_total_err.xAxis.valueFormatter = ScientificFormatter()
        chart_total_err.axisRight.isEnabled = false
        chart_total_err.axisLeft.valueFormatter = ScientificFormatter()
    }

    private fun drawFun(methodIndex: Int = 0, x0: Double = 0.0, y0: Double = 0.0, x: Double = 0.0, n: Int = 1, recompute: Boolean = true) {
        if (recompute)
            plotData = solver.generateSolutionPlotData(Method.from(methodIndex), x0, y0, x, n)

        chart_sol.data = LineData(plotData?.solutionPlotData)
        chart_err.data = LineData(plotData?.errorsPlotData)
        chart_total_err.data = LineData(plotData?.totalErrors)

        if (plotData!!.unstableMethods.size > 0)
            Toast.makeText(this, "${plotData!!.unstableMethods.joinToString { it.mname() }} " +
                    "seem to be unstable. They are not drawn.", Toast.LENGTH_LONG).show()

        chart_sol.animateX(300)
        chart_err.animateX(300)
        chart_total_err.animateX(300)
    }
}

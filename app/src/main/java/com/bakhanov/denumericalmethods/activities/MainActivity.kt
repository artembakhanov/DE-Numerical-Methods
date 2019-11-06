package com.bakhanov.denumericalmethods.activities

import  android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bakhanov.denumericalmethods.numericalMethods.Equation
import com.bakhanov.denumericalmethods.numericalMethods.exception.NMArgumentException
import com.bakhanov.denumericalmethods.numericalMethods.exception.NMDomainException
import com.bakhanov.denumericalmethods.numericalMethods.ScientificFormatter
import com.bakhanov.denumericalmethods.R
import com.bakhanov.denumericalmethods.solver.Method
import com.bakhanov.denumericalmethods.solver.PlotData
import com.bakhanov.denumericalmethods.solver.Solver
import com.bakhanov.denumericalmethods.solver.CurrentValueMarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
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
        { x, y -> y / x - x * exp(y / x) },
        { x, y -> exp(-y / x) - x},
        { x, c -> -x * log(x + c)},
        { x, c -> x != 0.0 && x > -c },
        { _ -> true },
        "Domain of the function: x != 0")
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
                drawFun(spinner.selectedItemPosition, x0, y0, x, n)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please, enter valid numbers", Toast.LENGTH_LONG).show()
            } catch (e: NMArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            } catch (e: NMDomainException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
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

    private fun setupPlots() {
        val charts = arrayListOf(chart_sol, chart_err, chart_total_err)

        chart_err.description.text = "Local errors"
        chart_sol.description.text = "Numerical and exact solutions"
        chart_total_err.description.text = "Total Errors"
        for (chart in charts) {
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            //chart.xAxis.valueFormatter = ScientificFormatter()
            chart.axisRight.isEnabled = false
            chart.axisLeft.valueFormatter = ScientificFormatter()
            chart.marker = CurrentValueMarkerView(this, R.layout.marker_view)
        }

//        chart_sol.setOnLongClickListener {
//            chart_sol.zoom(0f, 0f, 0f, 0f)
//            true
//        }
//
//        chart_err.setOnLongClickListener {
//            chart_err.zoom(0f, 0f, 0f, 0f)
//            true
//        }
//
//        chart_total_err.setOnLongClickListener {
//            chart_total_err.zoom(0f, 0f, 0f, 0f)
//            true
//        }
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

package com.bakhanov.denumericalmethods

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.pow
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    val eq = Equation(
        {x, _ -> 2 * x},
        {x, y -> y - pow(x, 2.0)},
        {x, c -> pow(x, 2.0) + c},
        { true })
    private val em = EulerMethod(eq)
    private var solution: Solution? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculate_button.setOnClickListener {
            try {
                val x0 = x0_edit.text.toString().toDouble()
                val x = x_edit.text.toString().toDouble()
                val y0 = y0_edit.text.toString().toDouble()
                val n = n_edit.text.toString().toInt()
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

        chart_err.description.text = "Global and local errors"
        chart_sol.description.text = "Numerical and exact solutions"

        if (savedInstanceState != null) {
            solution = savedInstanceState.get("solution") as Solution?

            if (solution != null) drawFun(0.0, 0.0, 0.0, 0, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("solution", solution)
    }

    private fun drawFun(x0: Double, y0: Double, x: Double, n: Int, recompute: Boolean = true) {
        //val solution = em.compute(0.0, 2.0, 10.0, 10000)
        if (recompute) solution = em.compute(x0, y0, x, n)

        val entries1 = solution!!.getEntries(EntryType.EXACT)
        val entries2 = solution!!.getEntries(EntryType.NUMERICAL)

        val dataSet1 = LineDataSet(entries1, "Exact")
        dataSet1.setColor(0xc2185b, 255)
        dataSet1.setDrawCircles(false)

        val dataSet2 = LineDataSet(entries2, "Numerical")
        dataSet2.setColor(0x00796b, 255)
        dataSet2.setDrawCircles(false)

        val lineData = LineData(arrayListOf(dataSet1, dataSet2) as List<ILineDataSet>?)

        chart_sol.data = lineData
        chart_sol.animateX(300)

        val entries3 = solution!!.getEntries(EntryType.L_ERROR)
        val entries4 = solution!!.getEntries(EntryType.G_ERROR)

        val dataSet3 = LineDataSet(entries3, "Local errors")
        dataSet3.setColor(0xe64a19, 255)
        dataSet3.setDrawCircles(false)

        val dataSet4 = LineDataSet(entries4, "Global errors")
        dataSet4.setColor(0x303f9f, 255)
        dataSet4.setDrawCircles(false)

        chart_err.data = LineData(arrayListOf(dataSet3, dataSet4) as List<ILineDataSet>)
        chart_err.animateX(300)
    }
}

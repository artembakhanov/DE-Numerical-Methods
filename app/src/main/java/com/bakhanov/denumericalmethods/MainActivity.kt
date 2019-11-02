package com.bakhanov.denumericalmethods

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eq = Equation({x, _ -> exp(x)},
            {x, y -> y - exp(x)},
            0.0, 2.0,
            {x, c -> exp(x) + c},
            arrayListOf())
        val em = EulerMethod(eq)

        val solution = em.compute(0.0, 2.0, 10.0, 10000)

        val entries1 = solution.exactSolution.map { Entry(it.x.toFloat(), it.y.toFloat()) }
        val entries2 = solution.numericalSolution.map { Entry(it.x.toFloat(), it.y.toFloat()) }

        val dataSet1 = LineDataSet(entries1, "Exact")
        dataSet1.setColor(0xc2185b, 255)

        val dataSet2 = LineDataSet(entries2, "Numerical")
        dataSet2.setColor(0x00796b, 255)
        val lineData = LineData(arrayListOf(dataSet1, dataSet2) as List<ILineDataSet>?)

        chart.data = lineData
        chart.animateX(1000)

    }
}

package com.bakhanov.denumericalmethods.Solver

import com.bakhanov.denumericalmethods.Solver.Method
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.io.Serializable

class PlotData(
    val solutionPlotData: ArrayList<ILineDataSet>,
    val errorsPlotData: ArrayList<ILineDataSet>,
    val totalErrors: ArrayList<ILineDataSet>,
    val unstableMethods: ArrayList<Method>
    ) : Serializable
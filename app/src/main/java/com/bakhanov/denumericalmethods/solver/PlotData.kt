package com.bakhanov.denumericalmethods.solver

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.io.Serializable

class PlotData(
    val solutionPlotData: ArrayList<ILineDataSet>,
    val errorsPlotData: ArrayList<ILineDataSet>,
    val totalErrors: ArrayList<ILineDataSet>,
    val unstableMethods: ArrayList<Method>
    ) : Serializable
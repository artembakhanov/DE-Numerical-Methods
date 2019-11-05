package com.bakhanov.denumericalmethods.NumericalMethods

import com.github.mikephil.charting.data.Entry
import java.io.Serializable
import kotlin.math.sign

class Solution(x0: Double, x: Double, private val n: Int) : Serializable {
    var totalError = 0.0
    var exactSolution: ArrayList<Double> = ArrayList(n + 1)
    val numericalSolution: ArrayList<Double> = ArrayList(n + 1)
    val globalErrors: ArrayList<Double> = ArrayList(n + 1)
    val localErrors: ArrayList<Double> = ArrayList(n + 1)
    val x: ArrayList<Double>

    init {
        localErrors.add(0.0)
        globalErrors.add(0.0)

        this.x = ArrayList(n + 1)
        val step = (x - x0) / n
        for (i in 0..n) {
            this.x.add(x0 + i * step)
        }
    }

    /**
     * Generates array list of entries for plotting.
     * Warning! All double values are converted to float ones.
     *
     * @param entryType the type of entries needs to be returned
     */
    fun getEntries(entryType: EntryType): ArrayList<Entry> {
        val entryY = when(entryType) {
            EntryType.EXACT -> exactSolution
            EntryType.NUMERICAL -> numericalSolution
            EntryType.G_ERROR -> globalErrors
            EntryType.L_ERROR -> localErrors
        }
        val es: ArrayList<Entry> = ArrayList(n + 1)
        for (i in 0..n) {
            var y = entryY[i].toFloat()
            y = if (y.isInfinite()) y.sign * (Float.MAX_VALUE / 10) else y
            es.add(Entry(x[i].toFloat(), y))
        }

        return es
    }
}
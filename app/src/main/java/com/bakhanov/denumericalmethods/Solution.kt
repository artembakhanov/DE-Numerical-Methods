package com.bakhanov.denumericalmethods

import com.github.mikephil.charting.data.Entry
import java.io.Serializable

class Solution (x0: Double, x: Double, val n: Int) : Serializable {
    val exactSolution: ArrayList<Double>
    val numericalSolution: ArrayList<Double>
    val globalErrors: ArrayList<Double>
    val localErrors: ArrayList<Double>
    val x: ArrayList<Double>

    init {
        this.exactSolution = ArrayList(n + 1)
        this.numericalSolution = ArrayList(n + 1)
        this.globalErrors = ArrayList(n + 1)
        this.localErrors = ArrayList(n + 1)

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
            es.add(Entry(x[i].toFloat(), entryY[i].toFloat()))
        }

        return es
    }
}
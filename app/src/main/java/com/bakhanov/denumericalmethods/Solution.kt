package com.bakhanov.denumericalmethods

class Solution (n: Int){
    val exactSolution: ArrayList<Point>
    val numericalSolution: ArrayList<Point>
    val globalErrors: ArrayList<Point>
    val localErrors: ArrayList<Point>

    init {
        this.exactSolution = ArrayList(n + 1)
        this.numericalSolution = ArrayList(n + 1)
        this.globalErrors = ArrayList(n + 1)
        this.localErrors = ArrayList(n + 1)
    }
}
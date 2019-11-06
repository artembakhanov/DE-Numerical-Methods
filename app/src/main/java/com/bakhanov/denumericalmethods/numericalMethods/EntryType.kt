package com.bakhanov.denumericalmethods.numericalMethods

/**
 * This enumeration represents different types of entries.
 *
 */
enum class EntryType {
    /**
     * Exact solution
     */
    EXACT,

    /**
     * Numerical solution
     */
    NUMERICAL,

    /**
     * Global errors
     */
    G_ERROR,

    /**
     * Local errors
     */
    L_ERROR
}
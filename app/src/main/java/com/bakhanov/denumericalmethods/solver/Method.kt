package com.bakhanov.denumericalmethods.solver

enum class Method(val methodNumber: Int) {

    EULER_METHOD(0) {
        override fun color(): Int = 0x1e88e5

        override fun mname(): String = "Euler Method"
    },

    IMPROVED_EULER_METHOD(1) {
        override fun color(): Int = 0x43a047

        override fun mname(): String = "Improved Euler Method"
    },

    RUNGE_KUTTA_METHOD(2) {
        override fun color(): Int = 0xf4511e

        override fun mname(): String = "Runge-Kutta method"
    },

    ALL(3) {
        override fun color(): Int = 0x8e24aa

        override fun mname(): String = "All methods"
    };

    /**
     * Returns the text name of a method.
     */
    abstract fun mname(): String

    /**
     * Returns the color (int) of a method.
     * @return int representation of color (without alpha)
     */
    abstract fun color(): Int

    companion object {
        fun from(findValue: Int): Method = Method.values().first { it.methodNumber == findValue }
    }
}
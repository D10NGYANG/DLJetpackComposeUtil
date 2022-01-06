package com.d10ng.basicjetpackcomposeapp.utils

internal object KeyInstant {

    private var mKey = Int.MIN_VALUE

    @Synchronized
    fun new(): Int {
        if (mKey < Int.MAX_VALUE) {
            mKey ++
        } else {
            mKey = Int.MIN_VALUE
        }
        return mKey
    }
}
package com.d10ng.basicjetpackcomposeapp.utils

internal object KeyInstant {

    private var mKey = 1

    @Synchronized
    fun new(): Int {
        if (mKey < Int.MAX_VALUE) {
            mKey ++
        } else {
            mKey = 1
        }
        return mKey
    }
}
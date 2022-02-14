package com.d10ng.basicjetpackcomposeapp

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

abstract class BaseComposeScreenObject(val name: String) {

    open fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity,
    ) {}

    fun go(
        controller: NavHostController,
        params: Map<String, Any> = emptyMap()
    ) {
        val builder = StringBuilder(name)
        if (params.isNotEmpty()) {
            builder.append("?")
            params.entries.forEachIndexed { index, entry ->
                if (index != 0) builder.append("&")
                builder.append("${entry.key}=${entry.value}")
            }
        }
        controller.navigate(builder.toString())
    }
}

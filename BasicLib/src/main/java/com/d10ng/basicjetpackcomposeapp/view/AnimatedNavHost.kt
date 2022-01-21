package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost

@ExperimentalAnimationApi
@Composable
fun AnimatedNavHostDefault(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        contentAlignment = contentAlignment,
        route = route,
        enterTransition = { _,_ ->
            slideInHorizontally( { it } , tween(300) )
        },
        exitTransition = { _,_ ->
            slideOutHorizontally( { -it } , tween(300) )
        },
        popEnterTransition = { _, _ ->
            slideInHorizontally( { -it } , tween(300) )
        },
        popExitTransition = { _, _ ->
            slideOutHorizontally( { it } , tween(300) )
        },
        builder = builder
    )
}
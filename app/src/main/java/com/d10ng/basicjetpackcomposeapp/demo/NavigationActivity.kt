package com.d10ng.basicjetpackcomposeapp.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppTheme
import com.d10ng.basicjetpackcomposeapp.view.AnimatedNavHostDefault
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class NavigationActivity: BaseActivity() {

    override fun isNeedLockScreenOrientation(): Boolean {
        return false
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(app = app) {
                val navController = rememberAnimatedNavController()
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Navigation(
                        Modifier
                            .fillMaxSize()
                            .weight(1f),
                        navController
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun Navigation(
        modifier: Modifier = Modifier,
        controller: NavHostController
    ) {
        AnimatedNavHostDefault(controller, NavigationOneScreenObj.name, modifier) {
            NavigationOneScreenObj.composable(this, controller, this@NavigationActivity)
            NavigationTwoScreenObj.composable(this, controller, this@NavigationActivity)
            NavigationThreeScreenObj.composable(this, controller, this@NavigationActivity)
        }
    }
}
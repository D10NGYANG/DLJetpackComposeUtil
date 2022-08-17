package com.d10ng.basicjetpackcomposeapp.demo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.basicjetpackcomposeapp.view.SolidButtonWithText
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.google.accompanist.navigation.animation.composable

object NavigationTwoScreenObj: BaseComposeScreenObject("NavigationTwoScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(
            route = "$name/{text}",
            arguments = listOf(navArgument("text") { type = NavType.StringType })
        ) {
            NavigationTwoScreen(controller)
        }
    }

    fun go(controller: NavHostController, text: String) {
        controller.navigate("$name/$text")
    }
}

class NavigationTwoScreenModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val text = savedStateHandle.get<String>("text")?: ""
}

@Composable
fun NavigationTwoScreen(
    controller: NavHostController,
    model: NavigationTwoScreenModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        TitleBar(value = "页面2", onClickBack = {
            controller.navigateUp()
        })

        SolidButtonWithText(text = "跳转页面3", onClick = {
            NavigationThreeScreenObj.go(controller, (0 .. 500L).random(), model.text)
        })

        Text(text = "接收文本：${model.text}")
    }
}
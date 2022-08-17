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
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.google.accompanist.navigation.animation.composable

object NavigationThreeScreenObj: BaseComposeScreenObject("NavigationThreeScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(
            route = "$name?id={id}&text={text}",
            listOf(navArgument("id"){type = NavType.LongType}, navArgument("text"){type=NavType.StringType})
        ) {
            NavigationThreeScreen(controller)
        }
    }

    fun go(controller: NavHostController, id: Long, text: String) {
        go(controller, mapOf(
            "id" to id,
            "text" to text
        ))
    }
}


class NavigationThreeScreenModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val id = savedStateHandle.get<Long>("id")
    val text = savedStateHandle.get<String>("text")
}

@Composable
fun NavigationThreeScreen(
    controller: NavHostController,
    model: NavigationThreeScreenModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        TitleBar(value = "页面3", onClickBack = {
            controller.navigateUp()
        })

        Text(text = "接收ID：${model.id}")
        Text(text = "接收文本：${model.text}")
    }
}
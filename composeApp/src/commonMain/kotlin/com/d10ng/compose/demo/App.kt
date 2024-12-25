package com.d10ng.compose.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.d10ng.compose.demo.pages.HomeScreen
import com.d10ng.compose.model.UiViewModelManager
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(HomeScreen()) { navigator ->
            SlideTransition(navigator)
        }
        UiViewModelManager.Init()
    }
}
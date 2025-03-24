package com.d10ng.compose.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.d10ng.compose.demo.pages.HomeScreen
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.defaultColorScheme
import com.d10ng.compose.ui.defaultTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = defaultColorScheme,
        typography = defaultTypography
    ) {
        Navigator(HomeScreen()) { navigator ->
            SlideTransition(navigator)
        }
        UiViewModelManager.Init()
    }
}
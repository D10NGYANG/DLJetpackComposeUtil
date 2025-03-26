package com.d10ng.compose.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.d10ng.compose.demo.pages.HomeScreen
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val fontFamilyResolver = LocalFontFamilyResolver.current
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        loadCustomFontFamily()?.apply {
            fontFamilyResolver.preload(this)
        }
        loading = false
    }

    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        MaterialTheme(
            colorScheme = AppColor.toColorScheme()
        ) {
            Navigator(HomeScreen()) { navigator ->
                SlideTransition(navigator)
            }
            UiViewModelManager.Init()
        }
    }
}

expect suspend fun loadCustomFontFamily(): FontFamily?
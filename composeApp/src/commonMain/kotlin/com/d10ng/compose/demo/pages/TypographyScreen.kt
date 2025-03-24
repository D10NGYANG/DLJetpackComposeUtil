package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.navigation.NavBar

/**
 * 字体排版
 * @Author d10ng
 * @Date 2025/3/24 11:55
 */
class TypographyScreen : Screen {
    @Composable
    override fun Content() {
        TypographyScreenView()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TypographyScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Typography", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color.Gray)
                    .padding(8.dp)
            ) {
                MaterialTheme.typography.apply {
                    Text("displayLarge", style = displayLarge)
                    Text("displayMedium", style = displayMedium)
                    Text("displaySmall", style = displaySmall)
                    Text("headlineLarge", style = headlineLarge)
                    Text("headlineMedium", style = headlineMedium)
                    Text("headlineSmall", style = headlineSmall)
                    Text("titleLarge", style = titleLarge)
                    Text("titleMedium", style = titleMedium)
                    Text("titleSmall", style = titleSmall)
                    Text("bodyLarge", style = bodyLarge)
                    Text("bodyMedium", style = bodyMedium)
                    Text("bodySmall", style = bodySmall)
                    Text("labelLarge", style = labelLarge)
                    Text("labelMedium", style = labelMedium)
                    Text("labelSmall", style = labelSmall)
                }
            }
        }
    }
}
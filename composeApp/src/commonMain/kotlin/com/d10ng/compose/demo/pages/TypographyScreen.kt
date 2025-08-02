package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d10ng.compose.demo.Nav
import com.d10ng.compose.ui.navigation.NavBar

/**
 * 字体排版
 * @Author d10ng
 * @Date 2025/3/24 11:55
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TypographyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(title = "Typography", onClickBack = { Nav.instant().popBackStack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            MaterialTheme.typography.apply {
                TextView("displayLarge", style = displayLarge)
                TextView("displayMedium", style = displayMedium)
                TextView("displaySmall", style = displaySmall)
                TextView("headlineLarge", style = headlineLarge)
                TextView("headlineMedium", style = headlineMedium)
                TextView("headlineSmall", style = headlineSmall)
                TextView("titleLarge", style = titleLarge)
                TextView("titleMedium", style = titleMedium)
                TextView("titleSmall", style = titleSmall)
                TextView("bodyLarge", style = bodyLarge)
                TextView("bodyMedium", style = bodyMedium)
                TextView("bodySmall", style = bodySmall)
                TextView("labelLarge", style = labelLarge)
                TextView("labelMedium", style = labelMedium)
                TextView("labelSmall", style = labelSmall)
            }
        }
    }
}

@Composable
private fun TextView(
    name: String,
    style: TextStyle
) {
    style.apply {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = name,
                style = this@apply
            )
            Text(
                text = "${fontWeight}, ${fontSize}, $lineHeight",
                fontSize = 12.sp
            )
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        }
    }
}
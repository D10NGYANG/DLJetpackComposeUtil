package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.utils.toHex

/**
 * 色彩系统
 * @Author d10ng
 * @Date 2025/3/24 11:50
 */
class ColorScreen : Screen {
    @Composable
    override fun Content() {
        ColorScreenView()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(title = "Color", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(14.dp))
            MaterialTheme.colorScheme.apply {
                ColorRow(primary, "primary")
                ColorRow(onPrimary, "onPrimary")
                ColorRow(primaryContainer, "primaryContainer")
                ColorRow(onPrimaryContainer, "onPrimaryContainer")
                ColorRow(inversePrimary, "inversePrimary")
                ColorRow(secondary, "secondary")
                ColorRow(onSecondary, "onSecondary")
                ColorRow(secondaryContainer, "secondaryContainer")
                ColorRow(onSecondaryContainer, "onSecondaryContainer")
                ColorRow(tertiary, "tertiary")
                ColorRow(onTertiary, "onTertiary")
                ColorRow(tertiaryContainer, "tertiaryContainer")
                ColorRow(onTertiaryContainer, "onTertiaryContainer")
                ColorRow(background, "background")
                ColorRow(onBackground, "onBackground")
                ColorRow(surface, "surface")
                ColorRow(onSurface, "onSurface")
                ColorRow(surfaceVariant, "surfaceVariant")
                ColorRow(onSurfaceVariant, "onSurfaceVariant")
                ColorRow(surfaceTint, "surfaceTint")
                ColorRow(inverseSurface, "inverseSurface")
                ColorRow(inverseOnSurface, "inverseOnSurface")
                ColorRow(error, "error")
                ColorRow(onError, "onError")
                ColorRow(errorContainer, "errorContainer")
                ColorRow(onErrorContainer, "onErrorContainer")
                ColorRow(outline, "outline")
                ColorRow(outlineVariant, "outlineVariant")
                ColorRow(scrim, "scrim")
                ColorRow(surfaceBright, "surfaceBright")
                ColorRow(surfaceDim, "surfaceDim")
                ColorRow(surfaceContainer, "surfaceContainer")
                ColorRow(surfaceContainerHigh, "surfaceContainerHigh")
                ColorRow(surfaceContainerHighest, "surfaceContainerHighest")
                ColorRow(surfaceContainerLow, "surfaceContainerLow")
                ColorRow(surfaceContainerLowest, "surfaceContainerLowest")
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ColorRow(
    color: Color,
    name: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(30.dp)
                .background(color)
                .border(1.dp, Color.Gray)
        )
        Text(
            text = color.toHex(),
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = name,
            modifier = Modifier
                .weight(4f)
                .padding(start = 2.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}
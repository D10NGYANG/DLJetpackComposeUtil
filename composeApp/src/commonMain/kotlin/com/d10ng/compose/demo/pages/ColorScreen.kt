package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.utils.isDark

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
                ColorRow(primary, "primary", onPrimary)
                ColorRow(onPrimary, "onPrimary", primary)
                ColorRow(primaryContainer, "primaryContainer", onPrimaryContainer)
                ColorRow(onPrimaryContainer, "onPrimaryContainer", primaryContainer)
                ColorRow(inversePrimary, "inversePrimary", primary)
                ColorRow(secondary, "secondary", onSecondary)
                ColorRow(onSecondary, "onSecondary", secondary)
                ColorRow(secondaryContainer, "secondaryContainer", onSecondaryContainer)
                ColorRow(onSecondaryContainer, "onSecondaryContainer", secondaryContainer)
                ColorRow(tertiary, "tertiary", onTertiary)
                ColorRow(onTertiary, "onTertiary", tertiary)
                ColorRow(tertiaryContainer, "tertiaryContainer", onTertiaryContainer)
                ColorRow(onTertiaryContainer, "onTertiaryContainer", tertiaryContainer)
                ColorRow(background, "background", onBackground)
                ColorRow(onBackground, "onBackground", background)
                ColorRow(surface, "surface", onSurface)
                ColorRow(onSurface, "onSurface", surface)
                ColorRow(surfaceVariant, "surfaceVariant", onSurfaceVariant)
                ColorRow(onSurfaceVariant, "onSurfaceVariant", surfaceVariant)
                ColorRow(surfaceTint, "surfaceTint")
                ColorRow(inverseSurface, "inverseSurface", inverseOnSurface)
                ColorRow(inverseOnSurface, "inverseOnSurface", inverseSurface)
                ColorRow(error, "error", onError)
                ColorRow(onError, "onError", error)
                ColorRow(errorContainer, "errorContainer", onErrorContainer)
                ColorRow(onErrorContainer, "onErrorContainer", errorContainer)
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
    name: String,
    nameColor: Color? = null
) {
    val nColor = remember(color, nameColor) {
        nameColor?: if (color.isDark()) Color.White else Color.Black
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
            .padding(horizontal = 16.dp)
            .background(color)
            .border(0.5.dp, Color.Gray)
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = name,
            color = nColor
        )
    }
}
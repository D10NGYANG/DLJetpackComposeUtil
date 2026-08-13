package com.d10ng.compose.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

/** Brand extensions that are intentionally outside Material 3's standard token set. */
@Immutable
data class AppBrandColors(
    val link: androidx.compose.ui.graphics.Color,
    val success: androidx.compose.ui.graphics.Color,
    val notice: androidx.compose.ui.graphics.Color,
    val noticeBg: androidx.compose.ui.graphics.Color,
    val assist: androidx.compose.ui.graphics.Color,
    val surface: androidx.compose.ui.graphics.Color,
    val background: androidx.compose.ui.graphics.Color,
    val card: androidx.compose.ui.graphics.Color,
    val line: androidx.compose.ui.graphics.Color,
    val border: androidx.compose.ui.graphics.Color,
    val hint: androidx.compose.ui.graphics.Color,
    val tips: androidx.compose.ui.graphics.Color,
    val body: androidx.compose.ui.graphics.Color,
    val title: androidx.compose.ui.graphics.Color,
    val scrim: androidx.compose.ui.graphics.Color
)

@Immutable
data class AppDesignTokens(
    val colors: AppBrandColors,
    val typography: Typography,
    val shapes: Shapes
)

val LocalAppColors = staticCompositionLocalOf { AppColor.defaultBrandColors() }
val LocalAppTypography = staticCompositionLocalOf { Typography() }
val LocalAppShapes = staticCompositionLocalOf { Shapes() }

@Composable
fun AppTheme(
    colors: ColorScheme? = null,
    brandColors: AppBrandColors? = null,
    typography: Typography? = null,
    shapes: Shapes? = null,
    content: @Composable () -> Unit
) {
    val resolvedColors = colors ?: MaterialTheme.colorScheme
    val resolvedBrandColors = brandColors ?: LocalAppColors.current
    val resolvedTypography = typography ?: MaterialTheme.typography
    val resolvedShapes = shapes ?: MaterialTheme.shapes
    MaterialTheme(
        colorScheme = resolvedColors,
        typography = resolvedTypography,
        shapes = resolvedShapes
    ) {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalAppColors provides resolvedBrandColors,
            LocalAppTypography provides resolvedTypography,
            LocalAppShapes provides resolvedShapes,
            content = content
        )
    }
}

object AppThemeTokens {
    val colors: AppBrandColors
        @Composable @ReadOnlyComposable get() = LocalAppColors.current
    val typography: Typography
        @Composable @ReadOnlyComposable get() = LocalAppTypography.current
    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = LocalAppShapes.current
}

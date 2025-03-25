package com.d10ng.compose.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class BasicTextFontWeight(private val fontWeight: FontWeight) {
    val Primary by lazy { BasicTextFontSize(fontWeight, AppColor.Main.primary) }
    val Secondary by lazy { BasicTextFontSize(fontWeight, AppColor.Main.secondary) }
    val Tertiary by lazy { BasicTextFontSize(fontWeight, AppColor.Main.tertiary) }

    val Bg by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.bg) }
    val Card by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.card) }
    val Line by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.line) }
    val Border by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.border) }
    val Hint by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.hint) }
    val Tips by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.tips) }
    val Body by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.body) }
    val Title by lazy { BasicTextFontSize(fontWeight, AppColor.Neutral.title) }
    
    val Link by lazy { BasicTextFontSize(fontWeight, AppColor.Func.link) }
    val Success by lazy { BasicTextFontSize(fontWeight, AppColor.Func.success) }
    val Notice by lazy { BasicTextFontSize(fontWeight, AppColor.Func.notice) }
    val NoticeBg by lazy { BasicTextFontSize(fontWeight, AppColor.Func.noticeBg) }
    val Error by lazy { BasicTextFontSize(fontWeight, AppColor.Func.error) }
    val Assist by lazy { BasicTextFontSize(fontWeight, AppColor.Func.assist) }

    // 白色
    @Deprecated("Use Surface instead", ReplaceWith("Surface"))
    val White by lazy { BasicTextFontSize(fontWeight, Color.White) }

    // 黑色
    @Deprecated("Use Scrim instead", ReplaceWith("Scrim"))
    val Black by lazy { BasicTextFontSize(fontWeight, Color.Black) }

    val SchemePrimary
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.primary)
    val OnPrimary
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onPrimary)
    val PrimaryContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.primaryContainer)
    val OnPrimaryContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onPrimaryContainer)
    val InversePrimary
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.inversePrimary)
    val SchemeSecondary
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.secondary)
    val OnSecondary
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onSecondary)
    val SecondaryContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.secondaryContainer)
    val OnSecondaryContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onSecondaryContainer)
    val SchemeTertiary
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.tertiary)
    val OnTertiary
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onTertiary)
    val TertiaryContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.tertiaryContainer)
    val OnTertiaryContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onTertiaryContainer)
    val Background
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.background)
    val OnBackground
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onBackground)
    val Surface
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surface)
    val OnSurface
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onSurface)
    val SurfaceVariant
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceVariant)
    val OnSurfaceVariant
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onSurfaceVariant)
    val SurfaceTint
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceTint)
    val InverseSurface
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.inverseSurface)
    val InverseOnSurface
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.inverseOnSurface)
    val SchemeError
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.error)
    val OnError
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onError)
    val ErrorContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.errorContainer)
    val OnErrorContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.onErrorContainer)
    val Outline
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.outline)
    val OutlineVariant
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.outlineVariant)
    val Scrim
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.scrim)
    val SurfaceBright
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceBright)
    val SurfaceDim
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceDim)
    val SurfaceContainer
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceContainer)
    val SurfaceContainerHigh
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceContainerHigh)
    val SurfaceContainerHighest
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceContainerHighest)
    val SurfaceContainerLow
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceContainerLow)
    val SurfaceContainerLowest
        @Composable @ReadOnlyComposable get() = BasicTextFontSize(fontWeight, MaterialTheme.colorScheme.surfaceContainerLowest)
}

class BasicTextFontSize(fontWeight: FontWeight, color: Color) {
    val v60 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 60.sp) }
    val v59 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 59.sp) }
    val v58 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 58.sp) }
    val v57 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 57.sp) }
    val v56 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 56.sp) }
    val v55 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 55.sp) }
    val v54 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 54.sp) }
    val v53 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 53.sp) }
    val v52 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 52.sp) }
    val v51 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 51.sp) }
    val v50 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 50.sp) }
    val v49 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 49.sp) }
    val v48 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 48.sp) }
    val v47 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 47.sp) }
    val v46 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 46.sp) }
    val v45 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 45.sp) }
    val v44 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 44.sp) }
    val v43 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 43.sp) }
    val v42 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 42.sp) }
    val v41 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 41.sp) }
    val v40 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 40.sp) }
    val v39 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 39.sp) }
    val v38 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 38.sp) }
    val v37 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 37.sp) }
    val v36 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 36.sp) }
    val v35 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 35.sp) }
    val v34 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 34.sp) }
    val v33 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 33.sp) }
    val v32 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 32.sp) }
    val v31 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 31.sp) }
    val v30 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 30.sp) }
    val v29 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 29.sp) }
    val v28 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 28.sp) }
    val v27 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 27.sp) }
    val v26 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 26.sp) }
    val v25 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 25.sp) }
    val v24 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 24.sp) }
    val v23 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 23.sp) }
    val v22 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 22.sp) }
    val v21 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 21.sp) }
    val v20 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 20.sp) }
    val v19 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 19.sp) }
    val v18 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 18.sp) }
    val v17 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 17.sp) }
    val v16 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 16.sp) }
    val v15 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 15.sp) }
    val v14 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 14.sp) }
    val v13 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 13.sp) }
    val v12 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 12.sp) }
    val v11 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 11.sp) }
    val v10 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 10.sp) }
    val v9 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 9.sp) }
    val v8 by lazy { TextStyle(fontWeight = fontWeight, color = color, fontSize = 8.sp) }

    // 超大
    val huge by lazy { v24 }
    // 巨大
    val large by lazy { v22 }
    // 大
    val big by lazy { v20 }
    // 中
    val medium by lazy { v18 }
    // 默认
    val default by lazy { v16 }
    // 小
    val small by lazy { v14 }
    // 迷你
    val mini by lazy { v12 }
}

object AppText {
    val Black by lazy { BasicTextFontWeight(FontWeight.Black) }
    val Bold by lazy { BasicTextFontWeight(FontWeight.Bold) }
    val ExtraBold by lazy { BasicTextFontWeight(FontWeight.ExtraBold) }
    val ExtraLight by lazy { BasicTextFontWeight(FontWeight.ExtraLight) }
    val Light by lazy { BasicTextFontWeight(FontWeight.Light) }
    val Medium by lazy { BasicTextFontWeight(FontWeight.Medium) }
    val Normal by lazy { BasicTextFontWeight(FontWeight.Normal) }
    val SemiBold by lazy { BasicTextFontWeight(FontWeight.SemiBold) }
    val Thin by lazy { BasicTextFontWeight(FontWeight.Thin) }
    val W100 by lazy { BasicTextFontWeight(FontWeight.W100) }
    val W200 by lazy { BasicTextFontWeight(FontWeight.W200) }
    val W300 by lazy { BasicTextFontWeight(FontWeight.W300) }
    val W400 by lazy { BasicTextFontWeight(FontWeight.W400) }
    val W500 by lazy { BasicTextFontWeight(FontWeight.W500) }
    val W600 by lazy { BasicTextFontWeight(FontWeight.W600) }
    val W700 by lazy { BasicTextFontWeight(FontWeight.W700) }
    val W800 by lazy { BasicTextFontWeight(FontWeight.W800) }
    val W900 by lazy { BasicTextFontWeight(FontWeight.W900) }
}
package com.d10ng.compose.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

@Deprecated("不推荐使用")
object AppColor {

    /**
     * 主色
     */
    @Deprecated("不推荐使用")
    object Main {
        var primary = Color(0xFF1989FA)
    }

    /**
     * 功能色
     */
    @Deprecated("不推荐使用")
    object Func {
        /**
         * 文字链颜色
         */
        var link = Color(0xFF576B95)

        /**
         * 成功色
         */
        var success = Color(0xFF07C160)

        /**
         * 错误色
         */
        var error = Color(0xFFEE0A24)

        /**
         * 通知消息中的文本颜色
         */
        var notice = Color(0xFFED6A0C)

        /**
         * 通知消息中的背景颜色
         */
        var noticeBg = Color(0xFFFFFBE8)

        /**
         * 文字辅助颜色
         */
        var assist = Color(0xFFFAAB0C)
    }

    /**
     * 中性色
     */
    @Deprecated("不推荐使用")
    object Neutral {
        /**
         * 中性色 1 页面背景色
         */
        var bg = Color(0xFFF7F8FA)

        /**
         * 中性色 2 item card 背景色
         */
        var card = Color(0xFFF2F3F5)

        /**
         * 中性色 3 边框、线色
         */
        var line = Color(0xFFEBEDF0)

        /**
         * 中性色 4 边框、线色
         */
        var border = Color(0xFFDCDEF0)

        /**
         * 中性色 5 文字色，disable、提示文本等
         */
        var hint = Color(0xFFC8C9CC)

        /**
         * 中性色 6 文字色，辅助、说明文本
         */
        var tips = Color(0xFF969799)

        /**
         * 中性色 7 主要文本2
         */
        var body = Color(0xFF646566)

        /**
         * 中性色 8 主要文本1
         */
        var title = Color(0xFF323233)
    }
}

val defaultColorScheme
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.copy(
        primary = Color(0xFF1989FA),
        onPrimary = Color.White,
        primaryContainer = Color(0xFF9EC9FF),
        onPrimaryContainer = Color(0xFF003972),
        inversePrimary = Color(0xFFD0E5FF),
        secondary = Color(0xFF07C160),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFF7BFFA2),
        onSecondaryContainer = Color(0xFF005124),
        tertiary = Color(0xFFED6A0C),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFFFDCCF),
        onTertiaryContainer = Color(0xFF331200),
        background = Color(0xFFF7F8FA),
        onBackground = Color(0xFF323233),
        surface = Color.White,
        onSurface = Color(0xFF323233),
        surfaceVariant = Color(0xFFF2F3F5),
        onSurfaceVariant = Color(0xFF646566),
        surfaceTint = Color(0xFF1989FA),
        inverseSurface = Color(0xFF323233),
        inverseOnSurface = Color.White,
        error = Color(0xFFEE0A24),
        onError = Color.White,
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        outline = Color(0xFFDCDEF0),
        outlineVariant = Color(0xFFEBEDF0),
        scrim = Color(0xFF000000),
        surfaceBright = Color.White,
        surfaceDim = Color(0xFFDEDEDE),
        surfaceContainer = Color(0xFFF0F0F0),
        surfaceContainerHigh = Color(0xFFECECEC),
        surfaceContainerHighest = Color(0xFFE6E6E6),
        surfaceContainerLow = Color(0xFFF7F7F7),
        surfaceContainerLowest = Color.White
    )

fun Color.alpha75() = copy(alpha = 0.75f)
fun Color.alpha50() = copy(alpha = 0.5f)
fun Color.alpha25() = copy(alpha = 0.25f)

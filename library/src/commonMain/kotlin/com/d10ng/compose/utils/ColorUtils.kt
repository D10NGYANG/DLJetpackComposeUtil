package com.d10ng.compose.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.roundToInt

/**
 * 判断颜色是否为深色
 * @receiver Color
 * @return Boolean
 */
fun Color.isDark() = luminance() <= 0.5f

/**
 * 获取颜色的深色或浅色
 * @receiver Color
 * @param level Double -1.0～1.0，当为-1.0时，返回纯黑色，当为1.0时，返回纯白色
 * @return Color
 */
fun Color.next(level: Double): Color {
    // 参数合法性校验
    require(level in -1.0..1.0) { "Level must be in [-1.0, 1.0]" }
    val amount = level.toFloat()
    return when {
        amount < 0 -> blendWith(Color.Black, -amount)
        amount > 0 -> blendWith(Color.White, amount)
        else -> this
    }
}

private fun Color.blendWith(target: Color, ratio: Float): Color {
    val inverseRatio = 1f - ratio
    return Color(
        red = red * inverseRatio + target.red * ratio,
        green = green * inverseRatio + target.green * ratio,
        blue = blue * inverseRatio + target.blue * ratio,
        alpha = alpha // 保持原始透明度
    )
}

/**
 * 将颜色转换为十六进制字符串
 * @receiver [Color]
 * @param includeAlpha [Boolean] 是否包含透明度，默认包含
 * @return [String] 十六进制字符串，如果包含透明度，则为 #RRGGBBAA，否则为 #RRGGBB
 */
fun Color.toHex(includeAlpha: Boolean = true): String {
    fun toHexComponent(value: Float) = value.times(255).roundToInt()
        .toString(16)
        .padStart(2, '0') // 确保始终是 2 位
        .uppercase()

    val r = toHexComponent(red)
    val g = toHexComponent(green)
    val b = toHexComponent(blue)
    val a = toHexComponent(alpha)

    return if (includeAlpha) "#$a$r$g$b" else "#$r$g$b"
}

/**
 * 获取颜色的相关颜色
 * @receiver [Color]
 * @return [List] 0: onColor; 1: colorContainer; 2: onColorContainer; 3: inverseColor;
 */
fun Color.makeRelatedColors(): List<Color> {
    val dark = isDark()
    return listOf(
        if (dark) Color.White else Color.Black,
        next(if (dark) 0.5 else -0.5),
        next(if (dark) -0.5 else 0.5),
        next(if (dark) 0.7 else -0.7)
    )
}

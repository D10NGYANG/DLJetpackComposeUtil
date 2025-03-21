package com.d10ng.compose.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

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

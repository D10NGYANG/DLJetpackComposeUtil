package com.d10ng.compose.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.d10ng.common.calculate.getHexColorStringFromRgbValueArray
import com.d10ng.common.calculate.getMiddleColor
import com.d10ng.common.calculate.getRgbValueArrayFromHexColorString
import kotlin.math.abs
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
    val color = toHexString()
    val nextColor = getRgbValueArrayFromHexColorString(getNextLevelColor(color, level))
        .map { (it / 255.0).toFloat() }
    return Color(nextColor[0], nextColor[1], nextColor[2], alpha)
}

/**
 * 获取颜色的下一个等级颜色，或深色或浅色
 * @param color String #45216B
 * @param level Double -1.0～1.0，当为-1.0时，返回纯黑色，当为1.0时，返回纯白色
 * @return String #45216B
 */
internal fun getNextLevelColor(color: String, level: Double): String {
    val target = if (level > 0) "#FFFFFF" else "#000000"
    return getMiddleColor(color, target, abs(level).toFloat())
}

/**
 * 将颜色转换为16进制字符串
 * @receiver Color
 * @return String
 */
fun Color.toHexString() = getHexColorStringFromRgbValueArray(
    arrayOf(red, green, blue).map { (it * 255).roundToInt() }.toTypedArray()
)

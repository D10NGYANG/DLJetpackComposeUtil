package com.d10ng.compose.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import java.util.Locale
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
    val color = toColorHex(listOf(red, green, blue).map { (it * 255).roundToInt() })
    val nextColor = toRGB(getNextLevelColor(color, level)).map { (it / 255.0).toFloat() }
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
    return getMiddleColor(color, target, abs(level))
}

/**
 * 从两个颜色间根据比例获得中间颜色
 * @param startColor String #45216B
 * @param endColor String #45216B
 * @param present Double 0.5
 * @return String #45216B
 */
private fun getMiddleColor(startColor: String, endColor: String, present: Double): String {
    val startColorNumbers = toRGB(startColor)
    val endColorNumbers = toRGB(endColor)
    val redRange = endColorNumbers[0] - startColorNumbers[0]
    val greenRange = endColorNumbers[1] - startColorNumbers[1]
    val blueRange = endColorNumbers[2] - startColorNumbers[2]
    val red = (redRange * present).roundToInt() + startColorNumbers[0]
    val green = (greenRange * present).roundToInt() + startColorNumbers[1]
    val blue = (blueRange * present).roundToInt() + startColorNumbers[2]
    return toColorHex(listOf(red, green, blue))
}

/**
 * 从16进制颜色值中提取红绿蓝的值
 * @param str String #45216B
 * @return List<Int> [69,33,107]
 */
private fun toRGB(str: String): List<Int> {
    val reg = "^#([0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})$".toRegex()
    if (!reg.matches(str)) {
        return listOf(0, 0, 0)
    }
    var hexColor = str.lowercase(Locale.ROOT)
    val len = hexColor.length
    if (len == 4) {
        var t = "#"
        for (i in 1 until len) {
            t += hexColor.slice(i..i) + hexColor.slice(i..i)
        }
        hexColor = t
    }
    val arr = mutableListOf<Int>()
    for (i in 1 until hexColor.length step 2) {
        val s = hexColor.slice(i..i + 1)
        arr.add(s.toInt(16))
    }
    return arr
}

/**
 * 将红绿蓝的值转换成16进制颜色值
 * @param array List<Int> [69,33,107]
 * @return String #45216B
 */
private fun toColorHex(array: List<Int>): String {
    var redColorStr = array[0].toString(16)
    if (redColorStr.length == 1) {
        redColorStr = "0$redColorStr"
    }
    var greenColorStr = array[1].toString(16)
    if (greenColorStr.length == 1) {
        greenColorStr = "0$greenColorStr"
    }
    var blueColorStr = array[2].toString(16)
    if (blueColorStr.length == 1) {
        blueColorStr = "0$blueColorStr"
    }
    return "#$redColorStr$greenColorStr$blueColorStr"
}

package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppText

/**
 * 时间选择器
 * @Author d10ng
 * @Date 2023/9/11 19:11
 */

/**
 * 时间选择器模式
 * 控制 [TimePicker] 显示的列组合，时间值均以秒为单位（0-86399）
 */
enum class TimePickerMode(
    /**
     * 获取可选项列表，参数依次为：小时集合、分钟集合、秒钟集合
     * @return List<Set<String>> 可选项列表
     */
    val getItems: (Set<String>, Set<String>, Set<String>) -> List<Set<String>>,

    /**
     * 获取选中项，参数依次为：选中小时、选中分钟、选中秒钟
     * @return List<String> 选中项
     */
    val getSelectedItems: (String, String, String) -> List<String>,

    /**
     * 获取时间对应的总秒数，参数为时间字符串列表
     * @return Int 总秒数
     */
    val getDate: (List<String>) -> Int,

    /**
     * 获取可选时间范围内的时间列表，参数为总秒数
     * @return List<Int> 时间列表
     */
    val getDateList: (Int) -> List<Int>,
) {
    // 时分秒（三列）
    HMS(
        { h, m, s -> listOf(h, m, s) },
        { h, m, s -> listOf(h, m, s) },
        { l -> l[0].toInt() * 3600 + l[1].toInt() * 60 + l[2].toInt() },
        { v -> listOf(v.hour(), v.minute(), v.second()) }
    ),

    // 时分（两列）
    HM(
        { h, m, _ -> listOf(h, m) },
        { h, m, _ -> listOf(h, m) },
        { l -> l[0].toInt() * 3600 + l[1].toInt() * 60 },
        { v -> listOf(v.hour(), v.minute()) }
    ),
}

/**
 * 时间选择器
 * 基于 [MultiPicker] 实现的时间滚轮选择组件，根据 [mode] 显示时/分/秒列
 * 各列之间自动联动（如小时变化时重新计算可选分钟范围），选中结果裁剪至 [[start], [endInclude]] 范围
 * @param value Int 当前选中时间，单位秒（0-86399），计算方式：小时 × 3600 + 分钟 × 60 + 秒，默认 0
 * @param onValueChange (Int, List<Int>) -> Unit 选中时间变更回调；第一个参数为总秒数，第二个参数为各列数值列表（HMS 模式为 [时, 分, 秒]，HM 模式为 [时, 分]）
 * @param start Int 可选范围的开始时间，单位秒（0-86399），默认 0（即 00:00:00）
 * @param endInclude Int 可选范围的结束时间（包含），单位秒（0-86399），默认 86399（即 23:59:59）
 * @param textStyle TextStyle 选项文字样式，默认 `AppText.Normal.Title.default`
 * @param mode TimePickerMode 时间列组合模式，默认 [TimePickerMode.HMS]（时分秒三列）
 * @param itemText (Int, String) -> String 自定义选项文字格式化函数，第一个参数为列索引（0 起），第二个参数为原始文字（如 "08"），默认原样返回
 */
@Composable
fun TimePicker(
    value: Int = 0,
    onValueChange: (Int, List<Int>) -> Unit,
    start: Int = 0,
    endInclude: Int = 86399,
    textStyle: TextStyle = AppText.Normal.Title.default,
    mode: TimePickerMode = TimePickerMode.HMS,
    itemText: (Int, String) -> String = { _, item -> item },
) {
    // 小时列表
    val hours = remember(start, endInclude) {
        (start.hour()..endInclude.hour()).map {
            it.toString().padStart(2, '0')
        }.toSet()
    }
    // 选择小时
    val hour = remember(value) {
        value.hour()
    }
    // 分钟列表
    val minutes = remember(hour, start, endInclude) {
        val startMinute = if (hour == start.hour()) start.minute() else 0
        val endMinute = if (hour == endInclude.hour()) endInclude.minute() else 59
        (startMinute..endMinute).map {
            it.toString().padStart(2, '0')
        }.toSet()
    }
    // 选择分钟
    val minute = remember(value) {
        value.minute()
    }
    // 秒列表
    val seconds = remember(hour, minute, start, endInclude) {
        val startSecond =
            if (hour == start.hour() && minute == start.minute()) start.second() else 0
        val endSecond =
            if (hour == endInclude.hour() && minute == endInclude.minute()) endInclude.second() else 59
        (startSecond..endSecond).map {
            it.toString().padStart(2, '0')
        }.toSet()
    }
    // 选择秒
    val second = remember(value) {
        value.second()
    }

    val items = remember(mode, hours, minutes, seconds) {
        mode.getItems(hours, minutes, seconds)
    }
    val selectedItems = remember(mode, hour, minute, second) {
        mode.getSelectedItems(
            hour.toString().padStart(2, '0'),
            minute.toString().padStart(2, '0'),
            second.toString().padStart(2, '0')
        )
    }

    MultiPicker(
        items = items,
        itemText = itemText,
        textStyle = textStyle,
        selectedItems = selectedItems,
        onValueChange = {
            val time = mode.getDate(it).coerceAtLeast(start).coerceAtMost(endInclude)
            onValueChange(time, mode.getDateList(time))
        }
    )
}

internal fun Int.hour(): Int = this / 3600
internal fun Int.minute(): Int = this / 60 % 60
internal fun Int.second(): Int = this % 60

@Preview
@Composable
fun PreviewTimePicker() {
    // 08:30:45 = 8*3600 + 30*60 + 45 = 30645 秒
    Box(modifier = Modifier.background(Color.White)) {
        TimePicker(
            value = 30645,
            onValueChange = { _, _ -> },
            start = 0,
            endInclude = 86399,
            mode = TimePickerMode.HMS
        )
    }
}

@Preview
@Composable
fun PreviewTimePickerHM() {
    // 14:30 = 14*3600 + 30*60 = 52200 秒
    Column(modifier = Modifier.background(Color.White)) {
        TimePicker(
            value = 52200,
            onValueChange = { _, _ -> },
            start = 28800,   // 08:00 开始
            endInclude = 72000, // 20:00 结束
            mode = TimePickerMode.HM
        )
    }
}

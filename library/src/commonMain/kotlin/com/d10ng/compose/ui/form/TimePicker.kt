package com.d10ng.compose.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText

/**
 * 时间选择器
 * @Author d10ng
 * @Date 2023/9/11 19:11
 */

enum class TimePickerMode(
    val getItems: (Set<String>, Set<String>, Set<String>) -> List<Set<String>>,
    val getSelectedItems: (String, String, String) -> List<String>,
    val getDate: (List<String>) -> Int,
    val getDateList: (Int) -> List<Int>,
) {
    // 时分秒
    HMS(
        { h, m, s -> listOf(h, m, s) },
        { h, m, s -> listOf(h, m, s) },
        { l -> l[0].toInt() * 3600 + l[1].toInt() * 60 + l[2].toInt() },
        { v -> listOf(v.hour(), v.minute(), v.second()) }
    ),

    // 时分
    HM(
        { h, m, _ -> listOf(h, m) },
        { h, m, _ -> listOf(h, m) },
        { l -> l[0].toInt() * 3600 + l[1].toInt() * 60 },
        { v -> listOf(v.hour(), v.minute()) }
    ),
}

/**
 * 时间选择器
 * @param value Int 选中的时间，单位秒，0-86399，小时*3600+分钟*60+秒
 * @param onValueChange Function2<Int, List<Int>, Unit> 选中时间改变事件，单位秒，0-86399，小时*3600+分钟*60+秒，Int为选中的时间，List为选中的时间列表
 * @param start Int 开始时间，单位秒，0-86399，小时*3600+分钟*60+秒
 * @param endInclude Int 结束时间，包含，单位秒，0-86399，小时*3600+分钟*60+秒
 * @param textStyle TextStyle 文本样式
 * @param mode TimePickerMode 选择器模式
 * @param itemText Function2<Int, String, String> 选项文本
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